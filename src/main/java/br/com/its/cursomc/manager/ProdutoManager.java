package br.com.its.cursomc.manager;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.its.cursomc.dao.CategoriaDao;
import br.com.its.cursomc.dao.ProdutoDao;
import br.com.its.cursomc.domain.Categoria;
import br.com.its.cursomc.domain.Produto;
import br.com.its.cursomc.manager.exception.DataIntegrityException;
import br.com.its.cursomc.manager.exception.ObjectNotFoundException;

@Service
public class ProdutoManager {

	@Autowired
	private CategoriaDao categoriaDao;
	
	@Autowired
	private ProdutoDao dao;
	
	public Produto find(Integer id) {
	    Optional<Produto> obj = dao.findById(id);
	    return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));
	}

	public Produto insert(Produto element) {
		element.setId(null);
		return dao.save(element);
	}

	public Produto update(Produto element) {
		find(element.getId());
		return dao.save(element);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			dao.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir um pedido com itens");
		}
	}
	
	public List<Produto> findAll() {
		return dao.findAll();
	}
	
	public Page<Produto> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page,linesPerPage,Direction.valueOf(direction),orderBy);
		return dao.findAll(pageRequest);
	}

	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page,linesPerPage,Direction.valueOf(direction),orderBy);
		List<Categoria> categorias = categoriaDao.findAllById(ids);
		return dao.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);
	}
}
