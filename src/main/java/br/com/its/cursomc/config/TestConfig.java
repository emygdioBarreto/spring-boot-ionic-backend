package br.com.its.cursomc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.com.its.cursomc.manager.DBManager;

@Configuration
@Profile("test")
public class TestConfig {

	@Autowired
	private DBManager dbManager;
	
	@Bean
	public boolean instantiateDatabase() throws ParseException {
		dbManager.instantiateTestDatabase();
		return true;
	}
}
