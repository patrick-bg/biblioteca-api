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
    lidosPorGeneroNoAno!: LidosGeneroResumo[];
    autoresMaisLidosNoAno!: AutorRankingResumo[];
    estantePorGenero!: ContagemResumo<string>[];
    estantePorGeneroAutor!: ContagemResumo<string>[];
    estantePorFormato!: ContagemResumo<string>[];
    estantePorIdioma!: ContagemResumo<string>[];
    estantePorPaisAutor!: ContagemResumo<string>[];
}