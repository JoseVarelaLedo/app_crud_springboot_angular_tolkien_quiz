package app.rest.controller;

import app.model.Categoria;
import app.exceptions.ResourceNotFoundException;
import app.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/categorias")
@CrossOrigin (origins = "http://localhost:4200/")
public class CategoriaRestController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    private ResponseEntity <Page<Categoria>> getAll(
            @RequestParam(defaultValue = "0") int pag,
            @RequestParam(defaultValue = "7") int tam,
            @RequestParam(defaultValue = "id") String campoOrdenacion,
            @RequestParam(defaultValue = "asc") String direccionOrdenacion){
        return ResponseEntity.ok(categoriaService.listarCategorias(pag, tam, campoOrdenacion, direccionOrdenacion));
    }

    @GetMapping("/noPag")
    private ResponseEntity<List<Categoria>> getAllNoPag(){
        return ResponseEntity.ok(categoriaService.listarCategorias());
    }

    @GetMapping ("/buscarCategoriaPorId/id/{id}")
    public ResponseEntity<Categoria> getCategoriaPorId(@PathVariable Long id){
        try{
            Categoria categoria = categoriaService.obtenerCategoriaPorId(id);
            return ResponseEntity.ok (categoria);
        }catch (ResourceNotFoundException ex){
            throw new ResourceNotFoundException("Categoria " + id + " no encontrada");
        }
    }

    @PostMapping ("/crearCategoria")
    public ResponseEntity<Categoria> crearCategoria(@RequestBody Categoria categoria) {
        try {
            categoriaService.crearCategoria(categoria);
            return new ResponseEntity<>(categoria, HttpStatus.CREATED);                 // retorna 201 CREATED
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);        // si ocurre un error, retorna 500
        }
    }

    @PutMapping("/editar/id/{id}")
    public ResponseEntity<Categoria> updateCategoria(@PathVariable Long id, @RequestBody Categoria datosActualizados) {
        try {
            Categoria categoriaExistente = categoriaService.obtenerCategoriaPorId(id);

            Categoria categoriaActualizada = Categoria.builder()                    // se crea un nuevo objeto combinando valores viejos y nuevos usando Builder de Lombok
                    .id(categoriaExistente.getId())                                 // se mantiene el mismo ID
                    .categoriaDesc(datosActualizados.getCategoriaDesc() != null ? datosActualizados.getCategoriaDesc() : categoriaExistente.getCategoriaDesc())
                    .build();

            categoriaService.actualizarCategoria(categoriaActualizada);
            return ResponseEntity.ok(categoriaActualizada);
        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Categoria " + id + " no encontrada");
        }
    }

    @DeleteMapping("/eliminar/id/{id}")
    public ResponseEntity<Map<String, Boolean>> borrarCategoria(@PathVariable Long id) {
        try {
            Categoria categoria = categoriaService.obtenerCategoriaPorId(id);
            if (categoria == null) {
                throw new ResourceNotFoundException("Categoria " + id + " no encontrada");
            }
            categoriaService.borrarCategoria(id);

            Map<String, Boolean> response = new HashMap<>();   // respuesta de éxito
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException ex) {
            throw new ResourceNotFoundException("Categoria " + id + " no encontrada");
        }
    }
}
