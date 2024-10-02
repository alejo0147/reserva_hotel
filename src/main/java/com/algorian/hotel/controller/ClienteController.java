package com.algorian.hotel.controller;

import com.algorian.hotel.models.ClientDTO;
import com.algorian.hotel.service.IClientService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor

@CrossOrigin(origins = "http://localhost:4200")

@RestController
@RequestMapping("/client")
@Tag(name = "Clientes", description = "Gestión de clientes")
public class ClienteController {

    private final IClientService _clienService;

    @GetMapping
    @Operation(summary = "Obtener todos los clientes", description = "Devuelve una lista de todos los clientes.")
    public ResponseEntity<List<ClientDTO>> getAllUsers() {
        return _clienService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener cliente por ID", description = "Devuelve un cliente basado en el ID proporcionado.")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        return _clienService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo cliente", description = "Crea un nuevo cliente con los detalles proporcionados.")
    public ResponseEntity<?> createUser(@Validated(ClientDTO.CreateGroup.class) @RequestBody ClientDTO clientDTO) {
        return _clienService.save(clientDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un cliente existente", description = "Actualiza los detalles de un cliente existente basado en el ID proporcionado.")
    public ResponseEntity<?> updateUser(@Validated(ClientDTO.UpdateGroup.class) @RequestBody ClientDTO clientDTO, @PathVariable Long id) {
        return _clienService.update(clientDTO, id);
    }

}
