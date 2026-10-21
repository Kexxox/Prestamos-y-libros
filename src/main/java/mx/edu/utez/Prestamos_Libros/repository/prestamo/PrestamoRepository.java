package mx.edu.utez.Prestamos_Libros.repository.prestamo;

import mx.edu.utez.Prestamos_Libros.model.prestamo.EstadoPrestamo;
import mx.edu.utez.Prestamos_Libros.model.prestamo.Prestamo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {

    // Se agrega '_' para especificar la propiedad id de la entidad Lector
    long countByLector_IdAndEstadoIn(Long lectorId, List<EstadoPrestamo> estados);

    List<Prestamo> findByLector_IdAndEstadoIn(Long lectorId, List<EstadoPrestamo> estados);
}