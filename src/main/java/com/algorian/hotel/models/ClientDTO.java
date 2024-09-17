package com.algorian.hotel.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.ToString;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ClientDTO {

    private Long id;

    @NotBlank(groups = {CreateGroup.class, UpdateGroup.class}, message = "El campo 'fullName' no puede estar en blanco.")
    private String fullName;

    @NotBlank(groups = {CreateGroup.class, UpdateGroup.class}, message = "El campo 'email' no puede estar en blanco.")
    @Email(groups = {CreateGroup.class, UpdateGroup.class}, message = "El campo 'email' debe ser un email válido.")
    private String email;

    // private List<ReservationDTO> reservations;

    public interface CreateGroup {}

    public interface UpdateGroup {}

}
