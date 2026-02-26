package com.livraria.exception;

/**
 * Lançada quando se tenta cadastrar um Usuário com CPF já existente.
 * Herda de CpfDuplicadoException (hierarquia de exceptions via herança).
 */
public class CpfJaCadastradoException extends CpfDuplicadoException {

    public CpfJaCadastradoException(String cpf) {
        super("Já existe um usuário cadastrado com o CPF: " + cpf, cpf);
    }
}
