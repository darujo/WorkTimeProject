package ru.darujo.service;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.darujo.exceptions.ResourceNotFoundRunTime;
import ru.darujo.model.Release;
import ru.darujo.model.ReleaseFull;
import ru.darujo.repository.ReleaseRepository;
import ru.darujo.specifications.Specifications;

import java.util.List;
import java.util.Optional;

@Service
public class ReleaseService {

    private ReleaseRepository releaseRepository;
    private ReleaseProjectService releaseProjectService;

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

    public ReleaseFull getReleaseFull(Long id, Long projectId) {
        Release release = findById(id);
        return new ReleaseFull(release, releaseProjectService.findReleaseProject(release, projectId));
    }

    public Release saveRelease(Release release) {
        return releaseRepository.save(release);
    }

    public void deleteRelease(Long id) {
        releaseRepository.deleteById(id);
    }

    public List<ReleaseFull> findAll(List<Long> releaseIdList, Long projectId) {
        Specification<@NonNull Release> specification = Specification.unrestricted();
        specification = Specifications.in(specification, "id", releaseIdList);
        return releaseRepository.findAll(specification, Sort.by("sort").and(Sort.by("name")))
                .stream().map(release -> new ReleaseFull(release, projectId == null ? null : releaseProjectService.findReleaseProject(release, projectId)))
                .toList();
    }

    @Autowired
    public void setReleaseProjectService(ReleaseProjectService releaseProjectService) {
        this.releaseProjectService = releaseProjectService;
    }
}
