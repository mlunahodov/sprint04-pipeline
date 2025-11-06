package io.github.Guimaraes131.vroom.tag;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository repository;

    public List<Tag> index() {
        return repository.findAllByOrderByCoordinateAsc();
    }
}
