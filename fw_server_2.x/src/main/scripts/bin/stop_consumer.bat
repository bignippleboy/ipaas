@echo off
rem consumer server stop script
setlocal
cd ..
for /F %%I in (pid) do (
echo Ready to kill pid %%I
TASKKILL /PID  %%I
)
endlocal