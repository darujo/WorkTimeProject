package ru.darujo.properties;

public interface PropertyConnectionInterface {
    Integer getConnectionTimeOut();
    Integer getReadTimeOut();

    Integer getWriteTimeOut();

    String getUrl();
}
