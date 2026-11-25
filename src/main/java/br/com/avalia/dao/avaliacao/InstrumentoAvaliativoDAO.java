package br.com.avalia.dao.avaliacao;

import br.com.avalia.dao.DataTableDAO;
import br.com.avalia.modelo.avaliacao.InstrumentoAvaliativo;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequestScoped
public class InstrumentoAvaliativoDAO extends DataTableDAO<InstrumentoAvaliativo> {

    public InstrumentoAvaliativoDAO() {
        super(InstrumentoAvaliativo.class);
    }

    @Override
    protected Set<String> camposFiltro() {
        return Set.of("titulo", "cicloReferencia", "status");
    }

    @Override
    protected Map<String, String> camposOrdenacao() {
        return Map.of("titulo", "titulo", "cicloReferencia", "cicloReferencia", "status", "status.descricao",
                "inicioAplicacao", "inicioAplicacao", "fimAplicacao", "fimAplicacao",
                "anonimo", "anonimo", "atualizadoEm", "atualizadoEm");
    }

    @Override
    protected String campoIdentificador() {
        return "instrumentoAvaliativoId";
    }

    @Override
    protected List<Order> ordenacaoPadrao(CriteriaBuilder builder, Root<InstrumentoAvaliativo> root) {
        return List.of(builder.desc(root.get("atualizadoEm")));
    }

    @Override
    protected void prepararConsulta(Root<InstrumentoAvaliativo> root) {
        root.fetch("status", JoinType.INNER);
    }

    @Override
    protected Predicate criarPredicado(CriteriaBuilder builder, Root<InstrumentoAvaliativo> root, String campo, String valor) {
        return "status".equals(campo)
                ? builder.equal(root.get("status").get("codigo"), valor)
                : contemIgnoreCase(builder, root.get(campo), valor);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<InstrumentoAvaliativo> listarTodos() {
        return entityManager.createQuery("""
                select instrumento from InstrumentoAvaliativo instrumento join fetch instrumento.status
                order by instrumento.atualizadoEm desc, instrumento.instrumentoAvaliativoId
                """, InstrumentoAvaliativo.class).getResultList();
    }
}
