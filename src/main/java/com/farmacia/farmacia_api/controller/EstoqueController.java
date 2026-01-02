package com.farmacia.farmacia_api.controller;


import com.farmacia.farmacia_api.model.dto.request.MovimentacaoEstoqueRequest;
import com.farmacia.farmacia_api.model.entity.MovimentacaoEstoque;
import com.farmacia.farmacia_api.service.EstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estoque")
@RequiredArgsConstructor
@Tag(name = "Estoque", description = "Operações relacionadas ao controle de estoque da farmácia")
public class EstoqueController {
    
    private final EstoqueService estoqueService;
    
    @PostMapping("/entrada")
    @Operation(summary = "Registrar entrada no estoque", description = "Registra a entrada de medicamentos no estoque")
    public ResponseEntity<MovimentacaoEstoque> registrarEntrada(
            @Valid @RequestBody MovimentacaoEstoqueRequest request) {
        MovimentacaoEstoque movimentacao = estoqueService.registrarEntrada(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacao);
    }
    
    @PostMapping("/saida")
    @Operation(summary = "Registrar saída do estoque", description = "Registra a saída de medicamentos do estoque")
    public ResponseEntity<MovimentacaoEstoque> registrarSaida(
            @Valid @RequestBody MovimentacaoEstoqueRequest request) {
        MovimentacaoEstoque movimentacao = estoqueService.registrarSaida(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(movimentacao);
    }
    
    @GetMapping("/{medicamentoId}")
    @Operation(summary = "Consultar movimentações de estoque", description = "Retorna todas as movimentações de um medicamento")
    public ResponseEntity<List<MovimentacaoEstoque>> consultarMovimentacoes(
            @PathVariable Long medicamentoId) {
        List<MovimentacaoEstoque> movimentacoes = 
            estoqueService.buscarMovimentacoesPorMedicamento(medicamentoId);
        return ResponseEntity.ok(movimentacoes);
    }
    
    @GetMapping("/{medicamentoId}/atual")
    @Operation(summary = "Consultar estoque atual", description = "Retorna a quantidade atual em estoque de um medicamento")
    public ResponseEntity<Integer> consultarEstoqueAtual(@PathVariable Long medicamentoId) {
        Integer quantidade = estoqueService.consultarEstoqueAtual(medicamentoId);
        return ResponseEntity.ok(quantidade);
    }
    
    @GetMapping("/recentes")
    @Operation(summary = "Listar movimentações recentes", description = "Retorna as movimentações de estoque dos últimos 30 dias")
    public ResponseEntity<List<MovimentacaoEstoque>> listarMovimentacoesRecentes() {
        List<MovimentacaoEstoque> movimentacoes = estoqueService.buscarMovimentacoesRecentes();
        return ResponseEntity.ok(movimentacoes);
    }
}