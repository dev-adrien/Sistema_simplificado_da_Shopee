package com.shopee.pattern.observer;

import com.shopee.model.Pedido;
import com.shopee.util.Logger;

import java.util.ArrayList;
import java.util.List;

public class NotificadorPedido {
    private List<Observador> observadores = new ArrayList<>();
    private static NotificadorPedido instance;

    private NotificadorPedido() {
    }

    public static NotificadorPedido getInstance() {
        if (instance == null) {
            instance = new NotificadorPedido();
        }
        return instance;
    }

    public void adicionarObservador(Observador obs) {
        observadores.add(obs);
    }

    public void removerObservador(Observador obs) {
        observadores.remove(obs);
    }

    public void notificar(Pedido pedido, String evento) {
        Logger.getInstance().log("Notificando observadores: " + evento, "INFO");
        for (Observador obs : observadores) {
            obs.atualizar(pedido, evento);
        }
    }
}