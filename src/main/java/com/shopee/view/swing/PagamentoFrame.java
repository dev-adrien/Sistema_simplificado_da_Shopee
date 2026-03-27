package com.shopee.view.swing;

import javax.swing.*;
import java.awt.*;
import com.shopee.model.Pedido;
import com.shopee.model.Usuario;
import com.shopee.service.PedidoService;
import java.time.LocalDateTime;

public class PagamentoFrame extends JFrame {

    // precisa saber quem ta comprando e o carrinho
    private Usuario usuarioLogado;
    private Pedido pedidoAtual;

    private JTextField campoEndereco;
    private JComboBox<String> comboPagamento;

    public PagamentoFrame(Usuario usuario, Pedido pedido) {
        this.usuarioLogado = usuario;
        this.pedidoAtual = pedido;

        // configs basicas
        setTitle("Shopee - Pagamento");
        setSize(400, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // topo laranja
        JPanel painelTopo = new JPanel();
        painelTopo.setBackground(Color.ORANGE);
        JLabel titulo = new JLabel("Finalizar compra");
        titulo.setForeground(Color.WHITE);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));
        painelTopo.add(titulo);

        // form no centro - MUDAMOS PARA BOXLAYOUT PRA NÃO ESTICAR FEIO
        JPanel painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));
        painelCentro.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // mostra o total
        JLabel lblTotal = new JLabel("Valor Total: R$ " + pedido.getValorTotal());
        lblTotal.setFont(new Font("Arial", Font.BOLD, 16));
        lblTotal.setForeground(new Color(0, 153, 51));
        painelCentro.add(lblTotal);
        painelCentro.add(Box.createVerticalStrut(20)); // espaco

        // campo de endereco
        painelCentro.add(new JLabel("Endereço de entrega:"));
        campoEndereco = new JTextField();
        campoEndereco.setMaximumSize(new Dimension(400, 30)); // trava a altura
        painelCentro.add(campoEndereco);
        painelCentro.add(Box.createVerticalStrut(15)); // espaco

        // caixinha de pagamento (1-Pix, 2-Boleto, 3-Cartao)
        painelCentro.add(new JLabel("Forma de Pagamento:"));
        String[] opcoesPagamento = {"1 - PIX", "2 - Boleto", "3 - Cartão de Crédito"};
        comboPagamento = new JComboBox<>(opcoesPagamento);
        comboPagamento.setMaximumSize(new Dimension(400, 30)); // trava a altura
        painelCentro.add(comboPagamento);

        // rodape com botoes
        JPanel painelRodape = new JPanel();

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> {
            this.dispose(); // fecha o pagamento
            new MainFrame(usuarioLogado).setVisible(true); // volta pra loja com carrinho vazio
        });

        JButton btnFinalizar = new JButton("Confirmar pagamento");
        btnFinalizar.setBackground(new Color(0, 153, 51)); // botao verde
        btnFinalizar.setForeground(Color.WHITE);

        // acao de finalizar a compra
        btnFinalizar.addActionListener(e -> {
            try {
                String endereco = campoEndereco.getText();
                int indexPagamento = comboPagamento.getSelectedIndex() + 1;
                String nomePagamento = (String) comboPagamento.getSelectedItem();

                // joga os dados pro pedido antes de salvar
                pedidoAtual.setEnderecoEntrega(endereco);
                pedidoAtual.setMetodoPagamento(nomePagamento);
                pedidoAtual.setDataPedido(LocalDateTime.now());

                PedidoService service = new PedidoService();
                service.processarCompra(pedidoAtual, indexPagamento);

                JOptionPane.showMessageDialog(this, "Compra aprovada com sucesso!", "Parabéns", JOptionPane.INFORMATION_MESSAGE);

                this.dispose();
                new MainFrame(usuarioLogado).setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro na compra", JOptionPane.ERROR_MESSAGE);
            }
        });

        painelRodape.add(btnCancelar);
        painelRodape.add(btnFinalizar);

        add(painelTopo, BorderLayout.NORTH);
        add(painelCentro, BorderLayout.CENTER);
        add(painelRodape, BorderLayout.SOUTH);
    }
}