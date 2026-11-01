package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autobots.automanager.entitades.Endereco;

@Repository
public interface RepositorioEndereco extends JpaRepository<Endereco, Long> {
}
