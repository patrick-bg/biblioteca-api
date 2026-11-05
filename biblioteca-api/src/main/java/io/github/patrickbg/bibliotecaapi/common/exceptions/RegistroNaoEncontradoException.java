package io.github.patrickbg.bibliotecaapi.common.exceptions;

public class RegistroNaoEncontradoException extends RuntimeException {
    public RegistroNaoEncontradoException() {
        super("Registro não encontrado.");
    }
}
