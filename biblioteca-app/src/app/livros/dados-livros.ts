export type StatusLivro = 'LIDO' | 'LENDO' | 'BACKLOG' | 'RELEITURA' | 'ABANDONADO';

export class DadosLivroForm {
    titulo!: string;
    isbn!: string;
    genero!: string;
    preco!: string;
    autorId!: string;
    idioma!: string;
    formato!: string;
    editora!: string;
    dataPublicacao!: string;
    paginasTotais!: number;
    status!: StatusLivro;
    paginasLidas?: number | null;
    nota?: number | null;
    lidoEm?: string;
    urlCapa?: string | null;
}

export class DetalhesLivros{
    id!: string;
    titulo!: string;
    isbn!: string;
    genero!: string;
    preco!: string;
    autorId!: string;
    idioma!: string;
    formato!: string;
    editora!: string;
    dataPublicacao!: Date;
    paginasTotais!: number;
    status!: StatusLivro;
    paginasLidas?: number;
    nota?: number | null;
    lidoEm?: string;
    urlCapa?: string | null;
    autorNome?: string | null;
}

export class FiltroLivro{
    genero?: string;
    titulo?: string;
    autorId?: string;
    idioma?: string;
    formato?: string;
    editora?: string;
    mes?: string;
    ano?: string;
    status?: StatusLivro;
    nota?: number;
    lidoEm?: string
    autorNome?: string;
    autorGenero?: string;
    paisOrigemAutor?: string;
    page?: number;
    size?: number;
}

export class LivroBuscaExterna{
    titulo?: string;
    isbn?: string;
    editora?: string;
    paginasTotais?: number;
    urlCapa?: string;
    autorNome?: string;
}