package com.shopee.pattern.observer;

import com.shopee.model.Pedido;

public interface Observador {
    void atualizar(Pedido pedido, String evento);
}