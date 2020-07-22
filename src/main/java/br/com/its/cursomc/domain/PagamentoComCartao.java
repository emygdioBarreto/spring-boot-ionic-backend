package br.com.its.cursomc.domain;

import javax.persistence.Entity;

import br.com.its.cursomc.domain.enums.EstadoPagamento;

@Entity
public class PagamentoComCartao extends Pagamento {
	private static final long serialVersionUID = 1L;

	private Integer numeroDeParcelas;
	
	public PagamentoComCartao() { }

	public PagamentoComCartao(Integer id, EstadoPagamento situacao, Pedido pedido, Integer numeroDeParcelas) {
		super(id, situacao, pedido);
		this.numeroDeParcelas = numeroDeParcelas;
	}

	public Integer getNumeroDeParcelas() {
		return numeroDeParcelas;
	}
	public void setNumeroDeParcelas(Integer numeroDeParcelas) {
		this.numeroDeParcelas = numeroDeParcelas;
	}
	
}
