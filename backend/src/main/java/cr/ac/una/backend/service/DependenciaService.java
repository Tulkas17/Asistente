package cr.ac.una.backend.service;

import cr.ac.una.backend.entity.Dependencia;
import cr.ac.una.backend.entity.Tarea;
import cr.ac.una.backend.repository.DependenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DependenciaService {

    @Autowired
    private DependenciaRepository dependenciaRepository;

    // Crear una nueva dependencia entre tareas
    public void crearDependencia(Dependencia dependencia) {
        Tarea tareaPrincipal = dependencia.getTarea();
        Tarea tareaDependiente = dependencia.getTareaDependiente();

        // Validar antes de crear la dependencia
        if (verificarCiclo(tareaPrincipal, tareaDependiente)) {
            throw new IllegalStateException("Error: Crear esta dependencia generaría un bucle.");
        }

        dependenciaRepository.save(dependencia);
    }

    // Método para verificar evitar bucles
    private boolean verificarCiclo(Tarea tareaPrincipal, Tarea tareaDependiente) {
        // Si la tarea principal ya depende de la tarea dependiente se encicla
        if (tareaPrincipal.equals(tareaDependiente)) {
            return true;
        }

        // Obtener todas las dependencias de la tarea dependiente
        List<Dependencia> dependencias = dependenciaRepository.findByTareaDependiente(tareaDependiente);

        for (Dependencia dep : dependencias) {
            if (verificarCiclo(tareaPrincipal, dep.getTarea())) {
                return true;
            }
        }

        return false;
    }

    // Obtener todas las dependencias de una tarea específica
    public List<Dependencia> obtenerDependenciasPorTarea(Long tareaId) {
        Tarea tarea = new Tarea();
        tarea.setId(tareaId);
        return dependenciaRepository.findByTarea(tarea);
    }

    // Obtener todas las tareas de las que depende una tarea específica
    public List<Dependencia> obtenerDependenciasPorTareaDependiente(Long tareaDependienteId) {
        Tarea tareaDependiente = new Tarea();
        tareaDependiente.setId(tareaDependienteId);
        return dependenciaRepository.findByTareaDependiente(tareaDependiente);
    }
}
