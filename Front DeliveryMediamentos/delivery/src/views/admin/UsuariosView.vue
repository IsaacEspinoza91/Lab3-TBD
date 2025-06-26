<template>
  <div class="productos-container">
    <h1>Gestión de Usuarios</h1>

    <div v-if="loading" class="loading-overlay">
      <div class="spinner"></div>
      <p>Cargando usuarios...</p>
    </div>

    <button @click="showModal = true" class="add-button" v-if="!loading">
      <i class="fas fa-plus"></i> Agregar Usuario
    </button>

    <div class="table-responsive" v-if="!loading">
      <table class="productos-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>RUT</th>
            <th>Nombre</th>
            <th>Apellido</th>
            <th>Email</th>
            <th>Teléfono</th>
            <th>Tipo</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="usuario in usuarios" :key="usuario.id">
            <td>{{ usuario.id }}</td>
            <td>{{ usuario.rut }}</td>
            <td>{{ usuario.nombre }}</td>
            <td>{{ usuario.apellido }}</td>
            <td>{{ usuario.email }}</td>
            <td>{{ usuario.telefono }}</td>
            <td>{{ usuario.tipo }}</td>
            <td class="actions">
              <button @click="editUsuario(usuario)" class="edit-button">
                <i class="fas fa-edit">Modificar</i>
              </button>
              <button @click="confirmDelete(usuario.id)" class="delete-button">
                <i class="fas fa-trash">Eliminar</i>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal de Crear / Editar -->
    <div v-if="showModal" class="modal-overlay">
      <div class="modal-content">
        <h3>{{ isEditing ? 'Editar Usuario' : 'Nuevo Usuario' }}</h3>
        <form @submit.prevent="isEditing ? updateUsuario() : createUsuario()">
          <div class="form-group">
            <label>RUT:</label>
            <input v-model="form.rut" type="text" required />
          </div>
          <div class="form-group">
            <label>Nombre:</label>
            <input v-model="form.nombre" type="text" required />
          </div>
          <div class="form-group">
            <label>Apellido:</label>
            <input v-model="form.apellido" type="text" required />
          </div>
          <div class="form-group">
            <label>Email:</label>
            <input v-model="form.email" type="email" required />
          </div>
          <div class="form-group">
            <label>Teléfono:</label>
            <input v-model="form.telefono" type="text" required />
          </div>
          <div class="form-group">
            <label>Tipo:</label>
            <select v-model="form.tipo" required>
              <option value="CLIENTE">Cliente</option>
              <option value="REPARTIDOR">Repartidor</option>
              <option value="ADMIN">Administrador</option>
            </select>
          </div>
          <div class="modal-actions">
            <button type="button" @click="closeModal" class="cancel-button">Cancelar</button>
            <button type="submit" class="save-button">{{ isEditing ? 'Actualizar' : 'Guardar' }}</button>
          </div>
        </form>
      </div>
    </div>

    <!-- Modal de Confirmación -->
    <div v-if="showDeleteModal" class="modal-overlay">
      <div class="modal-content delete-modal">
        <h3>¿Eliminar usuario?</h3>
        <p>Esta acción no se puede deshacer.</p>
        <div class="modal-actions">
          <button @click="showDeleteModal = false" class="cancel-button">Cancelar</button>
          <button @click="deleteUsuario" class="delete-button">Eliminar</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'
import axios from 'axios'
import api from '@/api'

const authStore = useAuthStore()
const usuarios = ref([])
const loading = ref(true)
const showModal = ref(false)
const showDeleteModal = ref(false)
const isEditing = ref(false)
const currentUsuarioId = ref(null)

const form = ref({
  rut: '',
  nombre: '',
  apellido: '',
  email: '',
  telefono: '',
  tipo: ''
})


// Obtener usuarios
const fetchUsuarios = async () => {
  try {
    const response = await api.get('/usuarios')
    usuarios.value = response.data
  } catch (error) {
    console.error('Error al obtener usuarios:', error)
  } finally {
    loading.value = false
  }
}

// Crear usuario
const createUsuario = async () => {
  try {
    await api.post('/usuarios', form.value)
    await fetchUsuarios()
    closeModal()
    alert('Usuario creado exitosamente')
  } catch (error) {
    console.error('Error al crear usuario:', error)
    alert('Error al crear usuario')
  }
}

// Editar usuario
const editUsuario = (usuario) => {
  form.value = {
    rut: usuario.rut,
    nombre: usuario.nombre,
    apellido: usuario.apellido,
    email: usuario.email,
    telefono: usuario.telefono,
    tipo: usuario.tipo,
    password: usuario.password
  }
  currentUsuarioId.value = usuario.id
  isEditing.value = true
  showModal.value = true
}

// Actualizar usuario
const updateUsuario = async () => {
  try {
    await api.put(`/usuarios/${currentUsuarioId.value}`, form.value)
    await fetchUsuarios()
    closeModal()
    alert('Usuario actualizado exitosamente')
  } catch (error) {
    console.error('Error al actualizar usuario:', error)
    alert('Error al actualizar usuario')
  }
}

// Confirmar eliminación
const confirmDelete = (id) => {
  currentUsuarioId.value = id
  showDeleteModal.value = true
}

// Eliminar usuario
const deleteUsuario = async () => {
  try {
    await api.delete(`/usuarios/${currentUsuarioId.value}`)
    await fetchUsuarios()
    showDeleteModal.value = false
    alert('Usuario eliminado exitosamente')
  } catch (error) {
    console.error('Error al eliminar usuario:', error)
    alert('Error al eliminar usuario')
  }
}

// Cerrar modal
const closeModal = () => {
  showModal.value = false
  isEditing.value = false
  form.value = {
    rut: '',
    nombre: '',
    apellido: '',
    email: '',
    telefono: '',
    tipo: ''
  }
  currentUsuarioId.value = null
}

onMounted(() => {
  fetchUsuarios()
})
</script>


<style scoped>
.productos-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.productos-container h2 {
  text-align: center;
  margin-bottom: 30px;
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
  display: flex;
  align-items: center;
  gap: 10px;
}

.productos-table {
  width: 100%;
  border-collapse: collapse;
  margin-bottom: 20px;
}

.productos-table th,
.productos-table td {
  border: 1px solid #ddd;
  padding: 12px;
  text-align: left;
}

.productos-table th {
  background-color: #f1f1f1;
  font-weight: bold;
}

.actions {
  display: flex;
  gap: 10px;
}

.edit-button,
.delete-button {
  padding: 10px 14px;
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
  padding: 30px;
  border-radius: 8px;
  width: 100%;
  max-width: 600px;
}

.form-group {
  margin-bottom: 20px;
}

.form-group label {
  display: block;
  margin-bottom: 10px;
  font-weight: bold;
}

.form-group input {
  width: 100%;
  padding: 12px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.cancel-button,
.save-button {
  padding: 10px 20px;
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

.delete-modal {
  background-color: #f9f9f9;
  border-radius: 8px;
  padding: 30px;
  text-align: center;
}

.delete-modal p {
  margin-bottom: 20px;
  font-size: 16px;
}

.delete-modal .modal-actions {
  display: flex;
  justify-content: space-around;
}
</style>
