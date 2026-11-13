package io.github.patrickbg.bibliotecaapi.dominio.livro;

import io.github.patrickbg.bibliotecaapi.common.exceptions.RegistroNaoEncontradoException;
import io.github.patrickbg.bibliotecaapi.common.exceptions.ValidationException;
import io.github.patrickbg.bibliotecaapi.dominio.autor.AutorRepository;
import io.github.patrickbg.bibliotecaapi.dominio.autor.model.AutorEntity;
import io.github.patrickbg.bibliotecaapi.dominio.livro.dto.LivroDetalhes;
import io.github.patrickbg.bibliotecaapi.dominio.livro.dto.LivroForm;
import io.github.patrickbg.bibliotecaapi.dominio.livro.mapper.LivroMapper;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.LivroEntity;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class LivroService {

    @Autowired
    private LivroRepository livroRepository;

    @Autowired
    private LivroMapper livroMapper;

    @Autowired
    private AutorRepository autorRepository;

    @Autowired
    private LivroValidator livroValidator;

    public LivroDetalhes criar (LivroForm livroForm){
        AutorEntity autor = null;
        if(livroForm.autorId() != null){
            autor = autorRepository.findById(livroForm.autorId()).orElseThrow(RegistroNaoEncontradoException::new);
        }

        var result = livroValidator.validar(livroForm, null);

        if(result.isInvalido()){
            throw new ValidationException(result.getCamposInvalidos());
        }

        LivroEntity entity = livroMapper.toEntity(livroForm,autor);
        livroRepository.save(entity);

        return livroMapper.toDetalhes(entity);
    }

    public LivroDetalhes obterDetalhes(UUID id){
        return livroRepository.findById(id)
                .map(livroMapper::toDetalhes)
                .orElseThrow(RegistroNaoEncontradoException::new);
    }

    public void atualizar(UUID id, @Valid LivroForm livroAtualizado){
        AutorEntity autor = null;
        if(livroAtualizado.autorId() != null){
            autor = autorRepository.findById(livroAtualizado.autorId()).orElseThrow(RegistroNaoEncontradoException::new);
        }

        var entity = livroRepository.findById(id).orElseThrow(RegistroNaoEncontradoException::new);
        var result = livroValidator.validar(livroAtualizado, id);

        if(result.isInvalido()){
            throw new ValidationException(result.getCamposInvalidos());
        }

        livroMapper.update(entity, livroAtualizado);
        livroRepository.save(entity);
    }

    public Page<LivroDetalhes> listar (PageRequest pageRequest){
        return livroRepository.findAll(pageRequest).map(livroMapper::toDetalhes);
    }

    public void deletar(UUID id){
        var livro =  livroRepository.findById(id).orElseThrow(RegistroNaoEncontradoException::new);
        livroRepository.delete(livro);
    }

}
