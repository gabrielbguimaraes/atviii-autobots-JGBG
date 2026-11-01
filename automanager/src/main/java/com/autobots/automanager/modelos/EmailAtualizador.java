package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Email;

@Component
public class EmailAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Email email, Email atualizacao) {
		if (email != null && atualizacao != null) {
			if (!verificador.verificar(atualizacao.getEndereco())) {
				email.setEndereco(atualizacao.getEndereco());
			}
		}
	}

	public void atualizar(Collection<Email> emails, Collection<Email> atualizacoes) {
		if (emails != null && atualizacoes != null) {
			for (Email atualizacao : atualizacoes) {
				for (Email email : emails) {
					if (atualizacao.getId() != null && email.getId() != null) {
						if (atualizacao.getId().equals(email.getId())) {
							atualizar(email, atualizacao);
						}
					}
				}
			}
		}
	}
}
