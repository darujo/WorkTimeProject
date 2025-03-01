package ru.darujo.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class ListString implements Serializable {
    Set<String> list;
    public ListString() {
        list = new HashSet<>();
    }

    public void setList(Set<String> list) {
        this.list = list;
    }

    public Set<String> getList() {
        return list;
    }
}
