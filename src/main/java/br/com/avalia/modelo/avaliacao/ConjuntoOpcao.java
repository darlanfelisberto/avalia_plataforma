package br.com.avalia.modelo.avaliacao;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "conjunto_opcao", schema = "avaliacao", uniqueConstraints = {
        @UniqueConstraint(name = "conjunto_opcao_nome_key", columnNames = {"nome"})
    })
public class ConjuntoOpcao implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "conjunto_opcao_id", nullable = false)
    private UUID conjuntoOpcaoId;

    @Column(name = "nome", nullable = false, length = 100)
    private String nome;

    @Column(name = "exclusivo")
    private Boolean exclusivo;

    public ConjuntoOpcao() {
    }

    public UUID getConjuntoOpcaoId() {
        return conjuntoOpcaoId;
    }

    public void setConjuntoOpcaoId(UUID conjuntoOpcaoId) {
        this.conjuntoOpcaoId = conjuntoOpcaoId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Boolean getExclusivo() {
        return exclusivo;
    }

    public void setExclusivo(Boolean exclusivo) {
        this.exclusivo = exclusivo;
    }

}
