package br.com.its.cursomc.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.its.cursomc.domain.Cliente;
import br.com.its.cursomc.domain.Pedido;

@Repository
public interface PedidoDao extends JpaRepository<Pedido, Integer> {

	@Transactional(readOnly = true)
	Page<Pedido> findByCliente(Cliente cliente, Pageable pageRequest); 
}
