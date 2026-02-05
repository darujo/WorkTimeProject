package ru.darujo.model;

import lombok.Getter;

@Getter
public class StageZiFind {
    private Integer stageZiLe = null;
    private Integer stageZiGe = null;

    public StageZiFind(Integer stageZi) {
        if (stageZi != null) {
            if (stageZi < 10) {
                stageZiGe = stageZi;
                stageZiLe = stageZi;
            } else {
                stageZiLe = stageZi - 10;
            }
        }
    }

}
