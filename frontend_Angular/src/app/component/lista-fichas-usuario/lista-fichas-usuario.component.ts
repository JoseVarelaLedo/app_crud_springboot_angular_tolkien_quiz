import { CommonModule, NgFor } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { FichaUsuario } from './../../model/ficha-usuario';
import { FichaUsuarioService } from '../../service/ficha-usuario.service';
import { FormsModule } from '@angular/forms';
import { NgxPaginationModule } from "ngx-pagination";
import { BaseComponent } from '../../base.component';
@Component({
  selector: 'app-lista-fichas-usuario',
  standalone: true,
  imports: [CommonModule, TranslateModule, NgFor, FormsModule, NgxPaginationModule],
  templateUrl: './lista-fichas-usuario.component.html',
  styleUrls: ['./lista-fichas-usuario.component.css']
})
export class ListaFichasUsuarioComponent extends BaseComponent implements OnInit, OnDestroy {
  fichasUsuario: FichaUsuario[] = [];
  totalElements = 0;
  columnas: any[] = [];
  langChangeSubscription: Subscription = new Subscription();
  filtro: string = '';                  // almacenar el texto del filtro
  fichasFiltradas: FichaUsuario[] = []; // almacenar los resultados filtrados


  constructor(
    private readonly fichaUsuarioService: FichaUsuarioService,
    private readonly router: Router,
    translate: TranslateService
  ) {
    super(translate);
  }

  ngOnInit() {
    this.langChangeSubscription = this.translate.onLangChange.subscribe(() => {   // suscribirse a los cambios de idioma
      this.traducirColumnas();
    });
    this.traducirColumnas();
    this.obtenerDatos();
  }

  ngOnDestroy() {                                // desuscribirse de los cambios de idioma cuando el componente se destruye
    if (this.langChangeSubscription) {
      this.langChangeSubscription.unsubscribe();
    }
  }

  obtenerFichas(){
    this.obtenerDatos();
  }

  protected obtenerDatos() {
    this.fichaUsuarioService.obtenerListaDeFichasDeUsuario(this.page-1, this.size, this.sortField, this.sortDirection, this.filtro).subscribe(
      {
        next:  (response: any) => {
          this.fichasUsuario = response.content;
          this.totalPages = response.totalPages;
          this.totalItems = response.totalElements;
          this.fichasFiltradas = this.fichasUsuario;
        },
        error:(error) => {
          this.lanzarSweetAlert({
            tituloKey: 'swalError',
            textoKey: 'swalNotPermitted',
            icono: 'error',
            botonTextKey: 'swalAccept',
            cancelar: false,
          });
          console.error('Error al obtener la lista de fichas de Usuario', error);
        }
      }
    );
  }

  actualizarFichaUsuario(id: number) {
    this.router.navigate(['actualizar-ficha-usuario/id', id]);
    this.obtenerDatos();                               // refrescar la lista de contactos después de eliminar
  }

  eliminarFichaUsuario(id: number) {
    this.eliminarEntidad(
      id,  // ID de la entidad a eliminar
      this.fichasUsuario,  // Array de entidades
      (id) => this.fichaUsuarioService.eliminarFichaDeUsuario(id),  // Método del servicio para eliminar
      'swalEraseUserFile',  // Clave de texto de la alerta de confirmación
      'swalSure',  // Clave de título de la alerta de confirmación
      'swalEraseUserFileConfirmedMessage'  // Clave de texto después de la eliminación confirmada
    );
  }

  private traducirColumnas() {
    this.columnas = [
      { titulo: this.translate.instant('id'), campo: 'id' },
      { titulo: this.translate.instant('name'), campo: 'nombre' },
      { titulo: this.translate.instant('surname'), campo: 'apellidos' },
      { titulo: this.translate.instant('phone'), campo: 'telefono' },
      { titulo: this.translate.instant('email'), campo: 'correoElectronico' },
      { titulo: this.translate.instant('address'), campo: 'direccion' },
      { titulo: this.translate.instant('userNickname'), campo: 'nickname' },
      { titulo: this.translate.instant('userPassword'), campo: 'password' },
      { titulo: this.translate.instant('score'), campo: 'score' },
      { titulo: this.translate.instant('birthDate'), campo: 'fechaNacimiento' },
      { titulo: this.translate.instant('registerDate'), campo: 'fechaRegistro' }
    ];
  }

  ocultarPassword(password: string): string {
    return '●'.repeat(7);                               // muestra 7 caracteres especiales
  }

  override nextPage() {
    super.nextPage();  // Usando el método heredado de BaseComponent
  }

  override prevPage() {
    super.prevPage();  // Usando el método heredado de BaseComponent
  }

  override ordenarPor(campo: string) {
    super.ordenarPor(campo);
    this.obtenerDatos();
  }

  protected override navigateAfterDelete(): void {
    this.router.navigate(['/fichas-usuario']);
  }
}
