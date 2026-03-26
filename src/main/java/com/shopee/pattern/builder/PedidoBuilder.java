package com.shopee.pattern.builder;

import com.shopee.model.ItemPedido;
import com.shopee.model.Pedido;
import com.shopee.model.StatusPedido;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class PedidoBuilder {

    private Pedido pedido;

    public PedidoBuilder() {
        this.pedido = new Pedido();
        // definindo valores iniciais
        this.pedido.setDataPedido(LocalDateTime.now());
        this.pedido.setStatus(StatusPedido.AGUARDANDO); // usando o Enum para definir o status
        this.pedido.setValorTotal(BigDecimal.ZERO);
    }

    public PedidoBuilder comCliente(int clienteId) {
        this.pedido.setClienteId(clienteId);
        return this;
    }

    public PedidoBuilder comMetodoPagamento(String metodoPagamento) {
        this.pedido.setMetodoPagamento(metodoPagamento);
        return this;
    }

    public PedidoBuilder comEnderecoEntrega(String enderecoEntrega) {
        this.pedido.setEnderecoEntrega(enderecoEntrega);
        return this;
    }

    // esse método tenta adicionar um item ao pedido e atualizar o valor total do pedido
    public PedidoBuilder adicionarItem(ItemPedido item) {
        this.pedido.getItens().add(item);
        // calcula: quantidade * precoUnitario (em operações de BigDecimal)
        BigDecimal valorDoItem = item.getPrecoUnitario().multiply(new BigDecimal(item.getQuantidade()));

        // atualiza o valor total do pedido
        this.pedido.setValorTotal(this.pedido.getValorTotal().add(valorDoItem));

        return this;
    }

    // validação de exceções e montagem do pedido
    public Pedido build() {
        if (this.pedido.getClienteId() <= 0) {
            throw new IllegalStateException("Erro: faça login para continuar.");
        }
        if (this.pedido.getItens().isEmpty()) {
            throw new IllegalStateException("Erro: você não possui itens no carrinho.");
        }
        if (this.pedido.getMetodoPagamento() == null || this.pedido.getEnderecoEntrega() == null) {
            throw new IllegalStateException("Erro: selecione o método de pagamento e o/ou preencha o endereço corretamente.");
        }

        return this.pedido;
    }
}