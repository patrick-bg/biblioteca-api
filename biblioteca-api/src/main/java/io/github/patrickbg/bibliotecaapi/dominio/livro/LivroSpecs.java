package io.github.patrickbg.bibliotecaapi.dominio.livro;

import io.github.patrickbg.bibliotecaapi.dominio.livro.model.*;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.UUID;

public class LivroSpecs {

    public static Specification<LivroEntity> statusEqual(StatusLivro status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }

    public static Specification<LivroEntity> autorEqual(UUID autorId) {
        return (root, query, criteriaBuilder) -> {
            if (autorId == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("autor").get("id"), autorId);
        };
    }

    public static Specification<LivroEntity> anoEqual(Year ano) {
        return (root, query, criteriaBuilder) -> {
            if (ano == null) {
                return null;
            }
            LocalDate inicioAno = ano.atDay(1);
            LocalDate fimAno = ano.atMonth(Month.DECEMBER).atEndOfMonth();

            return criteriaBuilder.between(root.get("dataPublicacao"), inicioAno, fimAno);
        };
    }

    public static Specification<LivroEntity> generoEqual(GeneroLivro genero) {
        return (root, query, criteriaBuilder) -> {
            if (genero == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("genero"), genero);
        };
    }

    public static Specification<LivroEntity> idiomaEqual(IdiomaLivro idioma) {
        return (root, query, criteriaBuilder) -> {
            if (idioma == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("idioma"), idioma);
        };
    }

    public static Specification<LivroEntity> formatoEqual(FormatoLivro formato) {
        return (root, query, criteriaBuilder) -> {
            if (formato == null) {
                return null;
            }
            return criteriaBuilder.equal(root.get("formato"), formato);
        };
    }

    public static Specification<LivroEntity> tituloLike(String titulo) {
        return (root, query, criteriaBuilder) -> {
            if (titulo == null || titulo.isBlank()) {
                return null;
            }

            String padraoBusca = "%" + titulo.trim().toLowerCase() + "%";
            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("titulo")), padraoBusca
            );
        };
    }

}
