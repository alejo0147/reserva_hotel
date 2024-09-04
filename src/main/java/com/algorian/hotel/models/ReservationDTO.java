package com.algorian.hotel.models;

import jakarta.validation.constraints.NotBlank;
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

    @NotNull(groups = UserTDTO.UpdateGroup.class, message = "El campo 'id' es obligatorio para la actualización.")
    private Long id;

    private LocalDate dateReservation;

    @NotNull(groups = {UserTDTO.CreateGroup.class, UserTDTO.UpdateGroup.class}, message = "El campo 'dateStart' no puede estar en blanco.")
    private LocalDate dateStart;

    @NotNull(groups = {UserTDTO.CreateGroup.class, UserTDTO.UpdateGroup.class}, message = "El campo 'dateEnd' no puede estar en blanco.")
    private LocalDate dateEnd;

    @NotNull(groups = {CreateGroup.class, UpdateGroup.class}, message = "El campo 'userId' es obligatorio.")
    private Long userId;

    @NotNull(groups = {CreateGroup.class, UpdateGroup.class}, message = "El campo 'serviceId' es obligatorio.")
    private Long serviceId;

    public interface CreateGroup {}

    public interface UpdateGroup {}

}
