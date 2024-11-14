import { CommonModule } from '@angular/common';
import { Component } from '@angular/core';
import { AuthService } from '../../service/auth.service';
import { FormsModule } from '@angular/forms';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Router } from '@angular/router';
import { BaseComponent } from '../../base.component';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [FormsModule, CommonModule, TranslateModule],
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent extends BaseComponent{
  username: string = '';
  password: string = '';
  errorMessage: string | null = null;

  constructor(
    private readonly authService: AuthService,
    translate: TranslateService,
    private readonly router:Router) {
      super (translate);
    }

  login() {
    this.authService.login(this.username, this.password).subscribe({
      next: (response) => {
        // Manejar la respuesta (por ejemplo, guardar el token en el almacenamiento local)
        console.log('Token recibido del backend:', response.token);
        if (response.token) {
          localStorage.setItem('token', response.token);  // Aquí guardas el token
          console.log('Token guardado:', response.token);
        }
        localStorage.setItem('role', response.roles[0]);
        localStorage.setItem('username', this.username);
        this.router.navigate(['/home']);

        console.log('Login exitoso', response);
        this.lanzarSweetAlertSimplificado(
          'swalSuccess',
          'swalLoginSuccess',
          {},
          'success',
          'swalAccept'
        ).then(() => {
          this.router.navigate(['/home']).then(() => window.location.reload()); // redirigir y recargar solo después de que se confirme la alerta
        });
      },
      error: () => {
        this.lanzarSweetAlertSimplificado(
          'swalError',
          'loginError',
          {},
          'error',
          'swalAccept'
        );
      }
    });
  }

  goToRegister() {
    console.log ('yendo a registro');
    this.router.navigate(['/nueva-ficha-usuario']);
    console.log ('salimos de login component');
  }

  protected override obtenerDatos() {
    console.log('empty');
  }

  protected override navigateAfterDelete(): void {
      console.log ("empty");
  }

}
