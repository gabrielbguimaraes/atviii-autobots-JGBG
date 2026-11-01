package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Veiculo;

@Component
public class VeiculoAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Veiculo veiculo, Veiculo atualizacao) {
		if (veiculo != null && atualizacao != null) {
			if (atualizacao.getTipo() != null) {
				veiculo.setTipo(atualizacao.getTipo());
			}
			if (!verificador.verificar(atualizacao.getModelo())) {
				veiculo.setModelo(atualizacao.getModelo());
			}
			if (!verificador.verificar(atualizacao.getPlaca())) {
				veiculo.setPlaca(atualizacao.getPlaca());
			}
		}
	}

	public void atualizar(Collection<Veiculo> veiculos, Collection<Veiculo> atualizacoes) {
		if (veiculos != null && atualizacoes != null) {
			for (Veiculo atualizacao : atualizacoes) {
				for (Veiculo veiculo : veiculos) {
					if (atualizacao.getId() != null && veiculo.getId() != null) {
						if (atualizacao.getId().equals(veiculo.getId())) {
							atualizar(veiculo, atualizacao);
						}
					}
				}
			}
		}
	}
}
