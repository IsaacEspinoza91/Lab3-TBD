<template>
  <div class="dashboard">
    <h1>Resumen del Sistema</h1>
    <div class="stats-grid">
      <router-link to="/admin/usuarios" class="stat-card clickable-card">
        <h3>Usuarios</h3>
        <div v-if="loading" class="loading-indicator">
          <div class="loader"></div>
          <span>Cargando...</span>
        </div>
        <p v-else>{{ usuariosCount }}</p>
      </router-link>

      <router-link to="/admin/farmacias" class="stat-card clickable-card">
        <h3>Farmacias</h3>
        <div v-if="loading" class="loading-indicator">
          <div class="loader"></div>
          <span>Cargando...</span>
        </div>
        <p v-else>{{ farmaciasCount }}</p>
      </router-link>

      <router-link to="/admin/pedidos" class="stat-card clickable-card">
        <h3>Pedidos del Mes</h3>
        <div v-if="loading" class="loading-indicator">
          <div class="loader"></div>
          <span>Cargando...</span>
        </div>
        <p v-else>{{ pedidosMesCount }}</p>
      </router-link>

      <router-link to="/admin/productos" class="stat-card clickable-card">
        <h3>Productos</h3>
        <div v-if="loading" class="loading-indicator">
          <div class="loader"></div>
          <span>Cargando...</span>
        </div>
        <p v-else>{{ productosCount }}</p>
      </router-link>
    </div>

    <!-- Sección de consultas -->
    <div class="consultas-section">
      <h2>Consultas del Sistema</h2>
      <div class="consulta-selector">
        <select v-model="consultaSeleccionada" @change="manejarCambioConsulta" class="consulta-dropdown">
          <option value="" disabled selected>Seleccione una consulta...</option>
          <option value="1">Consulta N°1: Encontrar los 5 puntos de entrega más cercanos a una farmacia o empresa
            asociada.</option>
          <option value="2">Consulta N°2: Determinar si un cliente se encuentra dentro de una zona de cobertura.
          </option>
          <option value="3">Consulta N°3: Calcular la distancia total recorrida por un repartidor en el último mes.
          </option>
          <option value="4">Consulta N°4: Identificar el punto de entrega más lejano desde cada empresa farmacia.
          </option>
          <option value="5">Consulta N°5: Listar todos los pedidos cuya ruta estimada cruce más de 2 zonas de reparto.
          </option>
          <option value="6">Consulta N°6: Determinar los clientes que están a más de 5km de cualquier empresa o
            farmacia.</option>
          <option value="7">Función Extra: Calcular la zona a la que pertenece un cliente.
          </option>
          <option value="8">Función Extra: Detectar zonas con alta densidad de pedidos.
          </option>
          <option value="9">Función Extra: Crear una tabla de puntos de interés cercanos y consultarlos con ST_DWithin.
          </option>
        </select>

        <!-- Mostrar campo de entrada solo para consulta 2 -->
        <div v-if="consultaSeleccionada === '2'" class="input-parametro">
          <label for="clienteId">ID del Cliente:</label>
          <input id="clienteId" type="text" v-model="parametroConsulta" placeholder="Ingrese el ID del cliente"
            class="input-cliente-id" />
          <button @click="ejecutarConsulta2" class="btn-buscar" :disabled="!parametroConsulta || consultaCargando">
            <i class="fas fa-search"></i> Buscar
          </button>
        </div>

        <!-- Input para la consulta 7 -->
        <div v-if="consultaSeleccionada === '7'" class="input-parametro">
          <label for="usuarioId">ID del Usuario:</label>
          <input 
            id="usuarioId" 
            type="text" 
            v-model="parametroConsulta" 
            placeholder="Ingrese el ID del usuario"
            class="input-cliente-id" 
          />
          <button 
            @click="ejecutarConsulta7" 
            class="btn-buscar" 
            :disabled="!parametroConsulta || consultaCargando"
          >
            Buscar
          </button>
        </div>

        <div v-if="consultaCargando" class="loading-indicator">
          <div class="loader"></div>
          <span>Ejecutando consulta...</span>
        </div>

        <div v-if="resultadoConsulta" class="resultado-consulta">
          <h3>Resultado:</h3>

          <!-- Consulta 1: Puntos más cercanos -->
          <div v-if="consultaSeleccionada === '1'" class="consulta-1-container">
            <div class="farmacia-group" v-for="(grupo, farmaciaId) in agruparPorFarmacia(resultadoConsulta)"
              :key="farmaciaId">
              <div class="farmacia-header">
                <h4>{{ grupo.farmaciaNombre }}</h4>
                <span class="badge">{{ grupo.puntos.length }} puntos cercanos</span>
              </div>

              <table class="resultado-table">
                <thead>
                  <tr>
                    <th style="width: 40%">Punto de Entrega</th>
                    <th style="width: 20%">Distancia</th>
                    <th style="width: 20%">Coordenadas</th>
                    <th style="width: 20%">Acciones</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(item, index) in grupo.puntos" :key="index">
                    <td>{{ item.puntoEntregaNombre }}</td>
                    <td>{{ parseFloat(item.distanciaMetros).toFixed(2) }} m</td>
                    <td>{{ item.latitud.toFixed(4) }}, {{ item.longitud.toFixed(4) }}</td>
                    <td>
                      <button @click="abrirMapa(item)" class="map-button">
                        <i class="fas fa-map-marked-alt"></i> Ver Mapa
                      </button>
                    </td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <!-- Elementos de consulta 2 -->
          <div v-else-if="consultaSeleccionada === '2'">

            <!-- Tabla resultados consulta 2 -->
            <div v-if="resultadoConsulta && consultaSeleccionada === '2'">
              <table class="resultado-table">
                <thead>
                  <tr>
                    <th>ID Usuario</th>
                    <th>ID Zona Cobertura</th>
                    <th>Nombre Zona Cobertura</th>
                  </tr>
                </thead>
                <tbody>
                  <tr v-for="(item, index) in resultadoConsulta" :key="index">
                    <td>{{ item.idUsuario }}</td>
                    <td>{{ item.idZonaCobertura }}</td>
                    <td>{{ item.nombreZonaCobertura }}</td>
                  </tr>
                </tbody>
              </table>
            </div>
          </div>

          <!-- Display para la consulta 3: Lista de objetos -->
          <div v-else-if="consultaSeleccionada === '3'">
            <table class="resultado-table">
              <thead>
                <tr>
                  <th>Repartidor</th>
                  <th>Mes</th>
                  <th>Distancia (Metros)</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in resultadoConsulta" :key="index">
                  <td>{{ item.repartidor_Nombre }} {{ item.repartidor_Apellido }}</td>
                  <td>{{ item.mes_Entrega }}</td>
                  <td>{{ parseFloat(item.distancia_Total_Metros).toFixed(2) }}</td>
                </tr>
              </tbody>
            </table>
          </div>


          <div v-else-if="consultaSeleccionada === '4'">
            <table class="resultado-table">
              <thead>
                <tr>
                  <th>Farmacia</th>
                  <th>Punto de Entrega</th>
                  <th>Distancia (metros)</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in resultadoConsulta" :key="index">
                  <td>{{ item.farmaciaNombre }}</td>
                  <td>{{ item.puntoEntregaNombre }}</td>
                  <td>{{ parseFloat(item.distanciaMetros).toFixed(2) }}</td>
                  <td>
                    <button @click="abrirMapa(item)" class="map-button">
                      Ver Mapa
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- Display para la consulta 5: Pedidos con rutas que cruzan múltiples zonas -->
          <div v-else-if="consultaSeleccionada === '5' && resultadoConsulta">
            <table class="resultado-table">
              <thead>
                <tr>
                  <th>ID Pedido</th>
                  <th>Zonas Cruzadas</th>
                  <th>Cantidad de Zonas</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(pedido, index) in resultadoConsulta" :key="index">
                  <td>{{ pedido.pedidoId }}</td>
                  <td>{{ pedido.zonasCruzadas }}</td>
                  <td>{{ pedido.cantidadZonas }}</td>
                </tr>
              </tbody>
            </table>
          </div>

          <div v-else-if="consultaSeleccionada === '6'">
            <table class="resultado-table">
              <thead>
                <tr>
                  <th>ID Cliente</th>
                  <th>Nombre</th>
                  <th>Email</th>
                  <th>Acciones</th> <!-- Nueva columna para el botón de detalles -->
                </tr>
              </thead>
              <tbody>
                <tr v-for="(cliente, index) in resultadoConsulta" :key="index">
                  <td>{{ cliente.usuarioId }}</td>
                  <td>{{ cliente.nombre }}</td>
                  <td>{{ cliente.email }}</td>
                  <td>
                    <button @click="mostrarDetallesUsuario(cliente.usuarioId)" class="detail-button">
                      Detalles
                    </button>
                  </td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- Resultados para la consulta 7 -->
          <div v-else-if="consultaSeleccionada === '7'">
            <div v-if="consultaCargando" class="loading">Cargando...</div>
            <div v-else-if="errorConsulta" class="error">{{ errorConsulta }}</div>
            
            <div v-else-if="resultadoConsulta" class="resultado-simple">
              <div class="info-usuario">
                <h3>Información del Usuario</h3>
                <p><strong>Nombre:</strong> {{ resultadoConsulta.nombreCliente }}</p>
                <p><strong>Zona de cobertura:</strong> {{ resultadoConsulta.nombreZona }}</p>
              </div>
            </div>
          </div>


          <!-- Display para la Consulta 8: Zonas con alta densidad de pedidos -->
          <div v-else-if="consultaSeleccionada === '8'">
            <table class="resultado-table">
              <thead>
                <tr>
                  <th>ID Zona</th>
                  <th>Nombre Zona</th>
                  <th>Cantidad Pedidos</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(zona, index) in resultadoConsulta" :key="index">
                  <td>{{ zona.idZona }}</td>
                  <td>{{ zona.nombreZona }}</td>
                  <td>{{ zona.cantidadPedidos }}</td>
                </tr>
              </tbody>
            </table>
          </div>

          <!-- Display para la consulta 9: Puntos de interés cercanos al usuario -->
          <div v-else-if="consultaSeleccionada === '9'">
            <table class="resultado-table" v-if="resultadoConsulta && resultadoConsulta.length">
              <thead>
              <tr>
                <th>ID</th>
                <th>Nombre</th>
                <th>Lugar</th>
                <th>Latitud</th>
                <th>Longitud</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(punto, index) in resultadoConsulta" :key="index">
                <td>{{ punto.id }}</td>
                <td>{{ punto.nombre }}</td>
                <td>{{ punto.lugar }}</td>
                <td>{{ punto.latitud }}</td>
                <td>{{ punto.longitud }}</td>
              </tr>
              </tbody>
            </table>

            <p style="margin-top: 1rem;">
              Para crear tabla para un usuario, ingrese un id de usuario:
            </p>

            <input
                type="text"
                v-model="usuarioIdInput"
                placeholder="Ingrese ID de usuario"
                style="margin-right: 8px; padding: 4px 8px;"
            />
            <button @click="generarTablaPorUsuario" :disabled="consultaCargando">
              Generar
            </button>

            <div v-if="errorConsulta" style="color: red; margin-top: 0.5rem;">
              {{ errorConsulta }}
            </div>
          </div>



          <pre v-else>{{ JSON.stringify(resultadoConsulta, null, 2) }}</pre>
        </div>

        <div v-if="errorConsulta" class="error-consulta">
          <p class="error-message">{{ errorConsulta }}</p>
        </div>
      </div>
    </div>


    <div v-if="mostrarModal" class="modal-overlay" @click.self="cerrarModal">
      <div class="modal-content">
        <div class="modal-header">
          <h3>
            {{ puntoSeleccionado.farmaciaCoords?.nombre || 'Farmacia' }} →
            {{ puntoSeleccionado.puntoEntregaNombre }}
          </h3>
          <button @click="cerrarModal" class="close-button">&times;</button>
        </div>
        <div class="modal-body">
          <div v-if="consultaCargando" class="loading-indicator">
            <div class="loader"></div>
            <span>Cargando mapa...</span>
          </div>
          <div id="map-container" ref="mapContainer" v-else></div>
          <div class="map-info" v-if="puntoSeleccionado.farmaciaCoords">
            <p>
              <strong>Farmacia:</strong>
              {{ puntoSeleccionado.farmaciaCoords.nombre }} -
              {{ puntoSeleccionado.farmaciaCoords.direccion }}
            </p>
            <p>
              <strong>Punto de entrega:</strong>
              {{ puntoSeleccionado.puntoEntregaNombre }}
            </p>
            <p>
              <strong>Distancia:</strong>
              {{ parseFloat(puntoSeleccionado.distanciaMetros).toFixed(2) }} metros
            </p>
          </div>
        </div>
      </div>
    </div>

    <div v-if="mostrarModalDetalles" class="modal-overlay" @click.self="cerrarModalDetalles">
      <div class="modal-content">
        <div class="modal-header">
          <h3>Detalles del Usuario</h3>
          <button @click="cerrarModalDetalles" class="close-button">&times;</button>
        </div>
        <div class="modal-body">
          <div v-if="consultaCargando" class="loading-indicator">
            <div class="loader"></div>
            <span>Cargando detalles...</span>
          </div>
          <div v-else-if="usuarioDetalleSeleccionado">
            <p><strong>ID:</strong> {{ usuarioDetalleSeleccionado.id }}</p>
            <p><strong>RUT:</strong> {{ usuarioDetalleSeleccionado.rut }}</p>
            <p><strong>Nombre:</strong> {{ usuarioDetalleSeleccionado.nombre }}</p>
            <p><strong>Apellido:</strong> {{ usuarioDetalleSeleccionado.apellido }}</p>
            <p><strong>Email:</strong> {{ usuarioDetalleSeleccionado.email }}</p>
            <p><strong>Teléfono:</strong> {{ usuarioDetalleSeleccionado.telefono }}</p>
            <p><strong>Tipo:</strong> {{ usuarioDetalleSeleccionado.tipo }}</p>
          </div>
          <div v-else-if="errorConsulta" class="error-message">
            <p>{{ errorConsulta }}</p>
          </div>
          <div v-else>
            <p>No se pudieron cargar los detalles del usuario.</p>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import api from '@/api'
import 'leaflet/dist/leaflet.css'
import L from 'leaflet'

// Añade estas variables si no las tienes
const parametroConsulta = ref('')
const mostrarModalMapa = ref(false)
const farmaciasCount = ref(0)
const usuariosCount = ref(0)
const productosCount = ref(0)
const pedidosMesCount = ref(0)
const loading = ref(true)

const mostrarMapa = ref(false); // Controla cuando mostrar el contenedor del mapa
const mapaInicializado = ref(false);

// Variables reactivas adicionales
const mapContainer = ref(null)
const mapaListo = ref(false)

// Variables para las consultas
const consultaSeleccionada = ref('')
const consultaCargando = ref(false)
const resultadoConsulta = ref(null)
const errorConsulta = ref(null)

const mostrarModal = ref(false)
const puntoSeleccionado = ref(null)
let map = null
let farmaciaMarker = null
let puntoMarker = null
let line = null
const usuarioIdInput = ref('')


const mostrarModalDetalles = ref(false)
const usuarioDetalleSeleccionado = ref(null)


const mostrarDetallesUsuario = async (usuarioId) => {
  try {
    errorConsulta.value = null; 
    consultaCargando.value = true; 

    const response = await api.get(`/usuarios/${usuarioId}`);
    usuarioDetalleSeleccionado.value = response.data; 
    mostrarModalDetalles.value = true;

  } catch (error) {
    console.error(`Error al obtener detalles del usuario ${usuarioId}:`, error);
    errorConsulta.value = error.response?.data?.message || 'Error al cargar los detalles del usuario.';
    usuarioDetalleSeleccionado.value = null;
  } finally {
    consultaCargando.value = false; 
  }
};

const cerrarModalDetalles = () => {
  mostrarModalDetalles.value = false;
  usuarioDetalleSeleccionado.value = null; 
};


const fetchData = async () => {
  try {
    loading.value = true
    const [farmaciasRes, usuariosRes, productosRes, pedidosRes] = await Promise.all([
      api.get('/farmacias/contar'),
      api.get('/usuarios/contar'),
      api.get('/productos/contar'),
      api.get('/pedidos/contar-mes')
    ])

    farmaciasCount.value = farmaciasRes.data.count
    usuariosCount.value = usuariosRes.data.count
    productosCount.value = productosRes.data.count
    pedidosMesCount.value = pedidosRes.data.count

  } catch (error) {
    console.error('Error al cargar datos:', error)
  } finally {
    loading.value = false
  }
}

const manejarCambioConsulta = () => {
  // Limpiar resultados y errores al cambiar de consulta
  resultadoConsulta.value = null;
  errorConsulta.value = null;
  parametroConsulta.value = ''; // Limpiar el parámetro al cambiar de consulta

  // Si no es la consulta 2, ejecutar inmediatamente
  if (consultaSeleccionada.value !== '2') {
    ejecutarConsulta();
  }
};

// Función para hacer el request con el id ingresado
const generarTablaPorUsuario = async () => {
  if (!usuarioIdInput.value) {
    errorConsulta.value = 'Debe ingresar un ID de usuario válido'
    return
  }
  try {
    consultaCargando.value = true
    errorConsulta.value = null
    resultadoConsulta.value = null

    const response = await api.get(`/puntos-de-interes/cercanos-a-usuario/${usuarioIdInput.value}`)
    resultadoConsulta.value = response.data
  } catch (error) {
    console.error('Error al obtener puntos de interés cercanos a usuario:', error)
    errorConsulta.value = error.response?.data?.message || 'Error al obtener datos'
  } finally {
    consultaCargando.value = false
  }
}

const ejecutarConsulta = async () => {
  if (!consultaSeleccionada.value) return;

  try {
    consultaCargando.value = true;
    resultadoConsulta.value = null;
    errorConsulta.value = null;

    let response;

    switch (consultaSeleccionada.value) {
      case '1':
        response = await api.get('/puntos/top5-cercanos');
        break;
      case '2':
        // Esta consulta es manejada por ejecutarConsulta2, se sale de aquí y se espera el botón "Buscar"
        return;
      case '3':
        response = await api.get('/repartidores/distancia-mensual');
        break;
      case '4':
        response = await api.get('/puntos/mas-lejanos');
        break;
      case '5':
        response = await api.get('/pedidos/rutas-zonas-cruzadas');
        break;
      case '6':
        response = await api.get('/clientes/lejanos-5km');
        break;
      case '7':
        // Solo preparamos el UI, no hacemos la consulta aquí
        consultaCargando.value = false;
        return;
      case '8': 
        response = await api.get('/zonas/densidad-pedidos');
        break;
      case '9':
        response = await api.get('/puntos-de-interes');
        // Aquí hacemos el parse del geom en latitud y longitud
        resultadoConsulta.value = response.data.map(punto => {
          const match = punto.geom.match(/POINT\((-?\d+\.?\d*) (-?\d+\.?\d*)\)/);
          return {
            id: punto.id,
            nombre: punto.nombre,
            lugar: punto.lugar,
            longitud: parseFloat(match[1]),
            latitud: parseFloat(match[2])
          };
        });
        consultaCargando.value = false;
        return; // para evitar asignar resultadoConsulta.value abajo otra vez
      default:
        response = await api.get(`/consultas/consulta-${consultaSeleccionada.value}`);
    }

    resultadoConsulta.value = response.data;

  } catch (error) {
    console.error('Error al ejecutar consulta:', error);
    errorConsulta.value = error.response?.data?.message || 'Error al ejecutar la consulta';
  } finally {
    consultaCargando.value = false;
  }
};

const ejecutarConsulta2 = async () => {
  if (!parametroConsulta.value) {
    errorConsulta.value = 'Debe ingresar el ID del cliente';
    return;
  }

  try {
    consultaCargando.value = true;
    resultadoConsulta.value = null;
    errorConsulta.value = null;

    const response = await api.get(`/usuarios/${parametroConsulta.value}/zonas`);
    resultadoConsulta.value = response.data;

  } catch (error) {
    console.error('Error al ejecutar consulta 2:', error);
    errorConsulta.value = error.response?.data?.message || 'Error al buscar zonas del cliente';
  } finally {
    consultaCargando.value = false;
  }
};

const ejecutarConsulta7 = async () => {
  if (!parametroConsulta.value) {
    errorConsulta.value = 'Debe ingresar el ID del usuario';
    return;
  }

  try {
    consultaCargando.value = true;
    resultadoConsulta.value = null;
    errorConsulta.value = null;

    const response = await api.get(`/usuarios/ver-zona/${parametroConsulta.value}`);
    resultadoConsulta.value = response.data;

  } catch (error) {
    console.error('Error al ejecutar consulta 7:', error);
    errorConsulta.value = error.response?.data?.message || 'Error al buscar zona del usuario';
  } finally {
    consultaCargando.value = false;
  }
};

// Función para extraer coordenadas legibles
const extraerCoordenadas = (ubicacionJSON) => {
  try {
    const ubicacion = JSON.parse(ubicacionJSON);
    if (ubicacion?.coordinates) {
      return `Latitud: ${ubicacion.coordinates[1]}, Longitud: ${ubicacion.coordinates[0]}`;
    }
    return 'Coordenadas no disponibles';
  } catch {
    return 'Formato de coordenadas inválido';
  }
};

const inicializarMapaConsulta7 = async () => {
  try {
    // Verificar usando la referencia
    if (!mapContainer.value) {
      throw new Error('El elemento del mapa no está disponible en el DOM')
    }

    // Limpiar mapa existente
    if (map) {
      map.remove()
      map = null
    }

    // Parsear datos
    const ubicacion = JSON.parse(resultadoConsulta.value.ubicacionCliente)
    const poligono = JSON.parse(resultadoConsulta.value.poligonoZona)

    // Validar coordenadas
    if (!ubicacion?.coordinates || !poligono?.coordinates) {
      throw new Error('Formato de coordenadas inválido')
    }

    // Invertir coordenadas para Leaflet [lat, lng]
    const centro = [ubicacion.coordinates[1], ubicacion.coordinates[0]]
    const coordsPoligono = poligono.coordinates[0].map(c => [c[1], c[0]])

    // Crear mapa
    map = L.map(mapContainer.value).setView(centro, 14)

    // Capa base
    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap'
    }).addTo(map)

    // Marcador
    L.marker(centro)
      .addTo(map)
      .bindPopup(resultadoConsulta.value.nombreCliente)

    // Polígono
    L.polygon(coordsPoligono, {
      color: 'blue',
      fillColor: '#3388ff',
      fillOpacity: 0.2,
      weight: 2
    }).addTo(map)

    // Ajustar vista
    map.fitBounds([...coordsPoligono, centro])

    return true
  } catch (error) {
    console.error('Error al inicializar mapa:', error)
    throw error // Re-lanzar el error para manejo superior
  }
}

// Método para abrir el mapa
const abrirMapa = async (punto) => {
  try {
    consultaCargando.value = true;
    puntoSeleccionado.value = punto;

    // Obtener coordenadas de la farmacia
    const response = await api.get(`/farmacias/coordenadas/${punto.farmaciaId}`);
    const farmaciaCoords = {
      nombre: response.data.nombre,
      direccion: response.data.lugar,
      latitud: parseFloat(response.data.latitud),
      longitud: parseFloat(response.data.longitud)
    };

    puntoSeleccionado.value.farmaciaCoords = farmaciaCoords;
    mostrarModal.value = true;

    nextTick(() => {
      initMap();
    });
  } catch (error) {
    console.error('Error al obtener coordenadas de la farmacia:', error);
    errorConsulta.value = 'No se pudieron cargar las coordenadas de la farmacia';
  } finally {
    consultaCargando.value = false;
  }
};

// Método para inicializar el mapa
const initMap = () => {
  if (map) {
    map.remove();
    map = null;
  }

  // Coordenadas centrales (promedio entre farmacia y punto)
  const centerLat = (puntoSeleccionado.value.latitud + puntoSeleccionado.value.farmaciaCoords.latitud) / 2;
  const centerLng = (puntoSeleccionado.value.longitud + puntoSeleccionado.value.farmaciaCoords.longitud) / 2;

  // Crear mapa
  map = L.map('map-container').setView([centerLat, centerLng], 13);

  // Añadir capa de tiles
  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
  }).addTo(map);

  // Iconos personalizados
  const farmaciaIcon = L.icon({
    iconUrl: 'https://cdn-icons-png.flaticon.com/512/4474/4474034.png',
    iconSize: [32, 32],
    iconAnchor: [16, 32]
  });

  const puntoIcon = L.icon({
    iconUrl: 'https://cdn-icons-png.flaticon.com/512/4474/4474051.png',
    iconSize: [32, 32],
    iconAnchor: [16, 32]
  });

  // Añadir marcador de la farmacia
  farmaciaMarker = L.marker(
    [puntoSeleccionado.value.farmaciaCoords.latitud, puntoSeleccionado.value.farmaciaCoords.longitud],
    { icon: farmaciaIcon }
  ).addTo(map)
    .bindPopup(`
      <b>${puntoSeleccionado.value.farmaciaCoords.nombre}</b><br>
      ${puntoSeleccionado.value.farmaciaCoords.direccion}
    `);

  // Añadir marcador del punto de entrega
  puntoMarker = L.marker(
    [puntoSeleccionado.value.latitud, puntoSeleccionado.value.longitud],
    { icon: puntoIcon }
  ).addTo(map)
    .bindPopup(`
      <b>${puntoSeleccionado.value.puntoEntregaNombre}</b><br>
      Distancia: ${parseFloat(puntoSeleccionado.value.distanciaMetros).toFixed(2)} metros
    `);

  // Añadir línea entre los puntos
  line = L.polyline([
    [puntoSeleccionado.value.farmaciaCoords.latitud, puntoSeleccionado.value.farmaciaCoords.longitud],
    [puntoSeleccionado.value.latitud, puntoSeleccionado.value.longitud]
  ], {
    color: '#1a237e',
    weight: 3,
    dashArray: '5, 5',
    opacity: 0.7
  }).addTo(map);

  // Añadir información de distancia
  const distanceInfo = L.control({ position: 'bottomright' });
  distanceInfo.onAdd = () => {
    const div = L.DomUtil.create('div', 'distance-info');
    div.innerHTML = `
      <strong>Distancia:</strong> ${parseFloat(puntoSeleccionado.value.distanciaMetros).toFixed(2)} metros<br>
      <small>Desde ${puntoSeleccionado.value.farmaciaCoords.nombre}</small>
    `;
    return div;
  };
  distanceInfo.addTo(map);

  // Ajustar el zoom para que ambos marcadores sean visibles
  map.fitBounds([
    [puntoSeleccionado.value.farmaciaCoords.latitud, puntoSeleccionado.value.farmaciaCoords.longitud],
    [puntoSeleccionado.value.latitud, puntoSeleccionado.value.longitud]
  ], { padding: [50, 50] });
};

// Método para cerrar el modal
const cerrarModal = () => {
  mostrarModal.value = false
  if (map) {
    map.remove()
    map = null
  }
}

const agruparPorFarmacia = (data) => {
  const grupos = {};

  data.forEach(item => {
    if (!grupos[item.farmaciaId]) {
      grupos[item.farmaciaId] = {
        farmaciaNombre: item.farmaciaNombre,
        puntos: []
      };
    }

    // Solo agregar si no hemos alcanzado el límite de 5 por farmacia
    if (grupos[item.farmaciaId].puntos.length < 5) {
      grupos[item.farmaciaId].puntos.push({
        ...item,
        latitud: parseFloat(item.latitud),
        longitud: parseFloat(item.longitud)
      });
    }
  });

  return grupos;
};

onMounted(() => {
  fetchData()
})
</script>

<style scoped>
.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 1.5rem;
  margin-top: 2rem;
}

.stat-card {
  background: white;
  padding: 1.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  text-align: center;
  transition: transform 0.3s;
}

.clickable-card {
  cursor: pointer;
  text-decoration: none;
  color: inherit;
}

.clickable-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
  background-color: #f8f9fa;
}

.stat-card h3 {
  color: #555;
  margin-bottom: 0.5rem;
  font-size: 1.2rem;
}

.stat-card p {
  font-size: 2.5rem;
  font-weight: bold;
  color: #1a237e;
  margin: 1rem 0;
}

/* Estilos para la sección de consultas */
.consultas-section {
  margin-top: 3rem;
  padding: 1.5rem;
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.consultas-section h2 {
  color: #1a237e;
  margin-bottom: 1.5rem;
}

.consulta-dropdown {
  width: 100%;
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  margin-bottom: 1rem;
}

.resultado-consulta {
  margin-top: 1.5rem;
  padding: 1.5rem;
  background-color: #f5f5f5;
  border-radius: 8px;
  max-height: 80vh;
  /* Aumentamos la altura máxima */
  min-height: 300px;
  /* Altura mínima para que no se vea muy pequeño */
  overflow-y: auto;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  border: 1px solid #e0e0e0;
}

.resultado-consulta h3 {
  color: #1a237e;
  margin-bottom: 0.5rem;
}

.resultado-consulta pre {
  white-space: pre-wrap;
  word-wrap: break-word;
  font-family: monospace;
}

/* Estilos específicos para la tabla de resultados de consulta */
/* Estilos consistentes para todas las tablas de resultados */
.resultado-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.95rem;
  margin-top: 1rem;
}

.resultado-table th {
  background-color: #f8f9fa;
  padding: 0.85rem 1rem;
  text-align: left;
  font-weight: 600;
  color: #555;
  position: sticky;
  top: 0;
  z-index: 10;
}

.resultado-table td {
  padding: 0.75rem 1rem;
  border-bottom: 1px solid #eee;
}

/* Estilo para el JSON de resultados */
.resultado-consulta pre {
  white-space: pre-wrap;
  word-wrap: break-word;
  font-family: 'Courier New', Courier, monospace;
  font-size: 0.9rem;
  line-height: 1.5;
  padding: 1rem;
  background-color: white;
  border-radius: 6px;
  border: 1px solid #e0e0e0;
  max-height: 60vh;
  overflow-y: auto;
}

.error-consulta {
  margin-top: 1rem;
  padding: 1rem;
  background-color: #ffebee;
  border-radius: 4px;
  color: #c62828;
}

.error-message {
  margin: 0;
}

.loading-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.5rem;
  margin: 1rem 0;
}

.loader {
  border: 3px solid #f3f3f3;
  border-top: 3px solid #1a237e;
  border-radius: 50%;
  width: 20px;
  height: 20px;
  animation: spin 1s linear infinite;
}


/* Estilos para el modal */
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.modal-content {
  background-color: white;
  border-radius: 8px;
  width: 85%;
  max-width: 1000px;
  max-height: 85vh;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

.modal-header {
  padding: 1rem;
  background-color: #1a237e;
  color: white;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.modal-header h3 {
  margin: 0;
}

.close-button {
  background: none;
  border: none;
  color: white;
  font-size: 1.5rem;
  cursor: pointer;
}

.modal-body {
  padding: 1.5rem;
  flex-grow: 1;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

#map-container {
  height: 550px;
  /* Más alto */
  width: 100%;
  border-radius: 6px;
  border: 1px solid #ddd;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  background: #f8f9fa;
}

#map-container {
  height: 550px;
  width: 100%;
  border-radius: 6px;
  border: 1px solid #ddd;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  background: #f8f9fa;
}

.distance-info {
  background: white;
  padding: 0.75rem;
  border-radius: 4px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
  font-size: 0.9rem;
  line-height: 1.4;
}

.distance-info strong {
  color: #1a237e;
}

.map-info {
  padding: 1rem;
  background-color: #f5f5f5;
  border-radius: 6px;
  margin-top: 1rem;
  font-size: 0.95rem;
}

.map-info p {
  margin: 0.5rem 0;
}

.map-info strong {
  color: #1a237e;
  font-weight: 600;
}

/* Estilos para los grupos de farmacia */
.farmacia-group {
  margin-bottom: 2rem;
  border: 1px solid #eee;
  border-radius: 8px;
  padding: 1rem;
  background-color: #f9f9f9;
}

.farmacia-group h4 {
  margin-top: 0;
  color: #1a237e;
  padding-bottom: 0.5rem;
  border-bottom: 1px solid #ddd;
}

/* Ajustes para las tablas dentro de los grupos */
.farmacia-group .resultado-table {
  margin-top: 0.5rem;
  margin-bottom: 0;
}

.farmacia-group .resultado-table thead th {
  background-color: #1a237e;
}

/* Estilos expandidos para el resultado */
.expanded-result {
  margin-top: 1.5rem;
  padding: 1.5rem;
  background-color: #f5f5f5;
  border-radius: 8px;
  max-height: 70vh;
  overflow-y: auto;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  border: 1px solid #e0e0e0;
}


.consulta-1-container {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
  max-height: 65vh;
  overflow-y: auto;
  padding-right: 0.5rem;
  /* Espacio para el scroll */
}

.farmacia-group {
  background: white;
  border-radius: 8px;
  padding: 1.5rem;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  margin-bottom: 1.5rem;
  border: 1px solid #eaeaea;
}

.farmacia-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1rem;
  padding-bottom: 0.75rem;
  border-bottom: 1px solid #eee;
}

.farmacia-header h4 {
  margin: 0;
  color: #1a237e;
  font-size: 1.2rem;
  font-weight: 600;
}

.badge {
  background-color: #1a237e;
  color: white;
  padding: 0.35rem 1rem;
  border-radius: 16px;
  font-size: 0.9rem;
  font-weight: 500;
}

.resultado-table {
  width: 100%;
  border-collapse: collapse;
  font-size: 0.95rem;
}

.resultado-table th {
  background-color: #1a237e;
  padding: 0.85rem 1rem;
  text-align: left;
  font-weight: 600;
  color: #fff;
  position: sticky;
  top: 0;
  z-index: 10;
}

.resultado-table td {
  padding: 0.75rem 1rem;
  border-bottom: 1px solid #eee;
  vertical-align: middle;
}

.resultado-table tr:last-child td {
  border-bottom: none;
}

.resultado-table tr:hover {
  background-color: #dbd8d8;
}

/* Mejoras para el botón del mapa */
.map-button {
  background-color: #1a237e;
  color: white;
  border: none;
  padding: 0.6rem 1rem;
  border-radius: 6px;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  gap: 0.5rem;
  font-size: 0.9rem;
  transition: all 0.2s;
  white-space: nowrap;
}

.map-button:hover {
  background-color: #303f9f;
  transform: translateY(-2px);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.map-button i {
  font-size: 0.9em;
}

/* Barra de scroll personalizada */
.resultado-consulta::-webkit-scrollbar,
.consulta-1-container::-webkit-scrollbar {
  width: 8px;
  height: 8px;
}

.resultado-consulta::-webkit-scrollbar-track,
.consulta-1-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.resultado-consulta::-webkit-scrollbar-thumb,
.consulta-1-container::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}

.resultado-consulta::-webkit-scrollbar-thumb:hover,
.consulta-1-container::-webkit-scrollbar-thumb:hover {
  background: #a8a8a8;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}

.resultado-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.resultado-table th,
.resultado-table td {
  border: 1px solid #7c7c7c;
  padding: 8px;
  text-align: left;
}

.resultado-table th {
  background-color: #202020;
}

.resultado-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.loading {
  padding: 20px;
  text-align: center;
}

.error {
  color: red;
  padding: 20px;
  text-align: center;
}

.input-parametro {
  margin: 1.5rem 0;
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
  max-width: 500px;
}

.input-parametro label {
  font-weight: 600;
  color: #555;
  margin-bottom: 0.25rem;
}

.input-cliente-id {
  padding: 0.75rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 1rem;
  width: 100%;
  margin-bottom: 0.5rem;
}

.input-cliente-id:focus {
  outline: none;
  border-color: #1a237e;
  box-shadow: 0 0 0 2px rgba(26, 35, 126, 0.2);
}

/* Estilo para el botón de búsqueda */
.input-parametro button {
  align-self: flex-start;
  padding: 0.75rem 1.5rem;
  background-color: #1a237e;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 1rem;
  transition: all 0.2s;
}

.input-parametro button:hover {
  background-color: #303f9f;
}

.input-parametro button:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.input-parametro button i {
  margin-right: 0.5rem;
}


.detail-button {
  background-color: #2196F3;
  color: white;
  padding: 8px 12px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.9em;
  transition: background-color 0.3s ease;
  white-space: nowrap;
}

.detail-button:hover {
  background-color: #1976D2;
}

/* En tu sección de estilos */
.input-parametro {
  margin: 20px 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.input-cliente-id {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.btn-buscar {
  padding: 8px 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-buscar:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.btn-buscar:hover:not(:disabled) {
  background-color: #45a049;
}

.info-usuario {
  background-color: #f8f9fa;
  padding: 15px;
  border-radius: 5px;
  margin-bottom: 20px;
}

.datos-geograficos {
  background-color: #f0f0f0;
  padding: 15px;
  border-radius: 5px;
  font-family: monospace;
  font-size: 13px;
  overflow-x: auto;
}

.loading {
  padding: 20px;
  text-align: center;
}

.error {
  color: red;
  padding: 20px;
  text-align: center;
}

#mapaConsulta7 {
  height: 400px !important;
  width: 100% !important;
  min-height: 400px;
  background-color: #f0f0f0;
}

.leaflet-container {
  height: 100%;
  width: 100%;
}

.input-parametro {
  margin: 20px 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.input-cliente-id {
  padding: 8px 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
  font-size: 14px;
}

.btn-buscar {
  padding: 8px 16px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 14px;
}

.btn-buscar:disabled {
  background-color: #cccccc;
  cursor: not-allowed;
}

.btn-buscar:hover:not(:disabled) {
  background-color: #45a049;
}

.info-usuario {
  background-color: #f8f9fa;
  padding: 15px;
  border-radius: 5px;
  margin-bottom: 20px;
}

.datos-geograficos {
  background-color: #f0f0f0;
  padding: 15px;
  border-radius: 5px;
  font-family: monospace;
  font-size: 13px;
  overflow-x: auto;
}

.loading {
  padding: 20px;
  text-align: center;
}

.error {
  color: red;
  padding: 20px;
  text-align: center;
}






.resultado-simple {
  background-color: #f8f9fa;
  padding: 20px;
  border-radius: 8px;
  margin-top: 20px;
}

.info-usuario {
  margin-bottom: 20px;
}

.info-usuario h3 {
  color: #2c3e50;
  border-bottom: 1px solid #eee;
  padding-bottom: 10px;
}

.info-usuario p {
  margin: 8px 0;
}

.datos-geograficos {
  background-color: #f0f0f0;
  padding: 15px;
  border-radius: 5px;
}

.datos-geograficos h4 {
  margin-top: 0;
  color: #2c3e50;
}

.loading {
  padding: 20px;
  text-align: center;
  color: #666;
}

.error {
  padding: 20px;
  text-align: center;
  color: #e74c3c;
  background-color: #fde8e8;
  border-radius: 5px;
}
</style>
