package com.farmacia.farmacia_api.model.dto.response;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ClienteResponse {
    private Long id;
    private String nome;
    private String cpf;
    private String email;
    private LocalDate dataNascimento;
    private boolean maiorDeIdade;
    private Integer totalCompras;
}