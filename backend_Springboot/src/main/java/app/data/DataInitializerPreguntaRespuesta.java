package app.data;

import app.model.Categoria;
import app.model.Pregunta;
import app.model.Respuesta;
import app.repository.CategoriaRepository;
import app.repository.PreguntaRepository;
import app.repository.RespuestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@Order(4)
@DependsOn("initializerCategorias")
public class DataInitializerPreguntaRespuesta {

    @Autowired
    private PreguntaRepository preguntaRepository;

    @Autowired
    private RespuestaRepository respuestaRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    List<Categoria> categorias;

    @Bean
    public ApplicationRunner initializerPreguntasRespuestas(PreguntaRepository preguntaRepository, RespuestaRepository respuestaRepository) {
       this.categorias = categoriaRepository.findAll();
        return args -> {
            if (preguntaRepository.count() == 0) {
                insertarPreguntasYRespuestas(this.preguntaRepository, this.respuestaRepository);
            }
        };
    }

    public void insertarPreguntasYRespuestas(PreguntaRepository preguntaRepository, RespuestaRepository respuestaRepository) {
        // Preguntas y respuestas para la categoría 1: Historia de la Tierra Media
        List<Pregunta> preguntas1 = Arrays.asList(
                new Pregunta(null, "¿En qué año fue destruido el Anillo Único?", false, false, categorias.get(0), null),
                new Pregunta(null, "¿Quién es conocido como el Medio Elfo?", false, false, categorias.get(0), null),
                new Pregunta(null, "¿Qué criatura derrotó Gandalf en Moria?", false, false, categorias.get(0), null),
                new Pregunta(null, "¿Cuál es el nombre del primer rey de Númenor?", false, false, categorias.get(0), null),
                new Pregunta(null, "¿Quién lideró a los Hombres en la Guerra de la Última Alianza?", true, false, categorias.get(0), null),  // Pregunta de quesito
                new Pregunta(null, "¿En qué batalla murió el Rey Theoden?", false, false, categorias.get(0), null),
                new Pregunta(null, "¿Cómo se llama el caballo de Gandalf?", true, false, categorias.get(0), null),  // Pregunta de quesito
                new Pregunta(null, "¿Qué ciudad gobernaba Denethor?", false, false, categorias.get(0), null),
                new Pregunta(null, "¿Quién fue el creador de los Anillos de Poder?", false, false, categorias.get(0), null),
                new Pregunta(null, "¿Quién es el Señor de los Nazgûl?", false, true, categorias.get(0), null)  // Pregunta de ronda final
        );

        List<Respuesta> respuestas1 = Arrays.asList(
                new Respuesta(null, "En el año 3019 de la Tercera Edad", true, preguntas1.get(0)),
                new Respuesta(null, "En el año 2020 de la Segunda Edad", false, preguntas1.get(0)),
                new Respuesta(null, "En el año 1050 de la Primera Edad", false, preguntas1.get(0)),
                new Respuesta(null, "En el año 3000 de la Cuarta Edad", false, preguntas1.get(0)),
                new Respuesta(null, "Elrond", true, preguntas1.get(1)),
                new Respuesta(null, "Aragorn", false, preguntas1.get(1)),
                new Respuesta(null, "Legolas", false, preguntas1.get(1)),
                new Respuesta(null, "Gandalf", false, preguntas1.get(1)),
                new Respuesta(null, "El Balrog", true, preguntas1.get(2)),
                new Respuesta(null, "Un orco", false, preguntas1.get(2)),
                new Respuesta(null, "Un troll", false, preguntas1.get(2)),
                new Respuesta(null, "Un dragón", false, preguntas1.get(2)),
                new Respuesta(null, "Elros", true, preguntas1.get(3)),
                new Respuesta(null, "Elrond", false, preguntas1.get(3)),
                new Respuesta(null, "Ar-Pharazôn", false, preguntas1.get(3)),
                new Respuesta(null, "Isildur", false, preguntas1.get(3)),
                new Respuesta(null, "Elendil", true, preguntas1.get(4)),
                new Respuesta(null, "Isildur", false, preguntas1.get(4)),
                new Respuesta(null, "Gil-galad", false, preguntas1.get(4)),
                new Respuesta(null, "Aragorn", false, preguntas1.get(4)),
                new Respuesta(null, "La Batalla de los Campos del Pelennor", true, preguntas1.get(5)),
                new Respuesta(null, "La Batalla de Helm", false, preguntas1.get(5)),
                new Respuesta(null, "La Batalla de Dagorlad", false, preguntas1.get(5)),
                new Respuesta(null, "La Batalla de la Última Alianza", false, preguntas1.get(5)),
                new Respuesta(null, "Sombragrís", true, preguntas1.get(6)),
                new Respuesta(null, "Asfaloth", false, preguntas1.get(6)),
                new Respuesta(null, "Brego", false, preguntas1.get(6)),
                new Respuesta(null, "Balinor", false, preguntas1.get(6)),
                new Respuesta(null, "Minas Tirith", true, preguntas1.get(7)),
                new Respuesta(null, "Edoras", false, preguntas1.get(7)),
                new Respuesta(null, "Osgiliath", false, preguntas1.get(7)),
                new Respuesta(null, "Lórien", false, preguntas1.get(7)),
                new Respuesta(null, "Sauron", true, preguntas1.get(8)),
                new Respuesta(null, "Celebrimbor", false, preguntas1.get(8)),
                new Respuesta(null, "Gandalf", false, preguntas1.get(8)),
                new Respuesta(null, "Galadriel", false, preguntas1.get(8)),
                new Respuesta(null, "El Rey Brujo de Angmar", true, preguntas1.get(9)),
                new Respuesta(null, "Saruman", false, preguntas1.get(9)),
                new Respuesta(null, "Sauron", false, preguntas1.get(9)),
                new Respuesta(null, "Morgoth", false, preguntas1.get(9))
        );

        // Guardar todas las preguntas y respuestas de la categoría 1
        preguntaRepository.saveAll(preguntas1);
        respuestaRepository.saveAll(respuestas1);

        // Preguntas y respuestas para la categoría 2: Historia Antigua de Arda
        List<Pregunta> preguntas2 = Arrays.asList(
                new Pregunta(null, "¿Quién fue el primer Rey Supremo de los Elfos?", false, false, categorias.get(1), null),
                new Pregunta(null, "¿Cuál fue el primer reino en Beleriand?", false, false, categorias.get(1), null),
                new Pregunta(null, "¿Qué hizo Morgoth después de la primera guerra de Beleriand?", false, false, categorias.get(1), null),
                new Pregunta(null, "¿Cuál es el nombre del árbol que dio origen a la luz de los Silmarils?", true, false, categorias.get(1), null),  // Quesito
                new Pregunta(null, "¿En qué batalla fue destruida Gondolin?", false, false, categorias.get(1), null),
                new Pregunta(null, "¿Quién forjó los Silmarils?", true, false, categorias.get(1), null),  // Quesito
                new Pregunta(null, "¿Cuál era el propósito de las Dos Lámparas?", false, false, categorias.get(1), null),
                new Pregunta(null, "¿Quién fue el primer elfo en despertar en Cuiviénen?", false, false, categorias.get(1), null),
                new Pregunta(null, "¿Qué destruyó el Reino de Angband?", false, false, categorias.get(1), null),
                new Pregunta(null, "¿Cuál es el nombre de la primera gran guerra en Arda?", false, true, categorias.get(1), null)  // Ronda Final
        );

        List<Respuesta> respuestas2 = Arrays.asList(
                new Respuesta(null, "Ingwe", true, preguntas2.get(0)),
                new Respuesta(null, "Fingolfin", false, preguntas2.get(0)),
                new Respuesta(null, "Feanor", false, preguntas2.get(0)),
                new Respuesta(null, "Maedhros", false, preguntas2.get(0)),
                new Respuesta(null, "Doriath", true, preguntas2.get(1)),
                new Respuesta(null, "Gondolin", false, preguntas2.get(1)),
                new Respuesta(null, "Nargothrond", false, preguntas2.get(1)),
                new Respuesta(null, "Menegroth", false, preguntas2.get(1)),
                new Respuesta(null, "Huyó a Angband", true, preguntas2.get(2)),
                new Respuesta(null, "Se unió a los Valar", false, preguntas2.get(2)),
                new Respuesta(null, "Reclamó Gondolin", false, preguntas2.get(2)),
                new Respuesta(null, "Creó el Anillo Único", false, preguntas2.get(2)),
                new Respuesta(null, "Telperion", true, preguntas2.get(3)),
                new Respuesta(null, "Laurelin", false, preguntas2.get(3)),
                new Respuesta(null, "Galathilion", false, preguntas2.get(3)),
                new Respuesta(null, "Glingal", false, preguntas2.get(3)),
                new Respuesta(null, "Batalla de la Caída de Gondolin", true, preguntas2.get(4)),
                new Respuesta(null, "Batalla de Dagor Bragollach", false, preguntas2.get(4)),
                new Respuesta(null, "Batalla de Nirnaeth Arnoediad", false, preguntas2.get(4)),
                new Respuesta(null, "Batalla de las Lágrimas Innumerables", false, preguntas2.get(4)),
                new Respuesta(null, "Feanor", true, preguntas2.get(5)),
                new Respuesta(null, "Celebrimbor", false, preguntas2.get(5)),
                new Respuesta(null, "Galadriel", false, preguntas2.get(5)),
                new Respuesta(null, "Thingol", false, preguntas2.get(5)),
                new Respuesta(null, "La luz de los dos árboles", true, preguntas2.get(6)),
                new Respuesta(null, "La creación de los Valar", false, preguntas2.get(6)),
                new Respuesta(null, "El primer Silmaril", false, preguntas2.get(6)),
                new Respuesta(null, "El regreso de Feanor", false, preguntas2.get(6)),
                new Respuesta(null, "La Guerra de la Ira", true, preguntas2.get(7)),
                new Respuesta(null, "La Batalla de Dagorlad", false, preguntas2.get(7)),
                new Respuesta(null, "La Batalla de la Última Alianza", false, preguntas2.get(7)),
                new Respuesta(null, "La Caída de Númenor", false, preguntas2.get(7)),
                new Respuesta(null, "Eärendil", true, preguntas2.get(8)),
                new Respuesta(null, "Thingol", false, preguntas2.get(8)),
                new Respuesta(null, "Lúthien", false, preguntas2.get(8)),
                new Respuesta(null, "Beren", false, preguntas2.get(8)),
                new Respuesta(null, "La Guerra de las Joyas", true, preguntas2.get(9))  // Ronda Final
        );

        // Guardar todas las preguntas y respuestas de la categoría 2
        preguntaRepository.saveAll(preguntas2);
        respuestaRepository.saveAll(respuestas2);

        // Preguntas y respuestas para la categoría 3: Geografía de Arda
        List<Pregunta> preguntas3 = Arrays.asList(
                new Pregunta(null, "¿Qué región de la Tierra Media es conocida por su clima frío y montañoso?", false, false, categorias.get(2), null),
                new Pregunta(null, "¿Cuál es el nombre del mar que rodea la región de Númenor?", false, false, categorias.get(2), null),
                new Pregunta(null, "¿Cómo se llama la capital de Rohan?", false, false, categorias.get(2), null),
                new Pregunta(null, "¿Qué río cruza el Bosque Negro?", false, false, categorias.get(2), null),
                new Pregunta(null, "¿Qué ciudad fue conocida como la ‘Ciudad de los Elfos’ en Beleriand?", true, false, categorias.get(2), null),  // Quesito
                new Pregunta(null, "¿Cuál es el nombre del gran río que divide Gondor y Rohan?", true, false,categorias.get(2), null),  // Quesito
                new Pregunta(null, "¿En qué montaña se encuentra la entrada secreta de la Morada de Durin?", false, false, categorias.get(2), null),
                new Pregunta(null, "¿Cuál es el nombre del reino subterráneo de los Enanos en la región de Erebor?", false, false, categorias.get(2), null),
                new Pregunta(null, "¿Qué bosque está gobernado por Thranduil?", false, false, categorias.get(2), null),
                new Pregunta(null, "¿Qué cadena montañosa es famosa por su paso conocido como el Paso del Carro?", false, true, categorias.get(2), null)  // Ronda Final
        );

        List<Respuesta> respuestas3 = Arrays.asList(
                new Respuesta(null, "Las Montañas Nubladas", true, preguntas3.get(0)),
                new Respuesta(null, "El Bosque de Lothlórien", false, preguntas3.get(0)),
                new Respuesta(null, "El Bosque Negro", false, preguntas3.get(0)),
                new Respuesta(null, "Las Montañas Grises", false, preguntas3.get(0)),
                new Respuesta(null, "El Mar de Rhûn", false, preguntas3.get(1)),
                new Respuesta(null, "El Gran Mar", false, preguntas3.get(1)),
                new Respuesta(null, "El Mar de Beleriand", false, preguntas3.get(1)),
                new Respuesta(null, "El Mar de Númenor", true, preguntas3.get(1)),
                new Respuesta(null, "Edoras", true, preguntas3.get(2)),
                new Respuesta(null, "Minas Tirith", false, preguntas3.get(2)),
                new Respuesta(null, "Osgiliath", false, preguntas3.get(2)),
                new Respuesta(null, "Lothlórien", false, preguntas3.get(2)),
                new Respuesta(null, "El Anduin", true, preguntas3.get(3)),
                new Respuesta(null, "El Guathló", false, preguntas3.get(3)),
                new Respuesta(null, "El Ailin", false, preguntas3.get(3)),
                new Respuesta(null, "El Limlight", false, preguntas3.get(3)),
                new Respuesta(null, "Eregion", true, preguntas3.get(4)),
                new Respuesta(null, "Doriath", false, preguntas3.get(4)),
                new Respuesta(null, "Valinor", false, preguntas3.get(4)),
                new Respuesta(null, "Nargothrond", false, preguntas3.get(4)),
                new Respuesta(null, "El Río Anduin", true, preguntas3.get(5)),
                new Respuesta(null, "El Río Gwathló", false, preguntas3.get(5)),
                new Respuesta(null, "El Río Lhûn", false, preguntas3.get(5)),
                new Respuesta(null, "El Río Isen", false, preguntas3.get(5)),
                new Respuesta(null, "La Montaña Solitaria", true, preguntas3.get(6)),
                new Respuesta(null, "La Montaña del Destino", false, preguntas3.get(6)),
                new Respuesta(null, "Las Montañas Nubladas", false, preguntas3.get(6)),
                new Respuesta(null, "El Monte Gundabad", false, preguntas3.get(6)),
                new Respuesta(null, "Erebor", true, preguntas3.get(7)),
                new Respuesta(null, "Moria", false, preguntas3.get(7)),
                new Respuesta(null, "Mirkwood", false, preguntas3.get(7)),
                new Respuesta(null, "Gondolin", false, preguntas3.get(7)),
                new Respuesta(null, "El Bosque Negro", true, preguntas3.get(8)),
                new Respuesta(null, "Lothlórien", false, preguntas3.get(8)),
                new Respuesta(null, "Eregion", false, preguntas3.get(8)),
                new Respuesta(null, "Amon Hen", false, preguntas3.get(8)),
                new Respuesta(null, "Las Montañas Blancas", true, preguntas3.get(9))  // Ronda Final
        );

        // Guardar todas las preguntas y respuestas de la categoría 3
        preguntaRepository.saveAll(preguntas3);
        respuestaRepository.saveAll(respuestas3);

        // Preguntas y respuestas para la categoría 4: Personajes Ilustres
        List<Pregunta> preguntas4 = Arrays.asList(
                new Pregunta(null, "¿Quién es el Señor Oscuro de Mordor?", true, false, categorias.get(3), null),  // Quesito
                new Pregunta(null, "¿Cuál es el nombre verdadero de Aragorn?", false, false,categorias.get(3), null),
                new Pregunta(null, "¿Quién fue el primer Rey de Númenor?", false, false, categorias.get(3), null),
                new Pregunta(null, "¿Cuál es el título de Gandalf?", false, false, categorias.get(3), null),
                new Pregunta(null, "¿Quién fue el creador de los Anillos de Poder?", true, false, categorias.get(3), null),  // Quesito
                new Pregunta(null, "¿Qué criatura traicionó a Frodo?", false, false, categorias.get(3), null),
                new Pregunta(null, "¿Quién es la Dama de Lothlórien?", false, false, categorias.get(3), null),
                new Pregunta(null, "¿Qué elfo era amigo de Aragorn?", false, false, categorias.get(3), null),
                new Pregunta(null, "¿Quién es el padre de Boromir?", false, false, categorias.get(3), null),
                new Pregunta(null, "¿Qué rey regresó a Gondor?", false, true, categorias.get(3), null)  // Ronda Final
        );

        List<Respuesta> respuestas4 = Arrays.asList(
                // Respuestas para la pregunta 31 (quesito)
                new Respuesta(null, "Sauron", true, preguntas4.get(0)),
                new Respuesta(null, "Saruman", false, preguntas4.get(0)),
                new Respuesta(null, "Melkor", false, preguntas4.get(0)),
                new Respuesta(null, "Gollum", false, preguntas4.get(0)),

                // Respuestas para la pregunta 32
                new Respuesta(null, "Estel", true, preguntas4.get(1)),
                new Respuesta(null, "Thorongil", false, preguntas4.get(1)),
                new Respuesta(null, "Telcontar", false, preguntas4.get(1)),
                new Respuesta(null, "Elessar", false, preguntas4.get(1)),

                // Respuestas para la pregunta 33
                new Respuesta(null, "Elros", true, preguntas4.get(2)),
                new Respuesta(null, "Elendil", false, preguntas4.get(2)),
                new Respuesta(null, "Isildur", false, preguntas4.get(2)),
                new Respuesta(null, "Ar-Pharazôn", false, preguntas4.get(2)),

                // Respuestas para la pregunta 34
                new Respuesta(null, "El Gris", true, preguntas4.get(3)),
                new Respuesta(null, "El Blanco", false, preguntas4.get(3)),
                new Respuesta(null, "El Sabio", false, preguntas4.get(3)),
                new Respuesta(null, "El Señor de los Caballos", false, preguntas4.get(3)),

                // Respuestas para la pregunta 35 (quesito)
                new Respuesta(null, "Celebrimbor", true, preguntas4.get(4)),
                new Respuesta(null, "Fëanor", false, preguntas4.get(4)),
                new Respuesta(null, "Aulë", false, preguntas4.get(4)),
                new Respuesta(null, "Galadriel", false, preguntas4.get(4)),

                // Respuestas para la pregunta 36
                new Respuesta(null, "Gollum", true, preguntas4.get(5)),
                new Respuesta(null, "Sam", false, preguntas4.get(5)),
                new Respuesta(null, "Merry", false, preguntas4.get(5)),
                new Respuesta(null, "Legolas", false, preguntas4.get(5)),

                // Respuestas para la pregunta 37
                new Respuesta(null, "Galadriel", true, preguntas4.get(6)),
                new Respuesta(null, "Arwen", false, preguntas4.get(6)),
                new Respuesta(null, "Eowyn", false, preguntas4.get(6)),
                new Respuesta(null, "Lúthien", false, preguntas4.get(6)),

                // Respuestas para la pregunta 38
                new Respuesta(null, "Legolas", true, preguntas4.get(7)),
                new Respuesta(null, "Faramir", false, preguntas4.get(7)),
                new Respuesta(null, "Boromir", false, preguntas4.get(7)),
                new Respuesta(null, "Eomer", false, preguntas4.get(7)),

                // Respuestas para la pregunta 39
                new Respuesta(null, "Denethor", true, preguntas4.get(8)),
                new Respuesta(null, "Faramir", false, preguntas4.get(8)),
                new Respuesta(null, "Elendil", false, preguntas4.get(8)),
                new Respuesta(null, "Isildur", false, preguntas4.get(8)),

                // Respuestas para la pregunta 40 (ronda final)
                new Respuesta(null, "Aragorn", true, preguntas4.get(9)),
                new Respuesta(null, "Boromir", false, preguntas4.get(9)),
                new Respuesta(null, "Faramir", false, preguntas4.get(9)),
                new Respuesta(null, "Elessar", false, preguntas4.get(9))
        );

        // Guardar todas las preguntas y respuestas de la categoría 4
        preguntaRepository.saveAll(preguntas4);
        respuestaRepository.saveAll(respuestas4);

        // Preguntas y respuestas para la categoría 5: Pueblos y Lenguas de la Tierra Media
        List<Pregunta> preguntas5 = Arrays.asList(
                new Pregunta(null, "¿Cuál es el idioma de los elfos de Rivendel?", true, false, categorias.get(4), null),  // Quesito
                new Pregunta(null, "¿Cuál es el nombre de la lengua de Mordor?", false, false, categorias.get(4), null),
                new Pregunta(null, "¿Qué pueblo habitaba en la Comarca?", false, false, categorias.get(4), null),
                new Pregunta(null, "¿Quiénes eran los Dúnedain?", false, false, categorias.get(4), null),
                new Pregunta(null, "¿Qué lengua hablan en Gondor?", true, false, categorias.get(4), null),  // Quesito
                new Pregunta(null, "¿A qué pueblo pertenece Legolas?", false, false, categorias.get(4), null),
                new Pregunta(null, "¿Cuál es el idioma de los Enanos?", false, false, categorias.get(4), null),
                new Pregunta(null, "¿Cómo se llama el dialecto común en la Tierra Media?", false, false, categorias.get(4), null),
                new Pregunta(null, "¿Qué lengua usaba Saruman?", false, false, categorias.get(4), null),
                new Pregunta(null, "¿Qué pueblo es conocido como los hijos de Ilúvatar?", false, true, categorias.get(4), null)  // Ronda Final
        );

        List<Respuesta> respuestas5 = Arrays.asList(
                // Respuestas para la pregunta 41 (quesito)
                new Respuesta(null, "Quenya", false, preguntas5.get(0)),
                new Respuesta(null, "Sindarin", true, preguntas5.get(0)),
                new Respuesta(null, "Westron", false, preguntas5.get(0)),
                new Respuesta(null, "Rohirrico", false, preguntas5.get(0)),

                // Respuestas para la pregunta 42
                new Respuesta(null, "La Lengua Negra", true, preguntas5.get(1)),
                new Respuesta(null, "Quenya", false, preguntas5.get(1)),
                new Respuesta(null, "Sindarin", false, preguntas5.get(1)),
                new Respuesta(null, "Khuzdul", false, preguntas5.get(1)),

                // Respuestas para la pregunta 43
                new Respuesta(null, "Los Hobbits", true, preguntas5.get(2)),
                new Respuesta(null, "Los Enanos", false, preguntas5.get(2)),
                new Respuesta(null, "Los Elfos", false, preguntas5.get(2)),
                new Respuesta(null, "Los Hombres", false, preguntas5.get(2)),

                // Respuestas para la pregunta 44
                new Respuesta(null, "Una tribu de elfos", false, preguntas5.get(3)),
                new Respuesta(null, "Descendientes de Númenor", true, preguntas5.get(3)),
                new Respuesta(null, "Un pueblo de orcos", false, preguntas5.get(3)),
                new Respuesta(null, "Habitantes de Mordor", false, preguntas5.get(3)),

                // Respuestas para la pregunta 45 (quesito)
                new Respuesta(null, "Sindarin", false, preguntas5.get(4)),
                new Respuesta(null, "Quenya", false, preguntas5.get(4)),
                new Respuesta(null, "Westron", true, preguntas5.get(4)),
                new Respuesta(null, "Rohirrico", false, preguntas5.get(4)),

                // Respuestas para la pregunta 46
                new Respuesta(null, "Los Elfos de la Casa de Fingolfin", false, preguntas5.get(5)),
                new Respuesta(null, "Los Elfos Sindar", false, preguntas5.get(5)),
                new Respuesta(null, "Los Elfos Silvanos", true, preguntas5.get(5)),
                new Respuesta(null, "Los Elfos de Rivendel", false, preguntas5.get(5)),

                // Respuestas para la pregunta 47
                new Respuesta(null, "Khuzdul", true, preguntas5.get(6)),
                new Respuesta(null, "Quenya", false, preguntas5.get(6)),
                new Respuesta(null, "Sindarin", false, preguntas5.get(6)),
                new Respuesta(null, "La Lengua Negra", false, preguntas5.get(6)),

                // Respuestas para la pregunta 48
                new Respuesta(null, "Quenya", false, preguntas5.get(7)),
                new Respuesta(null, "Westron", true, preguntas5.get(7)),
                new Respuesta(null, "Sindarin", false, preguntas5.get(7)),
                new Respuesta(null, "Adûnaico", false, preguntas5.get(7)),

                // Respuestas para la pregunta 49
                new Respuesta(null, "Quenya", false, preguntas5.get(8)),
                new Respuesta(null, "La Lengua Negra", true, preguntas5.get(8)),
                new Respuesta(null, "Westron", false, preguntas5.get(8)),
                new Respuesta(null, "Sindarin", false, preguntas5.get(8)),

                // Respuestas para la pregunta 50 (ronda final)
                new Respuesta(null, "Los Elfos", false, preguntas5.get(9)),
                new Respuesta(null, "Los Hombres", true, preguntas5.get(9)),
                new Respuesta(null, "Los Enanos", false, preguntas5.get(9)),
                new Respuesta(null, "Los Ents", false, preguntas5.get(9))
        );

        // Guardar todas las preguntas y respuestas de la categoría 5
        preguntaRepository.saveAll(preguntas5);
        respuestaRepository.saveAll(respuestas5);

        // Preguntas y respuestas para la categoría 6: Tolkien Vida y Obra
        List<Pregunta> preguntas6 = Arrays.asList(
                new Pregunta(null, "¿Cuál fue la primera novela de Tolkien publicada?", false, false, categorias.get(5), null),
                new Pregunta(null, "¿En qué año se publicó 'El Señor de los Anillos'?", true, false, categorias.get(5), null),  // Quesito
                new Pregunta(null, "¿Cuál es el nombre completo de Tolkien?", false, false,categorias.get(5), null),
                new Pregunta(null, "¿En qué universidad enseñó Tolkien?", false, false, categorias.get(5), null),
                new Pregunta(null, "¿Cuál es el título del libro que narra los inicios de Arda?", true, false, categorias.get(5), null),  // Quesito
                new Pregunta(null, "¿A qué grupo literario pertenecía Tolkien?", false, false, categorias.get(5), null),
                new Pregunta(null, "¿Qué obra fue publicada póstumamente en 1977?", false, false, categorias.get(5), null),
                new Pregunta(null, "¿Cómo se llama el poema sobre Túrin Turambar?", false, false, categorias.get(5), null),
                new Pregunta(null, "¿Cuál fue la profesión principal de Tolkien?", false, false, categorias.get(5), null),
                new Pregunta(null, "¿Qué libro de Tolkien fue adaptado primero al cine?", false, true, categorias.get(5), null)  // Ronda Final
        );

        List<Respuesta> respuestas6 = Arrays.asList(
                // Respuestas para la pregunta 51
                new Respuesta(null, "El Hobbit", true, preguntas6.get(0)),
                new Respuesta(null, "El Señor de los Anillos", false, preguntas6.get(0)),
                new Respuesta(null, "El Silmarillion", false, preguntas6.get(0)),
                new Respuesta(null, "Los Hijos de Húrin", false, preguntas6.get(0)),

                // Respuestas para la pregunta 52 (quesito)
                new Respuesta(null, "1954-1955", true, preguntas6.get(1)),
                new Respuesta(null, "1937", false, preguntas6.get(1)),
                new Respuesta(null, "1965", false, preguntas6.get(1)),
                new Respuesta(null, "1977", false, preguntas6.get(1)),

                // Respuestas para la pregunta 53
                new Respuesta(null, "John Ronald Reuel Tolkien", true, preguntas6.get(2)),
                new Respuesta(null, "James Ronald Tolkien", false, preguntas6.get(2)),
                new Respuesta(null, "John Robert Tolkien", false, preguntas6.get(2)),
                new Respuesta(null, "Ronald James Tolkien", false, preguntas6.get(2)),

                // Respuestas para la pregunta 54
                new Respuesta(null, "Universidad de Cambridge", false, preguntas6.get(3)),
                new Respuesta(null, "Universidad de Birmingham", false, preguntas6.get(3)),
                new Respuesta(null, "Universidad de Oxford", true, preguntas6.get(3)),
                new Respuesta(null, "Universidad de Londres", false, preguntas6.get(3)),

                // Respuestas para la pregunta 55 (quesito)
                new Respuesta(null, "El Silmarillion", true, preguntas6.get(4)),
                new Respuesta(null, "El Hobbit", false, preguntas6.get(4)),
                new Respuesta(null, "Los Cuentos Inconclusos", false, preguntas6.get(4)),
                new Respuesta(null, "Los Hijos de Húrin", false, preguntas6.get(4)),

                // Respuestas para la pregunta 56
                new Respuesta(null, "The Club of Oxford", false, preguntas6.get(5)),
                new Respuesta(null, "The Inkling Society", false, preguntas6.get(5)),
                new Respuesta(null, "The Inklings", true, preguntas6.get(5)),
                new Respuesta(null, "The Literary Circle", false, preguntas6.get(5)),

                // Respuestas para la pregunta 57
                new Respuesta(null, "El Hobbit", false, preguntas6.get(6)),
                new Respuesta(null, "El Silmarillion", true, preguntas6.get(6)),
                new Respuesta(null, "El Señor de los Anillos", false, preguntas6.get(6)),
                new Respuesta(null, "Los Cuentos Inconclusos", false, preguntas6.get(6)),

                // Respuestas para la pregunta 58
                new Respuesta(null, "La Balada de Beren y Lúthien", false, preguntas6.get(7)),
                new Respuesta(null, "La Balada de los Hijos de Húrin", true, preguntas6.get(7)),
                new Respuesta(null, "La Historia de Arda", false, preguntas6.get(7)),
                new Respuesta(null, "El Fin de los Días", false, preguntas6.get(7)),

                // Respuestas para la pregunta 59
                new Respuesta(null, "Escritor", false, preguntas6.get(8)),
                new Respuesta(null, "Lingüista y filólogo", true, preguntas6.get(8)),
                new Respuesta(null, "Poeta", false, preguntas6.get(8)),
                new Respuesta(null, "Historiador", false, preguntas6.get(8)),

                // Respuestas para la pregunta 60 (ronda final)
                new Respuesta(null, "El Hobbit", false, preguntas6.get(9)),
                new Respuesta(null, "El Señor de los Anillos", true, preguntas6.get(9)),
                new Respuesta(null, "El Silmarillion", false, preguntas6.get(9)),
                new Respuesta(null, "Los Cuentos Inconclusos", false, preguntas6.get(9))
        );

        // Guardar todas las preguntas y respuestas de la categoría 6
        preguntaRepository.saveAll(preguntas6);
        respuestaRepository.saveAll(respuestas6);

    }
}