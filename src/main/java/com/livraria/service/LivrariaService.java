package com.livraria.service;

import com.livraria.model.Admin;
import com.livraria.model.Livraria;
import com.livraria.model.Usuario;
import com.livraria.repository.LivrariaRepository;
import com.livraria.repository.UsuarioRepository;
import com.livraria.repository.impl.LivrariaRepositoryImpl;
import com.livraria.repository.impl.UsuarioRepositoryImpl;

import java.util.List;
import java.util.Optional;

/**
 * Camada de serviço para a entidade Livraria.
 * Garante que apenas um Admin pode criar uma livraria.
 */
public class LivrariaService {

    private final LivrariaRepository livrariaRepository;
    private final UsuarioRepository usuarioRepository;

    public LivrariaService() {
        this.livrariaRepository = new LivrariaRepositoryImpl();
        this.usuarioRepository = new UsuarioRepositoryImpl();
    }

    public LivrariaService(LivrariaRepository livrariaRepository, UsuarioRepository usuarioRepository) {
        this.livrariaRepository = livrariaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    /**
     * Cria uma nova livraria, validando que o responsável é um Admin.
     *
     * @throws IllegalArgumentException se nome ou cidade estiverem em branco,
     *                                  ou se o idAdmin não corresponder a um Admin
     */
    public Livraria criarLivraria(int idAdmin, String nome, String cidade) {
        validarNome(nome);
        validarCidade(cidade);
        validarAdmin(idAdmin);

        Livraria livraria = new Livraria(idAdmin, nome, cidade);
        return livrariaRepository.salvar(livraria);
    }

    public Optional<Livraria> buscarPorId(int id) {
        if (id <= 0)
            throw new IllegalArgumentException("O id deve ser positivo.");
        return livrariaRepository.buscarPorId(id);
    }

    public List<Livraria> listarTodas() {
        return livrariaRepository.listarTodos();
    }

    public boolean atualizarLivraria(Livraria livraria) {
        if (livraria.getId() <= 0)
            throw new IllegalArgumentException("A livraria deve ter um id válido.");
        validarNome(livraria.getNome());
        validarCidade(livraria.getCidade());
        validarAdmin(livraria.getIdAdmin());
        return livrariaRepository.atualizar(livraria);
    }

    public boolean deletarLivraria(int id) {
        if (id <= 0)
            throw new IllegalArgumentException("O id deve ser positivo.");
        return livrariaRepository.deletar(id);
    }

    // --- Validações privadas ---

    private void validarNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da livraria é obrigatório.");
        }
    }

    private void validarCidade(String cidade) {
        if (cidade == null || cidade.isBlank()) {
            throw new IllegalArgumentException("A cidade da livraria é obrigatória.");
        }
    }

    /**
     * Verifica que o id corresponde a um usuário existente e que é Admin.
     */
    private void validarAdmin(int idAdmin) {
        Usuario usuario = usuarioRepository.buscarPorId(idAdmin)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Usuário com id " + idAdmin + " não encontrado."));

        if (!(usuario instanceof Admin)) {
            throw new IllegalArgumentException(
                    "Somente um Administrador pode ser responsável por uma livraria. " +
                            "O usuário informado é: " + usuario.getRole().getDescricao());
        }
    }
}
