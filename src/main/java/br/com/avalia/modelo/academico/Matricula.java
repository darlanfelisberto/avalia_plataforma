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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "matricula", schema = "academico")
@DynamicInsert
public class Matricula implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_aluno", referencedColumnName = "id", foreignKey = @ForeignKey(name = "matricula_id_aluno_fkey"))
    private Aluno aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_turma", referencedColumnName = "id", foreignKey = @ForeignKey(name = "matricula_id_turma_fkey"))
    private Turma turma;

    @Column(name = "situacao", length = 100)
    private String situacao;

    @Column(name = "media_final", precision = 5, scale = 2)
    private BigDecimal mediaFinal;

    @Column(name = "numero_faltas")
    private Integer numeroFaltas;

    @Column(name = "frequencia", precision = 5, scale = 2)
    private BigDecimal frequencia;

    @Column(name = "ano")
    private Integer ano;

    @Column(name = "periodo")
    private Integer periodo;

    @Column(name = "data_matricula")
    private LocalDate dataMatricula;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    public Matricula() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Aluno getAluno() {
        return aluno;
    }

    public void setAluno(Aluno aluno) {
        this.aluno = aluno;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public BigDecimal getMediaFinal() {
        return mediaFinal;
    }

    public void setMediaFinal(BigDecimal mediaFinal) {
        this.mediaFinal = mediaFinal;
    }

    public Integer getNumeroFaltas() {
        return numeroFaltas;
    }

    public void setNumeroFaltas(Integer numeroFaltas) {
        this.numeroFaltas = numeroFaltas;
    }

    public BigDecimal getFrequencia() {
        return frequencia;
    }

    public void setFrequencia(BigDecimal frequencia) {
        this.frequencia = frequencia;
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

    public LocalDate getDataMatricula() {
        return dataMatricula;
    }

    public void setDataMatricula(LocalDate dataMatricula) {
        this.dataMatricula = dataMatricula;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

}
