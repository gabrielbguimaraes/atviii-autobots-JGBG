package com.autobots.automanager.controles;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.modelos.AdicionadorLinkEmail;
import com.autobots.automanager.modelos.EmailAtualizador;
import com.autobots.automanager.repositorios.RepositorioEmail;

@RestController
@RequestMapping({"/emails", "/email"})
public class EmailControle {

	@Autowired
	private RepositorioEmail repositorio;

	@Autowired
	private AdicionadorLinkEmail adicionadorLink;

	@Autowired
	private EmailAtualizador atualizador;

	@GetMapping
	public ResponseEntity<CollectionModel<Email>> obterEmails() {
		List<Email> emails = repositorio.findAll();
		adicionadorLink.adicionarLink(emails);
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EmailControle.class).obterEmails())
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(emails, linkProprio));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Email> obterEmail(@PathVariable long id) {
		Optional<Email> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Email email = opt.get();
		adicionadorLink.adicionarLink(email);
		return ResponseEntity.ok(email);
	}

	@PostMapping
	public ResponseEntity<Email> cadastrarEmail(@RequestBody Email email) {
		email.setId(null);
		Email novoEmail = repositorio.save(email);
		adicionadorLink.adicionarLink(novoEmail);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(novoEmail.getId())
				.toUri();
		return ResponseEntity.created(uri).body(novoEmail);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Email> atualizarEmail(@PathVariable long id, @RequestBody Email atualizacao) {
		Optional<Email> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Email email = opt.get();
		atualizador.atualizar(email, atualizacao);
		repositorio.save(email);
		adicionadorLink.adicionarLink(email);
		return ResponseEntity.ok(email);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirEmail(@PathVariable long id) {
		Optional<Email> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		repositorio.delete(opt.get());
		return ResponseEntity.noContent().build();
	}
}
