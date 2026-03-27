package com.shopee.view.swing;

import javax.swing.*;
import java.awt.*;
import com.shopee.model.Usuario;
import com.shopee.model.Pedido;
import com.shopee.dao.PedidoDAO;
import java.util.List;
import java.util.stream.Collectors;

public class MeusPedidosFrame extends JFrame {

    private Usuario usuarioLogado;

    public MeusPedidosFrame(Usuario usuario) {
        this.usuarioLogado = usuario;

        setTitle("Shopee - Meus Pedidos");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // topo
        JPanel painelTopo = new JPanel();
        painelTopo.setBackground(Color.ORANGE);
        JLabel titulo = new JLabel("Histórico de Pedidos");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        painelTopo.add(titulo);

        // centro
        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));
        painelCentro.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        try {
            PedidoDAO dao = new PedidoDAO();
            // puxa tudo e filtra só os pedidos do cliente logado na memoria
            List<Pedido> todosPedidos = dao.buscarTodos();
            List<Pedido> meusPedidos = todosPedidos.stream().filter(p -> p.getClienteId() == usuarioLogado.getId()).collect(Collectors.toList());

            if (meusPedidos.isEmpty()) {
                JLabel vazio = new JLabel("Você ainda não fez nenhum pedido.");
                vazio.setAlignmentX(Component.CENTER_ALIGNMENT);
                painelCentro.add(vazio);
            } else {
                for (Pedido p : meusPedidos) {
                    JPanel card = criarCardPedido(p);
                    painelCentro.add(card);
                    painelCentro.add(Box.createVerticalStrut(10));
                }
            }
        } catch (Exception ex) {
            JLabel erro = new JLabel("Erro ao carregar pedidos do banco (DAO incompleto?)");
            erro.setAlignmentX(Component.CENTER_ALIGNMENT);
            painelCentro.add(erro);
        }

        JScrollPane scroll = new JScrollPane(painelCentro);
        scroll.getVerticalScrollBar().setUnitIncrement(16);

        // rodape
        JPanel painelRodape = new JPanel();
        JButton btnVoltar = new JButton("Voltar pra Loja");
        btnVoltar.addActionListener(e -> {
            this.dispose();
            new MainFrame(usuarioLogado).setVisible(true);
        });
        painelRodape.add(btnVoltar);

        add(painelTopo, BorderLayout.NORTH);
        add(scroll, BorderLayout.CENTER);
        add(painelRodape, BorderLayout.SOUTH);
    }

    // cria o card de historico
    private JPanel criarCardPedido(Pedido pedido) {
        JPanel card = new JPanel(new GridLayout(3, 1));
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        card.setMaximumSize(new Dimension(500, 80));
        card.setBackground(Color.WHITE);

        card.add(new JLabel("  Pedido #" + pedido.getId() + " - " + pedido.getDataPedido()));

        JLabel lblStatus = new JLabel("  Status: " + pedido.getStatus());
        lblStatus.setForeground(Color.BLUE);
        card.add(lblStatus);

        JLabel lblValor = new JLabel("  Total: R$ " + pedido.getValorTotal() + " (" + pedido.getMetodoPagamento() + ")");
        lblValor.setForeground(new Color(0, 153, 51));
        card.add(lblValor);

        return card;
    }
}