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
import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "sessao_questao", schema = "avaliacao")
public class SessaoQuestao extends Model<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "sessao_questao_id", nullable = false)
    private UUID sessaoQuestaoId;

    @Column(name = "obrigatoria", nullable = false)
    private Boolean obrigatoria;

    @Column(name = "ordem", nullable = false)
    private Integer ordem;

    @Column(name = "peso", precision = 6, scale = 2)
    private BigDecimal peso;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "questao_id", referencedColumnName = "questao_id", nullable = false, foreignKey = @ForeignKey(name = "fkir3wu7wop32bb5y723tcpg57y"))
    private Questao questao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instrumento_sessao_id", referencedColumnName = "instrumento_sessao_id", nullable = false, foreignKey = @ForeignKey(name = "sessao_questao_instrumento_sessao_fk"))
    private InstrumentoSessao instrumentoSessao;

    public SessaoQuestao() {
    }

    @Override
    public UUID getId() {
        return sessaoQuestaoId;
    }

    public UUID getSessaoQuestaoId() {
        return sessaoQuestaoId;
    }

    public void setSessaoQuestaoId(UUID sessaoQuestaoId) {
        this.sessaoQuestaoId = sessaoQuestaoId;
    }

    public Boolean getObrigatoria() {
        return obrigatoria;
    }

    public void setObrigatoria(Boolean obrigatoria) {
        this.obrigatoria = obrigatoria;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

    public InstrumentoSessao getInstrumentoSessao() {
        return instrumentoSessao;
    }

    public void setInstrumentoSessao(InstrumentoSessao instrumentoSessao) {
        this.instrumentoSessao = instrumentoSessao;
    }

}
