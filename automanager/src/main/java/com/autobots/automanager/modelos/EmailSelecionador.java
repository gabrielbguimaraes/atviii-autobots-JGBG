package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Email;

@Component
public class EmailSelecionador {
	public Email selecionar(Collection<Email> emails, long id) {
		Email selecionado = null;
		for (Email email : emails) {
			if (email.getId() != null && email.getId() == id) {
				selecionado = email;
				break;
			}
		}
		return selecionado;
	}
}
