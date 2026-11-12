package io.github.patrickbg.bibliotecaapi.dominio.livro.dto;

import io.github.patrickbg.bibliotecaapi.dominio.livro.model.FormatoLivro;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.GeneroLivro;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.IdiomaLivro;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.StatusLivro;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LivroDetalhes(
        String id,
        String isbn,
        String titulo,
        LocalDate dataPublicacao,
        GeneroLivro genero,
        BigDecimal preco,
        IdiomaLivro idioma,
        FormatoLivro formato,
        String editora,
        Integer paginasTotais,
        Integer paginasLidas,
        StatusLivro status,
        String autorNome,
        String autorId,
        String urlCapa
) {
}
