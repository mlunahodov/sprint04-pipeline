package io.github.Guimaraes131.vroom.motorcycle;

import io.github.Guimaraes131.vroom.motorcycle.enums.MotorcycleModel;
import io.github.Guimaraes131.vroom.motorcycle.enums.ProblemCategory;
import io.github.Guimaraes131.vroom.tag.Tag;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_motorcycle")
public class Motorcycle {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "license_plate")
    private String licensePlate;

    private String chassis;

    @Column(name = "problem_description")
    private String problemDescription;

    @Enumerated(EnumType.STRING)
    private MotorcycleModel model;

    @Enumerated(EnumType.STRING)
    private ProblemCategory problem;

    @OneToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;
}
