package br.com.avalia.modelo;

import br.com.avalia.modelo.academico.Aluno;
import br.com.avalia.modelo.academico.UnidadeClosureId;
import jakarta.persistence.Entity;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;

/** Validação de integração, executada explicitamente por scripts/Validar-Modelo.ps1. */
public final class ValidarModelo {

    private ValidarModelo() {
    }

    public static void main(String[] args) throws Exception {
        VerificarIdentidadeModel.validarTiposDistintos();
        var registryBuilder = new StandardServiceRegistryBuilder()
                .applySetting("hibernate.connection.driver_class", "org.postgresql.Driver")
                .applySetting("hibernate.connection.url", "jdbc:postgresql://"
                        + environment("DB_HOST", "localhost") + ":" + environment("DB_PORT", "5432")
                        + "/" + environment("DB_NAME", "avalia")
                        + "?options=-c%20default_transaction_read_only%3Don&connectTimeout=10&socketTimeout=30")
                .applySetting("hibernate.connection.username", environment("DB_USER", "postgres"))
                .applySetting("hibernate.connection.password", environment("DB_PASSWORD", "postgres"))
                .applySetting("hibernate.hbm2ddl.auto", "validate")
                .applySetting("hibernate.connection.pool_size", "1")
                .applySetting("hibernate.show_sql", "false");
        var registry = registryBuilder.build();
        try {
            var metadata = new MetadataSources(registry);
            var mappedTables = new HashSet<String>();
            var classesRoot = Path.of(Aluno.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            try (var paths = Files.walk(classesRoot.resolve("br/com/avalia/modelo"))) {
                for (var path : paths.filter(p -> p.toString().endsWith(".class")).sorted().toList()) {
                    var name = classesRoot.relativize(path).toString()
                            .replace('\\', '.').replace('/', '.').replaceAll("\\.class$", "");
                    var entityClass = Class.forName(name);
                    if (entityClass.isAnnotationPresent(Entity.class)) {
                        metadata.addAnnotatedClass(entityClass);
                        var table = entityClass.getAnnotation(jakarta.persistence.Table.class);
                        mappedTables.add(table.schema() + "." + table.name());
                    }
                }
            }
            if (mappedTables.isEmpty()) {
                throw new IllegalStateException("Nenhuma entidade encontrada");
            }
            try (var factory = metadata.buildMetadata().buildSessionFactory();
                 var session = factory.openSession()) {
                session.setDefaultReadOnly(true);
                var transaction = session.beginTransaction();
                try {
                    session.doWork(connection -> {
                        Set<String> existingTables = new HashSet<>();
                        try (var statement = connection.createStatement();
                             var result = statement.executeQuery("select schemaname || '.' || tablename "
                                     + "from pg_catalog.pg_tables where schemaname in ('academico', 'avaliacao')")) {
                            while (result.next()) {
                                existingTables.add(result.getString(1));
                            }
                        }
                        if (!existingTables.equals(mappedTables)) {
                            throw new IllegalStateException("Tabelas e entidades divergem: banco="
                                    + existingTables + "; entidades=" + mappedTables);
                        }
                        try (var statement = connection.createStatement();
                             var result = statement.executeQuery("show transaction_read_only")) {
                            result.next();
                            if (!"on".equals(result.getString(1))) {
                                throw new IllegalStateException("A conexão deve ser somente leitura");
                            }
                        }
                    });
                    for (var entity : factory.getMetamodel().getEntities().stream()
                            .sorted(java.util.Comparator.comparing(jakarta.persistence.metamodel.EntityType::getName))
                            .toList()) {
                        VerificarIdentidadeModel.validar(entity.getJavaType(), session);
                        session.createQuery("from " + entity.getName(), entity.getJavaType())
                                .setMaxResults(1).getResultList();
                        System.out.println("Identidade, proxy, mapeamento e leitura OK: " + entity.getName());
                    }
                } finally {
                    transaction.rollback();
                }
            }
            var first = new UnidadeClosureId(1, 2);
            var same = new UnidadeClosureId(1, 2);
            var different = new UnidadeClosureId(2, 1);
            if (!first.equals(same) || first.hashCode() != same.hashCode() || first.equals(different)) {
                throw new IllegalStateException("Contrato da chave composta inválido");
            }
            System.out.println("Validação concluída: " + mappedTables.size()
                    + " entidades, schema compatível, consultas somente leitura e chave composta válidos.");
        } finally {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    private static String environment(String name, String fallback) {
        var value = System.getenv(name);
        return value == null || value.isBlank() ? fallback : value;
    }
}
