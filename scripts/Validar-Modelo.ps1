# Valida o schema e lê até um registro por entidade, sem imprimir dados de negócio.
$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $PSScriptRoot
Push-Location $projectRoot
try {
    & (Join-Path $PSScriptRoot 'mvn.ps1') -B test-compile dependency:build-classpath '-Dmdep.outputFile=target/modelo-classpath.txt' '-DincludeScope=test'
    if ($LASTEXITCODE -ne 0) { throw 'Falha ao compilar o modelo ou preparar o classpath.' }
    $classPath = @(
        (Join-Path $projectRoot 'target/classes'),
        (Join-Path $projectRoot 'target/test-classes'),
        (Get-Content (Join-Path $projectRoot 'target/modelo-classpath.txt') -Raw).Trim()
    ) -join [IO.Path]::PathSeparator
    & (Join-Path $projectRoot '.tools/jdk/bin/java.exe') -cp $classPath br.com.avalia.modelo.ValidarModelo
    if ($LASTEXITCODE -ne 0) { throw 'A validação de integração do modelo falhou.' }
} finally {
    Pop-Location
}
