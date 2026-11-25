package br.com.avalia.modelo.avaliacao;

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
import java.util.UUID;

@Entity
@Table(name = "instrumento_tipo_entidade_respondente", schema = "avaliacao")
public class InstrumentoTipoEntidadeRespondente extends Model<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "instrumento_tipo_entidade_respondente_id", nullable = false)
    private UUID instrumentoTipoEntidadeRespondenteId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instrumento_avaliativo_id", referencedColumnName = "instrumento_avaliativo_id", nullable = false, foreignKey = @ForeignKey(name = "fkcnbsexkd9q7lc34k2nfq17fjy"))
    private InstrumentoAvaliativo instrumentoAvaliativo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_entidade_id", referencedColumnName = "tipo_entidade_id", nullable = false, foreignKey = @ForeignKey(name = "fkqf9g74ewjr6uhhs81hq68hwnu"))
    private TipoEntidade tipoEntidade;

    public InstrumentoTipoEntidadeRespondente() {
    }

    @Override
    public UUID getId() {
        return instrumentoTipoEntidadeRespondenteId;
    }

    public UUID getInstrumentoTipoEntidadeRespondenteId() {
        return instrumentoTipoEntidadeRespondenteId;
    }

    public void setInstrumentoTipoEntidadeRespondenteId(UUID instrumentoTipoEntidadeRespondenteId) {
        this.instrumentoTipoEntidadeRespondenteId = instrumentoTipoEntidadeRespondenteId;
    }

    public InstrumentoAvaliativo getInstrumentoAvaliativo() {
        return instrumentoAvaliativo;
    }

    public void setInstrumentoAvaliativo(InstrumentoAvaliativo instrumentoAvaliativo) {
        this.instrumentoAvaliativo = instrumentoAvaliativo;
    }

    public TipoEntidade getTipoEntidade() {
        return tipoEntidade;
    }

    public void setTipoEntidade(TipoEntidade tipoEntidade) {
        this.tipoEntidade = tipoEntidade;
    }

}
