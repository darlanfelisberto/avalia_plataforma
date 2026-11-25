# Simula uma view de outra sessão, sem alterar dados de negócio.
$ErrorActionPreference = 'Stop'
$url = 'http://localhost:8080/avalia/instrumento_avaliativo.xhtml?testeExpiracao=1'
$pagina = Invoke-WebRequest $url -SessionVariable sessaoOriginal -TimeoutSec 30
$campo = [regex]::Match($pagina.Content, '<input[^>]*name="jakarta.faces.ViewState"[^>]*value="([^"]+)"')
if (-not $campo.Success) { throw 'ViewState não encontrado.' }
$estado = [Net.WebUtility]::HtmlDecode($campo.Groups[1].Value)

foreach ($ajax in @($true, $false)) {
    $novaSessao = [Microsoft.PowerShell.Commands.WebRequestSession]::new()
    $body = @{
        'filtrosInstrumentoForm' = 'filtrosInstrumentoForm'
        'jakarta.faces.ViewState' = $estado
        'filtrosInstrumentoForm:pesquisar' = 'filtrosInstrumentoForm:pesquisar'
    }
    if ($ajax) {
        $body['jakarta.faces.partial.ajax'] = 'true'
        $body['jakarta.faces.source'] = 'filtrosInstrumentoForm:pesquisar'
        $body['jakarta.faces.partial.execute'] = 'filtrosInstrumentoForm'
        $resposta = Invoke-WebRequest $url -Method Post -Body $body -WebSession $novaSessao -Headers @{'Faces-Request'='partial/ajax'} -TimeoutSec 30
        [xml]$xml = $resposta.Content
        $destino = $xml.'partial-response'.redirect.url
        if (-not $destino) { throw 'Ajax expirado não retornou redirect.' }
        $uri = [uri]::new([uri]$url, [string]$destino)
        if ($uri.AbsolutePath -ne '/avalia/instrumento_avaliativo.xhtml' -or $uri.Query -ne '?testeExpiracao=1') {
            throw 'Redirect perdeu o caminho ou os parâmetros.'
        }
        $novaSessao.Headers.Remove('Faces-Request') | Out-Null
        $resposta = Invoke-WebRequest $uri.AbsoluteUri -WebSession $novaSessao -TimeoutSec 30
    } else {
        $resposta = Invoke-WebRequest $url -Method Post -Body $body -WebSession $novaSessao -TimeoutSec 30
    }
    if ($resposta.StatusCode -ne 200 -or $resposta.Content -notmatch 'A página expirou e foi recarregada') {
        [IO.File]::WriteAllText((Join-Path $PSScriptRoot '../target/view-expirada-resposta.html'), $resposta.Content)
        throw "A página não foi recuperada com aviso (Ajax=$ajax)."
    }
    Write-Output "View expirada recuperada com aviso, HTTP 200 (Ajax=$ajax)."
}
