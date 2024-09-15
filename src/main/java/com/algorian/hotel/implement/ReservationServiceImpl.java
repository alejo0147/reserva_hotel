package com.algorian.hotel.implement;

import com.algorian.hotel.entity.Reservation;
import com.algorian.hotel.entity.ServiceT;
import com.algorian.hotel.entity.Cliente;
import com.algorian.hotel.exception.DateValidationException;
import com.algorian.hotel.models.*;
import com.algorian.hotel.repository.IReservationRepository;
import com.algorian.hotel.repository.IServiceRepository;
import com.algorian.hotel.repository.IClienteRepository;
import com.algorian.hotel.service.IReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@Service
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository _reservationRepository;
    private final IClienteRepository _clienteRepository;
    private final IServiceRepository _serviceRepository;

    @Override
    public ResponseEntity<List<ReservationListarDTO>> findAll() {
        List<Reservation> reservationList = _reservationRepository.findAll();
        List<ReservationListarDTO> reservationDTOList = reservationList.stream()
                .map(reservation -> {
                    String userName = reservation.getCliente().getFullName();
                    String serviceDescription = reservation.getServiceT().getDescription();

                    return ReservationListarDTO.builder()
                            .id(reservation.getId())
                            .dateReservation(reservation.getDateReservation())
                            .dateStart(reservation.getDateStart())
                            .dateEnd(reservation.getDateEnd())
                            .userName(userName)
                            .serviceDescription(serviceDescription)
                            .build();
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservationDTOList);
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        Optional<Reservation> find = _reservationRepository.findById(id);
        if (find.isPresent()) {
            Reservation reservation = find.get();

            // Recuperar el usuario y servicio por sus IDs
            Cliente user = _clienteRepository.findById(reservation.getCliente().getId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

            ServiceT service = _serviceRepository.findById(reservation.getServiceT().getId())
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

            // Crear el DTO de ReservationDetailDTO con los objetos completos
            ReservationDetailDTO reservationDetailDTO = ReservationDetailDTO.builder()
                    .id(reservation.getId())
                    .dateReservation(reservation.getDateReservation())
                    .dateStart(reservation.getDateStart())
                    .dateEnd(reservation.getDateEnd())
                    .userName(user.getFullName())
                    .serviceDescription(service.getDescription())
                    .build();

            return ResponseEntity.ok(reservationDetailDTO);
        }
        return ResponseEntity.notFound().build();
    }


    @Validated(ReservationDTO.CreateGroup.class)
    @Override
    public ResponseEntity<?> save(@Validated ReservationDTO reservationDTO) {

        if (reservationDTO.getDateEnd().isBefore(reservationDTO.getDateStart())) {
            throw new DateValidationException("La fecha de finalización no puede ser anterior a la fecha de inicio.");
        }

        Cliente cliente = _clienteRepository.findById(reservationDTO.getClienteId())
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

        ServiceT serviceT = _serviceRepository.findById(reservationDTO.getServiceId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        // Crear y guardar la reserva
        Reservation reservation = Reservation.builder()
                .dateReservation(LocalDate.now())
                .dateStart(reservationDTO.getDateStart())
                .dateEnd(reservationDTO.getDateEnd())
                .cliente(cliente)
                .serviceT(serviceT)
                .build();

        Reservation reservationSave = _reservationRepository.save(reservation);

        ReservationDetailDTO reservationSaveDTO = ReservationDetailDTO.builder()
                .id(reservationSave.getId())
                .dateReservation(reservationSave.getDateReservation())
                .dateStart(reservationSave.getDateStart())
                .dateEnd(reservationSave.getDateEnd())
                .userName(cliente.getFullName())
                .serviceDescription(serviceT.getDescription())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(reservationSaveDTO);
    }


    @Validated(ReservationDTO.UpdateGroup.class)
    @Override
    public ResponseEntity<?> update(@Validated ReservationDTO reservationDTO, Long id) {
        Optional<Reservation> find = _reservationRepository.findById(id);
        if (find.isPresent()) {
            if (reservationDTO.getDateEnd().isBefore(reservationDTO.getDateStart())) {
                throw new DateValidationException("La fecha de finalización no puede ser anterior a la fecha de inicio.");
            }

            Reservation reservation = find.get();

            Cliente cliente = _clienteRepository.findById(reservationDTO.getClienteId())
                    .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));

            ServiceT serviceT = _serviceRepository.findById(reservationDTO.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

            // Actualizar la reserva
            reservation.setDateStart(reservationDTO.getDateStart());
            reservation.setDateEnd(reservationDTO.getDateEnd());
            reservation.setCliente(cliente);
            reservation.setServiceT(serviceT);

            Reservation updatedReservation = _reservationRepository.save(reservation);

            // Devolver los detalles de la reserva actualizada
            ReservationDTO reservationSaveDTO = ReservationDTO.builder()
                    .id(updatedReservation.getId())
                    .dateReservation(updatedReservation.getDateReservation())
                    .dateStart(updatedReservation.getDateStart())
                    .dateEnd(updatedReservation.getDateEnd())
                    .clienteId(cliente.getId())
                    .serviceId(updatedReservation.getServiceT().getId())
                    .build();

            return ResponseEntity.status(HttpStatus.OK).body(reservationSaveDTO);
        }
        return ResponseEntity.noContent().build();
    }



    @Override
    public ResponseEntity<?> delete(Long id) {
        Optional<Reservation> find = _reservationRepository.findById(id);
        if (find.isPresent()){
            _reservationRepository.deleteById(id);

            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ReservationDTO>> findByDateRange(LocalDate startDate) {
        List<Reservation> reservations = _reservationRepository.findByDateRange(startDate);
        List<ReservationDTO> reservationDTOs = reservations.stream()
                .map(reservation -> ReservationDTO.builder()
                        .id(reservation.getId())
                        .dateReservation(reservation.getDateReservation())
                        .dateStart(reservation.getDateStart())
                        .dateEnd(reservation.getDateEnd())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservationDTOs);
    }

    @Override
    public ResponseEntity<List<ReservationListarDTO>> findByUserName(String userName) {
        Optional<Cliente> userT = _clienteRepository.findByFullName(userName);
        if (userT.isPresent()){
            Long userId = userT.get().getId();
            List<Reservation> reservations = _reservationRepository.findByUserTId(userId);
            List<ReservationListarDTO> reservationDTOs = reservations.stream()
                    .map(reservation -> {
                        String userNam = reservation.getCliente().getFullName(); // Solo el nombre del usuario
                        String serviceDescription = reservation.getServiceT().getDescription(); // Solo la descripción del servicio

                        return ReservationListarDTO.builder()
                                .id(reservation.getId())
                                .dateReservation(reservation.getDateReservation())
                                .dateStart(reservation.getDateStart())
                                .dateEnd(reservation.getDateEnd())
                                .userName(userNam)
                                .serviceDescription(serviceDescription)
                                .build();
                    })
                    .collect(Collectors.toList());
            return ResponseEntity.ok(reservationDTOs);
        }
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<ReservationListarDTO>> findByServiceId(Long serviceId) {
        List<Reservation> reservations = _reservationRepository.findByServiceTId(serviceId);
        List<ReservationListarDTO> reservationDTOs = reservations.stream()
                .map(reservation -> {
                    String userName = reservation.getCliente().getFullName(); // Solo el nombre del usuario
                    String serviceDescription = reservation.getServiceT().getDescription(); // Solo la descripción del servicio

                    return ReservationListarDTO.builder()
                            .id(reservation.getId())
                            .dateReservation(reservation.getDateReservation())
                            .dateStart(reservation.getDateStart())
                            .dateEnd(reservation.getDateEnd())
                            .userName(userName)
                            .serviceDescription(serviceDescription)
                            .build();
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservationDTOs);
    }

    public Cliente findOrCreateUser(Cliente cliente) {
        // Buscar usuario por correo electrónico
        Optional<Cliente> existingUser = _clienteRepository.findByEmail(cliente.getEmail());

        // Si el usuario ya existe, devolverlo
        if (existingUser.isPresent()) {
            return existingUser.get();
        }

        // Si el usuario no existe, guardarlo y devolver el nuevo usuario
        return _clienteRepository.save(cliente);
    }
}
