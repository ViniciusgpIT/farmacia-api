package com.farmacia.farmacia_api.util;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@Component
public class DataUtils {
    
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    
    public String formatarData(LocalDate data) {
        return data != null ? data.format(DATE_FORMATTER) : null;
    }
    
    public String formatarDataHora(LocalDateTime dataHora) {
        return dataHora != null ? dataHora.format(DATE_TIME_FORMATTER) : null;
    }
    
    public LocalDate parseData(String dataStr) {
        return LocalDate.parse(dataStr, DATE_FORMATTER);
    }
    
    public LocalDateTime parseDataHora(String dataHoraStr) {
        return LocalDateTime.parse(dataHoraStr, DATE_TIME_FORMATTER);
    }
    
    public boolean isDataFutura(LocalDate data) {
        return data != null && data.isAfter(LocalDate.now());
    }
    
    public boolean isDataPassada(LocalDate data) {
        return data != null && data.isBefore(LocalDate.now());
    }
    
    public boolean isDataHoje(LocalDate data) {
        return data != null && data.isEqual(LocalDate.now());
    }
    
    public long calcularDiasEntre(LocalDate dataInicio, LocalDate dataFim) {
        return ChronoUnit.DAYS.between(dataInicio, dataFim);
    }
    
    public boolean isDataNoPeriodo(LocalDate data, LocalDate inicio, LocalDate fim) {
        return data != null && 
               (data.isEqual(inicio) || data.isAfter(inicio)) && 
               (data.isEqual(fim) || data.isBefore(fim));
    }
}