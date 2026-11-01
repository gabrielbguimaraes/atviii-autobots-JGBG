package com.autobots.automanager.controles;

import java.net.URI;
import java.util.Date;
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

import com.autobots.automanager.entitades.Credencial;
import com.autobots.automanager.entitades.CredencialCodigoBarra;
import com.autobots.automanager.entitades.CredencialUsuarioSenha;
import com.autobots.automanager.modelos.AdicionadorLinkCredencial;
import com.autobots.automanager.modelos.CredencialAtualizador;
import com.autobots.automanager.repositorios.RepositorioCredencial;

@RestController
@RequestMapping({"/credenciais", "/credencial"})
public class CredencialControle {

	@Autowired
	private RepositorioCredencial repositorio;

	@Autowired
	private AdicionadorLinkCredencial adicionadorLink;

	@Autowired
	private CredencialAtualizador atualizador;

	@GetMapping
	public ResponseEntity<CollectionModel<Credencial>> obterCredenciais() {
		List<Credencial> credenciais = repositorio.findAll();
		adicionadorLink.adicionarLink(credenciais);
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(CredencialControle.class).obterCredenciais())
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(credenciais, linkProprio));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Credencial> obterCredencial(@PathVariable long id) {
		Optional<Credencial> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Credencial credencial = opt.get();
		adicionadorLink.adicionarLink(credencial);
		return ResponseEntity.ok(credencial);
	}

	@PostMapping("/usuario-senha")
	public ResponseEntity<Credencial> cadastrarCredencialUsuarioSenha(@RequestBody CredencialUsuarioSenha credencial) {
		credencial.setId(null);
		if (credencial.getCriacao() == null) {
			credencial.setCriacao(new Date());
		}
		Credencial nova = repositorio.save(credencial);
		adicionadorLink.adicionarLink(nova);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(nova.getId())
				.toUri();
		return ResponseEntity.created(uri).body(nova);
	}

	@PostMapping("/codigo-barra")
	public ResponseEntity<Credencial> cadastrarCredencialCodigoBarra(@RequestBody CredencialCodigoBarra credencial) {
		credencial.setId(null);
		if (credencial.getCriacao() == null) {
			credencial.setCriacao(new Date());
		}
		Credencial nova = repositorio.save(credencial);
		adicionadorLink.adicionarLink(nova);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(nova.getId())
				.toUri();
		return ResponseEntity.created(uri).body(nova);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Credencial> atualizarCredencial(@PathVariable long id, @RequestBody CredencialUsuarioSenha atualizacao) {
		Optional<Credencial> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Credencial credencial = opt.get();
		atualizador.atualizar(credencial, atualizacao);
		repositorio.save(credencial);
		adicionadorLink.adicionarLink(credencial);
		return ResponseEntity.ok(credencial);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirCredencial(@PathVariable long id) {
		Optional<Credencial> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		repositorio.delete(opt.get());
		return ResponseEntity.noContent().build();
	}
}
