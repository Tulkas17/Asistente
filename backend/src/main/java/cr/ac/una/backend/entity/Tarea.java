package cr.ac.una.backend.entity;

import cr.ac.una.backend.enumeration.Estado;
import cr.ac.una.backend.enumeration.Prioridad;
import cr.ac.una.backend.enumeration.CondicionClimatica;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tarea")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;

    private int tiempoEstimado;

    private Date fechaLimite;

    private String requisitos;

    @Enumerated(EnumType.STRING)
    private Estado estado;

    private Date horaInicio;

    @ManyToOne
    @JoinColumn(name = "schedule_id") // Campo para relacionar con el horario
    private Horario horario;

    @OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL)
    private List<Dependencia> dependencias;

    @ElementCollection(targetClass = CondicionClimatica.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "tarea_condiciones_climaticas", joinColumns = @JoinColumn(name = "tarea_id"))
    @Column(name = "condicion_climatica")
    private List<CondicionClimatica> condicionesClimaticas;

    // Métodos adicionales, como los agregados anteriormente...

    /**
     * Verifica si las condiciones climáticas actuales permiten realizar esta tarea.
     * @param climaActual Condición climática actual.
     * @return true si la tarea puede realizarse bajo el clima actual, false en caso contrario.
     */
    public boolean puedeRealizarseBajoCondicion(CondicionClimatica climaActual) {
        return condicionesClimaticas.contains(CondicionClimatica.INDEPENDIENTE) || condicionesClimaticas.contains(climaActual);
    }

    // Otros métodos de la clase, como los métodos de verificación de dependencias, estado, etc.
}
