<script>


export default {
  name: "WeidianCallback",
  data() {
    return {
      code:null
    }
  },
mounted() {
  if(this.$route.query.code){
    this.code = this.$route.query.code
  }
},
methods: {

}
}

</script>

<template>
<div>
  code:{{code}}
</div>
</template>

<style scoped lang="scss">

</style>
