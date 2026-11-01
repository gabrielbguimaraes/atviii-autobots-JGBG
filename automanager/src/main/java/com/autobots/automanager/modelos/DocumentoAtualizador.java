package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Documento;

@Component
public class DocumentoAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Documento documento, Documento atualizacao) {
		if (documento != null && atualizacao != null) {
			if (atualizacao.getTipo() != null) {
				documento.setTipo(atualizacao.getTipo());
			}
			if (!verificador.verificar(atualizacao.getNumero())) {
				documento.setNumero(atualizacao.getNumero());
			}
			if (atualizacao.getDataEmissao() != null) {
				documento.setDataEmissao(atualizacao.getDataEmissao());
			}
		}
	}

	public void atualizar(Collection<Documento> documentos, Collection<Documento> atualizacoes) {
		if (documentos != null && atualizacoes != null) {
			for (Documento atualizacao : atualizacoes) {
				for (Documento documento : documentos) {
					if (atualizacao.getId() != null && documento.getId() != null) {
						if (atualizacao.getId().equals(documento.getId())) {
							atualizar(documento, atualizacao);
						}
					}
				}
			}
		}
	}
}
