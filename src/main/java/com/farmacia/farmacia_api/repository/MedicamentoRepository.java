package com.farmacia.farmacia_api.repository;

import com.farmacia.farmacia_api.model.entity.*;
import com.farmacia.farmacia_api.model.dto.enums.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {

    Optional<Medicamento> findByNome(String nome);

    List<Medicamento> findByStatus(StatusMedicamento status);

    List<Medicamento> findByQuantidadeLessThan(Integer quantidade);

    List<Medicamento> findByQuantidadeLessThanAndStatus(Integer quantidade, StatusMedicamento status);

    List<Medicamento> findByDataValidadeBetween(LocalDate startDate, LocalDate endDate);

    @Query("SELECT m FROM Medicamento m WHERE m.dataValidade <= :dataLimite AND m.status = :status")
    List<Medicamento> findByDataValidadeLessThanEqualAndStatus(
            @Param("dataLimite") LocalDate dataLimite,
            @Param("status") StatusMedicamento status);

    boolean existsByNome(String nome);

    boolean existsByNomeAndIdNot(String nome, Long id);

    List<Medicamento> findByCategoriaId(Long categoriaId);

    long countByCategoriaId(Long categoriaId);
}