package com.algorian.hotel.controller;

import com.algorian.hotel.models.ReservationDTO;
import com.algorian.hotel.service.IReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/reservation")
@Tag(name = "Reservaciones", description = "Gestión de reservas")
public class ReservationController {

    private final IReservationService _reservationService;

    @GetMapping
    @Operation(summary = "Obtener todas las reservaciones", description = "Devuelve una lista de todas las reservas.")
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        return _reservationService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener reserva por ID", description = "Devuelve una reserva basado en el ID proporcionado.")
    public ResponseEntity<?> getReservationById(@PathVariable Long id) {
        return _reservationService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear una nueva reservación", description = "Crea una nueva reservación con los detalles proporcionados.")
    public ResponseEntity<?> createReservation(@Validated(ReservationDTO.CreateGroup.class) @RequestBody ReservationDTO reservationDTO) {
        return _reservationService.save(reservationDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar una reservación existente", description = "Actualiza los detalles de una reservación existente basado en el ID proporcionado.")
    public ResponseEntity<?> updatereservation(@Validated(ReservationDTO.UpdateGroup.class) @RequestBody ReservationDTO reservationDTO, @PathVariable Long id) {
        return _reservationService.update(reservationDTO, id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una reservación existente", description = "Elimina una reservación existente basado en el ID proporcionado.")
    public ResponseEntity<?> deleteReservation(@PathVariable Long id){
        return _reservationService.delete(id);
    }

    /**
     *  Métodos para buscar por fecha, usuario o servicio
     */
    @GetMapping("/search/date")
    @Operation(summary = "Buscar reservas por rango de fechas", description = "Busca reservas dentro de un rango de fechas.")
    public ResponseEntity<List<ReservationDTO>> findByDateRange(
            @RequestParam @NotNull LocalDate startDate) {
        return _reservationService.findByDateRange(startDate);
    }

    @GetMapping("/search/user/{userId}")
    @Operation(summary = "Buscar reservas por ID de usuario", description = "Busca reservas asociadas a un usuario.")
    public ResponseEntity<List<ReservationDTO>> findByUserId(@PathVariable Long userId) {
        return _reservationService.findByUserId(userId);
    }

    @GetMapping("/search/service/{serviceId}")
    @Operation(summary = "Buscar reservas por ID de servicio", description = "Busca reservas asociadas a un servicio.")
    public ResponseEntity<List<ReservationDTO>> findByServiceId(@PathVariable Long serviceId) {
        return _reservationService.findByServiceId(serviceId);
    }

}
