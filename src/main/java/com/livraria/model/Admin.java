package com.livraria.model;

import com.livraria.model.enums.Role;
import java.time.LocalDate;

/**
 * Representa um usuário com permissões de Administrador.
 * Herança + Polimorfismo: sobrescreve exibirPerfil() com comportamento de
 * Admin.
 */
public class Admin extends Usuario {

    public Admin(String cpf, String nomeCompleto, LocalDate dataNascimento) {
        super(cpf, nomeCompleto, dataNascimento, Role.ADMIN);
    }

    public Admin(String cpf, String nomeCompleto, LocalDate dataNascimento, String email) {
        super(cpf, nomeCompleto, dataNascimento, Role.ADMIN, email);
    }

    public Admin(int id, String cpf, String nomeCompleto, LocalDate dataNascimento) {
        super(id, cpf, nomeCompleto, dataNascimento, Role.ADMIN);
    }

    public Admin(int id, String cpf, String nomeCompleto, LocalDate dataNascimento, String email) {
        super(id, cpf, nomeCompleto, dataNascimento, Role.ADMIN, email);
    }

    @Override
    public void exibirPerfil() {
        System.out.println("==============================");
        System.out.println("  PERFIL: ADMINISTRADOR");
        System.out.println("------------------------------");
        System.out.println("  ID   : " + getId());
        System.out.println("  CPF  : " + getCpf());
        System.out.println("  Nome : " + getNomeCompleto());
        System.out.println("  Nasc.: " + getDataNascimento());
        System.out.println("  Email: " + (getEmail() != null ? getEmail() : "não informado"));
        System.out.println("  Role : " + getRole().getDescricao());
        System.out.println("==============================");
    }
}
