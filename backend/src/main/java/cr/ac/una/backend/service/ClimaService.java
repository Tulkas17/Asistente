package cr.ac.una.backend.service;

import cr.ac.una.backend.entity.Clima;
import cr.ac.una.backend.repository.ClimaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ClimaService {

    @Autowired
    private ClimaRepository climaRepository;

    // Obtener el clima en una fecha y hora específica
    public List<Clima> obtenerClimaPorHora(Date hora) {
        return climaRepository.findByHora(hora);
    }

    // Obtener el historial de clima entre dos fechas específicas
    public List<Clima> obtenerHistorialClima(Date start, Date end) {
        return climaRepository.findByHoraBetween(start, end);
    }

    // Agregar una nueva entrada de clima al historial
    public Clima agregarClima(Clima clima) {
        return climaRepository.save(clima);
    }

    // Obtener el clima actual (último registro en la base de datos)
    public Clima obtenerClimaActual() {
        return climaRepository.findTopByOrderByHoraDesc()
                .orElseThrow(() -> new RuntimeException("No se encontró una condición climática actual."));
    }
}
