package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Veiculo;

@Component
public class VeiculoSelecionador {
	public Veiculo selecionar(Collection<Veiculo> veiculos, long id) {
		Veiculo selecionado = null;
		for (Veiculo veiculo : veiculos) {
			if (veiculo.getId() != null && veiculo.getId() == id) {
				selecionado = veiculo;
				break;
			}
		}
		return selecionado;
	}
}
