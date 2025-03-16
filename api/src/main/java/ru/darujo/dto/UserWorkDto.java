package ru.darujo.dto;

import java.text.SimpleDateFormat;
import java.util.Date;

public class UserWorkDto implements UserFio {
    public UserWorkDto() {
    }
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    private String dateToText(Date date){
        if (date == null){
            return null;
        }
        return sdf.format(date);
    }
    private Integer userCol;
    private String nikName;
    private String authorFirstName;
    private String authorLastName;
    private String authorPatronymic;
    private Date dateStart;
    private Date dateEnd;
    private Float workPlan;
    private Float workZiFact;
    private Float workWenderFact;
    private Float workAdminFact;
    private Float workAllFact;

    public UserWorkDto(String nikName, String authorFirstName, String authorLastName, String authorPatronymic, Date dateStart, Date dateEnd, Float workPlan) {
        this.nikName = nikName;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.authorPatronymic = authorPatronymic;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.workPlan = workPlan;
        this.workZiFact = 0f;
        this.workWenderFact = 0f;
        this.workAdminFact = 0f;
        this.workAllFact = 0f;
    }
    public void addTime(Integer type,Float time ){
        if(type != null && time != null){
            if(type == 1){
                workZiFact = workZiFact + time;
            }
            else if(type == 2){
                workWenderFact = workWenderFact + time;
            }
            else if(type == 3){
                workAdminFact = workAdminFact + time;
            }
        }
    }
    public UserWorkDto addTimeAll(){
        addTimeAll(workZiFact);
        addTimeAll(workWenderFact);
        addTimeAll(workAdminFact);
        return this;
    }
    private void addTimeAll(Float time){
        if(time != null){
            workAllFact = workAllFact + time;
        }
    }

    public void setUserCol(Integer userCol) {
        this.userCol = userCol;
    }

    public String getNikName() {
        return nikName;
    }

    @Override
    public void setAuthorFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    @Override
    public void setAuthorLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    @Override
    public void setAuthorPatronymic(String authorPatronymic) {
        this.authorPatronymic = authorPatronymic;
    }

    public Integer getUserCol() {
        return userCol;
    }

    public String getAuthorFirstName() {
        return authorFirstName;
    }

    public String getAuthorLastName() {
        return authorLastName;
    }

    public String getAuthorPatronymic() {
        return authorPatronymic;
    }

    public String getDateStart() {
        return dateToText(dateStart);
    }

    public String getDateEnd() {
        return dateToText(dateEnd);
    }

    public Float getWorkPlan() {
        return workPlan;
    }

    public Float getWorkZiFact() {
        return workZiFact;
    }

    public Float getWorkWenderFact() {
        return workWenderFact;
    }

    public Float getWorkAdminFact() {
        return workAdminFact;
    }

    public Float getWorkAllFact() {
        return workAllFact;
    }
}
