package br.com.avalia.bean.avaliacao;

import br.com.avalia.dao.avaliacao.InstrumentoAvaliativoDAO;
import br.com.avalia.dao.avaliacao.StatusDAO;
import br.com.avalia.modelo.avaliacao.InstrumentoAvaliativo;
import br.com.avalia.modelo.avaliacao.Status;
import jakarta.annotation.PostConstruct;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.PersistenceException;
import java.io.Serial;
import java.io.Serializable;
import java.time.ZoneId;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;
import org.primefaces.model.SortOrder;

@Named("instrumentoAvalitivoBean")
@ViewScoped
public class InstrumentoAvalitivoBean implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private static final Logger LOGGER = Logger.getLogger(InstrumentoAvalitivoBean.class.getName());
    private static final ZoneId FUSO_HORARIO = ZoneId.of("America/Sao_Paulo");

    @Inject
    private InstrumentoAvaliativoDAO instrumentoAvaliativoDAO;

    @Inject
    private StatusDAO statusDAO;

    private List<Status> statusDisponiveis;

    private LazyDataModel<InstrumentoAvaliativo> instrumentos;
    private boolean falhaCarregamento;
    private String titulo;
    private String cicloReferencia;
    private String status;
    private Map<String, String> filtrosAplicados;
    private int primeiroRegistro;

    @PostConstruct
    public void inicializar() {
        statusDisponiveis = statusDAO.listarTodos();
        filtrosAplicados = new LinkedHashMap<>();
        atualizarLista();
    }

    public void pesquisar() {
        filtrosAplicados.clear();
        adicionarFiltro("titulo", titulo);
        adicionarFiltro("cicloReferencia", cicloReferencia);
        adicionarFiltro("status", status);
        primeiroRegistro = 0;
        atualizarLista();
    }

    public void limparFiltros() {
        titulo = null;
        cicloReferencia = null;
        status = null;
        pesquisar();
    }

    private void adicionarFiltro(String campo, String valor) {
        if (valor != null && !valor.isBlank()) {
            filtrosAplicados.put(campo, valor.strip());
        }
    }

    public void atualizarLista() {
        falhaCarregamento = false;
        instrumentos = new LazyDataModel<>() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                try {
                    int total = Math.toIntExact(instrumentoAvaliativoDAO.contar(filtros(filterBy)));
                    falhaCarregamento = false;
                    return total;
                } catch (PersistenceException exception) {
                    registrarFalha(exception);
                    return 0;
                }
            }

            @Override
            public List<InstrumentoAvaliativo> load(int first, int pageSize,
                    Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                var ordenacao = sortBy.values().stream()
                        .filter(sort -> sort.getOrder() != SortOrder.UNSORTED)
                        .min(Comparator.comparingInt(SortMeta::getPriority)).orElse(null);
                try {
                    return instrumentoAvaliativoDAO.listarPagina(first, pageSize, filtros(filterBy),
                            ordenacao == null ? null : ordenacao.getField(),
                            ordenacao != null && ordenacao.getOrder() == SortOrder.ASCENDING);
                } catch (PersistenceException exception) {
                    registrarFalha(exception);
                    return List.of();
                }
            }
        };
    }

    private Map<String, String> filtros(Map<String, FilterMeta> filterBy) {
        Map<String, String> filtros = new LinkedHashMap<>(filtrosAplicados);
        for (var filtro : filterBy.values()) {
            if (filtro.getFilterValue() != null && !filtro.getFilterValue().toString().isBlank()) {
                filtros.put(filtro.getField(), filtro.getFilterValue().toString());
            }
        }
        return filtros;
    }

    private void registrarFalha(PersistenceException exception) {
        falhaCarregamento = true;
        LOGGER.log(Level.SEVERE, "Falha ao consultar a página de instrumentos avaliativos", exception);
    }

    public LazyDataModel<InstrumentoAvaliativo> getInstrumentos() {
        return instrumentos;
    }

    public boolean isFalhaCarregamento() {
        return falhaCarregamento;
    }

    public ZoneId getFusoHorario() {
        return FUSO_HORARIO;
    }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getCicloReferencia() { return cicloReferencia; }
    public void setCicloReferencia(String cicloReferencia) { this.cicloReferencia = cicloReferencia; }
    public String getStatus() { return status; }
    public List<Status> getStatusDisponiveis() { return statusDisponiveis; }
    public void setStatus(String status) { this.status = status; }
    public int getPrimeiroRegistro() { return primeiroRegistro; }
    public void setPrimeiroRegistro(int primeiroRegistro) { this.primeiroRegistro = primeiroRegistro; }
}
