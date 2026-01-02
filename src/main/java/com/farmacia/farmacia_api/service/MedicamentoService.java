package com.farmacia.farmacia_api.service;

import com.farmacia.farmacia_api.model.dto.request.MedicamentoRequest;
import com.farmacia.farmacia_api.model.dto.response.CategoriaResponse;
import com.farmacia.farmacia_api.model.dto.response.MedicamentoResponse;
import com.farmacia.farmacia_api.model.entity.Medicamento;
import com.farmacia.farmacia_api.model.dto.enums.StatusMedicamento;
import com.farmacia.farmacia_api.repository.MedicamentoRepository;
import com.farmacia.farmacia_api.exception.BusinessException;
import com.farmacia.farmacia_api.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MedicamentoService {

    private final MedicamentoRepository medicamentoRepository;
    private final CategoriaService categoriaService;
    private final ModelMapper modelMapper;
    private final ValidatorService validatorService;

    @Transactional
    public MedicamentoResponse criar(MedicamentoRequest request) {
        validatorService.validarNomeUnicoMedicamento(request.getNome(), null);

        Medicamento medicamento = modelMapper.map(request, Medicamento.class);

        if (request.getCategoriaId() != null) {
            medicamento.setCategoria(categoriaService.buscarEntidadePorId(request.getCategoriaId()));
        }

        medicamento.setStatus(StatusMedicamento.ATIVO);
        medicamento = medicamentoRepository.save(medicamento);

        return toResponse(medicamento);
    }

    @Transactional
    public MedicamentoResponse atualizar(Long id, MedicamentoRequest request) {
        Medicamento medicamento = buscarEntidadePorId(id);

        if (medicamento.isVendido()) {
            throw new BusinessException("Não é possível alterar medicamento que já foi vendido");
        }

        validatorService.validarNomeUnicoMedicamento(request.getNome(), id);

        modelMapper.map(request, medicamento);

        if (request.getCategoriaId() != null) {
            medicamento.setCategoria(categoriaService.buscarEntidadePorId(request.getCategoriaId()));
        }

        medicamento = medicamentoRepository.save(medicamento);
        return toResponse(medicamento);
    }

    @Transactional(readOnly = true)
    public List<MedicamentoResponse> listarTodos() {
        return medicamentoRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public MedicamentoResponse buscarPorId(Long id) {
        Medicamento medicamento = buscarEntidadePorId(id);
        return toResponse(medicamento);
    }

    @Transactional
    public void excluir(Long id) {
        Medicamento medicamento = buscarEntidadePorId(id);

        if (medicamento.isVendido()) {
            throw new BusinessException("Não é possível excluir medicamento que já foi vendido");
        }

        medicamentoRepository.delete(medicamento);
    }

    @Transactional
    public MedicamentoResponse alterarStatus(Long id, StatusMedicamento status) {
        Medicamento medicamento = buscarEntidadePorId(id);
        medicamento.setStatus(status);
        medicamento = medicamentoRepository.save(medicamento);
        return toResponse(medicamento);
    }

    @Transactional(readOnly = true)
    public List<Medicamento> buscarMedicamentosAtivos() {
        return medicamentoRepository.findByStatus(StatusMedicamento.ATIVO);
    }

    @Transactional(readOnly = true)
    public Medicamento buscarEntidadePorId(Long id) {
        return medicamentoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medicamento não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Medicamento> buscarMedicamentosComEstoqueBaixo(Integer limite) {
        return medicamentoRepository.findByQuantidadeLessThanAndStatus(limite, StatusMedicamento.ATIVO);
    }

    @Transactional(readOnly = true)
    public List<Medicamento> buscarMedicamentosComValidadeProxima(Integer dias) {
        LocalDate dataLimite = LocalDate.now().plusDays(dias);
        return medicamentoRepository.findByDataValidadeLessThanEqualAndStatus(dataLimite, StatusMedicamento.ATIVO);
    }

    private MedicamentoResponse toResponse(Medicamento medicamento) {
        MedicamentoResponse response = modelMapper.map(medicamento, MedicamentoResponse.class);
        
        if (medicamento.getCategoria() != null) {
            CategoriaResponse categoriaResponse = modelMapper.map(medicamento.getCategoria(), CategoriaResponse.class);
            // Conta os medicamentos da categoria usando o repositório para evitar problemas de lazy loading
            long quantidade = medicamentoRepository.countByCategoriaId(medicamento.getCategoria().getId());
            categoriaResponse.setQuantidadeMedicamentos((int) quantidade);
            response.setCategoria(categoriaResponse);
        }
        
        return response;
    }
}