package com.autobots.automanager.controles;

import java.net.URI;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

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

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.modelos.AdicionadorLinkVenda;
import com.autobots.automanager.modelos.VendaAtualizador;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioMercadoria;
import com.autobots.automanager.repositorios.RepositorioServico;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.repositorios.RepositorioVenda;

@RestController
@RequestMapping({"/vendas", "/venda"})
public class VendaControle {

	@Autowired
	private RepositorioVenda repositorio;

	@Autowired
	private RepositorioUsuario repositorioUsuario;

	@Autowired
	private RepositorioVeiculo repositorioVeiculo;

	@Autowired
	private RepositorioMercadoria repositorioMercadoria;

	@Autowired
	private RepositorioServico repositorioServico;

	@Autowired
	private RepositorioEmpresa repositorioEmpresa;

	@Autowired
	private AdicionadorLinkVenda adicionadorLink;

	@Autowired
	private VendaAtualizador atualizador;

	@GetMapping
	public ResponseEntity<CollectionModel<Venda>> obterVendas() {
		List<Venda> vendas = repositorio.findAll();
		adicionadorLink.adicionarLink(vendas);
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(VendaControle.class).obterVendas())
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(vendas, linkProprio));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Venda> obterVenda(@PathVariable long id) {
		Optional<Venda> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Venda venda = opt.get();
		adicionadorLink.adicionarLink(venda);
		return ResponseEntity.ok(venda);
	}

	@PostMapping
	public ResponseEntity<Venda> cadastrarVenda(@RequestBody Venda venda) {
		venda.setId(null);
		if (venda.getCadastro() == null) {
			venda.setCadastro(new Date());
		}

		if (venda.getCliente() != null && venda.getCliente().getId() != null) {
			repositorioUsuario.findById(venda.getCliente().getId()).ifPresent(venda::setCliente);
		}
		if (venda.getFuncionario() != null && venda.getFuncionario().getId() != null) {
			repositorioUsuario.findById(venda.getFuncionario().getId()).ifPresent(venda::setFuncionario);
		}
		if (venda.getVeiculo() != null && venda.getVeiculo().getId() != null) {
			repositorioVeiculo.findById(venda.getVeiculo().getId()).ifPresent(venda::setVeiculo);
		}

		if (venda.getMercadorias() != null && !venda.getMercadorias().isEmpty()) {
			Set<Mercadoria> carregadas = new HashSet<>();
			for (Mercadoria m : venda.getMercadorias()) {
				if (m.getId() != null) {
					repositorioMercadoria.findById(m.getId()).ifPresent(carregadas::add);
				} else {
					carregadas.add(m);
				}
			}
			venda.setMercadorias(carregadas);
		}

		if (venda.getServicos() != null && !venda.getServicos().isEmpty()) {
			Set<Servico> carregados = new HashSet<>();
			for (Servico s : venda.getServicos()) {
				if (s.getId() != null) {
					repositorioServico.findById(s.getId()).ifPresent(carregados::add);
				} else {
					carregados.add(s);
				}
			}
			venda.setServicos(carregados);
		}

		Venda nova = repositorio.save(venda);

		if (nova.getCliente() != null) {
			nova.getCliente().getVendas().add(nova);
			repositorioUsuario.save(nova.getCliente());
		}
		if (nova.getVeiculo() != null) {
			nova.getVeiculo().getVendas().add(nova);
			repositorioVeiculo.save(nova.getVeiculo());
		}

		adicionadorLink.adicionarLink(nova);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(nova.getId())
				.toUri();
		return ResponseEntity.created(uri).body(nova);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Venda> atualizarVenda(@PathVariable long id, @RequestBody Venda atualizacao) {
		Optional<Venda> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Venda venda = opt.get();
		atualizador.atualizar(venda, atualizacao);

		if (atualizacao.getCliente() != null && atualizacao.getCliente().getId() != null) {
			repositorioUsuario.findById(atualizacao.getCliente().getId()).ifPresent(venda::setCliente);
		}
		if (atualizacao.getFuncionario() != null && atualizacao.getFuncionario().getId() != null) {
			repositorioUsuario.findById(atualizacao.getFuncionario().getId()).ifPresent(venda::setFuncionario);
		}
		if (atualizacao.getVeiculo() != null && atualizacao.getVeiculo().getId() != null) {
			repositorioVeiculo.findById(atualizacao.getVeiculo().getId()).ifPresent(venda::setVeiculo);
		}

		repositorio.save(venda);
		adicionadorLink.adicionarLink(venda);
		return ResponseEntity.ok(venda);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirVenda(@PathVariable long id) {
		Optional<Venda> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Venda venda = opt.get();

		if (venda.getCliente() != null) {
			venda.getCliente().getVendas().remove(venda);
			repositorioUsuario.save(venda.getCliente());
			venda.setCliente(null);
		}
		if (venda.getFuncionario() != null) {
			venda.getFuncionario().getVendas().remove(venda);
			repositorioUsuario.save(venda.getFuncionario());
			venda.setFuncionario(null);
		}
		if (venda.getVeiculo() != null) {
			venda.getVeiculo().getVendas().remove(venda);
			repositorioVeiculo.save(venda.getVeiculo());
			venda.setVeiculo(null);
		}

		List<Empresa> empresas = repositorioEmpresa.findAll();
		for (Empresa empresa : empresas) {
			if (empresa.getVendas() != null && empresa.getVendas().remove(venda)) {
				repositorioEmpresa.save(empresa);
			}
		}

		venda.getMercadorias().clear();
		venda.getServicos().clear();
		repositorio.delete(venda);
		return ResponseEntity.noContent().build();
	}
}
