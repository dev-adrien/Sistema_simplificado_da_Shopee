CREATE DATABASE shopee_bd;

\c shopee_bd;

CREATE TABLE IF NOT EXISTS usuario (
                                       id SERIAL PRIMARY KEY,
                                       nome VARCHAR(100) NOT NULL,
                                       email VARCHAR(100) UNIQUE NOT NULL,
                                       senha VARCHAR(255) NOT NULL,
                                       tipo VARCHAR(20) CHECK (tipo IN ('cliente', 'vendedor')) NOT NULL,
                                       data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS cliente (
                                       id SERIAL PRIMARY KEY,
                                       usuario_id INT UNIQUE REFERENCES usuario(id),
                                       cpf VARCHAR(14) UNIQUE,
                                       telefone VARCHAR(20),
                                       endereco TEXT,
                                       data_nascimento DATE
);

CREATE TABLE IF NOT EXISTS vendedor (
                                        id SERIAL PRIMARY KEY,
                                        usuario_id INT UNIQUE REFERENCES usuario(id),
                                        cnpj VARCHAR(18) UNIQUE,
                                        razao_social VARCHAR(200),
                                        telefone VARCHAR(20),
                                        avaliacao DECIMAL(3,2) DEFAULT 5.0
);

CREATE TABLE IF NOT EXISTS produto (
                                       id SERIAL PRIMARY KEY,
                                       vendedor_id INT NOT NULL REFERENCES vendedor(id),
                                       nome VARCHAR(200) NOT NULL,
                                       descricao TEXT,
                                       categoria VARCHAR(100),
                                       preco DECIMAL(10,2) NOT NULL,
                                       quantidade_estoque INT DEFAULT 0,
                                       imagem_url VARCHAR(500),
                                       data_cadastro TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       ativo BOOLEAN DEFAULT TRUE
);

CREATE TABLE IF NOT EXISTS pedido (
                                      id SERIAL PRIMARY KEY,
                                      cliente_id INT NOT NULL REFERENCES cliente(id),
                                      data_pedido TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                      status VARCHAR(20) DEFAULT 'AGUARDANDO' CHECK (status IN ('AGUARDANDO', 'PAGO', 'ENVIADO', 'ENTREGUE', 'CANCELADO', 'aguardando', 'pago', 'enviado', 'entregue', 'cancelado')),
                                      valor_total DECIMAL(10,2) NOT NULL,
                                      metodo_pagamento VARCHAR(50),
                                      endereco_entrega TEXT
);

CREATE TABLE IF NOT EXISTS item_pedido (
                                           id SERIAL PRIMARY KEY,
                                           pedido_id INT NOT NULL REFERENCES pedido(id),
                                           produto_id INT NOT NULL REFERENCES produto(id),
                                           quantidade INT NOT NULL,
                                           preco_unitario DECIMAL(10,2) NOT NULL
);

CREATE TABLE IF NOT EXISTS log_sistema (
                                           id SERIAL PRIMARY KEY,
                                           data_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                           nivel VARCHAR(10) CHECK (nivel IN ('INFO', 'WARNING', 'ERROR')) NOT NULL,
                                           mensagem TEXT NOT NULL,
                                           usuario_id INT REFERENCES usuario(id)
);

-- Zera todas as tabelas e reinicia os IDs
TRUNCATE TABLE usuario, cliente, vendedor, produto, pedido, item_pedido, log_sistema RESTART IDENTITY CASCADE;

-- Inserindo contas
INSERT INTO usuario (id, nome, email, senha, tipo) VALUES
                                                       (1, 'Adrien (Cliente)', 'cliente@shopee.com', '12345678', 'cliente'),
                                                       (2, 'Tech Hardware Store', 'vendedor1@shopee.com', '12345678', 'vendedor'),
                                                       (3, 'Geek & Dev Imports', 'vendedor2@shopee.com', '12345678', 'vendedor');

SELECT setval(pg_get_serial_sequence('usuario', 'id'), (SELECT MAX(id) FROM usuario));

-- Inserindo perfis
INSERT INTO cliente (id, usuario_id, cpf, telefone, endereco) VALUES
    (1, 1, '111.111.111-11', '(11) 99999-9999', 'Rua das Flores, 123 - Centro');
SELECT setval(pg_get_serial_sequence('cliente', 'id'), (SELECT MAX(id) FROM cliente));

INSERT INTO vendedor (id, usuario_id, cnpj, razao_social) VALUES
                                                              (1, 2, '11.111.111/0001-11', 'Tech Hardware Informática LTDA'),
                                                              (2, 3, '22.222.222/0002-22', 'Geek Dev e Livraria Comércio');
SELECT setval(pg_get_serial_sequence('vendedor', 'id'), (SELECT MAX(id) FROM vendedor));

-- Inserindo produtos
INSERT INTO produto (vendedor_id, nome, descricao, categoria, preco, quantidade_estoque) VALUES
                                                                                             (1, 'Notebook Asus TUF Gaming F16', 'Notebook gamer com RTX 3050 e 16GB RAM DDR5', 'Informática', 4500.00, 10),
                                                                                             (1, 'SSD Interno Kingston NV2 1TB', 'SSD NVMe M.2 Leitura 3500MB/s', 'Hardware', 350.00, 45),
                                                                                             (1, 'Monitor Gamer LG UltraGear 24"', 'Monitor 144Hz 1ms IPS Full HD', 'Monitores', 850.00, 15),
                                                                                             (1, 'Teclado Mecânico Keychron K2', 'Teclado wireless com switch brown', 'Acessórios', 450.00, 20),
                                                                                             (2, 'Controle 8BitDo Ultimate 2C', 'Controle sem fio para PC e Mobile com hall effect', 'Games', 250.00, 35),
                                                                                             (2, 'Livro: Python para Data Science', 'Guia prático de análise de dados com Pandas', 'Livros', 115.00, 25),
                                                                                             (2, 'Minecraft Java Edition', 'Key de ativação original para PC', 'Jogos', 149.90, 100),
                                                                                             (2, 'Hub USB-C Baseus 7 em 1', 'Adaptador com HDMI, USB 3.0 e rede RJ45', 'Acessórios', 160.00, 22);

SELECT setval(pg_get_serial_sequence('produto', 'id'), (SELECT MAX(id) FROM produto));