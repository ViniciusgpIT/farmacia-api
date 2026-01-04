# Exemplos de Testes - API Farmacia
## Coleção de Requisições para Demonstração

---

## 🔐 Autenticação

**Credenciais:**
- Usuário: `farmacia`
- Senha: `admin123`

**Header de Autenticação (Base64):**
```
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Ou configure no Postman/Insomnia:**
- Type: Basic Auth
- Username: `farmacia`
- Password: `admin123`

---

## 📦 1. CATEGORIAS

### 1.1. Criar Categoria
```http
POST http://localhost:8080/api/categorias
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "nome": "Analgésicos",
  "descricao": "Medicamentos para alívio de dores"
}
```

**Resposta Esperada (201 Created):**
```json
{
  "id": 1,
  "nome": "Analgésicos",
  "descricao": "Medicamentos para alívio de dores",
  "quantidadeMedicamentos": 0
}
```

### 1.2. Criar Mais Categorias
```http
POST http://localhost:8080/api/categorias
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "nome": "Antibióticos",
  "descricao": "Medicamentos antibacterianos"
}
```

```http
POST http://localhost:8080/api/categorias
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "nome": "Vitaminas",
  "descricao": "Suplementos vitamínicos"
}
```

### 1.3. Listar Todas as Categorias
```http
GET http://localhost:8080/api/categorias
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

### 1.4. Buscar Categoria por ID
```http
GET http://localhost:8080/api/categorias/1
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

---

## 💊 2. MEDICAMENTOS

### 2.1. Criar Medicamento
```http
POST http://localhost:8080/api/medicamentos
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "nome": "Paracetamol 500mg",
  "descricao": "Analgésico e antitérmico",
  "preco": 15.90,
  "quantidade": 50,
  "dataValidade": "2025-12-31",
  "categoriaId": 1
}
```

**Resposta Esperada (201 Created):**
```json
{
  "id": 1,
  "nome": "Paracetamol 500mg",
  "descricao": "Analgésico e antitérmico",
  "preco": 15.90,
  "quantidade": 50,
  "dataValidade": "2025-12-31",
  "status": "ATIVO",
  "categoria": {
    "id": 1,
    "nome": "Analgésicos",
    "descricao": "Medicamentos para alívio de dores",
    "quantidadeMedicamentos": 1
  }
}
```

### 2.2. Criar Medicamento com Estoque Baixo (para alerta)
```http
POST http://localhost:8080/api/medicamentos
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "nome": "Amoxicilina 500mg",
  "descricao": "Antibiótico de amplo espectro",
  "preco": 25.50,
  "quantidade": 5,
  "dataValidade": "2026-06-30",
  "categoriaId": 2
}
```

### 2.3. Criar Medicamento com Validade Próxima (para alerta)
```http
POST http://localhost:8080/api/medicamentos
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "nome": "Vitamina C 1000mg",
  "descricao": "Suplemento vitamínico",
  "preco": 12.90,
  "quantidade": 30,
  "dataValidade": "2025-02-15",
  "categoriaId": 3
}
```

### 2.4. Listar Todos os Medicamentos
```http
GET http://localhost:8080/api/medicamentos
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

### 2.5. Buscar Medicamento por ID
```http
GET http://localhost:8080/api/medicamentos/1
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

### 2.6. Atualizar Medicamento
```http
PUT http://localhost:8080/api/medicamentos/1
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "nome": "Paracetamol 500mg",
  "descricao": "Analgésico e antitérmico - Atualizado",
  "preco": 16.90,
  "quantidade": 50,
  "dataValidade": "2025-12-31",
  "categoriaId": 1
}
```

### 2.7. Alterar Status do Medicamento
```http
PATCH http://localhost:8080/api/medicamentos/1/status?status=INATIVO
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

### 2.8. Teste de Validação - Nome Duplicado (deve falhar)
```http
POST http://localhost:8080/api/medicamentos
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "nome": "Paracetamol 500mg",
  "descricao": "Tentativa de duplicar",
  "preco": 15.90,
  "quantidade": 50,
  "dataValidade": "2025-12-31",
  "categoriaId": 1
}
```

**Resposta Esperada (400 Bad Request):**
```json
{
  "message": "Já existe um medicamento com este nome"
}
```

---

## 👤 3. CLIENTES

### 3.1. Criar Cliente
```http
POST http://localhost:8080/api/clientes
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "nome": "João Silva",
  "cpf": "12345678909",
  "email": "joao.silva@email.com",
  "dataNascimento": "1990-05-15"
}
```

**Resposta Esperada (201 Created):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "cpf": "12345678909",
  "email": "joao.silva@email.com",
  "dataNascimento": "1990-05-15",
  "maiorDeIdade": true,
  "totalCompras": 0
}
```

### 3.2. Criar Cliente Menor de Idade (deve falhar)
```http
POST http://localhost:8080/api/clientes
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "email": "maria@email.com",
  "dataNascimento": "2015-01-01"
}
```

**Resposta Esperada (400 Bad Request):**
```json
{
  "message": "Cliente deve ter pelo menos 13 anos"
}
```

### 3.3. Listar Todos os Clientes
```http
GET http://localhost:8080/api/clientes
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

### 3.4. Buscar Cliente por ID
```http
GET http://localhost:8080/api/clientes/1
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

### 3.5. Atualizar Cliente
```http
PUT http://localhost:8080/api/clientes/1
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "nome": "João Silva Santos",
  "cpf": "12345678909",
  "email": "joao.silva.santos@email.com",
  "dataNascimento": "1990-05-15"
}
```

---

## 📦 4. ESTOQUE

### 4.1. Registrar Entrada no Estoque
```http
POST http://localhost:8080/api/estoque/entrada
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "medicamentoId": 1,
  "quantidade": 20,
  "observacao": "Reposição de estoque - Compra do fornecedor"
}
```

**Resposta Esperada (200 OK):**
```json
{
  "id": 1,
  "medicamento": {
    "id": 1,
    "nome": "Paracetamol 500mg",
    ...
  },
  "tipo": "ENTRADA",
  "quantidade": 20,
  "dataMovimentacao": "2025-01-27T10:30:00",
  "observacao": "Reposição de estoque - Compra do fornecedor"
}
```

### 4.2. Registrar Saída no Estoque
```http
POST http://localhost:8080/api/estoque/saida
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "medicamentoId": 1,
  "quantidade": 5,
  "observacao": "Ajuste de inventário"
}
```

### 4.3. Consultar Estoque Atual
```http
GET http://localhost:8080/api/estoque/1/atual
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Resposta Esperada (200 OK):**
```json
{
  "medicamentoId": 1,
  "medicamentoNome": "Paracetamol 500mg",
  "quantidadeAtual": 65
}
```

### 4.4. Consultar Movimentações de um Medicamento
```http
GET http://localhost:8080/api/estoque/1
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

### 4.5. Listar Movimentações Recentes
```http
GET http://localhost:8080/api/estoque/recentes
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

### 4.6. Teste de Validação - Saída Maior que Estoque (deve falhar)
```http
POST http://localhost:8080/api/estoque/saida
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "medicamentoId": 1,
  "quantidade": 1000,
  "observacao": "Tentativa de saída maior que estoque"
}
```

**Resposta Esperada (400 Bad Request):**
```json
{
  "message": "Quantidade insuficiente em estoque"
}
```

---

## 🛒 5. VENDAS

### 5.1. Registrar Venda
```http
POST http://localhost:8080/api/vendas
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "clienteId": 1,
  "itens": [
    {
      "medicamentoId": 1,
      "quantidade": 2,
      "precoUnitario": 15.90
    }
  ]
}
```

**Resposta Esperada (201 Created):**
```json
{
  "id": 1,
  "cliente": {
    "id": 1,
    "nome": "João Silva",
    ...
  },
  "itens": [
    {
      "id": 1,
      "medicamentoId": 1,
      "medicamentoNome": "Paracetamol 500mg",
      "quantidade": 2,
      "precoUnitario": 15.90,
      "subtotal": 31.80
    }
  ],
  "valorTotal": 31.80,
  "dataVenda": "2025-01-27T10:35:00",
  "quantidadeItens": 1
}
```

### 5.2. Registrar Venda com Múltiplos Itens
```http
POST http://localhost:8080/api/vendas
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "clienteId": 1,
  "itens": [
    {
      "medicamentoId": 1,
      "quantidade": 1,
      "precoUnitario": 15.90
    },
    {
      "medicamentoId": 2,
      "quantidade": 1,
      "precoUnitario": 25.50
    }
  ]
}
```

### 5.3. Listar Todas as Vendas
```http
GET http://localhost:8080/api/vendas
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

### 5.4. Buscar Venda por ID
```http
GET http://localhost:8080/api/vendas/1
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

### 5.5. Buscar Vendas por Cliente
```http
GET http://localhost:8080/api/vendas/cliente/1
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

### 5.6. Listar Vendas do Dia
```http
GET http://localhost:8080/api/vendas/hoje
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

### 5.7. Teste de Validação - Venda de Medicamento Inativo (deve falhar)
```http
POST http://localhost:8080/api/vendas
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "clienteId": 1,
  "itens": [
    {
      "medicamentoId": 1,
      "quantidade": 1,
      "precoUnitario": 15.90
    }
  ]
}
```
*(Primeiro inative o medicamento com PATCH /api/medicamentos/1/status?status=INATIVO)*

**Resposta Esperada (400 Bad Request):**
```json
{
  "message": "Não é possível vender medicamento inativo"
}
```

### 5.8. Teste de Validação - Estoque Insuficiente (deve falhar)
```http
POST http://localhost:8080/api/vendas
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "clienteId": 1,
  "itens": [
    {
      "medicamentoId": 1,
      "quantidade": 1000,
      "precoUnitario": 15.90
    }
  ]
}
```

**Resposta Esperada (400 Bad Request):**
```json
{
  "message": "Estoque insuficiente para o medicamento: Paracetamol 500mg"
}
```

---

## 🚨 6. ALERTAS

### 6.1. Listar Alertas de Estoque Baixo
```http
GET http://localhost:8080/api/alertas/estoque-baixo
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Resposta Esperada (200 OK):**
```json
[
  {
    "tipo": "ESTOQUE_BAIXO",
    "medicamento": {
      "id": 2,
      "nome": "Amoxicilina 500mg",
      ...
    },
    "mensagem": "Estoque baixo: 5 unidades",
    "quantidade": 5
  }
]
```

### 6.2. Listar Alertas de Validade Próxima
```http
GET http://localhost:8080/api/alertas/validade-proxima
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

### 6.3. Listar Todos os Alertas
```http
GET http://localhost:8080/api/alertas/todos
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

### 6.4. Configurar Limite de Estoque Baixo
```http
PUT http://localhost:8080/api/alertas/config/estoque?limite=15
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

### 6.5. Configurar Dias para Validade Próxima
```http
PUT http://localhost:8080/api/alertas/config/validade?dias=60
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

---

## 📋 7. FLUXO COMPLETO DE DEMONSTRAÇÃO

### Sequência Recomendada para Apresentação:

1. **Criar Categoria** → Teste 1.1
2. **Criar Medicamento** → Teste 2.1
3. **Verificar quantidadeMedicamentos na categoria** → Teste 1.4
4. **Criar Cliente** → Teste 3.1
5. **Registrar Entrada no Estoque** → Teste 4.1
6. **Consultar Estoque Atual** → Teste 4.3
7. **Registrar Venda** → Teste 5.1
8. **Verificar Estoque Atualizado** → Teste 4.3
9. **Verificar Total de Compras do Cliente** → Teste 3.4
10. **Consultar Alertas** → Teste 6.1 e 6.2

---

## 🔍 8. TESTES DE VALIDAÇÃO (Demonstrar Regras de Negócio)

### 8.1. Tentar Criar Medicamento com Nome Duplicado
→ Teste 2.8

### 8.2. Tentar Criar Cliente Menor de 13 Anos
→ Teste 3.2

### 8.3. Tentar Saída Maior que Estoque
→ Teste 4.6

### 8.4. Tentar Vender Medicamento Inativo
→ Teste 5.7

### 8.5. Tentar Vender com Estoque Insuficiente
→ Teste 5.8

---

## 💡 DICAS PARA USO

1. **Use o Swagger UI** para testes interativos: http://localhost:8080/swagger-ui/index.html
2. **Configure autenticação** uma vez no Postman/Insomnia como variável de ambiente
3. **Salve os IDs** retornados para usar nos próximos testes
4. **Execute na ordem** sugerida para um fluxo lógico
5. **Demonstre as validações** mostrando os erros retornados

---

## 📊 Resumo dos Endpoints

| Módulo | Método | Endpoint | Descrição |
|--------|--------|----------|-----------|
| Categorias | POST | `/api/categorias` | Criar categoria |
| Categorias | GET | `/api/categorias` | Listar categorias |
| Categorias | GET | `/api/categorias/{id}` | Buscar categoria |
| Medicamentos | POST | `/api/medicamentos` | Criar medicamento |
| Medicamentos | GET | `/api/medicamentos` | Listar medicamentos |
| Medicamentos | GET | `/api/medicamentos/{id}` | Buscar medicamento |
| Medicamentos | PUT | `/api/medicamentos/{id}` | Atualizar medicamento |
| Medicamentos | PATCH | `/api/medicamentos/{id}/status` | Alterar status |
| Clientes | POST | `/api/clientes` | Criar cliente |
| Clientes | GET | `/api/clientes` | Listar clientes |
| Clientes | GET | `/api/clientes/{id}` | Buscar cliente |
| Estoque | POST | `/api/estoque/entrada` | Entrada no estoque |
| Estoque | POST | `/api/estoque/saida` | Saída do estoque |
| Estoque | GET | `/api/estoque/{id}/atual` | Estoque atual |
| Estoque | GET | `/api/estoque/{id}` | Movimentações |
| Vendas | POST | `/api/vendas` | Registrar venda |
| Vendas | GET | `/api/vendas` | Listar vendas |
| Vendas | GET | `/api/vendas/{id}` | Buscar venda |
| Vendas | GET | `/api/vendas/cliente/{id}` | Vendas por cliente |
| Alertas | GET | `/api/alertas/estoque-baixo` | Estoque baixo |
| Alertas | GET | `/api/alertas/validade-proxima` | Validade próxima |
| Alertas | GET | `/api/alertas/todos` | Todos os alertas |

