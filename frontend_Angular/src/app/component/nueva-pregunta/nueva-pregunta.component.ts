import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Pregunta } from '../../model/pregunta';
import { PreguntaService } from '../../service/pregunta.service';
import { Respuesta } from '../../model/respuesta';
import { Categoria } from '../../model/categoria';
import { CategoriaService } from '../../service/categoria.service';
import { BaseComponent } from '../../base.component';

@Component({
  selector: 'app-nueva-pregunta',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, TranslateModule],
  templateUrl: './nueva-pregunta.component.html',
  styleUrls: ['./nueva-pregunta.component.css']
})
export class NuevaPreguntaComponent extends BaseComponent implements OnInit {
  pregunta: Pregunta = new Pregunta();
  categorias: Categoria[] = [];  // Para cargar la lista de categorías
  respuestaCorrectaIndex: number | null = null;  // Para seleccionar la respuesta correcta
  tipoPregunta: string = '';

  constructor(
    private readonly preguntaService: PreguntaService,
    private readonly categoriaService: CategoriaService,
    private readonly router: Router,
    translate: TranslateService,
  ) {
    super(translate)
  }

  ngOnInit() {
    this.cargarCategorias();
    this.pregunta.respuestas = [new Respuesta(), new Respuesta(), new Respuesta(), new Respuesta()]; // Crear cuatro respuestas vacías por defecto
  }

  private cargarCategorias() {
    this.categoriaService.obtenerCategoriasSinPaginar().subscribe({
      next: (categorias: Categoria[]) => this.categorias = categorias,
      error: (error) => {
        this.lanzarSweetAlertSimplificado('swalError', 'swalError', {}, 'error', 'swalAccept')
          .then(() => {
            console.log(error);
          });
      }
    });
  }

  setRespuestaCorrecta(index: number) {
    this.respuestaCorrectaIndex = index;
    this.pregunta.respuestas.forEach((respuesta, i) => respuesta.esRespuestaCorrecta = i === index);
  }

  guardarPregunta() {
    if (this.respuestaCorrectaIndex === null) {
      this.lanzarSweetAlertSimplificado(
        'selectCorrectAnswer',
        '',
        {},
        'warning',
        'swalAccept'
      )
        .then(() => {
          return;
        });
    }

    this.preguntaService.registrarNuevaPregunta(this.pregunta).subscribe(
      {
        next: () => {
          this.lanzarSweetAlertSimplificado(
            'swalSuccess',
            'questionCreatedSuccessfully',
            {},
            'success',
            'swalAccept'
          )
            .then(() => {
              this.router.navigate(['/preguntas']);
            });

        },
        error: () => {
          this.lanzarSweetAlert({
            tituloKey: 'swalError',
            textoKey: 'swalNotPermitted',
            icono: 'error',
            botonTextKey: 'swalAccept',
            cancelar: false,
          })
            .then((error) => {
              console.log(error);
            });
        }
      }
    )
  }

  setTipoPregunta(tipo: string) {
    this.tipoPregunta = tipo;
    switch (tipo) {
      case 'joya':
        this.pregunta.esPreguntaQuesito = true;
        this.pregunta.esPreguntaRondaFinal = false;
        break;
      case 'rondaFinal':
        this.pregunta.esPreguntaQuesito = false;
        this.pregunta.esPreguntaRondaFinal = true;
        break;
      case 'ninguno':
        this.pregunta.esPreguntaQuesito = false;
        this.pregunta.esPreguntaRondaFinal = false;
        break;
    }
  }

  backToCategory() {
    this.router.navigate(['/preguntas']);
  }

  protected override obtenerDatos() {
    console.log('empty');
  }

  protected override navigateAfterDelete(): void {
    console.log("empty");
  }
}
