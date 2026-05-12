<script>


import { pddOauthLogin } from '@/api/shop/oauth'
import { setToken } from '@/utils/auth'

export default {
  name: "PddCallback",
  data() {
    return {
      code:null,
      form:{

      }
    }
  },
mounted() {
  if(this.$route.query.code){
    this.form.code = this.$route.query.code
    this.form.state = this.$route.query.state
    pddOauthLogin(this.form).then(res => {
      if(res.code == 200){
        this.$modal.msgSuccess("登录成功")
        setToken(res.token)
        // commit('SET_TOKEN', res.token)
        this.$router.push({ path: this.redirect || "/" }).catch(()=>{});
      }else
      {
        this.$modal.msgError(res.msg)
      }
      console.log(res)
    })
  }else{
    this.$router.push({ path: this.redirect || "/" })
  }
},
methods: {}
}

</script>

<template>
<div>

</div>
</template>

<style scoped lang="scss">

</style>
