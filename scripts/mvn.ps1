# Encaminha argumentos ao Maven usando o JDK portátil do projeto.
$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $PSScriptRoot
$mavenCommand = Join-Path $projectRoot '.tools/apache-maven-3.9.11/bin/mvn.cmd'
$javaDirectory = Join-Path $projectRoot '.tools/jdk'
if (-not (Test-Path $mavenCommand) -or -not (Test-Path (Join-Path $javaDirectory 'bin/java.exe'))) {
    throw 'Execute primeiro .\scripts\Preparar-Ambiente.ps1.'
}
$previousJavaHome = $env:JAVA_HOME
try {
    $env:JAVA_HOME = $javaDirectory
    Push-Location $projectRoot
    try {
        & $mavenCommand @args
        $mavenExitCode = $LASTEXITCODE
    } finally {
        Pop-Location
    }
} finally {
    $env:JAVA_HOME = $previousJavaHome
}
exit $mavenExitCode
