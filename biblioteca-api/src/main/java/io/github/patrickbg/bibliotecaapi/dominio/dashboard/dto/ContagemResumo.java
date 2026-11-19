package io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto;

public record ContagemResumo<T>(
        T categoria,
        long quantidade
) {
}
