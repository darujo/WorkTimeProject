package ru.darujo.dto;


import java.awt.print.Pageable;

public class PageObjDto<T>{
    int totalPages;
    int number;
    int size;
    T Content;

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
    public T getContent() {
        return Content;
    }

    public PageObjDto(int totalPages, int number, int size, T content) {
        this.totalPages = totalPages;
        this.number = number;
        this.size = size;
        Content = content;
    }
}
