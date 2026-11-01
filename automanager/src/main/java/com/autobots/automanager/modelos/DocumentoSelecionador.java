package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Documento;

@Component
public class DocumentoSelecionador {
	public Documento selecionar(Collection<Documento> documentos, long id) {
		Documento selecionado = null;
		for (Documento documento : documentos) {
			if (documento.getId() != null && documento.getId() == id) {
				selecionado = documento;
				break;
			}
		}
		return selecionado;
	}
}
