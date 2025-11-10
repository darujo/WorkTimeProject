package ru.darujo.dto.information;

import java.io.Serializable;

public class CodeTelegramMes implements Serializable {
    @SuppressWarnings("unused")
    public CodeTelegramMes() {
    }

    private Boolean ok;
    private String link;

    private Integer code;
    private Integer time;

    public CodeTelegramMes(Boolean ok, String link,Integer code, Integer time) {
        this.ok = ok;
        this.link = link;
        this.code = code;
        this.time = time;
    }

    @SuppressWarnings("unused")
    public Boolean getOk() {
        return ok;
    }

    @SuppressWarnings("unused")
    public String getLink() {
        return link;
    }

    public Integer getCode() {
        return code;
    }

    public Integer getTime() {
        return time;
    }
}
