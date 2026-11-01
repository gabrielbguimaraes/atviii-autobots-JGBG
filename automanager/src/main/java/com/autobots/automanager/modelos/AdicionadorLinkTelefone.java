package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.TelefoneControle;
import com.autobots.automanager.entitades.Telefone;

@Component
public class AdicionadorLinkTelefone implements AdicionadorLink<Telefone> {

	@Override
	public void adicionarLink(List<Telefone> lista) {
		for (Telefone telefone : lista) {
			adicionarLink(telefone);
		}
	}

	@Override
	public void adicionarLink(Telefone objeto) {
		if (objeto != null && objeto.getId() != null && !objeto.hasLink("self")) {
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefone(objeto.getId()))
					.withSelfRel();
			Link linkColecao = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).obterTelefones())
					.withRel("telefones");
			Link linkAtualizar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).atualizarTelefone(objeto.getId(), null))
					.withRel("atualizar");
			Link linkExcluir = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(TelefoneControle.class).excluirTelefone(objeto.getId()))
					.withRel("excluir");
			objeto.add(linkProprio, linkColecao, linkAtualizar, linkExcluir);
		}
	}
}
