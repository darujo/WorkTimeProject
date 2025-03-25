package ru.darujo.dto;

import java.util.List;

public class PageDto<T>  {
    int totalPages;
    int number;
    int size;
    List<T> Content;

    public int getTotalPages() {
        return totalPages;
    }

    public int getNumber() {
        return number;
    }

    public int getSize() {
        return size;
    }

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
