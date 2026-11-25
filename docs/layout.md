# Template de interface

O layout usa Faces 4.1 e Bootstrap 5.3.9, com arquivos locais e tema próprio.
O menu é fixo à esquerda a partir de 992 px. Abaixo disso, o mesmo menu usa
o offcanvas do Bootstrap, com fechamento por botão, fundo e tecla Escape.
O cabeçalho permanece visível durante a rolagem.

## Criar uma página

```xml
<ui:composition template="/WEB-INF/templates/layout.xhtml"
                xmlns:ui="jakarta.faces.facelets">
    <ui:param name="menuAtivo" value="avaliacoes"/>

    <ui:define name="acoesPagina">
        <!-- Ações específicas da página. -->
    </ui:define>

    <ui:define name="conteudo">
        <!-- Conteúdo da página; cada formulário deve ter seu próprio h:form. -->
    </ui:define>
</ui:composition>
```

`menuAtivo` aceita `inicio`, `avaliacoes`, `questoes`, `comunidade` e `relatorios`.
Novas entradas podem ser adicionadas no include `menu-lateral.xhtml` ou pelo
bloco `menuAdicional`. Use links Faces com `outcome` para navegação por GET.

## Nome e perfil do usuário

O template aceita os parâmetros `nomeUsuario` e `perfilUsuario`.
Enquanto a autenticação não estiver implementada, mostra `Visitante`.
Após criar o bean de sessão, os parâmetros podem receber suas propriedades
por EL na composição ou ser ligados centralmente em `barra-superior.xhtml`.
O cabeçalho não implementa autenticação ou autorização.

Blocos disponíveis para personalização:

| Bloco | Uso |
| --- | --- |
| `conteudo` | Conteúdo principal |
| `acoesPagina` | Ações no início do conteúdo, alinhadas à direita |
| `cabecalhoPagina` | Substituição completa do cabeçalho da página |
| `usuario` | Área completa de identificação do usuário |
| `avatarUsuario` | Avatar ou iniciais |
| `acoesTopo` | Ações adicionais na barra superior |
| `menuAdicional` | Entradas extras no menu |
| `head` | Metadados específicos da página |
| `styles` | Estilos da página, declarados no body e relocados ao head pelo Faces |
| `scripts` | Scripts específicos da página |

Textos vindos do usuário são escapados pela renderização padrão do Faces.
Nomes longos usam truncamento visual para preservar a largura do cabeçalho.

## Organização

- `WEB-INF/templates/layout.xhtml`: estrutura compartilhada e recursos.
- `WEB-INF/includes/menu-lateral.xhtml`: menu e item ativo.
- `WEB-INF/tags/barra-superior.xhtml`: identificação e navegação móvel.
- `WEB-INF/tags/icone.xhtml`: tag reutilizável `t:icone` com sprite SVG local.
- `WEB-INF/tags/estado-modulo.xhtml`: apresentação dos módulos em preparação.
- `WEB-INF/resources/styles/layout.css`: tema, componentes e breakpoints.
- `WEB-INF/resources/scripts/layout.js`: estado acessível do botão do menu.
- `WEB-INF/resources/icons/interface.svg`: ícones vetoriais do projeto.
- `WEB-INF/resources/styles/vendor` e `scripts/vendor`: distribuição Bootstrap.

Os recursos são publicados pelo ResourceHandler do Faces. Templates, includes
e tag files ficam protegidos de acesso HTTP direto em `WEB-INF`.
O CSS usa variáveis `--av-*` para cores e medidas. Os estilos personalizados
ficam em arquivo separado. A licença MIT do Bootstrap acompanha a distribuição.

A página inicial demonstra o layout. O menu Avaliações abre a listagem real em
`instrumento_avaliativo.xhtml`. O endereço antigo `avaliacoes.xhtml` redireciona
para essa página. As páginas de questões, comunidade e relatórios são bases
visuais para os módulos futuros, com indicação de que estão em preparação.
