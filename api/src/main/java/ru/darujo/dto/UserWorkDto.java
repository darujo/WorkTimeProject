package ru.darujo.dto;

import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.service.CodeService;

import java.text.SimpleDateFormat;
import java.util.*;

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
    private Float workAllFact;
    private final Map<Integer,Float>  workTime = new LinkedHashMap <>();

    public UserWorkDto(String nikName, String authorFirstName, String authorLastName, String authorPatronymic, Date dateStart, Date dateEnd, Float workPlan) {
        this.nikName = nikName;
        this.authorFirstName = authorFirstName;
        this.authorLastName = authorLastName;
        this.authorPatronymic = authorPatronymic;
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
        this.workPlan = workPlan;
        this.workAllFact = 0f;
        CodeService.getTaskTypes().forEach((type, s) -> workTime.put(type,0f));
    }
    public void addTime(Integer type,Float time ){
        if(type != null && time != null){
            workTime.put(type,workTime.get(type) + time);
        }
    }
    public UserWorkDto addTimeAll(){
        workTime.forEach((type, time) ->addTimeAll(time) );
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
    public void setFirstName(String authorFirstName) {
        this.authorFirstName = authorFirstName;
    }

    @Override
    public void setLastName(String authorLastName) {
        this.authorLastName = authorLastName;
    }

    @Override
    public void setPatronymic(String authorPatronymic) {
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
        return workTime.get(1);
    }

    public Float getWorkWenderFact() {
        return workTime.get(2);
    }

    public Float getWorkAdminFact() {
        return workTime.get(3);
    }

    public Float getWorkAllFact() {
        return workAllFact;
    }

    public List<AttrDto<Integer>> getWorkTime() {
        List<AttrDto<Integer>> attrDTOs = new ArrayList<>();
        workTime.forEach((type, time) -> attrDTOs.add(new AttrDto<>(type,time.toString())) );
        return attrDTOs;
    }
}
