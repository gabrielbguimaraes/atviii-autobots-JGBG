package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autobots.automanager.entitades.Usuario;

@Repository
public interface RepositorioUsuario extends JpaRepository<Usuario, Long> {
}
