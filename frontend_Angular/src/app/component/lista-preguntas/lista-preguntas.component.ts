import { CommonModule, NgFor } from '@angular/common';
import { Component, OnInit, OnDestroy } from '@angular/core';
import { Router } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Subscription } from 'rxjs';
import { Pregunta } from '../../model/pregunta';
import { PreguntaService } from '../../service/pregunta.service';
import { FormsModule } from '@angular/forms';
import { Categoria } from '../../model/categoria';
import { CategoriaService } from '../../service/categoria.service';
import { NgxPaginationModule } from "ngx-pagination";
import { BaseComponent } from '../../base.component';

@Component({
  selector: 'app-lista-preguntas',
  standalone: true,
  imports: [CommonModule, TranslateModule, NgFor, FormsModule, NgxPaginationModule],
  templateUrl: './lista-preguntas.component.html',
  styleUrls: ['./lista-preguntas.component.css']
})

export class ListaPreguntasComponent extends BaseComponent implements OnInit, OnDestroy {

  preguntas: Pregunta[] = [];
  preguntasFiltradas: Pregunta[] = []; // necesitamos esta variable adicional ara almacenar los resultados filtrados
  categorias: Categoria[] = [];
  idCategoriaSeleccionada: number = 0;
  columnas: any[] = [];
  totalElements = 0;
  filtro: string = ''; // necesitamos esta variable para almacenar el texto del filtro
  langChangeSubscription: Subscription = new Subscription(); // así estamos siempre suscritos a los cambios de idioma en tiempo real
  mostrarFiltro = false;


  constructor(
    private readonly preguntaService: PreguntaService,
    private readonly categoriaService: CategoriaService,
    private readonly router: Router,
    translate: TranslateService
  ) {
    super (translate);
  }

  ngOnInit() {
    this.langChangeSubscription = this.translate.onLangChange.subscribe(() => {
      this.traducirColumnas();
    });
    this.obtenerCategorias();
    this.traducirColumnas();
    this.obtenerDatos();
  }

  ngOnDestroy() {
    if (this.langChangeSubscription) {
      this.langChangeSubscription.unsubscribe();
    }
  }

  obtenerCategorias() {
    this.categoriaService.obtenerCategoriasSinPaginar().subscribe(categorias => {
      this.categorias = categorias;
    });
  }

  // creamos un método público, accesible desde el HTML, para cambiar categorías, ya que los específicos para ello son privados
  obtenerPreguntasFiltradas() {
    this.page = 1; // reiniciar a la primera página al cambiar de categoría
    if (Number(this.idCategoriaSeleccionada) === 0) {
      this.obtenerDatos();
    } else {
      this.obtenerPreguntasPorCategoria(this.idCategoriaSeleccionada);
    }
  }

  protected obtenerDatos(): void {
    if (this.filtro.trim()) {
      this.preguntaService.obtenerListaDePreguntasPorContenido(this.page -1, this.size, this.sortField, this.sortDirection, this.filtro)
        .subscribe(response => {
          this.preguntasFiltradas = response.content;
          this.totalPages = response.totalPages;
          this.preguntas = response.content;
          this.totalItems = response.totalElements;
        });
    } else {
      this.preguntaService.obtenerListaDePreguntas(this.page -1, this.size, this.sortField, this.sortDirection)
        .subscribe(response => {
          this.preguntasFiltradas = response.content;
          this.totalPages = response.totalPages;
          this.preguntas = response.content;
          this.totalItems = response.totalElements;
        });
    }
  }

  private obtenerPreguntasPorCategoria(categoriaId: number) {
    this.page = 1;
    this.preguntaService.obtenerListaDePreguntasPorCategoria(this.page -1, this.size, this.sortField, this.sortDirection, categoriaId).subscribe(
      {
        next: (response: any) => {
          this.preguntas = response.content;
          this.preguntasFiltradas = this.preguntas; // actualizamos las preguntas filtradas
          this.totalPages = response.totalPages;
          this.totalItems = response.totalElements;
        },
        error: () => {
          super.lanzarSweetAlert({
            tituloKey: 'swalError',
            textoKey: 'swalErrorMessage',
            icono: 'error',
            botonTextKey: 'swalAccept',
            cancelar: false,
          });
        }
      }
    );
    this.mostrarFiltro = false;
  }

  filtrarPreguntas() {
    this.obtenerPreguntasFiltradas();
    this.page = 0;
    // this.mostrarFiltro = false;
  }

  filtrarPorTipo(tipo: string) {
    if (tipo === 'quesito') {
      this.preguntasFiltradas = this.preguntas.filter(pregunta => pregunta.esPreguntaQuesito);
    } else if (tipo === 'rondaFinal') {
      this.preguntasFiltradas = this.preguntas.filter(pregunta => pregunta.esPreguntaRondaFinal);
    }
    // this.mostrarFiltro = false;
  }

  // resetea el filtro y muestra todas las preguntas
  resetearFiltro() {
    this.preguntasFiltradas = this.preguntas; // vuelve a mostrar todas las preguntas
  }

  actualizarPregunta(id: number) {
    this.router.navigate(['/actualizar-pregunta/id', id]);
    // Para refrescar la lista de contactos después de eliminar
    this.obtenerDatos();
  }

  eliminarPregunta(id: number): void {
    this.eliminarEntidad(
      id,
      this.preguntas,
      (id) => this.preguntaService.eliminarPregunta(id),
      'swalEraseQuestion',
      'swalSure',
      'swalEraseQuestionConfirmedMessage'
    );
  }

  private traducirColumnas() {
    this.columnas = [
      { titulo: this.translate.instant('id'), campo: 'id' },
      { titulo: this.translate.instant('categoryDescription'), campo: 'categoria' },
      { titulo: this.translate.instant('questionText'), campo: 'textoPregunta' },
      { titulo: this.translate.instant('answerText'), campo: 'respuestaCorrecta' },
      { titulo: this.translate.instant('jewelQuestion'), campo: 'esPreguntaQuesito' },
      { titulo: this.translate.instant('finalRoundQuestion'), campo: 'esPreguntaRondaFinal' }
    ];
  }

  override nextPage() {
    super.nextPage();  // Usando el método heredado de BaseComponent
  }

  override prevPage() {
    super.prevPage();  // Usando el método heredado de BaseComponent
  }

  override ordenarPor(campo: string) {
    super.ordenarPor(campo);
    this.obtenerDatos();
  }

  protected override navigateAfterDelete(): void {
    this.router.navigate(['/preguntas']);
  }

}
