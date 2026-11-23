@echo off
setlocal
cd /d "%~dp0"
where mvn >nul 2>nul
if not errorlevel 1 (
  call mvn %*
  exit /b %errorlevel%
)
set "MAVEN_VERSION=3.9.11"
set "MAVEN_HOME=%~dp0.mvn\apache-maven-%MAVEN_VERSION%"
if not exist "%MAVEN_HOME%\bin\mvn.cmd" (
  echo Maven nao encontrado. Baixando Maven %MAVEN_VERSION% para esta pasta...
  powershell -NoProfile -ExecutionPolicy Bypass -Command "$ErrorActionPreference='Stop'; $dir='%~dp0.mvn'; New-Item -ItemType Directory -Path $dir -Force | Out-Null; $zip=Join-Path $dir 'maven-download.zip'; Invoke-WebRequest -Uri 'https://repo.maven.apache.org/maven2/org/apache/maven/apache-maven/%MAVEN_VERSION%/apache-maven-%MAVEN_VERSION%-bin.zip' -OutFile $zip; Expand-Archive -Path $zip -DestinationPath $dir -Force; Remove-Item $zip"
  if errorlevel 1 (
    echo Nao foi possivel baixar o Maven. Verifique sua conexao.
    exit /b 1
  )
)
call "%MAVEN_HOME%\bin\mvn.cmd" %*
exit /b %errorlevel%
