package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Venda;

@Component
public class VendaAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Venda venda, Venda atualizacao) {
		if (venda != null && atualizacao != null) {
			if (!verificador.verificar(atualizacao.getIdentificacao())) {
				venda.setIdentificacao(atualizacao.getIdentificacao());
			}
			if (atualizacao.getCadastro() != null) {
				venda.setCadastro(atualizacao.getCadastro());
			}
			if (atualizacao.getCliente() != null) {
				venda.setCliente(atualizacao.getCliente());
			}
			if (atualizacao.getFuncionario() != null) {
				venda.setFuncionario(atualizacao.getFuncionario());
			}
			if (atualizacao.getVeiculo() != null) {
				venda.setVeiculo(atualizacao.getVeiculo());
			}
			if (atualizacao.getMercadorias() != null && !atualizacao.getMercadorias().isEmpty()) {
				venda.getMercadorias().addAll(atualizacao.getMercadorias());
			}
			if (atualizacao.getServicos() != null && !atualizacao.getServicos().isEmpty()) {
				venda.getServicos().addAll(atualizacao.getServicos());
			}
		}
	}

	public void atualizar(Collection<Venda> vendas, Collection<Venda> atualizacoes) {
		if (vendas != null && atualizacoes != null) {
			for (Venda atualizacao : atualizacoes) {
				for (Venda venda : vendas) {
					if (atualizacao.getId() != null && venda.getId() != null) {
						if (atualizacao.getId().equals(venda.getId())) {
							atualizar(venda, atualizacao);
						}
					}
				}
			}
		}
	}
}
