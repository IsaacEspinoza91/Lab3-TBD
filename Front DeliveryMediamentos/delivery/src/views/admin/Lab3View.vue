<template>
  <div class="lab3-dashboard">
    <h1>Consultas del Laboratorio 3</h1>

    <div class="consultas-section">
      <h2>Consultas del Sistema - Lab3</h2>
      <div class="consulta-selector">
        <select v-model="consultaSeleccionada" @change="manejarCambioConsulta" class="consulta-dropdown">
          <option value="" disabled selected>Seleccione una consulta...</option>
          <option value="1">Consulta N°1: Obtener el promedio de puntuación por empresa o farmacia.</option>
          <option value="2">Consulta N°2: Listar las opiniones que contengan palabras clave como 'demora' o 'error'.</option>
          <option value="3">Consulta N°3: Contar cuántos pedidos tienen más de 3 cambios de estado en menos de 10 minutos.</option>
          <option value="4">Consulta N°4: Analizar las rutas más frecuentes de repartidores en los últimos 7 días.</option>
          <option value="5">Consulta N°5: Detectar clientes que realizaron búsquedas sin concretar pedidos (navegación sin compra).</option>
          <option value="6">Consulta N°6: Agrupar opiniones por hora del día para analizar patrones de satisfacción.</option>
        </select>

        <!-- Campo de entrada para la Consulta N°2 (palabra clave) -->
        <div v-if="consultaSeleccionada === '2'" class="input-parametro">
          <label for="palabraClaveOpinion">Palabra clave:</label>
          <input
            id="palabraClaveOpinion"
            type="text"
            v-model="palabraClaveOpinion"
            placeholder="Ej: demora, error"
            class="consulta-input"
            @keyup.enter="ejecutarConsulta"
          />
        </div>

        <div v-if="consultaCargando" class="loading-indicator">
          <div class="loader"></div>
          <span>Ejecutando consulta...</span>
        </div>

        <div v-if="resultadoConsulta !== null" class="resultado-consulta">
          <h3>Resultado:</h3>

          <!-- Display para la Consulta N°1: Promedio de puntuación por empresa o farmacia -->
          <div v-if="consultaSeleccionada === '1' && Array.isArray(resultadoConsulta)">
            <table class="resultado-table">
              <thead>
                <tr>
                  <th>ID Empresa/Farmacia</th>
                  <th>Promedio de Puntuación</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in resultadoConsulta" :key="index">
                  <td>{{ item.empresaId }}</td>
                  <td>{{ parseFloat(item.promedioPuntuacion).toFixed(2) }}</td>
                </tr>
              </tbody>
            </table>
            <p v-if="resultadoConsulta.length === 0" class="no-results-message">No se encontraron promedios de puntuación.</p>
          </div>

          <!-- Display para la Consulta N°2: Listar opiniones por palabras clave -->
          <div v-else-if="consultaSeleccionada === '2' && Array.isArray(resultadoConsulta)">
            <table class="resultado-table">
              <thead>
                <tr>
                  <th>ID Opinión</th>
                  <th>ID Cliente</th>
                  <th>ID Empresa</th>
                  <th>Comentario</th>
                  <th>Puntuación</th>
                  <th>Fecha</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in resultadoConsulta" :key="index">
                  <td>{{ item.id }}</td>
                  <td>{{ item.clienteId }}</td>
                  <td>{{ item.empresaId }}</td>
                  <td>{{ item.comentario }}</td>
                  <td>{{ item.puntuacion }}</td>
                  <td>{{ item.fecha }}</td>
                </tr>
              </tbody>
            </table>
            <p v-if="resultadoConsulta.length === 0" class="no-results-message">No se encontraron opiniones con las palabras clave especificadas.</p>
          </div>

          <!-- Display para la Consulta N°3: Conteo de cambios rápidos -->
          <div v-else-if="consultaSeleccionada === '3' && Array.isArray(resultadoConsulta)">
            <p>Cantidad de logs cambiantes registrados: {{ resultadoConsulta.length }}</p>

            <table class="resultado-table">
              <thead>
              <tr>
                <th>ID Pedido</th>
                <th>Acciones</th>
              </tr>
              </thead>
              <tbody>
              <tr v-for="(item, index) in resultadoConsulta" :key="index">
                <td>{{ item.idpedido }}</td>
                <td>
                  <button @click="consultarPedido(item.idpedido)">Consultar</button>
                </td>
              </tr>
              </tbody>
            </table>

            <!-- sub tabla -->
            <div v-if="logsPedidoSeleccionado.length > 0" class="logs-pedido">
              <h4>Logs del Pedido ID: {{ pedidoActual }}</h4>
              <table class="resultado-table">
                <thead>
                <tr>
                  <th>Estado</th>
                  <th>Timestamp</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(log, index) in logsPedidoSeleccionado" :key="index">
                  <td>{{ log.estado }}</td>
                  <td>{{ log.timestamp }}</td>
                </tr>
                </tbody>
              </table>
            </div>

            <p v-if="resultadoConsulta.length === 0" class="no-results-message">
              No se encontraron pedidos.
            </p>
          </div>

          <!-- Display para la Consulta N°4: Rutas más frecuentes de repartidores -->
          <div v-else-if="consultaSeleccionada === '4' && Array.isArray(resultadoConsulta)">
            <table class="resultado-table">
              <thead>
                <tr>
                  <th>ID Repartidor</th>
                  <th>Ruta (Ej. ID Ruta/Detalle)</th>
                  <th>Frecuencia</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in resultadoConsulta" :key="index">
                  <td>{{ item.repartidorId }}</td>
                  <td>{{ item.ruta }}</td>
                  <td>{{ item.frecuencia }}</td>
                </tr>
              </tbody>
            </table>
            <p v-if="resultadoConsulta.length === 0" class="no-results-message">No se encontraron rutas frecuentes.</p>
          </div>

          <!-- Display para la Consulta N°5: Clientes sin compra tras búsqueda -->
          <div v-else-if="consultaSeleccionada === '5' && Array.isArray(resultadoConsulta)">
            <table class="resultado-table">
              <thead>
                <tr>
                  <th>ID Cliente</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(cliente, index) in resultadoConsulta" :key="index">
                  <td>{{ cliente.clienteId }}</td>
                </tr>
              </tbody>
            </table>
            <p v-if="resultadoConsulta.length === 0" class="no-results-message">No se encontraron clientes que cumplan esta condición.</p>
          </div>

          <!-- Display para la Consulta N°6: Opiniones agrupadas por hora del día -->
          <div v-else-if="consultaSeleccionada === '6' && Array.isArray(resultadoConsulta)">
            <table class="resultado-table">
              <thead>
                <tr>
                  <th>Hora del Día</th>
                  <th>Promedio de Puntuación</th>
                  <th>Total de Opiniones</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="(item, index) in resultadoConsulta" :key="index">
                  <td>{{ item.hora }}</td> 
                  <td>{{ parseFloat(item.promedioPuntuacion).toFixed(2) }}</td>
                  <td>{{ item.totalOpiniones }}</td>
                </tr>
              </tbody>
            </table>
            <p v-if="resultadoConsulta.length === 0" class="no-results-message">No se encontraron patrones de satisfacción por hora.</p>
          </div>

          <pre v-else>{{ JSON.stringify(resultadoConsulta, null, 2) }}</pre>
        </div>

        <div v-if="errorConsulta" class="error-consulta">
          <p class="error-message">{{ errorConsulta }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import api from '@/api';

const consultaSeleccionada = ref('');
const consultaCargando = ref(false);
const resultadoConsulta = ref(null);
const errorConsulta = ref(null);
const palabraClaveOpinion = ref('');
const logsPedidoSeleccionado = ref([]);
const pedidoActual = ref(null);

const manejarCambioConsulta = () => {
  resultadoConsulta.value = null;
  errorConsulta.value = null;
  if (consultaSeleccionada.value === '2') {
  } else {
    ejecutarConsulta();
  }
};

const ejecutarConsulta = async () => {
  if (!consultaSeleccionada.value) return;

  try {
    consultaCargando.value = true;
    resultadoConsulta.value = null;
    errorConsulta.value = null;

    let response;

    switch (consultaSeleccionada.value) {
      case '1':
        response = await api.get('/opiniones_clientes/promedio_puntuacion_por_empresa');
        break;
      case '2':
        if (!palabraClaveOpinion.value) {
          errorConsulta.value = 'Debe ingresar una palabra clave (ej: demora, error) para la Consulta N°2.';
          consultaCargando.value = false;
          return;
        }
        response = await api.get(`/opiniones_clientes/buscar/${palabraClaveOpinion.value}`);
        break;
      case '3':
        response = await api.get('/logs-pedidos/pedidos-cambiantes');
        break;
      case '4': //falta
        response = await api.get('/repartidores/rutas-frecuentes');
        break;
      case '5':
        response = await api.get('/navegacion_usuarios/clientes-sin-compra');
        break;
      case '6':
        response = await api.get('/opiniones_clientes/por_hora');
        break;
      default:
        errorConsulta.value = 'Consulta no implementada para Lab3.';
        consultaCargando.value = false;
        return;
    }

    resultadoConsulta.value = response.data;

  } catch (error) {
    console.error('Error al ejecutar consulta del Lab3:', error);
    errorConsulta.value = error.response?.data?.message || 'Error al ejecutar la consulta.';
  } finally {
    consultaCargando.value = false;
  }
};

const consultarPedido = async (idpedido) => {
  try {
    const response = await api.get(`/logs_pedidos/pedido/${idpedido}`);
    logsPedidoSeleccionado.value = response.data;
    pedidoActual.value = idpedido;
  } catch (error) {
    console.error(`Error al consultar logs_pedidos ${idpedido}:`, error);
    errorConsulta.value = error.response?.data?.message || 'Error al consultar logs del pedido.';
  }
};

</script>

<style scoped>
.lab3-dashboard {
  padding: 2rem;
  background-color: #f0f2f5;
  min-height: 90vh;
  font-family: 'Inter', sans-serif;
  color: #333;
}

h1 {
  color: #1a237e;
  margin-bottom: 2rem;
  text-align: center;
  font-size: 2.5rem;
  font-weight: 700;
}

.consultas-section {
  margin-top: 3rem;
  padding: 1.5rem;
  background: white;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.consultas-section h2 {
  color: #1a237e;
  margin-bottom: 1.5rem;
  font-size: 1.8rem;
  font-weight: 600;
  text-align: center;
}

.consulta-dropdown {
  width: 100%;
  padding: 0.9rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 1rem;
  margin-bottom: 1.5rem;
  background-color: #f8f8f8;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.consulta-dropdown:focus {
  outline: none;
  border-color: #1a237e;
  box-shadow: 0 0 0 3px rgba(26, 35, 126, 0.2);
}

.resultado-consulta {
  margin-top: 2rem;
  padding: 1.8rem;
  background-color: #fbfbfb;
  border-radius: 10px;
  max-height: 70vh;
  overflow-y: auto;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.08);
  border: 1px solid #e5e5e5;
}

.resultado-consulta h3 {
  color: #1a237e;
  margin-bottom: 1rem;
  font-size: 1.5rem;
  font-weight: 600;
}

.resultado-consulta p {
  font-size: 1.1rem;
  line-height: 1.6;
  color: #444;
}

.resultado-consulta strong {
  font-weight: 700;
  color: #3f51b5;
}

.resultado-consulta pre {
  white-space: pre-wrap;
  word-wrap: break-word;
  font-family: 'Courier New', Courier, monospace;
  font-size: 0.9rem;
  line-height: 1.5;
  padding: 1rem;
  background-color: #eceff1;
  border-radius: 8px;
  border: 1px solid #cfd8dc;
  max-height: 50vh;
  overflow-y: auto;
}

.resultado-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 1.5rem;
  font-size: 0.95rem;
}

.resultado-table th,
.resultado-table td {
  padding: 1rem;
  text-align: left;
  border-bottom: 1px solid #e0e0e0;
}

.resultado-table th {
  background-color: #1a237e;
  color: white;
  font-weight: 600;
  position: sticky;
  top: 0;
  z-index: 1;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
}

.resultado-table tbody tr:nth-child(even) {
  background-color: #f9f9f9;
}

.resultado-table tbody tr:hover {
  background-color: #e8eaf6;
  cursor: pointer;
}

.no-results-message {
  text-align: center;
  color: #777;
  font-style: italic;
  margin-top: 1.5rem;
}

.error-consulta {
  margin-top: 2rem;
  padding: 1.5rem;
  background-color: #ffebee;
  border-radius: 8px;
  color: #c62828;
  border: 1px solid #ef9a9a;
  font-weight: 500;
}

.error-message {
  margin: 0;
}

.loading-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 0.8rem;
  margin: 2rem 0;
  font-size: 1.1rem;
  color: #555;
}

.loader {
  border: 4px solid #f3f3f3;
  border-top: 4px solid #1a237e;
  border-radius: 50%;
  width: 30px;
  height: 30px;
  animation: spin 1s linear infinite;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

/* Scrollbar personalizada */
.resultado-consulta::-webkit-scrollbar {
  width: 10px;
}
.resultado-consulta::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 5px;
}
.resultado-consulta::-webkit-scrollbar-thumb {
  background: #bdbdbd;
  border-radius: 5px;
}
.resultado-consulta::-webkit-scrollbar-thumb:hover {
  background: #9e9e9e;
}

/* Estilos específicos para el nuevo input de parámetro */
.input-parametro {
  margin-bottom: 1.5rem;
}

.input-parametro label {
  display: block;
  margin-bottom: 0.5rem;
  font-weight: 600;
  color: #333;
}

.consulta-input {
  width: 100%;
  padding: 0.9rem;
  border: 1px solid #ddd;
  border-radius: 8px;
  font-size: 1rem;
  box-shadow: inset 0 1px 3px rgba(0, 0, 0, 0.05);
  transition: all 0.3s ease;
}

.consulta-input:focus {
  outline: none;
  border-color: #1a237e;
  box-shadow: 0 0 0 3px rgba(26, 35, 126, 0.2);
}
</style>
