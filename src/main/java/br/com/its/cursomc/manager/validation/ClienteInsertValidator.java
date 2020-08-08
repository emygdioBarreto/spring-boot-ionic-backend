package br.com.its.cursomc.manager.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import br.com.its.cursomc.domain.enums.TipoCliente;
import br.com.its.cursomc.dto.NovoClienteDto;
import br.com.its.cursomc.manager.validation.utils.BR;
import br.com.its.cursomc.resources.exception.FieldMessage;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, NovoClienteDto> {

	@Override
	public void initialize(ClienteInsert ann) { }
	
	@Override
	public boolean isValid(NovoClienteDto objDto, ConstraintValidatorContext context) {
		List<FieldMessage> lista = new ArrayList<>();
		
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
