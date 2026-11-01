package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Mercadoria;

@Component
public class MercadoriaAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Mercadoria mercadoria, Mercadoria atualizacao) {
		if (mercadoria != null && atualizacao != null) {
			if (!verificador.verificar(atualizacao.getNome())) {
				mercadoria.setNome(atualizacao.getNome());
			}
			if (!verificador.verificar(atualizacao.getDescricao())) {
				mercadoria.setDescricao(atualizacao.getDescricao());
			}
			if (atualizacao.getValor() > 0) {
				mercadoria.setValor(atualizacao.getValor());
			}
			if (atualizacao.getQuantidade() >= 0) {
				mercadoria.setQuantidade(atualizacao.getQuantidade());
			}
			if (atualizacao.getValidade() != null) {
				mercadoria.setValidade(atualizacao.getValidade());
			}
			if (atualizacao.getFabricao() != null) {
				mercadoria.setFabricao(atualizacao.getFabricao());
			}
			if (atualizacao.getCadastro() != null) {
				mercadoria.setCadastro(atualizacao.getCadastro());
			}
		}
	}

	public void atualizar(Collection<Mercadoria> mercadorias, Collection<Mercadoria> atualizacoes) {
		if (mercadorias != null && atualizacoes != null) {
			for (Mercadoria atualizacao : atualizacoes) {
				for (Mercadoria mercadoria : mercadorias) {
					if (atualizacao.getId() != null && mercadoria.getId() != null) {
						if (atualizacao.getId().equals(mercadoria.getId())) {
							atualizar(mercadoria, atualizacao);
						}
					}
				}
			}
		}
	}
}
