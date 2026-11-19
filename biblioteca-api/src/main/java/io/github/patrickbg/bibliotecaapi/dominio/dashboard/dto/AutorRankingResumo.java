package io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto;

import java.util.UUID;

public record AutorRankingResumo(
        UUID autorId,
        String nome,
        long livrosLidos
) {
}
