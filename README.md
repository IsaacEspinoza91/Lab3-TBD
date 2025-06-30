
# Lab 3 – Taller de Base de Datos

📌 Aplicación Web diseñada para gestionar el delivery de medicamentos a domicilio. Permite administrar pedidos, clientes, farmacias, productos y repartidores, ofreciendo consultas analíticas y automatización de procesos.

---

## 👨‍💻 Integrantes – Grupo 1 (Ejecución)

- [@EmirSilva](https://github.com/EmirSilva) – Emir Silva  
- [@Bastian444](https://github.com/Bastian444) – Bastián Olea  
- [@IsaacEspinoza91](https://github.com/IsaacEspinoza91) – Isaac Espinoza  
- [@OmarSaez](https://github.com/OmarSaez) – Omar Sáez  
- [@willer0](https://github.com/willer0) – Williams Jiménez  

---

## 🛠️ Tecnologías Utilizadas

- **Backend:** Java 17 + Spring Boot 3.4.5  
- **Frontend:** Vue.js con Vite + Node.js 22.14.0  
- **Base de Datos:** PostgreSQL 17 + PostGIS + MongoDB
- **ORM:** SQL2O 1.9.1  
- **Autenticación:** JSON Web Token (JWT) 0.11.5  

---

# 📂 Configurar la Base de Datos

Antes de iniciar el backend y frontend, sigue estos pasos para crear y poblar la base de datos.

--- 

## 🐘 PostgreSQL + PostGIS

## 1. 💠 Crear la base de datos y activar PostGIS

Conéctate a PostgreSQL por pgAdmin4 o por CMD y ejecuta:

```sql
CREATE DATABASE delivery_medicamentos;
\c delivery_medicamentos
CREATE EXTENSION postgis;
```
o crea la base de datos `delivery_medicamentos` por medio de la interfaz de pgAdmin4
---

## 2. 🌍 Cargar datos georeferenciales (Chile)

Descarga el archivo desde Google Drive:

🔗 [Laboratorio\_2\_Geom\_TDB\_GRUPO1.sql](https://drive.google.com/file/d/1f-dBOcfms8si1tOwB9Hdn0xI0Y_1RPIt/view?usp=sharing)

---

## 3. 📅 Importar el archivo `.sql` en la base

### Opcion 1 – Por terminal 

```bash
psql -U <tu_usuario> -d delivery_medicamentos -f Laboratorio_2_Geom_TDB_GRUPO1.sql
```

### Opcion 2 – Usando pgAdmin4 (recomendado)

* Selecciona la base `delivery_medicamentos`
* Haz un `Query Tool`
* Carga y ejecuta el archivo `.sql`

🕒 *Nota:* la importación puede tardar entre 2 a 5 minutos.

---
## 🍃 MongoDB

## Requisitos Previos:

Se espera que el usuario que vaya a probar la aplicación tenga instaladas las siguientes herramientas y agregadas en la variable de entorno del sistema 'PATH'
de mongoDB:

* MongoDB, incluyendo MongoDB Compass
* MongoDB Shell Download (https://www.mongodb.com/try/download/shell#:~:text=Descarga%20de%20MongoDB%20Shell)
* MongoDB Command Line Database Tools Download (https://www.mongodb.com/try/download/database-tools#:~:text=MongoDB%20Command%20Line%20Database%20Tools%20Download)

## 1. 💠 Inicializar la base de datos

Inicializar el servicio MongoDB en una terminal CMD con el comando:

```bash
> mongod
```
```bash
> use delivery-med-mongo
```
```bash
> db.myCollection.insertOne({ name: "example" })
```

## 2. Poblar la base de datos


Hemos creado un archivo que facilita el poblado de datos llamado "poblado.bat", al hacer doble click en este se ejecutará el poblado.

---

## 🚀 Cómo Ejecutar el Proyecto

1. **Clonar repositorio**  
   ```bash
   git clone https://github.com/IsaacEspinoza91/Lab2-TBD.git
   cd Lab2-TBD
   ```

2. **Configurar y ejecutar el Backend**  
   - Abrir la carpeta "Backend DeliveryMediamentos" desde un IDE y ejecutar 
   > ⚠️ El backend arranca en el puerto **8080** por defecto.

3. **Ejecutar el Frontend**  
   ```bash
   cd 'Front DeliveryMedicamentos/delivery'
   npm install
   npm run dev
   ```  
   Abre en tu navegador: [http://localhost:5173](http://localhost:5173)

---

## ⚙️ Requisitos de Puertos

| Servicio | Puerto |
| -------- | ------ |
| Backend  | `8080` |
| Frontend | `5173` |

Asegúrate de que estos puertos estén **libres** antes de iniciar.

---


