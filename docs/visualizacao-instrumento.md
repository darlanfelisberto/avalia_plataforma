# Visualização do instrumento

A listagem oferece **Visualizar**, que abre `instrumento_avaliativo_visualizar.xhtml?id=UUID`.
`InstrumentoVisualizacaoBean` carrega os dados por `f:viewAction` no GET, sem
consultas nos getters. Sessões, questões e opções são carregadas em lote e
agrupadas por identificador. O acesso a instrumento inexistente retorna à listagem
com mensagem. Esta página não grava respostas.

O componente nativo `QuestaoComponent` estende `UIComponentBase` e é registrado
por `@FacesComponent(createTag=true)`. Uso em qualquer Facelet:

```xml
<!-- Declare xmlns:a="avalia.componentes" na composição. -->
<a:questao id="questao" value="#{vinculoSessaoQuestao}" opcoes="#{opcoesDaQuestao}"/>
```

`value` recebe `SessaoQuestao`; `opcoes` recebe `List<OpcaoQuestao>`. A página
fornece as opções de `conjuntoOpcao` para ESCALAS e as ligadas diretamente à
questão para UNICA_ESCOLHA/MULTIPLA_ESCOLHA. O componente não acessa DAOs.

DESCRITIVA produz uma área de texto; ESCALAS e UNICA_ESCOLHA produzem radio
buttons; MULTIPLA_ESCOLHA produz checkboxes. Os controles são desabilitados para
prévia. Não há decodificação, validação ou gravação de respostas. Todos os textos
são escapados pelo ResponseWriter; IDs e grupos usam o client ID do componente,
inclusive dentro de ui:repeat. O CSS é carregado por @ResourceDependency.

Tipos legados desconhecidos são informados na tela, sem alterar o banco. Falta
de opções ou de sessões também tem indicação explícita. As opções corretas e os
valores de pontuação não são expostos na prévia.
