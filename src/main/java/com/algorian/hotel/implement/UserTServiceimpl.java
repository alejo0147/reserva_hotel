package com.algorian.hotel.implement;

import com.algorian.hotel.entity.Cliente;
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
        List<Cliente> clienteList = _userTRepository.findAll();
        List<UserTDTO> userTDTOList = clienteList.stream()
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
        Optional<Cliente> find = _userTRepository.findById(id);
        if (find.isPresent()){
            Cliente cliente = find.get();

            UserTDTO userTDTO = UserTDTO.builder()
                    .id(cliente.getId())
                    .fullName(cliente.getFullName())
                    .email(cliente.getEmail())
                    .build();

            return ResponseEntity.ok(userTDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> save(@Valid  UserTDTO userTDTO) {
        Cliente cliente = Cliente.builder()
                .fullName(userTDTO.getFullName())
                .email(userTDTO.getEmail())
                .password(userTDTO.getPassword())
                .build();

        Cliente clienteSave = _userTRepository.save(cliente);

        UserTDTO tdtoSave = UserTDTO.builder()
                .id(clienteSave.getId())
                .fullName(userTDTO.getFullName())
                .email(userTDTO.getEmail())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(tdtoSave);
    }

    @Override
    @Transactional
    public ResponseEntity<?> update(@Valid UserTDTO userTDTO, Long id) {
        Optional<Cliente> find = _userTRepository.findById(id);
        if (find.isPresent()) {
            Cliente cliente = find.get();
            cliente.setFullName(userTDTO.getFullName());
            cliente.setEmail(userTDTO.getEmail());
            cliente.setPassword(userTDTO.getPassword());

            Cliente updatedCliente = _userTRepository.save(cliente);

            UserTDTO updatedUserTDTO = UserTDTO.builder()
                    .fullName(updatedCliente.getFullName())
                    .email(updatedCliente.getEmail())
                    .build();

            return ResponseEntity.ok(updatedUserTDTO);
        }
        return ResponseEntity.notFound().build();
    }
}
