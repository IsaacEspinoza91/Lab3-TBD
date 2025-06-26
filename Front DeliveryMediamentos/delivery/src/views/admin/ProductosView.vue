<template>
  <div class="productos-container">
    <h1>Gestión de Productos</h1>

    <div v-if="loading" class="loading-overlay">
      <div class="spinner"></div>
      <p>Cargando productos...</p>
    </div>

    <button @click="showModal = true" class="add-button" v-if="!loading">
      <i class="fas fa-plus"></i> Agregar Producto
    </button>
    <button @click="showModal = true" class="add-button" v-if="!loading">
      <i class="fas fa-plus"></i> Productos por farmacia
    </button>

    <div class="table-responsive" v-if="!loading">
      <table class="productos-table">
        <thead>
          <tr>
            <th>ID</th>
            <th>Nombre</th>
            <th>Precio</th>
            <th>Stock</th>
            <th>Requiere Receta</th>
            <th>Acciones</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="producto in productos" :key="producto.id">
            <td>{{ producto.id }}</td>
            <td>{{ producto.nombre }}</td>
            <td>{{ producto.precio }}</td>
            <td>{{ producto.stock }}</td>
            <td>{{ producto.requiere_receta ? 'Sí' : 'No' }}</td>
            <td class="actions">
              <button @click="editProducto(producto)" class="edit-button">
                <i class="fas fa-edit">Modificar</i>
              </button>
              <button @click="confirmDelete(producto.id)" class="delete-button">
                <i class="fas fa-trash">Eliminar</i>
              </button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <hr v-if="!loading" style="margin: 30px 0;">

    <!-- Productos cancelados -->
    <div class="ranking-card" v-if="!loading">
      <h2>Productos Más Cancelados</h2>
      <div v-if="loadingProductosCancelados" class="loading-overlay-inline">
        <div class="spinner-small"></div>
        <p>Cargando datos...</p>
      </div>
      <div v-if="productosCancelados.length > 0" class="table-responsive">
        <table class="ranking-table">
          <thead>
            <tr>
              <th>ID Producto</th>
              <th>Nombre</th>
              <th>Cancelaciones</th>
              <th>Porcentaje</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in productosCancelados" :key="'cancelados-' + item.productoId">
              <td>{{ index + 1 }}</td>
              <td>{{ item.nombre_producto }}</td>
              <td>{{ item.veces_cancelado }}</td>
              <td>{{ item.porcentaje_cancelaciones }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <p v-if="productosCancelados.length === 0 && !loadingProductosCancelados">
        No hay datos de productos cancelados.
      </p>
    </div>




    <hr v-if="!loading" style="margin: 30px 0;">

    <!-- Productos devueltos -->
    <div class="ranking-card" v-if="!loading">
      <h2>Productos Más Devueltos</h2>
      <div v-if="loadingProductosDevueltos" class="loading-overlay-inline">
        <div class="spinner-small"></div>
        <p>Cargando datos...</p>
      </div>
      <div v-if="productosDevueltos.length > 0" class="table-responsive">
        <table class="ranking-table">
          <thead>
            <tr>
              <th>ID Producto</th>
              <th>Nombre</th>
              <th>Devoluciones</th>
              <th>Porcentaje</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in productosDevueltos" :key="'devueltos-' + item.productoId">
              <td>{{ index + 1 }}</td>
              <td>{{ item.nombre_producto }}</td>
              <td>{{ item.veces_devuelto }}</td>
              <td>{{ item.procentajeDevoluciones }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <p v-if="productosDevueltos.length === 0 && !loadingProductosDevueltos">
        No hay datos de productos devueltos.
      </p>
    </div>





    <hr v-if="!loading" style="margin: 30px 0;">
    <div class="productos-mas-pedidos-section" v-if="!loading">
      <h2>Productos Más Pedidos (Último Mes)</h2>
      <div v-if="loadingProductosMasPedidos" class="loading-overlay-inline">
        <div class="spinner-small"></div>
        <p>Cargando datos...</p>
      </div>
      <div v-if="productosMasPedidosPorCategoria.length > 0" class="table-responsive">
        <table class="mas-pedidos-table">
          <thead>
            <tr>
              <th>Lista</th>
              <th>Nombre</th>
              <th>Número de Pedidos</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="(item, index) in productosMasPedidosPorCategoria" :key="item.producto">
              <td>{{ index + 1 }}</td>
              <td>{{ item.producto }}</td>
              <td>{{ item.total_pedidos }}</td>
            </tr>
          </tbody>
        </table>
      </div>
      <p v-if="productosMasPedidosPorCategoria.length === 0 && !loadingProductosMasPedidos">
        No hay datos de productos pedidos en el último mes.
      </p>
      <p v-if="errorProductosMasPedidos" class="error-message">
        Error al cargar los productos más pedidos.
      </p>
    </div>

    <div v-if="showModal" class="modal-overlay">
      <div class="modal-content">
        <h3>{{ isEditing ? 'Editar Producto' : 'Nuevo Producto' }}</h3>
        <form @submit.prevent="isEditing ? updateProducto() : createProducto()">
          <div class="form-group">
            <label>Nombre:</label>
            <input v-model="form.nombre" type="text" required>
          </div>
          <div class="form-group">
            <label>Precio:</label>
            <input v-model="form.precio" type="number" required>
          </div>
          <div class="form-group">
            <label>Stock:</label>
            <input v-model="form.stock" type="number" required>
          </div>
          <div class="form-group">
            <label>Requiere Receta:</label>
            <input v-model="form.requiere_receta" type="checkbox">
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
        <h3>¿Eliminar producto?</h3>
        <p>Esta acción no se puede deshacer.</p>
        <div class="modal-actions">
          <button @click="showDeleteModal = false" class="cancel-button">Cancelar</button>
          <button @click="deleteProducto" class="delete-button">Eliminar</button>
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
const productos = ref([])
const showModal = ref(false)
const showDeleteModal = ref(false)
const isEditing = ref(false)
const currentProductoId = ref(null)
const loading = ref(true)

const form = ref({
  nombre: '',
  precio: '',
  stock: '',
  requiere_receta: false
})

// Nuevo ref para los productos más pedidos
const productosMasPedidosPorCategoria = ref([])
const loadingProductosMasPedidos = ref(false)
const errorProductosMasPedidos = ref(null)
const productosCancelados = ref([])
const loadingProductosCancelados = ref(false)
const productosDevueltos = ref([])
const loadingProductosDevueltos = ref(false)

// Función para obtener productos cancelados
const fetchProductosCancelados = async () => {
  loadingProductosCancelados.value = true
  try {
    const response = await api.get('/productos/top-productos-cancelados')
    productosCancelados.value = response.data
  } catch (error) {
    console.error('Error al obtener productos cancelados:', error)
  } finally {
    loadingProductosCancelados.value = false
  }
}

// Función para obtener productos devueltos
const fetchProductosDevueltos = async () => {
  loadingProductosDevueltos.value = true
  try {
    const response = await api.get('/productos/top-productos-devueltos')
    productosDevueltos.value = response.data
  } catch (error) {
    console.error('Error al obtener productos devueltos:', error)
  } finally {
    loadingProductosDevueltos.value = false
  }
}

// Función para obtener los productos más pedidos en el último mes
const fetchProductosMasPedidosPorCategoria = async () => {
  loadingProductosMasPedidos.value = true
  productosMasPedidosPorCategoria.value = []
  errorProductosMasPedidos.value = null
  try {
    const response = await api.get('/detalle_pedidos/productos-mas-pedidos-mes')
    productosMasPedidosPorCategoria.value = response.data
  } catch (error) {
    console.error('Error al obtener productos más pedidos:', error)
    errorProductosMasPedidos.value = 'Error al cargar los productos más pedidos.'
  } finally {
    loadingProductosMasPedidos.value = false
  }
}

// Obtener todos los productos
const fetchProductos = async () => {
  try {
    const response = await api.get('/productos')
    productos.value = response.data
  } catch (error) {
    console.error('Detalles del error:', error)
  } finally {
    loading.value = false
  }
}

// Crear nuevo producto
const createProducto = async () => {
  try {
    await api.post('/productos', form.value)
    await fetchProductos()
    closeModal()
    alert('Producto creado exitosamente')
  } catch (error) {
    console.error('Error al crear producto:', error)
    alert('Error al crear producto')
  }
}

// Editar producto
const editProducto = (producto) => {
  form.value = {
    nombre: producto.nombre,
    precio: producto.precio,
    stock: producto.stock,
    requiere_receta: producto.requiere_receta
  }
  currentProductoId.value = producto.id
  isEditing.value = true
  showModal.value = true
}

// Actualizar producto
const updateProducto = async () => {
  try {
    await api.put(`/productos/${currentProductoId.value}`, form.value)
    await fetchProductos()
    closeModal()
    alert('Producto actualizado exitosamente')
  } catch (error) {
    console.error('Error al actualizar producto:', error)
    alert('Error al actualizar producto')
  }
}

// Confirmar eliminación
const confirmDelete = (id) => {
  currentProductoId.value = id
  showDeleteModal.value = true
}

// Eliminar producto
const deleteProducto = async () => {
  try {
    await api.delete(`/productos/${currentProductoId.value}`)
    await fetchProductos()
    showDeleteModal.value = false
    alert('Producto eliminado exitosamente')
  } catch (error) {
    console.error('Error al eliminar producto:', error)
    alert('Error al eliminar producto')
  }
}

// Cerrar modal
const closeModal = () => {
  showModal.value = false
  isEditing.value = false
  form.value = { nombre: '', precio: '', stock: '', requiere_receta: false }
  currentProductoId.value = null
}

// Cargar productos al montar el componente
onMounted(async () => {
  await fetchProductos()
  await fetchProductosMasPedidosPorCategoria()
  await fetchProductosCancelados()
  await fetchProductosDevueltos()
})
</script>

<style scoped>
.productos-container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

.productos-container h1 {
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

/* Botón para agregar producto */
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

/* Tabla de productos */
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

.productos-mas-pedidos-section {
  margin-top: 30px;
  padding: 20px;
  border: 1px solid #eee;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.productos-mas-pedidos-section h2 {
  margin-top: 0;
  margin-bottom: 20px;
  text-align: center;
}

.mas-pedidos-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 15px;
}

.mas-pedidos-table th,
.mas-pedidos-table td {
  padding: 10px 15px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

.mas-pedidos-table thead th {
  background-color: #f8f8f8;
  font-weight: bold;
}

.mas-pedidos-table tbody tr:nth-child(even) {
  background-color: #f9f9f9;
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

.rankings-section {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(350px, 1fr));
  gap: 20px;
  margin-top: 40px;
}

.ranking-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  padding: 20px;
  transition: transform 0.3s ease;
}

.ranking-card:hover {
  transform: translateY(-5px);
}

.ranking-card h2 {
  margin-top: 0;
  margin-bottom: 15px;
  font-size: 1.3rem;
  color: #1a237e;
  text-align: center;
}

.ranking-table {
  width: 100%;
  border-collapse: collapse;
}

.ranking-table th,
.ranking-table td {
  padding: 10px;
  text-align: left;
  border-bottom: 1px solid #eee;
}

.ranking-table th {
  background-color: #f8f9fa;
  font-weight: 600;
}

.ranking-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.action-buttons {
  display: flex;
  gap: 15px;
  margin-bottom: 20px;
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
</style>