package com.algorian.hotel.implement;

import com.algorian.hotel.entity.UserT;
import com.algorian.hotel.models.UserTDTO;
import com.algorian.hotel.repository.IUserTRepository;
import com.algorian.hotel.service.IUserTService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@Service
public class UserTServiceimpl implements IUserTService {

    private final IUserTRepository _userTRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<UserTDTO>> findAll() {
        List<UserT> userTList = _userTRepository.findAll();
        List<UserTDTO> userTDTOList = userTList.stream()
                .map(userT -> UserTDTO.builder()
                        .id(userT.getId())
                        .fullName(userT.getFullName())
                        .email(userT.getEmail())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(userTDTOList);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> findById(Long id) {
        Optional<UserT> find = _userTRepository.findById(id);
        if (find.isPresent()){
            UserT userT = find.get();

            UserTDTO userTDTO = UserTDTO.builder()
                    .id(userT.getId())
                    .fullName(userT.getFullName())
                    .email(userT.getEmail())
                    .build();

            return ResponseEntity.ok(userTDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> save(@Valid  UserTDTO userTDTO) {
        UserT userT = UserT.builder()
                .fullName(userTDTO.getFullName())
                .email(userTDTO.getEmail())
                .password(userTDTO.getPassword())
                .build();

        UserT userTSave = _userTRepository.save(userT);

        UserTDTO tdtoSave = UserTDTO.builder()
                .id(userTSave.getId())
                .fullName(userTDTO.getFullName())
                .email(userTDTO.getEmail())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(tdtoSave);
    }

    @Override
    @Transactional
    public ResponseEntity<?> update(@Valid UserTDTO userTDTO, Long id) {
        Optional<UserT> find = _userTRepository.findById(id);
        if (find.isPresent()) {
            UserT userT = find.get();
            userT.setFullName(userTDTO.getFullName());
            userT.setEmail(userTDTO.getEmail());
            userT.setPassword(userTDTO.getPassword());

            UserT updatedUserT = _userTRepository.save(userT);

            UserTDTO updatedUserTDTO = UserTDTO.builder()
                    .fullName(updatedUserT.getFullName())
                    .email(updatedUserT.getEmail())
                    .build();

            return ResponseEntity.ok(updatedUserTDTO);
        }
        return ResponseEntity.notFound().build();
    }
}
