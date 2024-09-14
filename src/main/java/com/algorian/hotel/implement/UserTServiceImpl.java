package com.algorian.hotel.implement;

import com.algorian.hotel.entity.UserT;
import com.algorian.hotel.models.UserTDTO;
import com.algorian.hotel.repository.IUserTRepository;
import com.algorian.hotel.service.IUserTService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor

@Service
public class UserTServiceImpl implements IUserTService {

    private final IUserTRepository _userTRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<UserTDTO>> findAll() {
        List<UserT> userTList = _userTRepository.findAll();
        List<UserTDTO> userTDTOList = userTList.stream()
                .map(userT -> UserTDTO.builder()
                        .id(userT.getId())
                        .email(userT.getEmail())
                        .password(userT.getPassword())
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
                    .email(userT.getEmail())
                    .password(userT.getPassword())
                    .build();
            return ResponseEntity.ok(userTDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @Validated(UserTDTO.CreateGroup.class)
    @Override
    @Transactional
    public ResponseEntity<?> save(@Validated UserTDTO userTDTO) {
        UserT userT = UserT.builder()
                .email(userTDTO.getEmail())
                .password(userTDTO.getPassword())
                .build();
        UserT save = _userTRepository.save(userT);

        UserTDTO userTDTOSave = UserTDTO.builder()
                .id(save.getId())
                .email(save.getEmail())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(userTDTOSave);
    }

    @Validated(UserTDTO.UpdateGroup.class)
    @Override
    @Transactional
    public ResponseEntity<?> update(UserTDTO userTDTO, Long id) {
        Optional<UserT> find = _userTRepository.findById(id);
        if (find.isPresent()){
            UserT userT = find.get();
            userT.setEmail(userTDTO.getEmail());
            userT.setPassword(userTDTO.getPassword());

            UserT userTSave = _userTRepository.save(userT);

            UserTDTO userTDTOSave = UserTDTO.builder()
                    .email(userTSave.getEmail())
                    .build();

            return ResponseEntity.ok(userTDTOSave);
        }
        return ResponseEntity.notFound().build();
    }
}
