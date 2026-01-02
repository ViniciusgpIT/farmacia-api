package com.farmacia.farmacia_api.model.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class AlertaResponse {
    private Long medicamentoId;
    private String medicamentoNome;
    private String tipoAlerta; // "ESTOQUE_BAIXO" ou "VALIDADE_PROXIMA"
    private String mensagem;
    private Integer quantidadeAtual;
    private LocalDate dataValidade;
    private Integer diasParaVencimento;
}