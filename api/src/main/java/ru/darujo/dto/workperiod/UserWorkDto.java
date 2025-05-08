package ru.darujo.dto.workperiod;

import ru.darujo.dto.printer.DataPrinter;
import ru.darujo.dto.user.UserFio;
import ru.darujo.service.CodeService;

import java.util.*;

public class UserWorkDto extends DataPrinter implements UserFio {
    public UserWorkDto() {
    }
    private Integer userCol;
    private String nikName;
    private String authorFirstName;
    private String authorLastName;
    private String authorPatronymic;
    protected Date dateStart;
    protected Date dateEnd;
    private Float workPlan;
    private Float workAllFact;
    protected final Map<Integer,Float>  workTime = new LinkedHashMap <>();

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
        workAllFact = 0f;
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


    public Float getWorkPlan() {
        return workPlan;
    }

    public Float getWorkAllFact() {
        return workAllFact;
    }


    protected final Map<Integer,Set<Long>>  workTask = new LinkedHashMap <>();
    public void addTask(Integer type, Long taskId) {
        Set<Long> tasks = workTask.computeIfAbsent(type, k -> new HashSet<>());
        tasks.add(taskId);

    }

    public Date getDateStart() {
        return dateStart;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public Map<Integer, Float> getWorkTime() {
        return workTime;
    }

    public Map<Integer, Set<Long>> getWorkTask() {
        return workTask;
    }
}
