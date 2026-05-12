<template>
  <div class="h5-layout">
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'H5Layout'
}
</script>

<style scoped>
.h5-layout {
  width: 100%;
  min-height: 100vh;
  background: #f5f5f5;
}
</style>