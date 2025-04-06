package ru.darujo.dto.ratestage;

import ru.darujo.dto.UserFio;


public class WorkStageDto implements UserFio {
    private String firstName;
    private String lastName;
    private String patronymic;

    @Override
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public void setLastName(String lastName) {
      this.lastName = lastName;
    }

    @Override
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;

    }

    public WorkStageDto() {
    }

    public WorkStageDto(Long id, String nikName, Integer role, Float stage0, Float stage1, Float stage2, Float stage3, Float stage4, Long workId) {
        this.id = id;
        this.nikName = nikName;
        this.role = role;
        this.stage0 = stage0;
        this.stage1 = stage1;
        this.stage2 = stage2;
        this.stage3 = stage3;
        this.stage4 = stage4;
        this.workId = workId;
        this.stageAll =0f;
        addAllTime(stage0);
        addAllTime(stage1);
        addAllTime(stage2);
        addAllTime(stage3);
        addAllTime(stage4);
    }

    private Long id;
    private String nikName;
    private Integer role;
    private Float stage0;
    private Float stage1;
    private Float stage2;
    private Float stage3;
    private Float stage4;
    private Float stageAll;
    private Long workId;

    public Long getId() {
        return id;
    }

    public String getNikName() {
        return nikName;
    }

    public Integer getRole() {
        return role;
    }

    public Float getStage0() {
        return stage0;
    }

    public Float getStage1() {
        return stage1;
    }

    public Float getStage2() {
        return stage2;
    }

    public Float getStage3() {
        return stage3;
    }

    public Float getStage4() {
        return stage4;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPatronymic() {
        return patronymic;
    }

    public Float getStageAll() {
        return stageAll;
    }

    public Long getWorkId() {
        return workId;
    }

    public String getRoleStr() {
        if (role == 1) {
            return "Разработка и отладка";
        } else if (role == 2) {
            return "Внутренее тестирование";
        }
        if (role == -1) {
            return "";
        }

        return "Не известная роль";
    }
    private void  addAllTime(Float time){
        if(time != null){
            stageAll = stageAll + time;
        }
    }
}
