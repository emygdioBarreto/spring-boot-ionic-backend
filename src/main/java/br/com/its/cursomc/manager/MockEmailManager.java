package br.com.its.cursomc.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

public class MockEmailManager extends AbstractEmailManager {

	private static final Logger LOG = LoggerFactory.getLogger(MockEmailManager.class);
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Simulando envio de email...");
		LOG.info(msg.toString());
		LOG.info("Email enviado");
	}

}
