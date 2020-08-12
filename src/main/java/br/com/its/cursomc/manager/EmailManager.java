package br.com.its.cursomc.manager;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import br.com.its.cursomc.domain.Pedido;

public interface EmailManager {

	void sendOrderConfirmationEmail(Pedido element);
	
	void sendEmail(SimpleMailMessage msg);
	
	void sendOrderConfirmationHtmlEmail(Pedido element); 
	
	void sendHtmlEmail(MimeMessage msg); 
}
