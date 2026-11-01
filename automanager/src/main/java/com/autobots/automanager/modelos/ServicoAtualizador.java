package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Servico;

@Component
public class ServicoAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Servico servico, Servico atualizacao) {
		if (servico != null && atualizacao != null) {
			if (!verificador.verificar(atualizacao.getNome())) {
				servico.setNome(atualizacao.getNome());
			}
			if (!verificador.verificar(atualizacao.getDescricao())) {
				servico.setDescricao(atualizacao.getDescricao());
			}
			if (atualizacao.getValor() > 0) {
				servico.setValor(atualizacao.getValor());
			}
		}
	}

	public void atualizar(Collection<Servico> servicos, Collection<Servico> atualizacoes) {
		if (servicos != null && atualizacoes != null) {
			for (Servico atualizacao : atualizacoes) {
				for (Servico servico : servicos) {
					if (atualizacao.getId() != null && servico.getId() != null) {
						if (atualizacao.getId().equals(servico.getId())) {
							atualizar(servico, atualizacao);
						}
					}
				}
			}
		}
	}
}
