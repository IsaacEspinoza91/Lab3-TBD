@echo off
echo Iniciando carga de datos en MongoDB...

REM Cambia esta ruta si tus archivos JSON están en otra carpeta
set "DATADIR=%~dp0"

mongoimport --db delivery-med-mongo --collection opiniones_clientes --file "%DATADIR%opiniones_clientes.json" --jsonArray
mongoimport --db delivery-med-mongo --collection logs_pedidos --file "%DATADIR%logs_pedidos.json" --jsonArray
mongoimport --db delivery-med-mongo --collection historial_repartidores --file "%DATADIR%historial_repartidores.json" --jsonArray
mongoimport --db delivery-med-mongo --collection navegacion_usuarios --file "%DATADIR%navegacion_usuarios.json" --jsonArray

echo Carga de datos finalizada.
pause
