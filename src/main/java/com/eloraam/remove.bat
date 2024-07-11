@echo off
setlocal enabledelayedexpansion

REM Function to remove first empty line from .java files recursively
:RemoveEmptyFirstLine
for /r %%f in (*.java) do (
    REM Get the first line of the file
    set "first_line="
    set /p "first_line="<"%%f"
    
    REM Check if the first line is empty
    if "!first_line!" == "" (
        REM Remove the first line and save to a temporary file
        more +1 "%%f" > "%%f.tmp"
        
        REM Replace the original file with the modified one
        move /y "%%f.tmp" "%%f" > nul
        echo Removed empty first line from: %%f
    )
)
exit /b

REM Start removing empty first lines from .java files in the current directory
call :RemoveEmptyFirstLine
