package br.com.avalia.modelo.academico;

import br.com.avalia.modelo.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.time.LocalDateTime;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "aluno", schema = "academico")
@DynamicInsert
public class Aluno extends Model<Integer> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id_pessoa", nullable = false, foreignKey = @ForeignKey(name = "aluno_id_pessoa_fkey"))
    private Pessoa pessoa;

    @Column(name = "matricula", length = 30)
    private String matricula;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_curso", referencedColumnName = "id", foreignKey = @ForeignKey(name = "aluno_id_curso_fkey"))
    private Curso curso;

    @Column(name = "nivel", length = 1)
    private Character nivel;

    @Column(name = "situacao", length = 100)
    private String situacao;

    @Column(name = "ano_ingresso")
    private Integer anoIngresso;

    @Column(name = "periodo_ingresso")
    private Integer periodoIngresso;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_situacao", referencedColumnName = "id", foreignKey = @ForeignKey(name = "aluno_id_situacao_fkey"))
    private StatusDiscente statusDiscente;

    public Aluno() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public Curso getCurso() {
        return curso;
    }

    public void setCurso(Curso curso) {
        this.curso = curso;
    }

    public Character getNivel() {
        return nivel;
    }

    public void setNivel(Character nivel) {
        this.nivel = nivel;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public Integer getAnoIngresso() {
        return anoIngresso;
    }

    public void setAnoIngresso(Integer anoIngresso) {
        this.anoIngresso = anoIngresso;
    }

    public Integer getPeriodoIngresso() {
        return periodoIngresso;
    }

    public void setPeriodoIngresso(Integer periodoIngresso) {
        this.periodoIngresso = periodoIngresso;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public StatusDiscente getStatusDiscente() {
        return statusDiscente;
    }

    public void setStatusDiscente(StatusDiscente statusDiscente) {
        this.statusDiscente = statusDiscente;
    }

}
