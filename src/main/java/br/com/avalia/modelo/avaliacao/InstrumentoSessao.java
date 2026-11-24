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
import java.util.UUID;

@Entity
@Table(name = "instrumento_sessao", schema = "avaliacao")
public class InstrumentoSessao implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "instrumento_sessao_id", nullable = false)
    private UUID instrumentoSessaoId;

    @Column(name = "descricao", columnDefinition = "text")
    private String descricao;

    @Column(name = "ordem", nullable = false)
    private Integer ordem;

    @Column(name = "tipo", length = 255)
    private String tipo;

    @Column(name = "titulo", nullable = false, length = 200)
    private String titulo;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "instrumento_avaliativo_id", referencedColumnName = "instrumento_avaliativo_id", nullable = false, foreignKey = @ForeignKey(name = "fka8uqj3jw4w250sqq0awtfixrr"))
    private InstrumentoAvaliativo instrumentoAvaliativo;

    @Column(name = "tiposessao", length = 255)
    private String tiposessao;

    public InstrumentoSessao() {
    }

    public UUID getInstrumentoSessaoId() {
        return instrumentoSessaoId;
    }

    public void setInstrumentoSessaoId(UUID instrumentoSessaoId) {
        this.instrumentoSessaoId = instrumentoSessaoId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getOrdem() {
        return ordem;
    }

    public void setOrdem(Integer ordem) {
        this.ordem = ordem;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public InstrumentoAvaliativo getInstrumentoAvaliativo() {
        return instrumentoAvaliativo;
    }

    public void setInstrumentoAvaliativo(InstrumentoAvaliativo instrumentoAvaliativo) {
        this.instrumentoAvaliativo = instrumentoAvaliativo;
    }

    public String getTiposessao() {
        return tiposessao;
    }

    public void setTiposessao(String tiposessao) {
        this.tiposessao = tiposessao;
    }

}
