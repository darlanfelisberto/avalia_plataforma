$ErrorActionPreference = 'Stop'
$raiz = Split-Path -Parent $PSScriptRoot
Push-Location $raiz
try {
    & ./scripts/mvn.ps1 -o -B test-compile dependency:build-classpath '-Dmdep.outputFile=target/cadastro-classpath.txt' '-DincludeScope=test'
    if ($LASTEXITCODE -ne 0) { throw 'Falha na compilação do cadastro.' }
    $cadastroClassPath = @((Join-Path $raiz 'target/classes'), (Join-Path $raiz 'target/test-classes'), (Get-Content target/cadastro-classpath.txt -Raw).Trim()) -join [IO.Path]::PathSeparator
    & ./.tools/jdk/bin/java.exe -cp $cadastroClassPath br.com.avalia.dao.avaliacao.ValidarCadastroInstrumento
    if ($LASTEXITCODE -ne 0) { throw 'Falha na validação do cadastro.' }
} finally { Pop-Location }
