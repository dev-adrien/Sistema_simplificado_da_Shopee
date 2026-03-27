package com.shopee.view.swing;

import com.shopee.model.Usuario;
import com.shopee.model.Vendedor;
import javax.swing.*;
import java.awt.*;
import com.shopee.service.ProdutoService;
import com.shopee.model.ItemPedido;
import com.shopee.model.Pedido;
import com.shopee.model.Produto;
import java.util.List;
import java.util.stream.Collectors;

public class MainFrame extends JFrame {

    // guarda user
    private Usuario usuarioLogado;
    private ProdutoService produtoService;
    private Pedido carrinhoAtual;

    // transformei o painel do centro em variavel global pra podermos apagar e recriar na pesquisa
    private JPanel painelCentro;

    // constructor para gerar MainFrame com user
    public MainFrame(Usuario usuario) {
        this.usuarioLogado = usuario;
        this.produtoService = new ProdutoService();

        // inicia o carrinho vazio
        this.carrinhoAtual = new Pedido();
        this.carrinhoAtual.setClienteId(usuario.getId());

        // Configurações básicas da janela
        setTitle("Shopee - Loja");
        setSize(900, 600); // deixei mais largo pra caber a barra de pesquisa
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // topo
        JPanel painelTopo = new JPanel(new BorderLayout());
        painelTopo.setBackground(Color.ORANGE);
        painelTopo.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        // nome do usuario no cantinho esquerdo
        JLabel saudacao = new JLabel("Olá, " + usuarioLogado.getNome() + "  ");
        saudacao.setForeground(Color.WHITE);
        saudacao.setFont(new Font("Arial", Font.BOLD, 14));
        painelTopo.add(saudacao, BorderLayout.WEST);

        // painel pra organizar o carrinho e sair na direita
        JPanel painelBotoesTopo = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        painelBotoesTopo.setOpaque(false); // fundo transparente pra manter o laranja

        // se for cliente, cria a super barra de pesquisa e o carrinho
        if (!(usuarioLogado instanceof Vendedor)) {

            // a barra de pesquisa fica no "centro" do topo, esticando o maximo q der
            JPanel painelBusca = new JPanel(new BorderLayout());
            painelBusca.setOpaque(false);
            painelBusca.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));

            JTextField campoBusca = new JTextField();
            JButton btnBuscar = new JButton("Buscar");

            painelBusca.add(campoBusca, BorderLayout.CENTER);
            painelBusca.add(btnBuscar, BorderLayout.EAST);

            painelTopo.add(painelBusca, BorderLayout.CENTER); // gruda no meio

            // acao de buscar clica no botao
            btnBuscar.addActionListener(e -> atualizarVitrine(campoBusca.getText()));

            // o carrinho pequeno no canto
            JButton btnVerCarrinho = new JButton("🛒");
            btnVerCarrinho.addActionListener(e -> {
                this.dispose();
                new CarrinhoFrame(usuarioLogado, carrinhoAtual).setVisible(true);
            });
            painelBotoesTopo.add(btnVerCarrinho);
        }

        painelTopo.add(painelBotoesTopo, BorderLayout.EAST);

        // center
        painelCentro = new JPanel();
        painelCentro.setLayout(new BoxLayout(painelCentro, BoxLayout.Y_AXIS));
        painelCentro.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JScrollPane scrollPane = new JScrollPane(painelCentro);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);

        // rodape
        JPanel painelRodape = new JPanel();

        if (usuarioLogado instanceof Vendedor) {
            // joguei o botao de cadastrar pro rodape do vendedor pra nao atrapalhar a vitrine
            JButton btnCadastrarProduto = new JButton("Cadastrar Novo Produto");
            btnCadastrarProduto.addActionListener(e -> {
                JFrame telaCadastro = new JFrame("Novo Produto");
                telaCadastro.setSize(400, 300);
                telaCadastro.setLocationRelativeTo(this);
                telaCadastro.add(new ProdutoPanel((Vendedor) usuarioLogado));
                telaCadastro.setVisible(true);
            });
            painelRodape.add(btnCadastrarProduto);
        } else {
            JButton btnMeusPedidos = new JButton("Meus Pedidos");

            // AÇÃO ADICIONADA: Chama a tela de histórico de pedidos!
            btnMeusPedidos.addActionListener(e -> {
                this.dispose();
                new MeusPedidosFrame(usuarioLogado).setVisible(true);
            });

            painelRodape.add(btnMeusPedidos);
        }

        JButton btnSair = new JButton("Logout");
        btnSair.addActionListener(e -> {
            this.dispose();
            new LoginFrame().setVisible(true);
        });
        painelRodape.add(btnSair);

        // monta as 3 grandes partes na tela
        add(painelTopo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(painelRodape, BorderLayout.SOUTH);

        // chama o metodo pra renderizar os produtos logo q a tela abre (pesquisa em branco)
        atualizarVitrine("");
    }

    // metodo q desenha e redesenha os produtos dependendo da pesquisa ou do vendedor
    private void atualizarVitrine(String termoBusca) {
        painelCentro.removeAll(); // varre a tela atual

        if (usuarioLogado instanceof Vendedor) {
            JLabel titulo = new JLabel("Meus produtos cadastrados");
            titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
            titulo.setFont(new Font("Arial", Font.BOLD, 16));
            painelCentro.add(titulo);
        } else {
            JLabel titulo = new JLabel("Catálogo de produtos");
            titulo.setAlignmentX(Component.CENTER_ALIGNMENT);
            titulo.setFont(new Font("Arial", Font.BOLD, 16));
            painelCentro.add(titulo);
        }

        painelCentro.add(Box.createVerticalStrut(15));

        try {
            // puxa TUDO do banco
            List<Produto> produtos = produtoService.listarTodosOsProdutos();

            // filtra direto na memoria do Java pra facilitar!
            if (usuarioLogado instanceof Vendedor) {
                // se for vendedor, deixa só os produtos q tem o ID dele
                produtos = produtos.stream()
                        .filter(p -> p.getVendedorId() == usuarioLogado.getId())
                        .collect(Collectors.toList());
            } else if (termoBusca != null && !termoBusca.trim().isEmpty()) {
                // se o cliente digitou algo na busca, filtra pelo nome (ignorando maiusculas)
                produtos = produtos.stream()
                        .filter(p -> p.getNome().toLowerCase().contains(termoBusca.toLowerCase()))
                        .collect(Collectors.toList());
            }

            // desenha o q sobrou do filtro
            if (produtos.isEmpty()) {
                JLabel vazio = new JLabel("Nenhum produto encontrado");
                vazio.setAlignmentX(Component.CENTER_ALIGNMENT);
                painelCentro.add(vazio);
            } else {
                for (Produto p : produtos) {
                    JPanel card = criarCardProduto(p);
                    painelCentro.add(card);
                    painelCentro.add(Box.createVerticalStrut(10));
                }
            }
        } catch (Exception ex) {
            painelCentro.add(new JLabel("Erro ao puxar produtos"));
        }

        // manda o Java atualizar os pixels da tela com os cards novos
        painelCentro.revalidate();
        painelCentro.repaint();
    }

    // cria card pro produto
    private JPanel criarCardProduto(Produto produto) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        card.setMaximumSize(new Dimension(800, 60)); // tamanho fixo

        // info na esquerda
        JPanel info = new JPanel(new GridLayout(2, 1));
        info.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        info.add(new JLabel(produto.getNome()));

        JLabel preco = new JLabel("R$ " + produto.getPreco());
        preco.setForeground(new Color(0, 153, 51));
        info.add(preco);

        card.add(info, BorderLayout.CENTER);

        // acoes da direita mudam se é vendedor ou cliente
        if (!(usuarioLogado instanceof Vendedor)) {
            // botao de add no carrinho
            JButton btnAdd = new JButton("Add ao Carrinho");
            btnAdd.setBackground(Color.ORANGE);
            btnAdd.setForeground(Color.WHITE);

            // acao de adicionar
            btnAdd.addActionListener(e -> {
                ItemPedido item = new ItemPedido();
                item.setProdutoId(produto.getId());
                item.setQuantidade(1);
                item.setPrecoUnitario(produto.getPreco());

                carrinhoAtual.getItens().add(item);
                JOptionPane.showMessageDialog(this, "Adicionado: " + produto.getNome());
            });

            card.add(btnAdd, BorderLayout.EAST);
        } else {
            // vendedor nao compra, so olha quanto de estoque tem
            JLabel estoque = new JLabel("Estoque: " + produto.getQuantidadeEstoque() + "  ");
            card.add(estoque, BorderLayout.EAST);
        }

        return card;
    }
}