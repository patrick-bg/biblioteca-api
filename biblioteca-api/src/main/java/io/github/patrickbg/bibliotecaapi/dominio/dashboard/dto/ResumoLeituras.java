package io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto;

import java.math.BigDecimal;
import java.util.List;

public record ResumoLeituras(
        Integer backlog,
        Integer lendo,
        Integer lidos,
        long paginasLidasNoAno,
        long paginasLidasTotais,
        double mediaNotaNoAno,
        double mediaNotaTotal,
        BigDecimal valorTotalInvestido
) {
}
