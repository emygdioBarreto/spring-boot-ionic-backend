package br.com.its.cursomc.manager;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.its.cursomc.dao.ItemPedidoDao;
import br.com.its.cursomc.dao.PagamentoDao;
import br.com.its.cursomc.dao.PedidoDao;
import br.com.its.cursomc.domain.ItemPedido;
import br.com.its.cursomc.domain.PagamentoComBoleto;
import br.com.its.cursomc.domain.Pedido;
import br.com.its.cursomc.domain.enums.EstadoPagamento;
import br.com.its.cursomc.dto.PedidoDTO;
import br.com.its.cursomc.manager.exception.DataIntegrityException;
import br.com.its.cursomc.manager.exception.ObjectNotFoundException;

@Service
public class PedidoManager {

	@Autowired
	private BoletoManager boletoManager;
	
	@Autowired
	private ClienteManager clienteManager;
	
	@Autowired
	private ItemPedidoDao itemPedidoDao;
	
	@Autowired
	private PagamentoDao pagamentoDao;
	
	@Autowired
	private PedidoDao dao;
	
	@Autowired
	private ProdutoManager produtoManager;
	
	public Pedido find(Integer id) {
	    Optional<Pedido> obj = dao.findById(id);
	    return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));
	}

	@Transactional
	public Pedido insert(Pedido element) {
		element.setId(null);
		element.setInstante(new Date());
		element.setCliente(clienteManager.find(element.getCliente().getId()));
		element.getPagamento().setSituacao(EstadoPagamento.PENDENTE);
		element.getPagamento().setPedido(element);
		if (element.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) element.getPagamento();
			boletoManager.preencherPagamentoComBoleto(pagto, element.getInstante());
		}
		element = dao.save(element);
		pagamentoDao.save(element.getPagamento());
		for (ItemPedido ip : element.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoManager.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(element);
		}
		itemPedidoDao.saveAll(element.getItens());
		System.out.println(element);
		return element;
	}

	public Pedido update(Pedido element) {
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
	
	public List<Pedido> findAll() {
		return dao.findAll();
	}
	
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
		PageRequest pageRequest = PageRequest.of(page,linesPerPage,Direction.valueOf(direction),orderBy);
		return dao.findAll(pageRequest);
	}
	
	public Pedido fromDTO(PedidoDTO objDto) {
		return new Pedido(objDto.getId(),objDto.getInstante(),objDto.getCliente(),objDto.getEnderecoDeEntrega());
	}
}
