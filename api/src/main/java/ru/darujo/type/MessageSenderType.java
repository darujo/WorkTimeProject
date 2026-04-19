package ru.darujo.type;

public enum MessageSenderType implements TypeEnum {
    Telegram("Телеграм"),
    Email("Почта");

    private final String name;

    MessageSenderType(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }
}
