package com.shopee.pattern.factory;

import com.shopee.pattern.strategy.BoletoStrategy;
import com.shopee.pattern.strategy.CartaoCreditoStrategy;
import com.shopee.pattern.strategy.PagamentoStrategy;
import com.shopee.pattern.strategy.PixStrategy;

public class PagamentoFactory {

    // metodo estático para criar estratégia de pagamento de acordo com o que o cliente seleciona
    public static PagamentoStrategy criarStrategy(int opcaoSelecionada) {
        switch (opcaoSelecionada) {
            case 1:
                return new PixStrategy();
            case 2:
                return new BoletoStrategy();
            case 3:
                return new CartaoCreditoStrategy();
            default:
                // excessão caso inválido
                throw new IllegalArgumentException("Opção de pagamento inválida! Escolha 1, 2 ou 3.");
        }
    }
}