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
    public Usuario salvar(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO usuario (nome, email, senha, tipo) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getEmail());
            stmt.setString(3, usuario.getSenha());
            stmt.setString(4, usuario instanceof Cliente ? "cliente" : "vendedor");
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                usuario.setId(rs.getInt(1));
            }
            Logger.getInstance().log("Usuário salvo: " + usuario.getEmail(), "INFO");
        }
        return usuario;
    }

    @Override
    public Optional<Usuario> buscarPorId(int id) throws SQLException {
        String sql = "SELECT * FROM usuario WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                /*
                Usuario usuario;
                if (rs.getString("tipo").equals("cliente")) {
                    usuario = new Cliente();
                }
            } else {
                usuario = new Vendedor();
                usuario.setId(rs.getInt("id"));
                usuario.setNome(rs.getString("nome"));
                usuario.setEmail(rs.getString("email"));
                usuario.setSenha(rs.getString("senha"));
                usuario.setAtivo(rs.getBoolean("ativo"));
                return Optional.of(usuario);
                */
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
    } // Outros métodos obrigatórios...

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