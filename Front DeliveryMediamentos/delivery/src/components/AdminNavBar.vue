<template>
    <nav class="admin-navbar">
        <router-link to="/admin" class="nav-link">Delivery Medicamentos</router-link>
        <div class="admin-nav-links">
            <router-link to="/admin/usuarios" class="nav-link">Usuarios</router-link>
            <router-link to="/admin/clientes" class="nav-link">Clientes</router-link>
            <router-link to="/admin/repartidores" class="nav-link">Repartidores</router-link>
            <router-link to="/admin/farmacias" class="nav-link">Farmacias</router-link>
            <router-link to="/admin/pedidos" class="nav-link">Pedidos</router-link>
            <router-link to="/admin/productos" class="nav-link">Productos</router-link>
            <router-link to="/admin/calificaciones" class="nav-link">Calificaciones</router-link>
            <button @click="logout" class="nav-button">Cerrar Sesión</button>
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
.admin-navbar {
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
    height: 60px;
}

.admin-nav-links {
    display: flex;
    gap: 1.5rem;
    align-items: center;
}

.nav-link {
    color: white;
    text-decoration: none;
    padding: 0.5rem 0;
    border-bottom: 2px solid transparent;
    transition: all 0.3s ease;
}

.nav-link:hover {
    border-bottom-color: white;
}

.nav-button {
    padding: 0.5rem 1rem;
    background-color: #d32f2f;
    color: white;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s;
}

.nav-button:hover {
    background-color: #b71c1c;
}
</style>