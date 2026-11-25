package br.com.avalia.dao.avaliacao;

import br.com.avalia.dao.InjectEntityDAO;
import br.com.avalia.modelo.avaliacao.InstrumentoAvaliativo;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.JoinType;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

@RequestScoped
public class InstrumentoAvaliativoDAO extends InjectEntityDAO<InstrumentoAvaliativo> {

    private static final Set<String> CAMPOS_FILTRO = Set.of("titulo", "cicloReferencia", "status");
    private static final Set<String> CAMPOS_ORDENACAO = Set.of(
            "titulo", "cicloReferencia", "status", "inicioAplicacao", "fimAplicacao", "anonimo", "atualizadoEm");

    public InstrumentoAvaliativoDAO() {
        super(InstrumentoAvaliativo.class);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<InstrumentoAvaliativo> listarTodos() {
        return entityManager.createQuery("""
                select instrumento
                from InstrumentoAvaliativo instrumento join fetch instrumento.status
                order by instrumento.atualizadoEm desc, instrumento.instrumentoAvaliativoId
                """, InstrumentoAvaliativo.class).getResultList();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public long contar(Map<String, String> filtros) {
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(Long.class);
        var instrumento = query.from(InstrumentoAvaliativo.class);
        query.select(builder.count(instrumento));
        query.where(predicados(builder, instrumento, filtros));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<InstrumentoAvaliativo> listarPagina(int primeiro, int tamanho, Map<String, String> filtros,
                                                  String campoOrdenacao, boolean crescente) {
        if (primeiro < 0 || tamanho < 1 || tamanho > 100) {
            throw new IllegalArgumentException("Página inválida: início não negativo e tamanho entre 1 e 100 são obrigatórios.");
        }
        if (campoOrdenacao != null && !CAMPOS_ORDENACAO.contains(campoOrdenacao)) {
            throw new IllegalArgumentException("Campo de ordenação não permitido.");
        }
        var builder = entityManager.getCriteriaBuilder();
        var query = builder.createQuery(InstrumentoAvaliativo.class);
        var instrumento = query.from(InstrumentoAvaliativo.class);
        query.select(instrumento).where(predicados(builder, instrumento, filtros));
        instrumento.fetch("status", JoinType.INNER);
        List<Order> ordem = new ArrayList<>();
        if (campoOrdenacao == null) {
            ordem.add(builder.desc(instrumento.get("atualizadoEm")));
        } else {
            var campo = "status".equals(campoOrdenacao)
                    ? instrumento.get("status").get("descricao") : instrumento.get(campoOrdenacao);
            ordem.add(crescente ? builder.asc(campo) : builder.desc(campo));
        }
        ordem.add(builder.asc(instrumento.get("instrumentoAvaliativoId")));
        query.orderBy(ordem);
        return entityManager.createQuery(query).setFirstResult(primeiro).setMaxResults(tamanho).getResultList();
    }

    private Predicate[] predicados(CriteriaBuilder builder, Root<InstrumentoAvaliativo> instrumento,
                                    Map<String, String> filtros) {
        List<Predicate> predicados = new ArrayList<>();
        for (var filtro : filtros.entrySet()) {
            if (!CAMPOS_FILTRO.contains(filtro.getKey())) {
                throw new IllegalArgumentException("Campo de filtro não permitido.");
            }
            var valor = filtro.getValue();
            if (valor != null && !valor.isBlank()) {
                if ("status".equals(filtro.getKey())) {
                    predicados.add(builder.equal(instrumento.get("status").get("codigo"), valor.strip()));
                    continue;
                }
                var busca = valor.strip().toLowerCase(Locale.ROOT)
                        .replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
                predicados.add(builder.like(builder.lower(instrumento.get(filtro.getKey())), "%" + busca + "%", '\\'));
            }
        }
        return predicados.toArray(Predicate[]::new);
    }
}
