import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterModule } from '@angular/router';
import { TranslateModule, TranslateService } from '@ngx-translate/core';
import { FichaUsuario } from '../../model/ficha-usuario';
import { FichaUsuarioService } from '../../service/ficha-usuario.service';
import { BaseComponent } from '../../base.component';

@Component({
  selector: 'app-nueva-ficha-usuario',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterModule, TranslateModule],
  templateUrl: './nueva-ficha-usuario.component.html',
  styleUrls: ['./nueva-ficha-usuario.component.css']
})
export class NuevaFichaUsuarioComponent extends BaseComponent{
  fichaUsuario: FichaUsuario = {
    id: 0,
    nombre: '',
    apellidos: '',
    telefono: '',
    direccion: '',
    correoElectronico: '',
    nickname: '',
    password: '',
    score: 0,
    fechaNacimiento: new Date(),
    fechaRegistro: new Date()
  }

  constructor(private readonly fichaUsuarioService: FichaUsuarioService,
    private readonly router: Router,
   translate: TranslateService) {
    super (translate);
    }

    guardarFichaUsuario() {
      this.fichaUsuarioService.registrarFichaDeUsuario(this.fichaUsuario).subscribe(
        {
          next: () => {
            this.navigateListaFichasDeUsuarios();
          },
          error: () => {
            this.lanzarSweetAlertSimplificado('swalError', 'swalError', {}, 'error', 'swalAccept')
          .then(() => {
            console.log("error");
          });
          }
        }
      );
    }

  navigateListaFichasDeUsuarios() {
    this.lanzarSweetAlertSimplificado(
      'swalUserRecordCreated',
      'swalUserRecordCreatedMessage',
      { nombre: this.fichaUsuario.nombre, apellidos: this.fichaUsuario.apellidos },
      'success',
      'swalAccept'
    )
    .then(() => {
      this.router.navigate(['/login']);
    });
  }

  onSubmit() {
    this.guardarFichaUsuario();
  }

  backToCategory(){
    this.router.navigate(['/fichas-usuario']);
  }

  protected override obtenerDatos() {
    console.log('empty');
  }

  protected override navigateAfterDelete(): void {
      console.log ("empty");
  }
}
