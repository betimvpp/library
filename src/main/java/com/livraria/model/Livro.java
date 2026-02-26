package com.livraria.model;

import java.time.LocalDate;

/**
 * Entidade que representa um Livro no sistema.
 * Somente um Autor pode criar e atualizar um Livro.
 * Encapsulamento: campos privados acessíveis via getters/setters.
 */
public class Livro {

    private int id;
    private int idAutor;
    private String titulo;
    private LocalDate dataLancamento;
    private LocalDate dataAtualizacao; // nullable — preenchida apenas quando há atualização

    public Livro() {
    }

    // Construtor para publicação (sem id, sem data de atualização)
    public Livro(int idAutor, String titulo, LocalDate dataLancamento) {
        this.idAutor = idAutor;
        this.titulo = titulo;
        this.dataLancamento = dataLancamento;
        this.dataAtualizacao = null;
    }

    // Construtor para recuperação do banco (com id e possível dataAtualizacao)
    public Livro(int id, int idAutor, String titulo, LocalDate dataLancamento, LocalDate dataAtualizacao) {
        this.id = id;
        this.idAutor = idAutor;
        this.titulo = titulo;
        this.dataLancamento = dataLancamento;
        this.dataAtualizacao = dataAtualizacao;
    }

    // --- Getters e Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public LocalDate getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDate dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    @Override
    public String toString() {
        return "Livro{" +
                "id=" + id +
                ", idAutor=" + idAutor +
                ", titulo='" + titulo + '\'' +
                ", dataLancamento=" + dataLancamento +
                ", dataAtualizacao=" + (dataAtualizacao != null ? dataAtualizacao : "nunca atualizado") +
                '}';
    }
}
