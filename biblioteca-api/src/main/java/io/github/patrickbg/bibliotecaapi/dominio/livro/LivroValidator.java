package io.github.patrickbg.bibliotecaapi.dominio.livro;

import io.github.patrickbg.bibliotecaapi.common.validation.CampoInvalido;
import io.github.patrickbg.bibliotecaapi.common.validation.ValidationResult;
import io.github.patrickbg.bibliotecaapi.dominio.autor.model.AutorEntity;
import io.github.patrickbg.bibliotecaapi.dominio.livro.dto.LivroForm;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.StatusLivro;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LivroValidator {

    @Autowired
    private LivroRepository livroRepository;

    public ValidationResult validar (LivroForm form, UUID uuid){
        var result = ValidationResult.novo();
        var isListaNaoVazia = !livroRepository.findByTituloAndNotId(form.titulo(), uuid).isEmpty();
        if(isListaNaoVazia){
            result.add(new CampoInvalido("titulo", "Já cadastrado"));
        }

        Integer totais = form.paginasTotais();
        Integer lidas =  form.paginasLidas();

        if (totais != null && totais <= 0) {
            result.add(new CampoInvalido("paginasTotais", "O livro deve ter pelo menos 1 página."));
        }

        if (lidas != null && lidas < 0) {
            result.add(new CampoInvalido("paginasLidas", "O número de páginas lidas não pode ser negativo."));
        }

        if(totais != null && lidas != null && lidas > totais){
            result.add(new CampoInvalido("paginasLidas", "Não pode ser maior que o total de páginas."));
        }

        if(form.status() != null) {
            switch (form.status()) {
                case BACKLOG -> {
                    if (lidas != null && lidas > 0) {
                        result.add(new CampoInvalido("paginasLidas", "Um livro não lido deve ter 0 páginas lidas."));
                    }
                    if (form.nota() != null) {
                        result.add(new CampoInvalido("nota", "Não é possível avaliar um livro que ainda não foi lido."));
                    }
                }

                case LENDO -> {
                    if (lidas == null || lidas <= 0) {
                        result.add(new CampoInvalido("paginasLidas", "Informe pelo menos 1 página lida para livros em andamento."));
                    }
                    if (totais != null  && totais.equals(lidas)) {
                        result.add(new CampoInvalido("status", "Se todas as páginas foram lidas, altere o status para LIDO."));
                    }
                }

                case LIDO ->  {
                    if (form.nota() == null) {
                        result.add(new CampoInvalido("nota","Um livro lido deve possuir uma nota."));
                    }
                    if(totais != null && !totais.equals(lidas)) {
                        result.add(new CampoInvalido("paginasLidas", "Para marcar como LIDO, o número de páginas lidas deve ser igual ao total."));
                    }
                }
            }
        }

        return result;
    }
}
