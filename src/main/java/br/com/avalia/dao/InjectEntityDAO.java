package br.com.avalia.dao;

import br.com.avalia.modelo.Model;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.Objects;

/** Operações de escrita compartilhadas pelos DAOs concretos gerenciados pelo CDI. */
public abstract class InjectEntityDAO<T extends Model<?>> {

    @PersistenceContext(unitName = "avaliaPU")
    protected EntityManager entityManager;

    private final Class<T> entityClass;

    protected InjectEntityDAO(Class<T> entityClass) {
        this.entityClass = Objects.requireNonNull(entityClass, "O tipo da entidade é obrigatório.");
    }

    protected Class<T> getEntityClass() {
        return entityClass;
    }

    /** Persiste uma entidade nova e retorna a mesma instância. */
    @Transactional
    public T salvar(T entidade) {
        Objects.requireNonNull(entidade, "A entidade é obrigatória.");
        entityManager.persist(entidade);
        return entidade;
    }

    /** Mescla o estado da entidade e retorna a instância gerenciada pelo contexto. */
    @Transactional
    public T atualizar(T entidade) {
        exigirIdentificador(entidade);
        return entityManager.merge(entidade);
    }

    /** Remove pelo identificador; uma entidade já ausente não produz alteração. */
    @Transactional
    public void deletar(T entidade) {
        var id = exigirIdentificador(entidade);
        T gerenciada = entityManager.contains(entidade)
                ? entidade
                : entityManager.find(entityClass, id);
        if (gerenciada != null) {
            entityManager.remove(gerenciada);
        }
    }

    private Object exigirIdentificador(T entidade) {
        Objects.requireNonNull(entidade, "A entidade é obrigatória.");
        // PersistenceUnitUtil também obtém o ID de proxies detached sem acessar seus campos.
        var id = entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(entidade);
        if (id == null) {
            throw new IllegalArgumentException("A entidade precisa possuir um identificador.");
        }
        return id;
    }
}
