package com.farmacia.farmacia_api.service;

import com.farmacia.farmacia_api.model.dto.request.MovimentacaoEstoqueRequest;
import com.farmacia.farmacia_api.model.entity.Medicamento;
import com.farmacia.farmacia_api.model.entity.MovimentacaoEstoque;
import com.farmacia.farmacia_api.model.dto.enums.TipoMovimentacao;
import com.farmacia.farmacia_api.repository.MovimentacaoEstoqueRepository;
import com.farmacia.farmacia_api.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final MovimentacaoEstoqueRepository movimentacaoRepository;
    private final MedicamentoService medicamentoService;

    @Transactional
    public MovimentacaoEstoque registrarEntrada(MovimentacaoEstoqueRequest request) {
        if (!request.getTipo().equals(TipoMovimentacao.ENTRADA)) {
            throw new BusinessException("Tipo de movimentação inválido para entrada");
        }

        Medicamento medicamento = medicamentoService.buscarEntidadePorId(request.getMedicamentoId());

        // Valida quantidade positiva
        if (request.getQuantidade() <= 0) {
            throw new BusinessException("Quantidade de entrada deve ser maior que zero");
        }

        // Atualiza quantidade
        Integer novaQuantidade = medicamento.getQuantidade() + request.getQuantidade();
        medicamento.setQuantidade(novaQuantidade);

        // Registra movimentação
        MovimentacaoEstoque movimentacao = MovimentacaoEstoque.builder()
                .medicamento(medicamento)
                .tipo(TipoMovimentacao.ENTRADA)
                .quantidade(request.getQuantidade())
                .observacao(request.getObservacao())
                .dataMovimentacao(LocalDateTime.now())
                .build();

        return movimentacaoRepository.save(movimentacao);
    }

    @Transactional
    public MovimentacaoEstoque registrarSaida(MovimentacaoEstoqueRequest request) {
        if (!request.getTipo().equals(TipoMovimentacao.SAIDA)) {
            throw new BusinessException("Tipo de movimentação inválido para saída");
        }

        Medicamento medicamento = medicamentoService.buscarEntidadePorId(request.getMedicamentoId());

        // Valida estoque suficiente
        if (medicamento.getQuantidade() < request.getQuantidade()) {
            throw new BusinessException(
                    String.format("Estoque insuficiente. Disponível: %d, Solicitado: %d",
                            medicamento.getQuantidade(), request.getQuantidade()));
        }

        // Valida quantidade positiva
        if (request.getQuantidade() <= 0) {
            throw new BusinessException("Quantidade de saída deve ser maior que zero");
        }

        // Atualiza quantidade
        Integer novaQuantidade = medicamento.getQuantidade() - request.getQuantidade();
        medicamento.setQuantidade(novaQuantidade);

        // Registra movimentação
        MovimentacaoEstoque movimentacao = MovimentacaoEstoque.builder()
                .medicamento(medicamento)
                .tipo(TipoMovimentacao.SAIDA)
                .quantidade(request.getQuantidade())
                .observacao(request.getObservacao())
                .dataMovimentacao(LocalDateTime.now())
                .build();

        return movimentacaoRepository.save(movimentacao);
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoEstoque> buscarMovimentacoesPorMedicamento(Long medicamentoId) {
        // Valida existência do medicamento
        medicamentoService.buscarEntidadePorId(medicamentoId);

        return movimentacaoRepository.findByMedicamentoIdOrderByDataMovimentacaoDesc(medicamentoId);
    }

    @Transactional(readOnly = true)
    public Integer consultarEstoqueAtual(Long medicamentoId) {
        Medicamento medicamento = medicamentoService.buscarEntidadePorId(medicamentoId);
        return medicamento.getQuantidade();
    }

    @Transactional(readOnly = true)
    public List<MovimentacaoEstoque> buscarMovimentacoesRecentes() {
        LocalDateTime umMesAtras = LocalDateTime.now().minusMonths(1);
        return movimentacaoRepository.findByDataMovimentacaoBetween(umMesAtras, LocalDateTime.now());
    }
}