package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Telefone;

@Component
public class TelefoneAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Telefone telefone, Telefone atualizacao) {
		if (telefone != null && atualizacao != null) {
			if (!verificador.verificar(atualizacao.getDdd())) {
				telefone.setDdd(atualizacao.getDdd());
			}
			if (!verificador.verificar(atualizacao.getNumero())) {
				telefone.setNumero(atualizacao.getNumero());
			}
		}
	}

	public void atualizar(Collection<Telefone> telefones, Collection<Telefone> atualizacoes) {
		if (telefones != null && atualizacoes != null) {
			for (Telefone atualizacao : atualizacoes) {
				for (Telefone telefone : telefones) {
					if (atualizacao.getId() != null && telefone.getId() != null) {
						if (atualizacao.getId().equals(telefone.getId())) {
							atualizar(telefone, atualizacao);
						}
					}
				}
			}
		}
	}
}
