package ru.darujo.dto.printer;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DataPrinter {
    SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
    protected String dateToText(Date date){
        if (date == null){
            return null;
        }
        return sdf.format(date);
    }
}
