import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { Categoria } from '../../model/categoria';
import { CategoriaService } from '../../service/categoria.service';
import { BaseComponent } from '../../base.component';

@Component({
  selector: 'app-nueva-categoria',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, TranslateModule],
  templateUrl: './nueva-categoria.component.html',
  styleUrls: ['./nueva-categoria.component.css']
})
export class NuevaCategoriaComponent extends BaseComponent{
  categoria: Categoria = {
    id: 0,
    categoriaDesc: ''
  };

  constructor(private readonly categoriaService: CategoriaService,
    private readonly router: Router,
    translate: TranslateService) {
      super (translate);
    }

  guardarCategoria() {
    this.categoriaService.registrarNuevaCategoria(this.categoria).subscribe(
      {
        next: () => {
          this.navigateListaCategorias();
        },
        error: () => {
          this.lanzarSweetAlert({
            tituloKey: 'swalError',
            textoKey: 'swalNotPermitted',
            icono: 'error',
            botonTextKey: 'swalAccept',
            cancelar: false,
          })
          .then(() => {
            console.log("error");
          });
        }
      }
    )
  }

  navigateListaCategorias() {
    this.lanzarSweetAlertSimplificado(
      'swalCategoryCreated',
      'swalCategoryCreatedMessage',
      { categoriaDesc: this.categoria.categoriaDesc },
      'success',
      'swalAccept'
    )
    .then(() => {
      this.router.navigate(['/categorias']);
    });
  }

  onSubmit() {
    this.guardarCategoria();
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
