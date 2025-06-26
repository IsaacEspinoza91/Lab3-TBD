-- Insertar datos en la tabla Usuarios (base común) - Con geometría para repartidores
INSERT INTO Usuarios (RUT, Nombre, Apellido, Email, Password, Telefono, Tipo, geom) VALUES
-- Clientes con ubicaciones reales dentro de las zonas
('12345678-9', 'Maria', 'Gonzalez', 'maria.gonzalez@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy...', '+56912345678', 'CLIENTE', ST_SetSRID(ST_MakePoint(-70.6600, -33.4400), 4326)), -- Centro
('17654321-0', 'Pedro', 'Perez', 'pedro.perez@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy...', '+56987654321', 'CLIENTE', ST_SetSRID(ST_MakePoint(-70.6200, -33.4250), 4326)), -- Providencia
('19283746-5', 'Ana', 'Ramirez', 'ana.ramirez@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy...', '+56911223344', 'CLIENTE', ST_SetSRID(ST_MakePoint(-70.5800, -33.4100), 4326)), -- Las Condes
('6473829-1', 'Jorge', 'Silva', 'jorge.silva@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy...', '+56999887766', 'CLIENTE', ST_SetSRID(ST_MakePoint(-70.5700, -33.5100), 4326)), -- La Florida
('15374659-7', 'Laura', 'Torres', 'laura.torres@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy...', '+56955667788', 'CLIENTE', ST_SetSRID(ST_MakePoint(-70.6100, -33.4500), 4326)), -- Ñuñoa
('12839201-3', 'Diego', 'Morales', 'diego.morales@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy...', '+56966778899', 'CLIENTE', ST_SetSRID(ST_MakePoint(-70.5700, -33.3900), 4326)), -- Vitacura
('20213487-6', 'Camila', 'Fernandez', 'camila.fernandez@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy...', '+56977889900', 'CLIENTE', ST_SetSRID(ST_MakePoint(-70.6400, -33.4100), 4326)), -- Recoleta
('18820194-2', 'Ignacio', 'Vega', 'ignacio.vega@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy...', '+56988990011', 'CLIENTE', ST_SetSRID(ST_MakePoint(-70.6200, -33.4800), 4326)), -- Macul
('17928476-4', 'Valentina', 'Navarro', 'valentina.navarro@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy...', '+56999001122', 'CLIENTE', ST_SetSRID(ST_MakePoint(-70.6700, -33.4350), 4326)), -- Centro
('16029375-1', 'Matias', 'Cortes', 'matias.cortes@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy...', '+56912344321', 'CLIENTE', ST_SetSRID(ST_MakePoint(-70.5900, -33.4300), 4326)), -- Providencia
-- Repartidores (con geometría)
('11223344-5', 'Carlos', 'Diaz', 'carlos.diaz@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy...', '+56911112222', 'REPARTIDOR', ST_SetSRID(ST_MakePoint(-70.6500, -33.4500), 4326)),
('22334455-6', 'Francisca', 'Soto', 'francisca.soto@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy...', '+56922223333', 'REPARTIDOR', ST_SetSRID(ST_MakePoint(-70.6000, -33.4300), 4326)),
('20445566-7', 'Luis', 'Rojas', 'luis.rojas@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy...', '+56933334444', 'REPARTIDOR', ST_SetSRID(ST_MakePoint(-70.5700, -33.4200), 4326)),
('19556677-8', 'Paula', 'Reyes', 'paula.reyes@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy...', '+56944445555', 'REPARTIDOR', ST_SetSRID(ST_MakePoint(-70.5900, -33.5200), 4326)),
('18667788-9', 'Tomas', 'Fuentes', 'tomas.fuentes@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy...', '+56955556666', 'REPARTIDOR', ST_SetSRID(ST_MakePoint(-70.5800, -33.4100), 4326)),
-- Administradores
('19887766-5', 'Jorge', 'Gonzalez', 'sali.con.tu.mujer@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy...', '+56912345678', 'ADMIN', ST_SetSRID(ST_MakePoint(-70.5800, -33.3100), 4326)),
('18776655-4', 'Claudio', 'Narea', 'claudio.n@gmail.com', '$2a$10$N9qo8uLOickgx2ZMRZoMy...', '+56998765432', 'ADMIN', ST_SetSRID(ST_MakePoint(-70.5900, -33.3300), 4326));

-- Insertar datos específicos de Clientes
INSERT INTO Clientes (Usuario_ID, Direccion) VALUES
(1, 'Av. Libertador Bernardo O Higgins 1234'),
(2, 'Calle Maipú 123, Providencia'),
(3, 'Los Olivos 456, Las Condes'),
(4, 'Av. Central 789, La Florida'),
(5, 'Pje. Las Rosas 321, Ñuñoa'),
(6, 'Av. San Carlos 1001, Apoquindo'),
(7, 'Pasaje Norte 45, Recoleta'),
(8, 'Calle Secundaria 21, Macul'),
(9, 'Calle Principal 88, Santiago Centro'),
(10, 'El Roble 1123, Vitacura');

-- Insertar datos específicos de Repartidores
INSERT INTO Repartidores (Usuario_ID, Tipo_vehiculo) VALUES
(11, 'AUTO'),
(12, 'MOTO'),
(13, 'BICICLETA'),
(14, 'CAMIONETA'),
(15, 'MOTO');

-- Insertar datos específicos de Administradores
INSERT INTO Administradores (Usuario_ID, Nivel_acceso, Departamento) VALUES
(16, 1, 'Sistemas'),
(17, 2, 'Operaciones');

-- Insertar datos en la tabla Farmacias con ubicaciones reales en Santiago
INSERT INTO Farmacias (Nombre, Lugar, geom) VALUES 
('Farmacia San Juan', 'Av. Providencia 1234', ST_SetSRID(ST_MakePoint(-70.6223, -33.4378), 4326)),
('Farmacia La Salud', 'Av. Las Condes 9876', ST_SetSRID(ST_MakePoint(-70.5756, -33.4089), 4326)),
('Farmacia El Bienestar', 'Av. La Florida 4567', ST_SetSRID(ST_MakePoint(-70.5932, -33.5227), 4326)),
('Farmacia Santa Maria', 'Av. Vitacura 2020', ST_SetSRID(ST_MakePoint(-70.5793, -33.4102), 4326)),
('Farmacia Central', 'Plaza de Armas 100', ST_SetSRID(ST_MakePoint(-70.6502, -33.4378), 4326)),
('Farmacia La Vida', 'Av. Tobalaba 1500', ST_SetSRID(ST_MakePoint(-70.6005, -33.4250), 4326)),
('Farmacia Salud Usach', 'Av. Libertador Bernardo O Higgins 3363', ST_SetSRID(ST_MakePoint(-70.6800, -33.4550), 4326));

-- Insertar datos en la tabla Medios_de_pago
INSERT INTO Medios_de_pago (Tipo) VALUES
('Efectivo'),
('Tarjeta');

-- Insertar datos en la tabla Productos
INSERT INTO Productos (Nombre, Precio, Stock, Requiere_receta) VALUES
('Aspirina', 8400, 100, false),
('Ibuprofeno', 12240, 150, false),
('Amoxicilina', 20000, 50, true),
('Paracetamol', 6400, 200, false),
('Diazepam', 14400, 80, true),
('Antibiótico', 28000, 60, true),
('Vitamina C', 4400, 300, false),
('Jarabe para la tos', 9600, 120, false),
('Loratadina', 7200, 130, false),
('Clonazepam', 17600, 90, true),
('Glicerina', 6000, 180, false),
('Cefalexina', 22400, 70, true),
('Lactulosa', 12800, 150, false),
('Paroxetina', 24000, 40, true),
('Omeprazol', 11000, 160, false),
('Metformina', 13400, 140, true),
('Enalapril', 9800, 75, true),
('Losartán', 10200, 85, true),
('Salbutamol', 11500, 95, false),
('Simvastatina', 12600, 100, true),
('Cetirizina', 7800, 120, false),
('Prednisona', 15000, 65, true),
('Miconazol', 8800, 70, false),
('Paroxetina', 24000, 40, true);

-- Insertar datos en la tabla Pedidos (las rutas serán calculadas por trigger)
INSERT INTO Pedidos (Fecha, Urgencia, Total_pagado, Estado_entrega, Fecha_entrega, Cliente_ID, Medio_pago_ID, Farmacia_ID, Repartidor_ID) VALUES
('2025-04-01', false, 21200, 'Entregado', '2025-04-03', 1, 1, 1, 11),
('2025-04-02', true, 44000, 'Pendiente', NULL, 2, 2, 2, 12),
('2025-04-03', false, 6400, 'Entregado', '2025-04-04', 3, 1, 3, 13),
('2025-04-03', true, 38880, 'Pendiente', NULL, 4, 2, 4, 14),
('2025-04-04', false, 9600, 'Entregado', '2025-04-06', 5, 1, 5, 15),
('2025-04-05', true, 32000, 'Pendiente', NULL, 6, 2, 6, 11),
('2025-04-06', false, 8800, 'Entregado', '2025-04-08', 7, 1, 1, 12),
('2025-04-06', true, 25200, 'Pendiente', NULL, 8, 2, 2, 13),
('2025-04-07', false, 7200, 'Entregado', '2025-04-09', 9, 1, 3, 14),
('2025-04-07', true, 22400, 'Pendiente', NULL, 10, 2, 4, 15),
('2025-04-08', false, 12800, 'Entregado', '2025-04-10', 1, 1, 5, 11),
('2025-04-08', true, 17600, 'Pendiente', NULL, 2, 2, 6, 12),
('2025-04-09', false, 24900, 'Entregado', '2025-04-11', 3, 1, 1, 13),
('2025-04-09', true, 10200, 'Pendiente', NULL, 4, 2, 2, 14),
('2025-04-10', false, 15600, 'Entregado', '2025-04-12', 5, 1, 3, 15),
('2025-04-11', true, 23800, 'Pendiente', NULL, 6, 2, 4, 11),
('2025-04-11', false, 28000, 'Entregado', '2025-04-13', 7, 1, 5, 12),
('2025-04-12', true, 6000, 'Pendiente', NULL, 8, 2, 6, 13),
('2025-04-13', false, 9800, 'Entregado', '2025-04-15', 9, 1, 1, 14),
('2025-04-13', true, 15000, 'Pendiente', NULL, 10, 2, 2, 15);

-- Insertar datos para detalle de pedidos
INSERT INTO Detalle_de_pedidos (Pedido_ID, Producto_ID, Cantidad) VALUES
(1, 1, 2),
(1, 7, 1),
(2, 3, 1),
(2, 14, 1),
(3, 4, 1),
(4, 2, 2),
(4, 5, 1),
(5, 8, 1),
(6, 5, 1),
(6, 10, 1),
(7, 15, 1),
(8, 16, 2),
(9, 9, 1),
(10, 12, 1),
(11, 7, 1),
(11, 1, 1),
(12, 10, 1),
(13, 17, 1),
(13, 18, 1),
(14, 20, 1),
(15, 21, 2),
(16, 13, 1),
(16, 19, 1),
(17, 6, 1),
(18, 11, 1),
(19, 22, 1),
(20, 23, 1);

-- Poblar tabla Calificaciones
INSERT INTO Calificaciones (Puntuacion, Estrellas, Cliente_ID, Repartidor_ID) VALUES
('Muy rápido y amable', 5, 1, 11),
('Demorado pero llegó', 3, 2, 12),
('Excelente servicio', 5, 3, 11),
('Poco cordial', 2, 4, 13),
('Normal', 4, 5, 12),
('No trajo todo', 2, 6, 14),
('Perfecto', 5, 7, 11),
('Se perdió en el camino', 1, 8, 15),
('Servicio aceptable', 3, 9, 12),
('Amable y rápido', 5, 10, 13),
('Atención excelente', 5, 1, 14),
('Muy educado', 4, 2, 15),
('Se equivocó de dirección', 2, 3, 12),
('Llegó frío el pedido', 2, 4, 11),
('Volvería a pedir', 5, 5, 15),
('Sin problemas', 4, 6, 13),
('Tardó demasiado', 2, 7, 14),
('Buen trato', 5, 8, 12),
('Me llamó para confirmar', 4, 9, 11),
('Entrega incompleta', 1, 10, 15),
('Entrega puntual', 5, 1, 13),
('Se pasó la hora', 2, 2, 14),
('Todo correcto', 4, 3, 12),
('Amable y atento', 5, 4, 11);

-- Poblar Tabla intermediaria Farmacia y productos
INSERT INTO Farmacias_Productos (Farmacia_ID, Producto_ID) VALUES
(1, 1), (1, 2), (1, 3), (1, 4), (1, 5),
(2, 6), (2, 7), (2, 8), (2, 9), (2, 10),
(3, 11), (3, 12), (3, 13), (3, 14), (3, 15),
(4, 16), (4, 17), (4, 18), (4, 19), (4, 20),
(5, 21), (5, 22), (5, 23), (5, 1), (5, 2),
(6, 3), (6, 6), (6, 9), (6, 12), (6, 18);

-- Insertar datos en la tabla Pedidos_Repartidores
INSERT INTO Pedidos_Repartidores (Pedido_ID, Repartidor_ID) VALUES
(1, 11),
(2, 12),
(3, 13),
(4, 14),
(5, 15),
(6, 11),
(7, 12),
(8, 13),
(9, 14),
(10, 15),
(11, 11),
(12, 12),
(13, 13),
(14, 14),
(15, 15),
(16, 11),
(17, 12),
(18, 13),
(19, 14),
(20, 15);

-- Insertar datos en la tabla Zonas de Cobertura (polígonos aproximados de comunas en Santiago)
INSERT INTO Zonas_cobertura (nombre, geom) VALUES
('Centro', ST_SetSRID(ST_GeomFromText('POLYGON((-70.6800 -33.4500, -70.6500 -33.4500, -70.6500 -33.4300, -70.6800 -33.4300, -70.6800 -33.4500))'), 4326)),
('Providencia', ST_SetSRID(ST_GeomFromText('POLYGON((-70.6500 -33.4300, -70.6000 -33.4300, -70.6000 -33.4200, -70.6500 -33.4200, -70.6500 -33.4300))'), 4326)),
('Las Condes', ST_SetSRID(ST_GeomFromText('POLYGON((-70.6000 -33.4200, -70.5500 -33.4200, -70.5500 -33.4000, -70.6000 -33.4000, -70.6000 -33.4200))'), 4326)),
('La Florida', ST_SetSRID(ST_GeomFromText('POLYGON((-70.6000 -33.5300, -70.5500 -33.5300, -70.5500 -33.5000, -70.6000 -33.5000, -70.6000 -33.5300))'), 4326)),
('Ñuñoa', ST_SetSRID(ST_GeomFromText('POLYGON((-70.6300 -33.4700, -70.5800 -33.4700, -70.5800 -33.4400, -70.6300 -33.4400, -70.6300 -33.4700))'), 4326)),
('Vitacura', ST_SetSRID(ST_GeomFromText('POLYGON((-70.6000 -33.4000, -70.5500 -33.4000, -70.5500 -33.3800, -70.6000 -33.3800, -70.6000 -33.4000))'), 4326)),
('Recoleta', ST_SetSRID(ST_GeomFromText('POLYGON((-70.6500 -33.4200, -70.6300 -33.4200, -70.6300 -33.4000, -70.6500 -33.4000, -70.6500 -33.4200))'), 4326)),
('Macul', ST_SetSRID(ST_GeomFromText('POLYGON((-70.6300 -33.5000, -70.6000 -33.5000, -70.6000 -33.4700, -70.6300 -33.4700, -70.6300 -33.5000))'), 4326));

-- Insertar puntos de entrega para Farmacia San Juan (ID 1)
INSERT INTO Punto_de_entrega (Nombre, Farmacia_ID, geom) VALUES
('Kiosco Centro Providencia', 1, ST_SetSRID(ST_MakePoint(-70.6180, -33.4385), 4326)),
('Minimarket Bella Vista', 1, ST_SetSRID(ST_MakePoint(-70.6250, -33.4350), 4326)),
('Depósito Los Leones', 1, ST_SetSRID(ST_MakePoint(-70.6150, -33.4420), 4326)),
('Tienda Metro Salvador', 1, ST_SetSRID(ST_MakePoint(-70.6280, -33.4400), 4326)),
('Supermercado Providencia', 1, ST_SetSRID(ST_MakePoint(-70.6200, -33.4320), 4326)),
('Local Manuel Montt', 1, ST_SetSRID(ST_MakePoint(-70.6225, -33.4300), 4326));

-- Insertar puntos de entrega para Farmacia La Salud (ID 2)
INSERT INTO Punto_de_entrega (Nombre, Farmacia_ID, geom) VALUES
('Almacén Las Condes', 2, ST_SetSRID(ST_MakePoint(-70.5700, -33.4050), 4326)),
('Tienda El Golf', 2, ST_SetSRID(ST_MakePoint(-70.5800, -33.4150), 4326)),
('Depósito Apoquindo', 2, ST_SetSRID(ST_MakePoint(-70.5750, -33.4100), 4326)),
('Minimarket San Damián', 2, ST_SetSRID(ST_MakePoint(-70.5720, -33.4000), 4326)),
('Kiosco Parque Arauco', 2, ST_SetSRID(ST_MakePoint(-70.5650, -33.4080), 4326)),
('Local Alonso de Córdova', 2, ST_SetSRID(ST_MakePoint(-70.5780, -33.4120), 4326)),
('Tienda Vitacura Center', 2, ST_SetSRID(ST_MakePoint(-70.5680, -33.4020), 4326));

-- Insertar puntos de entrega para Farmacia El Bienestar (ID 3)
INSERT INTO Punto_de_entrega (Nombre, Farmacia_ID, geom) VALUES
('Almacén La Florida Centro', 3, ST_SetSRID(ST_MakePoint(-70.5900, -33.5200), 4326)),
('Depósito Vicuña Mackenna', 3, ST_SetSRID(ST_MakePoint(-70.5950, -33.5250), 4326)),
('Minimarket Plaza La Florida', 3, ST_SetSRID(ST_MakePoint(-70.6000, -33.5180), 4326)),
('Tienda Santa Julia', 3, ST_SetSRID(ST_MakePoint(-70.5850, -33.5150), 4326)),
('Kiosco San José de La Estrella', 3, ST_SetSRID(ST_MakePoint(-70.5880, -33.5300), 4326)),
('Local Mirador Azul', 3, ST_SetSRID(ST_MakePoint(-70.5920, -33.5350), 4326)),
('Supermercado La Florida', 3, ST_SetSRID(ST_MakePoint(-70.5980, -33.5220), 4326)),
('Depósito Rojas Magallanes', 3, ST_SetSRID(ST_MakePoint(-70.6020, -33.5280), 4326));

-- Insertar puntos de entrega para Farmacia Santa Maria (ID 4)
INSERT INTO Punto_de_entrega (Nombre, Farmacia_ID, geom) VALUES
('Tienda Vitacura Alto', 4, ST_SetSRID(ST_MakePoint(-70.5750, -33.4050), 4326)),
('Almacén Bicentenario', 4, ST_SetSRID(ST_MakePoint(-70.5700, -33.4150), 4326)),
('Minimarket Alonso de Córdova', 4, ST_SetSRID(ST_MakePoint(-70.5800, -33.4080), 4326)),
('Depósito Sanhattan', 4, ST_SetSRID(ST_MakePoint(-70.5720, -33.4000), 4326)),
('Kiosco Parque Bicentenario', 4, ST_SetSRID(ST_MakePoint(-70.5780, -33.4020), 4326)),
('Local Nueva Costanera', 4, ST_SetSRID(ST_MakePoint(-70.5730, -33.4120), 4326)),
('Supermercado Vitacura', 4, ST_SetSRID(ST_MakePoint(-70.5760, -33.4070), 4326));

-- Insertar puntos de entrega para Farmacia Central (ID 5)
INSERT INTO Punto_de_entrega (Nombre, Farmacia_ID, geom) VALUES
('Almacén Plaza de Armas', 5, ST_SetSRID(ST_MakePoint(-70.6480, -33.4380), 4326)),
('Tienda Paseo Ahumada', 5, ST_SetSRID(ST_MakePoint(-70.6520, -33.4400), 4326)),
('Depósito Mercado Central', 5, ST_SetSRID(ST_MakePoint(-70.6550, -33.4350), 4326)),
('Minimarket Bandera', 5, ST_SetSRID(ST_MakePoint(-70.6500, -33.4420), 4326)),
('Kiosco La Moneda', 5, ST_SetSRID(ST_MakePoint(-70.6530, -33.4450), 4326)),
('Local Estación Central', 5, ST_SetSRID(ST_MakePoint(-70.6580, -33.4500), 4326)),
('Supermercado Centro', 5, ST_SetSRID(ST_MakePoint(-70.6450, -33.4400), 4326)),
('Depósito Mapocho', 5, ST_SetSRID(ST_MakePoint(-70.6600, -33.4300), 4326)),
('Tienda Santa Lucía', 5, ST_SetSRID(ST_MakePoint(-70.6470, -33.4350), 4326));

-- Insertar puntos de entrega para Farmacia La Vida (ID 6)
INSERT INTO Punto_de_entrega (Nombre, Farmacia_ID, geom) VALUES
('Almacén Tobalaba', 6, ST_SetSRID(ST_MakePoint(-70.6050, -33.4250), 4326)),
('Tienda Pedro de Valdivia', 6, ST_SetSRID(ST_MakePoint(-70.5950, -33.4200), 4326)),
('Depósito Los Leones', 6, ST_SetSRID(ST_MakePoint(-70.6000, -33.4300), 4326)),
('Minimarket El Golf', 6, ST_SetSRID(ST_MakePoint(-70.6020, -33.4220), 4326)),
('Kiosco Parque Bustamante', 6, ST_SetSRID(ST_MakePoint(-70.5980, -33.4280), 4326)),
('Local Metro Tobalaba', 6, ST_SetSRID(ST_MakePoint(-70.6030, -33.4230), 4326)),
('Supermercado Providencia', 6, ST_SetSRID(ST_MakePoint(-70.5950, -33.4250), 4326));

-- Insertar puntos de entrega para Farmacia Salud Usach (ID 7)
INSERT INTO Punto_de_entrega (Nombre, Farmacia_ID, geom) VALUES
('Almacén Estación Central', 7, ST_SetSRID(ST_MakePoint(-70.6850, -33.4550), 4326)),
('Tienda Usach', 7, ST_SetSRID(ST_MakePoint(-70.6750, -33.4500), 4326)),
('Depósito San Alberto Hurtado', 7, ST_SetSRID(ST_MakePoint(-70.6800, -33.4600), 4326)),
('Minimarket Ecuador', 7, ST_SetSRID(ST_MakePoint(-70.6780, -33.4520), 4326)),
('Kiosco Alameda', 7, ST_SetSRID(ST_MakePoint(-70.6820, -33.4480), 4326)),
('Local España', 7, ST_SetSRID(ST_MakePoint(-70.6720, -33.4550), 4326)),
('Supermercado Estación Central', 7, ST_SetSRID(ST_MakePoint(-70.6900, -33.4500), 4326)),
('Depósito Las Rejas', 7, ST_SetSRID(ST_MakePoint(-70.6850, -33.4650), 4326)),
('Tienda Maestranza', 7, ST_SetSRID(ST_MakePoint(-70.6750, -33.4650), 4326));


--- Insertar puntos de interes
INSERT INTO puntos_de_interes (nombre, lugar, geom) VALUES
-- Hospitales
('Hospital Clínico Universidad de Chile', 'Independencia', ST_SetSRID(ST_MakePoint(-70.653985, -33.416485), 4326)),
('Hospital El Carmen Dr. Luis Valentín Ferrada', 'Maipú', ST_SetSRID(ST_MakePoint(-70.764697, -33.502798), 4326)),

-- Centros de Salud Mental
('Instituto Psiquiátrico Dr. José Horwitz Barak', 'Recoleta', ST_SetSRID(ST_MakePoint(-70.654195, -33.414376), 4326)),
('Centro de Salud Mental Amulen – La Florida', 'La Florida', ST_SetSRID(ST_MakePoint(-70.567717, -33.548056), 4326)),

-- Clínicas
('Clínica Alemana de Santiago', 'Vitacura', ST_SetSRID(ST_MakePoint(-70.568645, -33.393114), 4326)),
('Clínica Santa María', 'Providencia', ST_SetSRID(ST_MakePoint(-70.630466, -33.424473), 4326)),

-- Consultorios (CESFAM)
('CESFAM Padre Alberto Hurtado – Pudahuel', 'Pudahuel', ST_SetSRID(ST_MakePoint(-70.761046, -33.444162), 4326)),
('CESFAM Dr. Salvador Bustos – Ñuñoa', 'Ñuñoa', ST_SetSRID(ST_MakePoint(-70.603940, -33.463139), 4326));
