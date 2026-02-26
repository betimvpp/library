package com.livraria.service;

import com.livraria.model.Emprestimo;
import com.livraria.model.LivroPorLivraria;
import com.livraria.repository.EmprestimoRepository;
import com.livraria.repository.impl.EmprestimoRepositoryImpl;

import java.time.LocalDate;
import java.util.List;

/**
 * Gerencia empréstimos de livros.
 * Só permite emprestar se estoqueDisponivel > 0.
 * Devolução restaura uma unidade ao estoque disponível.
 */
public class EmprestimoService {

    private final EmprestimoRepository emprestimoRepository;
    private final LivroPorLivrariaService lplService;

    public EmprestimoService() {
        this.emprestimoRepository = new EmprestimoRepositoryImpl();
        this.lplService = new LivroPorLivrariaService();
    }

    public EmprestimoService(EmprestimoRepository emprestimoRepository, LivroPorLivrariaService lplService) {
        this.emprestimoRepository = emprestimoRepository;
        this.lplService = lplService;
    }

    /**
     * Registra um empréstimo de livro para um consumidor.
     * Só é permitido se houver estoqueDisponivel > 0.
     *
     * @throws IllegalArgumentException se não houver estoque disponível
     */
    public Emprestimo realizarEmprestimo(int idLivroPorLivraria, int idConsumidor) {
        LivroPorLivraria lpl = lplService.buscarPorId(idLivroPorLivraria)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Exemplar id=" + idLivroPorLivraria + " não encontrado no acervo."));

        if (lpl.getEstoqueDisponivel() <= 0) {
            throw new IllegalArgumentException(
                    "Não há exemplares disponíveis para empréstimo. " +
                            "Estoque total: " + lpl.getEstoque() +
                            " | Disponível: " + lpl.getEstoqueDisponivel());
        }

        // Decrementa apenas o estoque disponível (temporário)
        lplService.decrementarEstoqueEmprestimo(lpl);

        Emprestimo emprestimo = new Emprestimo(
                lpl.getIdLivraria(), idLivroPorLivraria, idConsumidor, LocalDate.now());
        return emprestimoRepository.salvar(emprestimo);
    }

    /**
     * Registra a devolução de um livro emprestado.
     * Restaura uma unidade ao estoque disponível.
     *
     * @throws IllegalArgumentException se o empréstimo não existir ou já tiver sido
     *                                  devolvido
     */
    public Emprestimo registrarDevolucao(int idEmprestimo) {
        Emprestimo emprestimo = emprestimoRepository.buscarPorId(idEmprestimo)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Empréstimo id=" + idEmprestimo + " não encontrado."));

        if (!emprestimo.estaAtivo()) {
            throw new IllegalArgumentException(
                    "Este empréstimo já foi devolvido em: " + emprestimo.getDataDevolucao());
        }

        // Restaura o estoque disponível
        lplService.buscarPorId(emprestimo.getIdLivroPorLivraria())
                .ifPresent(lplService::incrementarEstoqueDevolucao);

        emprestimo.setDataDevolucao(LocalDate.now());
        emprestimoRepository.atualizar(emprestimo);

        return emprestimo;
    }

    public List<Emprestimo> listarPorConsumidor(int idConsumidor) {
        return emprestimoRepository.listarPorConsumidor(idConsumidor);
    }
}
