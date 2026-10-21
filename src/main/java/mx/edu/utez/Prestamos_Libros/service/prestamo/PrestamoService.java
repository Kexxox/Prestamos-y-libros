package mx.edu.utez.Prestamos_Libros.service.prestamo;

import mx.edu.utez.Prestamos_Libros.controller.dto.PrestamoRequestDTO;
import mx.edu.utez.Prestamos_Libros.exception.customExceptions.BadRequestException;
import mx.edu.utez.Prestamos_Libros.exception.customExceptions.ResourceNotFoundException;
import mx.edu.utez.Prestamos_Libros.model.lector.Lector;
import mx.edu.utez.Prestamos_Libros.model.libro.Libro;
import mx.edu.utez.Prestamos_Libros.model.prestamo.EstadoPrestamo;
import mx.edu.utez.Prestamos_Libros.model.prestamo.Prestamo;
import mx.edu.utez.Prestamos_Libros.repository.lector.LectorRepository;
import mx.edu.utez.Prestamos_Libros.repository.libro.LibroRepository;
import mx.edu.utez.Prestamos_Libros.repository.prestamo.PrestamoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class PrestamoService {

    private final PrestamoRepository prestamoRepository;
    private final LectorRepository lectorRepository;
    private final LibroRepository libroRepository;

    public PrestamoService(PrestamoRepository prestamoRepository,
                           LectorRepository lectorRepository,
                           LibroRepository libroRepository) {
        this.prestamoRepository = prestamoRepository;
        this.lectorRepository = lectorRepository;
        this.libroRepository = libroRepository;
    }

    public Prestamo solicitarPrestamo(PrestamoRequestDTO request) {
        Lector lector = lectorRepository.findById(request.getLectorId())
                .orElseThrow(() -> new ResourceNotFoundException("El lector no existe."));

        if (!lector.isActivo()) {
            throw new BadRequestException("El lector está inactivo/bloqueado.");
        }

        Libro libro = libroRepository.findById(request.getLibroId())
                .orElseThrow(() -> new ResourceNotFoundException("El libro no existe."));

        if (libro.getEjemplaresDisponibles() < 1) {
            throw new BadRequestException("El libro no tiene ejemplares disponibles.");
        }

        long prestamosActivos = prestamoRepository.contarByLectorIdAndEstadoIn(
                lector.getId(),
                List.of(EstadoPrestamo.PRESTADO, EstadoPrestamo.VENCIDO)
        );

        int limitePermitido = lector.isMembresiaPremium() ? 5 : 3;
        if (prestamosActivos >= limitePermitido) {
            throw new BadRequestException("El lector alcanzó el límite máximo de préstamos activos (" + limitePermitido + ").");
        }

        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() - 1);
        libroRepository.save(libro);

        Prestamo prestamo = new Prestamo();
        prestamo.setLector(lector);
        prestamo.setLibro(libro);
        prestamo.setFechaPrestamo(LocalDate.now());
        prestamo.setFechaDevolucionMaxima(LocalDate.now().plusDays(14));
        prestamo.setEstado(EstadoPrestamo.PRESTADO);

        return prestamoRepository.save(prestamo);
    }

    public Prestamo devolverLibro(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ResourceNotFoundException("El préstamo no existe."));

        if (prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new BadRequestException("El préstamo ya fue devuelto anteriormente.");
        }

        boolean entregaTardia = LocalDate.now().isAfter(prestamo.getFechaDevolucionMaxima())
                || prestamo.getEstado() == EstadoPrestamo.VENCIDO;

        prestamo.setEstado(EstadoPrestamo.DEVUELTO);

        Libro libro = prestamo.getLibro();
        libro.setEjemplaresDisponibles(libro.getEjemplaresDisponibles() + 1);
        libroRepository.save(libro);

        if (entregaTardia) {
            Lector lector = prestamo.getLector();
            lector.setActivo(false);
            lectorRepository.save(lector);
        }

        return prestamoRepository.save(prestamo);
    }

    public Prestamo renovarPrestamo(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new ResourceNotFoundException("El préstamo no existe."));

        if (prestamo.getEstado() == EstadoPrestamo.VENCIDO || prestamo.getEstado() == EstadoPrestamo.DEVUELTO) {
            throw new BadRequestException("No se puede renovar un préstamo que se encuentra VENCIDO o DEVUELTO.");
        }

        prestamo.setFechaDevolucionMaxima(prestamo.getFechaDevolucionMaxima().plusDays(7));
        return prestamoRepository.save(prestamo);
    }

    public List<Prestamo> listarPrestamosActivosLector(Long lectorId) {
        if (!lectorRepository.existsById(lectorId)) {
            throw new ResourceNotFoundException("El lector no existe.");
        }

        return prestamoRepository.encontrarByLectorIdAndEstadoIn(
                lectorId,
                List.of(EstadoPrestamo.PRESTADO, EstadoPrestamo.VENCIDO)
        );
    }
}