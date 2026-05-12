<template>
  <div class="login">
    <div class="layout">
      <div class="bgLeft"></div>
      <div class="bgRight"></div>
      <h3 class="title">{{title}}</h3>
      <div class="login-form">
        <div class="tabs" v-if="n !==3">
            <div class="item" :class="n==1?'on':''" @click="n=1">后台登录</div>
        </div>

        <!--star 密码登录-->
        <el-form ref="loginForm" :model="loginForm" :rules="loginRules" v-if="n==1">
          <el-form-item prop="username">
            <el-input
              v-model="loginForm.username"
              type="text"
              auto-complete="off"
              placeholder="账号/手机号"
            >
              <svg-icon slot="prefix" icon-class="user" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="loginForm.password"
              type="password"
              auto-complete="off"
              placeholder="密码"
              @keyup.enter.native="handleLogin"
            >
              <svg-icon slot="prefix" icon-class="password" class="el-input__icon input-icon" />
            </el-input>
          </el-form-item>
          <el-form-item prop="code" v-if="captchaEnabled">
            <el-input
              v-model="loginForm.code"
              auto-complete="off"
              placeholder="验证码"
              style="width: 63%"
              @keyup.enter.native="handleLogin"
            >
              <svg-icon slot="prefix" icon-class="validCode" class="el-input__icon input-icon" />
            </el-input>
            <div class="login-code">
              <img :src="codeUrl" @click="getCode" class="login-code-img"/>
            </div>
          </el-form-item>
<!--          <el-checkbox v-model="loginForm.rememberMe" style="margin:0px 0px 25px 0px;">记住密码</el-checkbox>-->
          <el-form-item style="width:100%;">
            <el-button class="btns"
                       :loading="loading"
                       size="medium"
                       type="primary"
                       style="width:100%;"
                       @click.native.prevent="handleLogin"
            >
              <span v-if="!loading">登 录</span>
              <span v-else>登 录 中...</span>
            </el-button>

          </el-form-item>
        </el-form>
        <!--end 密码登录-->


      </div>
    </div>
    <!--  底部  -->
    <div class="el-login-footer">
      <span>Copyright © 2023-2026 Qihang Ecom ERP V4.0 All Rights Reserved.</span>
    </div>
  </div>
</template>

<script>
import Cookies from "js-cookie";
import { encrypt, decrypt } from '@/utils/jsencrypt'
import { getCodeImg } from "@/api/login";
import {getConfig} from "@/api/system/config";
import {validatePassword} from "@/utils/validate";
export default {
  name: "Login",
  data() {
    return {
      n:1,
      codeUrl: "",
      loginForm: {
        username: "",
        password: "",
        rememberMe: false,
        code: "",
        uuid: ""
      },
      title:'',
      loginRules: {
        username: [
          { required: true, trigger: "blur", message: "请输入您的账号" }
        ],
        password: [
          { required: true, trigger: "blur", message: "请输入您的密码" }
        ],
        code: [{ required: true, trigger: "change", message: "请输入验证码" }]
      },
      loading: false,
      passwordError: null,
      // 验证码开关
      captchaEnabled: true,
      // 注册开关
      register: false,
      redirect: undefined,

      show: true,
      isCode: false,
      count: 60,
      form: {
        phone: "",
        smsCode: "",
        checkMove: "",
      },
      codeRules: {
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' },
        ],
        smsCode: [{ required: true, message: '请输入手机验证码', trigger: 'blur' }],
        checkMove: [{ required: true, message: '滑动完成验证', trigger: 'blur' }],
      },

      registerForm:{
        phone: "",
        passwords: "",
        passwordok: "",
      },
      registerRules: {
        phone: [
          { required: true, message: '请输入您的账号', trigger: 'blur' },
        ],
        passwords: [{ required: true, message: '请输入您的密码', trigger: 'blur' }],
        passwordok: [{ required: true, message: '请再此确认您的密码', trigger: 'blur' }],
      },
    };
  },
  watch: {
    $route: {
      handler: function(route) {
        this.redirect = route.query && route.query.redirect;
      },
      immediate: true
    }
  },
  created() {
    this.loading =true
    getConfig('sys.name').then(resp=>{
      if(resp.data){
        this.title = resp.data.configValue
        this.getCode();
        this.getCookie();
      }
    })

  },
  // directives: {
  //   move(el, binding, vnode) {
  //     let vm = this;
  //     el.onmousedown = function (e) {
  //       var X = e.clientX - el.offsetLeft;
  //       document.onmousemove = function (e) {
  //         var endx = e.clientX - X;
  //         el.className = "move moveBefore";
  //         el.style.left = endx + "px";
  //         var width = document.querySelector(".movebox").offsetWidth - document.querySelector(".move").offsetWidth;
  //         el.parentNode.children[0].style.width = endx + 20 + "px";
  //         el.parentNode.children[1].innerHTML = "按住滑块,拖动到最右边";
  //         //临界值小于
  //         if (endx <= 0) {
  //           el.style.left = 0 + "px";
  //           el.parentNode.children[0].style.width = 0 + "px";
  //         }
  //
  //         //临界值大于
  //         if (parseInt(el.style.left) >= width) {
  //           if(vnode.context.form.phone == ''){
  //             el.style.left = 0 + "px";
  //             el.parentNode.children[0].style.width = 0 + "px";
  //             el.className = "move moveBefore";
  //             document.onmousemove = null;
  //             vnode.context.$message({
  //               message: '请输入手机号',
  //               type: 'warning'
  //             });
  //           }else{
  //             el.style.left = width + "px";
  //             el.parentNode.children[0].style.width = width + 20 + "px";
  //             el.parentNode.children[1].innerHTML = "<span style='-webkit-text-fill-color: #fff;'>验证通过</span>";
  //             el.className = "move moveSuccess";
  //             document.onmousemove = null;
  //             el.onmousedown = null;
  //             binding.value()
  //           }
  //         }
  //       };
  //     };
  //     document.onmouseup = function () {
  //       document.onmousemove = null;
  //     };
  //   }
  // },
  methods: {
    getCookie() {
      const username = Cookies.get("username");
      const password = Cookies.get("password");
      const rememberMe = Cookies.get('rememberMe')
      this.loginForm = {
        username: username === undefined ? this.loginForm.username : username,
        password: password === undefined ? this.loginForm.password : decrypt(password),
        rememberMe: rememberMe === undefined ? false : Boolean(rememberMe)
      };
    },
    handleLogin() {
      const vm = this
      this.$refs.loginForm.validate(valid => {
        if (valid) {
          this.loading = true;

          // 验证密码强度
          // if (!vm.validatePassword()) {
          //   vm.$modal.msgError(vm.passwordError)
          //   this.loading = false
          //   return
          // }
          // const res = validatePassword(this.loginForm.password,this.loginForm.username)
          // if (!res.result) {
          //   this.$modal.msgError(res.msg)
          //   this.loading = false
          //   return
          // }

          if (this.loginForm.rememberMe) {
            Cookies.set("username", this.loginForm.username, { expires: 30 });
            Cookies.set("password", encrypt(this.loginForm.password), { expires: 30 });
            Cookies.set('rememberMe', this.loginForm.rememberMe, { expires: 30 });
          } else {
            Cookies.remove("username");
            Cookies.remove("password");
            Cookies.remove('rememberMe');
          }
          this.$store.dispatch("Login", this.loginForm).then(() => {
            this.$router.push({ path: this.redirect || "/" }).catch(()=>{});
          }).catch(() => {
            this.loading = false;
            if (this.captchaEnabled) {
              this.getCode();
            }
          });
        }
      });
    },
    getCode() {
      getCodeImg().then(res => {
        this.captchaEnabled = res.captchaEnabled === undefined ? true : res.captchaEnabled;
        if (this.captchaEnabled) {
          this.codeUrl = "data:image/gif;base64," + res.img;
          this.loginForm.uuid = res.uuid;
        }
        this.loading = false
      });
    },

    // 密码验证方法
    validatePassword() {
      this.passwordError = ""; // 清除之前的错误信息
      // 1. 检查密码长度
      if (this.loginForm.password.length < 8 || this.loginForm.password.length > 32) {
        this.passwordError = "密码长度必须在 8 到 32 个字符之间";
        return false;
      }

      // 2. 检查密码是否包含大小写字母、数字和特殊字符
      const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*(),.?":{}|<>]).+$/;
      if (!regex.test(this.loginForm.password)) {
        this.passwordError = "密码必须包含大小写字母、数字和特殊字符";
        return false;
      }

      // 3. 禁止连续或重复字符
      if (this.hasSequentialOrRepeatedChars(this.loginForm.password)) {
        this.passwordError = "密码不能包含连续或重复字符";
        return false;
      }

      // 4. 密码不能与用户名相同
      if (this.loginForm.password.toLowerCase() === this.loginForm.username.toLowerCase()) {
        this.passwordError = "密码不能与用户名相同";
        return false;
      }

      // 密码符合要求
      return true;
    },

    // 检查密码是否包含连续或重复字符
    hasSequentialOrRepeatedChars(password) {
      // 检查连续数字或字母
      for (let i = 0; i < password.length - 2; i++) {
        const current = password.charAt(i);
        const next = password.charAt(i + 1);
        const nextNext = password.charAt(i + 2);
        if (next === current + 1 && nextNext === current + 2) {
          return true;
        }
      }

      // 检查重复字符
      for (let i = 0; i < password.length - 1; i++) {
        if (password.charAt(i) === password.charAt(i + 1)) {
          return true;
        }
      }

      return false;
    }

  }
};
</script>

<style rel="stylesheet/scss" lang="scss">
.login {
  font-family: Barlow;
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 800px;
  height: 100%;
  width: 100%;
  // background-size: cover;
  background-image: url("../assets/images/login-background.png");
  background-attachment: fixed;
  overflow: hidden;
  .layout{
    position: relative;
    z-index: 3;
    height: 100%;
  }
  .bgLeft{
    position: absolute;
    width: 590px;
    height: 590px;
    bottom: 0;
    left: -359px;
    background-image: url("../assets/images/backgroundLeft.png");
    background-size: cover;
  }
  .bgRight{
    position: absolute;
    width: 414px;
    height: 414px;
    top: 0;
    right: -211px;
    background-image: url("../assets/images/backgroundRight.png");
    background-size: cover;
  }
  .title {
    margin: 79px auto 40px;
    text-align: center;
    color: #000000;
    font-size: 24px;
    font-weight: bold;
  }

}
.login-form {
  border-radius: 16px;
  width: 428px;
  min-height: 500px;
  padding-top: 68px;
  position: relative;
  z-index: 9999;
  box-shadow: 0 20px 80px 0 rgba(45,66,119,.10196);
  background: hsla(0, 0%, 100%, .65);
  border: 1px solid #fff;
  .zhuce{
    font-weight: 600;
    height: 20px;
    font-size: 20px;
    line-height: 20px;
    color: #333;
    text-align: center;
  }
  .tabs{
    display: flex;
    padding: 0 54px;
    text-align: center;
    .item{
      width: 100%;
      height: 20px + 12px;
      font-size: 20px;
      line-height: 20px;
      color: #333;
      font-weight: 600;
      position: relative;
      cursor: pointer;
      &.on{
        color: #307dff;
        &::after{
          content: '';
          position: absolute;
          bottom: 0;
          left: 0;
          right: 0;
          height: 3px;
          width: 28px;
          margin: 12px auto 0;
          background-color: #307dff;
        }
      }
    }
  }
  .el-form{
    padding: 0px 48px 99px;
    margin-top: 32px;
    .btns{
      color: #fff;
      background-color: #307dff;
      border-color: #307dff;
      width: 100%;
      border-radius: 8px;
      padding: 15px 0;
      font-weight: 500;
    }
  }
  .el-input {
    height: 46px;
    input {
      height: 46px;
      line-height: 46px;
      border-color: #e4e4e4;
      border-radius: 8px;
      padding-left: 40px;
      color: #000000;
    }
  }
  .input-icon {
    height: 46px;
    width: 14px;
    margin-left: 10px;
  }
  .register{
    width: 100%;
    height: 14px;
    line-height: 14px;
    color: #666;
    text-align: center;
    font-size: 14px;
    position: absolute;
    bottom: 48px;
    span{
      font-size: 14px;
      margin-left: 8px;
      color: #3179f8!important;
      cursor: pointer;
    }
  }
}
.login-tip {
  font-size: 13px;
  text-align: center;
  color: #bfbfbf;
}
.login-code {
  width: 33%;
  height: 46px;
  float: right;
  .el-button{
    height: 46px;
    padding: 0;
    width: 100%;
    border-radius: 8px;
    font-weight: bold;
    color: #000000;
  }
  img {
    cursor: pointer;
    vertical-align: middle;
  }
}
.el-login-footer {
  height: 40px;
  line-height: 40px;
  position: fixed;
  z-index: 2;
  bottom: 0;
  width: 100%;
  text-align: center;
  color: #000000;
  font-family: Arial;
  font-size: 12px;
  letter-spacing: 1px;
}
.login-code-img {
  height: 38px;
}

.movebox {
  position: relative;
  background-color: #e8e8e8;
  width: 100%;
  height: 46px;
  line-height: 46px;
  text-align: center;
  border-radius: 8px;
  overflow: hidden;
  .txt {
    position: absolute;
    top: 0px;
    width: 100%;
    -moz-user-select: none;
    -webkit-user-select: none;
    user-select: none;
    -o-user-select: none;
    -ms-user-select: none;
    font-size: 12px;
    color: #FFFFFF;

    background: -webkit-gradient(linear, left top, right top, color-stop(0, #4d4d4d), color-stop(.4, #4d4d4d), color-stop(.5, #fff), color-stop(.6, #4d4d4d), color-stop(1, #4d4d4d));
    -webkit-background-clip: text;
    -webkit-text-fill-color: transparent;
    -webkit-animation: slidetounlock 3s infinite;
    -webkit-text-size-adjust: none;
  }
  .movego {
    background-color: #7ac23c;
    height: 46px;
    width: 0px;
  }
  .move {
    position: absolute;
    top: 0px;
    left: 0px;
    width: 46px;
    height: 46px;
    border: 1px solid #e4e4e4;
    cursor: move;
    border-radius: 8px;
    background: #fff;
    background-size: cover;
    background-repeat: no-repeat;
    background-position: center center;
  }
  .moveBefore {
    background-image: url("../assets/icons/moveBefore.png");
    background-size: 18px 18px;
  }
  .moveSuccess {
    background-image: url("../assets/icons/moveSuccess.png");
    background-size: 18px 18px;
    -webkit-text-fill-color: #fff;
  }
}
@keyframes slidetounlock{
  0%{
    background-position: -200px 0;
  }
  100%{
    background-position: 200px 0;
  }
}
</style>
