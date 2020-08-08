package br.com.its.cursomc.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.its.cursomc.domain.Categoria;
import br.com.its.cursomc.domain.Produto;

@Repository
public interface ProdutoDao extends JpaRepository<Produto,Integer> {

  // https://docs.spring.io/spring-data/jpa/docs/current/reference/html/

	//	@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	@Transactional(readOnly = true)
	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);
}
