package ru.darujo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.darujo.exceptions.ResourceNotFoundException;
import ru.darujo.model.Right;
import ru.darujo.repository.RightRepository;

import java.util.Optional;

@Service
public class RightService {
    private RightRepository rightRepository;

    @Autowired
    public void setRightRepository(RightRepository rightRepository) {
        this.rightRepository = rightRepository;
    }

    public Optional<Right> findByName(String right) {
        return rightRepository.findByNameIgnoreCase(right);
    }

    public Iterable<Right> getListRight(){
        return  rightRepository.findAll();
    }

    public Right findById(long id) {
        return rightRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Роль не найдена"));
    }

}
