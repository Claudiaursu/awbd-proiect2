package com.awbd.internet.repositories;

import com.awbd.internet.model.Internet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface InternetRepository extends CrudRepository<Internet, Long> {
    List<Internet> findByProvider(String provider);
}
