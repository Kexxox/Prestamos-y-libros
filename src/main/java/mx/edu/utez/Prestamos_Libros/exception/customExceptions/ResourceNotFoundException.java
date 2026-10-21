package mx.edu.utez.Prestamos_Libros.exception.customExceptions;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}