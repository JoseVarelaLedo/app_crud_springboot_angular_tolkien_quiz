import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Usuario } from '../../model/usuario';
import { UsuarioService } from '../../service/usuario.service';
import { RolServiceService } from '../../service/rol.service.service';
import { Rol } from '../../model/rol';
import { BaseComponent } from '../../base.component';

@Component({
  selector: 'app-registrar-usuario',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, TranslateModule],
  templateUrl: './nuevo-usuario.component.html',
  styleUrls: ['./nuevo-usuario.component.css']
})

export class NuevoUsuarioComponent extends BaseComponent {

  usuario: Usuario = new Usuario();
  roles: Rol[] = [];
  rolUsuario = '';
  rolUsuarioLogueado : boolean;


  constructor(private readonly usuarioService: UsuarioService,
    private readonly rolService: RolServiceService,
    private readonly router: Router,
    translate: TranslateService) {
      super(translate);
      this.rolUsuarioLogueado = localStorage.getItem ('role') === 'ROLE_Administrador';
    }

  ngOnInit() {
    this.getRoles();
  }

  guardarUsuario() {
    this.usuarioService.registrarNuevoUsuario(this.usuario).subscribe(
      {
        next: () => { this.navigateListaUsuarios(); },
        error: () => {
          this.lanzarSweetAlert({
            tituloKey: 'swalError',
            textoKey: 'swalNotPermitted',
            icono: 'error',
            botonTextKey: 'swalAccept',
            cancelar: false,
          })
          .then(() => {
            console.log("error");
          });
        }
      });
  }

  navigateListaUsuarios() {
    this.lanzarSweetAlertSimplificado(
      'swalUserCreated',
      'swalUserCreatedMessage',
      { nickname: this.usuario.nickname },
      'success',
      'swalAccept'
    )
    .then(() => {
      this.router.navigate(['/usuarios']);
    });
  }

  verificarNicknameYGuardar() {
    this.usuarioService.verificarNickname(this.usuario.nickname).subscribe(
      {
        next:   exists => {
          if (exists) {

            this.lanzarSweetAlertSimplificado(
              'swalNicknameExists',
              'swalNicknameExistsMessage',
              {},
              'error',
              'swalAccept'
            )
            .then((error) => {
              console.log (error);
            });
          } else {
            this.guardarUsuario();      // en caso de no existir ese nickname, guarda el usuario
          }
        },

        error:  () => {
          this.lanzarSweetAlertSimplificado(
            'swalNickNameQueryError',
            'swalNickNameQueryErrorMessage',
            {},
            'error',
            'swalAccept'
          )
          .then((error) => {
            console.log (error);
          });
        }
      });
  }

  setRolUsuario(nombreRol: string) {
    this.usuario.rolID = (this.roles.map(rol => rol.nombreRol)).indexOf(nombreRol) + 1;
  }

  getRoles() {
    this.rolService.obtenerRoles().subscribe(
      {
        next: (response: Rol[]) => {
          this.roles = response;
        },
        error: (error) => {
          console.log(error);
        }
      }
    )
  }

  onSubmit() {
    this.verificarNicknameYGuardar();
  }

  backToCategory() {
    this.router.navigate(['/usuarios']);
  }

  protected override obtenerDatos() {
    console.log('empty');
  }

  protected override navigateAfterDelete(): void {
      console.log ("empty");
  }
}
