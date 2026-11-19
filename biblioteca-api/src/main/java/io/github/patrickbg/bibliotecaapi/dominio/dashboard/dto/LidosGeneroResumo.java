package io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto;

import io.github.patrickbg.bibliotecaapi.dominio.livro.model.GeneroLivro;

public record LidosGeneroResumo(
        GeneroLivro genero,
        long lidos
) {
}
