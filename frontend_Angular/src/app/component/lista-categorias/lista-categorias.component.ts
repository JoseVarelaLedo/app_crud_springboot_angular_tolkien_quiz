import { CommonModule } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { Categoria } from '../../model/categoria';
import { CategoriaService } from '../../service/categoria.service';
import { NgxPaginationModule } from "ngx-pagination";
import { BaseComponent } from '../../base.component';

@Component({
  selector: 'app-lista-categorias',
  standalone: true,
  imports: [CommonModule, TranslateModule, NgxPaginationModule],
  templateUrl: './lista-categorias.component.html',
  styleUrls: ['./lista-categorias.component.css']
})

export class ListaCategoriasComponent extends BaseComponent implements OnInit, OnDestroy{
  categorias: Categoria[] = [];
  totalElements = 0;
  columnas: any[] = [];
  langChangeSubscription: Subscription = new Subscription();

  constructor(
    private readonly categoriaService: CategoriaService,
    private readonly router: Router,
    translate: TranslateService,
  ) {
    super(translate);
  }

  ngOnInit() {
    this.langChangeSubscription = this.translate.onLangChange.subscribe(() => { // suscribirse a los cambios de idioma
      this.traducirColumnas();
    });
    this.traducirColumnas();
    this.obtenerDatos();
  }

  ngOnDestroy() {
    if (this.langChangeSubscription) {  // desuscribirse de los cambios de idioma cuando el componente se destruye
      this.langChangeSubscription.unsubscribe();
    }
  }
  protected obtenerDatos() {
    this.categoriaService.obtenerCategorias(this.page-1, this.size, this.sortField, this.sortDirection).subscribe(
      {
        next: (response: any) => {
          this.categorias = response.content;
          this.totalPages = response.totalPages;
          this.totalItems = response.totalElements;
        },
        error: (error) => {
          this.lanzarSweetAlert({
            tituloKey: 'swalError',
            textoKey: 'swalNotFoundMessage',
            icono: 'error',
            botonTextKey: 'swalAccept',
            cancelar: false,
          });
          console.error('Error al obtener la categoría', error);
        }
      }
    );
  }

  actualizarCategoria(id: number) {
    this.router.navigate(['/actualizar-categoria/id', id]);
    this.obtenerDatos(); // refrescar la lista de contactos después de eliminar
  }

  eliminarCategoria(id: number): void {
    this.eliminarEntidad(
      id,
      this.categorias,
      (id) => this.categoriaService.eliminarCategoria(id),
      'swalEraseCategory',
      'swalSure',
      'swalEraseCategoryConfirmedMessage'
    );
  }

  private traducirColumnas() {
    this.columnas = [
      { titulo: this.translate.instant('id'), campo: 'id' },
      { titulo: this.translate.instant('categoryDescription'), campo: 'categoriaDesc' },
    ];
  }

  override ordenarPor(campo: string) {
    super.ordenarPor(campo);
    this.obtenerDatos();
  }

  protected override navigateAfterDelete(): void {
    this.router.navigate(['/categorias']);
  }
}
