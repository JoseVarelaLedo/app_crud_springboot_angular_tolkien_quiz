import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Pregunta } from '../model/pregunta';

@Injectable({
  providedIn: 'root'
})
export class PreguntaService {

  private readonly baseURL = "http://localhost:9666/preguntas";
  private readonly authHeaders: HttpHeaders;

  constructor(private readonly httpClient: HttpClient) {
    const token = localStorage.getItem('token');
    if (!token) {
      throw new Error('Token no disponible');
    }
    this.authHeaders = new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }


  obtenerListaDePreguntas(page: number, size: number, sortField: string, sortDirection: string): Observable<any> {
    let params = new HttpParams()
    .set('pag', page.toString())
    .set('tam', size.toString())
    .set('campoOrdenacion', sortField)
    .set('direccionOrdenacion', sortDirection);
    return this.httpClient.get<any>(`${this.baseURL}/lista`, { params, headers: this.authHeaders });
  }

  obtenerListaDePreguntasPorCategoria(page: number, size: number, sortField: string, sortDirection: string, categoriaId: number): Observable<any> {
    return this.httpClient.get<any>(`${this.baseURL}?pag=${page}&tam=${size}&campoOrdenacion=${sortField}&direccionOrdenacion=${sortDirection}&categoriaId=${categoriaId}`, { headers: this.authHeaders });
  }

  obtenerListaDePreguntasPorContenido(page: number, size: number, sortField: string, sortDirection: string, textoPregunta: string): Observable<any> {
    return this.httpClient.get<any>(`${this.baseURL}/contenido/${textoPregunta}?pag=${page}&tam=${size}&campoOrdenacion=${sortField}&direccionOrdenacion=${sortDirection}`, { headers: this.authHeaders });
  }

  obtenerPreguntaPorId (id:number) : Observable<Pregunta>{
    return this.httpClient.get<Pregunta> (`${this.baseURL}/buscarPreguntaPorId/id/${id}`, { headers: this.authHeaders }); //comprobar si hace falta /id/${id}
  }

  registrarNuevaPregunta (pregunta:Pregunta) : Observable<Pregunta>{
    return this.httpClient.post<Pregunta>(`${this.baseURL}/crearPregunta`, pregunta, { headers: this.authHeaders });
  }

  eliminarPregunta (id:number) : Observable<Pregunta>{
    return this.httpClient.delete <Pregunta>(`${this.baseURL}/eliminar/id/${id}`, { headers: this.authHeaders });
  }

  actualizarPregunta(id: number, pregunta: Pregunta): Observable<Pregunta> {
    return this.httpClient.put<Pregunta>(`${this.baseURL}/editar/id/${id}`, pregunta, { headers: this.authHeaders });
  }
}
