------Se tiene las sentencias del LABS 1 y LABS 2---------------------------------

-----------------------------------------LABS 1------------------------------------

-- CONSULTAS
-- 1. ¿Qué cliente ha gastado más dinero en pedidos entregados? [Omar]
SELECT u.Nombre, u.Apellido, c.Usuario_ID AS Cliente_ID, SUM(p.Total_pagado) AS TotalGastado
FROM Clientes c
JOIN Usuarios u ON c.Usuario_ID = u.ID
JOIN Pedidos p ON p.Cliente_ID = c.Usuario_ID
WHERE p.Estado_entrega = 'Entregado'
GROUP BY c.Usuario_ID, u.Nombre, u.Apellido
ORDER BY TotalGastado DESC
LIMIT 1;


--2. ¿Cuáles son los productos más pedidos en el último mes por categoría?

SELECT 
    p.nombre AS "Producto",
    p.requiere_receta AS "Requiere_Receta",
    SUM(dp.cantidad) AS "Total_Pedidos"
FROM 
    productos p
JOIN 
    detalle_de_pedidos dp ON p.id = dp.producto_id
JOIN 
    pedidos pe ON pe.id = dp.pedido_id
WHERE 
    pe.fecha >= DATE_TRUNC('month', CURRENT_DATE) - INTERVAL '1 month'
    AND pe.fecha < DATE_TRUNC('month', CURRENT_DATE)
GROUP BY 
    p.id, p.nombre, p.requiere_receta
ORDER BY 
    p.requiere_receta DESC, SUM(dp.cantidad) DESC;

-- 3. Listar las farmacias con más entregas fallidas.[Isaac]
SELECT f.id, f.nombre, f.lugar, COUNT(*) AS entregas_fallidas
FROM pedidos AS p INNER JOIN farmacias AS F
	ON p.farmacia_id = f.id
WHERE p.estado_entrega = 'Fallido'
GROUP BY f.id, f.nombre, f.lugar
ORDER BY entregas_fallidas DESC;


-- 4. Calcular el tiempo promedio entre pedido y entrega por repartidor. [Emir]
SELECT 
    u.nombre AS repartidores_nombre,
    ROUND(AVG(EXTRACT(DAY FROM AGE(p.fecha_entrega, p.fecha))), 1) AS tiempo_promedio_dias
FROM Pedidos p
JOIN Repartidores r ON p.repartidor_id = r.usuario_id
JOIN Usuarios u ON r.usuario_id = u.id
WHERE p.fecha_entrega IS NOT NULL
GROUP BY u.id, u.nombre;



-- 5. Obtener los 3 repartidores con mejor rendimiento (basado en puntuación). [Williams]
WITH Ranking AS (
	SELECT
		repartidor_id,
		ROUND(AVG(estrellas),2) AS puntuacion, 
		RANK() OVER (ORDER BY ROUND(AVG(estrellas),2) DESC) AS ranking
	FROM public.calificaciones
	GROUP BY repartidor_id
	ORDER BY puntuacion DESC
)
SELECT
	I.nombre,
	I.apellido,
	R.puntuacion
FROM Ranking R
JOIN public.usuarios I ON R.repartidor_id = I.id
WHERE R.ranking <= 3
ORDER BY R.ranking;


-- 6. ¿Qué medio de pago se utiliza más en pedidos urgentes? [Emir]
SELECT mdp.tipo AS medio_pago, COUNT(*) AS cantidad_usos
FROM pedidos p
JOIN medios_de_pago mdp ON p.medio_pago_id = mdp.id
WHERE p.urgencia = true
GROUP BY mdp.tipo
ORDER BY cantidad_usos DESC
LIMIT 1;



-- PROCEDIMIENTOS ALMACENADOS
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
CALL cambiar_estado_pedido(2,'Fallido');


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

-- TRIGGERS
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
AFTER INSERT ON DetalleDePedidos
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

-- VISTAS
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


-- Extra
-- Ranking de productos devultos
SELECT 
    p.ID AS producto_id,
    p.Nombre AS nombre_producto,
    COUNT(dp.ID) AS veces_devuelto,
    CONCAT(
        ROUND(
            COUNT(dp.ID) * 100.0 / 
            NULLIF(SUM(COUNT(dp.ID)) OVER (), 0), -- Evita división por cero
            2
        ), 
        '%'
    ) AS porcentaje_devoluciones
FROM 
    Productos p
JOIN 
    Detalle_de_pedidos dp ON p.ID = dp.Producto_ID
JOIN 
    Pedidos ped ON dp.Pedido_ID = ped.ID
WHERE 
    ped.Estado_entrega = 'Devuelto'
GROUP BY 
    p.ID, p.Nombre
ORDER BY 
    veces_devuelto DESC
LIMIT 10;


-- Ranking de productos cancelados
SELECT 
    p.ID AS producto_id,
    p.Nombre AS nombre_producto,
    COUNT(dp.ID) AS veces_cancelado,
    CONCAT(
        ROUND(
            COUNT(dp.ID) * 100.0 / 
            NULLIF(SUM(COUNT(dp.ID)) OVER (), 0), -- Evita división por cero
            2
        ), 
        '%'
    ) AS porcentaje_cancelaciones
FROM 
    Productos p
JOIN 
    Detalle_de_pedidos dp ON p.ID = dp.Producto_ID
JOIN 
    Pedidos ped ON dp.Pedido_ID = ped.ID
WHERE 
    ped.Estado_entrega = 'Cancelado'
GROUP BY 
    p.ID, p.Nombre
ORDER BY 
    veces_cancelado DESC
LIMIT 10;




------------------------------------------------------------------------------

--------------------------------Sentecias LAB 2-------------------------------


-- 1. (Isaac) Encontrar los 5 puntos de entrega más cercanos a una farmacia o empresa asociada.
SELECT 
    f.ID AS farmacia_id,
    f.Nombre AS farmacia_nombre,
    p.ID AS punto_entrega_id,
    p.Nombre AS punto_entrega_nombre,
    ST_DistanceSphere(f.geom, p.geom) AS distancia_metros
FROM Farmacias f
JOIN LATERAL (		-- Join lateral permite acceder a columnas de tabla farmacia (para calcular distancia)
    SELECT p.*
    FROM Punto_de_entrega p
    ORDER BY ST_DistanceSphere(f.geom, p.geom)
    LIMIT 5
) p ON true			-- Como no hay condicion de JOIN, usa un valor true
ORDER BY f.ID, distancia_metros;	-- ordenamiento segun distancia cercana


-- 2. (Williams) Determinar si un cliente se encuentra dentro de una zona de cobertura.
SELECT z.id, z.nombre, z.geom
FROM zonas_cobertura z
JOIN usuarios u ON u.id = 2 -- Reemplaza 2 por el ID del usuario real consultado
WHERE ST_Intersects(z.geom, u.geom);


-- 3. (Emir) Calcular la distancia total recorrida por un repartidor en el último mes.
SELECT
    U.Nombre AS Repartidor_Nombre,
    U.Apellido AS Repartidor_Apellido,
    TO_CHAR(P.fecha_entrega, 'YYYY-MM') AS Mes_Entrega,
    SUM(ST_Length(P.ruta_estimada::geography, true)) AS Distancia_Total_Metros
FROM
    Pedidos P
JOIN
    Usuarios U ON P.Repartidor_ID = U.ID
WHERE
    P.estado_entrega = 'Entregado'
    AND P.fecha_entrega IS NOT NULL
    AND P.ruta_estimada IS NOT NULL
GROUP BY
    U.ID, U.Nombre, U.Apellido, Mes_Entrega
ORDER BY
    Repartidor_Nombre, Repartidor_Apellido, Mes_Entrega DESC;



-- 4. (Isaac) Identificar el punto de entrega más lejano desde cada empresa asociada.
SELECT DISTINCT ON (f.id) 			-- selecciona solo la primera fila de cada columna f.id (ya tiene orden decreciente)
    f.id AS farmacia_id,
    f.Nombre AS farmacia_nombre,
    p.id AS punto_entrega_id,
    p.Nombre AS punto_entrega_nombre,
    ST_DistanceSphere(f.geom, p.geom) AS distancia_metros   -- calculo distancia entre los dos puntos en metros
FROM Farmacias f JOIN Punto_de_entrega p ON p.farmacia_id = f.id
ORDER BY f.id, ST_DistanceSphere(f.geom, p.geom) DESC;  -- ordenamiento segun distancia mas lejana

-- 5. (Omar)Listar todos los pedidos cuya ruta estimada cruce más de 2 zonas de reparto.
SELECT 
    p.id AS pedido_id,
    STRING_AGG(DISTINCT z.nombre, ', ') AS zonas_cruzadas,
    COUNT(DISTINCT z.id) AS cantidad_zonas
FROM pedidos p
JOIN zonas_cobertura z ON ST_Intersects(p.ruta_estimada, z.geom)
GROUP BY p.id
HAVING COUNT(DISTINCT z.id) > 2
ORDER BY cantidad_zonas DESC

-- 6. (Bastian) Determinar los clientes que estan a mas de 5km de cualquier empresa o farmacia. ?Empresa?
-- Selecciona todos los campos de la tabla 'usuarios' (u)
SELECT
    u.id AS usuarioId, -- Cambiado de 'id' a 'usuarioId'
    u.nombre AS nombre,
    u.email AS email
-- Si 'direccion' existe en 'usuarios', agrégala aquí:
-- u.direccion AS direccion
FROM
    usuarios u
WHERE
    u.tipo = 'CLIENTE'
  AND u.geom IS NOT NULL
  AND NOT EXISTS (
    SELECT 1
    FROM farmacias f
    WHERE ST_DWithin(
                  ST_SetSRID(u.geom, 4326)::geography,
                  ST_SetSRID(f.geom, 4326)::geography,
                  5000
          )
);

-- Sentencias extras ---

-- 1. Implementar una función que calcule automáticamente la zona a la que pertenece un cliente.
SELECT 
    nombre_cliente AS nombreCliente,
    nombre_zona AS nombreZona,
    ubicacion_cliente AS ubicacionCliente,
    poligono_zona AS poligonoZona
FROM Zonas_clientes
WHERE cliente_id = :idCliente
LIMIT 1


-- 2. Detectar zonas con alta densidad de pedidos mediante agregación de puntos.
SELECT
    zc.ID AS idZona,
    zc.nombre AS nombreZona,
    COUNT(p.ID) AS cantidadPedidos
FROM
    Zonas_cobertura zc
JOIN
    Pedidos p ON ST_Contains(zc.geom, ST_StartPoint(p.ruta_estimada)) 
WHERE
    p.ruta_estimada IS NOT NULL
GROUP BY
    zc.ID, zc.nombre
ORDER BY
    cantidadPedidos DESC;

-- 3. Crear una tabla de puntos de interés cercanos (hospitales, centros logísticos, etc.) y consultarlos con ST_DWithin.

SELECT 
    pi.id, 
    pi.nombre, 
    pi.lugar, 
    ST_Y(pi.geom) AS latitud, 
    ST_X(pi.geom) AS longitud
FROM 
    puntos_de_interes AS pi, 
    usuarios AS u
WHERE 
    u.id = 1  -- Reemplaza con el ID del usuario deseado
    AND ST_DWithin(u.geom::geography, pi.geom::geography, 3000);  -- Distancia en metros
