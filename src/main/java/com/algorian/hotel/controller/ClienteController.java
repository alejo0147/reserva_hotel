package com.algorian.hotel.controller;

import com.algorian.hotel.models.ClienteDTO;
import com.algorian.hotel.service.IClienteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("/client")
@Tag(name = "Clientes", description = "Gestión de clientes")
public class ClienteController {

    private final IClienteService _clienService;

    @GetMapping
    @Operation(summary = "Obtener todos los clientes", description = "Devuelve una lista de todos los clientes.")
    public ResponseEntity<List<ClienteDTO>> getAllUsers() {
        return _clienService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID", description = "Devuelve un cliente basado en el ID proporcionado.")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return _clienService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo cliente", description = "Crea un nuevo cliente con los detalles proporcionados.")
    public ResponseEntity<?> createUser(@Validated(ClienteDTO.CreateGroup.class) @RequestBody ClienteDTO clienteDTO) {
        return _clienService.save(clienteDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un cliente existente", description = "Actualiza los detalles de un cliente existente basado en el ID proporcionado.")
    public ResponseEntity<?> updateUser(@Validated(ClienteDTO.UpdateGroup.class) @RequestBody ClienteDTO clienteDTO, @PathVariable Long id) {
        return _clienService.update(clienteDTO, id);
    }

}
