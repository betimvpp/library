package com.livraria.model;

import java.time.LocalDate;

/**
 * Entidade que representa uma Venda realizada na livraria.
 * id_livro_por_livraria é necessário para identificar qual exemplar foi vendido
 * e decrementar corretamente o estoque.
 */
public class Venda {

    private int id;
    private int idLivraria;
    private int idLivroPorLivraria; // qual exemplar (livro x livraria) foi vendido
    private int idConsumidor;
    private LocalDate dataVenda;

    public Venda() {
    }

    public Venda(int idLivraria, int idLivroPorLivraria, int idConsumidor, LocalDate dataVenda) {
        this.idLivraria = idLivraria;
        this.idLivroPorLivraria = idLivroPorLivraria;
        this.idConsumidor = idConsumidor;
        this.dataVenda = dataVenda;
    }

    public Venda(int id, int idLivraria, int idLivroPorLivraria, int idConsumidor, LocalDate dataVenda) {
        this.id = id;
        this.idLivraria = idLivraria;
        this.idLivroPorLivraria = idLivroPorLivraria;
        this.idConsumidor = idConsumidor;
        this.dataVenda = dataVenda;
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

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    @Override
    public String toString() {
        return "Venda{" +
                "id=" + id +
                ", idLivraria=" + idLivraria +
                ", idLivroPorLivraria=" + idLivroPorLivraria +
                ", idConsumidor=" + idConsumidor +
                ", dataVenda=" + dataVenda +
                '}';
    }
}
