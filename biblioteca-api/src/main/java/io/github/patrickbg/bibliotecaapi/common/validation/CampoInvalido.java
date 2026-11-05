package io.github.patrickbg.bibliotecaapi.common.validation;

public record CampoInvalido(
        String campo,
        String erro
) {
}
