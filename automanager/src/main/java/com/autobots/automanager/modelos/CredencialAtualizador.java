package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Credencial;
import com.autobots.automanager.entitades.CredencialCodigoBarra;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;

@Component
public class CredencialAtualizador {
	private StringVerificadorNulo verificador = new StringVerificadorNulo();

	public void atualizar(Credencial credencial, Credencial atualizacao) {
		if (credencial != null && atualizacao != null) {
			if (atualizacao.getUltimoAcesso() != null) {
				credencial.setUltimoAcesso(atualizacao.getUltimoAcesso());
			}
			credencial.setInativo(atualizacao.isInativo());

			if (credencial instanceof CredencialUsuarioSenha && atualizacao instanceof CredencialUsuarioSenha) {
				CredencialUsuarioSenha cOriginal = (CredencialUsuarioSenha) credencial;
				CredencialUsuarioSenha cAtualizacao = (CredencialUsuarioSenha) atualizacao;
				if (!verificador.verificar(cAtualizacao.getNomeUsuario())) {
					cOriginal.setNomeUsuario(cAtualizacao.getNomeUsuario());
				}
				if (!verificador.verificar(cAtualizacao.getSenha())) {
					cOriginal.setSenha(cAtualizacao.getSenha());
				}
			} else if (credencial instanceof CredencialCodigoBarra && atualizacao instanceof CredencialCodigoBarra) {
				CredencialCodigoBarra cOriginal = (CredencialCodigoBarra) credencial;
				CredencialCodigoBarra cAtualizacao = (CredencialCodigoBarra) atualizacao;
				if (cAtualizacao.getCodigo() > 0) {
					cOriginal.setCodigo(cAtualizacao.getCodigo());
				}
			}
		}
	}

	public void atualizar(Collection<Credencial> credenciais, Collection<Credencial> atualizacoes) {
		if (credenciais != null && atualizacoes != null) {
			for (Credencial atualizacao : atualizacoes) {
				for (Credencial credencial : credenciais) {
					if (atualizacao.getId() != null && credencial.getId() != null) {
						if (atualizacao.getId().equals(credencial.getId())) {
							atualizar(credencial, atualizacao);
						}
					}
				}
			}
		}
	}
}
