package ru.darujo.service;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class RunnableNotException implements Runnable{
    private final Runnable runnable;
    public RunnableNotException(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void run() {
        try {
            runnable.run();
        }catch (Exception ex){
            log.error(ex);
        }
    }
}
