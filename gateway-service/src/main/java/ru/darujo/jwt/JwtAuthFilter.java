package ru.darujo.jwt;

import org.springframework.stereotype.Component;
@Component
public class JwtAuthFilter extends JwtRightFilter {

    @Override
    protected String getRight() {
        return null;
    }
}
