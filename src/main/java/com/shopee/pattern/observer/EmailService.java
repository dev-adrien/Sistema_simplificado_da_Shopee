package com.shopee.pattern.observer;

import com.shopee.model.Pedido;

import java.util.UUID;

public class EmailService implements Observador {
    @Override
    public void atualizar(Pedido pedido, String evento) {
        switch (evento) {
            case "PEDIDO_CRIADO":
                enviarEmailConfirmacao(pedido);
                break;
            case "PAGAMENTO_APROVADO":
                enviarEmailPagamento(pedido);
                break;
            case "PEDIDO_ENVIADO":
                enviarEmailRastreio(pedido);
                break;
        }
    }

    private void enviarEmailConfirmacao(Pedido p) {
        System.out.println(" Email: Pedido " + p.getId() + " confirmado!");
    }

    private void enviarEmailPagamento(Pedido p) {
        System.out.println(" Email: Pagamento aprovado para pedido " + p.getId());
    }

    private void enviarEmailRastreio(Pedido p) {
        System.out.println(" Email: Pedido " + p.getId() + " enviado - Código: BR" +  UUID.randomUUID().toString().substring(0, 8).toUpperCase());
    }
}