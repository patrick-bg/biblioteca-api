package io.github.patrickbg.bibliotecaapi.dominio.autor;

import io.github.patrickbg.bibliotecaapi.common.validation.CampoInvalido;
import io.github.patrickbg.bibliotecaapi.common.validation.ValidationResult;
import io.github.patrickbg.bibliotecaapi.dominio.autor.dto.AutorForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AutorValidator {

    @Autowired
    private AutorRepository autorRepository;

    public ValidationResult validar(AutorForm form){
        var result = ValidationResult.novo();

        if(autorRepository.findByNome(form.nome()).isPresent()){
            result.add(new CampoInvalido("nome", "Já cadastrado."));
        }

        return result;
    }
}
