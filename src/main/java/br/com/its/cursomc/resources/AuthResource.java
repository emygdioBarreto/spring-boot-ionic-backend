package br.com.its.cursomc.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.its.cursomc.dto.EmailDTO;
import br.com.its.cursomc.manager.AuthService;
import br.com.its.cursomc.manager.UserManager;
import br.com.its.cursomc.security.JWTUtil;
import br.com.its.cursomc.security.UserSS;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

	@Autowired
	private AuthService service;
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@RequestMapping(value="/refresh_token", method=RequestMethod.POST) 
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) { 
		UserSS user = UserManager.authenticated(); 
		String token = jwtUtil.generateToken(user.getUsername()); 
		response.addHeader("Authorization", "Bearer " + token); 
		response.addHeader("access-control-expose-headers", "Authorization");
		return ResponseEntity.noContent().build(); 
	}

	@RequestMapping(value="/forgot", method=RequestMethod.POST) 
	public ResponseEntity<Void> forgot(@Valid @RequestBody EmailDTO objDto) { 
		service.sendNewPassword(objDto.getEmail());
		return ResponseEntity.noContent().build(); 
	}
}
