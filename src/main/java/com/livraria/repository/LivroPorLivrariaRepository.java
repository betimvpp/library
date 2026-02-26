package com.livraria.repository;

import com.livraria.model.LivroPorLivraria;

import java.util.List;
import java.util.Optional;

public interface LivroPorLivrariaRepository {

    LivroPorLivraria salvar(LivroPorLivraria livroPorLivraria);

    Optional<LivroPorLivraria> buscarPorId(int id);

    List<LivroPorLivraria> listarPorLivraria(int idLivraria);

    /** Retorna apenas exemplares com estoqueDisponivel > 0 em uma livraria. */
    List<LivroPorLivraria> listarDisponiveis(int idLivraria);

    /** Atualiza estoque e estoqueDisponivel (usado por vendas e empréstimos). */
    boolean atualizar(LivroPorLivraria livroPorLivraria);

    boolean deletar(int id);
}
