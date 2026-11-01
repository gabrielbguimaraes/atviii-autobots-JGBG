package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Servico;

@Component
public class ServicoSelecionador {
	public Servico selecionar(Collection<Servico> servicos, long id) {
		Servico selecionado = null;
		for (Servico servico : servicos) {
			if (servico.getId() != null && servico.getId() == id) {
				selecionado = servico;
				break;
			}
		}
		return selecionado;
	}
}
