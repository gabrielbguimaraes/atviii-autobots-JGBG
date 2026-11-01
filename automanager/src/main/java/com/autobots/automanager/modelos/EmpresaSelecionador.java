package com.autobots.automanager.modelos;

import java.util.Collection;

import org.springframework.stereotype.Component;

import com.autobots.automanager.entitades.Empresa;

@Component
public class EmpresaSelecionador {
	public Empresa selecionar(Collection<Empresa> empresas, long id) {
		Empresa selecionado = null;
		for (Empresa empresa : empresas) {
			if (empresa.getId() != null && empresa.getId() == id) {
				selecionado = empresa;
				break;
			}
		}
		return selecionado;
	}
}
