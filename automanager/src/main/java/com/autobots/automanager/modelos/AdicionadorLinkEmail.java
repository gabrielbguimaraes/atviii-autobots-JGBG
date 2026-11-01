package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmailControle;
import com.autobots.automanager.entitades.Email;

@Component
public class AdicionadorLinkEmail implements AdicionadorLink<Email> {

	@Override
	public void adicionarLink(List<Email> lista) {
		for (Email email : lista) {
			adicionarLink(email);
		}
	}

	@Override
	public void adicionarLink(Email objeto) {
		if (objeto != null && objeto.getId() != null && !objeto.hasLink("self")) {
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).obterEmail(objeto.getId()))
					.withSelfRel();
			Link linkColecao = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).obterEmails())
					.withRel("emails");
			Link linkAtualizar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).atualizarEmail(objeto.getId(), null))
					.withRel("atualizar");
			Link linkExcluir = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).excluirEmail(objeto.getId()))
					.withRel("excluir");
			objeto.add(linkProprio, linkColecao, linkAtualizar, linkExcluir);
		}
	}
}
