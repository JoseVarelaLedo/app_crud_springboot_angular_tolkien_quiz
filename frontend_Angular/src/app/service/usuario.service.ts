import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams  } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Usuario } from './../model/usuario';

@Injectable({
  providedIn: 'root'
})
export class UsuarioService {

  private readonly baseURL = "http://localhost:9666/usuarios";
  private readonly authHeaders: HttpHeaders;

  constructor(private readonly httpClient: HttpClient) {
    const token = localStorage.getItem('token');
    if (!token) {
      throw new Error('Token no disponible');
    }
    this.authHeaders = new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  obtenerListaDeUsuarios(page: number, size: number, sortField: string, sortDirection: string): Observable<Usuario> {
    let params = new HttpParams()
    .set('pag', page.toString())
    .set('tam', size.toString())
    .set('campoOrdenacion', sortField)
    .set('direccionOrdenacion', sortDirection);
    return this.httpClient.get<Usuario>(`${this.baseURL}/lista`, { params, headers: this.authHeaders });
  }

  obtenerUsuarioPorId (id:number) : Observable<Usuario>{
    return this.httpClient.get<Usuario> (`${this.baseURL}/buscarUsuarioPorId/id/${id}`, { headers: this.authHeaders });
  }

  obtenerUsuariosPorRol(page: number, size: number, sortField: string, sortDirection: string, rolId: number): Observable<any> {
    return this.httpClient.get<any>(`${this.baseURL}/porRol?rolId=${rolId}`, { headers: this.authHeaders });
  }

  buscarUsuarioPorNickname(nickname: string) {
    return this.httpClient.get<Usuario>(`${this.baseURL}/buscarNickname?nickname=${nickname}`, { headers: this.authHeaders });
  }

  buscarUsuariosPorFecha(fecha: string, page: number, size: number, sortField: string, sortDirection: string): Observable<any> {
    const params = new HttpParams()
      .set('fecha', `${fecha}T00:00:00`)
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sortField', sortField)
      .set('sortDirection', sortDirection);

    return this.httpClient.get<any>(`${this.baseURL}/buscarPorFecha`, { params });
  }

  registrarNuevoUsuario (usuario:Usuario) : Observable<Usuario>{
    return this.httpClient.post<Usuario>(`${this.baseURL}/crearUsuario`, usuario, { headers: this.authHeaders });
  }

  eliminarUsuario (id:number) : Observable<Usuario>{
    return this.httpClient.delete <Usuario>(`${this.baseURL}/eliminar/id/${id}`, { headers: this.authHeaders });
  }

  actualizarUsuario(id: number, usuario: Usuario): Observable<Usuario> {
    return this.httpClient.put<Usuario>(`${this.baseURL}/editar/id/${id}`, usuario, { headers: this.authHeaders });
  }

  verificarNickname(nickname: string): Observable<boolean> {
    return this.httpClient.get<boolean>(`${this.baseURL}/verificarNickname?nickname=${nickname}`, { headers: this.authHeaders });
  }
}
