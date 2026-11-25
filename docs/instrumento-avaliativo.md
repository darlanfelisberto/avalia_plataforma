# Instrumentos avaliativos

A primeira tela do cadastro é `src/main/webapp/instrumento_avaliativo.xhtml`.
Ela usa o template Bootstrap e um `p:dataTable` do PrimeFaces 16.0.2.

- `InstrumentoAvaliativoDAO`, em `br.com.avalia.dao.avaliacao`, estende
  `DataTableDAO<InstrumentoAvaliativo>`, que herda `InjectEntityDAO`.
- `InstrumentoAvalitivoBean`, em `br.com.avalia.bean.avaliacao`, controla a
  página e é exposto no EL como `instrumentoAvalitivoBean`.
- O nome `InstrumentoAvalitivoBean` mantém a grafia solicitada.
- O bean estende `DataTableBean<InstrumentoAvaliativo>`; filtros e ações comuns
  são herdados. Consulte [a estrutura reutilizável](tabelas-reutilizaveis.md).

## Paginação no banco

O bean mantém um `LazyDataModel<InstrumentoAvaliativo>`, inicializado em
`@PostConstruct`, pela base `DataTableBean`. `count()` usa `DAO.contar(filtros)` e `load()` usa
`DAO.listarPagina(...)`. Os getters apenas retornam o estado; nenhuma consulta
é feita dentro deles. A página não usa `listarTodos()`.

A primeira página apresenta até 10 registros. O paginador oferece 10, 25 e 50
registros por página. O DAO executa `COUNT`, `setFirstResult` e `setMaxResults`,
carregando somente a página solicitada. Ordenação e filtros também são aplicados
no PostgreSQL. A mesma construção de predicados é usada na contagem e na busca.

Há filtros textuais por título e ciclo, sem diferenciação de maiúsculas, e um
`h:selectOneMenu` para status, em um painel responsivo acima da tabela.
Pesquisar aplica os campos e volta à primeira página; Limpar filtros restaura
a listagem completa. O status é filtrado por igualdade do código selecionado.
Os caracteres `%`, `_` e `\` são tratados como texto literal. Campos de filtro
e ordenação passam por listas de campos permitidos. O DAO aceita páginas de
1 a 100 registros e rejeita offsets negativos.

A ordenação inicial usa a atualização mais recente. O UUID é usado como
critério de desempate em todas as ordenações para manter as páginas estáveis.
As colunas permitem mudar a ordenação por AJAX. **Atualizar lista** refaz a
consulta mantendo o estado dos filtros e da tabela. Quando o total diminui,
o paginador do PrimeFaces ajusta a posição da página.

## Interface

A tabela mostra título/descrição, ciclo, status, início e fim da aplicação e
anonimato. O status é exibido pela descrição cadastrada. Para exibição, as datas
`OffsetDateTime` são ajustadas com `atZoneSameInstant` para `America/Sao_Paulo`
e formatadas como `zonedDateTime`.

O painel de filtros e a tabela usam formulários separados. O cabeçalho da tabela contém apenas títulos e ordenação. Os filtros aplicados permanecem ao paginar e ordenar. O botão de atualização
fica em outro formulário, com `process="@this"`. Mensagens usam `redisplay="false"`.
Não há alteração de dados ao paginar, ordenar, filtrar ou atualizar a lista.
A falha de consulta é mostrada após a tabela e pode ser resolvida repetindo a
consulta. A tabela possui rolagem horizontal em telas pequenas; o paginador
permanece fora da região rolável.

O tema base é `saga-blue`, distribuído no JAR do PrimeFaces. A adaptação visual
fica em `WEB-INF/resources/styles/primefaces-overrides.css`, separado dos estilos
da aplicação. Os recursos são servidos localmente pelo Faces.

## Verificação

Execute `.\scripts\Validar-Modelo.ps1` com o PostgreSQL disponível para validar,
em conexão somente leitura, duas páginas reais, limites, ordenação, contagem,
filtros e o tratamento literal dos caracteres de LIKE. Nenhum registro extra é
necessário para os testes de páginas com dois registros.

Esta etapa implementa a listagem; os formulários de inclusão e edição serão
incorporados ao cadastro nas próximas etapas.

## Cadastro de status

A tabela `avaliacao.status` tem `id` inteiro gerado por identidade, `codigo`
único e `descricao`. A entidade `Status` estende `Model<Integer>`; `StatusDAO`
estende `DataTableDAO<Status>` e lista os registros por descrição. Ambos os DAOs
concretos são `@RequestScoped`.

`InstrumentoAvaliativo.status` é uma associação `ManyToOne` sem cascatas.
A coluna existente `instrumento_avaliativo.status` foi preservada e referencia
`status.codigo` por chave estrangeira. O código é estável (`updatable=false`);
a descrição pode ser atualizada. O DAO carrega o status junto à página e ordena
essa coluna pela descrição, evitando consultas durante a renderização.

O menu é carregado pelo DAO no `@PostConstruct`. Seus valores são os códigos,
portanto não precisa de conversor de entidade. “Todos os status” remove o filtro.

A migração `scripts/sql/001-criar-status-instrumento.sql` já foi aplicada à base
local `avalia`. Para outra base, execute esse arquivo uma única vez antes de
publicar a aplicação. Ela preserva todos os códigos existentes, cadastra os três
status iniciais, cria a chave estrangeira e seu índice, dentro de uma transação.

