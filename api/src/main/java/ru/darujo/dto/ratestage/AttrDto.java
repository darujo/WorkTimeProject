package ru.darujo.dto.ratestage;

public class AttrDto<T> {
    @SuppressWarnings("unused")
    public AttrDto() {
    }

    private  T codeInt;
    private  String code;
    private  String value;

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
