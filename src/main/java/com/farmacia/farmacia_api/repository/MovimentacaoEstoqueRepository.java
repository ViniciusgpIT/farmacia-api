package com.farmacia.farmacia_api.repository;

import com.farmacia.farmacia_api.model.entity.MovimentacaoEstoque;
import com.farmacia.farmacia_api.model.dto.enums.TipoMovimentacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {
    
    List<MovimentacaoEstoque> findByMedicamentoId(Long medicamentoId);
    
    List<MovimentacaoEstoque> findByMedicamentoIdAndTipo(Long medicamentoId, TipoMovimentacao tipo);
    
    List<MovimentacaoEstoque> findByDataMovimentacaoBetween(LocalDateTime inicio, LocalDateTime fim);
    
    List<MovimentacaoEstoque> findByMedicamentoIdOrderByDataMovimentacaoDesc(Long medicamentoId);
}