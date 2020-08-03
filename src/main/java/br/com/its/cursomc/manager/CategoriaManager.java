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
import br.com.its.cursomc.domain.Categoria;
import br.com.its.cursomc.dto.CategoriaDTO;
import br.com.its.cursomc.manager.exception.DataIntegrityException;
import br.com.its.cursomc.manager.exception.ObjectNotFoundException;

@Service
public class CategoriaManager {
	
	@Autowired
	private CategoriaDao dao;
	
	public Categoria find(Integer id) {
	    Optional<Categoria> obj = dao.findById(id);
	    return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
	
	public Categoria insert(Categoria element) {
		element.setId(null);
		return dao.save(element);
	}

	public Categoria update(Categoria element) {
		find(element.getId());
		return dao.save(element);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			dao.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possivel excluir uma categoria com produtos");
		}
	}
	
	public List<Categoria> findAll() {
		return dao.findAll();
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page,linesPerPage,Direction.valueOf(direction),orderBy);
		return dao.findAll(pageRequest);
	}
	
	public Categoria fromDTO(CategoriaDTO objDto) {
		return new Categoria(objDto.getId(),objDto.getNome());
	}
}