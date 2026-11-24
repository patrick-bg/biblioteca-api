package io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.github.patrickbg.bibliotecaapi.dominio.autor.model.GeneroAutor;
import io.github.patrickbg.bibliotecaapi.dominio.autor.model.PaisOrigemAutor;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.FormatoLivro;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.GeneroLivro;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.IdiomaLivro;

import java.time.Year;
import java.util.List;

public record Dashboard(
        @JsonFormat(shape = JsonFormat.Shape.NUMBER)
        Year ano,
        ResumoLeituras resumo,
        List<LivroResumoCard> leiturasAtuais,
        List<LivroResumoCard> leiturasFinalizadasNoAno,
        List<LivroResumoCard> livrosNotaCinco,
        List<LivroResumoCard> livrosBacklog,
        List<LidosGeneroResumo> lidosPorGeneroNoAno,
        List<AutorRankingResumo> autoresMaisLidosNoAno,
        List<AutorRankingResumo> autoresNaEstante,
        List<ContagemResumo<GeneroLivro>> estantePorGenero,
        List<ContagemResumo<GeneroAutor>> estantePorGeneroAutor,
        List<ContagemResumo<FormatoLivro>> estantePorFormato,
        List<ContagemResumo<IdiomaLivro>> estantePorIdioma,
        List<ContagemResumo<PaisOrigemAutor>> estantePorPaisAutor
) {
}
