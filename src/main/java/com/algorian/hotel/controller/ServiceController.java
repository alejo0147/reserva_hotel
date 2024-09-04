package com.algorian.hotel.controller;

import com.algorian.hotel.models.ServiceDTO;
import com.algorian.hotel.service.IServiceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor

@RestController
@RequestMapping("/service")
@Tag(name = "Servicios", description = "Gestión de servicios")
public class ServiceController {

    private final IServiceService _serviceService;

    @GetMapping
    @Operation(summary = "Obtener todos los servicios", description = "Devuelve una lista de todos los servicios.")
    public ResponseEntity<List<ServiceDTO>> getAllServices() {
        return _serviceService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener servicio por ID", description = "Devuelve un servicio basado en el ID proporcionado.")
    public ResponseEntity<?> getServiceById(@PathVariable Long id) {
        return _serviceService.findById(id);
    }

    @PostMapping
    @Operation(summary = "Crear un nuevo servicio", description = "Crea un nuevo servicio con los detalles proporcionados.")
    public ResponseEntity<?> createService(@Validated(ServiceDTO.CreateGroup.class) @RequestBody ServiceDTO serviceDTO) {
        return _serviceService.save(serviceDTO);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar un servicio existente", description = "Actualiza los detalles de un servicio existente basado en el ID proporcionado.")
    public ResponseEntity<?> updateService(@Validated(ServiceDTO.UpdateGroup.class) @RequestBody ServiceDTO serviceDTO, @PathVariable Long id) {
        return _serviceService.update(serviceDTO, id);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar un servicio existente", description = "Elimina un servicio existente basado en el ID proporcionado.")
    public ResponseEntity<?> deleteService(@PathVariable Long id){
        return _serviceService.delete(id);
    }

}
