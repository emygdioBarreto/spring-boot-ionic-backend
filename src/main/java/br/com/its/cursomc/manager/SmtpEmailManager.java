package br.com.its.cursomc.manager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;

public class SmtpEmailManager extends AbstractEmailManager {

	private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailManager.class);
	
	@Autowired
	private MailSender mailSender;
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Enviando email...");
		mailSender.send(msg);;
		LOG.info("Email enviado");
	}

}
