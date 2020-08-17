package br.com.its.cursomc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import br.com.its.cursomc.manager.S3Manager;

@SpringBootApplication
public class CursomcApplication implements CommandLineRunner {

	@Autowired
	private S3Manager s3Manager;
	
	public static void main(String[] args) {
		SpringApplication.run(CursomcApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		s3Manager.updoadFile("C:\\Temp\\SGNE\\codigos\\sgne\\src\\main\\webapp\\img\\em_construcao.jpg");
	}

}
