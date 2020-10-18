import Vue from "vue"
import App from "./App"
import filter from "./filter/filter";
//注意这个router使用小写
import router from "./router"
import axios from "axios"

Vue.config.productionTip = false;
Vue.prototype.$ajax = axios;

// 解决每次ajax请求，对应的sessionId不一致的问题
axios.defaults.withCredentials = true;


// 全局过滤器
Object.keys(filter).forEach(key => {
  Vue.filter(key, filter[key])
});

/**
 * axios拦截器
 */

axios.interceptors.request.use(function (config) {

  console.log("请求：", config);
  let token = Tool.getLoginUser().token;

  if (Tool.isNotEmpty(token)) {
    config.headers.token = token;
  }
  return config;
}, () => {});

/**
 * 根据后端响应状态做出回复
 */
axios.interceptors.response.use(
    response => {
      return response;
    },
    error => {
      console.log(error)
      if(error.request){
        console.log(error.request)
      } else if(error.response){
        console.log(error.response.data);
        console.log(error.response.status);
      }
      if (error && error.response) {
        switch (error.response.status) {
          case 400: error.message = '请求错误(400)';
            break;
          case 401: router.push("/").then(r => {});
                    router.go(0);
            break;
          case 403: error.message = '拒绝访问(403)';
            break;
          case 404: error.message = '请求出错(404)';
            break;
          case 408: error.message = '请求超时(408)';
            break;
          case 500: error.message = '服务器错误(500)';
            break;
          case 501: error.message = '服务未实现(501)';
            break;
          case 502: error.message = '网络错误(502)';
            break;
          case 503: error.message = '服务不可用(503)';
            break;
          case 504: error.message = '网络超时(504)';
            break;
          case 505: error.message = 'HTTP版本不受支持(505)';
            break;
          default: error.message = '连接出错：' + error.response.status;
        }
      }else{
        error.message ='连接服务器失败!'
      }
      return Promise.reject(error)
    }
)



// 路由登录拦截
router.beforeEach((to, from, next) => {
  // 要不要对meta.loginRequire属性做监控拦截
  if (to.matched.some(function (item) {
    return item.meta.loginRequire
  })) {
    let loginUser = Tool.getLoginUser();
    if (Tool.isEmpty(loginUser)) {
      next('/login');
    } else {
      next();
    }
  } else {
    next();
  }
});

new Vue({
  router,
  render: h => h(App),
}).$mount('#app');


