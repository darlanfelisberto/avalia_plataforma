package br.com.avalia.dao;

import br.com.avalia.modelo.Model;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/** Consultas paginadas sem dependência do PrimeFaces. Os DAOs concretos definem os campos permitidos. */
public abstract class DataTableDAO<T extends Model<?>> extends InjectEntityDAO<T> {

    public static final int TAMANHO_MAXIMO = 100;

    protected DataTableDAO(Class<T> entityClass) {
        super(entityClass);
    }

    protected abstract Set<String> camposFiltro();
    protected abstract Map<String, String> camposOrdenacao();
    protected abstract String campoIdentificador();
    protected abstract List<Order> ordenacaoPadrao(CriteriaBuilder builder, Root<T> root);
    protected abstract Predicate criarPredicado(CriteriaBuilder builder, Root<T> root, String campo, String valor);

    /** Carregue somente associações to-one aqui; fetch de coleções quebra a paginação SQL. */
    protected void prepararConsulta(Root<T> root) {
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public long contar(Map<String, String> filtros) {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(Long.class);
        var root = query.from(getEntityClass());
        query.select(builder.count(root)).where(predicados(builder, root, filtros));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<T> listarPagina(int primeiro, int tamanho, Map<String, String> filtros,
                               String campoOrdenacao, boolean crescente) {
        if (primeiro < 0 || tamanho < 1 || tamanho > TAMANHO_MAXIMO) {
            throw new IllegalArgumentException("Página inválida: início não negativo e tamanho entre 1 e " + TAMANHO_MAXIMO + ".");
        }
        String caminho = campoOrdenacao == null ? null : camposOrdenacao().get(campoOrdenacao);
        if (campoOrdenacao != null && caminho == null) {
            throw new IllegalArgumentException("Campo de ordenação não permitido.");
        }
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(getEntityClass());
        var root = query.from(getEntityClass());
        query.select(root).where(predicados(builder, root, filtros));
        prepararConsulta(root);
        List<Order> ordem = new ArrayList<>();
        if (caminho == null) {
            ordem.addAll(ordenacaoPadrao(builder, root));
        } else {
            var campo = resolverCaminho(root, caminho);
            ordem.add(crescente ? builder.asc(campo) : builder.desc(campo));
        }
        ordem.add(builder.asc(resolverCaminho(root, campoIdentificador())));
        query.orderBy(ordem);
        return entityManager.createQuery(query).setFirstResult(primeiro).setMaxResults(tamanho).getResultList();
    }

    private Predicate[] predicados(CriteriaBuilder builder, Root<T> root, Map<String, String> filtros) {
        List<Predicate> predicados = new ArrayList<>();
        for (var filtro : filtros.entrySet()) {
            if (!camposFiltro().contains(filtro.getKey())) {
                throw new IllegalArgumentException("Campo de filtro não permitido.");
            }
            if (filtro.getValue() != null && !filtro.getValue().isBlank()) {
                predicados.add(criarPredicado(builder, root, filtro.getKey(), filtro.getValue().strip()));
            }
        }
        return predicados.toArray(Predicate[]::new);
    }

    protected Predicate contemIgnoreCase(CriteriaBuilder builder, Expression<String> campo, String valor) {
        var busca = valor.toLowerCase(Locale.ROOT)
                .replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
        return builder.like(builder.lower(campo), "%" + busca + "%", '\\');
    }

    private Path<?> resolverCaminho(Root<T> root, String caminho) {
        Path<?> path = root;
        for (String parte : caminho.split("\\.")) {
            path = path.get(parte);
        }
        return path;
    }
}
