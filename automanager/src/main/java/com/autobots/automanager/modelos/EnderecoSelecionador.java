package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Endereco;

@Component
public class EnderecoSelecionador {
	public Endereco selecionar(Collection<Endereco> enderecos, long id) {
		Endereco selecionado = null;
		for (Endereco endereco : enderecos) {
			if (endereco.getId() != null && endereco.getId() == id) {
				selecionado = endereco;
				break;
			}
		}
		return selecionado;
	}
}
