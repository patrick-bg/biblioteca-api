@echo off
title Encerrar - Sistema Biblioteca

:: ==========================================
:: PORTAS DOS SEUS SERVICOS
:: ==========================================
set PORTA_API=8080
set PORTA_FRONT=4200

echo Encerrando API na porta %PORTA_API%...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr ":%PORTA_API%" ^| findstr "LISTENING"') do (
    taskkill /F /PID %%a >nul 2>&1
)

echo Encerrando Front-end na porta %PORTA_FRONT%...
for /f "tokens=5" %%a in ('netstat -aon ^| findstr ":%PORTA_FRONT%" ^| findstr "LISTENING"') do (
    taskkill /F /PID %%a >nul 2>&1
)

echo Parando container do Banco de Dados (Docker)...
cd /d "%PASTA_API%"
docker stop biblioteca-postgres


:: Fecha as janelas de terminal que foram abertas pelo iniciador
taskkill /FI "WINDOWTITLE eq API - Biblioteca*" /F >nul 2>&1
taskkill /FI "WINDOWTITLE eq Front - Biblioteca*" /F >nul 2>&1

echo.
echo Todos os servicos da Biblioteca foram encerrados com sucesso!
timeout /t 3 >nul
exit