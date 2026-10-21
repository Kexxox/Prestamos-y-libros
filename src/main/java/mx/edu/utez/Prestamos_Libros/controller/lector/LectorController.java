package mx.edu.utez.Prestamos_Libros.controller.lector;

import mx.edu.utez.Prestamos_Libros.model.lector.Lector;
import mx.edu.utez.Prestamos_Libros.service.lector.LectorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/lector")
@CrossOrigin({"*"})
public class LectorController {

    private final LectorService lectorService;

    public LectorController(LectorService lectorService) {
        this.lectorService = lectorService;
    }

    @PostMapping("/{id}/rehabilitar")
    public ResponseEntity<Lector> rehabilitarLector(@PathVariable Long id) {
        return ResponseEntity.ok(this.lectorService.rehabilitarLector(id));
    }
}