package br.com.avalia.bean.avaliacao;

import br.com.avalia.dao.avaliacao.InstrumentoCadastroDAO;
import br.com.avalia.dao.avaliacao.StatusDAO;
import br.com.avalia.dao.avaliacao.TipoQuestaoDAO;
import br.com.avalia.modelo.avaliacao.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Named
@ViewScoped
public class InstrumentoAvaliativoCadastroBean implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    private static final ZoneId ZONA = ZoneId.of("America/Sao_Paulo");
    private static final Logger LOG = Logger.getLogger(InstrumentoAvaliativoCadastroBean.class.getName());
    @Inject private InstrumentoCadastroDAO dao;
    @Inject private StatusDAO statusDAO;
    @Inject private TipoQuestaoDAO tipoQuestaoDAO;
    private List<TipoQuestao> tiposQuestao;
    private UUID id;
    private UUID instrumentoSalvoId;
    private InstrumentoAvaliativo instrumento;
    private String codigoStatus;
    private Integer pessoaId;
    private LocalDateTime inicio;
    private LocalDateTime fim;
    private LocalDateTime consolidado;
    private List<Status> statusDisponiveis;
    private List<TipoEntidade> tipos;
    private List<ConjuntoOpcao> conjuntos;
    private List<InstrumentoSessao> sessoes;
    private Map<UUID, List<SessaoQuestao>> questoesPorSessao;
    private List<SessaoQuestao> questoes;
    private InstrumentoSessao sessao;
    private UUID sessaoSelecionadaId;
    private String tituloSessaoSelecionada;
    private SessaoQuestao questao;
    private UUID conjuntoId;

    @PostConstruct
    public void inicializar() {
        tiposQuestao = tipoQuestaoDAO.listarTodos();
        instrumento = new InstrumentoAvaliativo();
        instrumento.setAnonimo(true); instrumento.setPermiteEdicao(true);
        codigoStatus = "RASCUNHO";
        statusDisponiveis = statusDAO.listarTodos(); tipos = dao.tiposAvaliados(); conjuntos = dao.conjuntos();
        sessoes = List.of(); questoesPorSessao = new LinkedHashMap<>(); questoes = List.of(); novaSessao(); novaQuestao();
    }

    public String carregar() {
        if (id==null) return null;
        var encontrado = dao.buscar(id);
        if (encontrado==null) {
            mensagem(FacesMessage.SEVERITY_ERROR, "Instrumento não encontrado.");
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            return "/instrumento_avaliativo?faces-redirect=true";
        }
        instrumento = encontrado; instrumentoSalvoId = encontrado.getId();
        codigoStatus = encontrado.getStatus().getCodigo();
        pessoaId = encontrado.getPessoa()==null ? null : encontrado.getPessoa().getId();
        inicio = local(encontrado.getInicioAplicacao()); fim = local(encontrado.getFimAplicacao());
        consolidado = local(encontrado.getConsolidadoEm());
        carregarEstrutura();
        return null;
    }

    public String salvar() {
        boolean novo = !isSalvo();
        executar(() -> {
            instrumento.setInicioAplicacao(offset(inicio)); instrumento.setFimAplicacao(offset(fim));
            instrumento.setConsolidadoEm(offset(consolidado));
            var salvo = dao.gravar(instrumento, codigoStatus, pessoaId);
            instrumento = salvo; instrumentoSalvoId = salvo.getId(); id = salvo.getId();
            mensagem(FacesMessage.SEVERITY_INFO, "Instrumento salvo. A aba Sessões e questões está disponível.");
        });
        if (novo && isSalvo()) {
            FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
            return "/instrumento_avaliativo_cadastro?id=" + instrumentoSalvoId + "&faces-redirect=true";
        }
        return null;
    }

    public void novaSessao() {
        sessao = new InstrumentoSessao(); sessao.setOrdem(sessoes.size()+1); sessao.setTipo("SESSAO");
    }

    public void editarSessao(InstrumentoSessao item) {
        sessao = new InstrumentoSessao(); sessao.setInstrumentoSessaoId(item.getId());
        sessao.setTitulo(item.getTitulo()); sessao.setDescricao(item.getDescricao()); sessao.setOrdem(item.getOrdem());
        sessao.setTipo(item.getTipo()); sessao.setTiposessao(item.getTiposessao());
    }

    public void salvarSessao() {
        executar(() -> {
            var salva = dao.gravarSessao(instrumentoSalvoId, sessao);
            carregarEstrutura(); sessaoSelecionadaId = salva.getId();
            selecionarSessao(); novaSessao();
            org.primefaces.PrimeFaces.current().ajax().update("abas:sessoesForm", "questoesDialogForm");
            mensagem(FacesMessage.SEVERITY_INFO, "Sessão salva. Cadastre suas questões abaixo.");
        });
    }

    public void abrirQuestoes(InstrumentoSessao item) {
        sessaoSelecionadaId = item.getId();
        tituloSessaoSelecionada = item.getTitulo();
        selecionarSessao();
    }

    private void carregarEstrutura() {
        sessoes = dao.sessoes(instrumentoSalvoId);
        Map<UUID, List<SessaoQuestao>> agrupadas = new LinkedHashMap<>();
        sessoes.forEach(s -> agrupadas.put(s.getId(), new ArrayList<>()));
        for (var vinculo : dao.questoesDoInstrumento(instrumentoSalvoId)) {
            agrupadas.get(vinculo.getInstrumentoSessao().getId()).add(vinculo);
        }
        questoesPorSessao = agrupadas;
    }

    public void selecionarSessao() {
        questoes = sessaoSelecionadaId==null ? List.of() : dao.questoes(instrumentoSalvoId, sessaoSelecionadaId);
        novaQuestao();
    }

    public void novaQuestao() {
        questao = new SessaoQuestao(); questao.setQuestao(new Questao());
        questao.setObrigatoria(true); questao.setOrdem(questoes.size()+1); conjuntoId = null;
    }

    public void editarQuestao(SessaoQuestao item) {
        questao = new SessaoQuestao(); questao.setSessaoQuestaoId(item.getId());
        questao.setOrdem(item.getOrdem()); questao.setObrigatoria(item.getObrigatoria()); questao.setPeso(item.getPeso());
        var q = new Questao(); q.setEnunciado(item.getQuestao().getEnunciado()); q.setAssunto(item.getQuestao().getAssunto());
        q.setTipo(item.getQuestao().getTipo()); questao.setQuestao(q);
        conjuntoId = item.getQuestao().getConjuntoOpcao()==null ? null : item.getQuestao().getConjuntoOpcao().getId();
    }

    public void salvarQuestao() {
        executar(() -> {
            dao.gravarQuestao(instrumentoSalvoId, sessaoSelecionadaId, questao,
                    TipoQuestaoId.ESCALAS.name().equals(questao.getQuestao().getTipo()) ? conjuntoId : null);
            carregarEstrutura();
            org.primefaces.PrimeFaces.current().ajax().update("abas:sessoesForm");
            selecionarSessao(); mensagem(FacesMessage.SEVERITY_INFO, "Questão salva na sessão.");
        });
    }

    public void alterarTipoQuestao() {
        if (!TipoQuestaoId.ESCALAS.name().equals(questao.getQuestao().getTipo())) {
            conjuntoId = null;
        }
    }

    private void executar(Runnable acao) {
        try { acao.run(); }
        catch (IllegalArgumentException e) { erro(e.getMessage()); }
        catch (jakarta.persistence.PersistenceException | jakarta.transaction.TransactionalException e) {
            LOG.log(Level.SEVERE, "Falha ao salvar cadastro de instrumento", e);
            erro("Não foi possível salvar. Confira os dados e tente novamente.");
        }
    }

    private void erro(String texto) { FacesContext.getCurrentInstance().validationFailed(); mensagem(FacesMessage.SEVERITY_ERROR, texto); }
    private void mensagem(FacesMessage.Severity nivel, String texto) { FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(nivel, texto, null)); }
    private LocalDateTime local(OffsetDateTime data) { return data==null ? null : data.atZoneSameInstant(ZONA).toLocalDateTime(); }
    private OffsetDateTime offset(LocalDateTime data) { return data==null ? null : data.atZone(ZONA).toOffsetDateTime(); }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id=id; }
    public boolean isSalvo() { return instrumentoSalvoId!=null; }
    public InstrumentoAvaliativo getInstrumento() { return instrumento; }
    public String getCodigoStatus() { return codigoStatus; }
    public void setCodigoStatus(String valor) { codigoStatus=valor; }
    public Integer getPessoaId() { return pessoaId; }
    public void setPessoaId(Integer valor) { pessoaId=valor; }
    public LocalDateTime getInicio() { return inicio; }
    public void setInicio(LocalDateTime valor) { inicio=valor; }
    public LocalDateTime getFim() { return fim; }
    public void setFim(LocalDateTime valor) { fim=valor; }
    public LocalDateTime getConsolidado() { return consolidado; }
    public void setConsolidado(LocalDateTime valor) { consolidado=valor; }
    public List<Status> getStatusDisponiveis() { return statusDisponiveis; }
    public List<TipoQuestao> getTiposQuestao() { return tiposQuestao; }
    public List<TipoEntidade> getTipos() { return tipos; }
    public List<ConjuntoOpcao> getConjuntos() { return conjuntos; }
    public List<InstrumentoSessao> getSessoes() { return sessoes; }
    public Map<UUID, List<SessaoQuestao>> getQuestoesPorSessao() { return questoesPorSessao; }
    public List<SessaoQuestao> getQuestoes() { return questoes; }
    public InstrumentoSessao getSessao() { return sessao; }
    public SessaoQuestao getQuestao() { return questao; }
    public UUID getSessaoSelecionadaId() { return sessaoSelecionadaId; }
    public String getTituloSessaoSelecionada() { return tituloSessaoSelecionada; }
    public void setSessaoSelecionadaId(UUID valor) { sessaoSelecionadaId=valor; }
    public UUID getConjuntoId() { return conjuntoId; }
    public void setConjuntoId(UUID valor) { conjuntoId=valor; }
}
