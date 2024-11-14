import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Categoria } from '../model/categoria';

@Injectable({
  providedIn: 'root'
})
export class CategoriaService {

  private readonly baseURL = "http://localhost:9666/categorias";
  private readonly authHeaders: HttpHeaders;

  constructor(private readonly httpClient: HttpClient) {
    const token = localStorage.getItem('token');
    if (!token) {
      throw new Error('Token no disponible');
    }
    this.authHeaders = new HttpHeaders().set('Authorization', `Bearer ${token}`);
  }

  obtenerCategorias(page: number, size: number, sortField: string, sortDirection: string): Observable<Categoria[]> {
    return this.httpClient.get<Categoria[]>(`${this.baseURL}?pag=${page}&tam=${size}&campoOrdenacion=${sortField}&direccionOrdenacion=${sortDirection}`, { headers: this.authHeaders });
  }
  obtenerCategoriasSinPaginar(){
    return this.httpClient.get<Categoria[]>(`${this.baseURL}/noPag`, { headers: this.authHeaders });
  }

  obtenerCategoriaPorId (id:number) : Observable<Categoria>{
    return this.httpClient.get<Categoria> (`${this.baseURL}/buscarCategoriaPorId/id/${id}`, { headers: this.authHeaders }); //comprobar si hace falta /id/${id}
  }

  registrarNuevaCategoria (categoria:Categoria) : Observable<Categoria>{
    return this.httpClient.post<Categoria>(`${this.baseURL}/crearCategoria`, categoria, { headers: this.authHeaders });
  }

  eliminarCategoria(id: number): Observable<Categoria> {
    return this.httpClient.delete<Categoria>(`${this.baseURL}/eliminar/id/${id}`, { headers: this.authHeaders });
  }

  actualizarCategoria(id: number, categoria: Categoria): Observable<Categoria> {
    return this.httpClient.put<Categoria>(`${this.baseURL}/editar/id/${id}`, categoria, { headers: this.authHeaders });
  }
}
