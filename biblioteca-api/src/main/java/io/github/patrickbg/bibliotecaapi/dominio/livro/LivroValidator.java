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

        if (form.paginasTotais() != null && form.paginasTotais() <= 0) {
            result.add(new CampoInvalido("paginasTotais", "O livro deve ter pelo menos 1 página."));
        }

        if (form.paginasLidas() != null && form.paginasLidas() > form.paginasTotais()) {
            result.add(new CampoInvalido("paginasLidas", "Não pode ser maior que o total de páginas."));
        }

        if (form.status() == StatusLivro.BACKLOG) {
            if (form.paginasLidas() != null && form.paginasLidas() > 0) {
                result.add(new CampoInvalido("paginasLidas", "Um livro não lido deve ter 0 páginas lidas."));
            }
            if (form.nota() != null) {
                result.add(new CampoInvalido("nota", "Não é possível avaliar um livro que ainda não foi lido."));
            }
        }

        if (form.status() == StatusLivro.LENDO) {
            if (form.paginasLidas() == null || form.paginasLidas() <= 0) {
                result.add(new CampoInvalido("paginasLidas", "Informe pelo menos 1 página lida para livros em andamento."));
            }
            if (form.paginasTotais() != null && form.paginasLidas() != null && form.paginasLidas().equals(form.paginasTotais())) {
                result.add(new CampoInvalido("status", "Se todas as páginas foram lidas, altere o status para LIDO."));
            }
        }

        return result;
    }



}
