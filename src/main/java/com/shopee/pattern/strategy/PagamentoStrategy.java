package com.shopee.pattern.strategy;

import java.math.BigDecimal;

public interface PagamentoStrategy {

    // assinatura do método para processar o pagamento
    void processarPagamento(BigDecimal valorTotal);

}