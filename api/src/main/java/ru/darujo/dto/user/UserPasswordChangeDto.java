package ru.darujo.dto.user;

import java.io.Serializable;

public class UserPasswordChangeDto implements Serializable {
    private String nikName;
    private String passwordOld;
    private String passwordNew;

    public UserPasswordChangeDto() {
    }

    public String getNikName() {
        return nikName;
    }

    public String getPasswordOld() {
        return passwordOld;
    }

    public String getPasswordNew() {
        return passwordNew;
    }

}
