package br.com.its.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.com.its.cursomc.manager.DBManager;
import br.com.its.cursomc.manager.EmailManager;
import br.com.its.cursomc.manager.SmtpEmailManager;

@Configuration
@Profile("dev")
public class DevConfig {

	@Autowired
	private DBManager dbManager;
	
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		if (!"create".equals(strategy)) {
			return false;
		}
		dbManager.instantiateTestDatabase();
		return true;
	}
	
	@Bean
	public EmailManager emailManager() {
		return new SmtpEmailManager();
	}
}
