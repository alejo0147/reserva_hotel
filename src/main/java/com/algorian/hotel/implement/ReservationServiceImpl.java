package com.algorian.hotel.implement;

import com.algorian.hotel.entity.Reservation;
import com.algorian.hotel.entity.ServiceT;
import com.algorian.hotel.entity.UserT;
import com.algorian.hotel.exception.DateValidationException;
import com.algorian.hotel.models.ReservationDTO;
import com.algorian.hotel.models.ReservationDetailDTO;
import com.algorian.hotel.models.ServiceDTO;
import com.algorian.hotel.models.UserTDTO;
import com.algorian.hotel.repository.IReservationRepository;
import com.algorian.hotel.repository.IServiceRepository;
import com.algorian.hotel.repository.IUserTRepository;
import com.algorian.hotel.service.IReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@Service
public class ReservationServiceImpl implements IReservationService {

    private final IReservationRepository _reservationRepository;
    private final IUserTRepository _userTRepository;
    private final IServiceRepository _serviceRepository;

    @Override
    public ResponseEntity<List<ReservationDTO>> findAll() {
        List<Reservation> reservationList = _reservationRepository.findAll();
        List<ReservationDTO> reservationDTOList = reservationList.stream()
                .map(reservation -> ReservationDTO.builder()
                        .id(reservation.getId())
                        .dateReservation(reservation.getDateReservation())
                        .dateStart(reservation.getDateStart())
                        .dateEnd(reservation.getDateEnd())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(reservationDTOList);
    }

    @Override
    public ResponseEntity<?> findById(Long id) {
        Optional<Reservation> find = _reservationRepository.findById(id);
        if (find.isPresent()) {
            Reservation reservation = find.get();

            // Recuperar el usuario por su ID
            Optional<UserT> userOptional = _userTRepository.findById(reservation.getUserT().getId());
            if (!userOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
            }
            UserT user = userOptional.get();

            // Recuperar el servicio por su ID
            Optional<ServiceT> serviceOptional = _serviceRepository.findById(reservation.getServiceT().getId());
            if (!serviceOptional.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
            }
            ServiceT service = serviceOptional.get();

            // Crear los DTOs a partir de los objetos recuperados
            UserTDTO userTDTO = UserTDTO.builder()
                    .id(user.getId())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .build();

            ServiceDTO serviceDTO = ServiceDTO.builder()
                    .id(service.getId())
                    .name(service.getName())
                    .description(service.getDescription())
                    .build();

            // Crear el DTO de ReservationDetailDTO con los objetos completos
            ReservationDetailDTO reservationDetailDTO = ReservationDetailDTO.builder()
                    .id(reservation.getId())
                    .dateReservation(reservation.getDateReservation())
                    .dateStart(reservation.getDateStart())
                    .dateEnd(reservation.getDateEnd())
                    .user(userTDTO)
                    .service(serviceDTO)
                    .build();

            return ResponseEntity.ok(reservationDetailDTO);
        }
        return ResponseEntity.notFound().build();
    }



    @Override
    public ResponseEntity<?> save(ReservationDTO reservationDTO) {

        if (reservationDTO.getDateEnd().isBefore(reservationDTO.getDateStart())) {
            throw new DateValidationException("La fecha de finalización no puede ser anterior a la fecha de inicio.");
        }

        UserT userT = _userTRepository.findById(reservationDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        ServiceT serviceT = _serviceRepository.findById(reservationDTO.getServiceId())
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

        Reservation reservation = Reservation.builder()
                .dateReservation(LocalDate.now())
                .dateStart(reservationDTO.getDateStart())
                .dateEnd(reservationDTO.getDateEnd())
                .userT(userT)
                .serviceT(serviceT)
                .build();

        Reservation reservationSave = _reservationRepository.save(reservation);

        // Solo devolver los IDs en el DTO
        ReservationDTO reservationSaveDTO = ReservationDTO.builder()
                .id(reservationSave.getId())
                .dateReservation(reservationSave.getDateReservation())
                .dateStart(reservationSave.getDateStart())
                .dateEnd(reservationSave.getDateEnd())
                .userId(userT.getId())
                .serviceId(serviceT.getId())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(reservationSaveDTO);
    }

    @Override
    public ResponseEntity<?> update(ReservationDTO reservationDTO, Long id) {
        Optional<Reservation> find = _reservationRepository.findById(id);
        if (find.isPresent()) {
            if (reservationDTO.getDateEnd().isBefore(reservationDTO.getDateStart())) {
                throw new DateValidationException("La fecha de finalización no puede ser anterior a la fecha de inicio.");
            }

            Reservation reservation = find.get();

            // Buscar UserT y ServiceT usando los IDs
            UserT userT = _userTRepository.findById(reservationDTO.getUserId())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            ServiceT serviceT = _serviceRepository.findById(reservationDTO.getServiceId())
                    .orElseThrow(() -> new RuntimeException("Servicio no encontrado"));

            // Actualizar la entidad Reservation con los nuevos valores
            reservation.setDateReservation(LocalDate.now());
            reservation.setDateStart(reservationDTO.getDateStart());
            reservation.setDateEnd(reservationDTO.getDateEnd());
            reservation.setUserT(userT);
            reservation.setServiceT(serviceT);

            Reservation updateReservation = _reservationRepository.save(reservation);

            // Solo devolver los IDs en el DTO
            ReservationDTO updateReservationDTO = ReservationDTO.builder()
                    .id(updateReservation.getId())
                    .dateReservation(updateReservation.getDateReservation())
                    .dateStart(updateReservation.getDateStart())
                    .dateEnd(updateReservation.getDateEnd())
                    .userId(userT.getId())
                    .serviceId(serviceT.getId())
                    .build();

            return ResponseEntity.ok(updateReservationDTO);
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
    public ResponseEntity<List<ReservationDTO>> findByUserId(Long userId) {
        List<Reservation> reservations = _reservationRepository.findByUserTId(userId);
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
    public ResponseEntity<List<ReservationDTO>> findByServiceId(Long serviceId) {
        List<Reservation> reservations = _reservationRepository.findByServiceTId(serviceId);
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


}
