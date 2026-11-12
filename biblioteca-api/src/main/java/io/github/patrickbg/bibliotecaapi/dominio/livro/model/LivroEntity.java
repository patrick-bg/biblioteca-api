package io.github.patrickbg.bibliotecaapi.dominio.livro.model;

import io.github.patrickbg.bibliotecaapi.dominio.autor.model.AutorEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "livro")
@Getter
@Setter
@ToString(exclude = "autor")
public class LivroEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "isbn", length = 20, nullable = false)
    private String isbn;

    @Column(name = "titulo", nullable = false, length = 100)
    private String titulo;

    @Column(name = "data_publicacao", nullable = false)
    private LocalDate dataPublicacao;

    @Column(name = "genero", nullable = false)
    @Enumerated(EnumType.STRING)
    private GeneroLivro genero;

    @Column(name = "preco", nullable = false, precision = 15, scale = 2)
    private BigDecimal preco;

    @ManyToOne
    @JoinColumn(name = "autor", nullable = false)
    private AutorEntity autor;

    @Column(name = "idioma", nullable = false)
    private String idioma;

    @Enumerated(EnumType.STRING)
    @Column(name = "formato", nullable = false)
    private FormatoLivro formato;

    @Column(name = "editora", nullable = false)
    private String editora;

    @Column(name = "paginasTotais", nullable = false)
    private Integer paginasTotais;

    @Column(name = "paginasLidas")
    private Integer paginasLidas = 0;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private StatusLivro status;

    @Column(name = "data_atualizacao")
    private LocalDate dataAtualizacao;

    @Column(name = "tipoCapa")
    private String tipoCapa;

    @Basic(fetch = FetchType.LAZY)
    @JdbcTypeCode(SqlTypes.VARBINARY)
    @Column(name = "capa")
    private byte[] capa;

    @PrePersist
    public void prePersist(){
        setDataAtualizacao(LocalDate.now());
    }

}
