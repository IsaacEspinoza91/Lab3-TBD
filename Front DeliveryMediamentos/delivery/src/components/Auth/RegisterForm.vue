<template>
    <div class="register-container">
        <div class="register-box">
            <div class="register-header">
                <h2 class="text-center mb-4">
                    <i class="bi bi-person-plus me-2"></i>
                    Registro
                </h2>
            </div>

            <form @submit.prevent="handleSubmit" class="register-form">
                <div class="form-group">
                    <label for="rut" class="form-label">
                        <i class="bi bi-card-text me-2"></i>
                        RUT
                    </label>
                    <input type="text" class="form-control" id="rut" v-model="rut" 
                        placeholder="Ej: 12345678-9" required>
                </div>

                <div class="form-group">
                    <label for="nombre" class="form-label">
                        <i class="bi bi-person me-2"></i>
                        Nombre
                    </label>
                    <input type="text" class="form-control" id="nombre" v-model="nombre" 
                        placeholder="Ingrese su nombre" required>
                </div>

                <div class="form-group">
                    <label for="apellido" class="form-label">
                        <i class="bi bi-person me-2"></i>
                        Apellido
                    </label>
                    <input type="text" class="form-control" id="apellido" v-model="apellido"
                        placeholder="Ingrese su apellido" required>
                </div>

                <div class="form-group">
                    <label for="email" class="form-label">
                        <i class="bi bi-envelope me-2"></i>
                        Email
                    </label>
                    <input type="email" class="form-control" id="email" v-model="email" 
                        placeholder="Ingrese su email" required>
                </div>

                <div class="form-group">
                    <label for="password" class="form-label">
                        <i class="bi bi-lock me-2"></i>
                        Contraseña
                    </label>
                    <input type="password" class="form-control" id="password" v-model="password"
                        placeholder="Cree una contraseña (mínimo 4 caracteres)" required>
                </div>

                <div class="form-group">
                    <label for="telefono" class="form-label">
                        <i class="bi bi-telephone me-2"></i>
                        Teléfono
                    </label>
                    <input type="tel" class="form-control" id="telefono" v-model="telefono"
                        placeholder="+56912345678" required>
                </div>

                <div class="form-group">
                    <label for="tipo" class="form-label">
                        <i class="bi bi-person-rolodex me-2"></i>
                        Tipo de Usuario
                    </label>
                    <select class="form-select" id="tipo" v-model="tipo" required>
                        <option value="CLIENTE">Cliente</option>
                        <option value="ADMIN">Administrador</option>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label">
                        <i class="bi bi-geo-alt me-2"></i>
                        Seleccione su ubicación
                    </label>
                    <l-map
                        ref="mapRef"
                        class="map-container"
                        style="height: 300px"
                        :zoom="13"
                        :center="mapCenter"
                        @click="onMapClick"
                    >
                        <l-tile-layer
                            url="https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png"
                        />
                        <l-marker :lat-lng="ubicacion" v-if="ubicacion" />
                    </l-map>
                    <small v-if="ubicacion" class="text-muted">
                        Ubicación seleccionada: Lat: {{ ubicacion.lat }}, Lng: {{ ubicacion.lng }}
                    </small>
                </div>

                <button type="submit" class="btn btn-primary register-btn" :disabled="loading">
                    <i class="bi bi-person-plus me-2"></i>
                    <span v-if="loading">Registrando...</span>
                    <span v-else>Registrarse</span>
                </button>
            </form>

            <div v-if="message" class="alert mt-3" :class="{ 
                'alert-success': success, 
                'alert-danger': !success 
            }">
                {{ message }}
            </div>

            <div class="register-footer">
                <router-link to="/login2" class="login-link">
                    <i class="bi bi-box-arrow-in-right me-1"></i>
                    ¿Ya tienes cuenta? Inicia sesión
                </router-link>
            </div>
        </div>
    </div>
</template>

<script>
import "leaflet/dist/leaflet.css"
import "../utils/leaflet-config" 
import { LMap, LTileLayer, LMarker } from "@vue-leaflet/vue-leaflet"
import { useAuthStore } from '../../stores/auth2'
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'

export default {
    components: {
        LMap,
        LTileLayer,
        LMarker
    },
    setup() {
        const authStore = useAuthStore()
        const router = useRouter()

        // Datos del formulario
        const rut = ref('')
        const nombre = ref('')
        const apellido = ref('')
        const email = ref('')
        const password = ref('')
        const telefono = ref('')
        const tipo = ref('CLIENTE')
        const mapCenter = ref({ lat: -33.450469, lng: -70.680136 })
        const ubicacion = ref(null)
        
        // Estado del formulario
        const loading = ref(false)
        const message = ref('')
        const success = ref(false)

        // Manejadores
        const onMapClick = (e) => {
            ubicacion.value = {
                lat: parseFloat(e.latlng.lat.toFixed(6)),
                lng: parseFloat(e.latlng.lng.toFixed(6))
            }
        }

        const handleSubmit = async () => {
            try {
                loading.value = true
                message.value = ''
                
                // Validaciones
                if (!ubicacion.value) {
                    throw new Error('Debe seleccionar una ubicación en el mapa')
                }

                if (password.value.length < 4) {
                    throw new Error('La contraseña debe tener al menos 4 caracteres')
                }

                // Enviar datos al backend
                const result = await authStore.register({
                    rut: rut.value,
                    nombre: nombre.value,
                    apellido: apellido.value,
                    email: email.value,
                    password: password.value,
                    telefono: telefono.value,
                    tipo: tipo.value,
                    lat: ubicacion.value.lat,
                    lng: ubicacion.value.lng
                })

                // Manejar respuesta
                success.value = result.success
                message.value = result.message

                if (result.success) {
                    // Limpiar formulario
                    rut.value = ''
                    nombre.value = ''
                    apellido.value = ''
                    email.value = ''
                    password.value = ''
                    telefono.value = ''
                    ubicacion.value = null
                    
                    // Redirigir después de 2 segundos
                    setTimeout(() => router.push('/login2'), 2000)
                }
            } catch (err) {
                success.value = false
                message.value = err.message || 'Error en el registro'
                console.error('Error en registro:', err)
            } finally {
                loading.value = false
            }
        }

        // Asegurar que el mapa se renderice correctamente
        const mapRef = ref(null)
        onMounted(() => {
            if (mapRef.value?.leafletObject) {
                mapRef.value.leafletObject.invalidateSize()
            }
        })

        return {
            rut, nombre, apellido, email, password, telefono, tipo,
            loading, message, success, handleSubmit,
            mapCenter, ubicacion, onMapClick, mapRef
        }
    }
}
</script>


<style scoped>
.register-container {
    background-size: cover;
    background-position: center;
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 20px;
}

.register-box {
    background-color: rgba(255, 255, 255, 0.85);
    backdrop-filter: blur(5px);
    border-radius: 10px;
    padding: 2rem;
    width: 100%;
    max-width: 500px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
} 

.register-header {
    margin-bottom: 1.5rem;
    text-align: center;
}

.register-header h2 {
    color: #2c3e50;
    font-weight: 600;
}

.register-form {
    margin-bottom: 1.5rem;
}

.form-group {
    margin-bottom: 1.2rem;
}

.form-label {
    display: block;
    margin-bottom: 0.5rem;
    color: #495057;
    font-weight: 500;
}

.form-control,
.form-select {
    width: 100%;
    padding: 0.75rem;
    border: 1px solid #ced4da;
    border-radius: 5px;
    font-size: 1rem;
    transition: border-color 0.3s;
}

.form-control:focus,
.form-select:focus {
    border-color: #3498db;
    outline: none;
    box-shadow: 0 0 0 0.2rem rgba(52, 152, 219, 0.25);
}

.register-btn {
    width: 100%;
    padding: 0.75rem;
    border-radius: 5px;
    font-size: 1rem;
    font-weight: 500;
    transition: all 0.3s;
    margin-top: 0.5rem;
}

.register-btn:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

.register-footer {
    text-align: center;
    padding-top: 1rem;
    border-top: 1px solid #eee;
    margin-top: 1rem;
}

.login-link {
    color: #3498db;
    text-decoration: none;
    transition: color 0.3s;
}

.login-link:hover {
    color: #2980b9;
    text-decoration: underline;
}

.alert {
    padding: 0.75rem;
    border-radius: 5px;
    margin-bottom: 1rem;
}

.alert-success {
    background-color: #d4edda;
    color: #155724;
    border: 1px solid #c3e6cb;
}

.alert-danger {
    background-color: #f8d7da;
    color: #721c24;
    border: 1px solid #f5c6cb;
}

.map-container {
    height: 300px;
    width: 100%;
}

/* Iconos */
.bi {
    font-size: 1.1rem;
}
</style>