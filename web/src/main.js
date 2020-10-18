import Vue from 'vue'
import App from './app.vue'
import router from './route'
import axios from 'axios'
import filter from "./filter/filter";

Vue.config.productionTip = false;
Vue.prototype.$ajax = axios;

// 全局过滤器
Object.keys(filter).forEach(key => {
  Vue.filter(key, filter[key])
});

// 解决每次ajax请求，对应的sessionId不一致的问题
axios.defaults.withCredentials = true;

new Vue({
  router,
  render: h => h(App),
}).$mount('#app');
