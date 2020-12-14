package br.com.its.cursomc.resources;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.its.cursomc.manager.CidadeManager;

@RestController
@RequestMapping(value = "/cidades")
public class CidadeResource {

	@SuppressWarnings("unused")
	@Autowired
	private CidadeManager manager;
}
