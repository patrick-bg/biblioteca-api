package io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record LivroResumoCard(
        UUID id,
        String titulo,
        String urlCapa,
        BigDecimal nota,
        Integer paginasLidas,
        Integer paginasTotais
) {
}
