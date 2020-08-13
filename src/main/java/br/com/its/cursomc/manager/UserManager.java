package br.com.its.cursomc.manager;

import org.springframework.security.core.context.SecurityContextHolder;

import br.com.its.cursomc.security.UserSS;

public class UserManager {
	
	public static UserSS authenticated() {
		try {
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		} catch (Exception e) {
			return null;
		}
	}

}
