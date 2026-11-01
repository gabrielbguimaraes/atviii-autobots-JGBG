package com.autobots.automanager;

import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Date;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class AutomanagerIntegrationTests {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Value("${easteregg}")
	private String easterEgg;

	@Test
	@Order(1)
	void testEasterEgg() {
		assertEquals("p_P = c^5 / (hG^2)", easterEgg);
	}

	@Test
	@Order(2)
	void testEmpresaEndpointsAndHateoas() throws Exception {
		mockMvc.perform(get("/empresas"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()))
				.andExpect(jsonPath("$._embedded.empresaList", notNullValue()))
				.andExpect(jsonPath("$._embedded.empresaList[0]._links.self.href", notNullValue()));

		mockMvc.perform(get("/empresas/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.razaoSocial", is("Car service toyota ltda")))
				.andExpect(jsonPath("$._links.self.href", notNullValue()))
				.andExpect(jsonPath("$._links.empresas.href", notNullValue()))
				.andExpect(jsonPath("$._links.atualizar.href", notNullValue()))
				.andExpect(jsonPath("$._links.excluir.href", notNullValue()));

		String novaEmpresaJson = "{\n" +
				"  \"razaoSocial\": \"Auto Mecânica Volkswagen Ltda\",\n" +
				"  \"nomeFantasia\": \"VW Service Centro\"\n" +
				"}";

		mockMvc.perform(post("/empresas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(novaEmpresaJson))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.razaoSocial", is("Auto Mecânica Volkswagen Ltda")))
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		String atualizacaoJson = "{\n" +
				"  \"nomeFantasia\": \"Car service manutenção veicular atualizada\"\n" +
				"}";

		mockMvc.perform(put("/empresas/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(atualizacaoJson))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nomeFantasia", is("Car service manutenção veicular atualizada")));

		mockMvc.perform(get("/empresas/1/usuarios"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		mockMvc.perform(get("/empresas/1/mercadorias"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		mockMvc.perform(get("/empresas/1/servicos"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		mockMvc.perform(get("/empresas/1/vendas"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		mockMvc.perform(get("/empresas/1/telefones"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		mockMvc.perform(get("/empresas/1/endereco"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()));
	}

	@Test
	@Order(3)
	void testUsuarioEndpointsAndHateoas() throws Exception {
		mockMvc.perform(get("/usuarios"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()))
				.andExpect(jsonPath("$._embedded.usuarioList", notNullValue()))
				.andExpect(jsonPath("$._embedded.usuarioList[0]._links.self.href", notNullValue()))
				.andExpect(jsonPath("$._embedded.usuarioList[0]._links.atualizar.href", notNullValue()));

		mockMvc.perform(get("/usuarios/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$._links.self.href", notNullValue()))
				.andExpect(jsonPath("$._links.usuarios.href", notNullValue()))
				.andExpect(jsonPath("$._links.atualizar.href", notNullValue()))
				.andExpect(jsonPath("$._links.excluir.href", notNullValue()));

		mockMvc.perform(get("/usuarios/1/documentos"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		mockMvc.perform(get("/usuarios/1/telefones"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		mockMvc.perform(get("/usuarios/1/emails"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		mockMvc.perform(get("/usuarios/1/credenciais"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		mockMvc.perform(get("/usuarios/1/endereco"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		String novoUsuario = "{\n" +
				"  \"nome\": \"Carlos Alberto Silva\",\n" +
				"  \"nomeSocial\": \"Carlinhos\",\n" +
				"  \"perfis\": [\"CLIENTE\"],\n" +
				"  \"emails\": [{\"endereco\": \"carlinhos@test.com\"}],\n" +
				"  \"telefones\": [{\"ddd\": \"11\", \"numero\": \"999887766\"}],\n" +
				"  \"documentos\": [{\"tipo\": \"CPF\", \"dataEmissao\": \"2020-01-01\", \"numero\": \"98765432100\"}]\n" +
				"}";

		mockMvc.perform(post("/usuarios")
				.contentType(MediaType.APPLICATION_JSON)
				.content(novoUsuario))
				.andExpect(status().isCreated())
				.andExpect(header().exists("Location"))
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.nome", is("Carlos Alberto Silva")))
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		String atualizacao = "{\"nomeSocial\": \"Beto\"}";
		mockMvc.perform(put("/usuarios/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content(atualizacao))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.nomeSocial", is("Beto")));
	}

	@Test
	@Order(4)
	void testVeiculoEndpointsAndSafeUnlinking() throws Exception {
		mockMvc.perform(get("/veiculos"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()))
				.andExpect(jsonPath("$._embedded.veiculoList", notNullValue()))
				.andExpect(jsonPath("$._embedded.veiculoList[0]._links.self.href", notNullValue()));

		mockMvc.perform(get("/veiculos/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.placa", is("ABC-0000")))
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		String novoVeiculo = "{\n" +
				"  \"tipo\": \"HATCH\",\n" +
				"  \"modelo\": \"Etios Hatch 1.5\",\n" +
				"  \"placa\": \"XYZ-9999\",\n" +
				"  \"proprietario\": {\"id\": 1}\n" +
				"}";

		String responseBody = mockMvc.perform(post("/veiculos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(novoVeiculo))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.placa", is("XYZ-9999")))
				.andExpect(jsonPath("$._links.self.href", notNullValue()))
				.andReturn().getResponse().getContentAsString();

		long novoVeiculoId = objectMapper.readTree(responseBody).get("id").asLong();

		String atualizacao = "{\"modelo\": \"Etios Platinum\"}";
		mockMvc.perform(put("/veiculos/" + novoVeiculoId)
				.contentType(MediaType.APPLICATION_JSON)
				.content(atualizacao))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.modelo", is("Etios Platinum")));

		mockMvc.perform(delete("/veiculos/" + novoVeiculoId))
				.andExpect(status().isNoContent());

		mockMvc.perform(get("/veiculos/" + novoVeiculoId))
				.andExpect(status().isNotFound());
	}

	@Test
	@Order(5)
	void testMercadoriaEndpoints() throws Exception {
		mockMvc.perform(get("/mercadorias"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		mockMvc.perform(get("/mercadorias/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		String novaMercadoria = "{\n" +
				"  \"nome\": \"Filtro de Óleo Toyota\",\n" +
				"  \"descricao\": \"Filtro de óleo original linha Hilux\",\n" +
				"  \"valor\": 45.0,\n" +
				"  \"quantidade\": 50,\n" +
				"  \"validade\": \"2028-12-31\",\n" +
				"  \"fabricao\": \"2024-01-01\"\n" +
				"}";

		String response = mockMvc.perform(post("/mercadorias")
				.contentType(MediaType.APPLICATION_JSON)
				.content(novaMercadoria))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.nome", is("Filtro de Óleo Toyota")))
				.andExpect(jsonPath("$._links.self.href", notNullValue()))
				.andReturn().getResponse().getContentAsString();

		long id = objectMapper.readTree(response).get("id").asLong();

		mockMvc.perform(put("/mercadorias/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"valor\": 49.90}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.valor", is(49.90)));

		mockMvc.perform(delete("/mercadorias/" + id))
				.andExpect(status().isNoContent());
	}

	@Test
	@Order(6)
	void testServicoEndpoints() throws Exception {
		mockMvc.perform(get("/servicos"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		mockMvc.perform(get("/servicos/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		String novoServico = "{\n" +
				"  \"nome\": \"Higienização de Ar Condicionado\",\n" +
				"  \"descricao\": \"Limpeza completa do sistema de ar\",\n" +
				"  \"valor\": 120.0\n" +
				"}";

		String response = mockMvc.perform(post("/servicos")
				.contentType(MediaType.APPLICATION_JSON)
				.content(novoServico))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$._links.self.href", notNullValue()))
				.andReturn().getResponse().getContentAsString();

		long id = objectMapper.readTree(response).get("id").asLong();

		mockMvc.perform(put("/servicos/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"valor\": 130.0}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.valor", is(130.0)));

		mockMvc.perform(delete("/servicos/" + id))
				.andExpect(status().isNoContent());
	}

	@Test
	@Order(7)
	void testVendaEndpoints() throws Exception {
		mockMvc.perform(get("/vendas"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()))
				.andExpect(jsonPath("$._embedded.vendaList", notNullValue()));

		mockMvc.perform(get("/vendas/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$._links.self.href", notNullValue()))
				.andExpect(jsonPath("$._links.atualizar.href", notNullValue()))
				.andExpect(jsonPath("$._links.excluir.href", notNullValue()));

		String novaVenda = "{\n" +
				"  \"identificacao\": \"VND-999888\",\n" +
				"  \"cliente\": {\"id\": 1},\n" +
				"  \"funcionario\": {\"id\": 1},\n" +
				"  \"veiculo\": {\"id\": 1}\n" +
				"}";

		String response = mockMvc.perform(post("/vendas")
				.contentType(MediaType.APPLICATION_JSON)
				.content(novaVenda))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$.identificacao", is("VND-999888")))
				.andExpect(jsonPath("$._links.self.href", notNullValue()))
				.andReturn().getResponse().getContentAsString();

		long id = objectMapper.readTree(response).get("id").asLong();

		mockMvc.perform(put("/vendas/" + id)
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"identificacao\": \"VND-999888-REV\"}"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.identificacao", is("VND-999888-REV")));

		mockMvc.perform(delete("/vendas/" + id))
				.andExpect(status().isNoContent());
	}

	@Test
	@Order(8)
	void testCredencialEndpoints() throws Exception {
		mockMvc.perform(get("/credenciais"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		mockMvc.perform(get("/credenciais/1"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		String credUsuarioSenha = "{\n" +
				"  \"nomeUsuario\": \"carlosteste\",\n" +
				"  \"senha\": \"segredo123\"\n" +
				"}";

		String resp1 = mockMvc.perform(post("/credenciais/usuario-senha")
				.contentType(MediaType.APPLICATION_JSON)
				.content(credUsuarioSenha))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$._links.self.href", notNullValue()))
				.andReturn().getResponse().getContentAsString();

		long id1 = objectMapper.readTree(resp1).get("id").asLong();

		String credCodigoBarra = "{\n" +
				"  \"codigo\": 7891234567890\n" +
				"}";

		String resp2 = mockMvc.perform(post("/credenciais/codigo-barra")
				.contentType(MediaType.APPLICATION_JSON)
				.content(credCodigoBarra))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.id", notNullValue()))
				.andExpect(jsonPath("$._links.self.href", notNullValue()))
				.andReturn().getResponse().getContentAsString();

		long id2 = objectMapper.readTree(resp2).get("id").asLong();

		mockMvc.perform(delete("/credenciais/" + id1)).andExpect(status().isNoContent());
		mockMvc.perform(delete("/credenciais/" + id2)).andExpect(status().isNoContent());
	}

	@Test
	@Order(9)
	void testDocumentoTelefoneEnderecoEmailEndpoints() throws Exception {
		mockMvc.perform(get("/documentos")).andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		mockMvc.perform(get("/telefones")).andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		mockMvc.perform(get("/enderecos")).andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()));

		mockMvc.perform(get("/emails")).andExpect(status().isOk())
				.andExpect(jsonPath("$._links.self.href", notNullValue()));
	}
}
