package com.farmacia.farmacia_api.controller;

import com.farmacia.farmacia_api.model.dto.request.MedicamentoRequest;
import com.farmacia.farmacia_api.model.dto.response.MedicamentoResponse;
import com.farmacia.farmacia_api.model.dto.enums.StatusMedicamento;
import com.farmacia.farmacia_api.service.MedicamentoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medicamentos")
@RequiredArgsConstructor
@Tag(name = "Medicamentos", description = "Operações relacionadas a medicamentos")
public class MedicamentoController {

    private final MedicamentoService medicamentoService;

    @PostMapping
    @Operation(summary = "Criar um novo medicamento")
    public ResponseEntity<MedicamentoResponse> criar(@Valid @RequestBody MedicamentoRequest request) {
        MedicamentoResponse response = medicamentoService.criar(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar um medicamento existente")
    public ResponseEntity<MedicamentoResponse> atualizar(
            @PathVariable Long id,
            @Valid @RequestBody MedicamentoRequest request) {
        MedicamentoResponse response = medicamentoService.atualizar(id, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Listar todos os medicamentos")
    public ResponseEntity<List<MedicamentoResponse>> listarTodos() {
        List<MedicamentoResponse> medicamentos = medicamentoService.listarTodos();
        return ResponseEntity.ok(medicamentos);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar medicamento por ID")
    public ResponseEntity<MedicamentoResponse> buscarPorId(@PathVariable Long id) {
        MedicamentoResponse response = medicamentoService.buscarPorId(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Excluir um medicamento")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        medicamentoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Alterar status do medicamento (ATIVO/INATIVO)")
    public ResponseEntity<MedicamentoResponse> alterarStatus(
            @PathVariable Long id,
            @RequestParam StatusMedicamento status) {
        MedicamentoResponse response = medicamentoService.alterarStatus(id, status);
        return ResponseEntity.ok(response);
    }
}