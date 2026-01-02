# Análise de Coerência do README.md com o Projeto

## ✅ Pontos Corretos e Coerentes

### 1. Tecnologias Utilizadas
- ✅ Java 17 - Confirmado no `pom.xml` (`<java.version>17</java.version>`)
- ✅ Spring Boot 3.2.0 - Confirmado no `pom.xml` (versão 3.2.0)
- ✅ Spring Data JPA - Confirmado (`spring-boot-starter-data-jpa`)
- ✅ Spring Security - Confirmado (`spring-boot-starter-security`)
- ✅ Spring Validation - Confirmado (`spring-boot-starter-validation`)
- ✅ H2 Database - Confirmado (dependência H2)
- ✅ ModelMapper - Confirmado (versão 3.2.0)
- ✅ Swagger/OpenAPI 3.0 - Confirmado (`springdoc-openapi-starter-webmvc-ui` versão 2.3.0)
- ✅ Lombok - Confirmado
- ✅ Maven - Confirmado

### 2. Funcionalidades
- ✅ Todas as funcionalidades mencionadas estão implementadas
- ✅ CRUD completo de medicamentos, categorias e clientes
- ✅ Controle de estoque com movimentações
- ✅ Processamento de vendas
- ✅ Sistema de alertas
- ✅ Validações e tratamento de erros
- ✅ Autenticação básica HTTP
- ✅ Documentação Swagger

### 3. Endpoints
- ✅ Todos os endpoints mencionados no README existem no código
- ✅ Endpoints detalhados estão corretos
- ✅ Métodos HTTP estão corretos (GET, POST, PUT, DELETE, PATCH)

### 4. Regras de Negócio - Medicamentos
- ✅ Nome obrigatório e único - Implementado (`@NotBlank` + `ValidatorService`)
- ✅ Preço deve ser maior que zero - Implementado (`@DecimalMin(value = "0.01")`)
- ✅ Quantidade não pode ser negativa - Implementado (`@Min(value = 0)`)
- ✅ Data de validade deve ser futura - Implementado (`@Future`)
- ✅ Medicamentos inativos não podem ser vendidos - Implementado (`VendaService.validarMedicamentoParaVenda()`)
- ✅ Não permite exclusão se já foi vendido - Implementado (`MedicamentoService.excluir()`)

### 5. Regras de Negócio - Categorias
- ✅ Nome obrigatório e único - Implementado
- ✅ Não permite exclusão se vinculada a medicamentos - Implementado (`CategoriaService.excluir()`)

### 6. Regras de Negócio - Clientes
- ✅ CPF obrigatório e válido - Implementado (`CPFValidator`)
- ✅ CPF único - Implementado (`ValidatorService.validarCpfUnico()`)
- ✅ Email obrigatório e válido - Implementado (`@NotBlank` + `@Email`)
- ✅ Cliente deve ter pelo menos 13 anos para cadastro - Implementado (`ClienteService.validarCliente()`)
- ✅ Cliente deve ter mais de 18 anos para realizar compras - Implementado (`VendaService.criar()`)

### 7. Regras de Negócio - Estoque
- ✅ Entrada aumenta estoque - Implementado
- ✅ Saída diminui estoque - Implementado
- ✅ Não permite saída maior que estoque disponível - Implementado
- ✅ Registro de todas as movimentações - Implementado

### 8. Regras de Negócio - Vendas
- ✅ Deve conter pelo menos um item - Implementado (`VendaService.validarVenda()`)
- ✅ Não permite venda de medicamento inativo - Implementado
- ✅ Não permite venda de medicamento vencido - Implementado
- ✅ Não permite venda com estoque insuficiente - Implementado
- ✅ Preço unitário deve ser igual ao preço atual - Implementado
- ✅ Atualiza estoque automaticamente - Implementado
- ✅ Calcula valor total automaticamente - Implementado

### 9. Regras de Negócio - Alertas
- ✅ Estoque baixo: quantidade < 10 (configurável) - Implementado
- ✅ Validade próxima: vence em até 30 dias (configurável) - Implementado
- ✅ Considera apenas medicamentos ativos - Implementado

### 10. Configurações
- ✅ Credenciais de acesso corretas (farmacia/admin123)
- ✅ Porta do servidor correta (8080)
- ✅ Caminho do banco de dados mencionado corretamente (`data/farmacia_db.mv.db`)
- ✅ Endpoints de configuração de alertas documentados

## ⚠️ Pontos que Precisam de Atenção

### 1. Regra de Negócio Faltante no README
**Problema**: O README menciona que não permite exclusão de medicamento vendido, mas não menciona que também não permite **atualização**.

**Evidência no Código**:
```java
// MedicamentoService.java linha 48-50
if (medicamento.isVendido()) {
    throw new BusinessException("Não é possível alterar medicamento que já foi vendido");
}
```

**Recomendação**: Adicionar esta regra na seção de Medicamentos:
```
- Não permite exclusão se já foi vendido (proteção de integridade)
- Não permite atualização se já foi vendido (proteção de integridade)
```

### 2. Caminho do Banco de Dados no application.properties
**Problema**: O `application.properties` contém um caminho absoluto específico do Windows:
```properties
spring.datasource.url=jdbc:h2:file:C:/Projetos/Java/farmacia-api/data/farmacia_db
```

**Observação**: O README menciona corretamente o caminho relativo (`data/farmacia_db.mv.db`), mas o arquivo de configuração usa caminho absoluto. Isso pode causar problemas em diferentes ambientes.

**Recomendação**: 
- Manter o README como está (caminho relativo é mais genérico)
- Considerar atualizar o `application.properties` para usar caminho relativo ou variável de ambiente

### 3. Tecnologias Adicionais no pom.xml não Mencionadas
**Observação**: O `pom.xml` contém algumas dependências não mencionadas no README:
- `spring-boot-starter-thymeleaf` (usado para templates HTML)
- `spring-boot-devtools` (ferramentas de desenvolvimento)

**Recomendação**: 
- Estas são dependências auxiliares e não precisam necessariamente estar no README
- Se quiser ser mais completo, pode adicionar uma nota sobre Thymeleaf (usado para a página de login)

### 4. Endpoint PUT para Categorias
**Observação**: O README não menciona endpoint `PUT /api/categorias/{id}` para atualizar categorias, e de fato não existe no código. Isso está correto - categorias não têm endpoint de atualização implementado.

## 📊 Resumo da Análise

| Categoria | Status | Observações |
|-----------|--------|-------------|
| Tecnologias | ✅ 100% | Todas corretas |
| Funcionalidades | ✅ 100% | Todas implementadas |
| Endpoints | ✅ 100% | Todos corretos |
| Regras de Negócio | ⚠️ 95% | Falta mencionar regra de atualização de medicamento vendido |
| Configurações | ✅ 100% | Todas corretas |
| Credenciais | ✅ 100% | Corretas |

## 🎯 Conclusão

O README está **muito coerente** com o projeto. A única melhoria recomendada é adicionar a regra de negócio sobre não permitir atualização de medicamento vendido, que está implementada mas não documentada.

**Score de Coerência: 98/100**

O documento está bem estruturado, completo e preciso. As pequenas observações são melhorias opcionais que aumentariam ainda mais a qualidade da documentação.

