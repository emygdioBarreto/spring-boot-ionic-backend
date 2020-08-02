package br.com.its.cursomc.manager;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.its.cursomc.dao.PedidoDao;
import br.com.its.cursomc.domain.Pedido;
import br.com.its.cursomc.manager.exception.ObjectNotFoundException;

@Service
public class PedidoManager {

	@Autowired
	private PedidoDao dao;
	
	public Pedido find(Integer id) {
	    Optional<Pedido> obj = dao.findById(id);
	    return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

}
