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

    @NotNull(groups = UpdateGroup.class, message = "El campo 'id' es obligatorio para la actualización.")
    private Long id;

    @NotBlank(groups = {CreateGroup.class, UpdateGroup.class}, message = "El campo 'fullName' no puede estar en blanco.")
    private String fullName;

    @Email(groups = {CreateGroup.class, UpdateGroup.class}, message = "El campo 'email' debe ser un email válido.")
    private String email;

    @NotBlank(groups = CreateGroup.class, message = "El campo 'password' es obligatorio para la creación.")
    private String password;

    public interface CreateGroup {}

    public interface UpdateGroup {}

}
