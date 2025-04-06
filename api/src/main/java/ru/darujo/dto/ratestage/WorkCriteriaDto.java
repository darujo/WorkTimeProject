package ru.darujo.dto.ratestage;


public class WorkCriteriaDto {
    private Long id;
    private Integer criteria;
    private Float develop10;
    private Float develop50;
    private Float develop100;
    private Long workId;

    public WorkCriteriaDto() {
    }

    public WorkCriteriaDto(Long id, Integer criteria, Float develop10, Float develop50, Float develop100, Long workId) {
        this.id = id;
        this.criteria = criteria;
        this.develop10 = develop10;
        this.develop50 = develop50;
        this.develop100 = develop100;
        this.workId = workId;
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
            return "Поддержака(реализация) протокола интеграции";
        }
        if (criteria == 6) {
            return "Реализация внешнего сервиса (ОС Windows)";
        }
        if (criteria == 7) {
            return "Реализация внешнего сервиса (ОС Linux)";
        }
        return "Не известная роль";
    }
}
