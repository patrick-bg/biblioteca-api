package io.github.patrickbg.bibliotecaapi.dominio.livro.mapper;

import io.github.patrickbg.bibliotecaapi.dominio.autor.model.AutorEntity;
import io.github.patrickbg.bibliotecaapi.dominio.livro.dto.LivroDetalhes;
import io.github.patrickbg.bibliotecaapi.dominio.livro.dto.LivroForm;
import io.github.patrickbg.bibliotecaapi.dominio.livro.model.LivroEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface LivroMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "autor", source = "autor")
    LivroEntity toEntity(LivroForm form, AutorEntity autor);

    @Mapping(target = "autorNome", source = "autor.nome")
    @Mapping(target = "autorId", source = "autor.id")
    LivroDetalhes toDetalhes(LivroEntity entity);

    @Mapping(target = "autor", ignore = true)
    void update(@MappingTarget LivroEntity entity, LivroForm dadosAtualizados);
}
