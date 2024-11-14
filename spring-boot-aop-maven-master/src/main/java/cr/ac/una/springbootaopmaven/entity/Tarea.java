package cr.ac.una.springbootaopmaven.entity;

import cr.ac.una.springbootaopmaven.enumeration.Estado;
import cr.ac.una.springbootaopmaven.enumeration.Prioridad;
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
    @JoinColumn(name = "schedule_id") // Arreglar este campo
    private Horario horario;

    @OneToMany(mappedBy = "tarea", cascade = CascadeType.ALL)
    private List<Dependencia> dependencias;
}
