package br.com.its.cursomc.manager;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;

import br.com.its.cursomc.domain.Pedido;

public abstract class AbstractEmailManager implements EmailManager {

	@Value("${default.sender}")
	private String sender;
	
	@Override
	public void sendOrderConfirmationEmail(Pedido element) {
		SimpleMailMessage sm = prepareSimpleMailMessageFromPedido(element);
		sendEmail(sm);
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido element) {
		SimpleMailMessage sm = new SimpleMailMessage();
		sm.setTo(element.getCliente().getEmail());
		sm.setFrom(sender);
		sm.setSubject("Pedido confirmado! Código: "+element.getId());
		sm.setSentDate(new Date(System.currentTimeMillis()));
		sm.setText(element.toString());
		return sm;
	}
}
