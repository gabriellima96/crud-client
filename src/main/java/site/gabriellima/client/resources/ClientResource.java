package site.gabriellima.client.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import site.gabriellima.client.dto.ClientDTO;
import site.gabriellima.client.services.ClientService;

import java.net.URI;

@RestController
@RequestMapping("/clients")
public class ClientResource {

  @Autowired private ClientService service;

  @GetMapping
  public ResponseEntity<Page<ClientDTO>> findAllPaged(
      @RequestParam(value = "page", defaultValue = "0") Integer page,
      @RequestParam(value = "linesPerPage", defaultValue = "12") Integer linesPerPage,
      @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
      @RequestParam(value = "direction", defaultValue = "DESC") String direction) {
    PageRequest pageRequest =
        PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
    Page<ClientDTO> list = service.findAllPaged(pageRequest);
    return ResponseEntity.ok().body(list);
  }

  @GetMapping("/{id}")
  public ResponseEntity<ClientDTO> findById(@PathVariable Long id) {
    ClientDTO dto = service.findById(id);
    return ResponseEntity.ok().body(dto);
  }

  @PostMapping
  public ResponseEntity<ClientDTO> insert(@RequestBody ClientDTO dto) {
    dto = service.insert(dto);
    URI uri =
        ServletUriComponentsBuilder.fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(dto.getId())
            .toUri();
    return ResponseEntity.created(uri).body(dto);
  }

  @PutMapping("/{id}")
  public ResponseEntity<ClientDTO> update(@PathVariable Long id, @RequestBody ClientDTO dto) {
    dto = service.update(id, dto);
    return ResponseEntity.ok().body(dto);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ClientDTO> update(@PathVariable Long id) {
    service.delete(id);
    return ResponseEntity.noContent().build();
  }
}
