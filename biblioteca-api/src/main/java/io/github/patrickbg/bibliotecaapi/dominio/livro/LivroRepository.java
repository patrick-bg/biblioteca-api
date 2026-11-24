package io.github.patrickbg.bibliotecaapi.dominio.livro;

import io.github.patrickbg.bibliotecaapi.dominio.autor.model.GeneroAutor;
import io.github.patrickbg.bibliotecaapi.dominio.autor.model.PaisOrigemAutor;
import io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.AutorRankingResumo;
import io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.ContagemResumo;
import io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.LidosGeneroResumo;
import io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.LivroResumoCard;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.*;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LivroRepository
        extends JpaRepository<LivroEntity, UUID>, JpaSpecificationExecutor<LivroEntity> {

    Optional<LivroEntity> findByTitulo(String titulo);

    @Query("""
        select l
        from LivroEntity l
        where ( :id is null or l.id != :id)
        and l.titulo = :titulo
""")
    List<LivroEntity> findByTituloAndNotId(@Param("titulo") String titulo, @Param("id") UUID id);

    boolean existsByAutorId(UUID id);

    // 1. Total de livros por STATUS
    Integer countByStatus(StatusLivro statusLivro);

    // 2. Total de livros LIDOS no ano informado
    Integer countByStatusAndLidoEmBetween(StatusLivro status, YearMonth inicio, YearMonth fim);

    // 3. Lidos no ano agrupados por Gênero do Livro
    @Query("""
            SELECT NEW io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.LidosGeneroResumo(l.genero, COUNT(l))
            FROM LivroEntity l
            WHERE l.status = :status
              AND l.lidoEm BETWEEN :inicio AND :fim
            GROUP BY l.genero
            ORDER BY COUNT(l) DESC
            """)
    List<LidosGeneroResumo> buscarLidosPorGeneroNoPeriodo(
            @Param("status") StatusLivro status,
            @Param("inicio") YearMonth inicio,
            @Param("fim") YearMonth fim);

    // 4. Lidos no ano agrupados por Gênero do Autor
    @Query("""
            SELECT NEW io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.ContagemResumo(l.autor.generoAutor, COUNT(l))
            FROM LivroEntity l
            WHERE l.status = :status
              AND l.lidoEm BETWEEN :inicio AND :fim
            GROUP BY l.autor.generoAutor
            ORDER BY COUNT(l) DESC
            """)
    List<LidosGeneroResumo> buscarLidosPorGeneroAutorNoPeriodo(
            @Param("status") StatusLivro status,
            @Param("inicio") YearMonth inicio,
            @Param("fim") YearMonth fim);

    // 5. Lidos no ano agrupados por Formato (Físico ou E-book)
    @Query("""
            SELECT NEW io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.ContagemResumo(l.formato, COUNT(l))
            FROM LivroEntity l
            WHERE l.status = :status
              AND l.lidoEm BETWEEN :inicio AND :fim
            GROUP BY l.formato
            ORDER BY COUNT(l) DESC
            """)
    List<ContagemResumo<FormatoLivro>> buscarLidosPorFormatoNoPeriodo(
            @Param("status") StatusLivro status,
            @Param("inicio") YearMonth inicio,
            @Param("fim") YearMonth fim);

    // 6. Lidos no ano agrupados por Idioma
    @Query("""
            SELECT NEW io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.ContagemResumo(l.idioma, COUNT(l))
            FROM LivroEntity l
            WHERE l.status = :status
              AND l.lidoEm BETWEEN :inicio AND :fim
            GROUP BY l.idioma
            ORDER BY COUNT(l) DESC
            """)
    List<ContagemResumo<IdiomaLivro>> buscarLidosPorIdiomaNoPeriodo(
            @Param("status") StatusLivro status,
            @Param("inicio") YearMonth inicio,
            @Param("fim") YearMonth fim);

    // 7. Lidos no ano agrupados por País de Origem do Autor
    @Query("""
            SELECT NEW io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.ContagemResumo(l.autor.paisOrigem, COUNT(l))
            FROM LivroEntity l
            WHERE l.status = :status
              AND l.lidoEm BETWEEN :inicio AND :fim
            GROUP BY l.autor.paisOrigem
            ORDER BY COUNT(l) DESC
            """)
    List<ContagemResumo<PaisOrigemAutor>> buscarLidosPorPaisAutorNoPeriodo(
            @Param("status") StatusLivro status,
            @Param("inicio") YearMonth inicio,
            @Param("fim") YearMonth fim);


    @Query("""
            SELECT NEW io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.AutorRankingResumo(l.autor.id, l.autor.nome, COUNT(l))
            FROM LivroEntity l
            WHERE l.status = :status
              AND l.lidoEm BETWEEN :inicio AND :fim
            GROUP BY l.autor.id, l.autor.nome
            ORDER BY COUNT(l) DESC
            """)
    List<AutorRankingResumo> buscarRankingAutoresNoPeriodo(
            @Param("status") StatusLivro status,
            @Param("inicio") YearMonth inicio,
            @Param("fim") YearMonth fim,
            Pageable pageable);

    // --- MÉTRICAS GERAIS DA ESTANTE INTEIRA ---

    @Query("""
            SELECT NEW io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.AutorRankingResumo(
                l.autor.id, l.autor.nome, COUNT(l)
            )
            FROM LivroEntity l
            WHERE l.status IN ('LIDO', 'LENDO', 'BACKLOG')
            GROUP BY l.autor.id, l.autor.nome
            ORDER BY COUNT(l) DESC
            """)
    List<AutorRankingResumo> buscarRankingAutoresEstante(Pageable pageable);

    @Query("""
            SELECT NEW io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.ContagemResumo(l.status, COUNT(l))
            FROM LivroEntity l
            GROUP BY l.status
            """)
    List<ContagemResumo<StatusLivro>> contarPorStatusGeral();

    @Query("""
            SELECT NEW io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.ContagemResumo(l.genero, COUNT(l))
            FROM LivroEntity l
            GROUP BY l.genero
            ORDER BY COUNT(l) DESC
            """)
    List<ContagemResumo<GeneroLivro>> contarEstantePorGenero();

    // Metodo correto para buscar o gênero do autor na estante inteira
    @Query("""
            SELECT NEW io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.ContagemResumo(l.autor.generoAutor, COUNT(l))
            FROM LivroEntity l
            GROUP BY l.autor.generoAutor
            ORDER BY COUNT(l) DESC
            """)
    List<ContagemResumo<GeneroAutor>> contarEstantePorGeneroAutor();

    @Query("""
            SELECT NEW io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.ContagemResumo(l.formato, COUNT(l))
            FROM LivroEntity l
            GROUP BY l.formato
            ORDER BY COUNT(l) DESC
            """)
    List<ContagemResumo<FormatoLivro>> contarEstantePorFormato();

    @Query("""
            SELECT NEW io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.ContagemResumo(l.idioma, COUNT(l))
            FROM LivroEntity l
            GROUP BY l.idioma
            ORDER BY COUNT(l) DESC
            """)
    List<ContagemResumo<IdiomaLivro>> contarEstantePorIdioma();

    @Query("""
            SELECT NEW io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.ContagemResumo(l.autor.paisOrigem, COUNT(l))
            FROM LivroEntity l
            GROUP BY l.autor.paisOrigem
            ORDER BY COUNT(l) DESC
            """)
    List<ContagemResumo<PaisOrigemAutor>> contarEstantePorPaisAutor();

    // --- PÁGINAS LIDAS E MÉDIA DE NOTAS ---

    @Query("""
            SELECT COALESCE(SUM(l.paginasTotais), 0L)
            FROM LivroEntity l
            WHERE l.status = :status
              AND l.lidoEm BETWEEN :inicio AND :fim
            """)
    Long somarPaginasLidasNoPeriodo(
            @Param("status") StatusLivro status,
            @Param("inicio") YearMonth inicio,
            @Param("fim") YearMonth fim);

    @Query("""
            SELECT COALESCE(SUM(
                CASE 
                    WHEN l.status = 'LIDO' THEN l.paginasTotais 
                    WHEN l.status = 'LENDO' THEN COALESCE(l.paginasLidas, 0) 
                    ELSE 0 
                END
            ), 0L)
            FROM LivroEntity l
            """)
    Long somarPaginasLidasTotal();

    @Query("""
            SELECT COALESCE(AVG(l.nota), 0.0)
            FROM LivroEntity l
            WHERE l.status = :status
              AND l.lidoEm BETWEEN :inicio AND :fim
              AND l.nota IS NOT NULL
            """)
    Double calcularMediaNotaNoPeriodo(
            @Param("status") StatusLivro status,
            @Param("inicio") YearMonth inicio,
            @Param("fim") YearMonth fim);

    @Query("""
            SELECT COALESCE(AVG(l.nota), 0.0)
            FROM LivroEntity l
            WHERE l.status = :status
              AND l.nota IS NOT NULL
            """)
    Double calcularMediaNotaTotal(@Param("status") StatusLivro status);

    // --- VALOR TOTAL INVESTIDO ---

    @Query("SELECT COALESCE(SUM(l.preco), 0) FROM LivroEntity l")
    BigDecimal somarValorTotalInvestido();

    // --- DADOS DE LIVROS ---
    @Query("""
            SELECT NEW io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.LivroResumoCard(
                l.id, l.titulo, l.urlCapa, l.nota, l.paginasLidas, l.paginasTotais
            )
            FROM LivroEntity l
            WHERE l.status = 'LENDO'
            ORDER BY l.dataAtualizacao DESC
            """)
    List<LivroResumoCard> buscarLeiturasAtuais();

    @Query("""
            SELECT NEW io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.LivroResumoCard(
                l.id, l.titulo, l.urlCapa, l.nota, l.paginasLidas, l.paginasTotais
            )
            FROM LivroEntity l
            WHERE l.status = 'LIDO'
              AND l.lidoEm BETWEEN :inicio AND :fim
            ORDER BY l.lidoEm DESC, l.dataAtualizacao DESC
            """)
    List<LivroResumoCard> buscarLivrosLidosNoPeriodo(
            @Param("inicio") YearMonth inicio,
            @Param("fim") YearMonth fim);

    @Query("""
            SELECT NEW io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.LivroResumoCard(
                l.id, l.titulo, l.urlCapa, l.nota, l.paginasLidas, l.paginasTotais
            )
            FROM LivroEntity l
            WHERE l.nota = 5.0
            ORDER BY l.lidoEm DESC, l.titulo ASC
            """)
    List<LivroResumoCard> buscarLivrosNotaCinco();

    @Query("""
            SELECT NEW io.github.patrickbg.bibliotecaapi.dominio.dashboard.dto.LivroResumoCard(
                l.id, l.titulo, l.urlCapa, l.nota, l.paginasLidas, l.paginasTotais
            )
            FROM LivroEntity l
            WHERE l.status = 'BACKLOG'
            ORDER BY l.dataAtualizacao DESC
            """)
    List<LivroResumoCard> buscarLivrosBacklog();


}



