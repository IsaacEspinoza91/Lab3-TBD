<template>
  <div class="farmacias-container">
    <h1>Gestión de Farmacias</h1>

    <div v-if="loadingFarmacias" class="loading-overlay">
      <div class="spinner"></div>
      <p>Cargando farmacias...</p>
    </div>

    <button @click="showModal = true" class="add-button" v-if="!loadingFarmacias">
      <i class="fas fa-plus"></i> Agregar Farmacia
    </button>

    <div class="table-responsive" v-if="!loadingFarmacias">
      <table class="farmacias-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Ubicación</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="farmacia in farmacias" :key="farmacia.id">
            <td>{{ farmacia.id }}</td>
            <td>{{ farmacia.nombre }}</td>
            <td>{{ farmacia.lugar }}</td>
            <td class="actions">
              <button @click="editFarmacia(farmacia)" class="edit-button">
                <i class="fas fa-edit">Modificar</i>
              </button>
              <button @click="confirmDelete(farmacia.id)" class="delete-button">
                <i class="fas fa-trash">Eliminar</i>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <div v-if="showModal" class="modal-overlay">
      <div class="modal-content">
        <h3>{{ isEditing ? 'Editar Farmacia' : 'Nueva Farmacia' }}</h3>
        <form @submit.prevent="isEditing ? updateFarmacia() : createFarmacia()">
          <div class="form-group">
            <label>Nombre:</label>
            <input v-model="form.nombre" type="text" required>
          </div>
          <div class="form-group">
            <label>Ubicación:</label>
            <input v-model="form.lugar" type="text" required>
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

    <div v-if="showDeleteModal" class="modal-overlay">
      <div class="modal-content delete-modal">
        <h3>¿Eliminar farmacia?</h3>
        <p>Esta acción no se puede deshacer.</p>
        <div class="modal-actions">
          <button @click="showDeleteModal = false" class="cancel-button">Cancelar</button>
          <button @click="deleteFarmacia" class="delete-button">Eliminar</button>
        </div>
      </div>
    </div>

    <hr v-if="!loadingFarmacias" style="margin: 30px 0;">

    <div class="farmacias-fallidas-section" v-if="!loadingFarmacias">
      <h2>Farmacias con Más Entregas Fallidas</h2>
      <div v-if="loadingFarmaciasFallidas" class="loading-overlay-inline">
        <div class="spinner-small"></div>
        <p>Cargando datos...</p>
      </div>
      <div v-if="farmaciasFallidasData.length > 0" class="table-responsive">
        <table class="farmacias-fallidas-table">
          <thead>
            <tr>
              <th>Nombre</th>
              <th>Ubicación</th>
              <th>Entregas Fallidas</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="item in farmaciasFallidasData" :key="item.id">
              <td>{{ item.nombre }}</td>
              <td>{{ item.lugar }}</td>
              <td>{{ item.entregas_fallidas }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <p v-if="farmaciasFallidasError" class="error-message">
        Error al cargar las farmacias con más entregas fallidas.
      </p>
      <p v-if="!loadingFarmaciasFallidas && farmaciasFallidasData.length === 0 && !farmaciasFallidasError">
        No hay datos disponibles sobre farmacias con entregas fallidas.
      </p>
    </div>
  </div>

  <hr v-if="!loadingFarmacias" style="margin: 30px 0;">

  <!-- Vista: Farmacias con mayor volumen de productos entregados -->
  <div class="table-responsive" v-if="!loadingFarmacias">
    <h2>Farmacias con mayor volumen de productos entregados</h2>
    <table class="farmacias-table">
      <thead>
        <tr>
          <th>ID</th>
          <th>Nombre</th>
          <th>Dirección</th>
          <th>Total Productos Pedidos</th>
          <th>Total Productos Entregados</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="farmacia in farmaciasVolumen" :key="farmacia.id">
          <td>{{ farmacia.id }}</td>
          <td>{{ farmacia.farmacia }}</td>
          <td>{{ farmacia.lugar }}</td>
          <td>{{ farmacia.total_productos_pedidos }}</td>
          <td>{{ farmacia.total_productos_entregados }}</td>
        </tr>
      </tbody>
    </table>
  </div>

</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import axios from 'axios'
import api from '@/api'

const authStore = useAuthStore()
const farmacias = ref([])
const showModal = ref(false)
const showDeleteModal = ref(false)
const isEditing = ref(false)
const currentFarmaciaId = ref(null)
const loadingFarmacias = ref(false)
const form = ref({ nombre: '', lugar: '' })

const farmaciasFallidasData = ref([])
const loadingFarmaciasFallidas = ref(true)
const farmaciasFallidasError = ref(null)

const farmaciasVolumen = ref([])

const fetchFarmaciasVolumen = async () => {
  try {
    const response = await api.get('/farmacias/volumen-entregado')
    farmaciasVolumen.value = response.data
  } catch (error) {
    console.error('Error al obtener farmacias con mayor volumen:', error)
  }
}

onMounted(() => {
  fetchFarmaciasVolumen()
})


const fetchFarmaciasFallidas = async () => {
  loadingFarmaciasFallidas.value = true
  farmaciasFallidasData.value = []
  farmaciasFallidasError.value = null
  try {
    const response = await api.get('/pedidos/farmacias-mas-fallidas')
    farmaciasFallidasData.value = response.data
  } catch (error) {
    console.error('Error al obtener farmacias con más entregas fallidas:', error)
    farmaciasFallidasError.value = 'Hubo un error al cargar los datos de entregas fallidas.'
  } finally {
    loadingFarmaciasFallidas.value = false
  }
}

// Obtener todas las farmacias
const fetchFarmacias = async () => {
  loadingFarmacias.value = true
  try {
    const response = await api.get('/farmacias')
    farmacias.value = response.data
  } catch (error) {
    console.error('Detalles del error:', {
      message: error.message,
      response: error.response,
      request: error.request,
      config: error.config
    })
  } finally {
    loadingFarmacias.value = false
  }
}

// Crear nueva farmacia
const createFarmacia = async () => {
  try {
    await api.post('/farmacias', form.value)
    await fetchFarmacias()
    closeModal()
    alert('Farmacia creada exitosamente')
  } catch (error) {
    console.error('Error al crear farmacia:', error)
    alert('Error al crear farmacia')
  }
}

// Editar farmacia
const editFarmacia = (farmacia) => {
  form.value = { nombre: farmacia.nombre, lugar: farmacia.lugar }
  currentFarmaciaId.value = farmacia.id
  isEditing.value = true
  showModal.value = true
}

// Actualizar farmacia
const updateFarmacia = async () => {
  try {
    await api.put(`/farmacias/${currentFarmaciaId.value}`, form.value)
    await fetchFarmacias()
    closeModal()
    alert('Farmacia actualizada exitosamente')
  } catch (error) {
    console.error('Error al actualizar farmacia:', error)
    alert('Error al actualizar farmacia')
  }
}

// Confirmar eliminación
const confirmDelete = (id) => {
  currentFarmaciaId.value = id
  showDeleteModal.value = true
}

// Eliminar farmacia
const deleteFarmacia = async () => {
  try {
    await api.delete(`/farmacias/${currentFarmaciaId.value}`)
    await fetchFarmacias()
    showDeleteModal.value = false
    alert('Farmacia eliminada exitosamente')
  } catch (error) {
    console.error('Error al eliminar farmacia:', error)
    alert('Error al eliminar farmacia')
  }
}

// Cerrar modal
const closeModal = () => {
  showModal.value = false
  isEditing.value = false
  form.value = { nombre: '', lugar: '' }
  currentFarmaciaId.value = null
}

// Cargar datos al montar el componente
onMounted(() => {
  fetchFarmacias()
  fetchFarmaciasFallidas()
})
</script>

<style scoped>
.farmacias-container {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.add-button {
  background-color: #1a237e;
  color: white;
  padding: 10px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  margin-bottom: 20px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.add-button:hover {
  background-color: #303f9f;
}

.farmacias-table,
.farmacias-fallidas-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
  margin-bottom: 30px;
  /* Espacio entre tablas */
}

.farmacias-table th,
.farmacias-table td,
.farmacias-fallidas-table th,
.farmacias-fallidas-table td {
  border: 1px solid #ddd;
  padding: 12px;
  text-align: left;
}

.farmacias-table th,
.farmacias-fallidas-table th {
  background-color: #f2f2f2;
  font-weight: bold;
}

.farmacias-table tr:nth-child(even),
.farmacias-fallidas-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.farmacias-table tr:hover,
.farmacias-fallidas-table tr:hover {
  background-color: #f1f1f1;
}

.actions {
  display: flex;
  gap: 10px;
}

.edit-button,
.delete-button {
  padding: 6px 10px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  display: flex;
  align-items: center;
  justify-content: center;
}

.edit-button {
  background-color: #ffc107;
  color: #212529;
}

.edit-button:hover {
  background-color: #e0a800;
}

.delete-button {
  background-color: #dc3545;
  color: white;
}

.loading-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(255, 255, 255, 0.8);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.spinner {
  border: 5px solid #f3f3f3;
  border-top: 5px solid #1a237e;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 1s linear infinite;
}

.delete-button:hover {
  background-color: #c82333;
}

/* Estilos para los modales */
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
  padding: 25px;
  border-radius: 8px;
  width: 100%;
  max-width: 500px;
}

.delete-modal {
  max-width: 400px;
  text-align: center;
}

.form-group {
  margin-bottom: 15px;
}

.form-group label {
  display: block;
  margin-bottom: 5px;
  font-weight: bold;
}

.form-group input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 10px;
  margin-top: 20px;
}

.cancel-button,
.save-button {
  padding: 8px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
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

.farmacias-fallidas-section {
  margin-top: 30px;
  padding: 20px;
  border: 1px solid #eee;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.farmacias-fallidas-section h2 {
  margin-top: 0;
  margin-bottom: 20px;
  text-align: center;
}

.farmacias-fallidas-table {
  width: 100%;
  border-collapse: collapse;
}

.farmacias-fallidas-table th,
.farmacias-fallidas-table td {
  border: 1px solid #ddd;
  padding: 10px 15px;
  text-align: left;
}

.farmacias-fallidas-table thead th {
  background-color: #f8f8f8;
  font-weight: bold;
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

.farmacias-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.farmacias-table th,
.farmacias-table td {
  padding: 10px;
  border: 1px solid #ddd;
  text-align: left;
}

.farmacias-table th {
  background-color: #f2f2f2;
  font-weight: bold;
}
</style>