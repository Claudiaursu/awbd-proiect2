package com.awbd.internet.services;

import com.awbd.internet.exceptions.InternetNotFound;
import com.awbd.internet.model.Internet;
import com.awbd.internet.repositories.InternetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class InternetServiceImpl implements InternetService {

    @Autowired
    InternetRepository internetRepository;

    @Override
    public List<Internet> findByProvider(String provider) {
        List<Internet> internetList = internetRepository.findByProvider(provider);
        return internetList;
    }

    @Override
    public Internet save(Internet internet) {
        Internet internetSave = internetRepository.save(internet);
        return internetSave;
    }

    @Override
    public List<Internet> findAll(){
        List<Internet> internetList = new LinkedList<>();
        internetRepository.findAll().iterator().forEachRemaining(internetList::add);
        return internetList;
    }

    @Override
    public Internet delete(Long id){
        Optional<Internet> internetOptional = internetRepository.findById(id);
        if (! internetOptional.isPresent())
            throw new InternetNotFound("Internet " + id + " not found!");
        internetRepository.delete(internetOptional.get());
        return internetOptional.get();
    }

    @Override
    public Internet findById(Long id) {
        Optional<Internet> internetOptional = internetRepository.findById(id);
        if (! internetOptional.isPresent())
            throw new InternetNotFound("Internet " + id + " not found!");
        return internetOptional.get();
    }


}
