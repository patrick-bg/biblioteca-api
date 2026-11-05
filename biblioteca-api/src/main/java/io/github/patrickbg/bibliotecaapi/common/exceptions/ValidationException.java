package io.github.patrickbg.bibliotecaapi.common.exceptions;

import io.github.patrickbg.bibliotecaapi.common.validation.CampoInvalido;

import java.util.List;

public class ValidationException extends RuntimeException {

    public final List<CampoInvalido> campoInvalidos;


    public ValidationException(List<CampoInvalido> campoInvalidos) {
        super("Erro de validação.");
        this.campoInvalidos = campoInvalidos;
    }

    public List<CampoInvalido> getCampoInvalidos() {
        return campoInvalidos;
    }
}
