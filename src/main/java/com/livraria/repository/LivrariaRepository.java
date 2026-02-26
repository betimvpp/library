package com.livraria.repository;

import com.livraria.model.Livraria;

import java.util.List;
import java.util.Optional;

/**
 * Interface com as operações de persistência para a entidade Livraria.
 */
public interface LivrariaRepository {

    Livraria salvar(Livraria livraria);

    Optional<Livraria> buscarPorId(int id);

    List<Livraria> listarTodos();

    boolean atualizar(Livraria livraria);

    boolean deletar(int id);
}
