package br.com.its.cursomc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.its.cursomc.domain.Pedido;
import br.com.its.cursomc.dto.PedidoDTO;
import br.com.its.cursomc.manager.PedidoManager;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {
	
	@Autowired
	private PedidoManager manager;
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Pedido> find(@PathVariable Integer id) {
		Pedido pedido = manager.find(id);
		return ResponseEntity.ok().body(pedido);
	}

	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody PedidoDTO objDto) {
		Pedido obj = manager.fromDTO(objDto);
		obj = manager.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody PedidoDTO objDto, @PathVariable Integer id) {
		Pedido obj = manager.fromDTO(objDto);
		obj.setId(id);
		obj = manager.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> delete(@PathVariable Integer id) {
		manager.delete(id);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<PedidoDTO>> findAll() {
		List<Pedido> listaPedido = manager.findAll();
		List<PedidoDTO> listaPedidoDTO = listaPedido.stream().map(obj -> new PedidoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaPedidoDTO);
	}
	
	@RequestMapping(value = "/page", method = RequestMethod.GET)
	public ResponseEntity<Page<PedidoDTO>> findPage(@RequestParam(value = "page", defaultValue = "0") Integer page, 
													@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, 
													@RequestParam(value = "orderBy", defaultValue = "nome") String orderBy, 
													@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		Page<Pedido> listaPedido = manager.findPage(page, linesPerPage, orderBy, direction);
		Page<PedidoDTO> listaPedidoDTO = listaPedido.map(obj -> new PedidoDTO(obj));
		return ResponseEntity.ok().body(listaPedidoDTO);
	}
}
