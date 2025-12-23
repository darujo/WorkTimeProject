package ru.darujo.dto.ratestage;

import ru.darujo.dto.user.UserFio;


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

    @SuppressWarnings("unused")
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
        this.stageAll = 0f;
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
    private Float stage0Fact;
    private Float stage1;
    private Float stage1Fact;
    private Float stage2;
    private Float stage2Fact;
    private Float stage3;
    private Float stage3Fact;
    private Float stage4;
    private Float stage4Fact;
    private Float stage5Fact;

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

    @SuppressWarnings("unused")
    public Float getStageAll() {
        return stageAll;
    }

    public Long getWorkId() {
        return workId;
    }

    @SuppressWarnings("unused")
    public String getRoleStr() {
        if (role == null) {
            return "";
        }
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

    private void addAllTime(Float time) {
        if (time != null) {
            stageAll = stageAll + time;
        }
    }

    @SuppressWarnings("unused")
    public Float getStage0Fact() {
        return stage0Fact;
    }


    public void setStage0Fact(Float stage0Fact) {
        this.stage0Fact = stage0Fact;
    }

    public void setStageFact(Integer stage, Float stageFact) {
        if (stage == 0) {
            setStage0Fact(stageFact);
        } else if (stage == 1) {
            setStage1Fact(stageFact);
        } else if (stage == 2) {
            setStage2Fact(stageFact);
        } else if (stage == 3) {
            setStage3Fact(stageFact);
        } else if (stage == 4) {
            setStage4Fact(stageFact);
        } else if (stage == 5) {
            setStage5Fact(stageFact);
        }
    }

    public void setStage1Fact(Float stage1Fact) {
        this.stage1Fact = stage1Fact;
    }

    public void setStage2Fact(Float stage2Fact) {
        this.stage2Fact = stage2Fact;
    }

    public void setStage3Fact(Float stage3Fact) {
        this.stage3Fact = stage3Fact;
    }

    public void setStage4Fact(Float stage4Fact) {
        this.stage4Fact = stage4Fact;
    }

    public void setStage5Fact(Float stage5Fact) {
        this.stage5Fact = stage5Fact;
    }

    @SuppressWarnings("unused")
    public Float getStage1Fact() {
        return stage1Fact;
    }

    @SuppressWarnings("unused")
    public Float getStage2Fact() {
        return stage2Fact;
    }

    @SuppressWarnings("unused")
    public Float getStage3Fact() {
        return stage3Fact;
    }

    @SuppressWarnings("unused")
    public Float getStage4Fact() {
        return stage4Fact;
    }

    @SuppressWarnings("unused")
    public Float getStage5Fact() {
        return stage5Fact;
    }
}
