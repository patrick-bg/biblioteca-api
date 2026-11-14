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
    dataPublicacao!: Date;
    paginasTotais!: number;
    status!: StatusLivro;
    paginasLidas?: number | null;
    nota?: number | null;
    urlCapa?: string | null;
}

export class DetalhesLivros{
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
    urlCapa?: string | null;
    autorNome?: string | null;
}

export class FiltroLivro{
    genero?: string;
    autorId?: string;
    idioma?: string;
    formato?: string;
    editora?: string;
    mes?: string;
    ano?: string;
    status?: StatusLivro;
    nota?: number;
    autorNome?: string;
    autorGenero?: string;
    paisOrigemAutor?: string;
}