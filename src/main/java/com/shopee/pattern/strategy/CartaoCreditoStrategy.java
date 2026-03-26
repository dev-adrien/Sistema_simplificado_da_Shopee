package com.shopee.pattern.strategy;

import java.math.BigDecimal;

public class CartaoCreditoStrategy implements PagamentoStrategy {

    @Override
    public void processarPagamento(BigDecimal valorTotal) {
        System.out.println("=== Processando Pagamento via Cartão de Crédito ===");
        System.out.println("Processando pagamento no cartão para o valor de R$ " + valorTotal + "...");

        // Lógica de processamento do pagamento
        System.out.println("Verificando limite disponível...");
        System.out.println("Transação autorizada.");
        System.out.println("Pagamento no Cartão de Crédito aprovado com sucesso!");
    }
}