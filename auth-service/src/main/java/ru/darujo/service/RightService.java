package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.model.Right;
import ru.darujo.repository.RightRepository;


@Service
public class RightService {
    private RightRepository rightRepository;

    @Autowired
    public void setRightRepository(RightRepository rightRepository) {
        this.rightRepository = rightRepository;
    }

    public Iterable<Right> getListRight(){
        return  rightRepository.findAll();
    }

    public Right getRight(String name) {
        return rightRepository.findByNameIgnoreCase(name).orElse(null);
    }
}
