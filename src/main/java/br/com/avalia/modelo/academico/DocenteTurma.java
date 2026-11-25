package br.com.avalia.modelo.academico;

import br.com.avalia.modelo.Model;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.io.Serial;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "docente_turma", schema = "academico")
@DynamicInsert
public class DocenteTurma extends Model<Integer> {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_servidor", referencedColumnName = "id", foreignKey = @ForeignKey(name = "docente_turma_id_servidor_fkey"))
    private Servidor servidor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "id_turma", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "docente_turma_id_turma_fkey"))
    private Turma turma;

    @Column(name = "ch_dedicada", precision = 6, scale = 2)
    private BigDecimal chDedicada;

    @Column(name = "tipo_docente", length = 30)
    private String tipoDocente;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    public DocenteTurma() {
    }

    @Override
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Servidor getServidor() {
        return servidor;
    }

    public void setServidor(Servidor servidor) {
        this.servidor = servidor;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    public BigDecimal getChDedicada() {
        return chDedicada;
    }

    public void setChDedicada(BigDecimal chDedicada) {
        this.chDedicada = chDedicada;
    }

    public String getTipoDocente() {
        return tipoDocente;
    }

    public void setTipoDocente(String tipoDocente) {
        this.tipoDocente = tipoDocente;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

}
