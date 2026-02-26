package com.livraria.repository;

import com.livraria.model.Autor;

import java.util.List;
import java.util.Optional;

/**
 * Interface com as operações de persistência para a entidade Autor.
 */
public interface AutorRepository {

    Autor salvar(Autor autor);

    Optional<Autor> buscarPorId(int id);

    Optional<Autor> buscarPorCpf(String cpf);

    List<Autor> listarTodos();

    boolean atualizar(Autor autor);

    boolean deletar(int id);
}
