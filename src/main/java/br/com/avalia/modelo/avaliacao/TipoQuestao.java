package br.com.avalia.modelo.avaliacao;

import br.com.avalia.modelo.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;

@Entity
@Table(name = "tipo_questao", schema = "avaliacao")
public class TipoQuestao extends Model<TipoQuestaoId> {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Enumerated(EnumType.STRING)
    @Column(name = "id", nullable = false, length = 30)
    private TipoQuestaoId id;

    @Column(name = "codigo", nullable = false, unique = true, length = 30)
    private String codigo;

    @Column(name = "descricao", nullable = false, length = 100)
    private String descricao;

    public TipoQuestao() {
    }

    @Override
    public TipoQuestaoId getId() { return id; }
    public void setId(TipoQuestaoId id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
}
