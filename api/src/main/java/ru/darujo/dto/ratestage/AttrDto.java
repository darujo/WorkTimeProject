package ru.darujo.dto.ratestage;

public class AttrDto<T> {
    @SuppressWarnings("unused")
    public AttrDto() {
    }

    private  T codeT;
    private  String code;
    private  String value;

    public AttrDto(T codeT, String value) {
        this.codeT = codeT;
        this.code = codeT.toString();
        this.value = value;
    }

    public T getCodeT() {
        return codeT;
    }
    public T getCodeInt() {
        return codeT;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }
}
