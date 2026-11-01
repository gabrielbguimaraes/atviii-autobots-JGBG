package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Telefone;

@Component
public class TelefoneSelecionador {
	public Telefone selecionar(Collection<Telefone> telefones, long id) {
		Telefone selecionado = null;
		for (Telefone telefone : telefones) {
			if (telefone.getId() != null && telefone.getId() == id) {
				selecionado = telefone;
				break;
			}
		}
		return selecionado;
	}
}
