import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'enumLabel',
  standalone: true,
})
export class EnumLabelPipe implements PipeTransform {
  // Dicionário centralizado com os labels acentuados e formatados
  private readonly labels: Record<string, string> = {
    // Gêneros de Livro
    FICCAO_CIENTIFICA: 'Ficção Científica',
    FANTASIA: 'Fantasia',
    ROMANCE: 'Romance',
    SUSPENSE: 'Suspense',
    TERROR: 'Terror',
    BIOGRAFIA: 'Biografia',
    ENSAIO_HISTORICO: 'Ensaio Histórico',
    DRAMA: 'Drama',
    MANGA: 'Mangá',
    CIENCIAS_POLITICAS: 'Ciências Políticas',
    CIENCIAS_BIOLOGICAS: 'Ciências Biológicas',
    PSICOLOGIA: 'Psicologia',

    // Formatos
    FISICO: 'Físico',
    EBOOK: 'E-book',
    AUDIOBOOK: 'Audiobook',

    // Idiomas
    PORTUGUES: 'Português',
    INGLES: 'Inglês',
    ESPANHOL: 'Espanhol',
    FRANCES: 'Francês',
    ALEMAO: 'Alemão',

    // Gênero do Autor
    MASCULINO: 'Masculino',
    FEMININO: 'Feminino',
    NEUTRO: 'Não-binário',

    // Países de Origem
    BRASIL: 'Brasil',
    EUA: 'Estados Unidos',
    REINO_UNIDO: 'Reino Unido',
    JAPAO: 'Japão',
    CHINA: 'China',
    ALEMANHA: 'Alemanha',
    PORTUGAL: 'Portugal',
    ESPANHA: 'Espanha',
    GRECIA: 'Grécia',
    HOLANDA: 'Holanda',
    RUSSIA: 'Rússia',

    // Status
    LIDO: 'Lido',
    LENDO: 'Lendo',
    BACKLOG: 'Na Fila',
    RELEITURA: 'Releitura',
    ABANDONADO: 'Abandonado',
  };

  transform(value: string | null | undefined): string {
    if (!value) return '';

    // 1. Se existir no dicionário acima, retorna o texto exato com acentuação
    if (this.labels[value]) {
      return this.labels[value];
    }

    // 2. Fallback automático: converte "OUTRO_VALOR" em "Outro Valor"
    return value
      .toLowerCase()
      .split('_')
      .map((palavra) => palavra.charAt(0).toUpperCase() + palavra.slice(1))
      .join(' ');
  }
}