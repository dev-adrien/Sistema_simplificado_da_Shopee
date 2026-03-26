package com.shopee.dao;

import com.shopee.model.ItemPedido;
import com.shopee.model.Pedido;
import com.shopee.util.DatabaseConnection;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public class PedidoDAO implements DAO<Pedido> {

    private Connection connection;

    public PedidoDAO() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public Pedido salvar(Pedido pedido) throws SQLException {
        String sqlPedido = "INSERT INTO pedido (cliente_id, data_pedido, status, valor_total, metodo_pagamento, endereco_entrega) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO item_pedido (pedido_id, produto_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";

        try {
            // desliga o salvamento automático para não salvar nada enquanto o processo não estiver completo
            connection.setAutoCommit(false);

            // cria o peddo principal sem itens para pegar o ID que o banco vai gerar
            try (PreparedStatement stmtPedido = connection.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                stmtPedido.setInt(1, pedido.getClienteId());
                stmtPedido.setTimestamp(2, Timestamp.valueOf(pedido.getDataPedido())); // conversão necessária
                stmtPedido.setString(3, pedido.getStatus().name()); // puxa o nome do enum
                stmtPedido.setBigDecimal(4, pedido.getValorTotal());
                stmtPedido.setString(5, pedido.getMetodoPagamento());
                stmtPedido.setString(6, pedido.getEnderecoEntrega());
                stmtPedido.executeUpdate();

                // pega o ID do peido (assim como no produtoDAO)
                try (ResultSet rs = stmtPedido.getGeneratedKeys()) {
                    if (rs.next()) {
                        pedido.setId(rs.getInt(1));
                    } else {
                        throw new SQLException("Erro: ID do pedido não obtido");
                    }
                }
            }

            // salva os itens pelo ID do pedido
            try (PreparedStatement stmtItem = connection.prepareStatement(sqlItem)) {
                for (ItemPedido item : pedido.getItens()) {
                    stmtItem.setInt(1, pedido.getId()); //A chave estrangeira que liga o item ao pedido
                    stmtItem.setInt(2, item.getProdutoId());
                    stmtItem.setInt(3, item.getQuantidade());
                    stmtItem.setBigDecimal(4, item.getPrecoUnitario());
                    stmtItem.executeUpdate();
                }
            }

            // salva tudo no bd
            connection.commit();
            System.out.println("Pedido e itens salvos no banco com sucesso! ID: " + pedido.getId());

        } catch (SQLException e) {
            // caso ocorra algum erro, é necessário desfazer tudo
            if (connection != null) {
                System.err.println("Erro ao salvar pedido. Desfazendo transação (Rollback)...");
                connection.rollback();
            }
            throw e; // Repassa o erro para o service tratar
        } finally {
            // é necessário ligar o salvamento automático pra n afetar outros DAOs
            if (connection != null) {
                connection.setAutoCommit(true);
            }
        }

        return pedido;
    }

    // metodos obrigatórios
    @Override
    public Optional<Pedido> buscarPorId(int id) throws SQLException { return Optional.empty(); }
    @Override
    public List<Pedido> buscarTodos() throws SQLException { return List.of(); }
    @Override
    public void atualizar(Pedido entidade) throws SQLException {}
    @Override
    public void deletar(int id) throws SQLException {}
    @Override
    public long contar() throws SQLException { return 0; }
}