package br.com.avalia.bean.avaliacao;

import br.com.avalia.bean.DataTableBean;
import br.com.avalia.dao.avaliacao.InstrumentoAvaliativoDAO;
import br.com.avalia.dao.avaliacao.StatusDAO;
import br.com.avalia.modelo.avaliacao.InstrumentoAvaliativo;
import br.com.avalia.modelo.avaliacao.Status;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serial;
import java.time.ZoneId;
import java.util.List;

@Named("instrumentoAvalitivoBean")
@ViewScoped
public class InstrumentoAvalitivoBean extends DataTableBean<InstrumentoAvaliativo> {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final ZoneId FUSO_HORARIO = ZoneId.of("America/Sao_Paulo");

    @Inject
    private InstrumentoAvaliativoDAO instrumentoAvaliativoDAO;

    @Inject
    private StatusDAO statusDAO;

    private List<Status> statusDisponiveis;

    @PostConstruct
    public void inicializar() {
        statusDisponiveis = statusDAO.listarTodos();
        inicializarTabela();
    }

    @Override
    protected InstrumentoAvaliativoDAO dao() {
        return instrumentoAvaliativoDAO;
    }

    public List<Status> getStatusDisponiveis() { return statusDisponiveis; }
    public ZoneId getFusoHorario() { return FUSO_HORARIO; }
}
