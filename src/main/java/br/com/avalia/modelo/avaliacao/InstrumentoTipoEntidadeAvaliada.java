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
import java.util.UUID;

@Entity
@Table(name = "instrumento_tipo_entidade_avaliada", schema = "avaliacao")
public class InstrumentoTipoEntidadeAvaliada implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "instrumento_entidade_avaliada_id", nullable = false)
    private UUID instrumentoEntidadeAvaliadaId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instrumento_avaliativo_id", referencedColumnName = "instrumento_avaliativo_id", nullable = false, foreignKey = @ForeignKey(name = "fkrilik85jscgaq8jtybjl7vgaw"))
    private InstrumentoAvaliativo instrumentoAvaliativo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_entidade_alvo_id", referencedColumnName = "tipo_entidade_id", nullable = false, foreignKey = @ForeignKey(name = "fkaa2jwk0iwjd8bpo9foy85hvgg"))
    private TipoEntidade tipoEntidadeAlvo;

    public InstrumentoTipoEntidadeAvaliada() {
    }

    public UUID getInstrumentoEntidadeAvaliadaId() {
        return instrumentoEntidadeAvaliadaId;
    }

    public void setInstrumentoEntidadeAvaliadaId(UUID instrumentoEntidadeAvaliadaId) {
        this.instrumentoEntidadeAvaliadaId = instrumentoEntidadeAvaliadaId;
    }

    public InstrumentoAvaliativo getInstrumentoAvaliativo() {
        return instrumentoAvaliativo;
    }

    public void setInstrumentoAvaliativo(InstrumentoAvaliativo instrumentoAvaliativo) {
        this.instrumentoAvaliativo = instrumentoAvaliativo;
    }

    public TipoEntidade getTipoEntidadeAlvo() {
        return tipoEntidadeAlvo;
    }

    public void setTipoEntidadeAlvo(TipoEntidade tipoEntidadeAlvo) {
        this.tipoEntidadeAlvo = tipoEntidadeAlvo;
    }

}
