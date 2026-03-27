package com.shopee.view.console;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import com.shopee.dao.UsuarioDAO;
import com.shopee.model.Cliente;
import com.shopee.model.ItemPedido;
import com.shopee.model.Pedido;
import com.shopee.model.Produto;
import com.shopee.model.Usuario;
import com.shopee.model.Vendedor;
import com.shopee.service.PedidoService;
import com.shopee.service.ProdutoService;
import com.shopee.service.UsuarioService;

public class ConsoleApp {
    private static Scanner scanner = new Scanner(System.in);
    private static UsuarioService usuarioService;
    private static ProdutoService produtoService;
    private static PedidoService pedidoService; // puxa o service de pedidos

    private static Usuario usuarioLogado = null; // variável que guarda o login atual
    private static Pedido carrinhoAtual = null; // guarda o carrinho na memoria

    public static void main(String[] args) {
        // instancia os services
        try {
            usuarioService = new UsuarioService();
            produtoService = new ProdutoService();
            pedidoService = new PedidoService();
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
        System.out.println("2 - Listar meus produtos");
        System.out.println("3 - Logout");
        System.out.print("Escolha: ");
        int opcao = lerOpcaoInteira();

        if (opcao == 1) {
            System.out.println("\n--- NOVO PRODUTO ---");
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Preço (R$): ");
            String precoStr = scanner.nextLine().replace(",", "."); // tratamento da virgula
            System.out.print("Estoque: ");
            int estoque = lerOpcaoInteira();
            System.out.print("Descrição: ");
            String desc = scanner.nextLine();

            try {
                // monta o objeto produto
                Produto p = new Produto();
                p.setNome(nome);
                p.setPreco(new BigDecimal(precoStr));
                p.setQuantidadeEstoque(estoque);
                p.setDescricao(desc);
                p.setVendedorId(usuarioLogado.getId()); // amarra ao vendedor
                p.setDataCadastro(LocalDateTime.now());
                p.setAtivo(true);

                produtoService.cadastrarProduto(p);
                System.out.println("Produto cadastrado com sucesso!");
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }

        } else if (opcao == 2) {
            try {
                // lista produtos filtrando pelo vendedor logado
                List<Produto> produtos = produtoService.listarTodosOsProdutos();
                System.out.println("\n--- MEUS PRODUTOS ---");
                for (Produto p : produtos) {
                    if (p.getVendedorId() == usuarioLogado.getId()) {
                        System.out.println("ID: " + p.getId() + " | " + p.getNome() + " | R$ " + p.getPreco());
                    }
                }
            } catch (Exception e) {
                System.out.println("Erro ao listar produtos.");
            }

        } else if (opcao == 3) {
            usuarioLogado = null;
            System.out.println("Deslogado com sucesso.");
        }
        return true;
    }

    private static boolean exibirMenuCliente() {
        System.out.println("\n======= INÍCIO =======");
        System.out.println("1 - Ver Vitrine de Produtos");
        System.out.println("2 - Adicionar ao Carrinho");
        System.out.println("3 - Ir para Pagamento");
        System.out.println("4 - Logout");
        System.out.print("Escolha: ");

        int opcao = lerOpcaoInteira();

        if (opcao == 1) {
            try {
                // puxa todos os produtos do banco
                List<Produto> produtos = produtoService.listarTodosOsProdutos();
                System.out.println("\n--- CATÁLOGO ---");
                for (Produto p : produtos) {
                    System.out.println("ID: " + p.getId() + " | " + p.getNome() + " | R$ " + p.getPreco());
                }
            } catch (Exception e) {
                System.out.println("Erro ao puxar produtos");
            }

        } else if (opcao == 2) {
            System.out.print("Digite o ID do produto: ");
            int id = lerOpcaoInteira();
            System.out.print("Confirme o preço (R$): "); // para simplificar no console
            String precoStr = scanner.nextLine().replace(",", ".");

            // joga no carrinho da memoria
            ItemPedido item = new ItemPedido();
            item.setProdutoId(id);
            item.setQuantidade(1);
            item.setPrecoUnitario(new BigDecimal(precoStr));

            carrinhoAtual.getItens().add(item);
            System.out.println("Adicionado ao carrinho!");

        } else if (opcao == 3) {
            if (carrinhoAtual.getItens().isEmpty()) {
                System.out.println("O carrinho está vazio.");
            } else {
                System.out.println("\n--- CHECKOUT ---");
                System.out.print("Endereço de entrega: ");
                String endereco = scanner.nextLine();
                System.out.println("Formas de Pagamento: 1-PIX, 2-Boleto, 3-Cartão");
                System.out.print("Escolha: ");
                int pag = lerOpcaoInteira();

                String metodo = (pag == 1) ? "PIX" : (pag == 2) ? "Boleto" : "Cartão";

                try {
                    // insere os dados faltantes no pedido
                    carrinhoAtual.setEnderecoEntrega(endereco);
                    carrinhoAtual.setMetodoPagamento(metodo);
                    carrinhoAtual.setDataPedido(LocalDateTime.now());

                    // valida e salva no banco
                    pedidoService.processarCompra(carrinhoAtual, pag);
                    System.out.println("Compra aprovada com sucesso!");

                    // zera o carrinho apos a compra
                    carrinhoAtual = new Pedido();
                    carrinhoAtual.setClienteId(usuarioLogado.getId());
                } catch (Exception e) {
                    System.out.println("Erro na compra: " + e.getMessage());
                }
            }

        } else if (opcao == 4) {
            usuarioLogado = null;
            carrinhoAtual = null;
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
        try {
            // usa o DAO pra validar a existencia
            UsuarioDAO dao = new UsuarioDAO();
            if (dao.validarLogin(email, senha)) {
                Optional<Usuario> user = dao.buscarPorEmail(email);
                usuarioLogado = user.get();

                // se for cliente, cria um carrinho vazio na hora que loga
                if (usuarioLogado instanceof Cliente) {
                    carrinhoAtual = new Pedido();
                    carrinhoAtual.setClienteId(usuarioLogado.getId());
                }
                System.out.println("Bem-vindo, " + usuarioLogado.getNome() + "!");
            } else {
                System.out.println("Email ou senha incorretos.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao conectar no banco de dados.");
        }
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