package mx.edu.utez.Prestamos_Libros.repository.prestamo;

import mx.edu.utez.Prestamos_Libros.model.prestamo.EstadoPrestamo;
import mx.edu.utez.Prestamos_Libros.model.prestamo.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository
        extends JpaRepository<Prestamo, Long> {

    long contarByLectorIdAndEstadoIn(Long lectorId, List<EstadoPrestamo> estados);

    List<Prestamo> encontrarByLectorIdAndEstadoIn(Long lectorId, List<EstadoPrestamo> estados);
}