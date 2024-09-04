package com.algorian.hotel.service;

import com.algorian.hotel.models.ServiceDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IServiceService {

    ResponseEntity<List<ServiceDTO>> findAll();

    ResponseEntity<?> findById(Long id);

    ResponseEntity<?> save(ServiceDTO serviceDTO);

    ResponseEntity<?> update(ServiceDTO serviceDTO, Long id);

    ResponseEntity<?> delete(Long id);

}
