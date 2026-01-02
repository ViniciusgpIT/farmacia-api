package com.farmacia.farmacia_api.util;

import org.springframework.stereotype.Component;

@Component
public class CPFValidator {
    
    private static final int[] PESO_CPF = {11, 10, 9, 8, 7, 6, 5, 4, 3, 2};
    
    public boolean isValid(String cpf) {
        if (cpf == null || cpf.isEmpty()) {
            return false;
        }
        
        // Remove caracteres não numéricos
        cpf = cpf.replaceAll("\\D", "");
        
        // Verifica se tem 11 dígitos
        if (cpf.length() != 11) {
            return false;
        }
        
        // Verifica se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        // Calcula o primeiro dígito verificador
        int digito1 = calcularDigito(cpf.substring(0, 9));
        
        // Calcula o segundo dígito verificador
        int digito2 = calcularDigito(cpf.substring(0, 9) + digito1);
        
        // Verifica se os dígitos calculados conferem com os dígitos informados
        return cpf.equals(cpf.substring(0, 9) + digito1 + digito2);
    }
    
    private int calcularDigito(String str) {
        int soma = 0;
        
        for (int indice = str.length() - 1, digito; indice >= 0; indice--) {
            digito = Integer.parseInt(str.substring(indice, indice + 1));
            soma += digito * PESO_CPF[PESO_CPF.length - str.length() + indice];
        }
        
        soma = 11 - soma % 11;
        return soma > 9 ? 0 : soma;
    }
    
    public String formatar(String cpf) {
        if (!isValid(cpf)) {
            return cpf;
        }
        
        cpf = cpf.replaceAll("\\D", "");
        return cpf.replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4");
    }
}