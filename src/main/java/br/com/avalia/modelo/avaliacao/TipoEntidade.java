package br.com.avalia.modelo.avaliacao;

import br.com.avalia.modelo.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serial;
import java.util.UUID;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "tipo_entidade", schema = "avaliacao", uniqueConstraints = {
        @UniqueConstraint(name = "tipo_entidade_codigo_key", columnNames = {"codigo"})
    })
@DynamicInsert
public class TipoEntidade extends Model<UUID> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "tipo_entidade_id", nullable = false)
    private UUID tipoEntidadeId;

    @Column(name = "codigo", nullable = false, length = 30)
    private String codigo;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "pode_ser_avaliada", nullable = false)
    private Boolean podeSerAvaliada;

    @Column(name = "pode_responder", nullable = false)
    private Boolean podeResponder;

    public TipoEntidade() {
    }

    @Override
    public UUID getId() {
        return tipoEntidadeId;
    }

    public UUID getTipoEntidadeId() {
        return tipoEntidadeId;
    }

    public void setTipoEntidadeId(UUID tipoEntidadeId) {
        this.tipoEntidadeId = tipoEntidadeId;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getPodeSerAvaliada() {
        return podeSerAvaliada;
    }

    public void setPodeSerAvaliada(Boolean podeSerAvaliada) {
        this.podeSerAvaliada = podeSerAvaliada;
    }

    public Boolean getPodeResponder() {
        return podeResponder;
    }

    public void setPodeResponder(Boolean podeResponder) {
        this.podeResponder = podeResponder;
    }

}
