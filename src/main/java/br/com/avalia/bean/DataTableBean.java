package br.com.avalia.bean;

import br.com.avalia.dao.DataTableDAO;
import br.com.avalia.modelo.Model;
import jakarta.persistence.PersistenceException;
import java.io.Serial;
import java.io.Serializable;
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

/** Estado por view e adaptação do PrimeFaces para os DAOs paginados. */
public abstract class DataTableBean<T extends Model<?>> implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(DataTableBean.class.getName());

    private Map<String, String> filtros;
    private Map<String, String> filtrosAplicados;
    private LazyDataModel<T> registros;
    private int primeiroRegistro;
    private boolean falhaCarregamento;

    protected abstract DataTableDAO<T> dao();

    /** Chamar no @PostConstruct do bean concreto, depois da injeção dos DAOs. */
    protected void inicializarTabela() {
        filtros = new LinkedHashMap<>();
        filtrosAplicados = new LinkedHashMap<>();
        atualizarLista();
    }

    public void pesquisar() {
        filtrosAplicados.clear();
        filtros.forEach((campo, valor) -> {
            if (valor != null && !valor.isBlank()) {
                filtrosAplicados.put(campo, valor.strip());
            }
        });
        primeiroRegistro = 0;
        atualizarLista();
    }

    public void limparFiltros() {
        filtros.clear();
        pesquisar();
    }

    public void atualizarLista() {
        falhaCarregamento = false;
        registros = new LazyDataModel<>() {
            @Serial
            private static final long serialVersionUID = 1L;

            @Override
            public int count(Map<String, FilterMeta> filterBy) {
                try {
                    int total = Math.toIntExact(dao().contar(filtrosConsulta(filterBy)));
                    falhaCarregamento = false;
                    return total;
                } catch (PersistenceException exception) {
                    registrarFalha(exception);
                    return 0;
                }
            }

            @Override
            public List<T> load(int first, int pageSize, Map<String, SortMeta> sortBy, Map<String, FilterMeta> filterBy) {
                var ordenacao = sortBy.values().stream()
                        .filter(sort -> sort.getOrder() != SortOrder.UNSORTED)
                        .min(Comparator.comparingInt(SortMeta::getPriority)).orElse(null);
                try {
                    return dao().listarPagina(first, pageSize, filtrosConsulta(filterBy),
                            ordenacao == null ? null : ordenacao.getField(),
                            ordenacao != null && ordenacao.getOrder() == SortOrder.ASCENDING);
                } catch (PersistenceException exception) {
                    registrarFalha(exception);
                    return List.of();
                }
            }
        };
    }

    private Map<String, String> filtrosConsulta(Map<String, FilterMeta> filterBy) {
        Map<String, String> consulta = new LinkedHashMap<>(filtrosAplicados);
        for (var filtro : filterBy.values()) {
            if (filtro.getFilterValue() != null && !filtro.getFilterValue().toString().isBlank()) {
                consulta.put(filtro.getField(), filtro.getFilterValue().toString().strip());
            }
        }
        return consulta;
    }

    private void registrarFalha(PersistenceException exception) {
        falhaCarregamento = true;
        LOGGER.log(Level.SEVERE, "Falha ao consultar tabela de " + getClass().getSimpleName(), exception);
    }

    public Map<String, String> getFiltros() { return filtros; }
    public LazyDataModel<T> getRegistros() { return registros; }
    public boolean isFalhaCarregamento() { return falhaCarregamento; }
    public int getPrimeiroRegistro() { return primeiroRegistro; }
    public void setPrimeiroRegistro(int primeiroRegistro) { this.primeiroRegistro = primeiroRegistro; }
}
