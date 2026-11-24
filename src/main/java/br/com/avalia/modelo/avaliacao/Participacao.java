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
@Table(name = "participacao", schema = "avaliacao")
public class Participacao implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "participacao_id", nullable = false)
    private UUID participacaoId;

    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    @Column(name = "registrada_em", nullable = false)
    private OffsetDateTime registradaEm;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instrumento_avaliativo_id", referencedColumnName = "instrumento_avaliativo_id", nullable = false, foreignKey = @ForeignKey(name = "fkrqhg0qukej2udicgpn48vh0i3"))
    private InstrumentoAvaliativo instrumentoAvaliativo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resposta_instrumento_id", referencedColumnName = "resposta_instrumento_id", foreignKey = @ForeignKey(name = "participacao_resposta_instrumento_fk"))
    private RespostaInstrumento respostaInstrumento;

    public Participacao() {
    }

    public UUID getParticipacaoId() {
        return participacaoId;
    }

    public void setParticipacaoId(UUID participacaoId) {
        this.participacaoId = participacaoId;
    }

    public OffsetDateTime getRegistradaEm() {
        return registradaEm;
    }

    public void setRegistradaEm(OffsetDateTime registradaEm) {
        this.registradaEm = registradaEm;
    }

    public InstrumentoAvaliativo getInstrumentoAvaliativo() {
        return instrumentoAvaliativo;
    }

    public void setInstrumentoAvaliativo(InstrumentoAvaliativo instrumentoAvaliativo) {
        this.instrumentoAvaliativo = instrumentoAvaliativo;
    }

    public RespostaInstrumento getRespostaInstrumento() {
        return respostaInstrumento;
    }

    public void setRespostaInstrumento(RespostaInstrumento respostaInstrumento) {
        this.respostaInstrumento = respostaInstrumento;
    }

}
