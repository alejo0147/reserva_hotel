package com.algorian.hotel.repository;

import com.algorian.hotel.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface IReservationRepository extends JpaRepository<Reservation, Long> {

    @Query("SELECT r FROM Reservation r WHERE r.dateReservation = :startDate")
    List<Reservation> findByDateRange(@Param("startDate") LocalDate startDate);

    @Query("SELECT r FROM Reservation r WHERE r.client.id = :clientId")
    List<Reservation> findByClientId(@Param("clientId") Long clientId);

    @Query("SELECT r FROM Reservation r WHERE r.serviceT.id = :serviceId")
    List<Reservation> findByServiceTId(@Param("serviceId") Long serviceId);

    List<Reservation> findByDateStartLessThanEqualAndDateEndGreaterThanEqual(LocalDate endDate, LocalDate startDate);

}
