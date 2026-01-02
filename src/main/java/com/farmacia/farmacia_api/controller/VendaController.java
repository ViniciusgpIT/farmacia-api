package com.farmacia.farmacia_api.controller;

import com.farmacia.farmacia_api.model.dto.request.VendaRequest;
import com.farmacia.farmacia_api.model.dto.response.VendaResponse;
import com.farmacia.farmacia_api.service.VendaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vendas")
@RequiredArgsConstructor
@Tag(name = "Vendas", description = "Operações relacionadas a vendas da farmácia")
public class VendaController {
    
    private final VendaService vendaService;
    
    @PostMapping
    @Operation(summary = "Registrar uma nova venda", description = "Registra uma nova venda na farmácia")
    public ResponseEntity<VendaResponse> criar(@Valid @RequestBody VendaRequest request) {
        VendaResponse response = vendaService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @Operation(summary = "Listar todas as vendas", description = "Retorna todas as vendas registradas")
    public ResponseEntity<List<VendaResponse>> listarTodos() {
        List<VendaResponse> vendas = vendaService.listarTodas();
        return ResponseEntity.ok(vendas);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Buscar venda por ID", description = "Retorna uma venda específica pelo seu ID")
    public ResponseEntity<VendaResponse> buscarPorId(@PathVariable Long id) {
        VendaResponse response = vendaService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/cliente/{clienteId}")
    @Operation(summary = "Buscar vendas por cliente", description = "Retorna todas as vendas de um cliente específico")
    public ResponseEntity<List<VendaResponse>> buscarPorCliente(@PathVariable Long clienteId) {
        List<VendaResponse> vendas = vendaService.buscarPorCliente(clienteId);
        return ResponseEntity.ok(vendas);
    }
    
    @GetMapping("/hoje")
    @Operation(summary = "Listar vendas do dia", description = "Retorna todas as vendas realizadas hoje")
    public ResponseEntity<List<VendaResponse>> listarVendasDoDia() {
        List<VendaResponse> vendas = vendaService.buscarVendasDoDia();
        return ResponseEntity.ok(vendas);
    }
}
