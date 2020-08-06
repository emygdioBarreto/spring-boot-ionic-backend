package br.com.its.cursomc.dto;

import java.io.Serializable;
import java.util.Date;

import br.com.its.cursomc.domain.Cliente;
import br.com.its.cursomc.domain.Endereco;
import br.com.its.cursomc.domain.Pagamento;
import br.com.its.cursomc.domain.Pedido;

public class PedidoDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;
	private Date instante;
	private Pagamento Pagamento;
	private Cliente cliente;
	private Endereco enderecoDeEntrega;
	
	public PedidoDTO() { }

	public PedidoDTO(Pedido pedido) {
		this.id = pedido.getId();
		this.instante = pedido.getInstante();
		this.cliente = pedido.getCliente();
		this.enderecoDeEntrega = pedido.getEnderecoDeEntrega();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Date getInstante() {
		return instante;
	}
	public void setInstante(Date instante) {
		this.instante = instante;
	}
	public Pagamento getPagamento() {
		return Pagamento;
	}
	public void setPagamento(Pagamento pagamento) {
		Pagamento = pagamento;
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	public Endereco getEnderecoDeEntrega() {
		return enderecoDeEntrega;
	}
	public void setEnderecoDeEntrega(Endereco enderecoDeEntrega) {
		this.enderecoDeEntrega = enderecoDeEntrega;
	}
}
