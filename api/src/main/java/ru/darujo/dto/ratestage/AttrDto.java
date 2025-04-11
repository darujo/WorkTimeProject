package ru.darujo.dto.ratestage;

public class AttrDto<T> {
    private final T codeInt;
    private final String code;
    private final String value;

    public AttrDto(T codeInt, String value) {
        this.codeInt = codeInt;
        this.code = codeInt.toString();
        this.value = value;
    }

    public T getCodeInt() {
        return codeInt;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
