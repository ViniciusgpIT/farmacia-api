package com.farmacia.farmacia_api.service;

import com.farmacia.farmacia_api.repository.CategoriaRepository;
import com.farmacia.farmacia_api.repository.ClienteRepository;
import com.farmacia.farmacia_api.repository.MedicamentoRepository;
import com.farmacia.farmacia_api.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidatorService {

    private final MedicamentoRepository medicamentoRepository;
    private final CategoriaRepository categoriaRepository;
    private final ClienteRepository clienteRepository;

    public void validarNomeUnicoMedicamento(String nome, Long id) {
        boolean existe;

        if (id != null) {
            existe = medicamentoRepository.existsByNomeAndIdNot(nome, id);
        } else {
            existe = medicamentoRepository.existsByNome(nome);
        }

        if (existe) {
            throw new BusinessException("Já existe um medicamento com o nome: " + nome);
        }
    }

    public void validarNomeUnicoCategoria(String nome, Long id) {
        boolean existe;

        if (id != null) {
            existe = categoriaRepository.existsByNomeAndIdNot(nome, id);
        } else {
            existe = categoriaRepository.existsByNome(nome);
        }

        if (existe) {
            throw new BusinessException("Já existe uma categoria com o nome: " + nome);
        }
    }

    public void validarCpfUnico(String cpf, Long id) {
        boolean existe;

        if (id != null) {
            existe = clienteRepository.existsByCpfAndIdNot(cpf, id);
        } else {
            existe = clienteRepository.existsByCpf(cpf);
        }

        if (existe) {
            throw new BusinessException("Já existe um cliente com o CPF: " + cpf);
        }
    }

    public void validarEmailUnico(String email, Long id) {
        boolean existe;

        if (id != null) {
            existe = clienteRepository.existsByEmailAndIdNot(email, id);
        } else {
            existe = clienteRepository.existsByEmail(email);
        }

        if (existe) {
            throw new BusinessException("Já existe um cliente com o e-mail: " + email);
        }
    }

    public void validarMedicamentoNaoVendido(Long medicamentoId) {
        // Esta validação seria usada antes de excluir um medicamento
        // Verifica se o medicamento já foi vendido em alguma venda
        // (Implementação depende da lógica de negócio específica)
    }
}