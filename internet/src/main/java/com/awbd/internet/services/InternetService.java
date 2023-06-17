package com.awbd.internet.services;


import com.awbd.internet.model.Internet;

import java.util.List;

public interface InternetService {
    List<Internet> findByProvider(String provider);
    Internet save(Internet internet);
    List<Internet> findAll();
    Internet delete(Long id);
    Internet findById(Long id);
}
