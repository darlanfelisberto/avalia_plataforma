package br.com.avalia.modelo.academico;

import br.com.avalia.modelo.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.time.LocalDateTime;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "situacao_servidor", schema = "academico")
@DynamicInsert
public class SituacaoServidor extends Model<Integer> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_situacao", nullable = false)
    private Integer idSituacao;

    @Column(name = "descricao", nullable = false, length = 100)
    private String descricao;

    @Column(name = "permite_cadastro_designacoes")
    private Boolean permiteCadastroDesignacoes;

    @Column(name = "codigo_siape")
    private Integer codigoSiape;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    public SituacaoServidor() {
    }

    @Override
    public Integer getId() {
        return idSituacao;
    }

    public Integer getIdSituacao() {
        return idSituacao;
    }

    public void setIdSituacao(Integer idSituacao) {
        this.idSituacao = idSituacao;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getPermiteCadastroDesignacoes() {
        return permiteCadastroDesignacoes;
    }

    public void setPermiteCadastroDesignacoes(Boolean permiteCadastroDesignacoes) {
        this.permiteCadastroDesignacoes = permiteCadastroDesignacoes;
    }

    public Integer getCodigoSiape() {
        return codigoSiape;
    }

    public void setCodigoSiape(Integer codigoSiape) {
        this.codigoSiape = codigoSiape;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

}
