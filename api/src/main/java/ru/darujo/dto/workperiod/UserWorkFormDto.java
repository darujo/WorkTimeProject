package ru.darujo.dto.workperiod;

import ru.darujo.assistant.helper.DataHelper;
import ru.darujo.dto.ratestage.AttrDto;

import java.util.*;

public class UserWorkFormDto extends UserWorkDto {
    public UserWorkFormDto() {
    }
    @SuppressWarnings("unused")
    public String getDateStartStr() {
        return DataHelper.dateToDDMMYYYY(dateStart);
    }

    @SuppressWarnings("unused")
    public String getDateEndStr() {
        return DataHelper.dateToDDMMYYYY(dateEnd);
    }


    @SuppressWarnings("unused")
    public List<AttrDto<Integer>> getWorkTimeAttr() {
        List<AttrDto<Integer>> attrDTOs = new ArrayList<>();
        workTime.forEach((type, time) -> attrDTOs.add(new AttrDto<>(type,time.toString())) );
        return attrDTOs;
    }
    @SuppressWarnings("unused")
    public List<AttrDto<Integer>> getWorkTaskColAttr() {
        List<AttrDto<Integer>> attrDTOs = new ArrayList<>();
        workTask.forEach((type, listTask) -> attrDTOs.add(new AttrDto<>(type,String.valueOf(listTask.size()))) );
        return attrDTOs;
    }
    @SuppressWarnings("unused")
    public List<AttrDto<Integer>> getWorkPercent() {

        List<AttrDto<Integer>> attrDTOs = new ArrayList<>();
        workTime.forEach((type, time) ->
        {
            String percent=  "0";
            if (getWorkAllFact() != 0f) {
                percent = String.format("%.0f", 100 * time / getWorkAllFact());
            }
            attrDTOs.add(new AttrDto<>(type, percent + " %" ));
        })
                    ;
        return attrDTOs;
    }

}
