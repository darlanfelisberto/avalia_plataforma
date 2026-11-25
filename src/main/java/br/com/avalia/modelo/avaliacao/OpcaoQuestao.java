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
@Table(name = "opcao_questao", schema = "avaliacao")
public class OpcaoQuestao extends Model<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "opcao_questao_id", nullable = false)
    private UUID opcaoQuestaoId;

    @Column(name = "correta", nullable = false)
    private Boolean correta;

    @Column(name = "ordem", nullable = false)
    private Integer ordem;

    @Column(name = "texto", nullable = false, length = 300)
    private String texto;

    @Column(name = "valor", precision = 6, scale = 2)
    private BigDecimal valor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "conjunto_opcao_id", referencedColumnName = "conjunto_opcao_id", nullable = false, foreignKey = @ForeignKey(name = "fk5ca2ep4d7cm82haxrla72rc9"))
    private ConjuntoOpcao conjuntoOpcao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "questao_id", referencedColumnName = "questao_id", foreignKey = @ForeignKey(name = "opcao_questao_questao_fk"))
    private Questao questao;

    public OpcaoQuestao() {
    }

    @Override
    public UUID getId() {
        return opcaoQuestaoId;
    }

    public UUID getOpcaoQuestaoId() {
        return opcaoQuestaoId;
    }

    public void setOpcaoQuestaoId(UUID opcaoQuestaoId) {
        this.opcaoQuestaoId = opcaoQuestaoId;
    }

    public Boolean getCorreta() {
        return correta;
    }

    public void setCorreta(Boolean correta) {
        this.correta = correta;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public ConjuntoOpcao getConjuntoOpcao() {
        return conjuntoOpcao;
    }

    public void setConjuntoOpcao(ConjuntoOpcao conjuntoOpcao) {
        this.conjuntoOpcao = conjuntoOpcao;
    }

    public Questao getQuestao() {
        return questao;
    }

    public void setQuestao(Questao questao) {
        this.questao = questao;
    }

}
