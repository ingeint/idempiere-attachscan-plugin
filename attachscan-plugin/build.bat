@echo off

set DEBUG_MODE=

if "%1" == "debug" (
  set DEBUG_MODE=debug
)

cd com.ingeint.attachscan.targetplatform
call .\plugin-builder.bat %DEBUG_MODE% ..\com.ingeint.attachscan ..\com.ingeint.attachscan.test
cd ..
