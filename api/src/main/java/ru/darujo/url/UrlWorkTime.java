package ru.darujo.url;

import ru.darujo.dto.work.WorkLittleDto;

public class UrlWorkTime {
    private static final String URL = "https://178.20.40.174:5555/#!";

    public static String getUrl(String url, String name) {
        return String.format("<a href=\"%s\">%s</a>", url, name);
    }

    public static String getUrlRate(Long workId, String name) {
        return getUrl(URL + "/work_rate?view=current&workId=" + workId, name);
    }

    public static String getUrlRateAll(Long workId, String name) {
        return getUrl(URL + "/rate?workId=" + workId, name);
    }
    public static String getUrlWorkSap(Long codeSap, String name) {
        return getUrl(URL + "/work?stageZi=50&codeSap=" + codeSap, name);
    }
    public static String getUrlVacation(String nikName,String text) {
        return getUrl(URL + "/vacation?nikName=" +  nikName,text);
    }

    public static String getUrlAgreement(Long workId, String name) {
        return getUrl(URL + "/rate?workId=" + workId, name);
    }
    public static String getUrlAgreement(WorkLittleDto workLittleDto) {
        return getUrlAgreement(workLittleDto.getId(),workLittleDto.getName());
    }
}
