package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.VendaControle;
import com.autobots.automanager.entitades.Venda;

@Component
public class AdicionadorLinkVenda implements AdicionadorLink<Venda> {

	@Override
	public void adicionarLink(List<Venda> lista) {
		for (Venda venda : lista) {
			adicionarLink(venda);
		}
	}

	@Override
	public void adicionarLink(Venda objeto) {
		if (objeto != null && objeto.getId() != null && !objeto.hasLink("self")) {
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVenda(objeto.getId()))
					.withSelfRel();
			Link linkColecao = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVendas())
					.withRel("vendas");
			Link linkAtualizar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).atualizarVenda(objeto.getId(), null))
					.withRel("atualizar");
			Link linkExcluir = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).excluirVenda(objeto.getId()))
					.withRel("excluir");
			objeto.add(linkProprio, linkColecao, linkAtualizar, linkExcluir);
		}
	}
}
