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

import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.modelos.AdicionadorLinkServico;
import com.autobots.automanager.modelos.ServicoAtualizador;
import com.autobots.automanager.repositorios.RepositorioServico;

@RestController
@RequestMapping({"/servicos", "/servico"})
public class ServicoControle {

	@Autowired
	private RepositorioServico repositorio;

	@Autowired
	private AdicionadorLinkServico adicionadorLink;

	@Autowired
	private ServicoAtualizador atualizador;

	@GetMapping
	public ResponseEntity<CollectionModel<Servico>> obterServicos() {
		List<Servico> servicos = repositorio.findAll();
		adicionadorLink.adicionarLink(servicos);
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(ServicoControle.class).obterServicos())
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(servicos, linkProprio));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Servico> obterServico(@PathVariable long id) {
		Optional<Servico> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Servico servico = opt.get();
		adicionadorLink.adicionarLink(servico);
		return ResponseEntity.ok(servico);
	}

	@PostMapping
	public ResponseEntity<Servico> cadastrarServico(@RequestBody Servico servico) {
		servico.setId(null);
		Servico novo = repositorio.save(servico);
		adicionadorLink.adicionarLink(novo);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(novo.getId())
				.toUri();
		return ResponseEntity.created(uri).body(novo);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Servico> atualizarServico(@PathVariable long id, @RequestBody Servico atualizacao) {
		Optional<Servico> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Servico servico = opt.get();
		atualizador.atualizar(servico, atualizacao);
		repositorio.save(servico);
		adicionadorLink.adicionarLink(servico);
		return ResponseEntity.ok(servico);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirServico(@PathVariable long id) {
		Optional<Servico> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		repositorio.delete(opt.get());
		return ResponseEntity.noContent().build();
	}
}
