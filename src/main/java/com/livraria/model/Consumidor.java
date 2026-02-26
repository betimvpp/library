package com.livraria.model;

import com.livraria.model.enums.Role;
import java.time.LocalDate;

/**
 * Representa um usuário Consumidor (cliente da livraria).
 * Herança + Polimorfismo: sobrescreve exibirPerfil() com comportamento de
 * Consumidor.
 */
public class Consumidor extends Usuario {

    public Consumidor(String cpf, String nomeCompleto, LocalDate dataNascimento) {
        super(cpf, nomeCompleto, dataNascimento, Role.CONSUMIDOR);
    }

    public Consumidor(String cpf, String nomeCompleto, LocalDate dataNascimento, String email) {
        super(cpf, nomeCompleto, dataNascimento, Role.CONSUMIDOR, email);
    }

    public Consumidor(int id, String cpf, String nomeCompleto, LocalDate dataNascimento) {
        super(id, cpf, nomeCompleto, dataNascimento, Role.CONSUMIDOR);
    }

    public Consumidor(int id, String cpf, String nomeCompleto, LocalDate dataNascimento, String email) {
        super(id, cpf, nomeCompleto, dataNascimento, Role.CONSUMIDOR, email);
    }

    @Override
    public void exibirPerfil() {
        System.out.println("╔══════════════════════════════╗");
        System.out.println("║     PERFIL: CONSUMIDOR       ║");
        System.out.println("╠══════════════════════════════╣");
        System.out.println("║ ID   : " + getId());
        System.out.println("║ CPF  : " + getCpf());
        System.out.println("║ Nome : " + getNomeCompleto());
        System.out.println("║ Nasc.: " + getDataNascimento());
        System.out.println("║ Email: " + (getEmail() != null ? getEmail() : "não informado"));
        System.out.println("║ Role : " + getRole().getDescricao());
        System.out.println("╚══════════════════════════════╝");
    }
}
