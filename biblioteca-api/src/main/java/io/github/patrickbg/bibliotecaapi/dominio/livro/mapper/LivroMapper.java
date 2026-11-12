package io.github.patrickbg.bibliotecaapi.dominio.livro.mapper;

import io.github.patrickbg.bibliotecaapi.dominio.livro.dto.LivroDetalhes;
import io.github.patrickbg.bibliotecaapi.dominio.livro.dto.LivroForm;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.LivroEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LivroMapper {
    LivroEntity toEntity(LivroForm form);

    LivroDetalhes toDetalhes(LivroEntity entity);

    void update(@MappingTarget LivroEntity entity, LivroForm dadosAtualizados);
}
