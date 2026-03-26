package com.shopee.pattern.factory;

import com.shopee.model.Cliente;
import com.shopee.model.Usuario;
import com.shopee.model.Vendedor;

public class UsuarioFactory {

    // metodo estático para criar usuário para vendedor e cliente
    public static Usuario criarUsuario(String tipo) {

        if (tipo == null) {
            throw new IllegalArgumentException("O tipo de usuário não pode ser nulo.");
        }

        if (tipo.toLowerCase().equals("cliente")) {
            return new Cliente();
        } else if (tipo.toLowerCase().equals("vendedor")) {
            return new Vendedor();
        } else {
            throw new IllegalArgumentException("Tipo de usuário inválido: " + tipo + ".\nEscolha 'cliente' ou 'vendedor'.\n");
        }
    }
}