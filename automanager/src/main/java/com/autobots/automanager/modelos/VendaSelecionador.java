package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Venda;

@Component
public class VendaSelecionador {
	public Venda selecionar(Collection<Venda> vendas, long id) {
		Venda selecionado = null;
		for (Venda venda : vendas) {
			if (venda.getId() != null && venda.getId() == id) {
				selecionado = venda;
				break;
			}
		}
		return selecionado;
	}
}
