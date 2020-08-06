package br.com.its.cursomc.manager;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.its.cursomc.dao.ClienteDao;
import br.com.its.cursomc.domain.Cliente;
import br.com.its.cursomc.manager.exception.DataIntegrityException;
import br.com.its.cursomc.manager.exception.ObjectNotFoundException;

@Service
public class ClienteManager {

	@Autowired
	private ClienteDao dao;
	
	public Cliente find(Integer id) {
	    Optional<Cliente> obj = dao.findById(id);
	    return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

	public Cliente insert(Cliente element) {
		element.setId(null);
		return dao.save(element);
	}

	public Cliente update(Cliente element) {
		find(element.getId());
		return dao.save(element);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			dao.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir um cliente com pedidos");
		}
	}
	
	public List<Cliente> findAll() {
		return dao.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page,linesPerPage,Direction.valueOf(direction),orderBy);
		return dao.findAll(pageRequest);
	}
	
//	public Cliente fromDTO(ClienteDTO objDto) {
//		return new Cliente(objDto.getId(),objDto.getNome(),objDto.getEmail(),objDto.getCpfOuCnpj());
//	}
}
