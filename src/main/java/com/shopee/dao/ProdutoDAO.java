package com.shopee.dao;

import com.shopee.model.Produto;
import com.shopee.util.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProdutoDAO implements DAO<Produto> {

    private Connection connection;

    // estabelecendo conexão
    public ProdutoDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Produto salvar(Produto produto) throws SQLException {

        // Comando SQL para inserir Produtos
        String sql = "INSERT INTO produto (vendedor_id, nome, descricao, categoria, preco, quantidade_estoque) VALUES (?, ?, ?, ?, ?, ?)";

        // o Statement.RETURN_GENERATED_KEYS avisa o banco para nos devolver o ID que ele gerar
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, produto.getVendedorId());
            stmt.setString(2, produto.getNome());
            stmt.setString(3, produto.getDescricao());
            stmt.setString(4, produto.getCategoria());
            stmt.setBigDecimal(5, produto.getPreco());
            stmt.setInt(6, produto.getQuantidadeEstoque());


            // esse comando atualiza o banco com as inserções
            stmt.executeUpdate();

            // try que tenta pegar o ID que o BD gerou
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    produto.setId(rs.getInt(1));
                }
            }

            System.out.println("Produto salvo!\nID: " + produto.getId());
            return produto;
        }
    }

    @Override
    public Optional<Produto> buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM produto WHERE id = ?";
        // stmt é o objeto que envia a atualização
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            // rs é a variável que vai guardar o resultado da busca
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs.getInt("id"));
                    produto.setVendedorId(rs.getInt("vendedor_id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setDescricao(rs.getString("descricao"));
                    produto.setCategoria(rs.getString("categoria"));
                    produto.setPreco(rs.getBigDecimal("preco"));
                    produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                    produto.setAtivo(rs.getBoolean("ativo"));

                    // retorna o produto caso encontrado
                    return Optional.of(produto);
                }
            }
        }
        return Optional.empty(); // retorna um objeto vazio, caso não ache o produto
    }

    @Override
    public List<Produto> buscarTodos() throws SQLException {
        String sql = "SELECT * FROM produto";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                List<Produto> produtos = new ArrayList<>();

                // usa o while para construir cada produto
                while (rs.next()) {
                    Produto produto = new Produto();
                    produto.setId(rs.getInt("id"));
                    produto.setVendedorId(rs.getInt("vendedor_id"));
                    produto.setNome(rs.getString("nome"));
                    produto.setDescricao(rs.getString("descricao"));
                    produto.setCategoria(rs.getString("categoria"));
                    produto.setPreco(rs.getBigDecimal("preco"));
                    produto.setQuantidadeEstoque(rs.getInt("quantidade_estoque"));
                    produto.setAtivo(rs.getBoolean("ativo"));
                    produtos.add(produto); // armazena o produto na lista
                }
                return produtos; // retorna a lista de produtos
            }
        }
    }

    @Override
    public void atualizar(Produto entidade) throws SQLException {
        //
        String sql = "UPDATE produto SET nome = ?, descricao = ?, categoria = ?, preco = ?, quantidade_estoque = ? WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, entidade.getNome());
            stmt.setString(2, entidade.getDescricao());
            stmt.setString(3, entidade.getCategoria());
            stmt.setBigDecimal(4, entidade.getPreco());
            stmt.setInt(5, entidade.getQuantidadeEstoque());
            stmt.setInt(6, entidade.getId());
            stmt.executeUpdate();
        }

        System.out.println("Produto atualizado!\nID: " + entidade.getId());
    }

    @Override
    public void deletar(int id) throws SQLException {
        // atualiza o ativo para false, com o objetivo de não ser mostrado
        String sql = "UPDATE produto SET ativo = false WHERE id = ?";

        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }

        System.out.println("Produto deletado!\nID: " + id);
    }

    @Override
    public long contar() throws SQLException {
        String sql = "SELECT COUNT(*) FROM produto WHERE ativo = true";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getLong(1); // pega o numero da coluna 1 (que tem a contagem da pesquisa SQL
                }
            }
        }
        return 0;
    }
}
