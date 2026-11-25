package br.com.avalia.modelo.avaliacao;

import br.com.avalia.modelo.academico.Pessoa;
import br.com.avalia.modelo.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

@Entity
@Table(name = "instrumento_avaliativo", schema = "avaliacao")
public class InstrumentoAvaliativo extends Model<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "instrumento_avaliativo_id", nullable = false)
    private UUID instrumentoAvaliativoId;

    @Column(name = "anonimo", nullable = false)
    private Boolean anonimo;

    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    @Column(name = "atualizado_em", nullable = false)
    private OffsetDateTime atualizadoEm;

    @Column(name = "ciclo_referencia", nullable = false, length = 20)
    private String cicloReferencia;

    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    @Column(name = "consolidado_em")
    private OffsetDateTime consolidadoEm;

    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    @Column(name = "criado_por", length = 60)
    private String criadoPor;

    @Column(name = "descricao", columnDefinition = "text")
    private String descricao;

    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    @Column(name = "fim_aplicacao", nullable = false)
    private OffsetDateTime fimAplicacao;

    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    @Column(name = "inicio_aplicacao", nullable = false)
    private OffsetDateTime inicioAplicacao;

    @Column(name = "permite_edicao", nullable = false)
    private Boolean permiteEdicao;

    @Column(name = "status", nullable = false, length = 12)
    private String status;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @Column(name = "tipo_entidade_avaliada_id", nullable = false)
    private UUID tipoEntidadeAvaliadaId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pessoa", referencedColumnName = "id_pessoa", foreignKey = @ForeignKey(name = "fk_instrumento_avaliativo_criado_por_pessoa"))
    private Pessoa pessoa;

    public InstrumentoAvaliativo() {
    }

    @Override
    public UUID getId() {
        return instrumentoAvaliativoId;
    }

    public UUID getInstrumentoAvaliativoId() {
        return instrumentoAvaliativoId;
    }

    public void setInstrumentoAvaliativoId(UUID instrumentoAvaliativoId) {
        this.instrumentoAvaliativoId = instrumentoAvaliativoId;
    }

    public Boolean getAnonimo() {
        return anonimo;
    }

    public void setAnonimo(Boolean anonimo) {
        this.anonimo = anonimo;
    }

    public OffsetDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(OffsetDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

    public String getCicloReferencia() {
        return cicloReferencia;
    }

    public void setCicloReferencia(String cicloReferencia) {
        this.cicloReferencia = cicloReferencia;
    }

    public OffsetDateTime getConsolidadoEm() {
        return consolidadoEm;
    }

    public void setConsolidadoEm(OffsetDateTime consolidadoEm) {
        this.consolidadoEm = consolidadoEm;
    }

    public OffsetDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(OffsetDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    public String getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(String criadoPor) {
        this.criadoPor = criadoPor;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public OffsetDateTime getFimAplicacao() {
        return fimAplicacao;
    }

    public void setFimAplicacao(OffsetDateTime fimAplicacao) {
        this.fimAplicacao = fimAplicacao;
    }

    public OffsetDateTime getInicioAplicacao() {
        return inicioAplicacao;
    }

    public void setInicioAplicacao(OffsetDateTime inicioAplicacao) {
        this.inicioAplicacao = inicioAplicacao;
    }

    public Boolean getPermiteEdicao() {
        return permiteEdicao;
    }

    public void setPermiteEdicao(Boolean permiteEdicao) {
        this.permiteEdicao = permiteEdicao;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public UUID getTipoEntidadeAvaliadaId() {
        return tipoEntidadeAvaliadaId;
    }

    public void setTipoEntidadeAvaliadaId(UUID tipoEntidadeAvaliadaId) {
        this.tipoEntidadeAvaliadaId = tipoEntidadeAvaliadaId;
    }

    public Pessoa getPessoa() {
        return pessoa;
    }

    public void setPessoa(Pessoa pessoa) {
        this.pessoa = pessoa;
    }

}
