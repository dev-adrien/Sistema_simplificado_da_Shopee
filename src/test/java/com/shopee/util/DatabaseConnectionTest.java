package com.shopee.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.sql.Connection;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest { // teste do DatabaseConnection

    @Test
    @DisplayName("Deve conectar ao PostgreSQL com sucesso")
    public void deveConectarAoBanco() {

        Connection conexao = DatabaseConnection.getInstance().getConnection();

        assertNotNull(conexao, "A conexão não deveria ser nula");

        try {
            // verifica a conexão
            assertFalse(conexao.isClosed(), "A conexão deveria estar aberta");
        } catch (Exception e) {
            fail("Erro ao verificar status da conexão: " + e.getMessage());
        }
    }
}