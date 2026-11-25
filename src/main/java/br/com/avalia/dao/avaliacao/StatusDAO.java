package br.com.avalia.dao.avaliacao;

import br.com.avalia.dao.InjectEntityDAO;
import br.com.avalia.modelo.avaliacao.Status;
import jakarta.enterprise.context.RequestScoped;
import jakarta.transaction.Transactional;
import java.util.List;

@RequestScoped
public class StatusDAO extends InjectEntityDAO<Status> {
    public StatusDAO() {
        super(Status.class);
    }

    @Transactional(Transactional.TxType.SUPPORTS)
    public List<Status> listarTodos() {
        return entityManager.createQuery("from Status order by descricao, id", Status.class).getResultList();
    }
}
