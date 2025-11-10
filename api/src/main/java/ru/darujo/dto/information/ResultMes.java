package ru.darujo.dto.information;

import java.io.Serializable;

public class ResultMes implements Serializable {
    @SuppressWarnings("unused")
    public ResultMes() {
    }

    Boolean ok;
    String message;

    public ResultMes(Boolean ok, String message) {
        this.ok = ok;
        this.message = message;
    }

    public Boolean isOk() {
        return ok;
    }

    public String getMessage() {
        return message;
    }
}
