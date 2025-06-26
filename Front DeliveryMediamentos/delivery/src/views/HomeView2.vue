<template>
    <div class="home-container">
        <MainNavBar />

        <div class="carousel-container">
            <div class="carousel">
                <div v-for="(image, index) in images" :key="index" class="slide"
                    :class="{ active: currentIndex === index }" :style="{ backgroundImage: `url(${image})` }">

                    <div class="welcome-text" :class="{ 'initial-animation': shouldAnimate }">
                        <h1 class="welcome-title">Bienvenido al Sistema de Gestión</h1>
                        <p class="welcome-subtitle">Delivery de Medicamentos</p>
                    </div>
                </div>
            </div>

            <div class="indicators">
                <button v-for="(image, index) in images" :key="index" @click="goToSlide(index)"
                    :class="{ active: currentIndex === index }" aria-label="Go to slide">
                </button>
            </div>
        </div>



        <div class="info-container">
            <div class="info-content">
                <div class="info-text">
                    <h2>Sistema delivery medicamentos</h2>
                    <p>
                        Permite a los usuarios administradores realizar un seguimiento
                        completo de sus el delivery de medicamentos de diferentes farmacias.
                    </p>
                    <ul class="features-list">
                        <li><i class="bi bi-check-circle"></i> Búsqueda de pedidos con estado</li>
                        <li><i class="bi bi-check-circle"></i> Rutas estimas de despacho</li>
                        <li><i class="bi bi-check-circle"></i> Administración farmacias, productos y clientes</li>
                        <li><i class="bi bi-check-circle"></i> Información general</li>
                        <li><i class="bi bi-check-circle"></i> Gestión administrativa</li>
                        <li><i class="bi bi-check-circle"></i> Asociación a sectores geográficos</li>
                    </ul>
                </div>
                <div class="info-image">
                    <img src="../assets/shipped.png" alt="Gestión de tareas" class="feature-img">
                </div>
            </div>
        </div>
    </div>
</template>


<script>
import MainNavBar from '../components/NavBars/MainNavBar.vue'
import { ref, onMounted, onBeforeUnmount } from 'vue'
import { useAuthStore } from '../stores/auth2'

import carousel1 from '../assets/to-do1.png'
import carousel2 from '../assets/to-do2.png'
import carousel3 from '../assets/to-do3.jpg'
import carousel4 from '../assets/to-do4.jpg'


export default {
    components: { MainNavBar },
    setup() {
        const authStore = useAuthStore()

        const images = [carousel3, carousel1, carousel4, carousel2]
        const currentIndex = ref(0)
        const shouldAnimate = ref(true)
        let interval = null

        const goToSlide = (index) => {
            currentIndex.value = index
            resetInterval()
        }

        const nextSlide = () => {
            currentIndex.value = (currentIndex.value + 1) % images.length
            shouldAnimate.value = false // Desactiva animación después del primer cambio
        }

        const startInterval = () => {
            interval = setInterval(nextSlide, 5000)
        }

        const resetInterval = () => {
            clearInterval(interval)
            startInterval()
        }

        onMounted(() => {
            startInterval()
            // Desactiva la animación después de que se complete
            setTimeout(() => {
                shouldAnimate.value = false
            }, 1500) // Tiempo igual a la duración de la animación
        })

        onBeforeUnmount(() => {
            clearInterval(interval)
        })

        return {
            authStore,
            images,
            currentIndex,
            goToSlide,
            shouldAnimate
        }
    }
}
</script>



<style scoped>
.home-container {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
}

/* Estilos del carrusel */
.carousel-container {
    position: relative;
    width: 100%;
    height: 600px;
    overflow: hidden;
}

.carousel {
    position: relative;
    width: 100%;
    height: 100%;
}

.slide {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-size: cover;
    background-position: center;
    opacity: 0;
    transition: opacity 1s ease-in-out;
    display: flex;
    align-items: center;
    justify-content: center;
}

.slide.active {
    opacity: 1;
}

/* Estilos del texto de bienvenida */
.welcome-text {
    text-align: center;
    color: white;
    padding: 2rem;
    background-color: rgba(0, 0, 0, 0.5);
    border-radius: 10px;
    max-width: 800px;
    margin: 0 auto;
    animation: fadeIn 1.5s ease-in-out;
}

.welcome-title {
    font-size: 3rem;
    font-weight: 700;
    margin-bottom: 1rem;
    text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.5);
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
    letter-spacing: 1px;
}

.welcome-subtitle {
    font-size: 1.5rem;
    font-weight: 300;
    text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.5);
    font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

@keyframes fadeIn {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

/* Indicadores del carrusel */
.indicators {
    position: absolute;
    bottom: 30px;
    left: 0;
    right: 0;
    display: flex;
    justify-content: center;
    gap: 12px;
    z-index: 10;
}

.indicators button {
    width: 14px;
    height: 14px;
    border-radius: 50%;
    border: none;
    background-color: rgba(255, 255, 255, 0.5);
    cursor: pointer;
    padding: 0;
    transition: all 0.3s ease;
}

.indicators button.active {
    background-color: white;
    transform: scale(1.3);
}

.indicators button:hover {
    background-color: rgba(255, 255, 255, 0.8);
}

.info-container {
    background-color: #ffffff;
    padding: 3rem 0;
    border-top: 1px solid #e9ecef;
    border-bottom: 1px solid #e9ecef;
}

.info-content {
    display: flex;
    align-items: center;
    justify-content: space-between;
    max-width: 1200px;
    margin: 0 auto;
    padding: 0 2rem;
    gap: 3rem;
}

.info-text {
    flex: 1;
    padding: 1.5rem;
    background-color: #f8f9fa;
    border-radius: 8px;
    box-shadow: 0 2px 15px rgba(0, 0, 0, 0.08);
}

.info-text h2 {
    color: #2c3e50;
    font-size: 1.8rem;
    margin-bottom: 1.2rem;
    font-weight: 600;
}

.info-text p {
    color: #495057;
    font-size: 1.1rem;
    line-height: 1.6;
    margin-bottom: 1.5rem;
}

.features-list {
    list-style-type: none;
    padding: 0;
}

.features-list li {
    margin-bottom: 0.8rem;
    display: flex;
    align-items: center;
    color: #343a40;
}

.features-list li i {
    color: #28a745;
    margin-right: 0.7rem;
    font-size: 1.2rem;
}

.info-image {
    flex: 1;
    display: flex;
    justify-content: center;
}

.feature-img {
    max-width: 40%;
    height: auto;
    border-radius: 8px;
    object-fit: cover;
}

/* Responsive */
@media (max-width: 992px) {
    .info-content {
        flex-direction: column;
        gap: 2rem;
    }

    .info-text,
    .info-image {
        width: 100%;
    }

    .info-text {
        order: 2;
    }

    .info-image {
        order: 1;
    }
}

@media (max-width: 576px) {
    .info-content {
        padding: 0 1rem;
    }

    .info-text {
        padding: 1rem;
    }

    .info-text h2 {
        font-size: 1.5rem;
    }

    .welcome-text {
        padding: 1rem;
        width: 90%;
    }

    .welcome-title {
        font-size: 1.8rem;
    }
}

@media (max-width: 768px) {
    .carousel-container {
        height: 400px;
    }

    .welcome-title {
        font-size: 2rem;
    }

    .welcome-subtitle {
        font-size: 1.2rem;
    }
}
</style>