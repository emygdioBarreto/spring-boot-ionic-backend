package br.com.its.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.its.cursomc.domain.Cliente;
import br.com.its.cursomc.dto.ClienteDTO;
import br.com.its.cursomc.dto.NovoClienteDto;
import br.com.its.cursomc.manager.ClienteManager;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {
	
	@Autowired
	private ClienteManager manager;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Cliente> find(@PathVariable Integer id) {
		Cliente cliente = manager.find(id);
		return ResponseEntity.ok().body(cliente);
	}

	@RequestMapping(value = "/email", method = RequestMethod.GET)
	public ResponseEntity<Cliente> find(@RequestParam(value = "value") String email) {
		Cliente cliente = manager.findByEmail(email);
		return ResponseEntity.ok().body(cliente);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody NovoClienteDto objDto) {
		Cliente obj = manager.fromDTO(objDto);
		obj = manager.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO objDto, @PathVariable Integer id) {
		Cliente obj = manager.fromDTO(objDto);
		obj.setId(id);
		obj = manager.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		manager.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<ClienteDTO>> findAll() {
		List<Cliente> listaCliente = manager.findAll();
		List<ClienteDTO> listaClienteDTO = listaCliente.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaClienteDTO);
	}
	
	@PreAuthorize("hasAnyRole('ADMIN')")
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<ClienteDTO>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page, 
										  		     @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, 
													 @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy, 
													 @RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		Page<Cliente> listaCliente = manager.findPage(page, linesPerPage, orderBy, direction);
		Page<ClienteDTO> listaClienteDTO = listaCliente.map(obj -> new ClienteDTO(obj));
		return ResponseEntity.ok().body(listaClienteDTO);
	}

	@RequestMapping(value = "/picture", method = RequestMethod.POST)
	public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name = "file") MultipartFile file) {
		URI uri =  manager.uploadProfilePicture(file);
		return ResponseEntity.created(uri).build();
	}
}
