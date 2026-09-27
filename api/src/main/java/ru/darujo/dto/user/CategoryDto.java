package ru.darujo.dto.user;

public class CategoryDto {

    private Long id;
    private String name;

    @SuppressWarnings("unused")
    public CategoryDto() {
    }

    public CategoryDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
