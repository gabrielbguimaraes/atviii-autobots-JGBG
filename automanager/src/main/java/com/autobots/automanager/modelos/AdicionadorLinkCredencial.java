package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.CredencialControle;
import com.autobots.automanager.entitades.Credencial;

@Component
public class AdicionadorLinkCredencial implements AdicionadorLink<Credencial> {

	@Override
	public void adicionarLink(List<Credencial> lista) {
		for (Credencial credencial : lista) {
			adicionarLink(credencial);
		}
	}

	@Override
	public void adicionarLink(Credencial objeto) {
		if (objeto != null && objeto.getId() != null && !objeto.hasLink("self")) {
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(CredencialControle.class).obterCredencial(objeto.getId()))
					.withSelfRel();
			Link linkColecao = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(CredencialControle.class).obterCredenciais())
					.withRel("credenciais");
			Link linkAtualizar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(CredencialControle.class).atualizarCredencial(objeto.getId(), null))
					.withRel("atualizar");
			Link linkExcluir = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(CredencialControle.class).excluirCredencial(objeto.getId()))
					.withRel("excluir");
			objeto.add(linkProprio, linkColecao, linkAtualizar, linkExcluir);
		}
	}
}
