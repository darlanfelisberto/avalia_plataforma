package br.com.avalia.modelo.academico;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "cargo", schema = "academico")
@DynamicInsert
public class Cargo implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "denominacao", nullable = false, length = 200)
    private String denominacao;

    @Column(name = "sigla", length = 20)
    private String sigla;

    @Column(name = "categoria", length = 100)
    private String categoria;

    @Column(name = "nivel_funcional", length = 50)
    private String nivelFuncional;

    @Column(name = "inativo")
    private Boolean inativo;

    public Cargo() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDenominacao() {
        return denominacao;
    }

    public void setDenominacao(String denominacao) {
        this.denominacao = denominacao;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNivelFuncional() {
        return nivelFuncional;
    }

    public void setNivelFuncional(String nivelFuncional) {
        this.nivelFuncional = nivelFuncional;
    }

    public Boolean getInativo() {
        return inativo;
    }

    public void setInativo(Boolean inativo) {
        this.inativo = inativo;
    }

}
