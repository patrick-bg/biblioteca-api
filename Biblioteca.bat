@echo off
title Iniciador - Sistema Biblioteca

:: ==========================================
:: CONFIGURE OS CAMINHOS DAS SUAS PASTAS AQUI
:: ==========================================

set "SPRING_PROFILES_ACTIVE=production"

set "JAVA_HOME=C:\Program Files (x86)\Java\zulu25.36.15-ca-jdk25.0.4-win_x64"
set "PATH=%JAVA_HOME%\bin;%PATH%"

set "PASTA_API=D:\coding\projects_java\Intellij\aplicacao-biblioteca\biblioteca-api"
set "PASTA_FRONT=D:\coding\projects_java\Intellij\aplicacao-biblioteca\biblioteca-app"

echo [1/4] Verificando se o Docker Desktop esta rodando...
docker info >nul 2>&1
if %errorlevel% neq 0 (
    echo Docker Desktop fechado. Abrindo o Docker...
    start "" "C:\Program Files\Docker\Docker\Docker Desktop.exe"
    
    :espera_docker
    timeout /t 3 /nobreak >nul
    docker info >nul 2>&1
    if %errorlevel% neq 0 goto espera_docker
    echo Docker pronto!
)

echo [2/4] Iniciando Banco de Dados (Porta 5435)...
cd /d "%PASTA_API%"
docker start biblioteca-postgres

timeout /t 4 /nobreak >nul

echo [3/4] Iniciando API (Spring Boot / Maven - Perfil Production)...
start "API - Biblioteca" cmd /k "cd /d "%PASTA_API%" && mvnw.cmd spring-boot:run"

echo [4/4] Iniciando Front-end (Angular)...
start "Front - Biblioteca" cmd /k "cd /d "%PASTA_FRONT%" && ng serve"

echo Aguardando aplicacao carregar para abrir o navegador...
timeout /t 12 /nobreak >nul
start http://localhost:4200

echo Aplicacao iniciada!
exit