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
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "servidor", schema = "academico")
@DynamicInsert
public class Servidor extends Model<Integer> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id_pessoa", nullable = false, foreignKey = @ForeignKey(name = "servidor_id_pessoa_fkey"))
    private Pessoa pessoa;

    @Column(name = "siape", length = 20)
    private String siape;

    @Column(name = "matricula_interna", length = 20)
    private String matriculaInterna;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_cargo", referencedColumnName = "id", foreignKey = @ForeignKey(name = "servidor_id_cargo_fkey"))
    private Cargo cargo;

    @Column(name = "id_unidade_lotacao")
    private Integer idUnidadeLotacao;

    @Column(name = "id_unidade_exercicio")
    private Integer idUnidadeExercicio;

    @Column(name = "categoria", length = 100)
    private String categoria;

    @Column(name = "situacao", length = 100)
    private String situacao;

    @Column(name = "regime_trabalho", length = 50)
    private String regimeTrabalho;

    @Column(name = "regime_juridico", length = 50)
    private String regimeJuridico;

    @Column(name = "admissao")
    private LocalDate admissao;

    @Column(name = "data_desligamento")
    private LocalDate dataDesligamento;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_situacao", referencedColumnName = "id_situacao", foreignKey = @ForeignKey(name = "servidor_id_situacao_fkey"))
    private SituacaoServidor situacaoServidor;

    public Servidor() {
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

    public String getSiape() {
        return siape;
    }

    public void setSiape(String siape) {
        this.siape = siape;
    }

    public String getMatriculaInterna() {
        return matriculaInterna;
    }

    public void setMatriculaInterna(String matriculaInterna) {
        this.matriculaInterna = matriculaInterna;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Integer getIdUnidadeLotacao() {
        return idUnidadeLotacao;
    }

    public void setIdUnidadeLotacao(Integer idUnidadeLotacao) {
        this.idUnidadeLotacao = idUnidadeLotacao;
    }

    public Integer getIdUnidadeExercicio() {
        return idUnidadeExercicio;
    }

    public void setIdUnidadeExercicio(Integer idUnidadeExercicio) {
        this.idUnidadeExercicio = idUnidadeExercicio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getRegimeTrabalho() {
        return regimeTrabalho;
    }

    public void setRegimeTrabalho(String regimeTrabalho) {
        this.regimeTrabalho = regimeTrabalho;
    }

    public String getRegimeJuridico() {
        return regimeJuridico;
    }

    public void setRegimeJuridico(String regimeJuridico) {
        this.regimeJuridico = regimeJuridico;
    }

    public LocalDate getAdmissao() {
        return admissao;
    }

    public void setAdmissao(LocalDate admissao) {
        this.admissao = admissao;
    }

    public LocalDate getDataDesligamento() {
        return dataDesligamento;
    }

    public void setDataDesligamento(LocalDate dataDesligamento) {
        this.dataDesligamento = dataDesligamento;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public SituacaoServidor getSituacaoServidor() {
        return situacaoServidor;
    }

    public void setSituacaoServidor(SituacaoServidor situacaoServidor) {
        this.situacaoServidor = situacaoServidor;
    }

}
