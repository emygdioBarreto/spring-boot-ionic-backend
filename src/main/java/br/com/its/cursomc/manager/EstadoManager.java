package br.com.its.cursomc.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.its.cursomc.dao.EstadoDao;
import br.com.its.cursomc.domain.Estado;

@Service
public class EstadoManager {
	
	@Autowired
	private EstadoDao dao;

	public List<Estado> findAll() {
		return dao.findAllByOrderByNome();
	}
}
