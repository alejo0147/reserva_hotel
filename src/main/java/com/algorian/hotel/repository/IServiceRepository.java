package com.algorian.hotel.repository;

import com.algorian.hotel.entity.ServiceT;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IServiceRepository extends JpaRepository<ServiceT, Long> {



}
