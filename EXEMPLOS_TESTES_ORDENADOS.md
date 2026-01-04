# Exemplos de Testes - API Farmacia
## Ordenados pela Lógica de Negócio

Este documento apresenta todos os endpoints da API organizados na ordem lógica do fluxo de negócio, do cadastro inicial até as consultas e alertas.

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

## 📋 ORDEM LÓGICA DO FLUXO DE NEGÓCIO

1. **Categorias** → Base para organização de medicamentos
2. **Medicamentos** → Produtos que dependem de categorias
3. **Clientes** → Necessários para realizar vendas
4. **Estoque - Entrada** → Reposição de medicamentos
5. **Vendas** → Processo principal que consome medicamentos e envolve clientes
6. **Estoque - Consultas** → Verificação de estoque após operações
7. **Estoque - Saída** → Ajustes manuais de estoque (opcional)
8. **Alertas** → Monitoramento do sistema

---

## 1️⃣ CATEGORIAS
### (Base para organização - Criar primeiro)

### 1.1. Criar Categoria - Analgésicos
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

### 1.2. Criar Categoria - Antibióticos
```http
POST http://localhost:8080/api/categorias
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "nome": "Antibióticos",
  "descricao": "Medicamentos antibacterianos"
}
```

### 1.3. Criar Categoria - Vitaminas
```http
POST http://localhost:8080/api/categorias
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "nome": "Vitaminas",
  "descricao": "Suplementos vitamínicos"
}
```

### 1.4. Listar Todas as Categorias
```http
GET http://localhost:8080/api/categorias
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Resposta Esperada (200 OK):**
```json
[
  {
    "id": 1,
    "nome": "Analgésicos",
    "descricao": "Medicamentos para alívio de dores",
    "quantidadeMedicamentos": 0
  },
  {
    "id": 2,
    "nome": "Antibióticos",
    "descricao": "Medicamentos antibacterianos",
    "quantidadeMedicamentos": 0
  },
  {
    "id": 3,
    "nome": "Vitaminas",
    "descricao": "Suplementos vitamínicos",
    "quantidadeMedicamentos": 0
  }
]
```

### 1.5. Buscar Categoria por ID
```http
GET http://localhost:8080/api/categorias/1
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

### 1.6. Excluir Categoria (apenas se não houver medicamentos vinculados)
```http
DELETE http://localhost:8080/api/categorias/1
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

---

## 2️⃣ MEDICAMENTOS
### (Dependem de categorias - Criar após categorias)

### 2.1. Criar Medicamento - Paracetamol (Estoque Normal)
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

**✨ Observação**: Note que `quantidadeMedicamentos` na categoria foi atualizado automaticamente!

### 2.2. Criar Medicamento - Amoxicilina (Estoque Baixo - para alerta)
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

### 2.3. Criar Medicamento - Vitamina C (Validade Próxima - para alerta)
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
  "descricao": "Analgésico e antitérmico - Descrição atualizada",
  "preco": 16.90,
  "quantidade": 50,
  "dataValidade": "2025-12-31",
  "categoriaId": 1
}
```

### 2.7. Alterar Status do Medicamento para INATIVO
```http
PATCH http://localhost:8080/api/medicamentos/1/status?status=INATIVO
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Resposta Esperada (200 OK):**
```json
{
  "id": 1,
  "nome": "Paracetamol 500mg",
  "status": "INATIVO",
  ...
}
```

### 2.8. Alterar Status do Medicamento para ATIVO
```http
PATCH http://localhost:8080/api/medicamentos/1/status?status=ATIVO
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

### 2.9. Excluir Medicamento (apenas se não foi vendido)
```http
DELETE http://localhost:8080/api/medicamentos/1
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

---

## 3️⃣ CLIENTES
### (Independentes, mas necessários para vendas)

### 3.1. Criar Cliente - Maior de Idade
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

**✨ Observação**: `maiorDeIdade` e `totalCompras` são calculados automaticamente!

### 3.2. Criar Cliente - Segundo Cliente
```http
POST http://localhost:8080/api/clientes
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "nome": "Maria Santos",
  "cpf": "98765432100",
  "email": "maria.santos@email.com",
  "dataNascimento": "1985-08-20"
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

### 3.6. Verificar se Cliente é Maior de Idade
```http
GET http://localhost:8080/api/clientes/1/maioridade
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Resposta Esperada (200 OK):**
```json
true
```

---

## 4️⃣ ESTOQUE - ENTRADA
### (Reposição de medicamentos - Antes das vendas)

### 4.1. Registrar Entrada no Estoque
```http
POST http://localhost:8080/api/estoque/entrada
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "medicamentoId": 1,
  "tipo": "ENTRADA",
  "quantidade": 20,
  "observacao": "Reposição de estoque - Compra do fornecedor"
}
```

**Resposta Esperada (201 Created):**
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

**✨ Observação**: O estoque do medicamento é atualizado automaticamente!

### 4.2. Registrar Outra Entrada
```http
POST http://localhost:8080/api/estoque/entrada
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "medicamentoId": 2,
  "tipo": "ENTRADA",
  "quantidade": 10,
  "observacao": "Recebimento de lote"
}
```

---

## 5️⃣ VENDAS
### (Processo principal - Consome medicamentos e envolve clientes)

### 5.1. Registrar Venda - Item Único
```http
POST http://localhost:8080/api/vendas
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "clienteId": 1,
  "itens": [
    {
      "medicamentoId": 1,
      "quantidade": 2
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
    "cpf": "12345678909",
    "email": "joao.silva@email.com",
    "maiorDeIdade": true,
    "totalCompras": 1
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

**✨ Observações**:
- `valorTotal` é calculado automaticamente
- `precoUnitario` usa o preço atual do medicamento
- Estoque do medicamento é reduzido automaticamente
- `totalCompras` do cliente é atualizado automaticamente

### 5.2. Registrar Venda - Múltiplos Itens
```http
POST http://localhost:8080/api/vendas
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "clienteId": 1,
  "itens": [
    {
      "medicamentoId": 1,
      "quantidade": 1
    },
    {
      "medicamentoId": 2,
      "quantidade": 1
    },
    {
      "medicamentoId": 3,
      "quantidade": 2
    }
  ]
}
```

### 5.3. Registrar Venda - Segundo Cliente
```http
POST http://localhost:8080/api/vendas
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "clienteId": 2,
  "itens": [
    {
      "medicamentoId": 1,
      "quantidade": 3
    }
  ]
}
```

### 5.4. Listar Todas as Vendas
```http
GET http://localhost:8080/api/vendas
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

### 5.5. Buscar Venda por ID
```http
GET http://localhost:8080/api/vendas/1
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

### 5.6. Buscar Vendas por Cliente
```http
GET http://localhost:8080/api/vendas/cliente/1
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Resposta Esperada (200 OK):**
```json
[
  {
    "id": 1,
    "cliente": {...},
    "itens": [...],
    "valorTotal": 31.80,
    "dataVenda": "2025-01-27T10:35:00",
    ...
  },
  {
    "id": 2,
    ...
  }
]
```

### 5.7. Listar Vendas do Dia
```http
GET http://localhost:8080/api/vendas/hoje
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

---

## 6️⃣ ESTOQUE - CONSULTAS
### (Verificar estoque após operações)

### 6.1. Consultar Estoque Atual de um Medicamento
```http
GET http://localhost:8080/api/estoque/1/atual
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Resposta Esperada (200 OK):**
```json
67
```

**✨ Observação**: Após entrada de 20 e venda de 2 unidades, o estoque do medicamento ID 1 que tinha 50, agora tem 67 (50 + 20 - 2 - 1 = 67, considerando outras vendas).

### 6.2. Consultar Movimentações de um Medicamento
```http
GET http://localhost:8080/api/estoque/1
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Resposta Esperada (200 OK):**
```json
[
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
    "observacao": "Reposição de estoque"
  }
]
```

**✨ Observação**: As vendas também criam movimentações de saída automaticamente!

### 6.3. Listar Movimentações Recentes (últimos 30 dias)
```http
GET http://localhost:8080/api/estoque/recentes
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

---

## 7️⃣ ESTOQUE - SAÍDA
### (Ajustes manuais de estoque - Opcional)

### 7.1. Registrar Saída Manual do Estoque
```http
POST http://localhost:8080/api/estoque/saida
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "medicamentoId": 1,
  "tipo": "SAIDA",
  "quantidade": 5,
  "observacao": "Ajuste de inventário - Perda ou dano"
}
```

**Resposta Esperada (201 Created):**
```json
{
  "id": 2,
  "medicamento": {...},
  "tipo": "SAIDA",
  "quantidade": 5,
  "dataMovimentacao": "2025-01-27T11:00:00",
  "observacao": "Ajuste de inventário - Perda ou dano"
}
```

**⚠️ Observação**: A saída não pode ser maior que o estoque disponível!

---

## 8️⃣ ALERTAS
### (Monitoramento do sistema - Depende de medicamentos)

### 8.1. Listar Alertas de Estoque Baixo
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
      "quantidade": 5,
      ...
    },
    "mensagem": "Estoque baixo: 5 unidades",
    "quantidade": 5
  }
]
```

### 8.2. Listar Alertas de Validade Próxima
```http
GET http://localhost:8080/api/alertas/validade-proxima
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Resposta Esperada (200 OK):**
```json
[
  {
    "tipo": "VALIDADE_PROXIMA",
    "medicamento": {
      "id": 3,
      "nome": "Vitamina C 1000mg",
      "dataValidade": "2025-02-15",
      ...
    },
    "mensagem": "Validade próxima: vence em 19 dias",
    "dataValidade": "2025-02-15"
  }
]
```

### 8.3. Listar Todos os Alertas
```http
GET http://localhost:8080/api/alertas/todos
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Resposta Esperada (200 OK):**
```json
[
  {
    "tipo": "ESTOQUE_BAIXO",
    "medicamento": {...},
    "mensagem": "Estoque baixo: 5 unidades",
    ...
  },
  {
    "tipo": "VALIDADE_PROXIMA",
    "medicamento": {...},
    "mensagem": "Validade próxima: vence em 19 dias",
    ...
  }
]
```

### 8.4. Configurar Limite de Estoque Baixo
```http
PUT http://localhost:8080/api/alertas/config/estoque?limite=15
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Resposta Esperada (200 OK):**
```json
{
  "message": "Limite de estoque baixo configurado para: 15"
}
```

### 8.5. Configurar Dias para Validade Próxima
```http
PUT http://localhost:8080/api/alertas/config/validade?dias=60
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Resposta Esperada (200 OK):**
```json
{
  "message": "Dias para validade próxima configurado para: 60"
}
```

---

## 🔄 FLUXO COMPLETO DE DEMONSTRAÇÃO

Siga esta sequência para uma demonstração completa do sistema:

### Fase 1: Setup Inicial (2 min)
1. ✅ Criar Categoria - Analgésicos (1.1)
2. ✅ Criar Categoria - Antibióticos (1.2)
3. ✅ Criar Categoria - Vitaminas (1.3)
4. ✅ Listar Categorias (1.4)

### Fase 2: Cadastro de Produtos (2 min)
5. ✅ Criar Medicamento - Paracetamol (2.1)
6. ✅ Criar Medicamento - Amoxicilina (2.2)
7. ✅ Criar Medicamento - Vitamina C (2.3)
8. ✅ Buscar Categoria (1.5) - Mostrar que `quantidadeMedicamentos` foi atualizado
9. ✅ Listar Medicamentos (2.4)

### Fase 3: Cadastro de Clientes (1 min)
10. ✅ Criar Cliente - João Silva (3.1)
11. ✅ Criar Cliente - Maria Santos (3.2)
12. ✅ Listar Clientes (3.3)

### Fase 4: Reposição de Estoque (1 min)
13. ✅ Registrar Entrada no Estoque (4.1)
14. ✅ Consultar Estoque Atual (6.1) - Verificar aumento

### Fase 5: Processamento de Vendas (2 min)
15. ✅ Registrar Venda - Item Único (5.1)
16. ✅ Consultar Estoque Atual (6.1) - Verificar redução
17. ✅ Buscar Cliente (3.4) - Mostrar `totalCompras` atualizado
18. ✅ Registrar Venda - Múltiplos Itens (5.2)
19. ✅ Buscar Vendas por Cliente (5.6)

### Fase 6: Consultas e Monitoramento (1.5 min)
20. ✅ Consultar Movimentações (6.2)
21. ✅ Listar Vendas (5.4)
22. ✅ Listar Alertas (8.3)
23. ✅ Configurar Alertas (8.4 e 8.5)

### Fase 7: Operações Complementares (0.5 min)
24. ✅ Atualizar Medicamento (2.6)
25. ✅ Alterar Status Medicamento (2.7)
26. ✅ Listar Movimentações Recentes (6.3)

---

## 🧪 TESTES DE VALIDAÇÃO

### Validação 1: Nome de Categoria Duplicado
```http
POST http://localhost:8080/api/categorias
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "nome": "Analgésicos",
  "descricao": "Tentativa de duplicar"
}
```
**Esperado**: 400 Bad Request - "Já existe uma categoria com este nome"

### Validação 2: Nome de Medicamento Duplicado
```http
POST http://localhost:8080/api/medicamentos
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "nome": "Paracetamol 500mg",
  "preco": 15.90,
  "quantidade": 50,
  "dataValidade": "2025-12-31",
  "categoriaId": 1
}
```
**Esperado**: 400 Bad Request - "Já existe um medicamento com este nome"

### Validação 3: Cliente Menor de 13 Anos
```http
POST http://localhost:8080/api/clientes
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "nome": "Criança",
  "cpf": "11111111111",
  "email": "crianca@email.com",
  "dataNascimento": "2015-01-01"
}
```
**Esperado**: 400 Bad Request - "Cliente deve ter pelo menos 13 anos"

### Validação 4: Saída Maior que Estoque
```http
POST http://localhost:8080/api/estoque/saida
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "medicamentoId": 1,
  "tipo": "SAIDA",
  "quantidade": 10000,
  "observacao": "Tentativa de saída maior que estoque"
}
```
**Esperado**: 400 Bad Request - "Estoque insuficiente"

### Validação 5: Venda de Medicamento Inativo
1. Primeiro, inative o medicamento (2.7)
2. Tente vender:
```http
POST http://localhost:8080/api/vendas
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "clienteId": 1,
  "itens": [
    {
      "medicamentoId": 1,
      "quantidade": 1
    }
  ]
}
```
**Esperado**: 400 Bad Request - "Não é possível vender medicamento inativo"

### Validação 6: Venda com Estoque Insuficiente
```http
POST http://localhost:8080/api/vendas
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "clienteId": 1,
  "itens": [
    {
      "medicamentoId": 1,
      "quantidade": 100000
    }
  ]
}
```
**Esperado**: 400 Bad Request - "Estoque insuficiente"

### Validação 7: Excluir Categoria com Medicamentos
```http
DELETE http://localhost:8080/api/categorias/1
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```
**Esperado**: 400 Bad Request - "Não é possível excluir categoria vinculada a medicamentos"

### Validação 8: Excluir Medicamento Vendido
Após realizar uma venda, tente excluir o medicamento:
```http
DELETE http://localhost:8080/api/medicamentos/1
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```
**Esperado**: 400 Bad Request - "Não é possível excluir medicamento que já foi vendido"

---

## 📊 RESUMO DOS ENDPOINTS

| Módulo | Método | Endpoint | # |
|--------|--------|----------|---|
| **Categorias** | POST | `/api/categorias` | 1.1-1.3 |
| | GET | `/api/categorias` | 1.4 |
| | GET | `/api/categorias/{id}` | 1.5 |
| | DELETE | `/api/categorias/{id}` | 1.6 |
| **Medicamentos** | POST | `/api/medicamentos` | 2.1-2.3 |
| | GET | `/api/medicamentos` | 2.4 |
| | GET | `/api/medicamentos/{id}` | 2.5 |
| | PUT | `/api/medicamentos/{id}` | 2.6 |
| | PATCH | `/api/medicamentos/{id}/status` | 2.7-2.8 |
| | DELETE | `/api/medicamentos/{id}` | 2.9 |
| **Clientes** | POST | `/api/clientes` | 3.1-3.2 |
| | GET | `/api/clientes` | 3.3 |
| | GET | `/api/clientes/{id}` | 3.4 |
| | PUT | `/api/clientes/{id}` | 3.5 |
| | GET | `/api/clientes/{id}/maioridade` | 3.6 |
| **Estoque** | POST | `/api/estoque/entrada` | 4.1-4.2 |
| | POST | `/api/estoque/saida` | 7.1 |
| | GET | `/api/estoque/{id}/atual` | 6.1 |
| | GET | `/api/estoque/{id}` | 6.2 |
| | GET | `/api/estoque/recentes` | 6.3 |
| **Vendas** | POST | `/api/vendas` | 5.1-5.3 |
| | GET | `/api/vendas` | 5.4 |
| | GET | `/api/vendas/{id}` | 5.5 |
| | GET | `/api/vendas/cliente/{id}` | 5.6 |
| | GET | `/api/vendas/hoje` | 5.7 |
| **Alertas** | GET | `/api/alertas/estoque-baixo` | 8.1 |
| | GET | `/api/alertas/validade-proxima` | 8.2 |
| | GET | `/api/alertas/todos` | 8.3 |
| | PUT | `/api/alertas/config/estoque` | 8.4 |
| | PUT | `/api/alertas/config/validade` | 8.5 |

**Total: 39 endpoints documentados**

---

## 💡 DICAS PARA USO

1. **Use IDs retornados**: Salve os IDs retornados nas respostas para usar nos próximos testes
2. **Siga a ordem**: A ordem lógica garante que todas as dependências estejam criadas
3. **Observe os cálculos**: Note que vários campos são calculados automaticamente
4. **Teste validações**: Execute os testes de validação para mostrar a robustez do sistema
5. **Use Swagger UI**: Para uma experiência visual melhor, use http://localhost:8080/swagger-ui/index.html

---

## 🔑 PONTOS-CHAVE DA DEMONSTRAÇÃO

✅ **Ordem Lógica**: Categorias → Medicamentos → Clientes → Estoque → Vendas → Alertas  
✅ **Cálculos Automáticos**: quantidadeMedicamentos, totalCompras, valorTotal  
✅ **Validações Robustas**: CPF, idade, estoque, validade, status  
✅ **Rastreabilidade**: Histórico completo de movimentações  
✅ **Sistema de Alertas**: Monitoramento proativo do sistema  
✅ **Regras de Negócio**: Proteção de integridade dos dados

