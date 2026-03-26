package com.shopee.service;

import com.shopee.dao.ProdutoDAO;
import com.shopee.model.Produto;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ProdutoService {
    private ProdutoDAO produtoDAO;

    public ProdutoService() {
        this.produtoDAO = new ProdutoDAO();
    }

    public Produto cadastrarProduto(Produto produto) throws Exception {

        // lógica de validação do cadastro do produto
        if (produto.getNome() == null || produto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("❌ Erro: O nome do produto não pode ficar em branco.");
        }
        if (produto.getPreco() == null || produto.getPreco().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("❌ Erro: O preço do produto deve ser maior que R$ 0,00.");
        }
        if (produto.getQuantidadeEstoque() < 0) {
            throw new IllegalArgumentException("❌ Erro: A quantidade em estoque não pode ser negativa.");
        }
        return produtoDAO.salvar(produto);
    }

    // retorna lista de produtos cadastrados no sistema
    public List<Produto> listarTodosOsProdutos() throws SQLException {
        return produtoDAO.buscarTodos();
    }
}