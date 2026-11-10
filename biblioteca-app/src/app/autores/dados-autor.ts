export type StatusAutor = 'VIVO' | 'FALECIDO' ;

export class DadosAutorForm{
    nome!: string;
    statusAutor!: string;
    dataNascimento!: string;
    dataFalecimento?: Date;
    generoAutor!: string;
    paisOrigem!: string;
}

export class DetalhesAutor{
    id!: string;
    nome!: string;
    statusAutor!: string;
    dataNascimento!: Date;
    dataFalecimento?: Date;
    generoAutor!: string;
    paisOrigem!: string;
        
}