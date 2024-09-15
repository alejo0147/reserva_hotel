package com.algorian.hotel.service;

import com.algorian.hotel.models.ClienteDTO;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface IClienteService {

    ResponseEntity<List<ClienteDTO>> findAll();

    ResponseEntity<?> findById(Long id);

    ResponseEntity<?> save(ClienteDTO userTDTO);

    ResponseEntity<?> update(ClienteDTO userTDTO, Long id);

}
