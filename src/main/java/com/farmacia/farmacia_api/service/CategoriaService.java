package com.farmacia.farmacia_api.service;

import com.farmacia.farmacia_api.model.dto.request.CategoriaRequest;
import com.farmacia.farmacia_api.model.dto.response.CategoriaResponse;
import com.farmacia.farmacia_api.model.entity.Categoria;
import com.farmacia.farmacia_api.repository.CategoriaRepository;
import com.farmacia.farmacia_api.exception.BusinessException;
import com.farmacia.farmacia_api.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;
    private final ModelMapper modelMapper;
    private final ValidatorService validatorService;

    @Transactional
    public CategoriaResponse criar(CategoriaRequest request) {
        validatorService.validarNomeUnicoCategoria(request.getNome(), null);

        Categoria categoria = modelMapper.map(request, Categoria.class);
        categoria = categoriaRepository.save(categoria);

        return toResponse(categoria);
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listarTodos() {
        return categoriaRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscarPorId(Long id) {
        Categoria categoria = buscarEntidadePorId(id);
        return toResponse(categoria);
    }

    @Transactional(readOnly = true)
    public Categoria buscarEntidadePorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada com ID: " + id));
    }

    @Transactional
    public void excluir(Long id) {
        Categoria categoria = buscarEntidadePorId(id);

        if (!categoria.getMedicamentos().isEmpty()) {
            throw new BusinessException("Não é possível excluir categoria vinculada a medicamentos");
        }

        categoriaRepository.delete(categoria);
    }

    @Transactional(readOnly = true)
    public boolean existeMedicamentosNaCategoria(Long categoriaId) {
        Categoria categoria = buscarEntidadePorId(categoriaId);
        return !categoria.getMedicamentos().isEmpty();
    }

    private CategoriaResponse toResponse(Categoria categoria) {
        CategoriaResponse response = modelMapper.map(categoria, CategoriaResponse.class);
        response.setQuantidadeMedicamentos(categoria.getMedicamentos().size());
        return response;
    }
}