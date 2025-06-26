<template>
    <nav class="navbar">
        <router-link to="/" class="navbar-brand">
            <span class="logo-text">Delivery de medicamentos</span>
        </router-link>

        <div class="navbar-actions">
            <template v-if="!authStore.isAuthenticated">
                <router-link to="/login" class="nav-button">Iniciar Sesión</router-link>
                <router-link to="/register" class="nav-button">Registrarse</router-link>
            </template>
            <button v-else @click="logout" class="nav-button">Cerrar Sesión</button>
        </div>
    </nav>
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'

const authStore = useAuthStore()
const router = useRouter()

const logout = () => {
    authStore.clearToken()
    router.push('/')
}
</script>

<style scoped>
.navbar {
    background-color: #1a237e;
    padding: 1rem 2rem;
    display: flex;
    justify-content: space-between;
    align-items: center;
    box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    z-index: 1000;
    background-color: var(--nav-bg, #1a237e);
}

.navbar-brand {
    color: white;
    font-size: 1.5rem;
    font-weight: bold;
    text-decoration: none;
    cursor: pointer;
}

.navbar-actions {
    display: flex;
    gap: 1rem;
}

.nav-button {
    padding: 0.5rem 1rem;
    background-color: transparent;
    color: white;
    border: 1px solid white;
    border-radius: 4px;
    text-decoration: none;
    transition: all 0.3s ease;
    cursor: pointer;
    font-size: 1rem;
}

.nav-button:hover {
    background-color: rgba(255, 255, 255, 0.1);
}
</style>