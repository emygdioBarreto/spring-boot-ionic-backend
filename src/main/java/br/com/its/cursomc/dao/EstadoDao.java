package br.com.its.cursomc.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.its.cursomc.domain.Estado;

@Repository
public interface EstadoDao extends JpaRepository<Estado, Integer> {

}
