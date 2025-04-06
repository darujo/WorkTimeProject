package ru.darujo.dto.ratestage;

public class AttrDto {
    private Float codeInt;
    private String code;
    private String value;

    public AttrDto(Float codeInt, String value) {
        this.codeInt = codeInt;
        this.code = codeInt.toString();
        this.value = value;
    }

    public Float getCodeInt() {
        return codeInt;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
