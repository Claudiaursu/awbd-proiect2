package com.awbd.internet.repositories;

import com.awbd.internet.model.Internet;
import org.springframework.data.repository.CrudRepository;

public interface InternetRepository extends CrudRepository<Internet, Long> {
    Internet findByProvider(String provider);
}
