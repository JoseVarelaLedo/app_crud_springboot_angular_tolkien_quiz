import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { FichaUsuario } from './../model/ficha-usuario';

@Injectable({
  providedIn: 'root'
})
export class FichaUsuarioService {

  private readonly baseURL = "http://localhost:9666/fichasUsuario";
  private readonly authHeaders: HttpHeaders;


  constructor(private readonly httpClient: HttpClient) {
    const token = localStorage.getItem('token');
  this.authHeaders = token
    ? new HttpHeaders().set('Authorization', `Bearer ${token}`)
    : new HttpHeaders();
    // this.authHeaders = token ? new HttpHeaders().set('Authorization', `Bearer ${token}`) : new HttpHeaders(({ 'Content-Type': 'application/json' }));
    // this.authHeaders = new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  obtenerListaDeFichasDeUsuario(
    page: number,
    size: number,
    sortField: string,
    sortDirection: string,
    filtro: string
  ): Observable<FichaUsuario[]> {
    let params = new HttpParams()
      .set('pag', page.toString())
      .set('tam', size.toString())
      .set('campoOrdenacion', sortField)
      .set('direccionOrdenacion', sortDirection);

    if (filtro) {                                     // así agregamos el filtro a los parámetros si está presente
      params = params.set('filtro', filtro);
    }

    return this.httpClient.get<FichaUsuario[]>(`${this.baseURL}/lista`, { params, headers: this.authHeaders });
  }

  obtenerFichaDeUsuarioPorId(id: number): Observable<FichaUsuario> {
    return this.httpClient.get<FichaUsuario>(`${this.baseURL}/buscarUsuarioPorId/id/${id}`, { headers: this.authHeaders });
  }

  registrarFichaDeUsuario(fichaUsuario: FichaUsuario): Observable<FichaUsuario> {
    return this.httpClient.post<FichaUsuario>(`${this.baseURL}/crearFichaDeUsuario`, fichaUsuario, { headers: this.authHeaders });
  }

  eliminarFichaDeUsuario(id: number): Observable<FichaUsuario> {
    return this.httpClient.delete<FichaUsuario>(`${this.baseURL}/eliminar/id/${id}`, { headers: this.authHeaders });
  }

  actualizarFichaDeUsuario(id: number, fichaUsuario: FichaUsuario): Observable<FichaUsuario> {
    return this.httpClient.put<FichaUsuario>(`${this.baseURL}/editar/id/${id}`, fichaUsuario, { headers: this.authHeaders });
  }
}
