package com.shopee.pattern.strategy;

import java.math.BigDecimal;

public class BoletoStrategy implements PagamentoStrategy {

    @Override
    public void processarPagamento(BigDecimal valorTotal) {
        System.out.println("=== Processando Pagamento via Boleto ===");
        System.out.println("Gerando boleto no valor de R$ " + valorTotal);

        // exemplo de código de barras
        String codigoBarras = "00000.00000 00000.000000 00000.000000 1 000000000 0001";
        System.out.println("Código de Barras: " + codigoBarras);

        System.out.println("Boleto gerado com sucesso. Aguardando pagamento do cliente.");
    }
}