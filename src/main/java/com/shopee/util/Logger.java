package com.shopee.util;

public class Logger {
    private static Logger instance;

    // contrutor privado para que não instancie o logger fora da classe (boa prática)
    private Logger() {
    }

    // cria porta de acesso pra usar apenas uma instancia no logger (boa prática)
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    // gera os logs no console segundo as normas padrões
    public void log(String mensagem, String nivel) {
        // info, warning, error
        System.out.println("[" + nivel + "] " + mensagem);


    }
}