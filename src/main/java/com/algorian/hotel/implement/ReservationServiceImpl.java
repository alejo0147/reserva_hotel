package com.algorian.hotel.implement;

import com.algorian.hotel.entity.Client;
import com.algorian.hotel.entity.Reservation;
import com.algorian.hotel.entity.ServiceR;
import com.algorian.hotel.models.*;
import com.algorian.hotel.repository.IReservationRepository;
import com.algorian.hotel.repository.IServiceRepository;
import com.algorian.hotel.repository.IClientRepository;
import com.algorian.hotel.service.IReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@Service
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository _reservationRepository;
    private final IClientRepository _clienteRepository;
    private final IServiceRepository _serviceRepository;


    @Override
    public ResponseEntity<List<ReservationDTO>> findAll() {
        List<Reservation> reservations = _reservationRepository.findAll();
        List<ReservationDTO> dtoList = reservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<ReservationDTO> findById(Long id) {
        return _reservationRepository.findById(id)
                .map(this::convertToDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());  // Si no encuentra, devuelve 404
    }


    @Validated(ReservationCreateDTO.CreateGroup.class)
    @Override
    @Transactional
    public ResponseEntity<?> save(@Validated ReservationCreateDTO reservationDTO) {
        // Convertir el DTO a entidad
        Reservation reservation = convertToEntity(reservationDTO);

        // Guardar la entidad en la base de datos
        Reservation savedReservation = _reservationRepository.save(reservation);

        // Convertir la entidad guardada de vuelta a DTO
        ReservationDTO savedReservationDTO = convertToDTO(savedReservation);

        // Retornar la respuesta con el DTO guardado
        return ResponseEntity.ok(savedReservationDTO);
    }


    @Validated(ReservationCreateDTO.UpdateGroup.class)
    @Override
    @Transactional
    public ResponseEntity<?> update(@Validated ReservationCreateDTO reservationDTO, Long id) {
        // Buscar la reserva existente por ID
        return _reservationRepository.findById(id)
                .map(existingReservation -> {
                    // Actualizar los campos de la reserva con los datos del DTO
                    existingReservation.setDateReservation(LocalDate.now());
                    existingReservation.setDateStart(reservationDTO.getDateStart());
                    existingReservation.setDateEnd(reservationDTO.getDateEnd());
                    // Actualizar el cliente utilizando el ID del cliente en el DTO
                    existingReservation.setClient(Client.builder().id(reservationDTO.getClientId()).build());
                    // Actualizar el servicio utilizando el ID del servicio en el DTO
                    existingReservation.setService(ServiceR.builder().id(reservationDTO.getServiceId()).build());

                    // Guardar la reserva actualizada en la base de datos
                    Reservation updatedReservation = _reservationRepository.save(existingReservation);

                    // Convertir la entidad actualizada de vuelta a DTO si es necesario
                    ReservationDTO updatedReservationDTO = convertToDTO(updatedReservation);

                    // Retornar la respuesta con el DTO actualizado
                    return ResponseEntity.ok(updatedReservationDTO);
                })
                .orElse(ResponseEntity.notFound().build()); // Si no encuentra, devolver 404
    }


    @Override
    @Transactional
    public ResponseEntity<?> delete(Long id) {
        Optional<Reservation> find = _reservationRepository.findById(id);
        if (find.isPresent()) {
            _reservationRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    public ResponseEntity<List<ReservationDTO>> findByDate(LocalDate date) {
        List<Reservation> reservations = _reservationRepository.findByDate(date);
        List<ReservationDTO> dtoList = reservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @Override
    public ResponseEntity<List<ReservationDTO>> findByClient(Long clientId) {
        List<Reservation> reservations = _reservationRepository.findByClientId(clientId);
        List<ReservationDTO> dtoList = reservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    @Override
    public ResponseEntity<List<ReservationDTO>> findByService(Long serviceId) {
        List<Reservation> reservations = _reservationRepository.findByServiceId(serviceId);
        List<ReservationDTO> dtoList = reservations.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtoList);
    }

    // Método de conversión de DTO a Entidad
    private Reservation convertToEntity(ReservationCreateDTO reservationDTO) {
        return Reservation.builder()
                .id(reservationDTO.getId())
                .dateReservation(LocalDate.now())
                .dateStart(reservationDTO.getDateStart())
                .dateEnd(reservationDTO.getDateEnd())
                // Asignamos el cliente utilizando su ID
                .client(Client.builder().id(reservationDTO.getClientId()).build())
                // Asignamos el servicio utilizando su ID
                .service(ServiceR.builder().id(reservationDTO.getServiceId()).build())
                .build();
    }

    // Método de conversión de Entidad a DTO
    private ReservationDTO convertToDTO(Reservation reservation) {
        return ReservationDTO.builder()
                .id(reservation.getId())
                .dateReservation(reservation.getDateReservation())
                .dateStart(reservation.getDateStart())
                .dateEnd(reservation.getDateEnd())
                .client(convertToClientDTO(reservation.getClient()))
                .service(convertToServiceDTO(reservation.getService()))
                .build();
    }

    // Método para convertir Client a DTO
    private ClientDTO convertToClientDTO(Client client) {
        return ClientDTO.builder()
                .id(client.getId())
                .fullName(client.getFullName())
                .email(client.getEmail())
                .build();
    }

    // Método para convertir de DTO a Client
    private Client convertToEntity(ClientDTO clientDTO) {
        return Client.builder()
                .id(clientDTO.getId())
                .fullName(clientDTO.getFullName())
                .email(clientDTO.getEmail())
                .build();
    }

    // Método para convertir ServiceR a DTO
    private ServiceDTO convertToServiceDTO(ServiceR service) {
        return ServiceDTO.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .build();
    }

    // Método para de DTO a ServiceR
    private ServiceR convertToEntity(ServiceDTO serviceDTO) {
        return ServiceR.builder()
                .id(serviceDTO.getId())
                .name(serviceDTO.getName())
                .description(serviceDTO.getDescription())
                .build();
    }

}
