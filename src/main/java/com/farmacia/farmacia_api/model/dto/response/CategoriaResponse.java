package com.farmacia.farmacia_api.model.dto.response;

import lombok.Data;

@Data
public class CategoriaResponse {
    private Long id;
    private String nome;
    private String descricao;
    private Integer quantidadeMedicamentos;
}