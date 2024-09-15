package com.algorian.hotel.models;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ReservationDTO {

    private Long id;

    private LocalDate dateReservation;

    @NotNull(groups = {CreateGroup.class, UpdateGroup.class}, message = "El campo 'dateStart' no puede estar en blanco.")
    private LocalDate dateStart;

    @NotNull(groups = {CreateGroup.class, UpdateGroup.class}, message = "El campo 'dateEnd' no puede estar en blanco.")
    private LocalDate dateEnd;

    @NotNull(groups = {CreateGroup.class, UpdateGroup.class}, message = "El campo 'clienteId' es obligatorio.")
    private Long clienteId;

    @NotNull(groups = {CreateGroup.class, UpdateGroup.class}, message = "El campo 'serviceId' es obligatorio.")
    private Long serviceId;

    public interface CreateGroup {}

    public interface UpdateGroup {}

}
