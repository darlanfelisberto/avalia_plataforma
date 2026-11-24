package br.com.avalia.modelo.academico;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UnidadeClosureId implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Column(name = "id_ancestral", nullable = false)
    private Integer idAncestral;

    @Column(name = "id_descendente", nullable = false)
    private Integer idDescendente;

    public UnidadeClosureId() {
    }

    public UnidadeClosureId(Integer idAncestral, Integer idDescendente) {
        this.idAncestral = idAncestral;
        this.idDescendente = idDescendente;
    }

    public Integer getIdAncestral() {
        return idAncestral;
    }

    public void setIdAncestral(Integer idAncestral) {
        this.idAncestral = idAncestral;
    }

    public Integer getIdDescendente() {
        return idDescendente;
    }

    public void setIdDescendente(Integer idDescendente) {
        this.idDescendente = idDescendente;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof UnidadeClosureId other)) {
            return false;
        }
        return Objects.equals(idAncestral, other.idAncestral)
                && Objects.equals(idDescendente, other.idDescendente);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAncestral, idDescendente);
    }

}
