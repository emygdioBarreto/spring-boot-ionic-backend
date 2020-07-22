package br.com.its.cursomc.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.its.cursomc.domain.Produto;

@Repository
public interface ProdutoDao extends JpaRepository<Produto,Integer> {

}
