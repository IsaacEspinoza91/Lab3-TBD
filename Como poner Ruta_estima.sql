=======COMO PONER EL CALCULO DE RUTA ESTIMADA==========

1. Instalación y configuración
✅ Instalar PostgreSQL
✅ Habilitar la extensión PostGIS
CREATE EXTENSION postgis;


✅ Habilitar la extensión pgRouting
CREATE EXTENSION pgrouting;



2. Cargar los datos OSM en la base
✅ Importar osm_2po_4pgr.sql (esto contiene la red vial) esta en el WSP.
Link Descarga: https://drive.google.com/file/d/1HiM09HpVrqF_bcZF4D3BC1_SyRPVzC9t/view?usp=sharing

3. Crear y poblar la tabla de vértices
✅ Crear ways_vertices_pgr con pgr_createTopology:
SELECT pgr_createTopology('osm_2po_4pgr', 0.00001, 'geom_way', 'id');


✅ Si está vacía, poblarla manualmente: (SI, SI ESTA VACIA)
INSERT INTO ways_vertices_pgr (id, the_geom)
SELECT source, ST_StartPoint(ST_LineMerge(geom_way)) FROM osm_2po_4pgr
UNION
SELECT target, ST_EndPoint(ST_LineMerge(geom_way)) FROM osm_2po_4pgr;



4. Crear funciones para calcular rutas
✅ Tabla auxiliar para arcos dinámicos:
CREATE TABLE arcos_dinamicos_aux (
    id SERIAL PRIMARY KEY,
    source INTEGER,
    target INTEGER,
    cost DOUBLE PRECISION
);


✅ Función crear_arcos_dinamicos
(genera los nodos más cercanos).
CREATE OR REPLACE FUNCTION crear_arcos_dinamicos(id_farmacia INT, id_usuario INT)
RETURNS TABLE(nodo_farmacia INT, nodo_usuario INT) AS $$
DECLARE
    f_geom geometry(Point, 4326);
    u_geom geometry(Point, 4326);
    nearest_f RECORD;
    nearest_u RECORD;
BEGIN
    -- Limpiar la tabla auxiliar
    DELETE FROM arcos_dinamicos_aux;

    -- Obtener geometría
    SELECT geom INTO f_geom FROM farmacias WHERE id = id_farmacia;
    SELECT geom INTO u_geom FROM usuarios WHERE id = id_usuario;

    -- Nodo más cercano a la farmacia
    SELECT id::INT AS id, source::INT AS nodo INTO nearest_f
    FROM osm_2po_4pgr
    ORDER BY geom_way <-> f_geom
    LIMIT 1;

    -- Nodo más cercano al usuario
    SELECT id::INT AS id, target::INT AS nodo INTO nearest_u
    FROM osm_2po_4pgr
    ORDER BY geom_way <-> u_geom
    LIMIT 1;

    -- Insertar arcos dinámicos
    INSERT INTO arcos_dinamicos_aux (source, target, cost)
    VALUES (
        nearest_f.nodo,
        nearest_f.id,
        ST_Distance(f_geom::geography, (
            SELECT geom_way FROM osm_2po_4pgr WHERE id = nearest_f.id
        )::geography) / 1000
    );

    INSERT INTO arcos_dinamicos_aux (source, target, cost)
    VALUES (
        nearest_u.id,
        nearest_u.nodo,
        ST_Distance(u_geom::geography, (
            SELECT geom_way FROM osm_2po_4pgr WHERE id = nearest_u.id
        )::geography) / 1000
    );

    -- Retornar los nodos a usar
    RETURN QUERY SELECT nearest_f.nodo, nearest_u.nodo;
END;
$$ LANGUAGE plpgsql;

✅ Función calcular_ruta
(ejecuta pgr_dijkstra para encontrar la mejor ruta).
CREATE OR REPLACE FUNCTION calcular_ruta(id_farmacia INT, id_usuario INT)
RETURNS TABLE(seq INT, node INT, edge INT, cost FLOAT, geom geometry) AS $$
DECLARE
    nodo_f INT;
    nodo_u INT;
BEGIN
    -- Generar arcos dinámicos y obtener nodos
    SELECT nodo_farmacia, nodo_usuario
    INTO nodo_f, nodo_u
    FROM crear_arcos_dinamicos(id_farmacia, id_usuario);

    -- Calcular ruta
    RETURN QUERY
	SELECT r.seq::INT, r.node::INT, r.edge::INT, r.cost::FLOAT, g.geom_way AS geom
	FROM pgr_dijkstra(
	    'SELECT id::INT, source::INT, target::INT, cost FROM osm_2po_4pgr
	     UNION
	     SELECT id, source, target, cost FROM arcos_dinamicos_aux',
	    nodo_f, nodo_u, false
	) AS r
	JOIN osm_2po_4pgr g ON r.edge = g.id;
END;
$$ LANGUAGE plpgsql;

✅ Función calcular_ruta_linestring
(ensambla la ruta como LineString).
DROP FUNCTION IF EXISTS calcular_ruta_linestring(integer, integer);
CREATE OR REPLACE FUNCTION calcular_ruta_linestring(
    id_farmacia INT,
    id_usuario INT
)
RETURNS geometry(LineString, 4326) AS
$$
DECLARE
    ruta_linestring geometry(LineString, 4326);
BEGIN
    -- Generar el LineString con los puntos en orden
    SELECT ST_MakeLine(array_agg(v.the_geom ORDER BY r.seq))
    INTO ruta_linestring
    FROM calcular_ruta(id_farmacia, id_usuario) AS r
    JOIN ways_vertices_pgr v ON r.node = v.id;

    RETURN ruta_linestring;
END;
$$ LANGUAGE plpgsql;

====PRUEBA
SELECT calcular_ruta_linestring(1,6);

SELECT ST_AsText(calcular_ruta_linestring(1,6));


Esto debería devolver la ruta en formato LINESTRING(...), igual que en tu máquina.

5. Crear= el trigger
✅ Función actualizar_ruta_estimada
(para actualizar la ruta automáticamente).
CREATE OR REPLACE FUNCTION actualizar_ruta_estimada()
RETURNS TRIGGER AS
$$
BEGIN
    -- Actualizar la columna `ruta_estimada` con la ruta generada por `calcular_ruta_linestring`
    UPDATE Pedidos
    SET ruta_estimada = calcular_ruta_linestring(NEW.Farmacia_ID, NEW.Cliente_ID)
    WHERE ID = NEW.ID;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


✅ Trigger trg_actualizar_ruta_estimada
(para ejecutar la función tras cada inserción).
CREATE TRIGGER trg_actualizar_ruta_estimada
AFTER INSERT ON Pedidos
FOR EACH ROW
EXECUTE FUNCTION actualizar_ruta_estimada();


6. Prueba final
✅ Insertar un pedido y verificar que la ruta se almacene correctamente.
INSERT INTO Pedidos (Fecha, Urgencia, Total_pagado, Estado_entrega, Fecha_entrega, Cliente_ID, Medio_pago_ID, Farmacia_ID, Repartidor_ID)
VALUES (CURRENT_DATE, TRUE, 10000, 'Pendiente', CURRENT_DATE + INTERVAL '3 days', 6, 1, 1, 2)
RETURNING ruta_estimada;

tambien se puede usar
INSERT INTO Pedidos (
    Fecha,
    Urgencia,
    Total_pagado,
    Estado_entrega,
    Fecha_entrega,
    Cliente_ID,
    Medio_pago_ID,
    Farmacia_ID,
    Repartidor_ID
) VALUES (
    CURRENT_DATE,
    TRUE,
    15000,
    'PENDIENTE',
    NULL,
    5,    -- Cliente_ID (Usuario destino)
    1,    -- Medio_pago_ID
    2,    -- Farmacia_ID (Origen)
    15     -- Repartidor_ID
);

NOTA: Verificar que farmacia y usuario tengan poblado el atributo de ubicacion para los ID que se prueba

✅ Si devuelve un LINESTRING en la tabla de pedidos el nuevo inserto, todo funciona correctamente.






================ version copiar y pegar =================

CREATE EXTENSION pgrouting;

SELECT pgr_createTopology('osm_2po_4pgr', 0.00001, 'geom_way', 'id');

INSERT INTO ways_vertices_pgr (id, the_geom)
SELECT source, ST_StartPoint(ST_LineMerge(geom_way)) FROM osm_2po_4pgr
UNION
SELECT target, ST_EndPoint(ST_LineMerge(geom_way)) FROM osm_2po_4pgr;

CREATE TABLE arcos_dinamicos_aux (
    id SERIAL PRIMARY KEY,
    source INTEGER,
    target INTEGER,
    cost DOUBLE PRECISION
);

CREATE OR REPLACE FUNCTION crear_arcos_dinamicos(id_farmacia INT, id_usuario INT)
RETURNS TABLE(nodo_farmacia INT, nodo_usuario INT) AS $$
DECLARE
    f_geom geometry(Point, 4326);
    u_geom geometry(Point, 4326);
    nearest_f RECORD;
    nearest_u RECORD;
BEGIN
    -- Limpiar la tabla auxiliar
    DELETE FROM arcos_dinamicos_aux;

    -- Obtener geometría
    SELECT geom INTO f_geom FROM farmacias WHERE id = id_farmacia;
    SELECT geom INTO u_geom FROM usuarios WHERE id = id_usuario;

    -- Nodo más cercano a la farmacia
    SELECT id::INT AS id, source::INT AS nodo INTO nearest_f
    FROM osm_2po_4pgr
    ORDER BY geom_way <-> f_geom
    LIMIT 1;

    -- Nodo más cercano al usuario
    SELECT id::INT AS id, target::INT AS nodo INTO nearest_u
    FROM osm_2po_4pgr
    ORDER BY geom_way <-> u_geom
    LIMIT 1;

    -- Insertar arcos dinámicos
    INSERT INTO arcos_dinamicos_aux (source, target, cost)
    VALUES (
        nearest_f.nodo,
        nearest_f.id,
        ST_Distance(f_geom::geography, (
            SELECT geom_way FROM osm_2po_4pgr WHERE id = nearest_f.id
        )::geography) / 1000
    );

    INSERT INTO arcos_dinamicos_aux (source, target, cost)
    VALUES (
        nearest_u.id,
        nearest_u.nodo,
        ST_Distance(u_geom::geography, (
            SELECT geom_way FROM osm_2po_4pgr WHERE id = nearest_u.id
        )::geography) / 1000
    );

    -- Retornar los nodos a usar
    RETURN QUERY SELECT nearest_f.nodo, nearest_u.nodo;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION calcular_ruta(id_farmacia INT, id_usuario INT)
RETURNS TABLE(seq INT, node INT, edge INT, cost FLOAT, geom geometry) AS $$
DECLARE
    nodo_f INT;
    nodo_u INT;
BEGIN
    -- Generar arcos dinámicos y obtener nodos
    SELECT nodo_farmacia, nodo_usuario
    INTO nodo_f, nodo_u
    FROM crear_arcos_dinamicos(id_farmacia, id_usuario);

    -- Calcular ruta
    RETURN QUERY
	SELECT r.seq::INT, r.node::INT, r.edge::INT, r.cost::FLOAT, g.geom_way AS geom
	FROM pgr_dijkstra(
	    'SELECT id::INT, source::INT, target::INT, cost FROM osm_2po_4pgr
	     UNION
	     SELECT id, source, target, cost FROM arcos_dinamicos_aux',
	    nodo_f, nodo_u, false
	) AS r
	JOIN osm_2po_4pgr g ON r.edge = g.id;
END;
$$ LANGUAGE plpgsql;

DROP FUNCTION IF EXISTS calcular_ruta_linestring(integer, integer);
CREATE OR REPLACE FUNCTION calcular_ruta_linestring(
    id_farmacia INT,
    id_usuario INT
)
RETURNS geometry(LineString, 4326) AS
$$
DECLARE
    ruta_linestring geometry(LineString, 4326);
BEGIN
    -- Generar el LineString con los puntos en orden
    SELECT ST_MakeLine(array_agg(v.the_geom ORDER BY r.seq))
    INTO ruta_linestring
    FROM calcular_ruta(id_farmacia, id_usuario) AS r
    JOIN ways_vertices_pgr v ON r.node = v.id;

    RETURN ruta_linestring;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION actualizar_ruta_estimada()
RETURNS TRIGGER AS
$$
BEGIN
    -- Actualizar la columna `ruta_estimada` con la ruta generada por `calcular_ruta_linestring`
    UPDATE Pedidos
    SET ruta_estimada = calcular_ruta_linestring(NEW.Farmacia_ID, NEW.Cliente_ID)
    WHERE ID = NEW.ID;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_actualizar_ruta_estimada
AFTER INSERT ON Pedidos
FOR EACH ROW
EXECUTE FUNCTION actualizar_ruta_estimada();



