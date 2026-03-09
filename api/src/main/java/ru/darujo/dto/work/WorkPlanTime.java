package ru.darujo.dto.work;

import java.util.List;

public interface WorkPlanTime {
    Long getWorkId();
    Long getProjectId();

    List<Long> getChildId();
   void setLaborAnalise(Float laborDevelop);
   void setLaborDevelop(Float laborDevelop);
   void setLaborDebug(Float laborDebug);
   void setLaborRelease(Float laborRelease);
   void setLaborOPE(Float laborOPE) ;
}
