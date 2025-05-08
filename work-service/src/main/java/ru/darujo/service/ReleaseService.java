package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.model.Release;
import ru.darujo.repository.ReleaseRepository;

import java.util.List;

@Service
public class ReleaseService {

    private ReleaseRepository releaseRepository;

    @Autowired
    public void setReleaseRepository(ReleaseRepository releaseRepository) {
        this.releaseRepository = releaseRepository;
    }


    public Release findById(long id) {
        return releaseRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Релиз с id " + id  + " не найден."));
    }

    public Release saveRelease(Release release) {
        return releaseRepository.save(release);
    }

    public void deleteRelease(Long id) {
        releaseRepository.deleteById(id);
    }

    public List<Release> findAll(
    ) {
        Specification<Release> specification = Specification.where(null);
        return releaseRepository.findAll(specification, Sort.by("name"));
    }
}
