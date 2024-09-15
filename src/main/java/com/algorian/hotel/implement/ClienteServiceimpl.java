package com.algorian.hotel.implement;

import com.algorian.hotel.entity.Cliente;
import com.algorian.hotel.models.ClienteDTO;
import com.algorian.hotel.repository.IClienteRepository;
import com.algorian.hotel.service.IClienteService;
import jakarta.validation.Valid;
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
public class ClienteServiceimpl implements IClienteService {

    private final IClienteRepository _userTRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<ClienteDTO>> findAll() {
        List<Cliente> clienteList = _userTRepository.findAll();
        List<ClienteDTO> userTDTOList = clienteList.stream()
                .map(cliente -> ClienteDTO.builder()
                        .id(cliente.getId())
                        .fullName(cliente.getFullName())
                        .email(cliente.getEmail())
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

            ClienteDTO userTDTO = ClienteDTO.builder()
                    .id(cliente.getId())
                    .fullName(cliente.getFullName())
                    .email(cliente.getEmail())
                    .build();

            return ResponseEntity.ok(userTDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @Validated(ClienteDTO.CreateGroup.class)
    @Override
    @Transactional
    public ResponseEntity<?> save(@Validated ClienteDTO clienteDTO) {
        Cliente cliente = Cliente.builder()
                .fullName(clienteDTO.getFullName())
                .email(clienteDTO.getEmail())
                .build();

        Cliente clienteSave = _userTRepository.save(cliente);

        ClienteDTO tdtoSave = ClienteDTO.builder()
                .id(clienteSave.getId())
                .fullName(clienteDTO.getFullName())
                .email(clienteDTO.getEmail())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(tdtoSave);
    }

    @Validated(ClienteDTO.UpdateGroup.class)
    @Override
    @Transactional
    public ResponseEntity<?> update(@Validated ClienteDTO clienteDTO, Long id) {
        Optional<Cliente> find = _userTRepository.findById(id);
        if (find.isPresent()) {
            Cliente cliente = find.get();
            cliente.setFullName(clienteDTO.getFullName());
            cliente.setEmail(clienteDTO.getEmail());

            Cliente updatedCliente = _userTRepository.save(cliente);

            ClienteDTO updatedUserTDTO = ClienteDTO.builder()
                    .id(updatedCliente.getId())
                    .fullName(updatedCliente.getFullName())
                    .email(updatedCliente.getEmail())
                    .build();

            return ResponseEntity.ok(updatedUserTDTO);
        }
        return ResponseEntity.notFound().build();
    }
}
