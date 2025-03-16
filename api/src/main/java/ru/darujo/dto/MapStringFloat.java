package ru.darujo.dto;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class MapStringFloat implements Serializable {
    Map<String,Float> list;
    public MapStringFloat() {
        list = new HashMap<>();
    }

    public void setList(Map<String,Float> list) {
        this.list = list;
    }

    public Map<String,Float> getList() {
        return list;
    }
}
