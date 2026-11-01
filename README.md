# AutoManager - Microsserviço AutoBots (ATVIII)

Microsserviço Spring Boot para gerenciamento de rede de manutenção veicular e autopeças.

## Tecnologias
- Java 17
- Spring Boot 2.6.7
- Spring Data JPA
- Spring HATEOAS (Nível 3 de Richardson)
- H2 Database (em memória) / MySQL
- Lombok

## Funcionalidades
- CRUD completo para todas as entidades: `Empresa`, `Usuario`, `Veiculo`, `Mercadoria`, `Servico`, `Venda`, `Credencial`, `Documento`, `Email`, `Endereco` e `Telefone`.
- Endpoints de associação e vínculos entre entidades.
- HATEOAS completo com links (`self`, coleção, `atualizar`, `excluir`).
- Prevenção de recursão JSON infinita em relacionamentos bidirecionais.
- População inicial de dados consistente via `CommandLineRunner`.

## Como Executar

### Executando a Aplicação
Acesse a pasta `automanager` e execute:
```bash
./mvnw spring-boot:run
```
A API estará disponível em `http://localhost:8080`.

### Executando os Testes
```bash
./mvnw test
```
