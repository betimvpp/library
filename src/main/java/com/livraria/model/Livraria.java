package com.livraria.model;

/**
 * Entidade que representa uma Livraria no sistema.
 * Encapsulamento: todos os campos são privados, acessíveis por getters/setters.
 */
public class Livraria {

    private int id;
    private int idAdmin; // FK para o Admin que criou a livraria
    private String nome;
    private String cidade;

    public Livraria() {
    }

    // Construtor para criação (sem id)
    public Livraria(int idAdmin, String nome, String cidade) {
        this.idAdmin = idAdmin;
        this.nome = nome;
        this.cidade = cidade;
    }

    // Construtor para recuperação do banco (com id)
    public Livraria(int id, int idAdmin, String nome, String cidade) {
        this.id = id;
        this.idAdmin = idAdmin;
        this.nome = nome;
        this.cidade = cidade;
    }

    // --- Getters e Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdAdmin() {
        return idAdmin;
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @Override
    public String toString() {
        return "Livraria{" +
                "id=" + id +
                ", idAdmin=" + idAdmin +
                ", nome='" + nome + '\'' +
                ", cidade='" + cidade + '\'' +
                '}';
    }
}
