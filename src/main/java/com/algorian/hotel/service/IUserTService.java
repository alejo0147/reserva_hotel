package com.algorian.hotel.service;

import com.algorian.hotel.models.UserTDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IUserTService {

    ResponseEntity<List<UserTDTO>> findAll();

    ResponseEntity<?> findById(Long id);

    ResponseEntity<?> save(UserTDTO userTDTO);

    ResponseEntity<?> update(UserTDTO userTDTO, Long id);

}
