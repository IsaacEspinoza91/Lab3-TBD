<template>
  <router-view />
  <AppFooter />
</template>

<script setup>
import { useAuthStore } from '@/stores/auth'
import { useRouter } from 'vue-router'
import AppFooter from '/src/components/AppFooter.vue'

const authStore = useAuthStore()
authStore.initialize()

const router = useRouter()

const logout = () => {
  authStore.clearToken()
  router.push({ name: 'home' })
}
</script>

<style>
@import 'bootstrap/dist/css/bootstrap.min.css';
@import 'bootstrap-icons/font/bootstrap-icons.css';

#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}

body {
  margin: 0;
  padding: 0;
  font-family: 'Arial', sans-serif;
  height: 100%;
}

nav {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  z-index: 1030;
  height: 60px;
}

nav a {
  font-weight: bold;
  color: #2c3e50;
  margin-right: 10px;
}

nav a.router-link-exact-active {
  color: #42b983;
}

button {
  padding: 8px 16px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #369f6b;
}
</style>