# Sistema de Gerenciamento de Farmácia - API REST

API REST completa para gerenciamento de farmácia desenvolvida em Java 17 com Spring Boot.

## Funcionalidades

- ✅ Gestão de medicamentos (CRUD completo)
- ✅ Gestão de categorias
- ✅ Gestão de clientes
- ✅ Controle de estoque com movimentações
- ✅ Processamento de vendas com regras de negócio
- ✅ Sistema de alertas (estoque baixo e validade próxima)
- ✅ Validações de dados e tratamento de erros
- ✅ Autenticação básica
- ✅ Documentação Swagger/OpenAPI

## Tecnologias Utilizadas

- Java 17
- Spring Boot 3.2.0
- Spring Data JPA
- Spring Security
- Spring Validation
- H2 Database (em arquivo)
- ModelMapper
- Swagger/OpenAPI 3.0
- Lombok
- Maven

## Diagrama de Entidades

┌─────────────┐ ┌─────────────┐ ┌─────────────┐
│ Cliente │ │ Venda │ │ Categoria │
├─────────────┤ ├─────────────┤ ├─────────────┤
│ id │◄─────┤ cliente_id │ │ id │
│ nome │ │ valorTotal │ │ nome │
│ cpf │ │ dataVenda │ │ descricao │
│ email │ └─────────────┘ └─────────────┘
│ dataNasc │ │ │
└─────────────┘ │ │
│ │
┌───────▼───────┐ ┌───────▼───────┐ ┌──────────────┐
│ ItemVenda │ │ Medicamento │ │MovimentacaoEstoque│
├───────────────┤ ├───────────────┤ ├──────────────┤
│ venda_id │ │ id │ │ id │
│ medicamento_id│─────┤ categoria_id │ │ medicamento_id│
│ quantidade │ │ nome │ │ tipo │
│ precoUnitario │ │ preco │ │ quantidade │
│ subtotal │ │ quantidade │ │ dataMovimentacao│
└───────────────┘ │ dataValidade │ │ observacao │
│ status │ └──────────────┘
└───────────────┘


## Regras de Negócio Implementadas

### Medicamentos
- Nome obrigatório e único
- Preço deve ser maior que zero
- Quantidade não pode ser negativa
- Data de validade deve ser futura
- Medicamentos inativos não podem ser vendidos
- Não permite exclusão se já foi vendido (proteção de integridade)
- Não permite atualização se já foi vendido (proteção de integridade)

### Categorias
- Nome obrigatório e único
- Não permite exclusão se vinculada a medicamentos

### Clientes
- CPF obrigatório e válido
- CPF único
- Email obrigatório e válido
- Cliente deve ter pelo menos 13 anos para cadastro
- Cliente deve ter mais de 18 anos para realizar compras

### Estoque
- Entrada aumenta estoque
- Saída diminui estoque
- Não permite saída maior que estoque disponível
- Registro de todas as movimentações

### Vendas
- Deve conter pelo menos um item
- Não permite venda de medicamento inativo
- Não permite venda de medicamento vencido
- Não permite venda com estoque insuficiente
- Preço unitário deve ser igual ao preço atual
- Atualiza estoque automaticamente
- Calcula valor total automaticamente

### Alertas
- Estoque baixo: quantidade < 10 (configurável)
- Validade próxima: vence em até 30 dias (configurável)
- Considera apenas medicamentos ativos

## Instalação e Execução

### Pré-requisitos
- Java 17 ou superior
- Maven 3.6+
- Git

### Passos para Executar

1. **Clone o repositório**
```bash
git clone https://github.com/seu-usuario/farmacia-api.git
cd farmacia-api
```

2. **Compile o projeto**
```bash
mvn clean install
```

3. **Execute a aplicação**
```bash
mvn spring-boot:run
```

4. **Acesse a aplicação**
- API: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- H2 Console: http://localhost:8080/h2-console
- Login: http://localhost:8080/login

### Credenciais de Acesso

- **Usuário**: farmacia
- **Senha**: admin123

### Configurações

As configurações podem ser ajustadas no arquivo `src/main/resources/application.properties`:

- **Banco de Dados**: H2 em arquivo (localizado em `data/farmacia_db.mv.db`)
- **Porta do Servidor**: 8080 (padrão)
- **Alertas** (valores padrão, podem ser configurados via API ou application.properties):
  - Estoque baixo: quantidade < 10 (configurável via `app.alerta.estoque-baixo` ou endpoint `/api/alertas/config/estoque`)
  - Validade próxima: vence em até 30 dias (configurável via `app.alerta.validade-dias` ou endpoint `/api/alertas/config/validade`)

### Endpoints Principais

#### Medicamentos (`/api/medicamentos`)
- `POST /api/medicamentos` - Criar medicamento
- `GET /api/medicamentos` - Listar todos os medicamentos
- `GET /api/medicamentos/{id}` - Buscar medicamento por ID
- `PUT /api/medicamentos/{id}` - Atualizar medicamento
- `DELETE /api/medicamentos/{id}` - Excluir medicamento
- `PATCH /api/medicamentos/{id}/status` - Alterar status do medicamento

#### Categorias (`/api/categorias`)
- `POST /api/categorias` - Criar categoria
- `GET /api/categorias` - Listar todas as categorias
- `GET /api/categorias/{id}` - Buscar categoria por ID
- `DELETE /api/categorias/{id}` - Excluir categoria

#### Clientes (`/api/clientes`)
- `POST /api/clientes` - Criar cliente
- `GET /api/clientes` - Listar todos os clientes
- `GET /api/clientes/{id}` - Buscar cliente por ID
- `PUT /api/clientes/{id}` - Atualizar cliente

#### Vendas (`/api/vendas`)
- `POST /api/vendas` - Registrar nova venda
- `GET /api/vendas` - Listar todas as vendas
- `GET /api/vendas/{id}` - Buscar venda por ID
- `GET /api/vendas/cliente/{clienteId}` - Buscar vendas por cliente
- `GET /api/vendas/hoje` - Listar vendas do dia

#### Estoque (`/api/estoque`)
- `POST /api/estoque/entrada` - Registrar entrada no estoque
- `POST /api/estoque/saida` - Registrar saída do estoque
- `GET /api/estoque/{medicamentoId}` - Consultar movimentações de um medicamento
- `GET /api/estoque/{medicamentoId}/atual` - Consultar estoque atual
- `GET /api/estoque/recentes` - Listar movimentações recentes (últimos 30 dias)

#### Alertas (`/api/alertas`)
- `GET /api/alertas/estoque-baixo` - Listar alertas de estoque baixo
- `GET /api/alertas/validade-proxima` - Listar alertas de validade próxima
- `GET /api/alertas/todos` - Listar todos os alertas
- `PUT /api/alertas/config/estoque?limite={valor}` - Configurar limite de estoque baixo
- `PUT /api/alertas/config/validade?dias={valor}` - Configurar dias para validade próxima

### Observações

- O banco de dados H2 é persistido em arquivo, então os dados são mantidos entre execuções
- A autenticação é básica HTTP (não recomendado para produção)
- O Swagger está disponível para documentação e testes da API
- O endpoint `/login` retorna uma página HTML simples para autenticação
- Todos os endpoints da API (exceto `/h2-console/**`, `/swagger-ui/**`, `/v3/api-docs/**` e `/login`) requerem autenticação HTTP Basic
