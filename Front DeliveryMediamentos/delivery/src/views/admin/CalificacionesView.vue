<template>
  <div class="calificaciones-container">
    <h1>Lista de Calificaciones</h1>

    <div v-if="loading" class="loading-overlay">
      <div class="spinner"></div>
      <p>Cargando calificaciones...</p>
    </div>

    <div v-if="!loading" class="table-responsive">
      <table class="calificaciones-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Puntuación</th>
            <th>Estrellas</th>
            <th>Cliente ID</th>
            <th>Repartidor ID</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="calificacion in calificaciones" :key="calificacion.id">
            <td>{{ calificacion.id }}</td>
            <td>{{ calificacion.puntuacion }}</td>
            <td>{{ calificacion.estrellas }}</td>
            <td>{{ calificacion.cliente_id }}</td>
            <td>{{ calificacion.repartidor_id }}</td>
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

const authStore = useAuthStore()
const calificaciones = ref([])
const loading = ref(true)

// Función para obtener las calificaciones
const fetchCalificaciones = async () => {
  try {
    const response = await api.get('/calificaciones', {
      headers: {
        Authorization: `Bearer ${authStore.token}`
      }
    })
    calificaciones.value = response.data
  } catch (error) {
    console.error('Error al obtener calificaciones:', error)
    alert('No se pudo cargar la lista de calificaciones.')
  } finally {
    loading.value = false
  }
}

// Cargar datos al montar el componente
onMounted(() => {
  fetchCalificaciones()
})
</script>

<style scoped>
.calificaciones-container {
  max-width: 800px;
  margin: 20px auto;
  padding: 30px;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.calificaciones-container h1 {
  text-align: center;
  margin-bottom: 30px;
  color: #1a237e;
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

.calificaciones-table {
  width: 100%;
  border-collapse: collapse;
  background-color: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 5px rgba(0, 0, 0, 0.05);
}

.calificaciones-table th,
.calificaciones-table td {
  padding: 12px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.calificaciones-table thead th {
  background-color: #f8f8f8;
  font-weight: bold;
  color: #333;
}

.calificaciones-table tbody tr:hover {
  background-color: #f5f5f5;
}

.calificaciones-table tbody tr:last-child td {
  border-bottom: none;
}
</style>