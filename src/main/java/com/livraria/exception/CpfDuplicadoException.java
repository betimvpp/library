package com.livraria.exception;

/**
 * Exception base para CPF duplicado no sistema.
 * Subclasses especificam a entidade afetada (Usuário, Autor, etc.).
 *
 * Conceito OOP: Herança — permite catch genérico ou específico.
 */
public class CpfDuplicadoException extends RuntimeException {

    private final String cpf;

    protected CpfDuplicadoException(String mensagem, String cpf) {
        super(mensagem);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }
}
