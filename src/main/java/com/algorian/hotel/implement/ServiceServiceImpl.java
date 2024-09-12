package com.algorian.hotel.implement;

import com.algorian.hotel.entity.ServiceT;
import com.algorian.hotel.models.ServiceDTO;
import com.algorian.hotel.repository.IServiceRepository;
import com.algorian.hotel.service.IServiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@Service
@Validated
public class ServiceServiceImpl implements IServiceService {

    private final IServiceRepository _serviceRepository;

    @Override
    public ResponseEntity<List<ServiceDTO>> findAll() {
        List<ServiceT> serviceList = _serviceRepository.findAll();
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
        Optional<ServiceT> find = _serviceRepository.findById(id);
        if (find.isPresent()){
            ServiceT serviceT = find.get();

            ServiceDTO serviceDTO = ServiceDTO.builder()
                    .id(serviceT.getId())
                    .name(serviceT.getName())
                    .description(serviceT.getDescription())
                    .build();

            return ResponseEntity.ok(serviceDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<?> save(ServiceDTO serviceDTO) {
        ServiceT serviceT = ServiceT.builder()
                .name(serviceDTO.getName())
                .description(serviceDTO.getDescription())
                .build();
        ServiceT serviceSave = _serviceRepository.save(serviceT);

        ServiceDTO serviceSaveDTO = ServiceDTO.builder()
                .id(serviceSave.getId())
                .name(serviceDTO.getName())
                .description(serviceDTO.getDescription())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(serviceSaveDTO);
    }

    @Override
    public ResponseEntity<?> update(ServiceDTO serviceDTO, Long id) {
        Optional<ServiceT> find = _serviceRepository.findById(id);
        if (find.isPresent()){
            ServiceT serviceT = find.get();
            serviceT.setName(serviceDTO.getName());
            serviceT.setDescription(serviceDTO.getDescription());

            ServiceT updateService = _serviceRepository.save(serviceT);

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
        Optional<ServiceT> find = _serviceRepository.findById(id);
        if (find.isPresent()){
            _serviceRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
