import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UsuarioService } from './../../service/usuario.service';
import { Usuario } from './../../model/usuario';
import { BaseComponent } from '../../base.component';

@Component({
  selector: 'app-detalle-usuario',
  standalone: true,
  imports: [CommonModule, TranslateModule],
  templateUrl: './detalles-usuario.component.html',
  styleUrls: ['./detalles-usuario.component.css']
})
export class DetalleUsuarioComponent extends BaseComponent implements OnInit {
  usuario: Usuario | null = null;
  columnas: any[] = [];
  mostrarPassword: boolean = false;
  usuarios: Usuario[] = [];

  constructor(
    private readonly usuarioService: UsuarioService,
    private readonly route: ActivatedRoute,
    translate: TranslateService,
    private readonly router: Router
  ) {
    super(translate);
  }

  ngOnInit(): void {
    this.traducirColumnas();
    const nickname = this.route.snapshot.paramMap.get('nickname');
    if (nickname) {
      this.usuarioService.buscarUsuarioPorNickname(nickname).subscribe(
        (response: Usuario) => {
          this.usuario = response;
        });
    }
  }

  actualizarUsuario(id: number) {
    this.router.navigate(['/actualizar-usuario/id', id]);
  }

  eliminarUsuario(id: number): void {
    // Crea un arreglo temporal que contenga solo el usuario a eliminar.
    const entidadSimulada = this.usuario ? [this.usuario] : [];

    // Llama a eliminarEntidad pasando el arreglo simulado y los parámetros necesarios.
    this.eliminarEntidad(
      id,
      entidadSimulada,
      (id) => this.usuarioService.eliminarUsuario(id), // Método del servicio
      'swalEraseUser',           // Clave del texto del SweetAlert de confirmación
      'swalSure',                // Clave del título del SweetAlert de confirmación
      'swalEraseUserConfirmedMessage' // Clave del mensaje de éxito de eliminación
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

  backToCategory() {
    this.router.navigate(['/usuarios']);
  }

  ocultarPassword(contrasena: string | undefined): string {
    return !this.mostrarPassword ? '●'.repeat(7) : (this.usuario ? this.usuario.contrasena : '');
  }

  toggleMostrarPassword() {
    this.mostrarPassword = !this.mostrarPassword;
  }

  override lanzarSweetAlertSimplificado(tituloKey: string, textoKey: string, parametros: any = {}, icono: any = 'success', botonTextKey: string = 'swalAccept'): Promise<any> {
    return super.lanzarSweetAlertSimplificado(tituloKey, textoKey, parametros, icono, botonTextKey);
  }
  protected override obtenerDatos(): void {
      console.log ("empty");
  }

  protected override navigateAfterDelete(): void {
    // Aquí defines cómo navegar después de eliminar el usuario
    this.router.navigate(['/usuarios']);
  }
}




