package ru.darujo.dto.ratestage;


public class WorkCriteriaDto {
    private Long id;
    private Integer criteria;
    private Float develop10;
    private Float develop50;
    private Float develop100;
    private Long workId;
    private Long projectId;

    @SuppressWarnings("unused")
    public WorkCriteriaDto() {
    }

    public WorkCriteriaDto(Long id, Integer criteria, Float develop10, Float develop50, Float develop100, Long workId, Long projectId) {
        this.id = id;
        this.criteria = criteria;
        this.develop10 = develop10;
        this.develop50 = develop50;
        this.develop100 = develop100;
        this.workId = workId;
        this.projectId = projectId;
    }

    public Long getId() {
        return id;
    }

    public Integer getCriteria() {
        return criteria;
    }

    public Float getDevelop10() {
        return develop10;
    }

    public Float getDevelop50() {
        return develop50;
    }

    public Float getDevelop100() {
        return develop100;
    }

    public Long getWorkId() {
        return workId;
    }

    @SuppressWarnings("unused")
    public String getCriteriaStr() {
        if (criteria == 1) {
            return "Реализация импорта данных";
        }
        if (criteria == 2) {
            return "Реализация хранения данных";
        }
        if (criteria == 3) {
            return "Реализация обработки и сравнения данных";
        }
        if (criteria == 4) {
            return "Реализация экспорта";
        }
        if (criteria == 5) {
            return "Поддержака (реализация) протокола интеграции";
        }
        if (criteria == 6) {
            return "Реализация внешнего сервиса (ОС Windows)";
        }
        if (criteria == 7) {
            return "Реализация внешнего сервиса (ОС Linux)";
        }
        if (criteria == 8) {
            return "Разработка экранной формы";
        }

        return "Не известная роль";
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }
}
