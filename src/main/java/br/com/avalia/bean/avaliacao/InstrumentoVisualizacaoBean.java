package br.com.avalia.bean.avaliacao;

import br.com.avalia.dao.avaliacao.InstrumentoCadastroDAO;
import br.com.avalia.modelo.avaliacao.*;
import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import java.io.Serial;
import java.io.Serializable;
import java.util.*;

@Named
@ViewScoped
public class InstrumentoVisualizacaoBean implements Serializable {
    @Serial private static final long serialVersionUID = 1L;
    @Inject private InstrumentoCadastroDAO dao;
    private UUID id;
    private InstrumentoAvaliativo instrumento;
    private List<InstrumentoSessao> sessoes;
    private Map<UUID,List<SessaoQuestao>> questoesPorSessao;
    private Map<UUID,List<OpcaoQuestao>> opcoesPorQuestao;

    @PostConstruct
    public void inicializar() {
        sessoes = List.of(); questoesPorSessao = new LinkedHashMap<>(); opcoesPorQuestao = new LinkedHashMap<>();
    }

    public String carregar() {
        instrumento = id==null ? null : dao.buscar(id);
        if (instrumento==null) {
            var faces = FacesContext.getCurrentInstance();
            faces.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,"Instrumento não encontrado.",null));
            faces.getExternalContext().getFlash().setKeepMessages(true);
            return "/instrumento_avaliativo?faces-redirect=true";
        }
        sessoes = dao.sessoes(id);
        var questoes = dao.questoesDoInstrumento(id);
        sessoes.forEach(s -> questoesPorSessao.put(s.getId(), new ArrayList<>()));
        Map<UUID,List<OpcaoQuestao>> porQuestao = new HashMap<>();
        Map<UUID,List<OpcaoQuestao>> porConjunto = new HashMap<>();
        for (var opcao : dao.opcoesDoInstrumento(id)) {
            porConjunto.computeIfAbsent(opcao.getConjuntoOpcao().getId(), key -> new ArrayList<>()).add(opcao);
            if (opcao.getQuestao()!=null) porQuestao.computeIfAbsent(opcao.getQuestao().getId(), key -> new ArrayList<>()).add(opcao);
        }
        for (var vinculo : questoes) {
            questoesPorSessao.get(vinculo.getInstrumentoSessao().getId()).add(vinculo);
            var questao = vinculo.getQuestao();
            var opcoes = "ESCALAS".equals(questao.getTipo()) && questao.getConjuntoOpcao()!=null
                    ? porConjunto.getOrDefault(questao.getConjuntoOpcao().getId(), List.of())
                    : porQuestao.getOrDefault(questao.getId(), List.of());
            opcoesPorQuestao.put(questao.getId(), opcoes);
        }
        return null;
    }
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id=id; }
    public InstrumentoAvaliativo getInstrumento() { return instrumento; }
    public List<InstrumentoSessao> getSessoes() { return sessoes; }
    public Map<UUID,List<SessaoQuestao>> getQuestoesPorSessao() { return questoesPorSessao; }
    public Map<UUID,List<OpcaoQuestao>> getOpcoesPorQuestao() { return opcoesPorQuestao; }
}
