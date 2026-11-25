# Recuperação de view expirada

OmniFaces 5.5.8 é incluído no WAR e é compatível com Faces 4.1/Java 21.
`ViewExpiredExceptionHandlerFactory`, registrado em `WEB-INF/faces-config.xml`,
trata especificamente `jakarta.faces.application.ViewExpiredException`.

O handler redireciona para a URL atual, incluindo a query string, criando uma
nova view por GET. Requisições Ajax recebem um redirect em `partial-response`;
POSTs normais recebem redirecionamento HTTP. O POST original não é repetido.
Outras exceções continuam seguindo o tratamento padrão.

O template consulta `flash['org.omnifaces.view_expired']` e informa que a página
foi recarregada. Campos ainda não enviados e estado da view anterior não são
recuperados. O timeout da sessão permanece em 30 minutos.

Isso pode acontecer por expiração da sessão, remoção de views antigas do cache
ou recarga da aplicação no modo dev. O tratamento recupera a navegação, sem
desativar o estado da view ou a proteção dos postbacks.

Execute `.\scripts\Validar-ViewExpirada.ps1` com o servidor ativo. O script usa
um ViewState de outra sessão para provocar a exceção e verifica a recuperação
Ajax e não Ajax, o aviso e a preservação de parâmetros. Não altera registros.

Referência: https://showcase.omnifaces.org/exceptionhandlers/ViewExpiredExceptionHandler
