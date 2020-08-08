package br.com.its.cursomc.manager.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import br.com.its.cursomc.dao.ClienteDao;
import br.com.its.cursomc.domain.Cliente;
import br.com.its.cursomc.dto.ClienteDTO;
import br.com.its.cursomc.resources.exception.FieldMessage;

public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {

	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ClienteDao dao;
	
	@Override
	public void initialize(ClienteUpdate ann) { }
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id"));
		
		List<FieldMessage> lista = new ArrayList<>();
		
		Cliente cli = dao.findByEmail(objDto.getEmail());
		if (cli != null && !cli.getId().equals(uriId)) {
			lista.add(new FieldMessage("email", "Email já existente"));
		}
		
		for (FieldMessage field : lista) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(field.getMessage()).addPropertyNode(field.getFieldName()).addConstraintViolation();
		}
		return lista.isEmpty();
	}
}
