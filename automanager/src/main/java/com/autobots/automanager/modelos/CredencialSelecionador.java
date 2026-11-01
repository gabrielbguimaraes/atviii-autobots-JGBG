package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Credencial;

@Component
public class CredencialSelecionador {
	public Credencial selecionar(Collection<Credencial> credenciais, long id) {
		Credencial selecionado = null;
		for (Credencial credencial : credenciais) {
			if (credencial.getId() != null && credencial.getId() == id) {
				selecionado = credencial;
				break;
			}
		}
		return selecionado;
	}
}
