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

import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.modelos.AdicionadorLinkMercadoria;
import com.autobots.automanager.modelos.MercadoriaAtualizador;
import com.autobots.automanager.repositorios.RepositorioMercadoria;

@RestController
@RequestMapping({"/mercadorias", "/mercadoria"})
public class MercadoriaControle {

	@Autowired
	private RepositorioMercadoria repositorio;

	@Autowired
	private AdicionadorLinkMercadoria adicionadorLink;

	@Autowired
	private MercadoriaAtualizador atualizador;

	@GetMapping
	public ResponseEntity<CollectionModel<Mercadoria>> obterMercadorias() {
		List<Mercadoria> mercadorias = repositorio.findAll();
		adicionadorLink.adicionarLink(mercadorias);
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(MercadoriaControle.class).obterMercadorias())
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(mercadorias, linkProprio));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Mercadoria> obterMercadoria(@PathVariable long id) {
		Optional<Mercadoria> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Mercadoria mercadoria = opt.get();
		adicionadorLink.adicionarLink(mercadoria);
		return ResponseEntity.ok(mercadoria);
	}

	@PostMapping
	public ResponseEntity<Mercadoria> cadastrarMercadoria(@RequestBody Mercadoria mercadoria) {
		mercadoria.setId(null);
		if (mercadoria.getCadastro() == null) {
			mercadoria.setCadastro(new Date());
		}
		Mercadoria nova = repositorio.save(mercadoria);
		adicionadorLink.adicionarLink(nova);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(nova.getId())
				.toUri();
		return ResponseEntity.created(uri).body(nova);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Mercadoria> atualizarMercadoria(@PathVariable long id, @RequestBody Mercadoria atualizacao) {
		Optional<Mercadoria> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Mercadoria mercadoria = opt.get();
		atualizador.atualizar(mercadoria, atualizacao);
		repositorio.save(mercadoria);
		adicionadorLink.adicionarLink(mercadoria);
		return ResponseEntity.ok(mercadoria);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirMercadoria(@PathVariable long id) {
		Optional<Mercadoria> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		repositorio.delete(opt.get());
		return ResponseEntity.noContent().build();
	}
}
