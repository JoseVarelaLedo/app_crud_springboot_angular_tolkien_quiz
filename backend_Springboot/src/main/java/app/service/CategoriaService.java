package app.service;

import app.model.Categoria;
import app.repository.CategoriaRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Page;

import java.util.List;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository categoriaRepository;

    public Page<Categoria> listarCategorias (int pag, int tam, String campoOrdenacion, String direccionOrdenacion){
        Sort.Direction direccion = direccionOrdenacion.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(pag, tam, Sort.by(direccion, campoOrdenacion));
        return categoriaRepository.findAll(pageable);
    }

    public List<Categoria> listarCategorias(){
        return categoriaRepository.findAll();
    }

    @Transactional
    public void crearCategoria (Categoria categoria) {
        categoriaRepository.save (categoria);
    }

    public Categoria obtenerCategoriaPorId (Long id){
        return categoriaRepository.findById(id).orElseGet(null);
    }

    @Transactional
    public void actualizarCategoria (Categoria categoria){
        categoriaRepository.save(categoria);
    }

    public void borrarCategoria (Long id){
        categoriaRepository.deleteById(id);
    }

}
