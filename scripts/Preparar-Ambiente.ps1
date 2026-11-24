# Instala ferramentas portáteis somente em .tools, sem alterar o Windows.
$ErrorActionPreference = 'Stop'
$projectRoot = Split-Path -Parent $PSScriptRoot
$toolsDirectory = Join-Path $projectRoot '.tools'
New-Item -ItemType Directory -Path $toolsDirectory -Force | Out-Null

if (-not (Test-Path (Join-Path $toolsDirectory 'jdk/bin/java.exe'))) {
    $jdkRelease = Invoke-RestMethod 'https://api.adoptium.net/v3/assets/latest/21/hotspot?architecture=x64&image_type=jdk&os=windows&vendor=eclipse'
    $jdkPackage = $jdkRelease[0].binary.package
    $jdkArchive = Join-Path $toolsDirectory 'jdk.zip'
    Invoke-WebRequest $jdkPackage.link -OutFile $jdkArchive
    if ((Get-FileHash $jdkArchive -Algorithm SHA256).Hash -ne $jdkPackage.checksum) {
        throw 'A verificação SHA-256 do JDK falhou.'
    }
    $jdkExtraction = Join-Path $toolsDirectory 'jdk-extract'
    Expand-Archive -LiteralPath $jdkArchive -DestinationPath $jdkExtraction -Force
    $jdkFolder = Get-ChildItem $jdkExtraction -Directory | Where-Object { Test-Path (Join-Path $_.FullName 'bin/java.exe') } | Select-Object -First 1
    if (-not $jdkFolder) { throw 'JDK não encontrado no arquivo baixado.' }
    $jdkDestination = [IO.Path]::GetFullPath((Join-Path $toolsDirectory 'jdk'))
    $toolsPrefix = [IO.Path]::GetFullPath($toolsDirectory) + [IO.Path]::DirectorySeparatorChar
    if (-not $jdkFolder.FullName.StartsWith($toolsPrefix, [StringComparison]::OrdinalIgnoreCase) -or
        -not $jdkDestination.StartsWith($toolsPrefix, [StringComparison]::OrdinalIgnoreCase)) {
        throw 'Os caminhos do JDK devem permanecer dentro de .tools.'
    }
    Move-Item -LiteralPath $jdkFolder.FullName -Destination $jdkDestination
}

$mavenVersion = '3.9.11'
$mavenFolder = Join-Path $toolsDirectory "apache-maven-$mavenVersion"
if (-not (Test-Path (Join-Path $mavenFolder 'bin/mvn.cmd'))) {
    $mavenUrl = "https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/$mavenVersion/apache-maven-$mavenVersion-bin.zip"
    $mavenArchive = Join-Path $toolsDirectory 'maven.zip'
    Invoke-WebRequest $mavenUrl -OutFile $mavenArchive
    $mavenChecksum = (Invoke-RestMethod "$mavenUrl.sha512").ToString().Trim().Split(' ')[0]
    if ((Get-FileHash $mavenArchive -Algorithm SHA512).Hash -ne $mavenChecksum) {
        throw 'A verificação SHA-512 do Maven falhou.'
    }
    Expand-Archive -LiteralPath $mavenArchive -DestinationPath $toolsDirectory -Force
}

Write-Output 'JDK 21 e Maven disponíveis em .tools. Execute .\scripts\mvn.ps1 package liberty:dev.'
