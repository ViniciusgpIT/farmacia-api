package com.farmacia.farmacia_api.service;

import com.farmacia.farmacia_api.model.dto.request.ItemVendaRequest;
import com.farmacia.farmacia_api.model.dto.request.VendaRequest;
import com.farmacia.farmacia_api.model.dto.response.ClienteResponse;
import com.farmacia.farmacia_api.model.dto.response.ItemVendaResponse;
import com.farmacia.farmacia_api.model.dto.response.VendaResponse;
import com.farmacia.farmacia_api.model.entity.*;
import com.farmacia.farmacia_api.model.dto.enums.StatusMedicamento;
import com.farmacia.farmacia_api.repository.VendaRepository;
import com.farmacia.farmacia_api.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendaService {

    private final VendaRepository vendaRepository;
    private final ClienteService clienteService;
    private final MedicamentoService medicamentoService;

    @Transactional
    public VendaResponse criar(VendaRequest request) {
        // Valida cliente
        Cliente cliente = clienteService.buscarEntidadePorId(request.getClienteId());

        if (!cliente.isMaiorDeIdade()) {
            throw new BusinessException("Cliente deve ser maior de 18 anos para realizar compras");
        }

        // Cria venda
        Venda venda = new Venda();
        venda.setCliente(cliente);
        venda.setDataVenda(LocalDateTime.now());

        List<ItemVenda> itens = new ArrayList<>();
        BigDecimal valorTotal = BigDecimal.ZERO;

        for (ItemVendaRequest itemRequest : request.getItens()) {
            ItemVenda item = criarItemVenda(itemRequest, venda);
            itens.add(item);
            valorTotal = valorTotal.add(item.getSubtotal());
        }

        venda.setItens(itens);
        venda.setValorTotal(valorTotal);

        // Validações finais
        validarVenda(venda);

        // Atualiza estoque e marca como vendido
        atualizarEstoqueEVendas(venda);

        // Salva a venda (cascade salvará os itens)
        venda = vendaRepository.save(venda);
        return toResponse(venda);
    }

    private ItemVenda criarItemVenda(ItemVendaRequest itemRequest, Venda venda) {
        Medicamento medicamento = medicamentoService.buscarEntidadePorId(itemRequest.getMedicamentoId());

        // Validações do medicamento
        validarMedicamentoParaVenda(medicamento, itemRequest.getQuantidade());

        ItemVenda item = ItemVenda.builder()
                .venda(venda)
                .medicamento(medicamento)
                .quantidade(itemRequest.getQuantidade())
                .precoUnitario(medicamento.getPreco()) // Usa preço atual do medicamento
                .build();

        // Calcular subtotal manualmente
        if (item.getPrecoUnitario() != null && item.getQuantidade() != null) {
            item.setSubtotal(item.getPrecoUnitario()
                    .multiply(BigDecimal.valueOf(item.getQuantidade())));
        }

        return item;
    }

    private void validarMedicamentoParaVenda(Medicamento medicamento, Integer quantidade) {
        if (medicamento.getStatus() == StatusMedicamento.INATIVO) {
            throw new BusinessException("Medicamento inativo não pode ser vendido: " + medicamento.getNome());
        }

        if (medicamento.getDataValidade().isBefore(LocalDate.now())) {
            throw new BusinessException("Medicamento vencido não pode ser vendido: " + medicamento.getNome());
        }

        if (medicamento.getQuantidade() < quantidade) {
            throw new BusinessException(
                    String.format("Estoque insuficiente para %s. Disponível: %d, Solicitado: %d",
                            medicamento.getNome(), medicamento.getQuantidade(), quantidade));
        }
    }

    private void validarVenda(Venda venda) {
        if (venda.getItens().isEmpty()) {
            throw new BusinessException("Venda deve conter pelo menos um item");
        }

        // Valida se algum preço unitário foi alterado (não deve ser diferente do preço
        // atual)
        for (ItemVenda item : venda.getItens()) {
            if (!item.getPrecoUnitario().equals(item.getMedicamento().getPreco())) {
                throw new BusinessException(
                        "Preço unitário não pode ser diferente do preço atual do medicamento: " +
                                item.getMedicamento().getNome());
            }
        }
    }

    private void atualizarEstoqueEVendas(Venda venda) {
        for (ItemVenda item : venda.getItens()) {
            Medicamento medicamento = item.getMedicamento();

            // Atualiza estoque
            medicamento.setQuantidade(medicamento.getQuantidade() - item.getQuantidade());

            // Marca como vendido (para controle de soft delete)
            medicamento.setVendido(true);
        }
    }

    @Transactional(readOnly = true)
    public List<VendaResponse> listarTodas() {
        return vendaRepository.findAllWithDetails().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public VendaResponse buscarPorId(Long id) {
        Venda venda = vendaRepository.findByIdWithDetails(id)
                .orElseThrow(() -> new BusinessException("Venda não encontrada com ID: " + id));
        return toResponse(venda);
    }

    @Transactional(readOnly = true)
    public List<VendaResponse> buscarPorCliente(Long clienteId) {
        List<Venda> vendas = vendaRepository.findByClienteId(clienteId);
        return vendas.stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<VendaResponse> buscarVendasDoDia() {
        LocalDateTime inicioDia = LocalDate.now().atStartOfDay();
        LocalDateTime fimDia = LocalDate.now().atTime(23, 59, 59);

        return vendaRepository.findByDataVendaBetween(inicioDia, fimDia).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private VendaResponse toResponse(Venda venda) {
        VendaResponse response = new VendaResponse();
        response.setId(venda.getId());
        response.setValorTotal(venda.getValorTotal());
        response.setDataVenda(venda.getDataVenda());
        response.setQuantidadeItens(venda.getItens().size());

        // Mapear cliente
        if (venda.getCliente() != null) {
            ClienteResponse clienteResponse = new ClienteResponse();
            clienteResponse.setId(venda.getCliente().getId());
            clienteResponse.setNome(venda.getCliente().getNome());
            clienteResponse.setCpf(venda.getCliente().getCpf());
            clienteResponse.setEmail(venda.getCliente().getEmail());
            clienteResponse.setDataNascimento(venda.getCliente().getDataNascimento());
            clienteResponse.setMaiorDeIdade(venda.getCliente().isMaiorDeIdade());

            // Calcular total de compras
            Integer totalCompras = vendaRepository.countByClienteId(venda.getCliente().getId());
            clienteResponse.setTotalCompras(totalCompras);

            response.setCliente(clienteResponse);
        }

        // Mapear itens
        if (venda.getItens() != null && !venda.getItens().isEmpty()) {
            List<ItemVendaResponse> itensResponse = venda.getItens().stream()
                    .map(item -> {
                        ItemVendaResponse itemResponse = new ItemVendaResponse();
                        itemResponse.setId(item.getId()); // ← ADICIONE ESTA LINHA
                        itemResponse.setMedicamentoId(item.getMedicamento().getId());
                        itemResponse.setMedicamentoNome(item.getMedicamento().getNome());
                        itemResponse.setQuantidade(item.getQuantidade());
                        itemResponse.setPrecoUnitario(item.getPrecoUnitario());
                        itemResponse.setSubtotal(item.getSubtotal());
                        return itemResponse;
                    })
                    .collect(Collectors.toList());
            response.setItens(itensResponse);
        }

        return response;
    }
}