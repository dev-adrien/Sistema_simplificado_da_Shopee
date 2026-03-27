package com.shopee.view.swing;

import javax.swing.*;
import java.awt.*;
import com.shopee.model.ItemPedido;
import com.shopee.model.Pedido;
import com.shopee.model.Usuario;
import java.math.BigDecimal;

public class CarrinhoFrame extends JFrame {
    // precisa saber quem ta logado e o que tem no carrinho
    private Usuario usuarioLogado;
    private Pedido carrinhoAtual;

    public CarrinhoFrame(Usuario usuario, Pedido carrinho) {
        this.usuarioLogado = usuario;
        this.carrinhoAtual = carrinho;

        setTitle("Meu Carrinho");
        setSize(500, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // topo
        JPanel painelTopo = new JPanel();
        painelTopo.setBackground(Color.ORANGE);
        JLabel titulo = new JLabel("Seu Carrinho");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        painelTopo.add(titulo);

        // centro (lista os itens)
        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));
        painelCentro.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        BigDecimal total = BigDecimal.ZERO;

        if (carrinhoAtual.getItens().isEmpty()) {
            painelCentro.add(new JLabel("Seu carrinho está vazio!"));
        } else {
            // varre os itens e mostra na tela
            for (ItemPedido item : carrinhoAtual.getItens()) {
                JPanel linha = new JPanel(new FlowLayout(FlowLayout.LEFT));
                linha.add(new JLabel("ID Produto: " + item.getProdutoId() + " | Qtd: " + item.getQuantidade() + " | R$ " + item.getPrecoUnitario()));
                painelCentro.add(linha);

                // vai somando o total
                BigDecimal subtotal = item.getPrecoUnitario().multiply(new BigDecimal(item.getQuantidade()));
                total = total.add(subtotal);
            }
        }

        // salva o total no pedido
        carrinhoAtual.setValorTotal(total);

        // mostra o valor total
        painelCentro.add(Box.createVerticalStrut(20));
        JLabel lblTotal = new JLabel("Total a pagar: R$ " + total);
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setForeground(new Color(0, 153, 51));
        painelCentro.add(lblTotal);

        JScrollPane scroll = new JScrollPane(painelCentro);

        // rodape (botoes)
        JPanel painelRodape = new JPanel();

        JButton btnVoltar = new JButton("Voltar pra Loja");
        btnVoltar.addActionListener(e -> {
            this.dispose(); // mata o carrinho
            new MainFrame(usuarioLogado).setVisible(true); // volta pra loja
        });

        JButton btnPagamento = new JButton("Ir para Pagamento");
        btnPagamento.setBackground(Color.ORANGE);
        btnPagamento.setForeground(Color.WHITE);

        // se tiver vazio, desativa o botao de pagar
        if (carrinhoAtual.getItens().isEmpty()) {
            btnPagamento.setEnabled(false);
        }

        // acao de ir pro pagamento
        btnPagamento.addActionListener(e -> {
            this.dispose();
            new PagamentoFrame(usuarioLogado, carrinhoAtual).setVisible(true);
        });

        painelRodape.add(btnVoltar);
        painelRodape.add(btnPagamento);

        add(painelTopo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(painelRodape, BorderLayout.SOUTH);
    }
}