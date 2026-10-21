package mx.edu.utez.Prestamos_Libros.service.lector;

import mx.edu.utez.Prestamos_Libros.exception.customExceptions.ResourceNotFoundException;
import mx.edu.utez.Prestamos_Libros.model.lector.Lector;
import mx.edu.utez.Prestamos_Libros.repository.lector.LectorRepository;
import org.springframework.stereotype.Service;

@Service
public class LectorService {

    private final LectorRepository lectorRepository;

    public LectorService(LectorRepository lectorRepository) {
        this.lectorRepository = lectorRepository;
    }

    public Lector rehabilitarLector(Long lectorId) {
        Lector lector = lectorRepository.findById(lectorId)
                .orElseThrow(() -> new ResourceNotFoundException("El lector con ID " + lectorId + " no existe."));

        lector.setActivo(true);
        return lectorRepository.save(lector);
    }
}