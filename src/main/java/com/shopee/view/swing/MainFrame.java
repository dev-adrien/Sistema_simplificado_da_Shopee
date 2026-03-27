package com.shopee.view.swing;

import com.shopee.model.Usuario;
import com.shopee.model.Vendedor;
import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {

    // guarda user
    private Usuario usuarioLogado;

    // constructor para gerar MainFrame com user
    public MainFrame(Usuario usuario) {
        this.usuarioLogado = usuario;

        // Configurações básicas da janela
        setTitle("Shopee - Loja");
        setSize(500, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // cabeçalho
        JPanel painelTopo = new JPanel();
        painelTopo.setBackground(Color.ORANGE);

        // Centro
        JPanel painelCentro = new JPanel(new GridLayout(3, 1, 10, 10));
        painelCentro.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // mainFrame se o user for vendedor ou cliente
        if (usuarioLogado instanceof Vendedor) {
            JButton btnCadastrarProduto = new JButton("Cadastrar Novo Produto");
            JButton btnMeusProdutos = new JButton("Listar Meus Produtos");

            // ação de cadastrar produto
            btnCadastrarProduto.addActionListener(e -> {
                JFrame telaCadastro = new JFrame("Novo Produto");
                telaCadastro.setSize(400, 300);
                telaCadastro.setLocationRelativeTo(this);
                telaCadastro.add(new ProdutoPanel((Vendedor) usuarioLogado));
                telaCadastro.setVisible(true);
            });

            painelCentro.add(btnCadastrarProduto);
            painelCentro.add(btnMeusProdutos);
        } else {
            JButton btnVitrine = new JButton("Catálogo");
            JButton btnMeusPedidos = new JButton("Meus pedidos");

            // ação temporária
            btnVitrine.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abrindo os produtos..."));
            painelCentro.add(btnVitrine);
            painelCentro.add(btnMeusPedidos);
        }

        // rodapé
        JPanel painelRodape = new JPanel();
        JButton btnSair = new JButton("Logout");
        btnSair.addActionListener(e -> {
            this.dispose(); // desliga a mainframe
            new LoginFrame().setVisible(true);
        });
        painelRodape.add(btnSair);

        add(painelTopo, BorderLayout.NORTH);
        add(painelCentro, BorderLayout.CENTER);
        add(painelRodape, BorderLayout.SOUTH);
    }
}