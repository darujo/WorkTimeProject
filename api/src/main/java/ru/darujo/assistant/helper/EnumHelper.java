package ru.darujo.assistant.helper;

import ru.darujo.dto.ratestage.AttrDto;
import ru.darujo.type.TypeEnum;

import java.util.ArrayList;
import java.util.List;

public class EnumHelper {
    public static List<AttrDto<Enum<?>>> getList(Enum<? extends TypeEnum>  [] enumObj ){
        List<AttrDto<Enum<?>>> list = new ArrayList<>();
        for (Enum<?> enumOne : enumObj){
            if(enumOne instanceof TypeEnum){
                list.add(new AttrDto<>(enumOne,((TypeEnum) enumOne).getName()));
            }
        }
        return list;
    }
}
