<template>
    <nav class="navbar navbar-expand-lg fixed-top">
        <div class="container-fluid">
            <router-link class="navbar-brand" to="/home2">Delivery Medicamentos</router-link>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarNav">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarNav">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item" v-if="!authStore.isAuthenticated">
                        <router-link class="nav-link" to="/login2">Iniciar Sesión</router-link>
                    </li>
                    <li class="nav-item" v-if="!authStore.isAuthenticated">
                        <router-link class="nav-link" to="/register2">Registrarse</router-link>
                    </li>
                    <li class="nav-item dropdown" v-if="authStore.isAuthenticated">
                        <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button"
                            data-bs-toggle="dropdown">
                            <img src="../../assets/user-icon.png" alt="Usuario" class="rounded-circle" width="30"
                                height="30">
                            {{ authStore.user?.nombreUsuario }}
                        </a>
                        <ul class="dropdown-menu dropdown-menu-end">
                            <li><button class="dropdown-item" @click="authStore.logout">Cerrar Sesión</button></li>
                        </ul>
                    </li>
                </ul>
            </div>
        </div>


        <!-- Modal de confirmación -->
        <div v-if="showLogoutModal" class="modal-backdrop fade show"></div>
        <div class="modal fade" :class="{ 'show d-block': showLogoutModal }">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Confirmar cierre de sesión</h5>
                        <button type="button" class="btn-close" @click="showLogoutModal = false"></button>
                    </div>
                    <div class="modal-body">
                        <p>¿Estás seguro que deseas cerrar tu sesión?</p>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" @click="showLogoutModal = false">
                            Cancelar
                        </button>
                        <button type="button" class="btn btn-danger" @click="handleLogout">
                            Cerrar Sesión
                        </button>
                    </div>
                </div>
            </div>
        </div>
    </nav>
</template>

<script>
import { useAuthStore } from '../../stores/auth2'
import { ref } from 'vue'
import { useRouter } from 'vue-router'

export default {
    setup() {
        const authStore = useAuthStore()
        const router = useRouter()
        const showLogoutModal = ref(false)

        const handleLogout = () => {
            authStore.logout()
            showLogoutModal.value = false
            router.push('/home2')
        }

        return { authStore, showLogoutModal, handleLogout }
    }
}
</script>

<style scoped>
/* Estilos base de la barra de navegación */
.navbar {
  padding: 0.8rem 2rem;
  background-color: #1a237e;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.15);
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1030;
}

.navbar-brand {
  color: white;
  font-size: 1.5rem;
  font-weight: 700;
  letter-spacing: 0.5px;
  transition: all 0.3s ease;
}

.navbar-brand:hover {
  color: #c5cae9;
  transform: translateY(-1px);
}

/* Estilos para los enlaces de navegación */
.nav-link {
  color: white !important;
  font-weight: 500;
  padding: 0.5rem 1rem !important;
  margin: 0 0.2rem;
  border-radius: 4px;
  transition: all 0.3s ease;
  position: relative;
}

/* Estilos específicos para botones de login/register */
.nav-item:not(.dropdown) .nav-link {
  background-color: rgba(255, 255, 255, 0.1);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.nav-item:not(.dropdown) .nav-link:hover {
  background-color: rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

/* Efecto especial para el botón de registro */
.nav-item:nth-child(2) .nav-link {
  background-color: #3949ab;
  border: 1px solid #5c6bc0;
}

.nav-item:nth-child(2) .nav-link:hover {
  background-color: #303f9f;
}

/* Estilos para el dropdown de usuario */
.dropdown-toggle {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.dropdown-toggle::after {
  margin-left: 0.5rem;
}

.dropdown-menu {
  border: none;
  box-shadow: 0 5px 15px rgba(0, 0, 0, 0.15);
  border-radius: 8px;
  padding: 0.5rem 0;
  margin-top: 0.5rem;
}

.dropdown-item {
  padding: 0.5rem 1.5rem;
  transition: all 0.2s;
  color: #333;
}

.dropdown-item:hover {
  background-color: #f5f5f5;
  color: #1a237e;
}

.dropdown-item:active {
  background-color: #e8eaf6;
}

/* Estilos para el modal de confirmación */
.modal-content {
  border-radius: 12px;
  overflow: hidden;
  border: none;
  box-shadow: 0 5px 20px rgba(0, 0, 0, 0.2);
}

.modal-header {
  background-color: #1a237e;
  color: white;
  border-bottom: none;
  padding: 1.2rem;
}

.modal-title {
  font-weight: 600;
}

.modal-body {
  padding: 1.5rem;
  font-size: 1.05rem;
}

.modal-footer {
  border-top: none;
  padding: 1rem 1.5rem;
  background-color: #f9f9f9;
}

.btn-secondary {
  background-color: #757de8;
  border: none;
  padding: 0.5rem 1.25rem;
}

.btn-danger {
  background-color: #f44336;
  border: none;
  padding: 0.5rem 1.25rem;
  box-shadow: 0 2px 5px rgba(244, 67, 54, 0.3);
}

/* Efecto hover para botones del modal */
.btn-secondary:hover {
  background-color: #5c6bc0;
  transform: translateY(-1px);
}

.btn-danger:hover {
  background-color: #e53935;
  transform: translateY(-1px);
}

/* Estilos responsivos */
@media (max-width: 992px) {
  .navbar-collapse {
    background-color: #1a237e;
    padding: 1rem;
    border-radius: 0 0 8px 8px;
    margin-top: 0.5rem;
  }
  
  .nav-item {
    margin: 0.3rem 0;
  }
  
  .nav-link {
    padding: 0.8rem 1rem !important;
  }
}

/* Animación para el toggler */
.navbar-toggler {
  border: none;
  padding: 0.5rem;
}

.navbar-toggler:focus {
  box-shadow: none;
}

.navbar-toggler-icon {
  background-image: url("data:image/svg+xml,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 30 30'%3e%3cpath stroke='rgba%28255, 255, 255, 0.8%29' stroke-linecap='round' stroke-miterlimit='10' stroke-width='2' d='M4 7h22M4 15h22M4 23h22'/%3e%3c/svg%3e");
  transition: transform 0.3s ease;
}

.navbar-toggler[aria-expanded="true"] .navbar-toggler-icon {
  transform: rotate(90deg);
}
</style>