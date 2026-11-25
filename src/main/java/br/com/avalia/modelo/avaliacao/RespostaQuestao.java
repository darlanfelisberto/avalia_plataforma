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
@Table(name = "resposta_questao", schema = "avaliacao")
public class RespostaQuestao extends Model<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "resposta_questao_id", nullable = false)
    private UUID respostaQuestaoId;

    @Column(name = "texto", columnDefinition = "text")
    private String texto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opcao_questao_id", referencedColumnName = "opcao_questao_id", foreignKey = @ForeignKey(name = "fk4x7aqyifyeevki38hex8onhmp"))
    private OpcaoQuestao opcaoQuestao;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "resposta_instrumento_id", referencedColumnName = "resposta_instrumento_id", nullable = false, foreignKey = @ForeignKey(name = "fkk4wsvqrnbvq8s5f7dc493xdm6"))
    private RespostaInstrumento respostaInstrumento;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questao_id", referencedColumnName = "questao_id", foreignKey = @ForeignKey(name = "resposta_questao_questao_fk"))
    private Questao questao;

    public RespostaQuestao() {
    }

    @Override
    public UUID getId() {
        return respostaQuestaoId;
    }

    public UUID getRespostaQuestaoId() {
        return respostaQuestaoId;
    }

    public void setRespostaQuestaoId(UUID respostaQuestaoId) {
        this.respostaQuestaoId = respostaQuestaoId;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public OpcaoQuestao getOpcaoQuestao() {
        return opcaoQuestao;
    }

    public void setOpcaoQuestao(OpcaoQuestao opcaoQuestao) {
        this.opcaoQuestao = opcaoQuestao;
    }

    public RespostaInstrumento getRespostaInstrumento() {
        return respostaInstrumento;
    }

    public void setRespostaInstrumento(RespostaInstrumento respostaInstrumento) {
        this.respostaInstrumento = respostaInstrumento;
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

}
