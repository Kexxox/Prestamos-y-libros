package mx.edu.utez.Prestamos_Libros.repository.libro;

import mx.edu.utez.Prestamos_Libros.model.libro.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository
        extends JpaRepository<Libro, Long> {
}