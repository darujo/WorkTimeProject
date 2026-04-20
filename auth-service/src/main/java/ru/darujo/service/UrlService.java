package ru.darujo.service;

public class UrlService {
    private static final String URL = "https://178.20.40.174:5555/#!";

    public static String getUrl(String url, String name) {
        return String.format("<a href=\"%s\">%s</a>", url, name);
    }

    public static String getUrlNewEmail(String nikName, String code) {
        return getUrl(URL + "/sys/email_confirm?code=" + code + "&nikName=" + nikName, "подтвердить почту");
    }

    public static String getUrlRecovery(String nikName, String code) {
        return getUrl(URL + "/sys/recovery_password?code=" + code + "&nikName=" + nikName, "восстановить пароль");
    }
}
