package app.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Clase creada para lanzar excepciones personalizadas, con fines de depuración
 */
@ResponseStatus (value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException  extends RuntimeException{
    private static final long serialVersionID = 1;

    public ResourceNotFoundException (String mensaje){
        super(mensaje);
    }
}
