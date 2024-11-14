package app.service;

import app.dto.RespuestaDTO;
import app.model.Pregunta;
import app.model.Respuesta;
import app.repository.PreguntaRepository;
import app.repository.RespuestaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;


import java.util.Optional;

@Service
public class RespuestaService {

    @Autowired
    private RespuestaRepository respuestaRepository;
    @Autowired
    private PreguntaRepository preguntaRepository;

    public Page<RespuestaDTO> listarRespuestas(int pag, int tam, String campoOrdenacion, String direccionOrdenacion){
        Sort.Direction direccion = direccionOrdenacion.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pag, tam, Sort.by(direccion, campoOrdenacion));

        Page<Respuesta> respuestasPage = respuestaRepository.findAll(pageable);        // obtener la página de respuestas desde el repositorio

        return respuestasPage.map(respuesta -> {                                       // convertir la página de entidades Respuesta a DTOs
            RespuestaDTO dto = new RespuestaDTO();
            dto.setId(respuesta.getId());
            dto.setTextoRespuesta(respuesta.getTextoRespuesta());
            dto.setEsRespuestaCorrecta(respuesta.getEsRespuestaCorrecta());
            dto.setPreguntaId(respuesta.getPregunta().getId());
            return dto;
        });
    }

    @Transactional
    public Respuesta crearRespuesta (RespuestaDTO respuestaDTO)
    {
        Optional<Pregunta> preguntaOptional = preguntaRepository.findById(respuestaDTO.getPreguntaId());     // buscar la pregunta asociada usando preguntaId del DTO
        if (!preguntaOptional.isPresent()) {
            throw new RuntimeException("Pregunta no encontrada");
        }
        Pregunta pregunta = preguntaOptional.get();

        Respuesta nuevaRespuesta = new Respuesta();                                                           // crear una nueva instancia de Respuesta y asignar valores del DTO
        nuevaRespuesta.setTextoRespuesta(respuestaDTO.getTextoRespuesta());
        nuevaRespuesta.setEsRespuestaCorrecta(respuestaDTO.isEsRespuestaCorrecta());
        nuevaRespuesta.setPregunta(pregunta);

        return respuestaRepository.save(nuevaRespuesta);                                                    // guardar la nueva respuesta
    }

    public Respuesta obtenerRespuestaPorId (Long id){
        return respuestaRepository.findById(id).orElseGet(null);
    }

    @Transactional
    public Respuesta actualizarRespuesta(Long id, RespuestaDTO respuestaDTO) {
        Optional<Respuesta> respuestaOptional = respuestaRepository.findById(id);                        // buscar la respuesta por ID
        if (!respuestaOptional.isPresent()) {
            throw new RuntimeException("Respuesta no encontrada");
        }

        Respuesta respuesta = respuestaOptional.get();

        respuesta.setTextoRespuesta(respuestaDTO.getTextoRespuesta());                                   // actualizar los campos de la respuesta
        respuesta.setEsRespuestaCorrecta(respuestaDTO.isEsRespuestaCorrecta());

        Optional<Pregunta> preguntaOptional = preguntaRepository.findById(respuestaDTO.getPreguntaId());  // buscar la pregunta relacionada usando preguntaId del DTO
        if (!preguntaOptional.isPresent()) {
            throw new RuntimeException("Pregunta no encontrada");
        }
        Pregunta pregunta = preguntaOptional.get();

        respuesta.setPregunta(pregunta);                                                                 // asignar la pregunta a la respuesta

        return respuestaRepository.save(respuesta);                                                      // guardar la respuesta actualizada
    }

    public void borrarRespuesta(Long id){
        respuestaRepository.deleteById(id);
    }
}
