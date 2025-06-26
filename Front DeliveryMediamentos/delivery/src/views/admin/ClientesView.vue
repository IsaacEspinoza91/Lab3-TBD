<template>
  <div class="clientes-container">
    <h1>Lista de Clientes</h1>

    <!-- Cargando Clientes -->
    <div v-if="loading" class="loading-overlay">
      <div class="spinner"></div>
      <p>Cargando clientes...</p>
    </div>

    <!-- Tabla de Clientes  -->
    <div v-if="!loading" class="table-responsive">
      <table class="clientes-table">
        <thead>
          <tr>
            <th>ID Usuario</th>
            <th>Nombre</th>
            <th>Email</th>
            <th>Dirección</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="cliente in clientes" :key="cliente.usuarioId">
            <td>{{ cliente.usuarioId }}</td>
            <td>{{ cliente.nombre }}</td>
            <td>{{ cliente.email }}</td>
            <td>{{ cliente.direccion }}</td>
          </tr>
        </tbody>
      </table>
    </div>


    <hr v-if="!loading" style="margin: 30px 0;">

    <!-- Sección Cliente con Mayor Gasto -->
    <div class="top-client-card" v-if="!loading && clienteMayorGasto">
      <div class="top-client-header">
        <h2>Cliente con Mayor Gasto en Pedidos Entregados</h2>
        <span class="badge">TOP CLIENTE</span>
      </div>
      <div class="top-client-details">
        <div class="detail-item">
          <span class="detail-label">Nombre:</span>
          <span class="detail-value">{{ clienteMayorGasto.nombre }} {{ clienteMayorGasto.apellido }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">Total Gastado:</span>
          <span class="detail-value highlight">{{ formatCurrency(clienteMayorGasto.totalGastado) }}</span>
        </div>
        <div class="detail-item">
          <span class="detail-label">ID Cliente:</span>
          <span class="detail-value">{{ clienteMayorGasto.clienteId }}</span>
        </div>
      </div>
    </div>

    <hr v-if="!loading" style="margin: 30px 0;">


    <!-- Resumen de Pedidos por Cliente -->
    <div v-if="resumenPedidosData" class="resumen-pedidos">
      <h2>Resumen de Pedidos</h2>
      <table class="resumen-pedidos-table">
        <thead>
          <tr>
            <th>Cliente ID</th>
            <th>Nombre</th>
            <th>Apellido</th>
            <th>Email</th>
            <th>Cantidad de Pedidos</th>
            <th>Monto Total</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="resumen in resumenPedidosData" :key="resumen.cliente_id">
            <td>{{ resumen.cliente_id }}</td>
            <td>{{ resumen.nombre }}</td>
            <td>{{ resumen.apellido }}</td>
            <td>{{ resumen.email }}</td>
            <td>{{ resumen.cantidad_pedidos }}</td>
            <td>{{ resumen.monto_total }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import api from '@/api'

const authStore = useAuthStore() // Inicializar el store de autenticación
const clientes = ref([]) // Lista de clientes
const resumenPedidosData = ref(null) // Resumen de pedidos por cliente
const loading = ref(true) // Estado de carga de clientes
const loadingResumenPedidos = ref(true) // Estado de carga del resumen de pedidos
const clienteMayorGasto = ref(null)


// Función para formatear moneda
const formatCurrency = (value) => {
  return new Intl.NumberFormat('es-CL', {
    style: 'currency',
    currency: 'CLP'
  }).format(value)
}


// Función para obtener el cliente con mayor gasto
const fetchClienteMayorGasto = async () => {
  try {
    const response = await api.get('/clientes/cliente-mayor-gasto')
    clienteMayorGasto.value = response.data
  } catch (error) {
    console.error('Error al obtener cliente con mayor gasto:', error)
  }
}



// Función para obtener los clientes
const fetchClientes = async () => {
  try {
    const response = await api.get('/clientes/detallado', {
      headers: {
        Authorization: `Bearer ${authStore.token}` // Usar el token del store
      }
    })
    clientes.value = response.data
  } catch (error) {
    console.error('Error al obtener clientes:', error)
    alert('No se pudo cargar la lista de clientes.')
  } finally {
    loading.value = false
  }
}

// Función para obtener el resumen de pedidos
const fetchResumenPedidos = async () => {
  loadingResumenPedidos.value = true
  resumenPedidosData.value = null
  try {
    const response = await api.get('/clientes/resumen-pedidos')
    resumenPedidosData.value = response.data
  } catch (error) {
    console.error('Error al obtener el resumen de pedidos:', error)
    alert('No se pudo cargar el resumen de pedidos por cliente.')
  } finally {
    loadingResumenPedidos.value = false
  }
}

// Llamamos las funciones cuando el componente se monta
onMounted(() => {
  fetchClientes()
  fetchResumenPedidos()
  fetchClienteMayorGasto()
})
</script>

<style scoped>
.clientes-container {
  max-width: 900px;
  margin: 20px auto;
  padding: 30px;
}

.clientes-container h1 {
  text-align: center;
  margin-bottom: 30px;
}

/* Estilo de la tabla de clientes */
.table-responsive {
  overflow-x: auto;
  margin-top: 20px;
}

.clientes-table,
.resumen-pedidos-table {
  width: 100%;
  border-collapse: collapse;
}

.clientes-table th,
.clientes-table td,
.resumen-pedidos-table th,
.resumen-pedidos-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.clientes-table thead th,
.resumen-pedidos-table thead th {
  background-color: #f8f8f8;
  font-weight: bold;
}

/* Estilo de la tabla de resumen de pedidos */
.resumen-pedidos {
  margin-top: 30px;
}

.resumen-pedidos-table {
  margin-top: 20px;
}

.loading-overlay {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 150px;
}

.spinner {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #3498db;
  border-radius: 50%;
  width: 40px;
  height: 40px;
  animation: spin 2s linear infinite;
  margin-right: 10px;
}


.top-client-card {
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  margin-bottom: 30px;
  padding: 20px;
  border-left: 4px solid #1a237e;
}

.top-client-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 15px;
  padding-bottom: 10px;
  border-bottom: 1px solid #eee;
}

.top-client-header h2 {
  margin: 0;
  color: #1a237e;
  font-size: 1.4rem;
}

.badge {
  background-color: #ffc107;
  color: #212529;
  padding: 5px 10px;
  border-radius: 20px;
  font-size: 0.8rem;
  font-weight: bold;
}

.top-client-details {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 15px;
}

.detail-item {
  margin-bottom: 10px;
}

.detail-label {
  display: block;
  font-weight: bold;
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 3px;
}

.detail-value {
  font-size: 1.1rem;
  color: #333;
}

.highlight {
  color: #1a237e;
  font-weight: bold;
  font-size: 1.3rem;
}

@media (max-width: 768px) {
  .top-client-details {
    grid-template-columns: 1fr;
  }

  .top-client-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .badge {
    margin-top: 10px;
  }
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}
</style>
