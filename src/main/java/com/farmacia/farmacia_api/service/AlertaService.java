package com.farmacia.farmacia_api.service;

import com.farmacia.farmacia_api.model.dto.response.AlertaResponse;
import com.farmacia.farmacia_api.model.entity.Medicamento;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AlertaService {

    private final MedicamentoService medicamentoService;

    @Value("${app.alerta.estoque-baixo:10}")
    private Integer limiteEstoqueBaixo;

    @Value("${app.alerta.validade-dias:30}")
    private Integer diasValidadeProxima;

    @Transactional(readOnly = true)
    public List<AlertaResponse> gerarAlertasEstoqueBaixo() {
        List<Medicamento> medicamentos = medicamentoService
                .buscarMedicamentosComEstoqueBaixo(limiteEstoqueBaixo);

        List<AlertaResponse> alertas = new ArrayList<>();

        for (Medicamento medicamento : medicamentos) {
            AlertaResponse alerta = new AlertaResponse();
            alerta.setMedicamentoId(medicamento.getId());
            alerta.setMedicamentoNome(medicamento.getNome());
            alerta.setTipoAlerta("ESTOQUE_BAIXO");
            alerta.setMensagem(String.format(
                    "Estoque baixo: apenas %d unidades disponíveis",
                    medicamento.getQuantidade()));
            alerta.setQuantidadeAtual(medicamento.getQuantidade());
            alerta.setDataValidade(medicamento.getDataValidade());

            long diasParaVencer = ChronoUnit.DAYS.between(LocalDate.now(), medicamento.getDataValidade());
            alerta.setDiasParaVencimento((int) diasParaVencer);

            alertas.add(alerta);
        }

        return alertas;
    }

    @Transactional(readOnly = true)
    public List<AlertaResponse> gerarAlertasValidadeProxima() {
        List<Medicamento> medicamentos = medicamentoService
                .buscarMedicamentosComValidadeProxima(diasValidadeProxima);

        List<AlertaResponse> alertas = new ArrayList<>();

        for (Medicamento medicamento : medicamentos) {
            AlertaResponse alerta = new AlertaResponse();
            alerta.setMedicamentoId(medicamento.getId());
            alerta.setMedicamentoNome(medicamento.getNome());
            alerta.setTipoAlerta("VALIDADE_PROXIMA");

            long diasParaVencer = ChronoUnit.DAYS.between(LocalDate.now(), medicamento.getDataValidade());
            alerta.setDiasParaVencimento((int) diasParaVencer);

            if (diasParaVencer <= 0) {
                alerta.setMensagem("MEDICAMENTO VENCIDO! Descarte imediato necessário");
            } else if (diasParaVencer <= 7) {
                alerta.setMensagem(String.format(
                        "VALIDADE CRÍTICA: Vence em %d dias", diasParaVencer));
            } else {
                alerta.setMensagem(String.format(
                        "Validade próxima: vence em %d dias", diasParaVencer));
            }

            alerta.setQuantidadeAtual(medicamento.getQuantidade());
            alerta.setDataValidade(medicamento.getDataValidade());

            alertas.add(alerta);
        }

        return alertas;
    }

    @Transactional(readOnly = true)
    public List<AlertaResponse> gerarTodosAlertas() {
        List<AlertaResponse> todosAlertas = new ArrayList<>();
        todosAlertas.addAll(gerarAlertasEstoqueBaixo());
        todosAlertas.addAll(gerarAlertasValidadeProxima());
        return todosAlertas;
    }

    public void configurarLimiteEstoqueBaixo(Integer novoLimite) {
        if (novoLimite < 0) {
            throw new IllegalArgumentException("Limite de estoque não pode ser negativo");
        }
        this.limiteEstoqueBaixo = novoLimite;
    }

    public void configurarDiasValidadeProxima(Integer novosDias) {
        if (novosDias < 1) {
            throw new IllegalArgumentException("Dias para validade devem ser pelo menos 1");
        }
        this.diasValidadeProxima = novosDias;
    }
}