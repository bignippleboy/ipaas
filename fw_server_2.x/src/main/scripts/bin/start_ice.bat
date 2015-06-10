@echo off
rem ice server start script
setlocal enabledelayedexpansion
rem 设置运行环境配置参数
cd ..
set MAINCLASS=com.github.ipaas.ifw.server.ice.ComponentServer
set DISTPATH=.\dist
set LIBPATH=.\lib
set CONFIGPATH=.\resources
set CP=%CONFIGPATH%;
set JVMARG=-Xms512M -Xmx1024M -Xss128k 

rem 遍历lib目录jar包，组装classpath
for /f %%i in ('dir /b %LIBPATH%\*.jar^|sort') do (
	set CP=!CP!%LIBPATH%\%%i;
)
rem 遍历dist目录jar包，组装classpath
for /f %%i in ('dir /b %DISTPATH%\*.jar^|sort') do (
	set CP=!CP!%DISTPATH%\%%i;
)
echo == Server Startup Environment ==     
echo   CONFIGPATH: %CONFIGPATH%    
echo   JVMARG: %JVMARG%    
echo   CLASSPATH: %CP%                                                                                           

start "ifw_ice_server" java %JVMARG% -cp %CP% %MAINCLASS%
FOR /F "tokens=2" %%I in ('TASKLIST /NH /FI "WINDOWTITLE eq ifw_ice_server"') DO (
echo %%I > pid
)
endlocal