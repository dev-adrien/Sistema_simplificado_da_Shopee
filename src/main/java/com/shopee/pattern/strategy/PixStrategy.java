package com.shopee.pattern.strategy;

import java.math.BigDecimal;

public class PixStrategy implements PagamentoStrategy {
    // implementa o método da interface
    @Override
    public void processarPagamento(BigDecimal valorTotal) {
        System.out.println("=== Processando Pagamento via PIX ===");

        System.out.println("Gerando código PIX Copia e Cola para o valor de R$ " + valorTotal);
        String codigoPix = "XXXXXXXXXXXX99999999999.gov.br/1234567890-empresa-x_pagamento"; // exemplo de código copia e cola ou chave pix
        System.out.println("Chave Copia e Cola: " + codigoPix);

        System.out.println("Aguardando confirmação imediata do banco...");
        System.out.println("Pagamento PIX aprovado com sucesso!");
    }
}