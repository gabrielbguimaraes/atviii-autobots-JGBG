package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Mercadoria;

@Component
public class MercadoriaSelecionador {
	public Mercadoria selecionar(Collection<Mercadoria> mercadorias, long id) {
		Mercadoria selecionado = null;
		for (Mercadoria mercadoria : mercadorias) {
			if (mercadoria.getId() != null && mercadoria.getId() == id) {
				selecionado = mercadoria;
				break;
			}
		}
		return selecionado;
	}
}
