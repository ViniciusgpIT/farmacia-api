package com.farmacia.farmacia_api.repository;

import com.farmacia.farmacia_api.model.entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    Optional<Cliente> findByCpf(String cpf);

    Optional<Cliente> findByEmail(String email);

    @Query("SELECT c FROM Cliente c WHERE YEAR(c.dataNascimento) = :ano")
    List<Cliente> findByAnoNascimento(@Param("ano") int ano);

    boolean existsByCpf(String cpf);

    boolean existsByCpfAndIdNot(String cpf, Long id);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    @Query("SELECT COUNT(v) FROM Cliente c JOIN c.vendas v WHERE c.id = :clienteId")
    Integer countVendasByClienteId(@Param("clienteId") Long clienteId);
}