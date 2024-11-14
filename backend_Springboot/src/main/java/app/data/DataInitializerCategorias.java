package app.data;

import app.model.Categoria;
import app.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import java.util.Arrays;
import java.util.List;

@Component
@Order(2)
public class DataInitializerCategorias {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Bean
    public ApplicationRunner initializerCategorias(CategoriaRepository categoriaRepository) {
        return args -> {
            if (categoriaRepository.count() == 0) {
                insertarCategorias(this.categoriaRepository);
            }
        };
    }

    public void insertarCategorias(CategoriaRepository categoriaRepository) {
        List<Categoria> categorias = Arrays.asList(
                new Categoria(null, "Historia de la Tierra Media"),
                new Categoria(null, "Historia Antigua de Arda"),
                new Categoria(null, "Geografía de Arda"),
                new Categoria(null, "Personajes Ilustres"),
                new Categoria(null, "Pueblos y Lenguas de la Tierra Media"),
                new Categoria(null, "Tolkien Vida y Obra")
        );
        categoriaRepository.saveAll(categorias);
    }
}