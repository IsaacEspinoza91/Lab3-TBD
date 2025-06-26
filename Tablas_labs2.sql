-- ========================
-- 🔹       EXTENSIONES
-- ========================
CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS pgrouting;

-- =================================
-- 🔹       CREAR TABLAS
-- =================================

-- Crear tabla Usuarios (base común)
CREATE TABLE Usuarios (
    ID SERIAL PRIMARY KEY,
    RUT VARCHAR(20) UNIQUE,
    Nombre VARCHAR(50) NOT NULL,
    Apellido VARCHAR(50) NOT NULL,
    Email VARCHAR(100) UNIQUE NOT NULL,
    Password VARCHAR(100) NOT NULL,
    Telefono VARCHAR(20),
    Tipo VARCHAR(20) NOT NULL CHECK (Tipo IN ('CLIENTE', 'REPARTIDOR', 'ADMIN')),
    geom geometry(Point, 4326) -- NUEVO: ubicación geoespacial del repartidor
);

-- Crear tabla Clientes (hereda de Usuarios)
CREATE TABLE Clientes (
    Usuario_ID INT PRIMARY KEY REFERENCES Usuarios(ID) ON DELETE CASCADE,
    Direccion VARCHAR(100) NOT NULL
);

-- Crear tabla Repartidores (hereda de Usuarios con nuevos atributos)
CREATE TABLE Repartidores (
    Usuario_ID INT PRIMARY KEY REFERENCES Usuarios(ID) ON DELETE CASCADE,
    Tipo_vehiculo VARCHAR(20) NOT NULL CHECK (Tipo_vehiculo IN ('AUTO', 'MOTO', 'BICICLETA', 'CAMIONETA'))
);

-- Crear tabla Administradores
CREATE TABLE Administradores (
    Usuario_ID INT PRIMARY KEY REFERENCES Usuarios(ID) ON DELETE CASCADE,
    Nivel_acceso INT DEFAULT 1,
    Departamento VARCHAR(50)
);

-- Crear tabla Medios_de_pago
CREATE TABLE Medios_de_pago (
    ID SERIAL PRIMARY KEY,
    Tipo VARCHAR(50)
);

-- Crear tabla Farmacias
CREATE TABLE Farmacias (
    ID SERIAL PRIMARY KEY,
    Nombre VARCHAR(100),
    Lugar VARCHAR(100),
    geom geometry(Point, 4326) -- NUEVO: ubicación geoespacial de la farmacia
);

-- Crear tabla De puntos de entrega de farmacias
CREATE TABLE Punto_de_entrega (
    ID SERIAL PRIMARY KEY,
    Nombre VARCHAR(100),
    Farmacia_ID INT REFERENCES Farmacias(ID),
    geom geometry(Point, 4326) -- NUEVO: ubicación geoespacial del punto de entrega
);

-- Crear tabla Pedidos
CREATE TABLE Pedidos (
    ID SERIAL PRIMARY KEY,
    Fecha DATE,
    Urgencia BOOLEAN,
    Total_pagado INT,
    Estado_entrega VARCHAR(50),
    Fecha_entrega DATE,
    Cliente_ID INT REFERENCES Usuarios(ID),
    Medio_pago_ID INT REFERENCES Medios_de_pago(ID),
    Farmacia_ID INT REFERENCES Farmacias(ID),
    Repartidor_ID INT REFERENCES Usuarios(ID),
    ruta_estimada geometry(LineString, 4326), -- NUEVO: ruta estimada del pedido
    ruta_estimada_mls geometry(MultiLineString, 4326)
);

-- Crear tabla Productos
CREATE TABLE Productos (
    ID SERIAL PRIMARY KEY,
    Nombre VARCHAR(100),
    Precio INT,
    Stock INT,
    Requiere_receta BOOLEAN
);

-- Crear tabla Detalle_de_pedidos
CREATE TABLE Detalle_de_pedidos (
    ID SERIAL PRIMARY KEY,
    Pedido_ID INT REFERENCES Pedidos(ID),
    Producto_ID INT REFERENCES Productos(ID),
    Cantidad INT
);

-- Crear tabla Calificaciones
CREATE TABLE Calificaciones (
    ID SERIAL PRIMARY KEY,
    Puntuacion VARCHAR(50),
    Estrellas INT,
    Cliente_ID INT REFERENCES Usuarios(ID),
    Repartidor_ID INT REFERENCES Usuarios(ID)
);

-- Crear tabla Pedidos_Repartidores
CREATE TABLE Pedidos_Repartidores (
    Pedido_ID INT REFERENCES Pedidos(ID),
    Repartidor_ID INT REFERENCES Usuarios(ID),
    PRIMARY KEY (Pedido_ID, Repartidor_ID)
);

-- Crear tabla Farmacias_Productos
CREATE TABLE Farmacias_Productos (
    Farmacia_ID INT REFERENCES Farmacias(ID),
    Producto_ID INT REFERENCES Productos(ID),
    PRIMARY KEY (Farmacia_ID, Producto_ID)
);

-- Crear tabla Notificaciones
CREATE TABLE Notificaciones (
    id SERIAL PRIMARY KEY,
    mensaje TEXT,
    pedido_id INT,
    fecha DATE
);

-- NUEVO: Tabla Zonas de Cobertura
CREATE TABLE Zonas_cobertura (
    ID SERIAL PRIMARY KEY,
    nombre VARCHAR(100),
    geom geometry(Polygon, 4326) -- Polígono que representa la zona de cobertura
);

-- Crear Tabla de puntos de interes
CREATE TABLE puntos_de_interes (
    id SERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    lugar VARCHAR(100),
    geom GEOMETRY(Point, 4326) -- O ajusta el tipo de geometría y SRID según tu necesidad
);


-- ===================================================
-- 🔹       PROCEDIMIENTOS ALMACENADOS
-- ===================================================

-- 7. Registrar un pedido completo. [Omar]
CREATE OR REPLACE PROCEDURE registrar_pedido(
  p_fecha DATE,
  p_urgencia BOOLEAN,
  p_total INT,
  p_estado TEXT,
  p_fecha_entrega DATE,
  p_medio_pago_id INT,
  p_farmacia_id INT,
  p_repartidor_id INT,
  p_cliente_id INT,
  p_detalles JSON
)
LANGUAGE plpgsql
AS $$
DECLARE
  item JSON;
  pedido_id INT;
BEGIN
  INSERT INTO "pedidos" (
    "fecha", "urgencia", "total_pagado", "estado_entrega", "fecha_entrega", "cliente_id",
    "medio_pago_id", "farmacia_id", "repartidor_id"
  )
  VALUES (
    p_fecha, p_urgencia, p_total, p_estado, p_fecha_entrega,
    p_medio_pago_id, p_farmacia_id, p_repartidor_id, p_cliente_id
  )
  RETURNING "id" INTO pedido_id;

  FOR item IN SELECT * FROM json_array_elements(p_detalles)
  LOOP
    INSERT INTO "detalle_de_pedidos" ("id", "pedido_id", "producto_id", "cantidad")
    VALUES (
      (SELECT COALESCE(MAX("id"), 0) + 1 FROM "detalle_de_pedidos"),
      pedido_id,
      (item->>'producto_id')::INT,
      (item->>'cantidad')::INT
    );
  END LOOP;
END;
$$;


-- 8. Cambiar el estado de un pedido con validación. [Isaac]
CREATE OR REPLACE PROCEDURE cambiar_estado_pedido(
    id_pedido INT,
    nuevo_estado VARCHAR(50)
)
LANGUAGE plpgsql
AS $$
BEGIN
    -- Verificar si pedido existe
    IF NOT EXISTS (SELECT 1 FROM pedidos WHERE id = id_pedido) THEN
        RAISE EXCEPTION 'Error: Pedido no existe';
    END IF;
    
    -- Validar el nuevo estado
    IF nuevo_estado NOT IN ('Entregado', 'Fallido', 'Pendiente') THEN
        RAISE EXCEPTION 'Error: Estado invalido';
    END IF;
    
    -- Actualizar el estado del pedido
    UPDATE pedidos 
    SET estado_entrega = nuevo_estado
    WHERE id = id_pedido;
END;
$$;
-- Ejemplo llamada, muestra tabla con resultadod de ejecuion
-- CALL cambiar_estado_pedido(2,'Fallido');


-- 9. Descontar stock al confirmar pedido. [Williams]
CREATE OR REPLACE PROCEDURE public.descontar_stock_confirmado(IN p_pedido_id integer)
    LANGUAGE 'plpgsql'
    
AS $BODY$
DECLARE
  v_producto_id INT;
  v_cantidad INT;
  v_stock_actual INT;
BEGIN
  -- Actualizamos el estado del pedido a 'Entregado'
  UPDATE pedidos
  SET estado_entrega = 'Entregado'
  WHERE id = p_pedido_id;

  -- Recorremos cada producto en el detalle del pedido
  FOR v_producto_id, v_cantidad IN
    SELECT producto_id, cantidad
    FROM detalle_de_pedidos
    WHERE pedido_id = p_pedido_id
  LOOP
    -- Obtener el stock actual del producto
    SELECT stock INTO v_stock_actual
    FROM productos
    WHERE id = v_producto_id;

    -- Verificar si el stock es suficiente
    IF v_stock_actual < v_cantidad THEN
      RAISE EXCEPTION 'Stock insuficiente para el producto %', v_producto_id;
    END IF;

    -- Descontar el stock
    UPDATE productos
    SET stock = stock - v_cantidad
    WHERE id = v_producto_id;
  END LOOP;
  
END;
$BODY$;

-- ========================
-- 🔹       TIGGERS
-- ========================

-- 10. Insertar automáticamente la fecha de entrega al marcar como entregado.

-- Función que utiliza el trigger
CREATE OR REPLACE FUNCTION actualizar_fecha_entrega()
RETURNS TRIGGER AS $$
BEGIN
    -- Verifica si el estado cambia a 'entregado'
    IF NEW.estado_entrega = 'entregado' THEN
        NEW.fecha_entrega := NOW();  -- Asigna la fecha actual
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger
CREATE TRIGGER trigger_actualizar_fecha_entrega
BEFORE UPDATE ON pedidos
FOR EACH ROW
WHEN (OLD.estado_entrega IS DISTINCT FROM NEW.estado_entrega)
EXECUTE FUNCTION actualizar_fecha_entrega();


-- 11. Registrar una notificación si un medicamento con receta es pedido sin validación. [Emir]
CREATE OR REPLACE FUNCTION registrar_notificacion_medicamento_sin_validacion()
RETURNS trigger AS
$$
BEGIN
    IF EXISTS (
        SELECT 1
        FROM Productos p
        JOIN Pedidos pe ON pe.id = NEW.pedido_id
        WHERE p.id = NEW.producto_id
        AND p.requiere_receta = TRUE
        AND pe.estado_entrega != 'Entregado'
    ) THEN
        INSERT INTO Notificaciones (mensaje, pedido_id, fecha)
        VALUES ('El medicamento con receta fue pedido sin validación.', NEW.pedido_id, CURRENT_DATE);
    END IF;
    RETURN NEW;
END;
$$
LANGUAGE plpgsql;

CREATE TRIGGER trigger_registrar_notificacion_medicamento_sin_validacion
AFTER INSERT ON Detalle_de_pedidos
FOR EACH ROW
EXECUTE FUNCTION registrar_notificacion_medicamento_sin_validacion();


-- 12. Insertar una calificación automática si no se recibe en 48 horas. [Williams]

-- Función para que el trigger utilice

CREATE OR REPLACE FUNCTION revisar_pedidos_tardados()
RETURNS TRIGGER AS $$
BEGIN
    -- Verificar si el pedido está pendiente y lleva más de 48 horas
    IF NEW.estado_entrega = 'Pendiente' AND NEW.fecha_entrega < NOW() - INTERVAL '48 hours' THEN
        -- Marcar como 'Tardado'
        UPDATE pedidos
        SET estado_entrega = 'Tardado'
        WHERE id = NEW.id;

        -- Insertar calificación automática
        INSERT INTO calificaciones (puntuacion, estrellas, cliente_id, repartidor_id)
        VALUES ('Fallo por demora en evaluación', 1, NEW.cliente_id, NEW.repartidor_id);
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Trigger

CREATE TRIGGER trigger_revisar_pedidos_tardados
AFTER INSERT OR UPDATE ON pedidos
FOR EACH ROW
EXECUTE FUNCTION revisar_pedidos_tardados();

-- ========================
-- 🔹       VISTAS
-- ========================

-- 13. Resumen de pedidos por cliente (monto total, número de pedidos).
CREATE OR REPLACE VIEW resumen_pedidos_por_cliente AS
SELECT
    u.id AS cliente_id,
    u.nombre,
    u.apellido,
    u.email,
    COUNT(p.id) AS cantidad_pedidos,
    COALESCE(SUM(p.total_pagado), 0) AS monto_total
FROM
    usuarios u
LEFT JOIN pedidos p ON u.id = p.cliente_id
WHERE
    u.tipo = 'CLIENTE'
GROUP BY
    u.id, u.nombre, u.apellido, u.email;

-- 15. Vista de farmacias con mayor volumen de productos entregados.[Isaac]
CREATE OR REPLACE VIEW farmacias_volumen_entregas_exitosas AS
SELECT 
    f.id, 
	f.Nombre AS farmacia, 
	f.Lugar,
    COALESCE(SUM(dp.cantidad), 0) AS total_productos_pedidos, -- pedidos de todos los estados
    COALESCE(SUM(dp.cantidad) FILTER (WHERE p.estado_entrega = 'Entregado'), 0) AS total_productos_entregados
FROM farmacias AS f LEFT JOIN pedidos AS p
	ON f.id = p.farmacia_id
	LEFT JOIN detalle_de_pedidos AS dp
	ON p.id = dp.pedido_id
GROUP BY f.id, f.nombre, f.lugar
ORDER BY total_productos_entregados DESC;

-- 14. Vista de desempeño por repartidor.

CREATE VIEW vista_desempeno_repartidor AS
SELECT 
    u.nombre AS nombre,
    u.apellido AS apellido,
    r.tipo_vehiculo AS vehiculo,
    COUNT(p.id) AS total_pedidos,
    AVG(c.estrellas) AS promedio_estrellas,
    COALESCE(fve.total_productos_entregados, 0) AS total_productos_entregados,
    COALESCE(fve.total_productos_pedidos, 0) AS total_productos_pedidos,
    CASE 
        WHEN fve.total_productos_pedidos > 0 
        THEN ROUND((fve.total_productos_entregados::decimal / fve.total_productos_pedidos) * 100, 2)
        ELSE 0 
    END AS porcentaje_entregas_exitosas
FROM repartidores r
JOIN usuarios u ON r.usuario_id = u.id
LEFT JOIN pedidos p ON p.repartidor_id = r.usuario_id
LEFT JOIN calificaciones c ON c.repartidor_id = r.usuario_id
LEFT JOIN farmacias_volumen_entregas_exitosas fve ON fve.farmacia = (
    SELECT f.nombre 
    FROM farmacias f 
    JOIN pedidos p2 ON p2.farmacia_id = f.id 
    WHERE p2.repartidor_id = r.usuario_id 
    LIMIT 1
)
GROUP BY u.nombre, u.apellido, r.tipo_vehiculo, fve.total_productos_entregados, fve.total_productos_pedidos;

---- cosas para Implementar una función que calcule automáticamente la zona a la que pertenece un cliente. ----

CREATE TABLE Zonas_clientes (
    id SERIAL PRIMARY KEY,
    cliente_id INT REFERENCES Usuarios(ID),
    nombre_cliente VARCHAR(100),
    nombre_zona VARCHAR(100),
    ubicacion_cliente TEXT, -- GeoJSON
    poligono_zona TEXT      -- GeoJSON
);


CREATE OR REPLACE FUNCTION asignar_zona_cliente()
RETURNS TRIGGER AS $$
BEGIN
    IF NEW.Tipo = 'CLIENTE' THEN
        INSERT INTO Zonas_clientes (cliente_id, nombre_cliente, nombre_zona, ubicacion_cliente, poligono_zona)
        SELECT 
            NEW.ID,
            NEW.Nombre,
            z.nombre,
            ST_AsGeoJSON(NEW.geom),
            ST_AsGeoJSON(z.geom)
        FROM Zonas_cobertura z
        WHERE ST_Within(NEW.geom, z.geom)
        LIMIT 1;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;


CREATE TRIGGER trg_asignar_zona_cliente
AFTER INSERT ON Usuarios
FOR EACH ROW
EXECUTE FUNCTION asignar_zona_cliente();

------APARTADO PARA RUTA ESTIMADA, SE DEBE TENER EL POBLADO DE CHILE-------

-- ========================
-- 🔹 3. CREAR Y POBLAR VÉRTICES
-- ========================
SELECT pgr_createTopology('osm_2po_4pgr', 0.00001, 'geom_way', 'id');

INSERT INTO osm_2po_4pgr_vertices_pgr (id, the_geom)
SELECT source, ST_StartPoint(ST_LineMerge(geom_way)) FROM osm_2po_4pgr
UNION
SELECT target, ST_EndPoint(ST_LineMerge(geom_way)) FROM osm_2po_4pgr;

-- ========================
-- 🔹 4. TABLA AUXILIAR PARA ARCOS DINÁMICOS
-- ========================
CREATE TABLE arcos_dinamicos_aux (
    id SERIAL PRIMARY KEY,
    source INTEGER,
    target INTEGER,
    cost DOUBLE PRECISION
);

-- ========================
-- 🔹 5. FUNCIÓN: CREAR ARCOS DINÁMICOS
-- ========================
CREATE OR REPLACE FUNCTION crear_arcos_dinamicos(id_farmacia INT, id_usuario INT)
RETURNS TABLE(nodo_farmacia INT, nodo_usuario INT) AS $$
DECLARE
    f_geom geometry(Point, 4326);
    u_geom geometry(Point, 4326);
    nearest_f RECORD;
    nearest_u RECORD;
BEGIN
    DELETE FROM arcos_dinamicos_aux;

    SELECT geom INTO f_geom FROM farmacias WHERE id = id_farmacia;
    SELECT geom INTO u_geom FROM usuarios WHERE id = id_usuario;

    SELECT id::INT AS id, source::INT AS nodo INTO nearest_f FROM osm_2po_4pgr ORDER BY geom_way <-> f_geom LIMIT 1;
    SELECT id::INT AS id, target::INT AS nodo INTO nearest_u FROM osm_2po_4pgr ORDER BY geom_way <-> u_geom LIMIT 1;

    INSERT INTO arcos_dinamicos_aux (source, target, cost) VALUES (nearest_f.nodo, nearest_f.id, ST_Distance(f_geom::geography, (SELECT geom_way FROM osm_2po_4pgr WHERE id = nearest_f.id)::geography) / 1000);
    INSERT INTO arcos_dinamicos_aux (source, target, cost) VALUES (nearest_u.id, nearest_u.nodo, ST_Distance(u_geom::geography, (SELECT geom_way FROM osm_2po_4pgr WHERE id = nearest_u.id)::geography) / 1000);

    RETURN QUERY SELECT nearest_f.nodo, nearest_u.nodo;
END;
$$ LANGUAGE plpgsql;

-- ========================
-- 🔹 6. FUNCIÓN: CALCULAR RUTA CON DIJKSTRA
-- ========================
CREATE OR REPLACE FUNCTION calcular_ruta(id_farmacia INT, id_usuario INT)
RETURNS TABLE(seq INT, node INT, edge INT, cost FLOAT, geom geometry) AS $$
DECLARE
    nodo_f INT;
    nodo_u INT;
BEGIN
    SELECT nodo_farmacia, nodo_usuario INTO nodo_f, nodo_u FROM crear_arcos_dinamicos(id_farmacia, id_usuario);

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

-- ========================
-- 🔹 7. FUNCIÓN: CALCULAR RUTA EN LINESTRING
-- ========================
CREATE OR REPLACE FUNCTION calcular_ruta_linestring(id_farmacia INT, id_usuario INT)
RETURNS geometry(LineString, 4326) AS $$
DECLARE
    ruta_linestring geometry(LineString, 4326);
BEGIN
    SELECT ST_MakeLine(array_agg(v.the_geom ORDER BY r.seq))
    INTO ruta_linestring
    FROM calcular_ruta(id_farmacia, id_usuario) AS r
    JOIN osm_2po_4pgr_vertices_pgr v ON r.node = v.id;

    RETURN ruta_linestring;
END;
$$ LANGUAGE plpgsql;

-- ========================
-- 🔹 8. FUNCIÓN: ACTUALIZAR RUTA ESTIMADA
-- ========================
CREATE OR REPLACE FUNCTION actualizar_ruta_estimada()
RETURNS TRIGGER AS $$
BEGIN
    UPDATE Pedidos
    SET ruta_estimada = calcular_ruta_linestring(NEW.Farmacia_ID, NEW.Cliente_ID)
    WHERE ID = NEW.ID;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- ========================
-- 🔹 9. CREAR TRIGGER PARA ACTUALIZACIÓN AUTOMÁTICA
-- ========================
CREATE TRIGGER trg_actualizar_ruta_estimada
AFTER INSERT ON Pedidos
FOR EACH ROW
EXECUTE FUNCTION actualizar_ruta_estimada();











-- Nuevo metodo calcular ruta estimada
CREATE OR REPLACE FUNCTION calcular_ruta_estimada_trigger()
RETURNS trigger AS $$
DECLARE
    origen geometry(Point, 4326);
    destino geometry(Point, 4326);
    id_origen INT;
    id_destino INT;
    geom_line geometry(MultiLineString, 4326);
BEGIN
    -- Obtener geometría de la farmacia (origen)
    SELECT geom INTO origen
    FROM Farmacias
    WHERE id = NEW.farmacia_id;

    -- Obtener geometría del cliente (destino)
    SELECT geom INTO destino
    FROM Usuarios
    WHERE id = NEW.cliente_id;

    -- Validar que ambos puntos existen
    IF origen IS NULL OR destino IS NULL THEN
        RAISE EXCEPTION 'No se pudo obtener el origen o destino geográfico para el pedido %', NEW.id;
    END IF;

    -- Buscar nodo más cercano al origen
	SELECT
	    CASE
	        WHEN ST_Distance(ST_PointN(geom_way, 1), origen) <
	             ST_Distance(ST_PointN(geom_way, ST_NPoints(geom_way)), origen)
	        THEN source
	        ELSE target
	    END
	INTO id_origen
	FROM osm_2po_4pgr
	ORDER BY geom_way <-> origen
	LIMIT 1;


    -- Buscar nodo más cercano al destino
	SELECT
	    CASE
	        WHEN ST_Distance(ST_PointN(geom_way, 1), destino) <
	             ST_Distance(ST_PointN(geom_way, ST_NPoints(geom_way)), destino)
	        THEN source
	        ELSE target
	    END
	INTO id_destino
	FROM osm_2po_4pgr
	ORDER BY geom_way <-> destino
	LIMIT 1;


    -- Calcular ruta y unir geometrías
    WITH ruta AS (
        SELECT * FROM pgr_dijkstra(
            'SELECT id, source, target, cost, reverse_cost FROM osm_2po_4pgr',
            id_origen, id_destino,
            directed := true
        )
    ),
    segmentos AS (
        SELECT w.geom_way
        FROM ruta r
        JOIN osm_2po_4pgr w ON r.edge = w.id
        WHERE r.edge != -1
        ORDER BY r.seq
    )
    SELECT ST_Union(geom_way)::geometry(MultiLineString, 4326) INTO geom_line
    FROM segmentos;

    -- Actualizar la fila con la ruta
    UPDATE Pedidos
    SET ruta_estimada_mls = geom_line
    WHERE id = NEW.id;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;



CREATE TRIGGER trigger_calcular_ruta
AFTER INSERT ON Pedidos
FOR EACH ROW
EXECUTE FUNCTION calcular_ruta_estimada_trigger();




