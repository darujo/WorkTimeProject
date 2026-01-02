package ru.darujo.jwt;

import org.springframework.stereotype.Component;

@Component
public class JwtStopServiceFilter extends JwtRightFilter {
    @Override
    protected String getRight() {
        return "STOP_SERVICE";
    }
}
