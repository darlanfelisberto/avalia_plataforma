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
@Table(name = "componente_curricular", schema = "academico")
@DynamicInsert
public class ComponenteCurricular extends Model<Integer> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "codigo", nullable = false, length = 30)
    private String codigo;

    @Column(name = "nome", nullable = false, length = 300)
    private String nome;

    @Column(name = "id_unidade")
    private Integer idUnidade;

    @Column(name = "nivel", length = 1)
    private Character nivel;

    @Column(name = "ch_total")
    private Integer chTotal;

    @Column(name = "ch_teorica")
    private Integer chTeorica;

    @Column(name = "ch_pratica")
    private Integer chPratica;

    @Column(name = "ementa", columnDefinition = "text")
    private String ementa;

    @Column(name = "ativo")
    private Boolean ativo;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    public ComponenteCurricular() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Integer getIdUnidade() {
        return idUnidade;
    }

    public void setIdUnidade(Integer idUnidade) {
        this.idUnidade = idUnidade;
    }

    public Character getNivel() {
        return nivel;
    }

    public void setNivel(Character nivel) {
        this.nivel = nivel;
    }

    public Integer getChTotal() {
        return chTotal;
    }

    public void setChTotal(Integer chTotal) {
        this.chTotal = chTotal;
    }

    public Integer getChTeorica() {
        return chTeorica;
    }

    public void setChTeorica(Integer chTeorica) {
        this.chTeorica = chTeorica;
    }

    public Integer getChPratica() {
        return chPratica;
    }

    public void setChPratica(Integer chPratica) {
        this.chPratica = chPratica;
    }

    public String getEmenta() {
        return ementa;
    }

    public void setEmenta(String ementa) {
        this.ementa = ementa;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

}
