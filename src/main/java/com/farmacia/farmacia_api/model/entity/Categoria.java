package com.farmacia.farmacia_api.model.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference; // ← ADICIONE ESTE IMPORT
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "categorias")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true, nullable = false)
    private String nome;

    private String descricao;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    @JsonManagedReference // ← ADICIONE ESTA LINHA
    @Builder.Default
    private List<Medicamento> medicamentos = new ArrayList<>();
}