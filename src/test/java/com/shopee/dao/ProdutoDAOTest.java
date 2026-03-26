package com.shopee.dao;

import com.shopee.model.Produto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProdutoDAOTest {

    @Test
    @DisplayName("Deve realizar o fluxo completo de CRUD do Produto")
    public void deveTestarFluxoCompletoDoProduto() {
        ProdutoDAO dao = new ProdutoDAO();
        Produto produto = new Produto();

        try {
            // salvar (insert)
            produto.setVendedorId(1);
            produto.setNome("Teclado Mecânico");
            produto.setDescricao("Teclado switch azul");
            produto.setCategoria("Periféricos");
            produto.setPreco(new BigDecimal("250.00"));
            produto.setQuantidadeEstoque(10);

            Produto produtoSalvo = dao.salvar(produto);
            assertNotNull(produtoSalvo.getId(), "O ID não deveria ser nulo após salvar");
            assertTrue(produtoSalvo.getId() > 0, "O ID gerado deve ser maior que zero");

            int idGerado = produtoSalvo.getId();

            // buscar por id (select)
            Optional<Produto> produtoBuscado = dao.buscarPorId(idGerado);
            assertTrue(produtoBuscado.isPresent(), "O produto salvo deveria ser encontrado");
            assertEquals("Teclado Mecânico", produtoBuscado.get().getNome());

            // update
            Produto produtoParaAtualizar = produtoBuscado.get();
            produtoParaAtualizar.setPreco(new BigDecimal("199.99"));
            dao.atualizar(produtoParaAtualizar);

            Optional<Produto> produtoAtualizado = dao.buscarPorId(idGerado);
            assertEquals(new BigDecimal("199.99"), produtoAtualizado.get().getPreco(), "O preço deveria ter sido atualizado");

            // select cont
            long total = dao.contar();
            assertTrue(total > 0, "Deveria haver pelo menos um produto cadastrado");

            // delete
            dao.deletar(idGerado);
            Optional<Produto> produtoDeletado = dao.buscarPorId(idGerado);
            assertFalse(produtoDeletado.get().isAtivo(), "O status do produto deveria ser false após a exclusão lógica");

            System.out.println("✅ Todos os testes do ProdutoDAO passaram com sucesso!");

        } catch (SQLException e) {
            fail("Erro de SQL durante o teste: " + e.getMessage());
        }
    }
}