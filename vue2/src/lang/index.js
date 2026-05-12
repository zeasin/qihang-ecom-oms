
import en from './en-US'
import zh from './zh-CN'
import Vue from 'vue'
import VueI18n from 'vue-i18n'
import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'


Vue.use(VueI18n)
Vue.use(ElementUI)

const messages = {
  en,
  zh
}

const i18n = new VueI18n({
  locale: 'zh', // 默认语言
  messages
})

export default i18n
