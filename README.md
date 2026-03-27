# Sistema Simplificado da Shopee

Sistema de e-commerce construído em Java abordando os padrões do modelo MVC, persistência de dados com PostgreSQL, Design Patterns e interface gráfica com Swing e Console.

## Tecnologias Utilizadas
* Java 17+
* PostgreSQL
* JDBC (Java Database Connectivity)
* Java Swing (Interface Gráfica)
* Design Patterns: Strategy, Factory, Singleton, Observer, Builder.

## Como Compilar e Executar

1. Clone o repositorio localmente.
2. Configure o banco de dados PostgreSQL rodando o script `database.sql` anexo ao projeto para gerar as tabelas e popular os dados de teste.
3. Altere o arquivo de conexao ou as credenciais na classe `DatabaseConnection` para refletir o seu usuario e senha do banco de dados local.
4. Para executar a interface grafica (Swing), rode a classe `com.shopee.view.swing.SwingApp`.
5. Para executar a versao de terminal, rode a classe `com.shopee.view.console.ConsoleApp`.

## Credenciais Padrao para Teste

Apos rodar o script SQL, o sistema contera as seguintes contas para teste:

**Conta de Cliente:**
* Email: cliente@shopee.com
* Senha: 12345678

**Conta de Vendedor:**
* Email: vendedor1@shopee.com
* Senha: 12345678

## Diagrama de Classes Simplificado (MVC)

* **Model**
    * Usuario (Base) -> Cliente, Vendedor
    * Produto
    * Pedido
    * ItemPedido
    * StatusPedido (Enum)
* **DAO (Data Access Object)**
    * DAO (Interface)
    * UsuarioDAO
    * ProdutoDAO
    * PedidoDAO
* **Service (Regras de Negocio)**
    * UsuarioService
    * ProdutoService
    * PedidoService
* **View (Swing / Console)**
    * SwingApp (Main UI)
    * MainFrame, LoginFrame, CarrinhoFrame, PagamentoFrame
    * ConsoleApp (Main CLI)
* **Design Patterns**
    * PagamentoStrategy, PagamentoFactory
    * NotificadorPedido (Observer)
    * PedidoBuilder
    * DatabaseConnection (Singleton)