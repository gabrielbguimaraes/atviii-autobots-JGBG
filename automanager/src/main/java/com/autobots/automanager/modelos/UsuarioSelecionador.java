package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Usuario;

@Component
public class UsuarioSelecionador {
	public Usuario selecionar(Collection<Usuario> usuarios, long id) {
		Usuario selecionado = null;
		for (Usuario usuario : usuarios) {
			if (usuario.getId() != null && usuario.getId() == id) {
				selecionado = usuario;
				break;
			}
		}
		return selecionado;
	}
}
