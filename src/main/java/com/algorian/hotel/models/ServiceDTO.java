package com.algorian.hotel.models;

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
public class ServiceDTO {

    @NotNull(groups = UserTDTO.UpdateGroup.class, message = "El campo 'id' es obligatorio para la actualización.")
    private Long id;

    @NotBlank(groups = {UserTDTO.CreateGroup.class, UserTDTO.UpdateGroup.class}, message = "El campo 'Name' no puede estar en blanco.")
    private String name;

    @NotBlank(groups = {UserTDTO.CreateGroup.class, UserTDTO.UpdateGroup.class}, message = "El campo 'description' no puede estar en blanco.")
    private String description;

    public interface CreateGroup {}

    public interface UpdateGroup {}

}
