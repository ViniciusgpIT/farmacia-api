package com.farmacia.farmacia_api.model.entity;

import com.farmacia.farmacia_api.model.dto.enums.StatusMedicamento;
import com.fasterxml.jackson.annotation.JsonBackReference;  // ← ADICIONE ESTE IMPORT
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "medicamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Medicamento {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank
    @Column(unique = true, nullable = false)
    private String nome;
    
    private String descricao;
    
    @NotNull
    @DecimalMin(value = "0.01")
    @Column(precision = 10, scale = 2)
    private BigDecimal preco;
    
    @NotNull
    @Min(0)
    private Integer quantidade;
    
    @NotNull
    @Future
    private LocalDate dataValidade;
    
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StatusMedicamento status = StatusMedicamento.ATIVO;
    
    @ManyToOne
    @JoinColumn(name = "categoria_id")
    @JsonBackReference  // ← ADICIONE ESTA LINHA
    private Categoria categoria;
    
    @Builder.Default
    private boolean vendido = false;
}