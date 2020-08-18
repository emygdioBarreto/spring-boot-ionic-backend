package br.com.its.cursomc.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.its.cursomc.domain.Cidade;
import br.com.its.cursomc.domain.Estado;
import br.com.its.cursomc.dto.CidadeDTO;
import br.com.its.cursomc.dto.EstadoDTO;
import br.com.its.cursomc.manager.CidadeManager;
import br.com.its.cursomc.manager.EstadoManager;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {
	
	@Autowired
	private CidadeManager cidadeManager;
	
	@Autowired
	private EstadoManager manager;

	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<EstadoDTO>> findAll() {
		List<Estado> lista = manager.findAll();
		List<EstadoDTO> listaDto = lista.stream().map(obj -> new EstadoDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaDto);
	}

	@RequestMapping(value = "/{estadoId}/cidades" ,method = RequestMethod.GET)
	public ResponseEntity<List<CidadeDTO>> findCidades(@PathVariable Integer estadoId) {
		List<Cidade> lista = cidadeManager.findByEstado(estadoId);
		List<CidadeDTO> listaDto = lista.stream().map(obj -> new CidadeDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listaDto);
	}
}
