package com.livraria.repository;

import com.livraria.model.Livro;

import java.util.List;
import java.util.Optional;

/**
 * Interface com as operações de persistência para a entidade Livro.
 */
public interface LivroRepository {

    Livro salvar(Livro livro);

    Optional<Livro> buscarPorId(int id);

    /** Retorna todos os livros de um determinado Autor. */
    List<Livro> listarPorAutor(int idAutor);

    /** Busca livros cujo título contenha o termo informado (case-insensitive). */
    List<Livro> buscarPorTitulo(String titulo);

    /**
     * Busca livros cujo nome do autor contenha o termo informado (JOIN com
     * autores).
     */
    List<Livro> buscarPorNomeAutor(String nomeAutor);

    List<Livro> listarTodos();

    boolean atualizar(Livro livro);

    boolean deletar(int id);
}
