import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { catchError, of, tap } from 'rxjs';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { FichaUsuario } from '../../model/ficha-usuario';
import { FichaUsuarioService } from '../../service/ficha-usuario.service';
import { BaseComponent } from '../../base.component';

@Component({
  selector: 'app-actualizar-ficha-usuario',
  standalone: true,
  imports: [FormsModule, CommonModule, TranslateModule],
  templateUrl: './actualizar-ficha-usuario.component.html',
  styleUrls: ['./actualizar-ficha-usuario.component.css']
})
export class ActualizarFichaUsuarioComponent extends BaseComponent implements OnInit{

  id = 0;
  fichaUsuario = new FichaUsuario();

  constructor(
    private readonly fichaUsuarioService: FichaUsuarioService,
    private readonly router: Router,
    private readonly route: ActivatedRoute,
    translate: TranslateService
  ) {
    super (translate);
  }

  ngOnInit(): void {
    this.id = this.route.snapshot.params['id'];

    this.fichaUsuarioService.obtenerFichaDeUsuarioPorId(this.id).pipe(
      tap((datosActualizados: FichaUsuario) => {
        this.fichaUsuario = datosActualizados;
      }),
      catchError(() => {
        this.lanzarSweetAlertSimplificado('swalError', 'swalErrorOccurredText', {}, 'error', 'swalRetry');
        return of(null);
      })
    ).subscribe();
  }

  navigateListaFichasDeUsuario() {
    const parametros = { nombre: this.fichaUsuario.nombre, apellidos: this.fichaUsuario.apellidos };
    this.lanzarSweetAlertSimplificado('swalUserFileUpdated', 'swalUserFileUpdatedSuccess', parametros)
    .then(() => {
      this.router.navigate(['/fichas-usuario']);
    });
  }

  onSubmit(): void {
    if (this.fichaUsuario) {
      this.fichaUsuarioService.actualizarFichaDeUsuario(this.id, this.fichaUsuario).pipe(
        tap(() => {
          this.navigateListaFichasDeUsuario();
        }),
        catchError(() => {
          this.lanzarSweetAlertSimplificado('swalError', 'swalErrorOccurredText', {}, 'error', 'swalRetry');
          return of(null);
        })
      ).subscribe();
    }
  }

  backToCategory() {
    this.router.navigate(['/fichas-usuario']);
  }


  protected override obtenerDatos() {
    console.log('empty');
  }

  protected override navigateAfterDelete(): void {
    console.log ("empty");
}
}
