export class FichaUsuario {
  id: number = 0;
  nombre: string ='';
  apellidos: string ='';
  telefono: string ='';
  direccion: string ='';
  correoElectronico: string ='';
  nickname: string ='';
  password: string ='';
  score: number =0;
  fechaNacimiento: Date = new Date();
  fechaRegistro: Date = new Date();
}
