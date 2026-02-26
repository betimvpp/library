package com.livraria.service;

import com.livraria.model.LivroPorLivraria;
import com.livraria.model.Venda;
import com.livraria.repository.VendaRepository;
import com.livraria.repository.impl.VendaRepositoryImpl;

import java.time.LocalDate;
import java.util.List;

/**
 * Gerencia vendas de livros.
 * Ao vender, decrementa estoque total e disponível permanentemente.
 */
public class VendaService {

    private final VendaRepository vendaRepository;
    private final LivroPorLivrariaService lplService;

    public VendaService() {
        this.vendaRepository = new VendaRepositoryImpl();
        this.lplService = new LivroPorLivrariaService();
    }

    public VendaService(VendaRepository vendaRepository, LivroPorLivrariaService lplService) {
        this.vendaRepository = vendaRepository;
        this.lplService = lplService;
    }

    /**
     * Realiza a venda de um exemplar para um consumidor.
     *
     * @throws IllegalArgumentException se o exemplar não existir ou não houver
     *                                  estoque disponível
     */
    public Venda realizarVenda(int idLivroPorLivraria, int idConsumidor) {
        LivroPorLivraria lpl = lplService.buscarPorId(idLivroPorLivraria)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Exemplar id=" + idLivroPorLivraria + " não encontrado no acervo."));

        if (lpl.getEstoqueDisponivel() <= 0) {
            throw new IllegalArgumentException(
                    "Estoque indisponível para venda. Estoque atual: " + lpl.getEstoque() +
                            " | Disponível: " + lpl.getEstoqueDisponivel());
        }

        // Decrementa estoque (venda é permanente)
        lplService.decrementarEstoqueVenda(lpl);

        Venda venda = new Venda(lpl.getIdLivraria(), idLivroPorLivraria, idConsumidor, LocalDate.now());
        return vendaRepository.salvar(venda);
    }

    public List<Venda> listarPorLivraria(int idLivraria) {
        return vendaRepository.listarPorLivraria(idLivraria);
    }

    public List<Venda> listarPorConsumidor(int idConsumidor) {
        return vendaRepository.listarPorConsumidor(idConsumidor);
    }
}
