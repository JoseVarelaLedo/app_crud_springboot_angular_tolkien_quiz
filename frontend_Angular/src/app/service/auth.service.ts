import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, BehaviorSubject, tap } from 'rxjs';
import { jwtDecode } from "jwt-decode";
import { Router } from '@angular/router';
@Injectable({
  providedIn: 'root'
})

export class AuthService {

  private readonly baseUrl = 'http://localhost:9666/auth/login';
  private readonly loggedInSubject = new BehaviorSubject<boolean>(!!localStorage.getItem('token'));

  loggedIn$ = this.loggedInSubject.asObservable();

  username: string | null = null;
  roles: string[] = [];

  constructor(private readonly httpClient: HttpClient, private readonly router: Router) { }

  login(username: string, password: string): Observable<any> {
    const headers = new HttpHeaders({ 'Content-Type': 'application/json' });
    const body = JSON.stringify({ username, password: password });

    return this.httpClient.post<LoginResponse>(this.baseUrl, body, { headers }).pipe(
      tap(response => {

        if (response?.token) {
          localStorage.setItem('token', response.token);  // almacenamiento token en el localstorage

          const decodedToken: any = jwtDecode(response.token); //decodificar el token usando la librería jwtdecode

          if (decodedToken && decodedToken.roles.length > 0 && decodedToken.sub) {   // guardar el rol y username si existen en el localstorage
            localStorage.setItem('role', decodedToken.roles[0]);
          } else {
            console.warn('El token no contiene roles o sub');
          }
          localStorage.setItem('username', decodedToken.sub);

          this.loggedInSubject.next(true);                        // emitir estado de login
        } else {
          console.error('Respuesta no contiene jwt:', response);
        }
      })
    );
  }

  logout(): void {
    const token = localStorage.getItem('token');
    if (token) {
      const headers = new HttpHeaders({
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}` // envía el token al backend
      });

      this.httpClient.post('http://localhost:9666/auth/logout', {}, { headers })
        .subscribe({
          next: () => {
            localStorage.removeItem('token');    // una vez que el backend confirma la invalidación, borra los datos en localStorage
            localStorage.removeItem('role');
            localStorage.removeItem('username');
            this.loggedInSubject.next(false);
            this.router.navigate(['/login']);
          },
          error: (err) => {
            console.error('Error en la invalidación del token:', err);
            localStorage.removeItem('token');    // si ocurre un error en la invalidación, puedes seguir eliminando los datos locales
            localStorage.removeItem('role');
            localStorage.removeItem('username');
            this.loggedInSubject.next(false);
            this.router.navigate(['/login']);
          }
        });
    } else {
      localStorage.removeItem('role');          // si no hay token, realiza el logout directamente
      localStorage.removeItem('username');
      this.loggedInSubject.next(false);
      this.router.navigate(['/login']);
    }
    this.router.navigate(['/login']).then(() => window.location.reload()); // redirigir y recargar solo después de que se confirme la alerta
  }


}
export interface LoginResponse {
  token: string; // la propiedad token tiene que coincidir con el nombre que llega del backend!!! OJO!
}
