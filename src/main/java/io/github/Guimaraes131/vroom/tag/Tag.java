package io.github.Guimaraes131.vroom.tag;

import io.github.Guimaraes131.vroom.motorcycle.Motorcycle;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tb_tag")
public class Tag {

    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String coordinate;

    private String color;

    @Column(name = "is_available")
    private Boolean isAvailable;

    @OneToOne(mappedBy = "tag")
    private Motorcycle motorcycle;

    public boolean isDeletable() {
        return this.isAvailable;
    }
}
