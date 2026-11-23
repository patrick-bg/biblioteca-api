package io.github.patrickbg.bibliotecaapi.dominio.dashboard;

import io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.ContagemResumo;
import io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.Dashboard;
import io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.ResumoLeituras;
import io.github.patrickbg.bibliotecaapi.dominio.livro.LivroRepository;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.StatusLivro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Year;
import java.time.YearMonth;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    @Autowired
    private LivroRepository livroRepository;

    public Dashboard obterDashboardPorAno(Year ano){
        Year anoDashboardAtual = (ano != null) ? ano : Year.now();
        YearMonth inicioAno = anoDashboardAtual.atMonth(1);
        YearMonth fimAno = anoDashboardAtual.atMonth(12);

        // 1. Resumo de status (Backlog e Lendo = estante inteira; Lidos = filtrado por ano)
        Map<StatusLivro,Integer> contagemPorStatus = livroRepository.contarPorStatusGeral()
                .stream()
                .collect(Collectors.toMap(
                        ContagemResumo::categoria,
                        resumo -> (int) resumo.quantidade()
                ));

        int totalLivrosEstante = contagemPorStatus.values()
                .stream().mapToInt(Integer::intValue).sum();

        int backlog = contagemPorStatus.getOrDefault(StatusLivro.BACKLOG, 0);
        int lendo = contagemPorStatus.getOrDefault(StatusLivro.LENDO, 0);
        Integer lidosNoAno = livroRepository.countByStatusAndLidoEmBetween(StatusLivro.LIDO, inicioAno, fimAno);

        int lidos = (lidosNoAno != null) ? lidosNoAno : 0;

        long paginasNoAno = livroRepository.somarPaginasLidasNoPeriodo(StatusLivro.LIDO,  inicioAno, fimAno);
        long paginasTotal = livroRepository.somarPaginasLidasTotal();

        double mediaNotaNoAno = arredondarNota(
                livroRepository.calcularMediaNotaNoPeriodo(StatusLivro.LIDO, inicioAno, fimAno)
        );

        double mediaNotaTotal = arredondarNota(
                livroRepository.calcularMediaNotaTotal(StatusLivro.LIDO)
        );

        BigDecimal valorTotalInvestido = livroRepository.somarValorTotalInvestido();

        ResumoLeituras resumoLeituras = new ResumoLeituras(
                totalLivrosEstante,
                backlog,
                lendo,
                lidos,
                paginasNoAno,
                paginasTotal,
                mediaNotaNoAno,
                mediaNotaTotal,
                valorTotalInvestido);

        var lidosPorGeneroNoAno = livroRepository.buscarLidosPorGeneroNoPeriodo(StatusLivro.LIDO, inicioAno, fimAno);
        var autoresMaisLidosNoAno = livroRepository.buscarRankingAutoresNoPeriodo(
                StatusLivro.LIDO,inicioAno,fimAno, PageRequest.of(0, 5)
        );

        var estantePorGenero = livroRepository.contarEstantePorGenero();
        var estantePorGeneroAutor = livroRepository.contarEstantePorGeneroAutor();
        var estantePorFormato = livroRepository.contarEstantePorFormato();
        var estantePorIdioma = livroRepository.contarEstantePorIdioma();
        var estantePorPaisAutor = livroRepository.contarEstantePorPaisAutor();

        return new Dashboard(
                anoDashboardAtual,
                resumoLeituras,
                lidosPorGeneroNoAno,
                autoresMaisLidosNoAno,
                estantePorGenero,
                estantePorGeneroAutor,
                estantePorFormato,
                estantePorIdioma,
                estantePorPaisAutor
        );
    }

    private double arredondarNota(Double nota){
        if (nota == null || nota == 0.0){
            return 0.0;
        }
        return BigDecimal.valueOf(nota)
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
