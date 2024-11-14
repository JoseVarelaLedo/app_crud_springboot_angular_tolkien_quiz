import Swal from 'sweetalert2';
import { TranslateService } from '@ngx-translate/core';
import { Observable } from 'rxjs';

export abstract class BaseComponent {
  page: number = 1;
  size: number = 7;
  totalPages: number = 0;
  totalItems: number = 0;
  sortField: string = '';
  sortDirection: string = 'asc';

  constructor(public readonly _translate: TranslateService) { }

  get translate(): TranslateService {
    return this._translate;
  }

   // Método genérico para eliminar entidad
   eliminarEntidad<T>(id: number, entidad: T[], eliminarServiceMethod: (id: number) => Observable<any>, textoKey: string, tituloKey: string, textoConfirmado: string): void {
    const entidadAEliminar = entidad.find((e: any) => e.id === id);  // Busca la entidad por ID
    const parametros = entidadAEliminar ? { ...entidadAEliminar } : {};

    if (entidadAEliminar) {
      this.lanzarSweetAlert({
        tituloKey: tituloKey,
        textoKey: textoKey,
        parametros: parametros,
        icono: 'warning',
        botonTextKey: 'swalEraseConfirm',
        cancelar: true,
        cancelarTextKey: 'swalEraseCancel',
        botonesColores: { confirmButtonColor: '#3085d6', cancelButtonColor: '#d33' }
      })
      .then((result) => {
        if (result.isConfirmed) {
          eliminarServiceMethod(id).subscribe({
            next: () => {
              this.lanzarSweetAlert({
                tituloKey: 'swalEraseConfirmed',
                textoKey: textoConfirmado,
                parametros: parametros,
                icono: 'success',
                botonTextKey: 'swalAccept',
                cancelar: false,
              });
              this.obtenerDatos();  // Refrescar datos después de eliminar
              this.navigateAfterDelete();
            },
            error: () => {
              this.lanzarSweetAlert({
                tituloKey: 'swalError',
                textoKey: 'swalErrorMessage',
                icono: 'error',
                botonTextKey: 'swalAccept',
                cancelar: false,
              });
            }
          });
        }
      });
    } else {
      this.lanzarSweetAlert({
        tituloKey: 'swalError',
        textoKey: 'swalNotFoundMessage',
        icono: 'error',
        botonTextKey: 'swalAccept',
        cancelar: false,
      });
    }
  }


  // Método para lanzar SweetAlert con opciones configurables
  lanzarSweetAlert(options: AlertaOptions): Promise<any> {
    // obtención de las traducciones de las claves pasadas
    const title = this.translate.instant(options.tituloKey);
    const text = this.translate.instant(options.textoKey, options.parametros || {});
    const confirmButtonText = this.translate.instant(options.botonTextKey ?? 'swalAccept');
    const cancelButtonText = this.translate.instant(options.cancelarTextKey ?? 'swalCancel');

    // configuración de los colores de los botones, con valores predeterminados
    const confirmButtonColor = options.botonesColores?.confirmButtonColor ?? '#3085d6';
    const cancelButtonColor = options.botonesColores?.cancelButtonColor ?? '#d33';

    return Swal.fire({
      title: title,
      text: text,
      icon: options.icono ?? 'success',
      confirmButtonText: confirmButtonText,
      cancelButtonText: options.cancelar ? cancelButtonText : undefined,
      confirmButtonColor: confirmButtonColor,
      cancelButtonColor: options.cancelar ? cancelButtonColor : undefined,
      showCancelButton: options.cancelar ?? false
    });
  }

  lanzarSweetAlertSimplificado(tituloKey: string, textoKey: string, parametros: any = {}, icono: any = 'success', botonTextKey: string = 'swalAccept'): Promise<any> {
    const title = this.translate.instant(tituloKey);
    const text = this.translate.instant(textoKey, parametros);
    const confirmButtonText = this.translate.instant(botonTextKey);

    return Swal.fire({
      title: title,
      text: text,
      icon: icono,
      confirmButtonText: confirmButtonText
    });
  }

  // Método para cambiar a la siguiente página
  public nextPage(): void {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.obtenerDatos();  // Llamada a método genérico
    }
  }

  // Método para cambiar a la página anterior
  public prevPage(): void {
    if (this.page > 0) {
      this.page--;
      this.obtenerDatos();  // Llamada a método genérico
    }
  }

  // Método para ordenar por un campo específico
  public ordenarPor(campo: string): void {
    this.sortField = campo;
    this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    this.obtenerDatos();  // Llamada a método genérico // Volver a obtener registros con la nueva ordenación
  }

  onKeyDown(event: KeyboardEvent, columna: any) {
    // Comprobamos si la tecla presionada fue Enter o Space
    if (event.key === 'Enter' || event.key === ' ') {
      this.ordenarPor(columna.campo);  // Llama al método ordenarPor con el campo correspondiente
    }
  }

  onPageChange(page: number): void {
    this.page = page;
    this.obtenerDatos();  // Recargar los datos cuando la página cambie
  }

  // Método genérico que cada clase hija debe implementar para obtener los datos
  protected abstract obtenerDatos(): void;
  protected abstract navigateAfterDelete(): void;
}

interface AlertaOptions {
  tituloKey: string;
  textoKey: string;
  parametros?: any;
  icono?: 'success' | 'error' | 'warning' | 'info';
  botonTextKey?: string;
  cancelar?: boolean;
  cancelarTextKey?: string;
  botonesColores?: {
    confirmButtonColor?: string;
    cancelButtonColor?: string;
  };
}
