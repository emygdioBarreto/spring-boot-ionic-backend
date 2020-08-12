package br.com.its.cursomc.manager;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

public class SmtpEmailManager extends AbstractEmailManager {

	private static final Logger LOG = LoggerFactory.getLogger(SmtpEmailManager.class);
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Autowired
	private MailSender mailSender;
	
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Enviando email...");
		mailSender.send(msg);;
		LOG.info("Email enviado");
	}

	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOG.info("Enviando email HTML...");
		javaMailSender.send(msg);;
		LOG.info("Email enviado");
	}

}
