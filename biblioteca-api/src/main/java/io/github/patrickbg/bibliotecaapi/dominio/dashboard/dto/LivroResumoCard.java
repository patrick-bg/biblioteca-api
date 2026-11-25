package io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto;

import io.github.patrickbg.bibliotecaapi.dominio.livro.model.StatusLivro;

import java.math.BigDecimal;
import java.util.UUID;

public record LivroResumoCard(
        UUID id,
        String titulo,
        String urlCapa,
        BigDecimal nota,
        Integer paginasLidas,
        Integer paginasTotais,
        StatusLivro status
) {
}
