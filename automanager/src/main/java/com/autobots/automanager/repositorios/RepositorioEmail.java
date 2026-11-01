package com.autobots.automanager.repositorios;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.autobots.automanager.entitades.Email;

@Repository
public interface RepositorioEmail extends JpaRepository<Email, Long> {
}
