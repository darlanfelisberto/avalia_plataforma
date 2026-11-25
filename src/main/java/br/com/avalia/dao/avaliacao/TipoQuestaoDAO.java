package br.com.avalia.dao.avaliacao;

import br.com.avalia.dao.DataTableDAO;
import br.com.avalia.modelo.avaliacao.TipoQuestao;
import jakarta.enterprise.context.RequestScoped;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RequestScoped
public class TipoQuestaoDAO extends DataTableDAO<TipoQuestao> {
    public TipoQuestaoDAO() {
        super(TipoQuestao.class);
    }

    @Override
    protected Set<String> camposFiltro() { return Set.of("codigo", "descricao"); }

    @Override
    protected Map<String, String> camposOrdenacao() { return Map.of("codigo", "codigo", "descricao", "descricao"); }

    @Override
    protected String campoIdentificador() { return "id"; }

    @Override
    protected List<Order> ordenacaoPadrao(CriteriaBuilder builder, Root<TipoQuestao> root) {
        return List.of(builder.asc(root.get("descricao")));
    }

    @Override
    protected Predicate criarPredicado(CriteriaBuilder builder, Root<TipoQuestao> root, String campo, String valor) {
        return "codigo".equals(campo) ? builder.equal(root.get(campo), valor)
                : contemIgnoreCase(builder, root.get(campo), valor);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<TipoQuestao> listarTodos() {
        return entityManager.createQuery("from TipoQuestao order by descricao, id", TipoQuestao.class).getResultList();
    }
}
