package com.livraria.repository;

import com.livraria.model.Emprestimo;

import java.util.List;
import java.util.Optional;

public interface EmprestimoRepository {

    Emprestimo salvar(Emprestimo emprestimo);

    Optional<Emprestimo> buscarPorId(int id);

    List<Emprestimo> listarPorConsumidor(int idConsumidor);

    /** Retorna empréstimos ativos (sem dataDevolucao) de um exemplar. */
    List<Emprestimo> listarAtivos(int idLivroPorLivraria);

    /** Atualiza o empréstimo ao registrar a devolução. */
    boolean atualizar(Emprestimo emprestimo);
}
