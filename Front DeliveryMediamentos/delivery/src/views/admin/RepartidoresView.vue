<template>
  <div class="repartidores-container">
    <h1>Lista de Repartidores</h1>

    <div v-if="loadingRepartidores" class="loading-overlay">
      <div class="spinner"></div>
      <p>Cargando repartidores...</p>
    </div>

    <div class="table-responsive" v-if="!loadingRepartidores">
      <table class="repartidores-table">
        <thead>
          <tr>
            <th>ID Usuario</th>
            <th>Tipo de Vehículo</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="repartidor in repartidores" :key="repartidor.usuario_id">
            <td>{{ repartidor.usuario_id }}</td>
            <td>{{ repartidor.tipo_vehiculo }}</td>
          </tr>
        </tbody>
      </table>
    </div>

    <hr v-if="!loadingRepartidores" style="margin: 30px 0;">

    <div class="tiempo-promedio-todos-section" v-if="!loadingRepartidores">
      <h2>Tiempo Promedio de Entrega de Todos los Repartidores</h2>

      <div v-if="loadingTiempoPromedioTodos" class="loading-overlay-inline">
        <div class="spinner-small"></div>
        <p>Cargando tiempo promedio...</p>
      </div>

      <div v-if="tiempoPromedioTodosRepartidores.length > 0 && !loadingTiempoPromedioTodos" class="table-responsive">
        <table class="tiempo-promedio-table">
          <thead>
            <tr>
              <th>Nombre del Repartidor</th>
              <th>Tiempo Promedio (Días)</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in tiempoPromedioTodosRepartidores" :key="item.repartadores_nombre">
              <td>{{ item.repartidores_nombre }}</td>
              <td>{{ parseFloat(item.tiempo_promedio_dias).toFixed(2) }}</td>
            </tr>
          </tbody>
        </table>
      </div>

      <p v-if="tiempoPromedioTodosRepartidores.length === 0 && !loadingTiempoPromedioTodos">
        No se encontraron datos de tiempo promedio para los repartidores.
      </p>
    </div>

    <hr v-if="!loadingRepartidores" style="margin: 30px 0;">

    <div class="tiempo-promedio-por-id-section" v-if="!loadingRepartidores">
      <h2>Tiempo Promedio de Entrega por ID de Repartidor</h2>

      <div class="input-group">
        <label for="repartidorId">ID del Repartidor:</label>
        <input type="number" id="repartidorId" v-model="repartidorIdConsulta" placeholder="Ingrese ID" />
        <button @click="fetchTiempoPromedioPorId" :disabled="!repartidorIdConsulta" class="add-button"
          style="margin-bottom: 0;">Buscar</button>
      </div>

      <div v-if="loadingTiempoPromedioPorId" class="loading-overlay-inline">
        <div class="spinner-small"></div>
        <p>Cargando tiempo promedio...</p>
      </div>

      <div v-if="tiempoPromedioRepartidorPorId.length > 0 && !loadingTiempoPromedioPorId" class="table-responsive">
        <h3>Tiempo Promedio para el Repartidor ID {{ repartidorIdConsulta }}</h3>
        <table class="tiempo-promedio-table">
          <thead>
            <tr>
              <th>Nombre del Repartidor</th>
              <th>Tiempo Promedio (Días)</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in tiempoPromedioRepartidorPorId" :key="item.repartidores_nombre">
              <td>{{ item.repartidores_nombre }}</td>
              <td>{{ parseFloat(item.tiempo_promedio_dias).toFixed(2) }}</td>
            </tr>
          </tbody>
        </table>
      </div>

    </div>
  </div>

  <!-- Vista de desempeño por repartidor -->
  <div class="table-responsive" v-if="!loadingRepartidores">
    <h2>Desempeño por Repartidor</h2>
    <table class="clientes-table">
      <thead>
        <tr>
          <th>Nombre</th>
          <th>Apellido</th>
          <th>Vehículo</th>
          <th>Prom. Estrellas</th>
          <th>Total Pedidos</th>
          <th>Total Productos Pedidos</th>
          <th>Total Productos Entregados</th>
          <th>% Entregas Exitosas</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="r in desempenoRepartidores" :key="r.nombre + r.apellido">
          <td>{{ r.nombre }}</td>
          <td>{{ r.apellido }}</td>
          <td>{{ r.vehiculo }}</td>
          <td>{{ r.promedio_estrellas.toFixed(2) }}</td>
          <td>{{ r.total_pedidos }}</td>
          <td>{{ r.total_productos_pedidos }}</td>
          <td>{{ r.total_productos_entregados }}</td>
          <td>{{ r.porcentaje_entregas_exitosas }}%</td>
        </tr>
      </tbody>
    </table>
  </div>

  <div class="repartidores-table">
    <h2>Top 3 Repartidores con Mejor Rendimiento</h2>
    <table class="table">
      <thead>
        <tr>
          <th>Nombre</th>
          <th>Apellido</th>
          <th>Puntuación</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="repartidor in topRepartidores" :key="repartidor.nombre">
          <td>{{ repartidor.nombre }}</td>
          <td>{{ repartidor.apellido }}</td>
          <td>{{ repartidor.puntuacion }}</td>
        </tr>
      </tbody>
    </table>
  </div>

</template>

<script setup>
import { ref, onMounted } from 'vue'
import api from '@/api'

const repartidores = ref([])
const loadingRepartidores = ref(true)
const desempenoRepartidores = ref([])

const topRepartidores = ref([])

const fetchTopRepartidores = async () => {
  try {
    const response = await api.get('/repartidores/top')
    topRepartidores.value = response.data
  } catch (error) {
    console.error('Error al obtener los repartidores:', error)
  }
}

onMounted(() => {
  fetchTopRepartidores()
})

// Desempeno de repartidores
const fetchDesempenoRepartidores = async () => {
  try {
    const response = await api.get('/repartidores/desempeno')
    desempenoRepartidores.value = response.data
  } catch (error) {
    console.error('Error al obtener el desempeño de repartidores:', error)
  }
}

onMounted(() => {
  fetchDesempenoRepartidores()
})


// Para la tabla de todos los tiempos promedio
const tiempoPromedioTodosRepartidores = ref([])
const loadingTiempoPromedioTodos = ref(false)

// Para la búsqueda por ID
const repartidorIdConsulta = ref(null)
const tiempoPromedioRepartidorPorId = ref([])
const loadingTiempoPromedioPorId = ref(false)

// Obtener todos los repartidores
const fetchRepartidores = async () => {
  try {
    const response = await api.get('/repartidores')
    repartidores.value = response.data
  } catch (error) {
    console.error('Error al obtener repartidores:', error)
  } finally {
    loadingRepartidores.value = false
  }
}

// Obtener el tiempo promedio para todos los repartidores
const fetchTiempoPromedioTodos = async () => {
  loadingTiempoPromedioTodos.value = true
  tiempoPromedioTodosRepartidores.value = []

  try {
    const response = await api.get('/pedidos/tiempo-promedio-repartidor')
    tiempoPromedioTodosRepartidores.value = response.data
  } catch (error) {
    console.error('Error al obtener el tiempo promedio de todos los repartidores:', error)
    alert('Error al obtener el tiempo promedio de los repartidores.')
  } finally {
    loadingTiempoPromedioTodos.value = false
  }
}

// Obtener el tiempo promedio para un repartidor específico por ID
const fetchTiempoPromedioPorId = async () => {
  if (!repartidorIdConsulta.value) return

  loadingTiempoPromedioPorId.value = true
  tiempoPromedioRepartidorPorId.value = []

  try {
    const response = await api.get(`/pedidos/tiempo-promedio-repartidor/${repartidorIdConsulta.value}`)
    tiempoPromedioRepartidorPorId.value = response.data
  } catch (error) {
    console.error('Error al obtener el tiempo promedio del repartidor:', error)
    alert('Error al obtener el tiempo promedio del repartidor.')
  } finally {
    loadingTiempoPromedioPorId.value = false
  }
}

onMounted(() => {
  fetchRepartidores()
  fetchTiempoPromedioTodos()
})
</script>

<style scoped>
.repartidores-container {
  max-width: 900px;
  margin: 20px auto;
  padding: 30px;
}

.repartidores-container h1,
.repartidores-container h2,
.repartidores-container h3 {
  text-align: center;
  margin-bottom: 30px;
}

.loading-overlay,
.loading-overlay-inline {
  display: flex;
  justify-content: center;
  align-items: center;
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1000;
}

.loading-overlay-inline {
  margin-top: 10px;
}

.spinner,
.spinner-small {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
}

.spinner {
  width: 50px;
  height: 50px;
  animation: spin 2s linear infinite;
}

.spinner-small {
  width: 30px;
  height: 30px;
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

.table-responsive {
  overflow-x: auto;
  margin-top: 20px;
}

.repartidores-table,
.tiempo-promedio-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 30px;
  border-spacing: 0 10px;
}

.repartidores-table th,
.repartidores-table td,
.tiempo-promedio-table th,
.tiempo-promedio-table td {
  padding: 10px 15px;
  text-align: left;
  border-bottom: 1px solid #f0f0f0;
}

.repartidores-table thead th,
.tiempo-promedio-table thead th {
  background-color: #f8f8f8;
  font-weight: bold;
  padding: 12px 15px;
  border-bottom: 2px solid #ccc;
}

.repartidores-table tbody tr,
.tiempo-promedio-table tbody tr {
  background-color: white;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.repartidores-table tbody tr:last-child td,
.tiempo-promedio-table tbody tr:last-child td {
  border-bottom: none;
}

.tiempo-promedio-todos-section,
.tiempo-promedio-por-id-section {
  margin-top: 40px;
  padding: 20px;
  border: 1px solid #eee;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.tiempo-promedio-todos-section h2,
.tiempo-promedio-por-id-section h2 {
  margin-top: 0;
}

.input-group {
  display: flex;
  gap: 15px;
  align-items: center;
  margin-bottom: 20px;
}

.input-group label {
  font-weight: bold;
}

.input-group input[type="number"] {
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  width: 120px;
}

.input-group button {
  padding: 10px 18px;
  background-color: #1a237e;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.input-group button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}

/* Vista de desempeño por repartidor */
.table-responsive {
  overflow-x: auto;
  margin-top: 20px;
}

.clientes-table {
  width: 100%;
  border-collapse: collapse;
}

.clientes-table th,
.clientes-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.clientes-table thead th {
  background-color: #f8f8f8;
  font-weight: bold;
}

.clientes-table tbody tr:hover {
  background-color: #f1f1f1;
}

.repartidores-table {
  margin: 20px;
  padding: 10px;
  border-radius: 8px;
  background-color: #fff;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
}

.table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.table th, .table td {
  text-align: left;
  padding: 12px;
  border-bottom: 1px solid #ddd;
}

.table th {
  background-color: #f4f4f4;
}

.table tr:hover {
  background-color: #f9f9f9;
}

h2 {
  font-size: 1.5rem;
  font-weight: bold;
  margin-bottom: 10px;
}
</style>