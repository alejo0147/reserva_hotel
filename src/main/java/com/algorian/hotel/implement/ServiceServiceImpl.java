package com.algorian.hotel.implement;

import com.algorian.hotel.entity.ServiceR;
import com.algorian.hotel.models.ServiceDTO;
import com.algorian.hotel.repository.IServiceRepository;
import com.algorian.hotel.service.IServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@org.springframework.stereotype.Service
@Validated
public class ServiceServiceImpl implements IServiceService {

    private final IServiceRepository _serviceRepository;

    @Override
    public ResponseEntity<List<ServiceDTO>> findAll() {
        List<ServiceR> serviceList = _serviceRepository.findAll();
        List<ServiceDTO> serviceDTOList = serviceList.stream()
                .map(serviceT  -> ServiceDTO.builder()
                        .id(serviceT.getId())
                        .name(serviceT.getName())
                        .description(serviceT.getDescription())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(serviceDTOList);
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        Optional<ServiceR> find = _serviceRepository.findById(id);
        if (find.isPresent()){
            ServiceR service = find.get();

            ServiceDTO serviceDTO = ServiceDTO.builder()
                    .id(service.getId())
                    .name(service.getName())
                    .description(service.getDescription())
                    .build();

            return ResponseEntity.ok(serviceDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<?> save(@Validated(ServiceDTO.CreateGroup.class) ServiceDTO serviceDTO) {
        ServiceR service = ServiceR.builder()
                .name(serviceDTO.getName())
                .description(serviceDTO.getDescription())
                .build();
        ServiceR serviceSave = _serviceRepository.save(service);

        ServiceDTO serviceSaveDTO = ServiceDTO.builder()
                .id(serviceSave.getId())
                .name(serviceDTO.getName())
                .description(serviceDTO.getDescription())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(serviceSaveDTO);
    }

    @Override
    public ResponseEntity<?> update(@Validated(ServiceDTO.UpdateGroup.class) ServiceDTO serviceDTO, Long id) {
        Optional<ServiceR> find = _serviceRepository.findById(id);
        if (find.isPresent()){
            ServiceR service = find.get();
            service.setName(serviceDTO.getName());
            service.setDescription(serviceDTO.getDescription());

            ServiceR updateService = _serviceRepository.save(service);

            ServiceDTO updateServiceDTO = ServiceDTO.builder()
                    .id(updateService.getId())
                    .name(updateService.getName())
                    .description(updateService.getDescription())
                    .build();

            return ResponseEntity.ok(updateServiceDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<?> delete(Long id) {
        Optional<ServiceR> find = _serviceRepository.findById(id);
        if (find.isPresent()){
            _serviceRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
