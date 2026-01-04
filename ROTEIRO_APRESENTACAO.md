# Roteiro de Apresentação - API Farmacia
## Tempo: 10 minutos

---

## 📋 Estrutura da Apresentação

### 1. Introdução (1 minuto)
- **O que é**: API REST completa para gestão de farmácia
- **Tecnologias**: Java 17, Spring Boot 3.2, H2 Database
- **Acesso**: 
  - Swagger UI: http://localhost:8080/swagger-ui/index.html
  - Credenciais: `farmacia` / `admin123`

### 2. Demonstração das Funcionalidades (8 minutos)

#### 2.1. Gestão de Categorias (1 min)
**Objetivo**: Mostrar cadastro e listagem de categorias

**Teste 1: Criar Categoria**
```bash
POST http://localhost:8080/api/categorias
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "nome": "Analgésicos",
  "descricao": "Medicamentos para dor"
}
```

**Teste 2: Listar Categorias**
```bash
GET http://localhost:8080/api/categorias
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Destaque**: Mostrar que `quantidadeMedicamentos` é calculado automaticamente

---

#### 2.2. Gestão de Medicamentos (2 min)
**Objetivo**: Demonstrar CRUD completo e regras de negócio

**Teste 3: Criar Medicamento**
```bash
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

**Teste 4: Listar Medicamentos**
```bash
GET http://localhost:8080/api/medicamentos
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Teste 5: Buscar Medicamento por ID**
```bash
GET http://localhost:8080/api/medicamentos/1
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Destaque**: 
- Validações (nome único, data futura, preço > 0)
- Status do medicamento (ATIVO/INATIVO)
- Categoria com quantidadeMedicamentos calculado

---

#### 2.3. Gestão de Clientes (1 min)
**Objetivo**: Mostrar cadastro com validações de CPF e idade

**Teste 6: Criar Cliente**
```bash
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

**Teste 7: Listar Clientes**
```bash
GET http://localhost:8080/api/clientes
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Destaque**: 
- Validação de CPF
- Cálculo automático de maior de idade
- Total de compras do cliente

---

#### 2.4. Controle de Estoque (1.5 min)
**Objetivo**: Demonstrar movimentações de estoque

**Teste 8: Registrar Entrada no Estoque**
```bash
POST http://localhost:8080/api/estoque/entrada
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
Content-Type: application/json

{
  "medicamentoId": 1,
  "quantidade": 20,
  "observacao": "Reposição de estoque"
}
```

**Teste 9: Consultar Estoque Atual**
```bash
GET http://localhost:8080/api/estoque/1/atual
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Teste 10: Consultar Movimentações**
```bash
GET http://localhost:8080/api/estoque/1
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Destaque**: 
- Rastreabilidade completa das movimentações
- Histórico de entradas e saídas

---

#### 2.5. Processamento de Vendas (2 min)
**Objetivo**: Demonstrar fluxo completo de venda com regras de negócio

**Teste 11: Registrar Venda**
```bash
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

**Teste 12: Listar Vendas**
```bash
GET http://localhost:8080/api/vendas
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Teste 13: Buscar Vendas por Cliente**
```bash
GET http://localhost:8080/api/vendas/cliente/1
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Destaque**: 
- Validações automáticas (estoque, validade, status)
- Atualização automática de estoque
- Cálculo automático do valor total
- Histórico de compras do cliente

---

#### 2.6. Sistema de Alertas (0.5 min)
**Objetivo**: Mostrar alertas inteligentes do sistema

**Teste 14: Alertas de Estoque Baixo**
```bash
GET http://localhost:8080/api/alertas/estoque-baixo
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Teste 15: Alertas de Validade Próxima**
```bash
GET http://localhost:8080/api/alertas/validade-proxima
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Teste 16: Todos os Alertas**
```bash
GET http://localhost:8080/api/alertas/todos
Authorization: Basic ZmFybWFjaWE6YWRtaW4xMjM=
```

**Destaque**: 
- Alertas automáticos e configuráveis
- Prevenção de problemas de estoque e validade

---

### 3. Conclusão (1 minuto)
- **Resumo**: API completa com CRUD, validações, regras de negócio e alertas
- **Diferenciais**: 
  - Validações robustas
  - Rastreabilidade completa
  - Sistema de alertas inteligente
  - Documentação Swagger integrada
- **Próximos passos**: Testes automatizados, deploy em produção

---

## 🔧 Preparação para Demonstração

### Pré-requisitos
1. Aplicação rodando: `mvn spring-boot:run`
2. Swagger UI aberto: http://localhost:8080/swagger-ui/index.html
3. Postman/Insomnia ou similar configurado com autenticação

### Dados de Teste Sugeridos (Pré-cadastrar antes da apresentação)

**Categorias:**
- Analgésicos
- Antibióticos
- Vitaminas

**Medicamentos:**
- Paracetamol 500mg (Analgésicos) - Estoque: 50
- Amoxicilina 500mg (Antibióticos) - Estoque: 5 (para alerta)
- Vitamina C (Vitaminas) - Validade próxima (para alerta)

**Cliente:**
- João Silva (CPF válido, maior de 18 anos)

---

## 📝 Notas para Apresentação

1. **Use o Swagger UI** para demonstrações visuais mais impactantes
2. **Destaque as validações** tentando criar dados inválidos
3. **Mostre os alertas** após criar medicamentos com estoque baixo ou validade próxima
4. **Demonstre o fluxo completo**: Categoria → Medicamento → Cliente → Venda → Estoque atualizado
5. **Mencione as regras de negócio** enquanto demonstra (ex: "Veja que não permite vender medicamento inativo")

---

## 🎯 Pontos-Chave para Enfatizar

✅ **Validações robustas** (CPF, email, idade, estoque, validade)  
✅ **Rastreabilidade completa** (histórico de movimentações)  
✅ **Regras de negócio** (proteção de integridade, validações de venda)  
✅ **Sistema de alertas** (prevenção proativa)  
✅ **Documentação Swagger** (facilita integração)  
✅ **Arquitetura limpa** (separação de responsabilidades)

