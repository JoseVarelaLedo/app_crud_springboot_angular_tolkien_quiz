import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { FormsModule } from '@angular/forms';
import { Subscription } from 'rxjs';
import { UsuarioService } from './../../service/usuario.service';
import { Usuario } from './../../model/usuario';
import { NgxPaginationModule } from "ngx-pagination";
import { BaseComponent } from '../../base.component';

@Component({
  selector: 'app-lista-usuarios',
  standalone: true,
  imports: [CommonModule, TranslateModule, FormsModule, NgxPaginationModule],
  templateUrl: './lista-usuarios.component.html',
  styleUrls: ['./lista-usuarios.component.css']
})

export class ListaUsuariosComponent extends BaseComponent implements OnInit, OnDestroy {
  usuarios: Usuario[] = [];
  totalElements = 0;
  rolSeleccionado: number | null = null;
  columnas: any[] = [];
  langChangeSubscription: Subscription = new Subscription();
  searchNickname: string = '';
  fichaUsuarioId = 0;
  searchResults: Usuario[] = [];
  fechaFiltro: string = '';
  mostrarFiltro = false;

  constructor(
    private readonly usuarioService: UsuarioService,
    private readonly router: Router,
    translate: TranslateService,
  ) {
    super(translate);
  }

  ngOnInit() {
    this.langChangeSubscription = this.translate.onLangChange.subscribe(() => {    // suscribirse a los cambios de idioma
      this.traducirColumnas();
    });
    this.traducirColumnas();
    this.obtenerDatos();
  }

  ngOnDestroy() {                                     // desuscribirse de los cambios de idioma cuando el componente se destruye
    if (this.langChangeSubscription) {
      this.langChangeSubscription.unsubscribe();
    }
  }
  protected obtenerDatos() {
    this.usuarioService.obtenerListaDeUsuarios(this.page -1, this.size, this.sortField, this.sortDirection).subscribe(
      {
        next: (response: any) => {
          this.usuarios = response.content;
          this.totalPages = response.totalPages;
          this.totalItems = response.totalElements;
        },
        error: (error) => {
          console.error('Error al obtener la lista de fichas de Usuario', error);
        }
      }
    );
  }

  actualizarUsuario(id: number) {
    this.router.navigate(['/actualizar-usuario/id', id]);
    this.obtenerDatos();
  }

  filtrarUsuarios(rolId: number | null): void {                             // método para alternar los roles seleccionados
    this.rolSeleccionado = rolId;                                           // actualizar el rol seleccionado
    if (rolId === null) {
      this.obtenerDatos();                                                  // si no hay rol seleccionado, muestra todos los usuarios
    } else {                                                                // filtrar por el rol seleccionado
      this.usuarioService.obtenerUsuariosPorRol(this.page, this.size, this.sortField, this.sortDirection, rolId).subscribe((response: any) => {
        this.usuarios = response.content;
        this.totalPages = response.totalPages;
      });
    }
    this.mostrarFiltro = false;
  }

  buscarUsuariosPorFechaPost() {
    if (this.fechaFiltro) {
      this.usuarioService.buscarUsuariosPorFecha(this.fechaFiltro, this.page, this.size, this.sortField, this.sortDirection).subscribe(
        {
          next: response => {
            this.usuarios = response.content;
            this.totalPages = response.totalPages;
          },
          error: () => {
            super.lanzarSweetAlert({
              tituloKey: 'swalError',
              textoKey: 'swalErrorMessage',
              icono: 'error',
              botonTextKey: 'swalAccept',
              cancelar: false,
            });
          }
        }
      );
    } else {
      super.lanzarSweetAlert({
        tituloKey: 'swalError',
        textoKey: 'swalErrorMessage',
        icono: 'error',
        botonTextKey: 'swalAccept',
        cancelar: false,
      });
    }
    this.mostrarFiltro = false;
  }

  buscarUsuarioPorNickname() {
    this.usuarioService.buscarUsuarioPorNickname(this.searchNickname).subscribe(
      {
        next: (response: Usuario) => {
          this.router.navigate(['/usuario', response.nickname]); // redirigir al detalle del usuario
        },
        error:
          () => {
            super.lanzarSweetAlert({
              tituloKey: 'swalError',
              textoKey: 'swalNotFoundMessage',
              icono: 'error',
              botonTextKey: 'swalAccept',
              cancelar: false,
            });
            this.searchResults = []; // vaciar los resultados en caso de error
          }
      }
    );
  }

  eliminarUsuario(id: number): void {
    this.eliminarEntidad(
      id,
      this.usuarios,
      (id) => this.usuarioService.eliminarUsuario(id),
      'swalEraseUser',
      'swalSure',
      'swalEraseUserConfirmedMessage'
    );
  }

  private traducirColumnas() {
    this.columnas = [
      { titulo: this.translate.instant('id'), campo: 'id' },
      { titulo: this.translate.instant('userNickname'), campo: 'nickname' },
      { titulo: this.translate.instant('userPassword'), campo: 'contrasena' },
      { titulo: this.translate.instant('roleName'), campo: 'nombreRol' },
      { titulo: this.translate.instant('fileAsociated'), campo: 'fichaUsuarioId' },
      { titulo: this.translate.instant('registerDate'), campo: 'fechaRegistro' }
    ];
  }

  ocultarPassword(password: string): string {
    return '●'.repeat(7); // muestra 7 puntos para ocultar contraseña
  }

  override ordenarPor(campo: string) {
    super.ordenarPor(campo);
    this.obtenerDatos();
  }
  protected override navigateAfterDelete(): void {
    this.router.navigate(['/usuarios']);
  }

  cambiarPagina(pagina: number) {
    this.page = pagina;
    this.buscarUsuariosPorFechaPost();
  }
}
