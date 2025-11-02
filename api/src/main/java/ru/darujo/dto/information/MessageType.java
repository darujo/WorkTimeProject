package ru.darujo.dto.information;

public enum MessageType {
    SYSTEM_INFO("Системные информационые сообщения"),
    ESTIMATION_WORK("Проведена оценка"),
    CHANGE_STAGE_WORK("Смена статуса ЗИ"),
    UPDATE_INFO("Список исправлений в новой версии "),
    AVAIL_WORK_LAST_DAY("Работы отмеченые вами за предыдущий рабочий день"),
    AVAIL_WORK_LAST_WEEK("Работы отмеченые вами за предыдущей неделю");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
