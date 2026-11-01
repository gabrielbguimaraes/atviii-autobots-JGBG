# AutoManager - Microsserviço AutoBots (ATVIII)

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
