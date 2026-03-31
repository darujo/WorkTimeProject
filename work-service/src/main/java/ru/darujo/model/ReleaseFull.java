package ru.darujo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReleaseFull {
    private Release release;
    private ReleaseProject releaseProject;

}
