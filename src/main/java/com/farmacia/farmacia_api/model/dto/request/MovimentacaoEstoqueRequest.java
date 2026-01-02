package com.farmacia.farmacia_api.model.dto.request;

import com.farmacia.farmacia_api.model.dto.enums.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class MovimentacaoEstoqueRequest {

    @NotNull(message = "Medicamento é obrigatório")
    private Long medicamentoId;

    @NotNull(message = "Tipo é obrigatório")
    private TipoMovimentacao tipo;

    @NotNull(message = "Quantidade é obrigatória")
    private Integer quantidade;

    private String motivo;
    private String observacao;
}