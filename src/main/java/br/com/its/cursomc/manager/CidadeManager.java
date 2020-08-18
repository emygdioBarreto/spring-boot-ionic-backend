package br.com.its.cursomc.manager;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.its.cursomc.dao.CidadeDao;
import br.com.its.cursomc.domain.Cidade;

@Service
public class CidadeManager {

	@Autowired
	private CidadeDao dao;
	
	public List<Cidade> findByEstado(Integer estadoId) {
		return dao.findCidades(estadoId);
	}
}
