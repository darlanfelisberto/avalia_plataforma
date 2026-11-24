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
@Table(name = "questao", schema = "avaliacao")
public class Questao implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "questao_id", nullable = false)
    private UUID questaoId;

    @Column(name = "assunto", length = 100)
    private String assunto;

    @TimeZoneStorage(TimeZoneStorageType.NATIVE)
    @Column(name = "criado_em", nullable = false)
    private OffsetDateTime criadoEm;

    @Column(name = "criado_por", nullable = false, length = 60)
    private String criadoPor;

    @Column(name = "enunciado", nullable = false, columnDefinition = "text")
    private String enunciado;

    @Column(name = "tipo", nullable = false, length = 16)
    private String tipo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conjunto_opcao_id", referencedColumnName = "conjunto_opcao_id", foreignKey = @ForeignKey(name = "fkoy4qeql3nohc8uy7w3v2hl7lc"))
    private ConjuntoOpcao conjuntoOpcao;

    public Questao() {
    }

    public UUID getQuestaoId() {
        return questaoId;
    }

    public void setQuestaoId(UUID questaoId) {
        this.questaoId = questaoId;
    }

    public String getAssunto() {
        return assunto;
    }

    public void setAssunto(String assunto) {
        this.assunto = assunto;
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

    public String getEnunciado() {
        return enunciado;
    }

    public void setEnunciado(String enunciado) {
        this.enunciado = enunciado;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public ConjuntoOpcao getConjuntoOpcao() {
        return conjuntoOpcao;
    }

    public void setConjuntoOpcao(ConjuntoOpcao conjuntoOpcao) {
        this.conjuntoOpcao = conjuntoOpcao;
    }

}
