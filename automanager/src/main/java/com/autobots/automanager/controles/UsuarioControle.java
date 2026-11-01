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

import com.autobots.automanager.entitades.Credencial;
import com.autobots.automanager.entitades.Documento;
import com.autobots.automanager.entitades.Email;
import com.autobots.automanager.entitades.Empresa;
import com.autobots.automanager.entitades.Endereco;
import com.autobots.automanager.entitades.Mercadoria;
import com.autobots.automanager.entitades.Telefone;
import com.autobots.automanager.entitades.Usuario;
import com.autobots.automanager.entitades.Veiculo;
import com.autobots.automanager.entitades.Venda;
import com.autobots.automanager.modelos.AdicionadorLinkCredencial;
import com.autobots.automanager.modelos.AdicionadorLinkDocumento;
import com.autobots.automanager.modelos.AdicionadorLinkEmail;
import com.autobots.automanager.modelos.AdicionadorLinkEndereco;
import com.autobots.automanager.modelos.AdicionadorLinkMercadoria;
import com.autobots.automanager.modelos.AdicionadorLinkTelefone;
import com.autobots.automanager.modelos.AdicionadorLinkUsuario;
import com.autobots.automanager.modelos.AdicionadorLinkVeiculo;
import com.autobots.automanager.modelos.AdicionadorLinkVenda;
import com.autobots.automanager.modelos.UsuarioAtualizador;
import com.autobots.automanager.repositorios.RepositorioEmpresa;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.repositorios.RepositorioVeiculo;
import com.autobots.automanager.repositorios.RepositorioVenda;

@RestController
@RequestMapping({"/usuarios", "/usuario"})
public class UsuarioControle {

	@Autowired
	private RepositorioUsuario repositorio;

	@Autowired
	private RepositorioEmpresa repositorioEmpresa;

	@Autowired
	private RepositorioVeiculo repositorioVeiculo;

	@Autowired
	private RepositorioVenda repositorioVenda;

	@Autowired
	private AdicionadorLinkUsuario adicionadorLink;

	@Autowired(required = false)
	private AdicionadorLinkEndereco adicionadorLinkEndereco;

	@Autowired(required = false)
	private AdicionadorLinkTelefone adicionadorLinkTelefone;

	@Autowired(required = false)
	private AdicionadorLinkDocumento adicionadorLinkDocumento;

	@Autowired(required = false)
	private AdicionadorLinkEmail adicionadorLinkEmail;

	@Autowired(required = false)
	private AdicionadorLinkCredencial adicionadorLinkCredencial;

	@Autowired(required = false)
	private AdicionadorLinkVeiculo adicionadorLinkVeiculo;

	@Autowired(required = false)
	private AdicionadorLinkMercadoria adicionadorLinkMercadoria;

	@Autowired(required = false)
	private AdicionadorLinkVenda adicionadorLinkVenda;

	@Autowired
	private UsuarioAtualizador atualizador;

	@GetMapping
	public ResponseEntity<CollectionModel<Usuario>> obterUsuarios() {
		List<Usuario> usuarios = repositorio.findAll();
		adicionadorLink.adicionarLink(usuarios);
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterUsuarios())
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(usuarios, linkProprio));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Usuario> obterUsuario(@PathVariable long id) {
		Optional<Usuario> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Usuario usuario = opt.get();
		adicionadorLink.adicionarLink(usuario);
		return ResponseEntity.ok(usuario);
	}

	@PostMapping
	public ResponseEntity<Usuario> cadastrarUsuario(@RequestBody Usuario usuario) {
		usuario.setId(null);
		if (usuario.getCredenciais() != null) {
			for (Credencial cred : usuario.getCredenciais()) {
				if (cred.getCriacao() == null) {
					cred.setCriacao(new Date());
				}
			}
		}
		Usuario novo = repositorio.save(usuario);
		adicionadorLink.adicionarLink(novo);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}")
				.buildAndExpand(novo.getId())
				.toUri();
		return ResponseEntity.created(uri).body(novo);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Usuario> atualizarUsuario(@PathVariable long id, @RequestBody Usuario atualizacao) {
		Optional<Usuario> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Usuario usuario = opt.get();
		atualizador.atualizar(usuario, atualizacao);
		repositorio.save(usuario);
		adicionadorLink.adicionarLink(usuario);
		return ResponseEntity.ok(usuario);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluirUsuario(@PathVariable long id) {
		Optional<Usuario> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Usuario usuario = opt.get();

		List<Empresa> empresas = repositorioEmpresa.findAll();
		for (Empresa empresa : empresas) {
			if (empresa.getUsuarios() != null && empresa.getUsuarios().remove(usuario)) {
				repositorioEmpresa.save(empresa);
			}
		}

		List<Veiculo> veiculos = repositorioVeiculo.findAll();
		for (Veiculo v : veiculos) {
			if (v.getProprietario() != null && v.getProprietario().getId().equals(usuario.getId())) {
				v.setProprietario(null);
				repositorioVeiculo.save(v);
			}
		}

		List<Venda> vendas = repositorioVenda.findAll();
		for (Venda v : vendas) {
			if (v.getCliente() != null && v.getCliente().getId().equals(usuario.getId())) {
				v.setCliente(null);
				repositorioVenda.save(v);
			}
			if (v.getFuncionario() != null && v.getFuncionario().getId().equals(usuario.getId())) {
				v.setFuncionario(null);
				repositorioVenda.save(v);
			}
		}

		repositorio.delete(usuario);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/{id}/endereco")
	public ResponseEntity<Endereco> obterEnderecoUsuario(@PathVariable long id) {
		Optional<Usuario> opt = repositorio.findById(id);
		if (opt.isEmpty() || opt.get().getEndereco() == null) {
			return ResponseEntity.notFound().build();
		}
		Endereco endereco = opt.get().getEndereco();
		if (adicionadorLinkEndereco != null) {
			adicionadorLinkEndereco.adicionarLink(endereco);
		}
		return ResponseEntity.ok(endereco);
	}

	@GetMapping("/{id}/documentos")
	public ResponseEntity<CollectionModel<Documento>> obterDocumentosUsuario(@PathVariable long id) {
		Optional<Usuario> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Collection<Documento> docs = opt.get().getDocumentos();
		if (adicionadorLinkDocumento != null) {
			for (Documento d : docs) {
				adicionadorLinkDocumento.adicionarLink(d);
			}
		}
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterDocumentosUsuario(id))
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(docs, linkProprio));
	}

	@GetMapping("/{id}/telefones")
	public ResponseEntity<CollectionModel<Telefone>> obterTelefonesUsuario(@PathVariable long id) {
		Optional<Usuario> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Collection<Telefone> tels = opt.get().getTelefones();
		if (adicionadorLinkTelefone != null) {
			for (Telefone t : tels) {
				adicionadorLinkTelefone.adicionarLink(t);
			}
		}
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterTelefonesUsuario(id))
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(tels, linkProprio));
	}

	@GetMapping("/{id}/emails")
	public ResponseEntity<CollectionModel<Email>> obterEmailsUsuario(@PathVariable long id) {
		Optional<Usuario> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Collection<Email> emails = opt.get().getEmails();
		if (adicionadorLinkEmail != null) {
			for (Email e : emails) {
				adicionadorLinkEmail.adicionarLink(e);
			}
		}
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterEmailsUsuario(id))
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(emails, linkProprio));
	}

	@GetMapping("/{id}/credenciais")
	public ResponseEntity<CollectionModel<Credencial>> obterCredenciaisUsuario(@PathVariable long id) {
		Optional<Usuario> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Collection<Credencial> creds = opt.get().getCredenciais();
		if (adicionadorLinkCredencial != null) {
			for (Credencial c : creds) {
				adicionadorLinkCredencial.adicionarLink(c);
			}
		}
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterCredenciaisUsuario(id))
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(creds, linkProprio));
	}

	@GetMapping("/{id}/veiculos")
	public ResponseEntity<CollectionModel<Veiculo>> obterVeiculosUsuario(@PathVariable long id) {
		Optional<Usuario> opt = repositorio.findById(id);
		if (opt.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		Collection<Veiculo> veiculos = opt.get().getVeiculos();
		if (adicionadorLinkVeiculo != null) {
			for (Veiculo v : veiculos) {
				adicionadorLinkVeiculo.adicionarLink(v);
			}
		}
		Link linkProprio = WebMvcLinkBuilder
				.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterVeiculosUsuario(id))
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(veiculos, linkProprio));
	}

	@GetMapping("/{id}/vendas")
	public ResponseEntity<CollectionModel<Venda>> obterVendasUsuario(@PathVariable long id) {
		Optional<Usuario> opt = repositorio.findById(id);
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
				.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterVendasUsuario(id))
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(vendas, linkProprio));
	}

	@GetMapping("/{id}/mercadorias")
	public ResponseEntity<CollectionModel<Mercadoria>> obterMercadoriasUsuario(@PathVariable long id) {
		Optional<Usuario> opt = repositorio.findById(id);
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
				.linkTo(WebMvcLinkBuilder.methodOn(UsuarioControle.class).obterMercadoriasUsuario(id))
				.withSelfRel();
		return ResponseEntity.ok(CollectionModel.of(mercadorias, linkProprio));
	}
}
