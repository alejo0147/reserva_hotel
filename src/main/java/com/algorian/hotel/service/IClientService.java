package com.algorian.hotel.service;

import com.algorian.hotel.models.ClientDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IClientService {

    ResponseEntity<List<ClientDTO>> findAll();

    ResponseEntity<?> findById(Long id);

    ResponseEntity<?> save(ClientDTO userTDTO);

    ResponseEntity<?> update(ClientDTO userTDTO, Long id);

}
