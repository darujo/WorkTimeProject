package ru.darujo.dto.information;

public enum MessageType {
    SYSTEM_INFO("Системные информационые сообщения"),
    ESTIMATION_WORK("Проведена оценка"),
    CHANGE_STAGE_WORK("Смена статуса ЗИ"),
    UPDATE_INFO("Список исправлений в новой версии "),
    AVAIL_WORK_LAST_DAY("Работы отмеченные вами за предыдущий рабочий день. Рассылается по рабочим дням в 11 часов"),
    AVAIL_WORK_LAST_WEEK("Работы отмеченные вами за предыдущую неделю. Рассылается в 12 на второй рабочий день."),
    AVAIL_WORK_FULL_REPORT("Статус ЗИ. 10 утра по вторникам"),
    VACATION_MY_START("Начало вашего отпуска 20 в день перед отпуском"),
    VACATION_MY_END("Конец Вашего отпуска. 14 часов в последний день отпуска"),
    VACATION_USER_START("Список отпуской. 12 утра ежедневно");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
