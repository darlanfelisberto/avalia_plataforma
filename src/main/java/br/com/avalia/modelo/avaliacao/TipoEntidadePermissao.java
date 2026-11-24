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
import jakarta.persistence.UniqueConstraint;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "tipo_entidade_permissao", schema = "avaliacao", uniqueConstraints = {
        @UniqueConstraint(name = "tipo_entidade_permissao_tipo_respondente_id_tipo_avaliado_i_key", columnNames = {"tipo_respondente_id", "tipo_avaliado_id"}),
        @UniqueConstraint(name = "uk767lkpbbhylhpbyg3ewlm4wep", columnNames = {"tipo_respondente_id", "tipo_avaliado_id"})
    })
public class TipoEntidadePermissao implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "tipo_entidade_permissao_id", nullable = false)
    private UUID tipoEntidadePermissaoId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_respondente_id", referencedColumnName = "tipo_entidade_id", nullable = false, foreignKey = @ForeignKey(name = "tipo_entidade_permissao_tipo_respondente_id_fkey"))
    private TipoEntidade tipoRespondente;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tipo_avaliado_id", referencedColumnName = "tipo_entidade_id", nullable = false, foreignKey = @ForeignKey(name = "tipo_entidade_permissao_tipo_avaliado_id_fkey"))
    private TipoEntidade tipoAvaliado;

    public TipoEntidadePermissao() {
    }

    public UUID getTipoEntidadePermissaoId() {
        return tipoEntidadePermissaoId;
    }

    public void setTipoEntidadePermissaoId(UUID tipoEntidadePermissaoId) {
        this.tipoEntidadePermissaoId = tipoEntidadePermissaoId;
    }

    public TipoEntidade getTipoRespondente() {
        return tipoRespondente;
    }

    public void setTipoRespondente(TipoEntidade tipoRespondente) {
        this.tipoRespondente = tipoRespondente;
    }

    public TipoEntidade getTipoAvaliado() {
        return tipoAvaliado;
    }

    public void setTipoAvaliado(TipoEntidade tipoAvaliado) {
        this.tipoAvaliado = tipoAvaliado;
    }

}
