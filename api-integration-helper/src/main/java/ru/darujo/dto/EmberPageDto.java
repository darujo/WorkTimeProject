package ru.darujo.dto;

public class EmberPageDto {
    @SuppressWarnings("unused")
    public EmberPageDto() {
    }

    private Integer totalPages;
    private Integer number;
    private Integer size;
    private Long totalElements;

    @SuppressWarnings("unused")
    public Integer getTotalPages() {
        return totalPages;
    }

    @SuppressWarnings("unused")
    public Integer getNumber() {
        return number;
    }

    @SuppressWarnings("unused")
    public Integer getSize() {
        return size;
    }

    @SuppressWarnings("unused")
    public Long getTotalElements() {
        return totalElements;
    }


    @SuppressWarnings("unused")
    public EmberPageDto(Integer totalPages, Integer number, Integer size, Long totalElements) {
        this.totalPages = totalPages;
        this.number = number;
        this.size = size;
        this.totalElements = totalElements;
    }
}
