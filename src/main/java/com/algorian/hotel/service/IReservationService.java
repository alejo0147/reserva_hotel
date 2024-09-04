package com.algorian.hotel.service;

import com.algorian.hotel.models.ReservationDTO;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface IReservationService {

    ResponseEntity<List<ReservationDTO>> findAll();

    ResponseEntity<?> findById(Long id);

    ResponseEntity<?> save(ReservationDTO reservationDTO);

    ResponseEntity<?> update(ReservationDTO reservationDTO, Long id);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<List<ReservationDTO>> findByDateRange(LocalDate startDate);

    ResponseEntity<List<ReservationDTO>> findByUserId(Long userId);

    ResponseEntity<List<ReservationDTO>> findByServiceId(Long serviceId);

}
