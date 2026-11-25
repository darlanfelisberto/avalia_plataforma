package br.com.avalia.modelo;

import br.com.avalia.modelo.academico.Aluno;
import br.com.avalia.modelo.academico.Cargo;
import br.com.avalia.modelo.academico.UnidadeClosureId;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Id;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.UUID;
import org.hibernate.Hibernate;
import org.hibernate.Session;

final class VerificarIdentidadeModel {

    private VerificarIdentidadeModel() {
    }

    static void validar(Class<?> entityClass, Session session) throws ReflectiveOperationException {
        if (!Model.class.isAssignableFrom(entityClass)) {
            throw new AssertionError(entityClass.getSimpleName() + " deve estender Model");
        }
        var first = (Model<?>) entityClass.getConstructor().newInstance();
        var second = (Model<?>) entityClass.getConstructor().newInstance();
        var third = (Model<?>) entityClass.getConstructor().newInstance();
        check(first.equals(first), "Reflexividade", entityClass);
        check(!first.equals(second), "Instâncias novas sem ID são distintas", entityClass);
        check(!first.equals(null) && !first.equals("id"), "Comparação com outros objetos", entityClass);

        Field field = Arrays.stream(entityClass.getDeclaredFields())
                .filter(f -> f.isAnnotationPresent(Id.class) || f.isAnnotationPresent(EmbeddedId.class))
                .findFirst().orElseThrow();
        field.setAccessible(true);
        field.set(first, identifier(field.getType(), false));
        field.set(second, identifier(field.getType(), false));
        field.set(third, identifier(field.getType(), false));
        check(first.getId().equals(field.get(first)), "getId retorna a chave mapeada", entityClass);
        check(first.equals(second) && second.equals(first), "Simetria", entityClass);
        check(second.equals(third) && first.equals(third), "Transitividade", entityClass);
        check(first.hashCode() == second.hashCode(), "Hash igual para IDs iguais", entityClass);
        var values = new HashSet<Model<?>>();
        values.add(first);
        check(values.contains(second), "Busca em HashSet por identidade", entityClass);
        field.set(third, identifier(field.getType(), true));
        check(!first.equals(third), "IDs diferentes", entityClass);

        // Nenhum INSERT: a referência é criada e destacada sem carregar o registro.
        Object proxy = session.getReference(entityClass, first.getId());
        session.detach(proxy);
        check(!Hibernate.isInitialized(proxy), "Proxy deve iniciar sem carregar", entityClass);
        check(first.equals(proxy) && proxy.equals(first), "Simetria com proxy detached", entityClass);
        check(first.hashCode() == proxy.hashCode(), "Hash compatível com proxy", entityClass);
        check(!Hibernate.isInitialized(proxy), "equals/hashCode não devem carregar o proxy", entityClass);
    }

    static void validarTiposDistintos() {
        var aluno = new Aluno();
        aluno.setId(42);
        var cargo = new Cargo();
        cargo.setId(42);
        if (aluno.equals(cargo) || cargo.equals(aluno)) {
            throw new AssertionError("Entidades de tipos distintos não podem ser iguais pelo mesmo ID");
        }
    }

    private static Object identifier(Class<?> type, boolean different) {
        if (type.isEnum()) {
            return type.getEnumConstants()[different ? 1 : 0];
        }
        if (type == Integer.class) {
            return different ? 43 : 42;
        }
        if (type == UUID.class) {
            return new UUID(0, different ? 43 : 42);
        }
        if (type == UnidadeClosureId.class) {
            return new UnidadeClosureId(42, different ? 43 : 42);
        }
        throw new AssertionError("Tipo de identificador não testado: " + type);
    }

    private static void check(boolean condition, String contract, Class<?> type) {
        if (!condition) {
            throw new AssertionError(contract + ": " + type.getSimpleName());
        }
    }
}
