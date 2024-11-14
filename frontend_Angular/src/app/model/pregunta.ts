import { Categoria } from "./categoria";
import { Respuesta } from "./respuesta";

export class Pregunta {
  id: number = 0;
  textoPregunta = '';
  esPreguntaQuesito = false;
  esPreguntaRondaFinal = false;

  categoria: Categoria = {
    id: 0,
    categoriaDesc: ''
  };
  respuestas : Respuesta []= [];
}
