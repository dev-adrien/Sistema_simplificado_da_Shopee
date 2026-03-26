package com.shopee.pattern.observer;

import com.shopee.model.Pedido;

public class SMSNotification implements Observador {
    @Override
    public void atualizar(Pedido pedido, String evento) {
        if (evento.equals("PEDIDO_ENVIADO")) {
            System.out.println("SMS: Seu pedido " + pedido.getId() + " saiu para entrega!");
        }
    }
}