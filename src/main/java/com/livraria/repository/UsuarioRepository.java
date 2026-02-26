package com.livraria.repository;

import com.livraria.model.Usuario;

import java.util.List;
import java.util.Optional;

/**
 * Interface que define as operações de persistência para a entidade Usuario.
 */
public interface UsuarioRepository {

    Usuario salvar(Usuario usuario);

    Optional<Usuario> buscarPorId(int id);

    /**
     * Busca um usuário pelo CPF.
     * 
     * @param cpf o CPF a ser pesquisado
     * @return Optional com o usuário, ou vazio se não existir
     */
    Optional<Usuario> buscarPorCpf(String cpf);

    List<Usuario> listarTodos();

    boolean atualizar(Usuario usuario);

    boolean deletar(int id);
}
