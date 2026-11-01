package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.ServicoControle;
import com.autobots.automanager.entitades.Servico;

@Component
public class AdicionadorLinkServico implements AdicionadorLink<Servico> {

	@Override
	public void adicionarLink(List<Servico> lista) {
		for (Servico servico : lista) {
			adicionarLink(servico);
		}
	}

	@Override
	public void adicionarLink(Servico objeto) {
		if (objeto != null && objeto.getId() != null && !objeto.hasLink("self")) {
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServico(objeto.getId()))
					.withSelfRel();
			Link linkColecao = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServicos())
					.withRel("servicos");
			Link linkAtualizar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).atualizarServico(objeto.getId(), null))
					.withRel("atualizar");
			Link linkExcluir = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).excluirServico(objeto.getId()))
					.withRel("excluir");
			objeto.add(linkProprio, linkColecao, linkAtualizar, linkExcluir);
		}
	}
}
