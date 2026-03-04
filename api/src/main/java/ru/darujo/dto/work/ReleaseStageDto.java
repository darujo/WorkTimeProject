package ru.darujo.dto.work;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ReleaseStageDto implements Comparable<ReleaseStageDto> {
    private Long id;
    // Порядковый номер релиза
    private String name;
    // Выдача релиза даты План
    private Float sort;

    @SuppressWarnings("unchecked")
    private final List<WorkLittleDto>[] works = new ArrayList[7];

    public ReleaseStageDto(Long id, String name, Float sort) {
        this.id = id;
        this.name = name;
        this.sort = sort;
        for (int i = 0; i < works.length; i++) {
            works[i] = new ArrayList<>();
        }

    }

    public List<WorkLittleDto>[] getWorks() {
        return works;
    }

    @SuppressWarnings("unused")
    public ReleaseStageDto() {
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("unused")
    public Float getSort() {
        return sort;
    }

    @Override
    public int compareTo(@NotNull ReleaseStageDto releaseStageDto) {
        if (sort == null) {
            if (releaseStageDto.getSort() == null) {
                return name.compareTo(releaseStageDto.getName());
            }
            return 1;
        } else {
            int result = sort.compareTo(releaseStageDto.getSort());
            if (result != 0) {
                return result;
            }
            return name.compareTo(releaseStageDto.getName());
        }

    }
}
