package com.algorian.hotel.controller;

import com.algorian.hotel.models.UserTDTO;
import com.algorian.hotel.service.IUserTService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("/user")
@Tag(name = "usuarios", description = "Gestión de usuarios")
public class UserTController {

    private  final IUserTService _userTService;

    @GetMapping
    @Operation(summary = "Obtener todos los usuarios", description = "Devuelve una lista de todos los usuarios.")
    public ResponseEntity<List<UserTDTO>> getAllUsers() {
        return _userTService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID", description = "Devuelve un cliente basado en el ID proporcionado.")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return _userTService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo cliente", description = "Crea un nuevo cliente con los detalles proporcionados.")
    public ResponseEntity<?> createUser(@Validated(UserTDTO.CreateGroup.class) @RequestBody UserTDTO userTDTO) {
        return _userTService.save(userTDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un cliente existente", description = "Actualiza los detalles de un cliente existente basado en el ID proporcionado.")
    public ResponseEntity<?> updateUser(@Validated(UserTDTO.UpdateGroup.class) @RequestBody UserTDTO userTDTO, @PathVariable Long id) {
        return _userTService.update(userTDTO, id);
    }

}
