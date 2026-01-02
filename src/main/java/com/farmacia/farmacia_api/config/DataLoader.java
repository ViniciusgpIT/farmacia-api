package com.farmacia.farmacia_api.config;

import com.farmacia.farmacia_api.model.entity.Categoria;
import com.farmacia.farmacia_api.repository.CategoriaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class DataLoader {
    
    @Bean
    public CommandLineRunner loadData(CategoriaRepository categoriaRepository) {
        return args -> {
            // Verifica se já existem dados
            if (categoriaRepository.count() == 0) {
                System.out.println("✅ Criando dados iniciais...");
                
                Categoria categoria = new Categoria();
                categoria.setNome("Analgesicos");
                categoria.setDescricao("Medicamentos para dor");
                categoriaRepository.save(categoria);
                
                System.out.println("✅ Dados iniciais criados!");
            } else {
                System.out.println("📊 Banco já contém " + categoriaRepository.count() + " categorias");
            }
        };
    }
}