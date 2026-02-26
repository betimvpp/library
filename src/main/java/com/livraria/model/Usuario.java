package com.livraria.model;

import com.livraria.model.enums.Role;
import java.time.LocalDate;

/**
 * Classe abstrata que representa um Usuário do sistema.
 *
 * Conceitos OOP aplicados:
 * - Abstração: classe abstrata com método abstrato exibirPerfil()
 * - Encapsulamento: todos os campos são privados, acessíveis via
 * getters/setters
 * - Herança: Admin e Consumidor herdam desta classe
 */
public abstract class Usuario {

    private int id;
    private String cpf;
    private String nomeCompleto;
    private LocalDate dataNascimento;
    private Role role;
    private String email; // opcional — pode ser null

    protected Usuario(String cpf, String nomeCompleto, LocalDate dataNascimento, Role role) {
        this(cpf, nomeCompleto, dataNascimento, role, null);
    }

    protected Usuario(String cpf, String nomeCompleto, LocalDate dataNascimento, Role role, String email) {
        this.cpf = cpf;
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.role = role;
        this.email = email;
    }

    protected Usuario(int id, String cpf, String nomeCompleto, LocalDate dataNascimento, Role role) {
        this(id, cpf, nomeCompleto, dataNascimento, role, null);
    }

    protected Usuario(int id, String cpf, String nomeCompleto, LocalDate dataNascimento, Role role, String email) {
        this.id = id;
        this.cpf = cpf;
        this.nomeCompleto = nomeCompleto;
        this.dataNascimento = dataNascimento;
        this.role = role;
        this.email = email;
    }

    public abstract void exibirPerfil();

    // --- Getters e Setters ---

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getNomeCompleto() {
        return nomeCompleto;
    }

    public void setNomeCompleto(String nomeCompleto) {
        this.nomeCompleto = nomeCompleto;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", cpf='" + cpf + '\'' +
                ", nomeCompleto='" + nomeCompleto + '\'' +
                ", dataNascimento=" + dataNascimento +
                ", role=" + role.getDescricao() +
                ", email='" + (email != null ? email : "não informado") + '\'' +
                '}';
    }
}
