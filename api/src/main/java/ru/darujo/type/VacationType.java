package ru.darujo.type;

public enum VacationType implements TypeEnum {
    VACATION("Отпуск"),
    MEDICAL("Больничный"),
    TIME_OFF("Отгул");

    private final String name;

    VacationType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
