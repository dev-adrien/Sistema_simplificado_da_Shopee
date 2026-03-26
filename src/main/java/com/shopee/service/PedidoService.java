package com.shopee.service;

import com.shopee.dao.PedidoDAO;
import com.shopee.dao.ProdutoDAO;
import com.shopee.model.ItemPedido;
import com.shopee.model.Pedido;
import com.shopee.model.Produto;
import com.shopee.model.StatusPedido;
import com.shopee.pattern.factory.PagamentoFactory;
import com.shopee.pattern.observer.NotificadorPedido;
import com.shopee.pattern.strategy.PagamentoStrategy;

import java.util.Optional;

public class PedidoService {
    private PedidoDAO pedidoDAO;
    private ProdutoDAO produtoDAO;

    // instanciando os DAOS no construtor
    public PedidoService() {
        this.pedidoDAO = new PedidoDAO();
        this.produtoDAO = new ProdutoDAO();
    }

    // metodo que processa a compra, fazendo validação e implementando a lógica da compra
    public Pedido processarCompra(Pedido pedido, int opcaoPagamento) throws Exception {
        System.out.println("\n===== Iniciando Processamento do Pedido =====");

        //verifica se o pedido tem itens
        if (pedido.getItens() == null || pedido.getItens().isEmpty()) {
            throw new IllegalArgumentException("Erro: o carrinho está vazio!");
        }

        // verifica se tem no estoque
        System.out.println("Verificando disponibilidade no estoque...");
        for (ItemPedido item : pedido.getItens()) {
            Optional<Produto> produtoBuscado = produtoDAO.buscarPorId(item.getProdutoId());
            if (produtoBuscado.isEmpty()) {
                throw new Exception("Erro: produto ID " + item.getProdutoId() + " não encontrado no sistema.");
            }
            Produto produto = produtoBuscado.get();
            // impede que o cliente compre mais do que o que tem no estoque
            if (produto.getQuantidadeEstoque() < item.getQuantidade()) {
                // exceção
                throw new Exception("Erro: estoque insuficiente para o produto '" + produto.getNome() + "'. Disponível: " + produto.getQuantidadeEstoque() + ", Solicitado: " + item.getQuantidade());
            }
        }

        // implementa a lógica de pagamento usando o padrão Strategy e Factory
        System.out.println("Processando pagamento...");
        PagamentoStrategy strategy = PagamentoFactory.criarStrategy(opcaoPagamento);
        strategy.processarPagamento(pedido.getValorTotal()); //Executa a cobrança

        // lógica que atualiza o estoque
        System.out.println("Atualizando estoque no banco de dados...");
        for (ItemPedido item : pedido.getItens()) {
            Produto produto = produtoDAO.buscarPorId(item.getProdutoId()).get();
            // nova quantidade = quantidade antiga - quantidade comprada
            int novoEstoque = produto.getQuantidadeEstoque() - item.getQuantidade();
            produto.setQuantidadeEstoque(novoEstoque);

            // instancia o DAO pra lançar o update no bd
            produtoDAO.atualizar(produto);
        }

        // atualizando status e enviando requisição para o db
        System.out.println("Salvando pedido no banco de dados...");
        pedido.setStatus(StatusPedido.PAGO);
        pedidoDAO.salvar(pedido);

        // envia a notificação de que o pedido foi aprovado usando a lógica do observer
        NotificadorPedido.getInstance().notificar(pedido, "PAGAMENTO_APROVADO");
        System.out.println("Compra finalizada com sucesso!");
        return pedido;
    }
}