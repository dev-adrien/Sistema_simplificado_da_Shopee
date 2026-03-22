package com.shopee.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface DAO<T> {
    T salvar(T entidade) throws SQLException;

    Optional<T> buscarPorId(int id) throws SQLException;

    List<T> buscarTodos() throws SQLException;

    void atualizar(T entidade) throws SQLException;

    void deletar(int id) throws SQLException;

    long contar() throws SQLException;
}