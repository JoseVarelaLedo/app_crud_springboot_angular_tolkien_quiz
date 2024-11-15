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
        if (response.token) {
          localStorage.setItem('token', response.token);  // almacenamiento del token en el localStorage
          console.log('Token guardado:', response.token);
        }
        localStorage.setItem('role', response.roles[0]);  // ídem roles
        localStorage.setItem('username', this.username);  // ídem username / nickname
        this.router.navigate(['/home']);

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
    this.router.navigate(['/nueva-ficha-usuario']);
  }

  protected override obtenerDatos() {
    console.log('empty');
  }

  protected override navigateAfterDelete(): void {
      console.log ("empty");
  }
}
