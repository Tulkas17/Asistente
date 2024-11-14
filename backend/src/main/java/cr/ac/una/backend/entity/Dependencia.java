package cr.ac.una.backend.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "dependencias")
public class Dependencia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "tarea_id")
    @JsonBackReference
    private Tarea tarea;

    @ManyToOne
    @JoinColumn(name = "tarea_dependiente_id")
    private Tarea tareaDependiente;
}
