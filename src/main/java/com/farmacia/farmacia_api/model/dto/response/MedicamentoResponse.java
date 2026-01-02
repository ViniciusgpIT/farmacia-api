package com.farmacia.farmacia_api.model.dto.response;

import com.farmacia.farmacia_api.model.dto.enums.StatusMedicamento;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class MedicamentoResponse {
    private Long id;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private Integer quantidade;
    private LocalDate dataValidade;
    private StatusMedicamento status;
    private CategoriaResponse categoria;
}