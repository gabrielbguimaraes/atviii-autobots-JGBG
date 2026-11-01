package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Usuario;

@Component
public class UsuarioAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();
	private EnderecoAtualizador enderecoAtualizador = new EnderecoAtualizador();
	private DocumentoAtualizador documentoAtualizador = new DocumentoAtualizador();
	private TelefoneAtualizador telefoneAtualizador = new TelefoneAtualizador();
	private EmailAtualizador emailAtualizador = new EmailAtualizador();
	private CredencialAtualizador credencialAtualizador = new CredencialAtualizador();

	public void atualizar(Usuario usuario, Usuario atualizacao) {
		if (usuario != null && atualizacao != null) {
			if (!verificador.verificar(atualizacao.getNome())) {
				usuario.setNome(atualizacao.getNome());
			}
			if (!verificador.verificar(atualizacao.getNomeSocial())) {
				usuario.setNomeSocial(atualizacao.getNomeSocial());
			}
			if (atualizacao.getPerfis() != null && !atualizacao.getPerfis().isEmpty()) {
				usuario.getPerfis().addAll(atualizacao.getPerfis());
			}
			if (atualizacao.getEndereco() != null) {
				if (usuario.getEndereco() == null) {
					usuario.setEndereco(atualizacao.getEndereco());
				} else {
					enderecoAtualizador.atualizar(usuario.getEndereco(), atualizacao.getEndereco());
				}
			}
			if (atualizacao.getDocumentos() != null && !atualizacao.getDocumentos().isEmpty()) {
				documentoAtualizador.atualizar(usuario.getDocumentos(), atualizacao.getDocumentos());
			}
			if (atualizacao.getTelefones() != null && !atualizacao.getTelefones().isEmpty()) {
				telefoneAtualizador.atualizar(usuario.getTelefones(), atualizacao.getTelefones());
			}
			if (atualizacao.getEmails() != null && !atualizacao.getEmails().isEmpty()) {
				emailAtualizador.atualizar(usuario.getEmails(), atualizacao.getEmails());
			}
			if (atualizacao.getCredenciais() != null && !atualizacao.getCredenciais().isEmpty()) {
				credencialAtualizador.atualizar(usuario.getCredenciais(), atualizacao.getCredenciais());
			}
		}
	}

	public void atualizar(Collection<Usuario> usuarios, Collection<Usuario> atualizacoes) {
		if (usuarios != null && atualizacoes != null) {
			for (Usuario atualizacao : atualizacoes) {
				for (Usuario usuario : usuarios) {
					if (atualizacao.getId() != null && usuario.getId() != null) {
						if (atualizacao.getId().equals(usuario.getId())) {
							atualizar(usuario, atualizacao);
						}
					}
				}
			}
		}
	}
}
