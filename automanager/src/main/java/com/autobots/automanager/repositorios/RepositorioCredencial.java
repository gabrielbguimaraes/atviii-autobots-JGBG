package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autobots.automanager.entitades.Credencial;

@Repository
public interface RepositorioCredencial extends JpaRepository<Credencial, Long> {
}
