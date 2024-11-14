import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError, of, tap } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Categoria } from '../../model/categoria';
import { CategoriaService } from '../../service/categoria.service';
import { BaseComponent } from '../../base.component';

@Component({
  selector: 'app-actualizar-categoria',
  standalone: true,
  imports: [FormsModule, CommonModule, TranslateModule],
  templateUrl: './actualizar-categoria.component.html',
  styleUrls: ['./actualizar-categoria.component.css']
})
export class ActualizarCategoriaComponent extends BaseComponent implements OnInit {
  id = 0;
  categoria = new Categoria();

  constructor(
    private readonly categoriaService: CategoriaService,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    translate: TranslateService // inyecta TranslateService
  ) {
    super(translate);
  }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];

    this.categoriaService.obtenerCategoriaPorId(this.id).pipe(
      tap((datosActualizados: Categoria) => {
        this.categoria = datosActualizados;
      }),
      catchError(error => {
        console.error(error);
        return of(null);
      })
    ).subscribe();
  }

  navigateListaCategorias() {
    const parametros = { id: this.categoria.id };
    this.lanzarSweetAlertSimplificado('swalCategoryUpdated', 'swalCategoryUpdatedSuccess', parametros)
      .then(() => {
        this.router.navigate(['/categorias']);
      });
  }

  onSubmit(): void {
    if (this.categoria) {
      this.categoriaService.actualizarCategoria(this.id, this.categoria).pipe(
        tap(() => {
          this.navigateListaCategorias(); // redirigir en caso de éxito
        }),
        catchError(() => {
          this.lanzarSweetAlert({
            tituloKey: 'swalError',
            textoKey: 'swalNotPermitted',
            icono: 'error',
            botonTextKey: 'swalAccept',
            cancelar: false,
          });
          return of(null); // retornar un observable vacío en caso de error
        })
      ).subscribe();
    }
  }

  backToCategory() {
    this.router.navigate(['/categorias']);
  }

  protected override obtenerDatos() {
    console.log('empty');
  }

  protected override navigateAfterDelete(): void {
      console.log ("empty");
  }
}
