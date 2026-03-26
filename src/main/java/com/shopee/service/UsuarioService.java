package com.shopee.service;

import com.shopee.dao.UsuarioDAO;
import com.shopee.model.Usuario;
import com.shopee.pattern.factory.UsuarioFactory;

import java.sql.SQLException;
import java.util.Optional;

public class UsuarioService {
    private UsuarioDAO usuarioDAO;

    public UsuarioService() throws SQLException {
        this.usuarioDAO = new UsuarioDAO();
    }

    // método para cadastrar user novo
    public Usuario cadastrarUsuario(String nome, String email, String senha, String tipo) throws Exception {
        // adiciona regras de email e senha
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("Erro: o formato do e-mail é inválido");
        }
        if (senha == null || senha.length() < 8) {
            throw new IllegalArgumentException("Erro: A senha deve ter pelo menos 6 caracteres.");
        }
        // busca email repetido no bd
        Optional<Usuario> usuarioExistente = usuarioDAO.buscarPorEmail(email);
        if (usuarioExistente.isPresent()) {
            throw new Exception("Erro: Este e-mail já está cadastrado no sistema");
        }

        // constrói o usuário usando o padrão criado instanciando a factory
        Usuario novoUsuario = UsuarioFactory.criarUsuario(tipo);
        novoUsuario.setNome(nome);
        novoUsuario.setEmail(email);
        novoUsuario.setSenha(senha);
        // retorna a requisição de salvcar o user no bd
        return usuarioDAO.salvar(novoUsuario);
    }
}