package com.autobots.automanager.modelos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

import com.autobots.automanager.controles.UsuarioControle;
import com.autobots.automanager.entitades.Credencial;
import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;

@Component
public class AdicionadorLinkUsuario implements AdicionadorLink<Usuario> {

	@Autowired(required = false)
	private AdicionadorLinkEndereco adicionadorLinkEndereco;

	@Autowired(required = false)
	private AdicionadorLinkTelefone adicionadorLinkTelefone;

	@Autowired(required = false)
	private AdicionadorLinkDocumento adicionadorLinkDocumento;

	@Autowired(required = false)
	private AdicionadorLinkEmail adicionadorLinkEmail;

	@Autowired(required = false)
	private AdicionadorLinkCredencial adicionadorLinkCredencial;

	@Autowired(required = false)
	private AdicionadorLinkVeiculo adicionadorLinkVeiculo;

	@Autowired(required = false)
	private AdicionadorLinkMercadoria adicionadorLinkMercadoria;

	@Autowired(required = false)
	private AdicionadorLinkVenda adicionadorLinkVenda;

	@Override
	public void adicionarLink(List<Usuario> lista) {
		for (Usuario usuario : lista) {
			adicionarLink(usuario);
		}
	}

	@Override
	public void adicionarLink(Usuario objeto) {
		if (objeto != null && objeto.getId() != null && !objeto.hasLink("self")) {
			Link linkProprio = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuario(objeto.getId()))
					.withSelfRel();
			Link linkColecao = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuarios())
					.withRel("usuarios");
			Link linkAtualizar = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).atualizarUsuario(objeto.getId(), null))
					.withRel("atualizar");
			Link linkExcluir = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).excluirUsuario(objeto.getId()))
					.withRel("excluir");

			Link linkSubEndereco = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterEnderecoUsuario(objeto.getId()))
					.withRel("usuario_endereco");
			Link linkSubDocumentos = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterDocumentosUsuario(objeto.getId()))
					.withRel("usuario_documentos");
			Link linkSubTelefones = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterTelefonesUsuario(objeto.getId()))
					.withRel("usuario_telefones");
			Link linkSubEmails = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterEmailsUsuario(objeto.getId()))
					.withRel("usuario_emails");
			Link linkSubCredenciais = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterCredenciaisUsuario(objeto.getId()))
					.withRel("usuario_credenciais");
			Link linkSubVeiculos = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterVeiculosUsuario(objeto.getId()))
					.withRel("usuario_veiculos");
			Link linkSubVendas = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterVendasUsuario(objeto.getId()))
					.withRel("usuario_vendas");
			Link linkSubMercadorias = WebMvcLinkBuilder
					.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterMercadoriasUsuario(objeto.getId()))
					.withRel("usuario_mercadorias");

			objeto.add(linkProprio, linkColecao, linkAtualizar, linkExcluir,
					linkSubEndereco, linkSubDocumentos, linkSubTelefones,
					linkSubEmails, linkSubCredenciais, linkSubVeiculos,
					linkSubVendas, linkSubMercadorias);

			if (objeto.getEndereco() != null && adicionadorLinkEndereco != null) {
				adicionadorLinkEndereco.adicionarLink(objeto.getEndereco());
			}
			if (objeto.getTelefones() != null && adicionadorLinkTelefone != null) {
				for (Telefone t : objeto.getTelefones()) {
					adicionadorLinkTelefone.adicionarLink(t);
				}
			}
			if (objeto.getDocumentos() != null && adicionadorLinkDocumento != null) {
				for (Documento d : objeto.getDocumentos()) {
					adicionadorLinkDocumento.adicionarLink(d);
				}
			}
			if (objeto.getEmails() != null && adicionadorLinkEmail != null) {
				for (Email e : objeto.getEmails()) {
					adicionadorLinkEmail.adicionarLink(e);
				}
			}
			if (objeto.getCredenciais() != null && adicionadorLinkCredencial != null) {
				for (Credencial c : objeto.getCredenciais()) {
					adicionadorLinkCredencial.adicionarLink(c);
				}
			}
			if (objeto.getVeiculos() != null && adicionadorLinkVeiculo != null) {
				for (Veiculo v : objeto.getVeiculos()) {
					adicionadorLinkVeiculo.adicionarLink(v);
				}
			}
			if (objeto.getMercadorias() != null && adicionadorLinkMercadoria != null) {
				for (Mercadoria m : objeto.getMercadorias()) {
					adicionadorLinkMercadoria.adicionarLink(m);
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
