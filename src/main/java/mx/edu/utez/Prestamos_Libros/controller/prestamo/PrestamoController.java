package mx.edu.utez.Prestamos_Libros.controller.prestamo;

import jakarta.validation.Valid;
import mx.edu.utez.Prestamos_Libros.controller.dto.PrestamoRequestDTO;
import mx.edu.utez.Prestamos_Libros.model.prestamo.Prestamo;
import mx.edu.utez.Prestamos_Libros.service.prestamo.PrestamoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/prestamo")
@CrossOrigin({"*"})
public class PrestamoController {

    private final PrestamoService prestamoService;

    public PrestamoController(PrestamoService prestamoService) {
        this.prestamoService = prestamoService;
    }

    @PostMapping("/solicitar")
    public ResponseEntity<Prestamo> solicitarPrestamo(@RequestBody @Valid PrestamoRequestDTO datos) {
        return new ResponseEntity<>(this.prestamoService.solicitarPrestamo(datos), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/devolver")
    public ResponseEntity<Prestamo> devolverLibro(@PathVariable Long id) {
        return ResponseEntity.ok(this.prestamoService.devolverLibro(id));
    }

    @PostMapping("/{id}/renovar")
    public ResponseEntity<Prestamo> renovarPrestamo(@PathVariable Long id) {
        return ResponseEntity.ok(this.prestamoService.renovarPrestamo(id));
    }

    @GetMapping("/lector/{lectorId}/activos")
    public ResponseEntity<List<Prestamo>> listarPrestamosActivosLector(@PathVariable Long lectorId) {
        return ResponseEntity.ok(this.prestamoService.listarPrestamosActivosLector(lectorId));
    }
}