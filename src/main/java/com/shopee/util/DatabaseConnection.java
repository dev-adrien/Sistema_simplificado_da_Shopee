package com.shopee.util;

// import java.sql.DatabaseMetaData;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() { // construtor privado pra não instanciar externamente
        try {
            InputStream input = getClass().getClassLoader().getResourceAsStream("config.properties");
            if (input == null) { // verificação do arquivo config.properties
                System.out.println("Erro: Arquivo config.properties não encontrado");
                return;
            }

            Properties properties = new Properties();
            properties.load(input);

            // chamando as credenciais do banco de dados
            String url = properties.getProperty("db.url");
            String usuario = properties.getProperty("db.usuario");
            String senha = properties.getProperty("db.senha");

            connection = DriverManager.getConnection(url, usuario, senha);
            System.out.println("Conectado ao banco de dados!");
        } catch (SQLException e) {
            System.out.println("Erro ao conectar ao banco de dados: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static DatabaseConnection getInstance() { // obtém a unica instancia da conexão
        if (instance == null) {
            instance = new DatabaseConnection();
        }

        return instance;
    }

    public Connection getConnection() { // getter da conexão
        return connection;
    }
}
