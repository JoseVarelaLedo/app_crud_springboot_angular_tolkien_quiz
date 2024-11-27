import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Rol } from '../model/rol'

@Injectable({
  providedIn: 'root'
})
export class RolServiceService {
  private readonly baseURL = "http://localhost:9666/roles";
  private readonly authHeaders: HttpHeaders;

  constructor(private readonly httpClient: HttpClient) {
    const token = localStorage.getItem('token');
    if (!token) {
      throw new Error('Token no disponible');
    }
    this.authHeaders = new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  obtenerRoles(): Observable<Rol[]> {
    return this.httpClient.get<Rol[]>(`${this.baseURL}/lista`, { headers: this.authHeaders });
  }
}
