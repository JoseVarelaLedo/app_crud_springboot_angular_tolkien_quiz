package app.service;

import app.model.Pregunta;
import app.model.Respuesta;
import app.repository.PreguntaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PreguntaService {
    @Autowired
    private PreguntaRepository preguntaRepository;

    public Page<Pregunta> listarPreguntas(int pag, int tam, String campoOrdenacion, String direccionOrdenacion, Long categoriaId) {
        Sort.Direction direccion = direccionOrdenacion.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pag, tam, Sort.by(direccion, campoOrdenacion));

        if (categoriaId != null) {
            return preguntaRepository.findByCategoriaId(categoriaId, pageable);   // filtrar preguntas por categoría
        } else {
            return preguntaRepository.findAll(pageable);                          // si no se especifica categoría, devolver todas las preguntas
        }
    }

    public Page<Pregunta> encontrarPreguntasPorContenido (int pag, int tam, String campoOrdenacion, String direccionOrdenacion,String textoPregunta){
        Sort.Direction direccion = direccionOrdenacion.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pag, tam, Sort.by(direccion, campoOrdenacion));
        return preguntaRepository.findByTextoPreguntaContainingIgnoreCase(textoPregunta, pageable);
    }

    public Page<Pregunta> encontrarPreguntasPorCategoria (int pag, int tam, String campoOrdenacion, String direccionOrdenacion,Integer categoriaId){
        Sort.Direction direccion = direccionOrdenacion.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pag, tam, Sort.by(direccion, campoOrdenacion));
        return preguntaRepository.findQuestionByCategoryId(categoriaId, pageable);
    }

    @Transactional
    public void crearPregunta(Pregunta pregunta) {
        preguntaRepository.save(pregunta);
    }

    public Pregunta obtenerPreguntaPorId(Long id) {
        return preguntaRepository.findById(id).orElseGet(null);
    }

    @Transactional
    public void actualizarPregunta(Pregunta pregunta) {
        System.out.println ("Quesito?:" + pregunta.getEsPreguntaQuesito());
        System.out.println ("Ronda final?:" + pregunta.getEsPreguntaRondaFinal());
        List<Respuesta> respuestas = pregunta.getRespuestas();
        for (Respuesta respuesta : respuestas) {
            respuesta.setPregunta(pregunta);
        }

        preguntaRepository.save(pregunta);
    }

    public void borrarPregunta(Long id) {
        preguntaRepository.deleteById(id);
    }
}