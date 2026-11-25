package br.com.avalia.modelo.academico;

import br.com.avalia.modelo.Model;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import java.io.Serial;

@Entity
@Table(name = "unidade_closure", schema = "academico")
public class UnidadeClosure extends Model<UnidadeClosureId> {

    @Serial
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private UnidadeClosureId id;

    @Column(name = "profundidade", nullable = false)
    private Integer profundidade;

    public UnidadeClosure() {
    }

    @Override
    public UnidadeClosureId getId() {
        return id;
    }

    public void setId(UnidadeClosureId id) {
        this.id = id;
    }

    public Integer getProfundidade() {
        return profundidade;
    }

    public void setProfundidade(Integer profundidade) {
        this.profundidade = profundidade;
    }

}
