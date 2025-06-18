package ru.darujo.dto;

import java.util.List;

public class PageDto<T> {
    int totalPages;
    int number;
    int size;
    List<T> Content;

    @SuppressWarnings("unused")
    public int getTotalPages() {
        return totalPages;
    }

    @SuppressWarnings("unused")
    public int getNumber() {
        return number;
    }

    @SuppressWarnings("unused")
    public int getSize() {
        return size;
    }

    @SuppressWarnings("unused")
    public List<T> getContent() {
        return Content;
    }

    public PageDto(int totalPages, int number, int size, List<T> content) {
        this.totalPages = totalPages;
        this.number = number;
        this.size = size;
        Content = content;
    }
}
