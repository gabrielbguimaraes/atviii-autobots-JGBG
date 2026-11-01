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

import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.modelos.AdicionadorLinkVeiculo;
import com.autobots.automanager.modelos.VeiculoAtualizador;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.repositorios.RepositorioVenda;

@RestController
@RequestMapping({"/veiculos", "/veiculo"})
public class VeiculoControle {

	@Autowired
	private RepositorioVeiculo repositorio;

	@Autowired
	private RepositorioUsuario repositorioUsuario;

	@Autowired
	private RepositorioVenda repositorioVenda;

	@Autowired
	private AdicionadorLinkVeiculo adicionadorLink;

	@Autowired
	private VeiculoAtualizador atualizador;

	@GetMapping
	public ResponseEntity<CollectionModel<Veiculo>> obterVeiculos() {
		List<Veiculo> veiculos = repositorio.findAll();
		adicionadorLink.adicionarLink(veiculos);
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(VeiculoControle.class).obterVeiculos())
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(veiculos, linkProprio));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Veiculo> obterVeiculo(@PathVariable long id) {
		Optional<Veiculo> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Veiculo veiculo = opt.get();
		adicionadorLink.adicionarLink(veiculo);
		return ResponseEntity.ok(veiculo);
	}

	@PostMapping
	public ResponseEntity<Veiculo> cadastrarVeiculo(@RequestBody Veiculo veiculo) {
		veiculo.setId(null);
		if (veiculo.getProprietario() != null && veiculo.getProprietario().getId() != null) {
			Optional<Usuario> usuarioOpt = repositorioUsuario.findById(veiculo.getProprietario().getId());
			if (usuarioOpt.isPresent()) {
				Usuario proprietario = usuarioOpt.get();
				veiculo.setProprietario(proprietario);
				proprietario.getVeiculos().add(veiculo);
			}
		}
		Veiculo novo = repositorio.save(veiculo);
		adicionadorLink.adicionarLink(novo);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(novo.getId())
				.toUri();
		return ResponseEntity.created(uri).body(novo);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Veiculo> atualizarVeiculo(@PathVariable long id, @RequestBody Veiculo atualizacao) {
		Optional<Veiculo> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Veiculo veiculo = opt.get();
		atualizador.atualizar(veiculo, atualizacao);
		if (atualizacao.getProprietario() != null && atualizacao.getProprietario().getId() != null) {
			Optional<Usuario> novoPropOpt = repositorioUsuario.findById(atualizacao.getProprietario().getId());
			if (novoPropOpt.isPresent()) {
				if (veiculo.getProprietario() != null) {
					veiculo.getProprietario().getVeiculos().remove(veiculo);
				}
				veiculo.setProprietario(novoPropOpt.get());
				novoPropOpt.get().getVeiculos().add(veiculo);
			}
		}
		repositorio.save(veiculo);
		adicionadorLink.adicionarLink(veiculo);
		return ResponseEntity.ok(veiculo);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirVeiculo(@PathVariable long id) {
		Optional<Veiculo> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Veiculo veiculo = opt.get();

		if (veiculo.getProprietario() != null) {
			veiculo.getProprietario().getVeiculos().remove(veiculo);
			repositorioUsuario.save(veiculo.getProprietario());
			veiculo.setProprietario(null);
		}

		List<Venda> vendas = repositorioVenda.findAll();
		for (Venda venda : vendas) {
			if (venda.getVeiculo() != null && venda.getVeiculo().getId().equals(veiculo.getId())) {
				venda.setVeiculo(null);
				repositorioVenda.save(venda);
			}
		}

		veiculo.getVendas().clear();
		repositorio.delete(veiculo);
		return ResponseEntity.noContent().build();
	}
}
