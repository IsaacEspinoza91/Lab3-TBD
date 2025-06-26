import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { useRouter } from 'vue-router'
import api from '../api'

export const useAuthStore = defineStore('auth2', () => {
    const router = useRouter()
    const token = ref(localStorage.getItem('token') || null)
    const user = ref(JSON.parse(localStorage.getItem('user')) || {
        email: null,
        tipoUsuario: null,
        nombreUsuario: null,
        idUsuario: null
    })

    const isAuthenticated = computed(() => !!token.value)
    const userRole = computed(() => user.value?.tipoUsuario || null)
    const userId = computed(() => user.value?.idUsuario || null)
    const userName = computed(() => user.value?.nombreUsuario || null)


    async function login(credentials) {
        try {
            const response = await api.post('/auth/login', credentials)

            if (!response.data) {
                throw new Error('No se recibieron datos del servidor')
            }

            // Actualizar estado local
            token.value = response.data.token
            user.value = {
                email: credentials.email,
                tipoUsuario: response.data.tipoUsuario,
                nombreUsuario: response.data.nombreUsuario,
                idUsuario: response.data.idUsuario
            }

            // Persistir en localStorage
            localStorage.setItem('token', response.data.token)
            localStorage.setItem('user', JSON.stringify(user.value))

            // Redirigir según el rol
            redirectAfterLogin(response.data.tipoUsuario)

            return { success: true }
        } catch (error) {
            console.error('Error en login:', error)
            let errorMessage = 'Error al iniciar sesión'

            if (error.response) {
                // El servidor respondió con un código de error
                errorMessage = error.response.data?.message ||
                    error.response.statusText ||
                    'Credenciales inválidas'
            } else if (error.request) {
                // La petición fue hecha pero no hubo respuesta
                errorMessage = 'No se recibió respuesta del servidor'
            }

            throw new Error(errorMessage)
        }
    }

    async function register(userData) {
        try {
            // Preparar los datos para enviar al backend
            const registrationData = {
                rut: userData.rut,
                nombre: userData.nombre,
                apellido: userData.apellido,
                email: userData.email,
                password: userData.password,
                telefono: userData.telefono || '', // Agrega campo teléfono si es necesario
                tipo: userData.tipo.toUpperCase(), // Asegurar mayúsculas
                lat: userData.lat,
                lng: userData.lng
            };

            const response = await api.post('/auth/register', registrationData);

            if (!response.data) {
                throw new Error('No se recibieron datos del servidor');
            }

            // await login({ email: userData.email, password: userData.password }); // en caso de auto login despues del registro

            return {
                success: true,
                message: 'Registro exitoso. Por favor inicia sesión.'
            };
        } catch (error) {
            console.error('Error en registro:', error);
            let errorMessage = 'Error en el registro';

            if (error.response) {
                errorMessage = error.response.data?.message ||
                    error.response.statusText ||
                    'Error al registrar usuario';
            } else if (error.request) {
                errorMessage = 'No se recibió respuesta del servidor';
            }

            return {
                success: false,
                message: errorMessage
            };
        }
    }

    function redirectAfterLogin(role) {
        // Normalizar el rol a mayúsculas para evitar problemas
        const normalizedRole = role?.toUpperCase()

        switch (normalizedRole) {
            case 'ADMIN':
                router.push('/admin')
                break
            case 'CLIENTE':
                router.push('/client')
                break
            default:
                router.push('/home2')
        }
    }

    function logout() {
        token.value = null
        user.value = {
            email: null,
            tipoUsuario: null,
            nombreUsuario: null,
            idUsuario: null
        }
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        router.push('/home2')
    }

    return {
        token,
        user,
        isAuthenticated,
        userRole,
        userId,
        userName,
        login,
        logout,
        register
    }
})