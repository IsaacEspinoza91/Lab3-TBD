<template>
    <div class="page-content">
        <MainNavBar />
        <div class="main-content">
            <div class="container mt-5">
                <div class="row justify-content-center">
                    <div class="col-md-6">
                        <LoginForm />
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import MainNavBar from '../../components/NavBars/MainNavBar.vue'
import LoginForm from '../../components/Auth/LoginForm.vue'
</script>

<style scoped>
.page-content {
    background-image: url('../../assets/background-home.jpg');
    background-size: cover;
    flex: 1;
    display: flex;
    flex-direction: column;
}

.main-content {
    flex: 1;
    padding: 20px;
}
</style>