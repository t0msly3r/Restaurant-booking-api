package com.foodkin.restaurante.repository;

import com.foodkin.restaurante.model.Table;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TableRepository extends MongoRepository<Table, String> {
    List<Table> findByAvailableTrue();
    List<Table> findByCapacityGreaterThan(Integer capacity);
    Boolean existsByNumber(Integer number);
    Optional<Table> findByNumber(Integer number);
}
