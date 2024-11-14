import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError, of, tap } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Pregunta } from '../../model/pregunta';
import { PreguntaService } from '../../service/pregunta.service';
import { Categoria } from '../../model/categoria';
import { CategoriaService } from '../../service/categoria.service';
import { BaseComponent } from '../../base.component';

@Component({
  selector: 'app-actualizar-pregunta',
  standalone: true,
  imports: [FormsModule, CommonModule, TranslateModule],
  templateUrl: './actualizar-pregunta.component.html',
  styleUrls: ['./actualizar-pregunta.component.css']
})
export class ActualizarPreguntaComponent extends BaseComponent implements OnInit {
  id = 0;
  pregunta = new Pregunta();
  respuestaCorrectaIndex: number = -1;
  tipoPregunta: string = '';
  categorias: Categoria[] = [];

  constructor(
    private readonly preguntaService: PreguntaService,
    private readonly categoriaService: CategoriaService,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    translate: TranslateService
  ) {
    super(translate);
  }


  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];
    this.cargarCategorias();
    this.preguntaService.obtenerPreguntaPorId(this.id).pipe(

      tap((datosAActualizar: Pregunta) => {
        this.pregunta = datosAActualizar;
        this.respuestaCorrectaIndex = this.pregunta.respuestas.findIndex(respuesta => respuesta.esRespuestaCorrecta);

        if (this.pregunta.esPreguntaQuesito) {
          this.tipoPregunta = 'quesito';
        } else if (this.pregunta.esPreguntaRondaFinal) {
          this.tipoPregunta = 'rondaFinal';
        } else {
          this.tipoPregunta = 'normal';
        }
      }),
      catchError(() => {
        this.lanzarSweetAlertSimplificado('swalError', 'swalErrorOccurredText', {}, 'error', 'swalRetry');
        return of(null);
      })
    ).subscribe();
  }

  navigateListaPreguntas() {
    const parametros = { id: this.pregunta.id };
    this.lanzarSweetAlertSimplificado('swalQuestionUpdated', 'swalQuestionUpdatedSuccess', parametros)
      .then(
        () => {
          this.router.navigate(['/preguntas']);
        });
  }

  private cargarCategorias() {
    this.categoriaService.obtenerCategoriasSinPaginar()
      .pipe()
      .subscribe(
        categorias => this.categorias = categorias);
  }

  updatePreguntaType(tipo: string): void {
    if (tipo === 'quesito') {
      this.pregunta.esPreguntaQuesito = true;
      this.pregunta.esPreguntaRondaFinal = false;
    } else if (tipo === 'rondaFinal') {
      this.pregunta.esPreguntaQuesito = false;
      this.pregunta.esPreguntaRondaFinal = true;
    } else {
      this.pregunta.esPreguntaQuesito = false;
      this.pregunta.esPreguntaRondaFinal = false;
    }
  }

  setRespuestaCorrecta(index: number) {
    this.respuestaCorrectaIndex = index;
    this.pregunta.respuestas.forEach((respuesta, i) => respuesta.esRespuestaCorrecta = i === index);
  }

  onSubmit(): void {
    if (this.pregunta) {
      this.pregunta.respuestas = this.pregunta.respuestas.map(respuesta => ({
        ...respuesta,
        preguntaId: this.pregunta.id  // asignar el ID de la pregunta actual
      }));
      this.preguntaService.actualizarPregunta(this.id, this.pregunta).pipe(
        tap(() => {
          this.navigateListaPreguntas(); // redirigir en caso de éxito
        }),
        catchError(() => {
          this.lanzarSweetAlertSimplificado('swalError', 'swalErrorOccurredText', {}, 'error', 'swalRetry');
          return of(null); // retornar un observable vacío en caso de error
        })
      ).subscribe();
    }
    console.log(this.pregunta);
  }

  backToCategory() {
    this.router.navigate(['/preguntas']);
  }

  protected override obtenerDatos(): void {
    console.log("empty");
  }

  protected override navigateAfterDelete(): void {
    console.log("empty");
  }
}
