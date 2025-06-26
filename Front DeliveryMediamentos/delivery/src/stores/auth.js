import { defineStore } from 'pinia'
import { ref } from 'vue'
import router from '@/router'

export const useAuthStore = defineStore('auth', () => {
    const token = ref(null)
    const isAuthenticated = ref(false)
    const userType = ref(null) // Agregamos tipo de usuario

    function setToken(newToken, type) {
        token.value = newToken
        isAuthenticated.value = true
        userType.value = type
        localStorage.setItem('token', newToken)
        localStorage.setItem('userType', type)
    }

    function clearToken() {
        token.value = null
        isAuthenticated.value = false
        userType.value = null
        localStorage.removeItem('token')
        localStorage.removeItem('userType')
    }

    function initialize() {
        const savedToken = localStorage.getItem('token')
        const savedType = localStorage.getItem('userType')
        if (savedToken) {
            token.value = savedToken
            isAuthenticated.value = true
            userType.value = savedType
        }
    }

    return { token, isAuthenticated, userType, setToken, clearToken, initialize }
})