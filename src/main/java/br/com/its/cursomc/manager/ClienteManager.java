package br.com.its.cursomc.manager;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.its.cursomc.dao.ClienteDao;
import br.com.its.cursomc.domain.Cliente;
import br.com.its.cursomc.manager.exception.ObjectNotFoundException;

@Service
public class ClienteManager {

	@Autowired
	private ClienteDao dao;
	
	public Cliente find(Integer id) {
	    Optional<Cliente> obj = dao.findById(id);
	    return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}

}
