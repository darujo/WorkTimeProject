package ru.darujo.jwt;

import org.springframework.stereotype.Component;

@Component
public class JwtAdminFilter extends JwtRightFilter {
    @Override
    protected String getRight() {
        return "EDIT_USER";
    }
}
