package com.algorian.hotel.service;

import com.algorian.hotel.models.ReservationDTO;
import com.algorian.hotel.models.ReservationListarDTO;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;

public interface IReservationService {

    ResponseEntity<List<ReservationListarDTO>> findAll();

    ResponseEntity<?> findById(Long id);

    ResponseEntity<?> save(ReservationDTO reservationDTO);

    ResponseEntity<?> update(ReservationDTO reservationDTO, Long id);

    ResponseEntity<?> delete(Long id);

    ResponseEntity<List<ReservationDTO>> findByDateRange(LocalDate startDate);

    ResponseEntity<List<ReservationListarDTO>> findByClientId(Long clientId);

    ResponseEntity<List<ReservationListarDTO>> findByServiceId(Long serviceId);

}
