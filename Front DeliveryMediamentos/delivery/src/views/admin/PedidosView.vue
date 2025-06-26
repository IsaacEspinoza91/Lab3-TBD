<template>
  <div class="pedidos-container">
    <h1>Gestión de Pedidos</h1>

    <div v-if="loadingPedidos" class="loading-overlay">
      <div class="spinner"></div>
      <p>Cargando pedidos...</p>
    </div>

    <button @click="showModal = true" class="add-button" v-if="!loadingPedidos">
      <i class="fas fa-plus"></i> Agregar Pedido
    </button>
    <div class="table-responsive" v-if="!loadingPedidos">

      <div class="table-responsive">
        <table class="pedidos-table">
          <thead>
            <tr>
              <th>ID</th>
              <th>Fecha</th>
              <th>Urgencia</th>
              <th>Total Pagado</th>
              <th>Estado de Entrega</th>
              <th>Fecha de Entrega</th>
              <th>Cliente</th>
              <th>Medio de Pago</th>
              <th>Farmacia</th>
              <th>Repartidor</th>
              <th>Alerta</th>
              <th>Ruta</th>
              <th>Acciones</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="pedido in pedidos" :key="pedido.id"
              :class="{ 'pedido-con-alerta': pedidosConAlerta.includes(pedido.id) }">
              <td>{{ pedido.id }}</td>
              <td>{{ pedido.fecha }}</td>
              <td>{{ pedido.urgencia ? 'Sí' : 'No' }}</td>
              <td>{{ pedido.total_pagado }}</td>
              <td>{{ pedido.estado_entrega }}</td>
              <td>{{ pedido.fecha_entrega }}</td>
              <td>{{ pedido.cliente_id }}</td>
              <td>{{ pedido.medio_pago_id }}</td>
              <td>{{ pedido.farmacia_id }}</td>
              <td>{{ pedido.repartidor_id }}</td>
              <td>
                <span v-if="pedidosConAlerta.includes(pedido.id)" class="alerta-text">Si</span>
              </td>
              <td>
                <button @click="viewPedidoRoute(pedido.id)" class="view-route-button">
                  Ver Ruta
                </button>
              </td>
              <td class="actions">
                <button @click="editPedido(pedido)" class="edit-button">
                  <i class="fas fa-edit">Modificar</i>
                </button>
                <button @click="confirmDelete(pedido.id)" class="delete-button">
                  <i class="fas fa-trash">Eliminar</i>
                </button>
                <button v-if="pedido.estado_entrega.toLowerCase() !== 'entregado'" @click="marcarEntregado(pedido.id)"
                  class="save-button">
                  Marcar Entregado
                </button>
                <span v-else>Entregado</span>
                <button v-if="pedido.estado_entrega.toLowerCase() === 'entregado' && !pedido.confirmado"
                  @click="confirmarPedido(pedido.id)" class="confirm-button">
                  <i class="fas fa-check">Confirmar</i>
                </button>
                <span v-else-if="pedido.confirmado" class="confirmed-text">Confirmado</span>
              </td>
            </tr>
          </tbody>
        </table>
      </div>

      <!-- Modal para agregar/editar pedido -->
      <div v-if="showModal" class="modal-overlay">
        <div class="modal-content">
          <h3>{{ isEditing ? 'Editar Pedido' : 'Nuevo Pedido' }}</h3>
          <div class="modal-scroll-container">
            <form @submit.prevent="isEditing ? updatePedido() : createPedido()">
              <div class="form-group">
                <label>Fecha:</label>
                <input v-model="form.fecha" type="date" required />
              </div>
              <div class="form-group">
                <label>Urgencia:</label>
                <input v-model="form.urgencia" type="checkbox" />
              </div>
              <div class="form-group">
                <label>Total Pagado:</label>
                <input v-model="form.total_pagado" type="number" min="0" step="0.01" required />
              </div>
              <div class="form-group">
                <label>Estado de Entrega:</label>
                <select v-model="form.estado_entrega" required>
                  <option value="">Seleccione estado</option>
                  <option value="Pendiente">Pendiente</option>
                  <option value="Demorado">Demorado</option>
                  <option value="Entregado">Entregado</option>
                </select>
              </div>
              <div class="form-group">
                <label>Fecha de Entrega:</label>
                <input v-model="form.fecha_entrega" type="date" required />
              </div>
              <div class="form-group">
                <label>Cliente ID:</label>
                <input v-model="form.cliente_id" type="number" min="1" required />
              </div>
              <div class="form-group">
                <label>Medio de Pago ID:</label>
                <input v-model="form.medio_pago_id" type="number" min="1" required />
              </div>
              <div class="form-group">
                <label>Farmacia ID:</label>
                <input v-model="form.farmacia_id" type="number" min="1" required />
              </div>
              <div class="form-group">
                <label>Repartidor ID:</label>
                <input v-model="form.repartidor_id" type="number" min="1" required />
              </div>
              <div class="modal-actions">
                <button type="button" @click="closeModal" class="cancel-button">Cancelar</button>
                <button type="submit" class="save-button">
                  {{ isEditing ? 'Actualizar' : 'Guardar' }}
                </button>
              </div>
            </form>
          </div>
        </div>
      </div>

      <!-- Modal de confirmación de eliminación -->
      <div v-if="showDeleteModal" class="modal-overlay">
        <div class="modal-content delete-modal">
          <h3>¿Eliminar pedido?</h3>
          <p>Esta acción no se puede deshacer.</p>
          <div class="modal-actions">
            <button @click="showDeleteModal = false" class="cancel-button">Cancelar</button>
            <button @click="deletePedido" class="delete-button">Eliminar</button>
          </div>
        </div>
      </div>

      <!-- Modal para mostrar la ruta -->
      <div v-show="showRutaModal" class="modal-overlay" @click.self="closeRutaModal">
        <div class="modal-content map-modal-content">
          <div class="modal-header">
            <h3>Ruta del Pedido #{{ currentPedidoIdForRoute }}</h3>
            <button @click="closeRutaModal" class="close-button">&times;</button>
          </div>
          <div class="modal-body">
            <div v-if="loadingRuta" class="loading-indicator">
              <div class="loader"></div>
              <span>Cargando ruta...</span>
            </div>
            <div id="mapRutaContainer" ref="mapRutaContainer" style="height: 400px; width: 100%;" v-else></div>
            <p v-if="!currentRutaGeoJson && !loadingRuta" class="no-route-message">
              No hay ruta estimada disponible para este pedido.
            </p>
          </div>
        </div>
      </div>

      <hr v-if="!loadingPedidos" style="margin: 30px 0;">
      <hr style="margin: 30px 0" />

      <div class="medio-pago-urgente-section" v-if="!loadingPedidos">
        <h2>Medio de Pago Más Usado en Pedidos Urgentes</h2>
        <div v-if="loadingMedioPagoUrgente" class="loading-overlay-inline">
          <div class="spinner-small"></div>
          <p>Cargando datos...</p>
        </div>
        <div v-if="medioPagoUrgenteData" class="table-responsive">
          <table class="medio-pago-urgente-table">
            <thead>
              <tr>
                <th>Medio de Pago</th>
                <th>Cantidad de Usos</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ medioPagoUrgenteData.medio_pago }}</td>
                <td>{{ medioPagoUrgenteData.cantidad_usos }}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <p v-if="medioPagoUrgenteError" class="error-message">
          Error al cargar el medio de pago más usado.
        </p>
        <p v-if="
            !loadingMedioPagoUrgente &&
            !medioPagoUrgenteData &&
            !medioPagoUrgenteError
          ">
          No hay datos disponibles sobre el medio de pago más usado en urgencias.
        </p>
      </div>
      <hr v-if="!loadingPedidos" style="margin: 30px 0;">
      <hr style="margin: 30px 0" />

      <div class="cliente-mayor-gasto-section" v-if="!loadingPedidos">
        <h2>Cliente con Mayor Gasto en Pedidos Entregados</h2>
        <div v-if="loadingClienteMayorGasto" class="loading-overlay-inline">
          <div class="spinner-small"></div>
          <p>Cargando datos...</p>
        </div>
        <div v-if="clienteMayorGastoData" class="table-responsive">
          <table class="cliente-mayor-gasto-table">
            <thead>
              <tr>
                <th>ID Cliente</th>
                <th>Nombre</th>
                <th>Apellido</th>
                <th>Total Gastado</th>
              </tr>
            </thead>
            <tbody>
              <tr>
                <td>{{ clienteMayorGastoData.cliente_id }}</td>
                <td>{{ clienteMayorGastoData.nombre }}</td>
                <td>{{ clienteMayorGastoData.apellido }}</td>
                <td>{{ clienteMayorGastoData.totalgastado }}</td>
              </tr>
            </tbody>
          </table>
        </div>
        <p v-if="clienteMayorGastoError" class="error-message">
          Error al cargar el cliente con mayor gasto.
        </p>
        <p v-if="
            !loadingClienteMayorGasto &&
            !clienteMayorGastoData &&
            !clienteMayorGastoError
          ">
          No hay datos disponibles sobre el cliente con mayor gasto.
        </p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useAuthStore } from '@/stores/auth' // Asegúrate de que esta importación sea correcta si la usas
import axios from 'axios' // Axios ya está importado por '@/api' si lo usas directamente
import api from '@/api' // Tu instancia de Axios configurada

import 'leaflet/dist/leaflet.css'
import L from 'leaflet'

// Corrección para el icono de marcador por defecto de Leaflet
if (L.Icon.Default.prototype._get) {
  delete L.Icon.Default.prototype._get;
}
L.Icon.Default.mergeOptions({
  iconRetinaUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon-2x.png',
  iconUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-icon.png',
  shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/1.7.1/images/marker-shadow.png',
});


const authStore = useAuthStore() // Si usas este store

const pedidos = ref([])
const loadingPedidos = ref(true)
const showModal = ref(false)
const showDeleteModal = ref(false)
const isEditing = ref(false)
const currentPedidoId = ref(null)

const form = ref({
  fecha: '',
  urgencia: false,
  total_pagado: 0,
  estado_entrega: '',
  fecha_entrega: '',
  cliente_id: '',
  medio_pago_id: '',
  farmacia_id: '',
  repartidor_id: ''
})

const medioPagoUrgenteData = ref(null)
const loadingMedioPagoUrgente = ref(true)
const medioPagoUrgenteError = ref(null)

const clienteMayorGastoData = ref(null)
const loadingClienteMayorGasto = ref(true)
const clienteMayorGastoError = ref(null)

const pedidosConAlerta = ref([])
const loadingAlertas = ref(false)
const errorAlertas = ref(null)

// Variables para el modal de ruta
const showRutaModal = ref(false);
const currentRutaGeoJson = ref(null);
const loadingRuta = ref(false);
const currentPedidoIdForRoute = ref(null);
let mapInstance = null;
let geoJsonLayer = null; 


const fetchPedidosConAlerta = async () => {
  loadingAlertas.value = true
  pedidosConAlerta.value = []
  errorAlertas.value = null
  try {
    const response = await api.get('/pedidos/pedidos-con-alerta')
    pedidosConAlerta.value = response.data
    console.log('Pedidos con alerta:', pedidosConAlerta.value)
  } catch (error) {
    console.error('Error al obtener pedidos con alerta:', error)
    errorAlertas.value = 'Error al cargar las alertas de pedidos.'
  } finally {
    loadingAlertas.value = false
  }
}

const fetchMedioPagoUrgente = async () => {
  loadingMedioPagoUrgente.value = true
  medioPagoUrgenteData.value = null
  medioPagoUrgenteError.value = null
  try {
    const response = await api.get('/pedidos/medio-pago-urgente')
    medioPagoUrgenteData.value = response.data
  }  catch (error) {
    console.error('Error al obtener el medio de pago más usado:', error)
    medioPagoUrgenteError.value = 'Hubo un error al cargar los datos.'
  } finally {
    loadingMedioPagoUrgente.value = false
  }
}

const fetchClienteMayorGasto = async () => {
  loadingClienteMayorGasto.value = true
  clienteMayorGastoData.value = null
  clienteMayorGastoError.value = null
  try {
    const response = await api.get('/pedidos/cliente-mas-gastador')
    clienteMayorGastoData.value = response.data
  } catch (error) {
    console.error('Error al obtener el cliente con mayor gasto:', error)
    clienteMayorGastoError.value =
      'Hubo un error al cargar los datos del cliente con mayor gasto.'
  } finally {
    loadingClienteMayorGasto.value = false
  }
}

// Función para marcar un pedido como entregado
const marcarEntregado = async (pedidoId) => {
  console.log('Intentando marcar como entregado el pedido con ID:', pedidoId);
  try {
    const response = await api.put(`/pedidos/actualizarEntrega/${pedidoId}`, {
      estado_entrega: 'entregado',
    });
    if (response.status === 200) {
      alert(`Pedido ${pedidoId} marcado como entregado.`);
      await fetchPedidos();
    } else {
      alert(`Error al marcar el pedido ${pedidoId} como entregado.`);
      console.error('Error al marcar como entregado:', response);
    }
  } catch (error) {
    alert(`Error al marcar el pedido ${pedidoId} como entregado.`);
    console.error('Error al marcar como entregado:', error);
  }
}

// Función para confirmar un pedido
const confirmarPedido = async (pedidoId) => {
  console.log('Intentando confirmar pedido con ID:', pedidoId);
  try {
    await api.post(`/pedidos/confirmar/${pedidoId}`);
    alert(`Pedido ${pedidoId} confirmado.`);
    const index = pedidos.value.findIndex(p => p.id === pedidoId);
    if (index !== -1) {
      pedidos.value[index] = { ...pedidos.value[index], confirmado: true };
    }
  } catch (error) {
    alert(`Error al confirmar el pedido ${pedidoId}.`);
    console.error('Error al confirmar pedido:', error);
  }
}

// Obtener pedidos
const fetchPedidos = async () => {
  try {
    const response = await api.get('/pedidos')
    pedidos.value = response.data
  } catch (error) {
    console.error('Error al obtener pedidos:', error)
  } finally {
    loadingPedidos.value = false
  }
}

// Crear pedido
const createPedido = async () => {
  try {
    // Preparar los datos para enviar
    const pedidoData = {
      fecha: form.value.fecha,
      urgencia: Boolean(form.value.urgencia),
      total_pagado: Number(form.value.total_pagado),
      estado_entrega: form.value.estado_entrega,
      fecha_entrega: form.value.fecha_entrega,
      cliente_id: Number(form.value.cliente_id),
      medio_pago_id: Number(form.value.medio_pago_id),
      farmacia_id: Number(form.value.farmacia_id),
      repartidor_id: Number(form.value.repartidor_id)
    }

    const response = await api.post('/pedidos', pedidoData)
    
    if (response.status === 201) {
      await fetchPedidos()
      closeModal()
      alert('Pedido creado exitosamente')
    } else {
      throw new Error('Error al crear pedido')
    }
  } catch (error) {
    console.error('Error al crear pedido:', error)
    alert(`Error al crear pedido: ${error.message}`)
  }
}

// Editar pedido
const editPedido = (pedido) => {
  form.value = {
    id: pedido.id, // Añadir el ID para la edición
    fecha: pedido.fecha,
    urgencia: pedido.urgencia,
    total_pagado: pedido.total_pagado,
    estado_entrega: pedido.estado_entrega,
    fecha_entrega: pedido.fecha_entrega,
    cliente_id: pedido.cliente_id,
    medio_pago_id: pedido.medio_pago_id,
    farmacia_id: pedido.farmacia_id,
    repartidor_id: pedido.repartidor_id,
    rutaEstimada: pedido.rutaEstimada,
  }
  currentPedidoId.value = pedido.id
  isEditing.value = true
  showModal.value = true
}

// Actualizar pedido
const updatePedido = async () => {
  try {
    const pedidoData = {
      fecha: form.value.fecha,
      urgencia: Boolean(form.value.urgencia),
      total_pagado: Number(form.value.total_pagado),
      estado_entrega: form.value.estado_entrega,
      fecha_entrega: form.value.fecha_entrega,
      cliente_id: Number(form.value.cliente_id),
      medio_pago_id: Number(form.value.medio_pago_id),
      farmacia_id: Number(form.value.farmacia_id),
      repartidor_id: Number(form.value.repartidor_id)
    }

    const response = await api.put(`/pedidos/${currentPedidoId.value}`, pedidoData)
    
    if (response.status === 200) {
      await fetchPedidos()
      closeModal()
      alert('Pedido actualizado exitosamente')
    } else {
      throw new Error('Error al actualizar pedido')
    }
  } catch (error) {
    console.error('Error al actualizar pedido:', error)
    alert(`Error al actualizar pedido: ${error.message}`)
  }
}

// Confirmar eliminación
const confirmDelete = (id) => {
  currentPedidoId.value = id
  showDeleteModal.value = true
}

// Eliminar pedido
const deletePedido = async () => {
  try {
    await api.delete(`/pedidos/${currentPedidoId.value}`)
    await fetchPedidos()
    showDeleteModal.value = false
    alert('Pedido eliminado exitosamente')
  } catch (error) {
    console.error('Error al eliminar pedido:', error)
    alert('Error al eliminar pedido')
  }
}

// Cerrar modal de creación/edición
const closeModal = () => {
  showModal.value = false
  isEditing.value = false
  form.value = {
    fecha: '',
    urgencia: false,
    total_pagado: '',
    estado_entrega: '',
    fecha_entrega: '',
    cliente_id: '',
    medio_pago_id: '',
    farmacia_id: '',
    repartidor_id: '',
    rutaEstimada: null,
  }
  currentPedidoId.value = null
}

// Función para abrir el modal de ruta
const viewPedidoRoute = async (pedidoId) => {
  currentPedidoIdForRoute.value = pedidoId;
  currentRutaGeoJson.value = null;
  loadingRuta.value = true;
  showRutaModal.value = true;

  await nextTick();

  try {
    const response = await api.get(`/pedidos/${pedidoId}/ruta/multilinestring`);
    currentRutaGeoJson.value = response.data; 
    console.log('Ruta MultiLineString recibida:', currentRutaGeoJson.value);

    if (currentRutaGeoJson.value) {
      setTimeout(() => {
        initMapForRoute(); 
      }, 50); 
    } else {
      console.warn(`No hay ruta estimada para el pedido ${pedidoId}`);
      alert(`No hay ruta estimada disponible para el pedido ${pedidoId}.`);
    }

  } catch (error) {
    console.error(`Error al obtener la ruta para el pedido ${pedidoId}:`, error);
    currentRutaGeoJson.value = null; 
    alert(`Error al cargar la ruta del pedido ${pedidoId}.`);
  } finally {
    loadingRuta.value = false;
  }
};

// Inicializar el mapa en el modal de ruta
const initMapForRoute = () => {
  if (mapInstance) {
    mapInstance.remove();
    mapInstance = null;
  }

  const mapContainer = document.getElementById('mapRutaContainer');
  if (!mapContainer) {
    console.error('Contenedor del mapa de ruta no encontrado');
    return;
  }

  // Inicializa el mapa con una vista centrada en Santiago de Chile como fallback
  mapInstance = L.map('mapRutaContainer').setView([-33.45, -70.66], 12);

  L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
  }).addTo(mapInstance);

  if (geoJsonLayer) {
    geoJsonLayer.remove();
  }

  try {
    const geoJsonData = currentRutaGeoJson.value;

    if (!geoJsonData || !geoJsonData.type || !geoJsonData.coordinates) {
      throw new Error("La respuesta de la ruta está vacía o no es un GeoJSON válido.");
    }

    // Estilo para el MultiLineString
    const lineStyle = {
      color: '#1a237e',
      weight: 4,
      opacity: 0.7,
      dashArray: '5, 5',
    };

    // Crear el GeoJSON layer con el estilo definido
    geoJsonLayer = L.geoJSON(geoJsonData, {
      style: lineStyle
    }).addTo(mapInstance);

    // Ajustar la vista del mapa para que quepa toda la ruta
    if (geoJsonLayer.getBounds().isValid()) {
      mapInstance.fitBounds(geoJsonLayer.getBounds(), { padding: [50, 50] });
    } else {
      // Vista por defecto para Santiago si no hay bounds válidos
      mapInstance.setView([-33.45, -70.66], 12);
    }
  } catch (e) {
    console.error('Error al parsear o dibujar la geometría:', e);
    mapInstance.setView([-33.45, -70.66], 12);
    alert('Error: El formato de la ruta recibida no es válido.');
  }

  mapInstance.invalidateSize();
};

// Cerrar modal de ruta
const closeRutaModal = () => {
  showRutaModal.value = false;
  if (mapInstance) {
    mapInstance.remove();
    mapInstance = null;
  }
  currentRutaGeoJson.value = null;
  currentPedidoIdForRoute.value = null;
};


// Cargar datos al montar el componente
onMounted(async () => {
  await Promise.all([
    fetchPedidos(),
    fetchMedioPagoUrgente(),
    fetchClienteMayorGasto(),
    fetchPedidosConAlerta(),
  ])
})
</script>

<style scoped>

.pedidos-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 30px;
}

.pedidos-container h2 {
  text-align: center;
  margin-bottom: 40px;
}

.loading-overlay {
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

.spinner {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 2s linear infinite;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}

.add-button {
  background-color: #1a237e;
  color: white;
  padding: 12px 18px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-bottom: 30px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
}

.table-responsive {
  overflow-x: auto;
}

.pedidos-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 30px;
  border-spacing: 0 10px;
}

.pedidos-table th,
.pedidos-table td {
  padding: 10px 15px;
  text-align: left;
  border-bottom: 1px solid #f0f0f0;
}

.pedidos-table thead th {
  background-color: #f8f8f8;
  font-weight: bold;
  padding: 12px 10px;
  border-bottom: 2px solid #ccc;
}

.pedidos-table tbody tr {
  background-color: white;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.pedidos-table tbody tr:last-child td {
  border-bottom: none;
}

.pedidos-table th:nth-child(2),
/* Fecha */
.pedidos-table td:nth-child(2) {
  min-width: 120px;
}

.pedidos-table th:nth-child(6),
/* Fecha de Entrega */
.pedidos-table td:nth-child(6) {
  min-width: 120px;
}

.actions {
  display: flex;
  gap: 10px;
}

.edit-button,
.delete-button {
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  border: none;
  font-size: 0.9em;
}

.edit-button {
  background-color: #ffc107;
  color: #212529;
}

.edit-button:hover {
  background-color: #e0a800;
}

.delete-button {
  background-color: #f44336;
  color: white;
}

.delete-button:hover {
  background-color: #e53935;
}

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
  padding: 20px;
  border-radius: 8px;
  width: 100%;
  max-width: 600px;
  max-height: 90vh; /* Limita la altura máxima al 90% del viewport */
  display: flex;
  flex-direction: column;
}


/* Estilos específicos para el modal de mapa */
.map-modal-content {
  max-width: 800px; 
  max-height: 600px; 
  overflow: hidden; 
}

.modal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding: 1rem; 
  background-color: #1a237e; 
  color: white;
  border-radius: 8px 8px 0 0; 
}

.modal-header h3 {
  margin: 0;
  text-align: left; 
}

.close-button {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
  color: white; 
}

.modal-body {
  padding-top: 10px;
  flex-grow: 1; 
  display: flex;
  flex-direction: column;
}

#mapRutaContainer {
  height: 400px;
  width: 100%;
  border-radius: 8px;
  border: 1px solid #ccc;
  margin-bottom: 15px;
  flex-shrink: 0;
}

.no-route-message {
  text-align: center;
  color: #888;
  font-style: italic;
  padding: 20px;
}


.modal-content h3 {
  margin-top: 0;
  margin-bottom: 20px;
  text-align: center;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  font-weight: bold;
  font-size: 0.95em;
}

.form-group input[type="text"],
.form-group input[type="number"],
.form-group input[type="date"],
.form-group textarea {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
  font-size: 0.9em;
}

.form-group input[type="checkbox"] {
  margin-right: 8px;
}

.form-group select {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
  font-size: 0.9em;
}

input[type="number"]::-webkit-outer-spin-button,
input[type="number"]::-webkit-inner-spin-button {
  -webkit-appearance: none;
  margin: 0;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 15px;
}

.cancel-button,
.save-button {
  padding: 10px 15px;
  border-radius: 4px;
  cursor: pointer;
  border: none;
}

.cancel-button {
  background-color: #6c757d;
  color: white;
}

.cancel-button:hover {
  background-color: #5a6268;
}

.save-button {
  background-color: #28a745;
  color: white;
}

.save-button:hover {
  background-color: #218838;
}

.delete-modal {
  background-color: #f9f9f9;
  border-radius: 8px;
  padding: 30px;
  text-align: center;
}

.delete-modal h3 {
  margin-top: 0;
  margin-bottom: 15px;
}

.delete-modal p {
  margin-bottom: 20px;
  font-size: 1.1em;
}

.delete-modal .modal-actions {
  display: flex;
  justify-content: center;
  gap: 10px;
}

.medio-pago-urgente-section {
  margin-top: 30px;
  padding: 20px;
  border: 1px solid #eee;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.medio-pago-urgente-section h2 {
  text-align: center;
  margin-bottom: 20px;
}

.medio-pago-urgente-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.medio-pago-urgente-table th,
.medio-pago-urgente-table td {
  padding: 10px 15px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.medio-pago-urgente-table thead th {
  background-color: #f8f8f8;
  font-weight: bold;
}

.medio-pago-urgente-table tbody tr:last-child td {
  border-bottom: none;
}

.loading-overlay-inline {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 15px;
}

.spinner-small {
  border: 3px solid #f3f3f3;
  border-top: 3px solid #3498db;
  border-radius: 50%;
  width: 25px;
  height: 25px;
  animation: spin 1.5s linear infinite;
  margin-right: 10px;
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}

.error-message {
  color: red;
  margin-top: 10px;
  text-align: center;
}

.cliente-mayor-gasto-section {
  margin-top: 30px;
  padding: 20px;
  border: 1px solid #eee;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.cliente-mayor-gasto-section h2 {
  text-align: center;
  margin-bottom: 20px;
}

.cliente-mayor-gasto-table {
  width: 100%;
  border-collapse: collapse;
}

.cliente-mayor-gasto-table th,
.cliente-mayor-gasto-table td {
  padding: 10px 15px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.cliente-mayor-gasto-table thead th {
  background-color: #f8f8f8;
  font-weight: bold;
}

.pedido-con-alerta {
  background-color: #ffdddd !important;
}

.alerta-text {
  color: red;
  font-weight: bold;
}

.confirm-button {
  background-color: #28a745;
  color: white;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
  border: none;
  font-size: 0.9em;
}

.confirm-button:hover {
  background-color: #218838;
}

.view-route-button {
  background-color: #007bff;
  color: white;
  padding: 8px 12px;
  border-radius: 4px;
  cursor: pointer;
  border: none;
  font-size: 0.9em;
  white-space: nowrap;
}

.view-route-button:hover {
  background-color: #0056b3;
}

.modal-scroll-container {
  overflow-y: auto; /* Habilita el scroll vertical cuando sea necesario */
  padding-right: 10px; /* Evita que el contenido se pegue al scrollbar */
  margin-right: -10px; /* Compensa el padding-right */
  flex-grow: 1; /* Ocupa todo el espacio disponible */
}

/* Estilos para el scrollbar (opcional) */
.modal-scroll-container::-webkit-scrollbar {
  width: 8px;
}

.modal-scroll-container::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 4px;
}

.modal-scroll-container::-webkit-scrollbar-thumb {
  background: #888;
  border-radius: 4px;
}

.modal-scroll-container::-webkit-scrollbar-thumb:hover {
  background: #555;
}

</style>