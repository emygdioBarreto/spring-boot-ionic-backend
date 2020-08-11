package br.com.its.cursomc.manager;

import org.springframework.mail.SimpleMailMessage;

import br.com.its.cursomc.domain.Pedido;

public interface EmailManager {

	void sendOrderConfirmationEmail(Pedido element);
	
	void sendEmail(SimpleMailMessage msg);
}
