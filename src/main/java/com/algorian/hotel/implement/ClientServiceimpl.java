package com.algorian.hotel.implement;

import com.algorian.hotel.entity.Client;
import com.algorian.hotel.models.ClientDTO;
import com.algorian.hotel.repository.IClientRepository;
import com.algorian.hotel.service.IClientService;
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
public class ClientServiceimpl implements IClientService {

    private final IClientRepository _userTRepository;

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<ClientDTO>> findAll() {
        List<Client> clientList = _userTRepository.findAll();
        List<ClientDTO> userTDTOList = clientList.stream()
                .map(client -> ClientDTO.builder()
                        .id(client.getId())
                        .fullName(client.getFullName())
                        .email(client.getEmail())
                        .build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(userTDTOList);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<?> findById(Long id) {
        Optional<Client> find = _userTRepository.findById(id);
        if (find.isPresent()){
            Client client = find.get();

            ClientDTO userTDTO = ClientDTO.builder()
                    .id(client.getId())
                    .fullName(client.getFullName())
                    .email(client.getEmail())
                    .build();

            return ResponseEntity.ok(userTDTO);
        }
        return ResponseEntity.notFound().build();
    }

    @Validated(ClientDTO.CreateGroup.class)
    @Override
    @Transactional
    public ResponseEntity<?> save(@Validated ClientDTO clientDTO) {
        Client client = Client.builder()
                .fullName(clientDTO.getFullName())
                .email(clientDTO.getEmail())
                .build();

        Client clientSave = _userTRepository.save(client);

        ClientDTO tdtoSave = ClientDTO.builder()
                .id(clientSave.getId())
                .fullName(clientDTO.getFullName())
                .email(clientDTO.getEmail())
                .build();
        return ResponseEntity.status(HttpStatus.CREATED).body(tdtoSave);
    }

    @Validated(ClientDTO.UpdateGroup.class)
    @Override
    @Transactional
    public ResponseEntity<?> update(@Validated ClientDTO clientDTO, Long id) {
        Optional<Client> find = _userTRepository.findById(id);
        if (find.isPresent()) {
            Client client = find.get();
            client.setFullName(clientDTO.getFullName());
            client.setEmail(clientDTO.getEmail());

            Client updatedClient = _userTRepository.save(client);

            ClientDTO updatedUserTDTO = ClientDTO.builder()
                    .id(updatedClient.getId())
                    .fullName(updatedClient.getFullName())
                    .email(updatedClient.getEmail())
                    .build();

            return ResponseEntity.ok(updatedUserTDTO);
        }
        return ResponseEntity.notFound().build();
    }
}
