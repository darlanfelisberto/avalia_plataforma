# Avalia Plataforma

Aplicação WAR com Java 21, APIs Jakarta EE 11 e Jakarta Faces 4.1.
Servidor Open Liberty 26.0.0.11, gerenciado pelo Liberty Maven Plugin 3.12.5.
As APIs Jakarta têm escopo `provided`: a implementação é fornecida pelo servidor.
Hibernate ORM 7.4.17.Final é o provedor de Jakarta Persistence 3.2.

## Ambiente local no Windows

No PowerShell, a partir da raiz do projeto:

```powershell
.\scripts\Preparar-Ambiente.ps1
.\scripts\mvn.ps1 package liberty:dev
```

O primeiro comando baixa Temurin JDK 21 e Maven 3.9.11 para `.tools`, verificando
os checksums publicados. Nenhuma instalação global ou alteração de PATH é feita.
O Maven armazena as dependências em `.m2/repository` dentro do projeto.
O modo de desenvolvimento instala o Liberty e suas features, compila e publica
a aplicação, acompanhando alterações. Use Ctrl+C para encerrar o modo dev.

O `package` prepara as bibliotecas compartilhadas antes de iniciar o modo dev.
Se já possuir JDK 21 e Maven 3.9.11 ou posterior, use `mvn package liberty:dev` diretamente.

## Executar e parar em segundo plano

```powershell
.\scripts\mvn.ps1 clean package liberty:create liberty:install-feature liberty:deploy liberty:start
.\scripts\mvn.ps1 liberty:stop
```

Acesse http://localhost:8080/avalia/ após a inicialização.
O listener fica restrito a `localhost`. Portas padrão: HTTP 8080 e HTTPS 9443.
A porta HTTPS requer configuração de certificado/keystore antes de ser utilizada.
As portas podem ser sobrescritas por variáveis do Liberty, como `HTTP_PORT` e `HTTPS_PORT`.

## Arquivos principais

- `pom.xml`: dependências, geração do WAR e versão do servidor.
- `src/main/liberty/config/server.xml`: features, aplicação, portas e logs.
- `src/main/liberty/config/jvm.options`: memória e codificação da JVM.
- `src/main/webapp/WEB-INF`: configuração de Faces 4.1, Servlet 6.1 e CDI 4.1.
- `src/main/webapp/index.xhtml`: página mínima para verificar o processamento Faces.
- `target/liberty/wlp/usr/servers/avaliaServer/logs/messages.log`: log do servidor.

Esta configuração usa `jakarta.faces.PROJECT_STAGE=Development` para desenvolvimento
local. Para produção, altere para `Production` e configure TLS e os demais requisitos
do ambiente. A interface usa Bootstrap 5.3.9 com tema próprio e recursos locais.
Veja [como reutilizar o template e integrar o usuário](docs/layout.md).

## PostgreSQL e Hibernate

O servidor usa features explícitas, incluindo `persistenceContainer-3.2`,
`bells-1.0` e `xmlBinding-4.0`. Não habilite `webProfile-11.0` ou
`persistence-3.2`: essas features ativam o EclipseLink.
O Hibernate é registrado como provedor padrão e compartilhado com a aplicação.
O `package` do Maven copia Hibernate e suas dependências para
`target/liberty/wlp/usr/shared/resources/hibernate` e o driver PostgreSQL
42.7.14 para `target/liberty/wlp/usr/shared/resources/postgresql`.
Os JARs do provedor, driver e APIs Jakarta não são incluídos no WAR.

Configuração local do datasource:

| Propriedade | Valor |
| --- | --- |
| JNDI | `jdbc/avalia` |
| Host e porta | `localhost:5432` |
| Banco | `avalia` |
| Usuário | `postgres` |
| Senha local | `postgres` |
| Pool | até 20 conexões |
| Unidade de persistência | `avaliaPU` (JTA) |

O PostgreSQL deve estar iniciado e a base `avalia` deve existir. A aplicação não
cria a base nem cria, atualiza ou remove tabelas automaticamente.
As variáveis de ambiente `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER` e `DB_PASSWORD`
sobrescrevem os valores locais de `server.xml`.

Nos serviços CDI/EJB, use `@PersistenceContext(unitName = "avaliaPU")` para obter
um `EntityManager`, e `@Transactional` nos métodos que alteram dados.
A integração JTA usa `WebSphereLibertyJtaPlatform`. O datasource é XA;
transações distribuídas com mais de um recurso podem exigir habilitar transações
preparadas no PostgreSQL. Transações com apenas esse datasource não precisam disso.

## Entidades do banco existente

As 29 tabelas foram mapeadas em `src/main/java/br/com/avalia/modelo`:

- `academico`: 15 entidades e a chave composta `UnidadeClosureId`.
- `avaliacao`: 14 entidades.

As classes usam anotações Jakarta Persistence, construtores sem argumentos,
getters e setters explícitos, sem Lombok. Os 35 relacionamentos declarados no
banco usam `@ManyToOne(fetch = LAZY)`, sem cascatas de remoção na aplicação.
As tabelas de associação também possuem suas próprias entidades.

Os IDs inteiros das tabelas acadêmicas são atribuídos externamente, exceto
`EtlLog`, cujo ID usa a sequência existente por meio de `IDENTITY`.
Os IDs UUID com default `gen_random_uuid()` usam `GenerationType.UUID`.
`UnidadeClosure` usa `@EmbeddedId`, com igualdade e hash da chave composta.

Datas sem fuso usam `LocalDateTime`, datas com fuso usam `OffsetDateTime`,
datas civis usam `LocalDate` e valores decimais usam `BigDecimal`.
Colunas PostgreSQL `text` são mapeadas diretamente como texto, sem `@Lob`.
As entidades que têm defaults para atributos comuns usam `@DynamicInsert`:
um atributo nulo no INSERT permite aplicar o default do banco. Use `refresh`
ou uma nova leitura quando precisar desses valores na instância em memória.
Não há atualização automática de timestamps em Java; vale a regra existente no banco.

Colunas sem chave estrangeira declarada continuam como identificadores simples,
incluindo referências a unidades e `InstrumentoAvaliativo.tipoEntidadeAvaliadaId`.
Os campos `tipo` e `tiposessao` de `InstrumentoSessao` permanecem distintos,
conforme a tabela existente. Valores de status e tipo continuam como `String`.

Para validar o modelo contra a base local:

```powershell
.\scripts\Validar-Modelo.ps1
```

A verificação é explícita e precisa do PostgreSQL. Ela compara o conjunto de
tabelas com as entidades, valida o schema pelo Hibernate e consulta até um registro
por entidade em uma conexão PostgreSQL somente leitura. Não imprime dados dos
registros e encerra a transação com rollback. A chave composta também é verificada.
As variáveis `DB_HOST`, `DB_PORT`, `DB_NAME`, `DB_USER` e `DB_PASSWORD` são aceitas.
O Yasson tem escopo `test` para executar essa verificação fora do servidor; no
Liberty, JSON-B continua sendo fornecido pela feature `jsonb-3.0`.

### Identidade das entidades

As 29 entidades estendem `Model<ID>` e implementam `getId()` com o tipo da chave
correspondente (`Integer`, `UUID` ou `UnidadeClosureId`). Os getters originais,
como `getIdPessoa()` e `getQuestaoId()`, continuam disponíveis.
`equals()` e `hashCode()` são centralizados em `Model` e consideram o tipo da
entidade e seu identificador. Instâncias distintas sem ID não são iguais.
Proxies Hibernate usam o identificador já disponível sem carregar registros,
inclusive fora da sessão. `UnidadeClosureId` continua como objeto de valor da
chave composta, com sua própria igualdade baseada nos dois componentes.

Como o hash depende do ID, atribua ou gere o identificador antes de usar a
entidade como chave de `HashMap` ou elemento de `HashSet`, e não altere essa chave
enquanto ela estiver na coleção. A validação de integração também verifica os
contratos de identidade e a comparação com proxies detached das 29 entidades.
