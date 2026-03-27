package com.shopee.dao;

import com.shopee.model.Cliente;
import com.shopee.model.Usuario;
import com.shopee.model.Vendedor;
import com.shopee.util.DatabaseConnection;
import com.shopee.util.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UsuarioDAO implements DAO<Usuario> {
    private Connection connection;

    public UsuarioDAO() throws SQLException {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    // método para salvar o usuário já fazendo a ligação com a tabela cliente ou vendedor
    public Usuario salvar(Usuario usuario) throws SQLException {
        String sqlUsuario = "INSERT INTO usuario (nome, email, senha, tipo) VALUES (?, ?, ?, ?)";

        //pega a conexão usando a sua classe utilitária
        Connection conn = com.shopee.util.DatabaseConnection.getConnection();

        try {
            //Desliga o salvamento automático
            conn.setAutoCommit(false);

            // salva o user e pega o id
            //prepara o SQL avisando que vamos querer a chave gerada de volta
            PreparedStatement stmt = conn.prepareStatement(sqlUsuario, java.sql.Statement.RETURN_GENERATED_KEYS);
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());

            // descobre qual é a string certa pro banco
            String tipoBanco = (usuario instanceof com.shopee.model.Vendedor) ? "vendedor" : "cliente";
            stmt.setString(4, tipoBanco);

            stmt.executeUpdate(); // executa o primeiro insert

            // pega o ID que o PostgreSQL acabou de gerar pra esse cara
            java.sql.ResultSet rs = stmt.getGeneratedKeys();
            int idGerado = 0;
            if (rs.next()) {
                idGerado = rs.getInt(1);
                usuario.setId(idGerado); // atualiza o objeto no java
            }

            //salva na tabela dependendo do tipo de user
            if (usuario instanceof com.shopee.model.Vendedor) {
                // se for vendedor, insere na tabela vendedor com dados fictícios pro CNPJ
                String sqlVendedor = "INSERT INTO vendedor (usuario_id, cnpj, razao_social) VALUES (?, ?, ?)";
                PreparedStatement stmtVend = conn.prepareStatement(sqlVendedor);
                stmtVend.setInt(1, idGerado);
                stmtVend.setString(2, "00.000.000/000" + idGerado); // Gera um CNPJ fake pra não dar erro de UNIQUE
                stmtVend.setString(3, "Loja do " + usuario.getNome());
                stmtVend.executeUpdate();

            } else {
                // se for cliente, insere na tabela cliente ligando o ID
                String sqlCliente = "INSERT INTO cliente (usuario_id) VALUES (?)";
                PreparedStatement stmtCli = conn.prepareStatement(sqlCliente);
                stmtCli.setInt(1, idGerado);
                stmtCli.executeUpdate();
            }

            conn.commit();
            return usuario;

        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw new SQLException("Erro crítico ao salvar usuário: " + e.getMessage());
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
            }
        }
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return Optional.of(montarUsuario(rs));
            }
        }
        return Optional.empty();
    }

    public Optional<Usuario> buscarPorEmail(String email) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE email = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { // Similar ao buscarPorId
                return Optional.of(montarUsuario(rs));
            }
        }
        return Optional.empty();
    }

    public boolean validarLogin(String email, String senha) throws SQLException {
        Optional<Usuario> usuario = buscarPorEmail(email);
        return usuario.isPresent() && usuario.get().getSenha().equals(senha);
    }

    @Override
    public List<Usuario> buscarTodos() throws SQLException {
        return new ArrayList<>();
    }

    @Override
    public void atualizar(Usuario entidade) throws SQLException {
    }

    @Override
    public void deletar(int id) throws SQLException {
    }

    @Override
    public long contar() throws SQLException {
        return 0;
    }

    private Usuario montarUsuario(ResultSet rs) throws SQLException {
        Usuario usuario;
        if ("cliente".equals(rs.getString("tipo"))) {
            usuario = new Cliente();
        } else {
            usuario = new Vendedor();
        }
        usuario.setId(rs.getInt("id"));
        usuario.setNome(rs.getString("nome"));
        usuario.setEmail(rs.getString("email"));
        usuario.setSenha(rs.getString("senha"));
        usuario.setAtivo(rs.getBoolean("ativo"));
        return usuario;
    }
}