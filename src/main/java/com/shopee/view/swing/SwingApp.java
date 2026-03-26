package com.shopee.view.swing;

import javax.swing.SwingUtilities;

public class SwingApp {
    public static void main(String[] args) {
        // inicialização na thread de eventos do Swing (boa prática)
        SwingUtilities.invokeLater(() -> {
            LoginFrame telaLogin = new LoginFrame();
            telaLogin.setVisible(true);
        });
    }
}