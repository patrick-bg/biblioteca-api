export class ResumoLeituras{
    totalLivrosEstante!: number;
    backlog!: number;
    lendo!: number;
    lidos!: number;
    paginasLidasNoAno!: number;
    paginasLidasTotais!: number;
    mediaNotaNoAno!: number;
    mediaNotaTotal!: number;
    valorTotalInvestido!: number;
}

export class LivroResumoCard {
    id!: string;
    titulo!: string;
    urlCapa!: string | null;
    nota!: number | null;
    paginasLidas!: number;
    paginasTotais!: number;
}


export class AutorRankingResumo{
    autorId!: string;
    nome!: string;
    livrosLidos!: number;
}

export class ContagemResumo<T>{
    categoria!: T;
    quantidade!: number;
}

export class LidosGeneroResumo{
    genero!: string;
    lidos!: number;
}

export class Dashboard{
    ano!: number;
    resumo!: ResumoLeituras;
    leiturasAtuais!: LivroResumoCard[];
    leiturasFinalizadasNoAno!: LivroResumoCard[];
    livrosNotaCinco!: LivroResumoCard[];
    livrosBacklog!: LivroResumoCard[];
    lidosPorGeneroNoAno!: LidosGeneroResumo[];
    autoresMaisLidosNoAno!: AutorRankingResumo[];
    autoresNaEstante!: AutorRankingResumo[];
    estantePorGenero!: ContagemResumo<string>[];
    estantePorGeneroAutor!: ContagemResumo<string>[];
    estantePorFormato!: ContagemResumo<string>[];
    estantePorIdioma!: ContagemResumo<string>[];
    estantePorPaisAutor!: ContagemResumo<string>[];
}