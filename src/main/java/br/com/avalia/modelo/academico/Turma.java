package br.com.avalia.modelo.academico;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "turma", schema = "academico")
@DynamicInsert
public class Turma implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_componente_curricular", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "turma_id_componente_curricular_fkey"))
    private ComponenteCurricular componenteCurricular;

    @Column(name = "codigo_turma", nullable = false, length = 20)
    private String codigoTurma;

    @Column(name = "ano", nullable = false)
    private Integer ano;

    @Column(name = "periodo", nullable = false)
    private Integer periodo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso", referencedColumnName = "id", foreignKey = @ForeignKey(name = "turma_id_curso_fkey"))
    private Curso curso;

    @Column(name = "capacidade")
    private Integer capacidade;

    @Column(name = "situacao", length = 50)
    private String situacao;

    @Column(name = "id_unidade")
    private Integer idUnidade;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    public Turma() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ComponenteCurricular getComponenteCurricular() {
        return componenteCurricular;
    }

    public void setComponenteCurricular(ComponenteCurricular componenteCurricular) {
        this.componenteCurricular = componenteCurricular;
    }

    public String getCodigoTurma() {
        return codigoTurma;
    }

    public void setCodigoTurma(String codigoTurma) {
        this.codigoTurma = codigoTurma;
    }

    public Integer getAno() {
        return ano;
    }

    public void setAno(Integer ano) {
        this.ano = ano;
    }

    public Integer getPeriodo() {
        return periodo;
    }

    public void setPeriodo(Integer periodo) {
        this.periodo = periodo;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Integer getIdUnidade() {
        return idUnidade;
    }

    public void setIdUnidade(Integer idUnidade) {
        this.idUnidade = idUnidade;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

}
