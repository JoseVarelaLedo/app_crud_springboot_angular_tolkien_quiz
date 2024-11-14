import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError, of, tap } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Usuario } from '../../model/usuario';
import { UsuarioService } from '../../service/usuario.service';
import { RolServiceService } from '../../service/rol.service.service';
import { Rol } from '../../model/rol';
import { BaseComponent } from '../../base.component';
import { LocationUpgradeModule } from '@angular/common/upgrade';
@Component({
  selector: 'app-actualizar-usuario',
  standalone: true,
  imports: [FormsModule, CommonModule, TranslateModule],
  templateUrl: './actualizar-usuario.component.html',
  styleUrls: ['./actualizar-usuario.component.css']
})
export class ActualizarUsuarioComponent extends BaseComponent implements OnInit {
  id = 0;
  usuario = new Usuario();
  roles: Rol[] = [];
  rolUsuario = '';
  esAdministrador: boolean;


  constructor(
    private readonly usuarioService: UsuarioService,
    private readonly rolService: RolServiceService,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    translate: TranslateService // Inyecta TranslateService
  ) {
    super(translate);
    this.esAdministrador = localStorage.getItem('role') === 'ROLE_Administrador';
  }


  ngOnInit() {
    this.getRoles();
    this.id = this.route.snapshot.params['id'];

    this.usuarioService.obtenerUsuarioPorId(this.id).pipe(
      tap((datosActualizados: Usuario) => {
        this.usuario = datosActualizados;

      }),
      catchError(error => {
        console.error(error);
        return of(null); // Retorna un observable vacío en caso de error
      })
    ).subscribe();
  }

  navigateListaUsuarios() {
    const parametros = { nickname: this.usuario.nickname };
    this.lanzarSweetAlertSimplificado('swalUserUpdated', 'swalUserUpdatedSuccess', parametros)
      .then(() => {
        this.router.navigate(['/usuarios']);
      });
  }

  onSubmit(): void {
    if (this.usuario) {
      this.usuarioService.actualizarUsuario(this.id, this.usuario).pipe(
        tap(() => {
          this.navigateListaUsuarios();
        }),
        catchError(() => {
          this.lanzarSweetAlertSimplificado('swalError', 'swalErrorOccurredText', {}, 'error', 'swalRetry');
          return of(null);
        })
      ).subscribe();
    }
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
        error: () => {
          this.lanzarSweetAlertSimplificado('swalError', 'swalErrorOccurredText', {}, 'error', 'swalRetry');
          console.log('Error al obtener los roles');
        }
      }
    );
  }

  backToCategory() {
    this.router.navigate(['/usuarios']);
  }

  ocultarPassword(): string {
    return '●'.repeat(7); // 7 puntos para ocultar la contraseña
  }

  protected override obtenerDatos() {
    console.log('empty');
  }

  protected override navigateAfterDelete(): void {
    console.log("empty");
  }
}
