<template>
    <AdminNavBar />
    <div class="admin-container">

        <div class="admin-content">
            <router-view />
        </div>
    </div>
</template>

<script setup>
import AdminNavBar from '@/components/AdminNavBar.vue'
</script>

<style scoped>
.admin-container {
    padding-top: 80px;
    padding-left: 20px;
    padding-right: 20px;
    min-height: calc(100vh - 80px);
    max-width: 1200px;
    margin: 0 auto;
}

.admin-content {
    margin-top: 2rem;
    padding: 1.5rem;
    background-color: white;
    border-radius: 8px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
</style>