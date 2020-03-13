import Vue from 'vue';
import App from './App.vue';

import 'normalize.css/normalize.css' // A modern alternative to CSS resets

import ElementUI from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
import locale from 'element-ui/lib/locale/lang/en' // lang i18n
import '@/styles/index.scss' // global css
import '@/icons' // icon

Vue.config.productionTip = false;

import router from './router'; // 路由配置
import './router/routerGuards'; // 导航卫士
import store from './store'
// vue-localstorage
import VueLocalStorage from 'vue-localstorage'
Vue.use(VueLocalStorage)

Vue.use(ElementUI, { locale })

// axios
import {get,post,put,deleteRequest,formPost,uploadFileRequest,formPut} from './utils/request'
Vue.prototype.$get = get;
Vue.prototype.$post = post;
Vue.prototype.$put = put;
Vue.prototype.$deleteRequest = deleteRequest;
Vue.prototype.$formPost = formPost;
Vue.prototype.$uploadFileRequest = uploadFileRequest;
Vue.prototype.$formPut = formPut;

new Vue({
  render: h => h(App),
  store,
  router
}).$mount('#app')
