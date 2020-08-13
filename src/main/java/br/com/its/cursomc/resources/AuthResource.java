package br.com.its.cursomc.resources;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.its.cursomc.manager.UserManager;
import br.com.its.cursomc.security.JWTUtil;
import br.com.its.cursomc.security.UserSS;

@RestController
@RequestMapping(value = "/auth")
public class AuthResource {

	private JWTUtil jwtUtil;
	
	@RequestMapping(value="/refresh_token", method=RequestMethod.POST) 
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) { 
		UserSS user = UserManager.authenticated(); 
		String token = jwtUtil.generateToken(user.getUsername()); 
		response.addHeader("Authorization", "Bearer " + token); 
		return ResponseEntity.noContent().build(); 
	}
}
