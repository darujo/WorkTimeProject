package ru.darujo.dto.work;

public interface WorkPlanTime {
   Long getId();
   void setLaborAnalise(Float laborDevelop);
   void setLaborDevelop(Float laborDevelop);
   void setLaborDebug(Float laborDebug);
   void setLaborRelease(Float laborRelease);
   void setLaborOPE(Float laborOPE) ;
}
