package com.farmacia.farmacia_api.model.dto.response;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class ItemVendaResponse {
    private Long id; // Adicione este campo se quiser o ID do ItemVenda
    private Long medicamentoId;
    private String medicamentoNome;
    private Integer quantidade;
    private BigDecimal precoUnitario;
    private BigDecimal subtotal;
}