package io.github.patrickbg.bibliotecaapi.dominio.livro;

import io.github.patrickbg.bibliotecaapi.dominio.livro.model.LivroEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface LivroRepository extends JpaRepository<LivroEntity, UUID> {

    Optional<LivroEntity> findByTitulo(String titulo);

    @Query("""
        select l
        from LivroEntity l
        where ( :id is null or l.id != :id)
        and l.titulo = :titulo
""")
    List<LivroEntity> findByTituloAndNotId(@Param("titulo") String titulo, @Param("id") UUID id);
}
