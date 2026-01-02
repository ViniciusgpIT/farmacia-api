package com.farmacia.farmacia_api.service;

import com.farmacia.farmacia_api.model.dto.request.ClienteRequest;
import com.farmacia.farmacia_api.model.dto.response.ClienteResponse;
import com.farmacia.farmacia_api.model.entity.Cliente;
import com.farmacia.farmacia_api.repository.ClienteRepository;
import com.farmacia.farmacia_api.exception.BusinessException;
import com.farmacia.farmacia_api.exception.ResourceNotFoundException;
import com.farmacia.farmacia_api.util.CPFValidator;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ModelMapper modelMapper;
    private final CPFValidator cpfValidator;
    private final ValidatorService validatorService;

    @Transactional
    public ClienteResponse criar(ClienteRequest request) {
        validarCliente(request, null);

        Cliente cliente = modelMapper.map(request, Cliente.class);
        cliente = clienteRepository.save(cliente);

        return toResponse(cliente);
    }

    @Transactional
    public ClienteResponse atualizar(Long id, ClienteRequest request) {
        Cliente cliente = buscarEntidadePorId(id);
        validarCliente(request, id);

        modelMapper.map(request, cliente);
        cliente = clienteRepository.save(cliente);

        return toResponse(cliente);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponse> listarTodos() {
        return clienteRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ClienteResponse buscarPorId(Long id) {
        Cliente cliente = buscarEntidadePorId(id);
        return toResponse(cliente);
    }

    @Transactional(readOnly = true)
    public Cliente buscarEntidadePorId(Long id) {
        return clienteRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));
    }

    private void validarCliente(ClienteRequest request, Long id) {
        // Valida CPF
        if (!cpfValidator.isValid(request.getCpf())) {
            throw new BusinessException("CPF inválido");
        }

        // Valida unicidade
        validatorService.validarCpfUnico(request.getCpf(), id);
        validatorService.validarEmailUnico(request.getEmail(), id);

        // Valida idade (apenas para criação, atualização pode manter a data)
        if (id == null) {
            int idade = Period.between(request.getDataNascimento(), LocalDate.now()).getYears();
            if (idade < 13) {
                throw new BusinessException("Cliente deve ter pelo menos 13 anos");
            }
        }
    }

    private ClienteResponse toResponse(Cliente cliente) {
        ClienteResponse response = modelMapper.map(cliente, ClienteResponse.class);
        response.setMaiorDeIdade(cliente.isMaiorDeIdade());

        // Conta o número de vendas do cliente
        Integer totalCompras = clienteRepository.countVendasByClienteId(cliente.getId());
        response.setTotalCompras(totalCompras != null ? totalCompras : 0);

        return response;
    }

    @Transactional(readOnly = true)
    public boolean isMaiorDeIdade(Long clienteId) {
        Cliente cliente = buscarEntidadePorId(clienteId);
        return cliente.isMaiorDeIdade();
    }
}