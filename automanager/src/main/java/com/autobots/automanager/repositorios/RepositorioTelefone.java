package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autobots.automanager.entitades.Telefone;

@Repository
public interface RepositorioTelefone extends JpaRepository<Telefone, Long> {
}
