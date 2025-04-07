package ru.darujo.properties;

import lombok.Data;

@Data
public abstract class ServiceProperty {
    private String url;
    private Integer connectionTimeOut;
    private Integer readTimeOut;
    private Integer writeTimeOut;
}
