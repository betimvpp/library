package com.livraria.model;

import java.time.LocalDate;

/**
 * Entidade que representa um Empréstimo de livro.
 * dataDevolucao é null enquanto o livro não foi devolvido.
 * Só pode ser realizado se houver estoqueDisponivel > 0.
 */
public class Emprestimo {

    private int id;
    private int idLivraria;
    private int idLivroPorLivraria;
    private int idConsumidor;
    private LocalDate dataEmprestimo;
    private LocalDate dataDevolucao; // nullable — null indica que ainda não foi devolvido

    public Emprestimo() {
    }

    public Emprestimo(int idLivraria, int idLivroPorLivraria, int idConsumidor, LocalDate dataEmprestimo) {
        this.idLivraria = idLivraria;
        this.idLivroPorLivraria = idLivroPorLivraria;
        this.idConsumidor = idConsumidor;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = null;
    }

    public Emprestimo(int id, int idLivraria, int idLivroPorLivraria, int idConsumidor,
            LocalDate dataEmprestimo, LocalDate dataDevolucao) {
        this.id = id;
        this.idLivraria = idLivraria;
        this.idLivroPorLivraria = idLivroPorLivraria;
        this.idConsumidor = idConsumidor;
        this.dataEmprestimo = dataEmprestimo;
        this.dataDevolucao = dataDevolucao;
    }

    public boolean estaAtivo() {
        return dataDevolucao == null;
    }

    // --- Getters e Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdLivraria() {
        return idLivraria;
    }

    public void setIdLivraria(int idLivraria) {
        this.idLivraria = idLivraria;
    }

    public int getIdLivroPorLivraria() {
        return idLivroPorLivraria;
    }

    public void setIdLivroPorLivraria(int idLivroPorLivraria) {
        this.idLivroPorLivraria = idLivroPorLivraria;
    }

    public int getIdConsumidor() {
        return idConsumidor;
    }

    public void setIdConsumidor(int idConsumidor) {
        this.idConsumidor = idConsumidor;
    }

    public LocalDate getDataEmprestimo() {
        return dataEmprestimo;
    }

    public void setDataEmprestimo(LocalDate dataEmprestimo) {
        this.dataEmprestimo = dataEmprestimo;
    }

    public LocalDate getDataDevolucao() {
        return dataDevolucao;
    }

    public void setDataDevolucao(LocalDate dataDevolucao) {
        this.dataDevolucao = dataDevolucao;
    }

    @Override
    public String toString() {
        return "Emprestimo{" +
                "id=" + id +
                ", idLivroPorLivraria=" + idLivroPorLivraria +
                ", idConsumidor=" + idConsumidor +
                ", dataEmprestimo=" + dataEmprestimo +
                ", dataDevolucao=" + (dataDevolucao != null ? dataDevolucao : "pendente") +
                '}';
    }
}
