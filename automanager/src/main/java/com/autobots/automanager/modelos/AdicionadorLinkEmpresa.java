package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.EmpresaControle;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Venda;

@Component
public class AdicionadorLinkEmpresa implements AdicionadorLink<Empresa> {

	@Autowired(required = false)
	private AdicionadorLinkEndereco adicionadorLinkEndereco;

	@Autowired(required = false)
	private AdicionadorLinkTelefone adicionadorLinkTelefone;

	@Autowired(required = false)
	private AdicionadorLinkUsuario adicionadorLinkUsuario;

	@Autowired(required = false)
	private AdicionadorLinkMercadoria adicionadorLinkMercadoria;

	@Autowired(required = false)
	private AdicionadorLinkServico adicionadorLinkServico;

	@Autowired(required = false)
	private AdicionadorLinkVenda adicionadorLinkVenda;

	@Override
	public void adicionarLink(List<Empresa> lista) {
		for (Empresa empresa : lista) {
			adicionarLink(empresa);
		}
	}

	@Override
	public void adicionarLink(Empresa objeto) {
		if (objeto != null && objeto.getId() != null && !objeto.hasLink("self")) {
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresa(objeto.getId()))
					.withSelfRel();
			Link linkColecao = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresas())
					.withRel("empresas");
			Link linkAtualizar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).atualizarEmpresa(objeto.getId(), null))
					.withRel("atualizar");
			Link linkExcluir = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).excluirEmpresa(objeto.getId()))
					.withRel("excluir");

			Link linkSubEndereco = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEnderecoEmpresa(objeto.getId()))
					.withRel("empresa_endereco");
			Link linkSubTelefones = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterTelefonesEmpresa(objeto.getId()))
					.withRel("empresa_telefones");
			Link linkSubUsuarios = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterUsuariosEmpresa(objeto.getId()))
					.withRel("empresa_usuarios");
			Link linkSubMercadorias = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterMercadoriasEmpresa(objeto.getId()))
					.withRel("empresa_mercadorias");
			Link linkSubServicos = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterServicosEmpresa(objeto.getId()))
					.withRel("empresa_servicos");
			Link linkSubVendas = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterVendasEmpresa(objeto.getId()))
					.withRel("empresa_vendas");

			objeto.add(linkProprio, linkColecao, linkAtualizar, linkExcluir,
					linkSubEndereco, linkSubTelefones, linkSubUsuarios,
					linkSubMercadorias, linkSubServicos, linkSubVendas);

			if (objeto.getEndereco() != null && adicionadorLinkEndereco != null) {
				adicionadorLinkEndereco.adicionarLink(objeto.getEndereco());
			}
			if (objeto.getTelefones() != null && adicionadorLinkTelefone != null) {
				for (Telefone t : objeto.getTelefones()) {
					adicionadorLinkTelefone.adicionarLink(t);
				}
			}
			if (objeto.getUsuarios() != null && adicionadorLinkUsuario != null) {
				for (Usuario u : objeto.getUsuarios()) {
					adicionadorLinkUsuario.adicionarLink(u);
				}
			}
			if (objeto.getMercadorias() != null && adicionadorLinkMercadoria != null) {
				for (Mercadoria m : objeto.getMercadorias()) {
					adicionadorLinkMercadoria.adicionarLink(m);
				}
			}
			if (objeto.getServicos() != null && adicionadorLinkServico != null) {
				for (Servico s : objeto.getServicos()) {
					adicionadorLinkServico.adicionarLink(s);
				}
			}
			if (objeto.getVendas() != null && adicionadorLinkVenda != null) {
				for (Venda v : objeto.getVendas()) {
					adicionadorLinkVenda.adicionarLink(v);
				}
			}
		}
	}
}
