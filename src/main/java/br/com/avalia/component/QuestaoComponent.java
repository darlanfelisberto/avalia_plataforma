package br.com.avalia.component;

import br.com.avalia.modelo.avaliacao.OpcaoQuestao;
import br.com.avalia.modelo.avaliacao.SessaoQuestao;
import jakarta.faces.application.ResourceDependency;
import jakarta.faces.component.FacesComponent;
import jakarta.faces.component.UIComponentBase;
import jakarta.faces.context.FacesContext;
import jakarta.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.List;

/** Renderização de prévia: não recebe nem persiste respostas. */
@FacesComponent(value="avalia.Questao", createTag=true, namespace="avalia.componentes", tagName="questao")
@ResourceDependency(name="styles/questao.css")
public class QuestaoComponent extends UIComponentBase {
    @Override public String getFamily() { return "avalia.componentes"; }

    public SessaoQuestao getValue() { return (SessaoQuestao) getStateHelper().eval("value"); }
    public void setValue(SessaoQuestao value) { getStateHelper().put("value", value); }
    @SuppressWarnings("unchecked")
    public List<OpcaoQuestao> getOpcoes() { return (List<OpcaoQuestao>) getStateHelper().eval("opcoes", List.of()); }
    public void setOpcoes(List<OpcaoQuestao> opcoes) { getStateHelper().put("opcoes", opcoes); }

    @Override
    public void encodeBegin(FacesContext context) throws IOException {
        if (!isRendered() || getValue()==null) return;
        var vinculo = getValue(); var questao = vinculo.getQuestao();
        var writer = context.getResponseWriter(); String id = getClientId(context);
        writer.startElement("fieldset", this); writer.writeAttribute("id", id, null);
        writer.writeAttribute("class", "questao-preview", null);
        writer.startElement("legend", this);
        writer.writeText(vinculo.getOrdem() + ". " + questao.getEnunciado(), null);
        writer.endElement("legend");
        if (Boolean.TRUE.equals(vinculo.getObrigatoria())) texto(writer, "span", "questao-preview-obrigatoria", "Obrigatória");
        String tipo = questao.getTipo();
        if ("DESCRITIVA".equals(tipo)) {
            writer.startElement("textarea", this); writer.writeAttribute("id", id+"_resposta", null);
            writer.writeAttribute("aria-label", questao.getEnunciado(), null);
            writer.writeAttribute("rows", 4, null); writer.writeAttribute("disabled", true, null);
            writer.endElement("textarea");
        } else if (List.of("ESCALAS", "UNICA_ESCOLHA", "MULTIPLA_ESCOLHA").contains(tipo==null ? "" : tipo)) {
            var opcoes = getOpcoes();
            if (opcoes==null || opcoes.isEmpty()) texto(writer, "p", "questao-preview-aviso", "Nenhuma opção cadastrada.");
            else {
                writer.startElement("div", this);
                writer.writeAttribute("class", "questao-preview-opcoes " +
                        (Integer.valueOf(1).equals(questao.getAlinhamento()) ? "questao-preview-vertical" : "questao-preview-horizontal"), null);
                for (int i=0;i<opcoes.size();i++) {
                    writer.startElement("label", this);
                    writer.startElement("input", this);
                    writer.writeAttribute("type", "MULTIPLA_ESCOLHA".equals(tipo) ? "checkbox" : "radio", null);
                    writer.writeAttribute("name", id+"_resposta", null); writer.writeAttribute("id", id+"_opcao_"+i, null);
                    writer.writeAttribute("disabled", true, null);
                    writer.endElement("input");
                    writer.startElement("span", this); writer.writeText(opcoes.get(i).getTexto(), null); writer.endElement("span");
                    writer.endElement("label");
                }
                writer.endElement("div");
            }
        } else texto(writer, "p", "questao-preview-aviso", "Tipo de questão não reconhecido: " + tipo);
        writer.endElement("fieldset");
    }

    private void texto(ResponseWriter writer, String tag, String classe, String texto) throws IOException {
        writer.startElement(tag, this); writer.writeAttribute("class", classe, null); writer.writeText(texto, null); writer.endElement(tag);
    }
}
