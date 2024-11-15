package cr.ac.una.backend.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import cr.ac.una.backend.enumeration.CondicionClimatica;
import cr.ac.una.backend.enumeration.Estado;
import cr.ac.una.backend.enumeration.Prioridad;
import jakarta.persistence.*;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    @NotBlank(message = "El nombre de la tarea es obligatorio.")
    private String nombre;

    @NotNull(message = "La prioridad es obligatoria.")
    @Enumerated(EnumType.STRING)
    private Prioridad prioridad;

    @Min(value = 1, message = "El tiempo estimado debe ser al menos 1 minuto.")
    private float tiempoEstimado;

    @FutureOrPresent(message = "La fecha límite debe ser en el presente o en el futuro.")
    private LocalDateTime fechaLimite;

    @NotNull(message = "El estado de la tarea es obligatorio.")
    @Enumerated(EnumType.STRING)
    private Estado estado = Estado.PENDIENTE; // Estado predeterminado si no se especifica

    @FutureOrPresent(message = "La fecha de inicio debe ser en el presente o en el futuro.")
    private LocalDateTime fechaInicio;

    @OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<Dependencia> dependencias = new ArrayList<>();

    @ElementCollection(targetClass = CondicionClimatica.class)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "tarea_condiciones_climaticas", joinColumns = @JoinColumn(name = "tarea_id"))
    @Column(name = "condicion_climatica")
    private List<CondicionClimatica> condicionesClimaticas;

    // Constructor parcial para solo el id
    public Tarea(Long id) {
        this.id = id;
    }

}
