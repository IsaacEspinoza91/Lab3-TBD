<template>
    <div class="auth-container">
        <div class="auth-box">
            <div class="auth-header">
                <h2 class="text-center mb-4">
                    <i class="bi bi-bank me-2"></i>
                    Iniciar Sesión
                </h2>
            </div>

            <form @submit.prevent="handleSubmit" class="auth-form">
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
                        placeholder="Ingrese su contraseña" required>
                </div>

                <button type="submit" class="btn btn-primary auth-btn" :disabled="loading">
                    <i class="bi bi-box-arrow-in-right me-2"></i>
                    <span v-if="loading">Ingresando...</span>
                    <span v-else>Ingresar</span>
                </button>

                <!-- Mostrar mensaje de error si existe -->
                <div v-if="error" class="alert alert-danger mt-3">
                    {{ error }}
                </div>
            </form>

            <div class="auth-footer">
                <router-link to="/register2" class="auth-link">
                    <i class="bi bi-person-plus me-1"></i>
                    ¿No tienes cuenta? Registrarse
                </router-link>
            </div>
        </div>
    </div>
</template>

<script>
import { useAuthStore } from '../../stores/auth2'
import { ref } from 'vue'

export default {
    setup() {
        const authStore = useAuthStore()
        const email = ref('')
        const password = ref('')
        const loading = ref(false)
        const error = ref('')

        const handleSubmit = async () => {
            try {
                loading.value = true
                error.value = ''
                await authStore.login({
                    email: email.value,
                    password: password.value
                })
            } catch (err) {
                error.value = err.message || 'Error al iniciar sesión'
                console.error('Error en login:', err)
            } finally {
                loading.value = false
            }
        }

        return { email, password, loading, error, handleSubmit }
    }
}
</script>

<style scoped>
.auth-container {
    background-image: url('../../assets/login-bg.jpg');
    background-size: cover;
    background-position: center;
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 20px;
}

.auth-box {
    background-color: rgba(255, 255, 255, 0.85);
    backdrop-filter: blur(5px);
    border-radius: 10px;
    padding: 2.5rem;
    width: 100%;
    max-width: 450px;
    box-shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
    margin: auto;
    position: relative;
    top: -5%;
    /* Ajuste fino de posición vertical */
}

.auth-header {
    margin-bottom: 2rem;
    text-align: center;
}

.auth-header h2 {
    color: #2c3e50;
    font-weight: 600;
}

.auth-form {
    margin-bottom: 1.5rem;
}

.form-group {
    margin-bottom: 1.5rem;
}

.form-label {
    display: block;
    margin-bottom: 0.5rem;
    color: #495057;
    font-weight: 500;
}

.form-control {
    width: 100%;
    padding: 0.75rem;
    border: 1px solid #ced4da;
    border-radius: 5px;
    font-size: 1rem;
    transition: border-color 0.3s;
}

.auth-btn {
    width: 100%;
    padding: 0.75rem;
    border-radius: 5px;
    font-size: 1rem;
    font-weight: 500;
    transition: all 0.3s;
    margin-top: 1rem;
}

.auth-footer {
    text-align: center;
    padding-top: 1rem;
    border-top: 1px solid #eee;
    margin-top: 1.5rem;
}

.auth-link {
    color: #3498db;
    text-decoration: none;
    transition: color 0.3s;
}

.auth-link:hover {
    color: #2980b9;
    text-decoration: underline;
}

.bi {
    font-size: 1.1rem;
}
</style>