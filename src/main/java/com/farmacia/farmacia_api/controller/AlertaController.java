package com.farmacia.farmacia_api.controller;

import com.farmacia.farmacia_api.model.dto.response.AlertaResponse;
import com.farmacia.farmacia_api.service.AlertaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/alertas")
@RequiredArgsConstructor
@Tag(name = "Alertas", description = "Operações relacionadas aos alertas do sistema")
public class AlertaController {

    private final AlertaService alertaService;

    @GetMapping("/estoque-baixo")
    @Operation(summary = "Listar alertas de estoque baixo", description = "Retorna medicamentos com estoque abaixo do limite configurado")
    public ResponseEntity<List<AlertaResponse>> listarAlertasEstoqueBaixo() {
        List<AlertaResponse> alertas = alertaService.gerarAlertasEstoqueBaixo();
        return ResponseEntity.ok(alertas);
    }

    @GetMapping("/validade-proxima")
    @Operation(summary = "Listar alertas de validade próxima", description = "Retorna medicamentos com validade próxima ao vencimento")
    public ResponseEntity<List<AlertaResponse>> listarAlertasValidadeProxima() {
        List<AlertaResponse> alertas = alertaService.gerarAlertasValidadeProxima();
        return ResponseEntity.ok(alertas);
    }

    @GetMapping("/todos")
    @Operation(summary = "Listar todos os alertas", description = "Retorna todos os alertas do sistema")
    public ResponseEntity<List<AlertaResponse>> listarTodosAlertas() {
        List<AlertaResponse> alertas = alertaService.gerarTodosAlertas();
        return ResponseEntity.ok(alertas);
    }

    @PutMapping("/config/estoque")
    @Operation(summary = "Configurar limite de estoque baixo", description = "Define o limite para alertas de estoque baixo")
    public ResponseEntity<Void> configurarLimiteEstoque(@RequestParam Integer limite) {
        alertaService.configurarLimiteEstoqueBaixo(limite);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/config/validade")
    @Operation(summary = "Configurar dias para validade próxima", description = "Define os dias para alertas de validade próxima")
    public ResponseEntity<Void> configurarDiasValidade(@RequestParam Integer dias) {
        alertaService.configurarDiasValidadeProxima(dias);
        return ResponseEntity.ok().build();
    }
}