package cr.ac.una.backend.controller;

import cr.ac.una.backend.entity.Clima;
import cr.ac.una.backend.service.ClimaService;
import cr.ac.una.backend.service.TareaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/clima")
public class ClimaController {

    @Autowired
    private ClimaService climaService;

    @Autowired
    private TareaService tareaService;

    // Obtener el clima en una fecha y hora específica
    @GetMapping("/por-hora")
    public List<Clima> obtenerClimaPorHora(@RequestParam Date hora) {
        return climaService.obtenerClimaPorHora(hora);
    }

    // Obtener el historial de clima entre dos fechas
    @GetMapping("/historial")
    public List<Clima> obtenerHistorialClima(@RequestParam Date start, @RequestParam Date end) {
        return climaService.obtenerHistorialClima(start, end);
    }

    // Agregar una nueva entrada de clima
    @PostMapping("/nuevo")
    public Clima agregarClima(@RequestBody Clima clima) {
        return climaService.agregarClima(clima);
    }

    // Obtener la condición climática actual
    @GetMapping("/actual")
    public Clima obtenerClimaActual() {
        return climaService.obtenerClimaActual();
    }

    // Registrar una nueva condición climática y activar replanificación
    @PostMapping("/registrar")
    public ResponseEntity<?> registrarCondicionClimatica(@RequestBody Clima clima) {
        try {
            climaService.agregarClima(clima);

            // Activar replanificación automática
            List<String> nuevoPlan = tareaService.replanificarSiCambiaClima(clima.getTipo());
            return ResponseEntity.ok(nuevoPlan);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al registrar la condición climática y replanificar: " + e.getMessage());
        }
    }
}
