package com.livraria.model;

/**
 * Entidade intermediária que representa um Livro disponível em uma Livraria
 * específica.
 * Controla o estoque total e o estoque disponível para empréstimo/venda.
 *
 * estoque_disponivel = estoque - empréstimos_ativos - livros_vendidos
 */
public class LivroPorLivraria {

    private int id;
    private int idLivraria;
    private int idLivro;
    private int estoque;
    private int estoqueDisponivel;

    public LivroPorLivraria() {
    }

    public LivroPorLivraria(int idLivraria, int idLivro, int estoque) {
        this.idLivraria = idLivraria;
        this.idLivro = idLivro;
        this.estoque = estoque;
        this.estoqueDisponivel = estoque; // começa igual ao estoque total
    }

    public LivroPorLivraria(int id, int idLivraria, int idLivro, int estoque, int estoqueDisponivel) {
        this.id = id;
        this.idLivraria = idLivraria;
        this.idLivro = idLivro;
        this.estoque = estoque;
        this.estoqueDisponivel = estoqueDisponivel;
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

    public int getIdLivro() {
        return idLivro;
    }

    public void setIdLivro(int idLivro) {
        this.idLivro = idLivro;
    }

    public int getEstoque() {
        return estoque;
    }

    public void setEstoque(int estoque) {
        this.estoque = estoque;
    }

    public int getEstoqueDisponivel() {
        return estoqueDisponivel;
    }

    public void setEstoqueDisponivel(int estoqueDisponivel) {
        this.estoqueDisponivel = estoqueDisponivel;
    }

    @Override
    public String toString() {
        return "LivroPorLivraria{" +
                "id=" + id +
                ", idLivraria=" + idLivraria +
                ", idLivro=" + idLivro +
                ", estoque=" + estoque +
                ", estoqueDisponivel=" + estoqueDisponivel +
                '}';
    }
}
