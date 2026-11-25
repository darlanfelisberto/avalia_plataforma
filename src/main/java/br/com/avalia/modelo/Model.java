package br.com.avalia.modelo;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;
import org.hibernate.proxy.HibernateProxy;

/** Identidade comum às entidades, sem adicionar atributos ao mapeamento JPA. */
public abstract class Model<ID extends Serializable> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    public abstract ID getId();

    @Override
    public final boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof Model<?> other) || entityClass(this) != entityClass(other)) {
            return false;
        }
        var id = identifier(this);
        return id != null && id.equals(identifier(other));
    }

    @Override
    public final int hashCode() {
        return Objects.hash(entityClass(this), identifier(this));
    }

    private static Class<?> entityClass(Model<?> model) {
        var initializer = HibernateProxy.extractLazyInitializer(model);
        return initializer == null ? model.getClass() : initializer.getPersistentClass();
    }

    private static Object identifier(Model<?> model) {
        var initializer = HibernateProxy.extractLazyInitializer(model);
        // O proxy já conhece o ID; acessá-lo diretamente evita carregar uma entidade detached.
        return initializer == null ? model.getId() : initializer.getInternalIdentifier();
    }
}
