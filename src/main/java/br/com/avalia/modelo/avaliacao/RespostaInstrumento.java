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
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "resposta_instrumento", schema = "avaliacao")
public class RespostaInstrumento implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "resposta_instrumento_id", nullable = false)
    private UUID respostaInstrumentoId;

    @Column(name = "data_envio")
    private LocalDate dataEnvio;

    @Column(name = "status", nullable = false, length = 12)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instrumento_avaliativo_id", referencedColumnName = "instrumento_avaliativo_id", nullable = false, foreignKey = @ForeignKey(name = "fkqxn7fslsi73nfv2sfd8ne9e0k"))
    private InstrumentoAvaliativo instrumentoAvaliativo;

    public RespostaInstrumento() {
    }

    public UUID getRespostaInstrumentoId() {
        return respostaInstrumentoId;
    }

    public void setRespostaInstrumentoId(UUID respostaInstrumentoId) {
        this.respostaInstrumentoId = respostaInstrumentoId;
    }

    public LocalDate getDataEnvio() {
        return dataEnvio;
    }

    public void setDataEnvio(LocalDate dataEnvio) {
        this.dataEnvio = dataEnvio;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public InstrumentoAvaliativo getInstrumentoAvaliativo() {
        return instrumentoAvaliativo;
    }

    public void setInstrumentoAvaliativo(InstrumentoAvaliativo instrumentoAvaliativo) {
        this.instrumentoAvaliativo = instrumentoAvaliativo;
    }

}
