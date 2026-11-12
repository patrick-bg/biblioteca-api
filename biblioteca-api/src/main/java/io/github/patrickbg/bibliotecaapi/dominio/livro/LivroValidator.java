package io.github.patrickbg.bibliotecaapi.dominio.livro;

import io.github.patrickbg.bibliotecaapi.common.validation.CampoInvalido;
import io.github.patrickbg.bibliotecaapi.common.validation.ValidationResult;
import io.github.patrickbg.bibliotecaapi.dominio.autor.model.AutorEntity;
import io.github.patrickbg.bibliotecaapi.dominio.livro.dto.LivroForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class LivroValidator {

    @Autowired
    private LivroRepository livroRepository;

    public ValidationResult validar (LivroForm form, AutorEntity autor){
        var result = ValidationResult.novo();

        if(autor == null){
            result.add(new CampoInvalido("autorId", "Autor não encontrado."));
        }

        assert autor != null;
        var isListaNaoVazia = !livroRepository.findByTituloAndNotId(form.titulo(), autor.getId()).isEmpty();

        if(isListaNaoVazia){
            result.add(new CampoInvalido("titulo", "Já cadastrado"));
        }

        return result;
    }
}
