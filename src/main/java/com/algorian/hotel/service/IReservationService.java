package com.algorian.hotel.service;

import com.algorian.hotel.models.ReservationCreateDTO;
import com.algorian.hotel.models.ReservationDTO;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface IReservationService {

    ResponseEntity<List<ReservationDTO>> findAll();

    ResponseEntity<ReservationDTO> findById(Long id);

    ResponseEntity<?> save(ReservationCreateDTO reservationDTO);

    ResponseEntity<?> update(ReservationCreateDTO reservationDTO, Long id);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<List<ReservationDTO>> findByClient(Long clientId);

    ResponseEntity<List<ReservationDTO>> findByService(Long serviceId);

    ResponseEntity<List<ReservationDTO>> findByDate(LocalDate date);

}
