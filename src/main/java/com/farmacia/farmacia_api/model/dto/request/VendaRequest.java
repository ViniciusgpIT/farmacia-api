package com.farmacia.farmacia_api.model.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.List;

@Data
public class VendaRequest {
    
    @NotNull(message = "Cliente é obrigatório")
    private Long clienteId;
    
    @Valid
    @NotNull(message = "Itens são obrigatórios")
    @Size(min = 1, message = "Venda deve conter pelo menos um item")
    private List<ItemVendaRequest> itens;
}