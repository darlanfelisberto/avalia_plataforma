package br.com.avalia.modelo.academico;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import org.hibernate.annotations.DynamicInsert;

@Entity
@Table(name = "pessoa", schema = "academico")
@DynamicInsert
public class Pessoa implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "id_pessoa", nullable = false)
    private Integer idPessoa;

    @Column(name = "nome", nullable = false, length = 200)
    private String nome;

    @Column(name = "nome_social", length = 200)
    private String nomeSocial;

    @Column(name = "cpf_cnpj", length = 14)
    private String cpfCnpj;

    @Column(name = "email", length = 200)
    private String email;

    @Column(name = "telefone", length = 30)
    private String telefone;

    @Column(name = "celular", length = 30)
    private String celular;

    @Column(name = "data_nascimento")
    private LocalDate dataNascimento;

    @Column(name = "sexo", length = 1)
    private Character sexo;

    @Column(name = "data_cadastro")
    private LocalDateTime dataCadastro;

    @Column(name = "atualizado_em")
    private LocalDateTime atualizadoEm;

    public Pessoa() {
    }

    public Integer getIdPessoa() {
        return idPessoa;
    }

    public void setIdPessoa(Integer idPessoa) {
        this.idPessoa = idPessoa;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeSocial() {
        return nomeSocial;
    }

    public void setNomeSocial(String nomeSocial) {
        this.nomeSocial = nomeSocial;
    }

    public String getCpfCnpj() {
        return cpfCnpj;
    }

    public void setCpfCnpj(String cpfCnpj) {
        this.cpfCnpj = cpfCnpj;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public Character getSexo() {
        return sexo;
    }

    public void setSexo(Character sexo) {
        this.sexo = sexo;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getAtualizadoEm() {
        return atualizadoEm;
    }

    public void setAtualizadoEm(LocalDateTime atualizadoEm) {
        this.atualizadoEm = atualizadoEm;
    }

}
