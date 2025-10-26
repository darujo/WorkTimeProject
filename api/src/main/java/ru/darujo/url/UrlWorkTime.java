package ru.darujo.url;

public class UrlWorkTime {
    private static final String URL = "https://178.20.40.174:5555/#!";

    public static String getUrl(String url, String name) {
        return String.format("<a href=\"%s\">%s</a>", url, name);
    }

    public static String getUrlRate(Long workId, String name) {
        return getUrl(URL + "/rate?workId=" + workId, name);
    }
    public static String getUrlWorkSap(Long codeSap, String name) {
        return getUrl(URL + "/work?stageZi=50&codeSap=" + codeSap, name);
    }
}
