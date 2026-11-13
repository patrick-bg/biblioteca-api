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

    public ValidationResult validar (LivroForm form, UUID uuid){
        var result = ValidationResult.novo();

        var isListaNaoVazia = !livroRepository.findByTituloAndNotId(form.titulo(), uuid).isEmpty();

        if(isListaNaoVazia){
            result.add(new CampoInvalido("titulo", "Já cadastrado"));
        }

        return result;
    }

}
