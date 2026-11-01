package com.autobots.automanager.controles;

import java.net.URI;
import java.util.Collection;
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

import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Servico;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.modelos.AdicionadorLinkEmpresa;
import com.autobots.automanager.modelos.AdicionadorLinkEndereco;
import com.autobots.automanager.modelos.AdicionadorLinkMercadoria;
import com.autobots.automanager.modelos.AdicionadorLinkServico;
import com.autobots.automanager.modelos.AdicionadorLinkTelefone;
import com.autobots.automanager.modelos.AdicionadorLinkUsuario;
import com.autobots.automanager.modelos.AdicionadorLinkVenda;
import com.autobots.automanager.modelos.EmpresaAtualizador;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioMercadoria;
import com.autobots.automanager.repositorios.RepositorioServico;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVenda;

@RestController
@RequestMapping({"/empresas", "/empresa"})
public class EmpresaControle {

	@Autowired
	private RepositorioEmpresa repositorio;

	@Autowired
	private RepositorioUsuario repositorioUsuario;

	@Autowired
	private RepositorioMercadoria repositorioMercadoria;

	@Autowired
	private RepositorioServico repositorioServico;

	@Autowired
	private RepositorioVenda repositorioVenda;

	@Autowired
	private AdicionadorLinkEmpresa adicionadorLink;

	@Autowired(required = false)
	private AdicionadorLinkEndereco adicionadorLinkEndereco;

	@Autowired(required = false)
	private AdicionadorLinkTelefone adicionadorLinkTelefone;

	@Autowired(required = false)
	private AdicionadorLinkUsuario adicionadorLinkUsuario;

	@Autowired(required = false)
	private AdicionadorLinkMercadoria adicionadorLinkMercadoria;

	@Autowired(required = false)
	private AdicionadorLinkServico adicionadorLinkServico;

	@Autowired(required = false)
	private AdicionadorLinkVenda adicionadorLinkVenda;

	@Autowired
	private EmpresaAtualizador atualizador;

	@GetMapping
	public ResponseEntity<CollectionModel<Empresa>> obterEmpresas() {
		List<Empresa> empresas = repositorio.findAll();
		adicionadorLink.adicionarLink(empresas);
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterEmpresas())
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(empresas, linkProprio));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Empresa> obterEmpresa(@PathVariable long id) {
		Optional<Empresa> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Empresa empresa = opt.get();
		adicionadorLink.adicionarLink(empresa);
		return ResponseEntity.ok(empresa);
	}

	@PostMapping
	public ResponseEntity<Empresa> cadastrarEmpresa(@RequestBody Empresa empresa) {
		empresa.setId(null);
		if (empresa.getCadastro() == null) {
			empresa.setCadastro(new Date());
		}
		Empresa nova = repositorio.save(empresa);
		adicionadorLink.adicionarLink(nova);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(nova.getId())
				.toUri();
		return ResponseEntity.created(uri).body(nova);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Empresa> atualizarEmpresa(@PathVariable long id, @RequestBody Empresa atualizacao) {
		Optional<Empresa> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Empresa empresa = opt.get();
		atualizador.atualizar(empresa, atualizacao);
		repositorio.save(empresa);
		adicionadorLink.adicionarLink(empresa);
		return ResponseEntity.ok(empresa);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirEmpresa(@PathVariable long id) {
		Optional<Empresa> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Empresa empresa = opt.get();
		empresa.getUsuarios().clear();
		empresa.getMercadorias().clear();
		empresa.getServicos().clear();
		empresa.getVendas().clear();
		repositorio.delete(empresa);
		return ResponseEntity.noContent().build();
	}

	@PostMapping("/{id}/usuarios/{usuarioId}")
	public ResponseEntity<Empresa> associarUsuario(@PathVariable long id, @PathVariable long usuarioId) {
		Optional<Empresa> empOpt = repositorio.findById(id);
		Optional<Usuario> usrOpt = repositorioUsuario.findById(usuarioId);
		if (empOpt.isEmpty() || usrOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Empresa empresa = empOpt.get();
		empresa.getUsuarios().add(usrOpt.get());
		repositorio.save(empresa);
		adicionadorLink.adicionarLink(empresa);
		return ResponseEntity.ok(empresa);
	}

	@PostMapping("/{id}/mercadorias/{mercadoriaId}")
	public ResponseEntity<Empresa> associarMercadoria(@PathVariable long id, @PathVariable long mercadoriaId) {
		Optional<Empresa> empOpt = repositorio.findById(id);
		Optional<Mercadoria> mercOpt = repositorioMercadoria.findById(mercadoriaId);
		if (empOpt.isEmpty() || mercOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Empresa empresa = empOpt.get();
		empresa.getMercadorias().add(mercOpt.get());
		repositorio.save(empresa);
		adicionadorLink.adicionarLink(empresa);
		return ResponseEntity.ok(empresa);
	}

	@PostMapping("/{id}/servicos/{servicoId}")
	public ResponseEntity<Empresa> associarServico(@PathVariable long id, @PathVariable long servicoId) {
		Optional<Empresa> empOpt = repositorio.findById(id);
		Optional<Servico> servOpt = repositorioServico.findById(servicoId);
		if (empOpt.isEmpty() || servOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Empresa empresa = empOpt.get();
		empresa.getServicos().add(servOpt.get());
		repositorio.save(empresa);
		adicionadorLink.adicionarLink(empresa);
		return ResponseEntity.ok(empresa);
	}

	@PostMapping("/{id}/vendas/{vendaId}")
	public ResponseEntity<Empresa> associarVenda(@PathVariable long id, @PathVariable long vendaId) {
		Optional<Empresa> empOpt = repositorio.findById(id);
		Optional<Venda> vendaOpt = repositorioVenda.findById(vendaId);
		if (empOpt.isEmpty() || vendaOpt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Empresa empresa = empOpt.get();
		empresa.getVendas().add(vendaOpt.get());
		repositorio.save(empresa);
		adicionadorLink.adicionarLink(empresa);
		return ResponseEntity.ok(empresa);
	}

	@GetMapping("/{id}/usuarios")
	public ResponseEntity<CollectionModel<Usuario>> obterUsuariosEmpresa(@PathVariable long id) {
		Optional<Empresa> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Collection<Usuario> usuarios = opt.get().getUsuarios();
		if (adicionadorLinkUsuario != null) {
			for (Usuario u : usuarios) {
				adicionadorLinkUsuario.adicionarLink(u);
			}
		}
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterUsuariosEmpresa(id))
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(usuarios, linkProprio));
	}

	@GetMapping("/{id}/mercadorias")
	public ResponseEntity<CollectionModel<Mercadoria>> obterMercadoriasEmpresa(@PathVariable long id) {
		Optional<Empresa> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Collection<Mercadoria> mercadorias = opt.get().getMercadorias();
		if (adicionadorLinkMercadoria != null) {
			for (Mercadoria m : mercadorias) {
				adicionadorLinkMercadoria.adicionarLink(m);
			}
		}
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterMercadoriasEmpresa(id))
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(mercadorias, linkProprio));
	}

	@GetMapping("/{id}/servicos")
	public ResponseEntity<CollectionModel<Servico>> obterServicosEmpresa(@PathVariable long id) {
		Optional<Empresa> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Collection<Servico> servicos = opt.get().getServicos();
		if (adicionadorLinkServico != null) {
			for (Servico s : servicos) {
				adicionadorLinkServico.adicionarLink(s);
			}
		}
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterServicosEmpresa(id))
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(servicos, linkProprio));
	}

	@GetMapping("/{id}/vendas")
	public ResponseEntity<CollectionModel<Venda>> obterVendasEmpresa(@PathVariable long id) {
		Optional<Empresa> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Collection<Venda> vendas = opt.get().getVendas();
		if (adicionadorLinkVenda != null) {
			for (Venda v : vendas) {
				adicionadorLinkVenda.adicionarLink(v);
			}
		}
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterVendasEmpresa(id))
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(vendas, linkProprio));
	}

	@GetMapping("/{id}/telefones")
	public ResponseEntity<CollectionModel<Telefone>> obterTelefonesEmpresa(@PathVariable long id) {
		Optional<Empresa> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Collection<Telefone> telefones = opt.get().getTelefones();
		if (adicionadorLinkTelefone != null) {
			for (Telefone t : telefones) {
				adicionadorLinkTelefone.adicionarLink(t);
			}
		}
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(EmpresaControle.class).obterTelefonesEmpresa(id))
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(telefones, linkProprio));
	}

	@GetMapping("/{id}/endereco")
	public ResponseEntity<Endereco> obterEnderecoEmpresa(@PathVariable long id) {
		Optional<Empresa> opt = repositorio.findById(id);
		if (opt.isEmpty() || opt.get().getEndereco() == null) {
			return ResponseEntity.notFound().build();
		}
		Endereco endereco = opt.get().getEndereco();
		if (adicionadorLinkEndereco != null) {
			adicionadorLinkEndereco.adicionarLink(endereco);
		}
		return ResponseEntity.ok(endereco);
	}
}
