package com.algorian.hotel.models;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class UserTDTO {

    private Long id;

    @NotBlank(groups = {CreateGroup.class, UpdateGroup.class}, message = "El campo 'email' no puede estar vacío.")
    @Email(groups = {CreateGroup.class, UpdateGroup.class}, message = "El campo 'email' debe ser un email válido.")
    private String email;

    @NotBlank(groups = CreateGroup.class, message = "El campo 'password' es obligatorio para la creación.")
    private String password;

    public interface CreateGroup {}

    public interface UpdateGroup {}

}
