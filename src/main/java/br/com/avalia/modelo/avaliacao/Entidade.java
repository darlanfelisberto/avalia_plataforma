package br.com.avalia.modelo.avaliacao;

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
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.hibernate.annotations.TimeZoneStorage;
import org.hibernate.annotations.TimeZoneStorageType;

@Entity
@Table(name = "entidade", schema = "avaliacao")
public class Entidade implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "entidade_id", nullable = false)
    private UUID entidadeId;

    @Column(name = "ativo", nullable = false)
    private Boolean ativo;

    @Column(name = "referencia_externa", nullable = false, length = 60)
    private String referenciaExterna;

    @Column(name = "rotulo", nullable = false, length = 300)
    private String rotulo;

    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    @Column(name = "sincronizado_em", nullable = false)
    private OffsetDateTime sincronizadoEm;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_entidade_id", referencedColumnName = "tipo_entidade_id", nullable = false, foreignKey = @ForeignKey(name = "fkfxgadyhfl9porvmtlcfyuo87i"))
    private TipoEntidade tipoEntidade;

    public Entidade() {
    }

    public UUID getEntidadeId() {
        return entidadeId;
    }

    public void setEntidadeId(UUID entidadeId) {
        this.entidadeId = entidadeId;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public String getReferenciaExterna() {
        return referenciaExterna;
    }

    public void setReferenciaExterna(String referenciaExterna) {
        this.referenciaExterna = referenciaExterna;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    public OffsetDateTime getSincronizadoEm() {
        return sincronizadoEm;
    }

    public void setSincronizadoEm(OffsetDateTime sincronizadoEm) {
        this.sincronizadoEm = sincronizadoEm;
    }

    public TipoEntidade getTipoEntidade() {
        return tipoEntidade;
    }

    public void setTipoEntidade(TipoEntidade tipoEntidade) {
        this.tipoEntidade = tipoEntidade;
    }

}
