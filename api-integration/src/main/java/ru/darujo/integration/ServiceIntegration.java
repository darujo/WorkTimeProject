package ru.darujo.integration;

import java.text.SimpleDateFormat;
import java.util.Date;

public abstract  class ServiceIntegration {
    protected void addTeg(StringBuilder stringBuilder, String str, Date value) {
        addTeg(stringBuilder,str,dateToText(value));
    }
    protected void addTeg(StringBuilder stringBuilder, String str, String value) {
        if (value != null) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(str).append("=").append(value);
        }
    }
    protected void addTeg(StringBuilder stringBuilder, String str, Long value) {
        if (value != null) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(str).append("=").append(value);
        }
    }
    protected void addTeg(StringBuilder stringBuilder, String str, Integer value) {
        if (value != null) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("&");
            }
            stringBuilder.append(str).append("=").append(value);
        }
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    protected String dateToText(Date date) {
        if (date == null) {
            return null;
        }
        return sdf.format(date) + "T00:00:00.000Z";
    }

}
