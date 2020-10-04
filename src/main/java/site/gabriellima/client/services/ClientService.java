package site.gabriellima.client.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.gabriellima.client.dto.ClientDTO;
import site.gabriellima.client.entities.Client;
import site.gabriellima.client.exceptions.ResourceNotFoundException;
import site.gabriellima.client.repositories.ClientRepository;

import java.util.Optional;

@Service
@Transactional
public class ClientService {

  private static final String ENTITY_NOT_FOUND = "Entity not found";

  private final ClientRepository repository;

  @Autowired
  public ClientService(ClientRepository clientRepository) {
    this.repository = clientRepository;
  }

  @Transactional(readOnly = true)
  public Page<ClientDTO> findAllPaged(Pageable pageable) {
    return repository.findAll(pageable).map(ClientDTO::new);
  }

  @Transactional(readOnly = true)
  public ClientDTO findById(Long id) {
    return new ClientDTO(
        repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(ENTITY_NOT_FOUND)));
  }

  public ClientDTO insert(ClientDTO dto) {
    Client client = new Client();
    copyClientDtoToClient(dto, client);
    return new ClientDTO(repository.save(client));
  }

  public ClientDTO update(Long id, ClientDTO dto) {
    return Optional.of(repository.findById(id))
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(
            client -> {
              copyClientDtoToClient(dto, client);
              return client;
            })
        .map(ClientDTO::new)
        .orElseThrow(() -> new ResourceNotFoundException(ENTITY_NOT_FOUND));
  }

  public void delete(Long id) {
    repository
        .findById(id)
        .ifPresentOrElse(
            repository::delete,
            () -> {
              throw new ResourceNotFoundException(ENTITY_NOT_FOUND);
            });
  }

  private void copyClientDtoToClient(ClientDTO dto, Client client) {
    client.setCpf(dto.getCpf());
    client.setBirthDate(dto.getBirthDate());
    client.setChildren(dto.getChildren());
    client.setIncome(dto.getIncome());
    client.setName(dto.getName());
  }
}
