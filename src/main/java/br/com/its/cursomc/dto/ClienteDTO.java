package br.com.its.cursomc.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;

import br.com.its.cursomc.domain.Cliente;

public class ClienteDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Integer id;

	@NotEmpty(message="Preenchimento obrigatório")
	@Length(min=5, max=120, message="O tamanho deve ser entre 5 e 120 caracteres")
	private String nome;

	@NotEmpty(message="Preenchimento obrigatório")
	@Email(message="Email inválido")
	private String email;
//	private String cpfOuCnpj;
//	private Integer tipo;

	public ClienteDTO() { }

	public ClienteDTO(Cliente cliente) {
		this.id = cliente.getId();
		this.nome = cliente.getNome();
		this.email = cliente.getEmail();
//		this.cpfOuCnpj = cliente.getCpfOuCnpj();
//		this.tipo = cliente.getTipo().getCodigo();
	}

	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
//	public String getCpfOuCnpj() {
//		return cpfOuCnpj;
//	}
//	public void setCpfOuCnpj(String cpfOuCnpj) {
//		this.cpfOuCnpj = cpfOuCnpj;
//	}
//	public Integer getTipo() {
//		return tipo;
//	}
//	public void setTipo(Integer tipo) {
//		this.tipo = tipo;
//	}
}