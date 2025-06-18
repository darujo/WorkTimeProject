package ru.darujo.dto;

import java.io.Serializable;

public abstract class ColorDto implements Serializable {
    @SuppressWarnings("unused")
    public abstract String getColor();
}
