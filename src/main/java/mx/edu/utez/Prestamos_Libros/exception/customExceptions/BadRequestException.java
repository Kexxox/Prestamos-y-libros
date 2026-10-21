package mx.edu.utez.Prestamos_Libros.exception.customExceptions;

public class BadRequestException extends RuntimeException {

    public BadRequestException(String message) {
        super(message);
    }
}