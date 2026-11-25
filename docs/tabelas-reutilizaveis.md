# Tabelas paginadas reutilizáveis

O bean estende `DataTableBean<T>` e o DAO estende `DataTableDAO<T>`.
Essa segunda classe estende `InjectEntityDAO<T>`, mantendo as operações de
salvar, atualizar e deletar. Os DAOs concretos usam `@RequestScoped`; beans
de tela usam `@Named` e `@ViewScoped` e herdam `Serializable` da base.

## Bean

Implemente `dao()` retornando o DAO injetado e chame `inicializarTabela()` no
`@PostConstruct`, depois das inicializações específicas da tela. Não consulte
dados em getters. `InstrumentoAvalitivoBean` é o exemplo completo.

Propriedades e ações herdadas:

| Nome | Uso |
| --- | --- |
| `filtros` | Mapa dos campos em edição, com valores textuais |
| `registros` | `LazyDataModel<T>` usado no `p:dataTable` |
| `primeiroRegistro` | Posição ligada ao atributo `first` da tabela |
| `falhaCarregamento` | Indicador para mostrar erro de consulta |
| `pesquisar()` | Aplica uma cópia dos filtros, volta ao início e refaz o modelo |
| `limparFiltros()` | Limpa a edição e os filtros aplicados e volta ao início |
| `atualizarLista()` | Refaz o modelo usando a última pesquisa aplicada |

Exemplo de bindings, dentro dos formulários correspondentes:

```xml
<h:inputText value="#{meuBean.filtros['titulo']}"/>
<p:commandButton value="Pesquisar" action="#{meuBean.pesquisar}"
                 process="@form" update=":listaForm"/>

<p:dataTable value="#{meuBean.registros}" var="item" lazy="true"
             first="#{meuBean.primeiroRegistro}" paginator="true" rows="10"
             rowsPerPageTemplate="10,25,50" sortMode="single">
    <p:column field="titulo" headerText="Título" sortBy="#{item.titulo}" filterable="false">
        <h:outputText value="#{item.titulo}"/>
    </p:column>
</p:dataTable>
```

Mantenha os filtros em formulário separado da listagem. Para limpar, use
`process="@this"`, atualize ambos os formulários e use `resetValues="true"`.
Editar um campo sem pesquisar não altera a consulta ao paginar ou ordenar.
Também há suporte à leitura de `FilterMeta` do PrimeFaces para telas que optem
por filtros nas colunas; esses valores prevalecem sobre filtros externos da
mesma chave. A estrutura usa uma ordenação por vez.

## DAO

O construtor passa a classe da entidade para `super`. Cada DAO implementa:

| Método protegido | Responsabilidade |
| --- | --- |
| `camposFiltro()` | Chaves de filtro permitidas |
| `camposOrdenacao()` | Mapa da chave da coluna para o caminho da propriedade |
| `campoIdentificador()` | Caminho da chave usado como desempate estável |
| `ordenacaoPadrao(builder, root)` | Ordem inicial, sem precisar repetir o desempate |
| `criarPredicado(builder, root, campo, valor)` | Regra de cada filtro não vazio |
| `prepararConsulta(root)` | Opcional: fetch de associações to-one para exibição |

`contar(filtros)` e `listarPagina(primeiro, tamanho, filtros, campo, crescente)`
são herdados e compartilham os mesmos predicados. A base rejeita campos não
permitidos, offsets negativos e tamanhos fora de 1 a 100. Caminhos como
`status.descricao` são definidos pelo DAO, nunca aceitos diretamente da tela.

`contemIgnoreCase(builder, campo, valor)` monta um LIKE sem diferenciar
maiúsculas e trata `%`, `_` e `\` como texto literal. Comparações exatas, datas,
intervalos e regras adicionais pertencem a `criarPredicado`; os filtros desta
base são textuais, então converta valores para o tipo da propriedade quando
necessário. Não passe objetos de entidade ou `FilterMeta` ao DAO.

Não faça fetch de coleções na consulta paginada: isso pode multiplicar linhas
e impedir a paginação SQL. Os predicados também devem preservar uma linha por
entidade; filtros por coleções devem usar subconsultas/EXISTS. O hook de fetch
é chamado apenas para a consulta de dados, nunca para COUNT.

`InstrumentoAvaliativoDAO` demonstra filtros textuais, seleção exata de status,
ordenação por associação e fetch. `StatusDAO` reutiliza a mesma base com outra
entidade. Nenhuma classe do pacote DAO depende de PrimeFaces.

## Verificação

`.\scripts\Validar-Modelo.ps1` testa, em conexão somente leitura, paginação SQL,
contagem, ordenação, filtros e limites de instrumentos, reutilização com Status
e o estado do bean (edição, pesquisa, atualização, retorno ao início e limpeza).
