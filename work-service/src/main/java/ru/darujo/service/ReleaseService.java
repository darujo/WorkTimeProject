package ru.darujo.service;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.Release;
import ru.darujo.repository.ReleaseRepository;
import ru.darujo.specifications.Specifications;

import java.util.List;
import java.util.Optional;

@Service
public class ReleaseService {

    private ReleaseRepository releaseRepository;

    @Autowired
    public void setReleaseRepository(ReleaseRepository releaseRepository) {
        this.releaseRepository = releaseRepository;
    }

    public Optional<Release> findOptionalById(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return releaseRepository.findById(id);
    }

    public Release findById(Long id) {
        if (id == null) {
            return null;
        }
        return findOptionalById(id).orElseThrow(() -> new ResourceNotFoundRunTime("Релиз с id " + id + " не найден."));
    }

    public Release saveRelease(Release release) {
        return releaseRepository.save(release);
    }

    public void deleteRelease(Long id) {
        releaseRepository.deleteById(id);
    }

    public List<Release> findAll(List<Long> releaseIdList) {
        Specification<@NonNull Release> specification = Specification.unrestricted();
        specification = Specifications.in(specification, "id", releaseIdList);
        return releaseRepository.findAll(specification, Sort.by("sort").and(Sort.by("name")));
    }
}
