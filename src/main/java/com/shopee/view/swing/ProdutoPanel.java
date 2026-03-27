package com.shopee.view.swing;

import java.awt.*;
import javax.swing.*;
import com.shopee.model.Vendedor;
import com.shopee.service.ProdutoService;
import com.shopee.model.Produto;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class ProdutoPanel extends JPanel {
    private JTextField campoNome;
    private JTextField campoPreco;
    private JTextField campoEstoque;
    private JTextField campoDescricao;
    private JButton btnSalvar;

    public ProdutoPanel(Vendedor vendedorLogado) {
        setLayout(new GridLayout(5, 2, 10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // campos do produto
        add(new JLabel("Nome do Produto:"));
        campoNome = new JTextField();
        add(campoNome);

        add(new JLabel("Preço (R$):"));
        campoPreco = new JTextField();
        add(campoPreco);

        add(new JLabel("Quantidade em Estoque:"));
        campoEstoque = new JTextField();
        add(campoEstoque);

        add(new JLabel("Descrição rápida:"));
        campoDescricao = new JTextField();
        add(campoDescricao);

        add(new JLabel(""));
        btnSalvar = new JButton("Salvar Produto");
        add(btnSalvar);

        // botão salvar
        btnSalvar.addActionListener(e -> {
            try {
                String nome = campoNome.getText();
                String precoStr = campoPreco.getText().replace(",", "."); // troca virgula por ponto (tratamento obrigatório)
                String estoqueStr = campoEstoque.getText();
                String descricao = campoDescricao.getText();

                // instancia o novo produto
                Produto novoProduto = new Produto();
                novoProduto.setNome(nome);
                novoProduto.setPreco(new BigDecimal(precoStr));
                novoProduto.setQuantidadeEstoque(Integer.parseInt(estoqueStr));
                novoProduto.setDescricao(descricao);
                novoProduto.setVendedorId(vendedorLogado.getId()); // liga o produto ao vendedor pelo login
                novoProduto.setDataCadastro(LocalDateTime.now());
                novoProduto.setAtivo(true);

                // instancia o service pra validar e salvar o produto no bd
                ProdutoService service = new ProdutoService();
                service.cadastrarProduto(novoProduto);
                JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!", "Sucesso", JOptionPane.INFORMATION_MESSAGE);

                // limpa os campos (boa prática)
                campoNome.setText("");
                campoPreco.setText("");
                campoEstoque.setText("");
                campoDescricao.setText("");

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}