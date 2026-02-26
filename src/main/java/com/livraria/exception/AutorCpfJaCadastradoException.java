package com.livraria.exception;

/**
 * Lançada quando se tenta cadastrar um Autor com CPF já existente.
 * Herda de CpfDuplicadoException (hierarquia de exceptions via herança).
 */
public class AutorCpfJaCadastradoException extends CpfDuplicadoException {

    public AutorCpfJaCadastradoException(String cpf) {
        super("Já existe um autor cadastrado com o CPF: " + cpf, cpf);
    }
}
