package com.farmacia.farmacia_api.model.dto.response;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class VendaResponse {
    private Long id;
    private ClienteResponse cliente;
    private List<ItemVendaResponse> itens;
    private BigDecimal valorTotal;
    private LocalDateTime dataVenda;
    private Integer quantidadeItens;
}
