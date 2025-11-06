package io.github.Guimaraes131.vroom.motorcycle;


import io.github.Guimaraes131.vroom.motorcycle.dto.MotorcycleForm;
import io.github.Guimaraes131.vroom.tag.Tag;
import io.github.Guimaraes131.vroom.tag.TagRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MotorcycleService {

    private final MotorcycleRepository repository;
    private final TagRepository tagRepository;

    public Motorcycle toEntity(MotorcycleForm dto) {
        Tag tag = tagRepository.findById(dto.getTagId())
                .orElseThrow(() -> new RuntimeException("Tag não encontrada"));

        return Motorcycle.builder()
                .model(dto.getModel())
                .problemDescription(dto.getProblemDescription())
                .problem(dto.getProblem())
                .chassis(dto.getChassis())
                .licensePlate(dto.getLicensePlate())
                .tag(tag)
                .build();
    }

    public void create(Motorcycle motorcycle) {
        motorcycle.getTag().setIsAvailable(false);
        motorcycle.getTag().setColor(motorcycle.getProblem().getAssociatedColor());

        repository.save(motorcycle);
    }

    public Motorcycle getById(UUID id) {
        return repository.findById(id).orElseThrow(
                () -> new RuntimeException("Moto não encontrada")
        );
    }

    public void delete(UUID id) {
        var motorcycle = getById(id);
        var tag = tagRepository.findById(motorcycle.getTag().getId())
                .orElseThrow(
                        () -> new IllegalStateException("Tag não encontrada.")
                );

        tag.setIsAvailable(true);
        tag.setColor(null);
        tag.setMotorcycle(null);

        repository.delete(motorcycle);
    }

    public void update(UUID id, MotorcycleForm form) {
        Motorcycle motorcycle = getById(id);
        motorcycle.setLicensePlate(form.getLicensePlate());
        motorcycle.setModel(form.getModel());
        motorcycle.setChassis(form.getChassis());
        motorcycle.setProblem(form.getProblem());
        motorcycle.setProblemDescription(form.getProblemDescription());
        motorcycle.getTag().setColor(form.getProblem().getAssociatedColor());

        repository.save(motorcycle);
    }
}
