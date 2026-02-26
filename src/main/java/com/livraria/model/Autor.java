package com.livraria.model;

import java.time.LocalDate;

/**
 * Entidade que representa um Autor de livros.
 * Encapsulamento: campos privados com getters/setters.
 * Somente um Autor pode criar um Livro no sistema.
 */
public class Autor {

    private int id;
    private String nomeCompleto;
    private String cpf;
    private LocalDate dataNascimento;
    private String email; // opcional — pode ser null

    public Autor() {
    }

    public Autor(String nomeCompleto, String cpf, LocalDate dataNascimento) {
        this(nomeCompleto, cpf, dataNascimento, null);
    }

    public Autor(String nomeCompleto, String cpf, LocalDate dataNascimento, String email) {
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.email = email;
    }

    public Autor(int id, String nomeCompleto, String cpf, LocalDate dataNascimento) {
        this(id, nomeCompleto, cpf, dataNascimento, null);
    }

    public Autor(int id, String nomeCompleto, String cpf, LocalDate dataNascimento, String email) {
        this.id = id;
        this.nomeCompleto = nomeCompleto;
        this.cpf = cpf;
        this.dataNascimento = dataNascimento;
        this.email = email;
    }

    // --- Getters e Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Autor{" +
                "id=" + id +
                ", nomeCompleto='" + nomeCompleto + '\'' +
                ", cpf='" + cpf + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", email='" + (email != null ? email : "não informado") + '\'' +
                '}';
    }
}
