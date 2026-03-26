package com.shopee.view;

import com.shopee.model.Cliente;
import com.shopee.model.Produto;
import com.shopee.model.Usuario;
import com.shopee.model.Vendedor;
import com.shopee.service.ProdutoService;
import com.shopee.service.UsuarioService;
import java.util.Scanner;

public class ConsoleApp {
    private static Scanner scanner = new Scanner(System.in);
    private static UsuarioService usuarioService;
    private static ProdutoService produtoService;

    private static Usuario usuarioLogado = null; // variável que guarda o login atual

    public static void main(String[] args) {
        // instancia os services
        try {
            usuarioService = new UsuarioService();
            produtoService = new ProdutoService();
            // pedidoService = new PedidoService();
        } catch (Exception e) {
            System.out.println("Erro: não foi possível iniciar o sistema\n" + e.getMessage());
            return; // para o programa se o banco não conectar
        }

        System.out.println("=================================================");
        System.out.println("🛒 Shopee 🛒");
        System.out.println("=================================================");

        boolean rodando = true; // loop pra deixar o sistema aberto
        while (rodando) {
            if (usuarioLogado == null) {
                rodando = exibirMenuDeslogado();
            } else if (usuarioLogado instanceof Vendedor) { // verifica se o user é vendedor ou cliente
                rodando = exibirMenuVendedor();
            } else if (usuarioLogado instanceof Cliente) {
                rodando = exibirMenuCliente();
            }
        }

        System.out.println("\n\n\n\n\nSaindo do sistema...");
        scanner.close();
    }

    // instanciando menus
    private static boolean exibirMenuDeslogado() {
        System.out.println("\n============ LOJA ============");
        System.out.println("1 - Fazer Login");
        System.out.println("2 - Cadastre-se");
        System.out.println("3 - Sair");
        System.out.print("Escolha: ");
        int opcao = lerOpcaoInteira();

        switch (opcao) {
            case 1:
                fazerLogin();
                return true;
            case 2:
                cadastrarUsuario();
                return true;
            case 3:
                return false; // sai do sistema
            default:
                System.out.println("Opção inválida!");
                return true;
        }
    }

    private static boolean exibirMenuVendedor() {
        System.out.println("\n====== PAINEL DO VENDEDOR (" + usuarioLogado.getNome() + ") ======");
        System.out.println("1 - Cadastrar novo produto");
        System.out.println("2 - Listar meus produtos (Em breve)");
        System.out.println("3 - Logout");
        System.out.print("Escolha: ");
        int opcao = lerOpcaoInteira();

        if (opcao == 1) {
            System.out.println("Chamando a tela de cadastro de produto...");
            // lógica para cadastrar produto (ainda não implementada)
        } else if (opcao == 3) {
            usuarioLogado = null;
            System.out.println("Deslogado com sucesso.");
        }
        return true;
    }

    private static boolean exibirMenuCliente() {
        System.out.println("\n======= INÍCIO =======");
        System.out.println("1 -  Ver Vitrine de Produtos");
        System.out.println("2 -  Comprar (Em breve)");
        System.out.println("3 - Logout");
        System.out.print("Escolha: ");

        int opcao = lerOpcaoInteira();

        if (opcao == 1) {
            System.out.println("Listando produtos do banco de dados...");
            // produtoService.listarTodosOsProdutos()
        } else if (opcao == 3) {
            usuarioLogado = null;
            System.out.println("Deslogado com sucesso.");
        }
        return true;
    }

    private static void fazerLogin() {
        System.out.println("\n===== Entrar =====");
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();

        System.out.println("Validando informações...");
        // UsuarioService, UsuarioDAO, validar login, etc
    }

    private static void cadastrarUsuario() {
        System.out.println("\n====== CADASTRE-SE =======");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Senha: ");
        String senha = scanner.nextLine();
        System.out.print("Tipo (cliente/vendedor): ");
        String tipo = scanner.nextLine();

        try {
            // cria user novo pelo service
            Usuario novo = usuarioService.cadastrarUsuario(nome, email, senha, tipo);
            System.out.println("Usuário " + novo.getNome() + " cadastrado com sucesso!");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    // método para ler opções do menu e evitar erros de input (boa prática)
    private static int lerOpcaoInteira() {
        try {
            int opcao = Integer.parseInt(scanner.nextLine());
            return opcao;
        } catch (NumberFormatException e) {
            return -1; // retornar -1 para indicar opção inválida
        }
    }
}