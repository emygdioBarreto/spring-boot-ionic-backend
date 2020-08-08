package br.com.its.cursomc.manager.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.its.cursomc.dao.ClienteDao;
import br.com.its.cursomc.domain.Cliente;
import br.com.its.cursomc.domain.enums.TipoCliente;
import br.com.its.cursomc.dto.NovoClienteDto;
import br.com.its.cursomc.manager.validation.utils.BR;
import br.com.its.cursomc.resources.exception.FieldMessage;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, NovoClienteDto> {

	@Autowired
	private ClienteDao dao;
	
	@Override
	public void initialize(ClienteInsert ann) { }
	
	@Override
	public boolean isValid(NovoClienteDto objDto, ConstraintValidatorContext context) {
		List<FieldMessage> lista = new ArrayList<>();
		
		Cliente cli = dao.findByEmail(objDto.getEmail());
		if (cli != null) {
			lista.add(new FieldMessage("email", "Email já existente"));
		}
		
		if (objDto.getTipo().equals(TipoCliente.PESSOA_FISICA.getCodigo()) && !BR.isValidCPF(objDto.getCpfOuCnpj())) {
			lista.add(new FieldMessage("cpfOuCnpj", "CPF inválido"));
		}

		if (objDto.getTipo().equals(TipoCliente.PESSOA_JURIDICA.getCodigo()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
			lista.add(new FieldMessage("cpfOuCnpj", "CNPJ inválido"));
		}

		for (FieldMessage field : lista) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(field.getMessage()).addPropertyNode(field.getFieldName()).addConstraintViolation();
		}
		return lista.isEmpty();
	}
}
