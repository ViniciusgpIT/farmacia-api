package com.farmacia.farmacia_api.repository;

import com.farmacia.farmacia_api.model.entity.Venda;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface VendaRepository extends JpaRepository<Venda, Long> {
    
    List<Venda> findByClienteId(Long clienteId);
    
    List<Venda> findByDataVendaBetween(LocalDateTime inicio, LocalDateTime fim);
    
    // Método para buscar com todos os relacionamentos
    @Query("SELECT DISTINCT v FROM Venda v " +
           "LEFT JOIN FETCH v.cliente " +
           "LEFT JOIN FETCH v.itens i " +
           "LEFT JOIN FETCH i.medicamento " +
           "WHERE v.id = :id")
    Optional<Venda> findByIdWithDetails(@Param("id") Long id);
    
    // Método para listar todas com relacionamentos
    @Query("SELECT DISTINCT v FROM Venda v " +
           "LEFT JOIN FETCH v.cliente " +
           "LEFT JOIN FETCH v.itens")
    List<Venda> findAllWithDetails();
    
    @Query("SELECT COUNT(v) FROM Venda v WHERE DATE(v.dataVenda) = :data")
    Integer countVendasByData(@Param("data") LocalDate data);

    // NOVO MÉTODO: Contar vendas por cliente
    @Query("SELECT COUNT(v) FROM Venda v WHERE v.cliente.id = :clienteId")
    Integer countByClienteId(@Param("clienteId") Long clienteId);
}