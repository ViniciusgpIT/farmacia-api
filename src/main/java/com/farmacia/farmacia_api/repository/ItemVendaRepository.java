package com.farmacia.farmacia_api.repository;

import com.farmacia.farmacia_api.model.entity.ItemVenda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemVendaRepository extends JpaRepository<ItemVenda, Long> {
    
    List<ItemVenda> findByVendaId(Long vendaId);
    
    @Query("SELECT SUM(i.quantidade) FROM ItemVenda i WHERE i.medicamento.id = :medicamentoId")
    Integer findQuantidadeTotalVendidaByMedicamentoId(@Param("medicamentoId") Long medicamentoId);
    
    @Query("SELECT i.medicamento.id, SUM(i.quantidade) FROM ItemVenda i GROUP BY i.medicamento.id ORDER BY SUM(i.quantidade) DESC")
    List<Object[]> findMedicamentosMaisVendidos();
}