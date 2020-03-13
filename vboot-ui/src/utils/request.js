import axios from 'axios'
import { Message, MessageBox } from 'element-ui'
import store from '../store'
// import { getToken } from '@/utils/auth'
import router from '@/router'

// 创建axios实例
const service = axios.create({
  withCredentials: true,
  baseURL: process.env.BASE_API, // api 的 base_url
  timeout: 5000 // 请求超时时间
});

// request拦截器
service.interceptors.request.use(
  config => {
    if (store.getters.token) {
      // config.headers['X-Token'] = getToken() // 让每个请求携带自定义token 请根据实际情况自行修改
    }
    return config
  },
  error => {
    // Do something with request error
    console.log(error) // for debug
    Promise.reject(error)
  }
);

// response 拦截器
service.interceptors.response.use(
  response => {
    /**
     * status为200是表示成功！
     */
    if (response.status !== 200) {
      Message({
        message: response.status,
        type: 'error',
        duration: 5 * 1000
      })
    }
    const res = response.data
    if (res.status !== 200) {
      // Message({
      //   message: res.msg,
      //   type: 'error',
      //   duration: 5 * 1000
      // })

      if (res.status === 401) {
        MessageBox.confirm(
          '你已被登出，可以取消继续留在该页面，或者重新登录',
          '确定登出',
          {
            confirmButtonText: '重新登录',
            cancelButtonText: '取消',
            type: 'warning'
          }
        ).then(() => {
          store.dispatch('FedLogOut').then(() => {
            location.reload() // 为了重新实例化vue-router对象 避免bug
            router.push({ path: "/login" });
          })
        })
        // Message({
        //   message: "登录超时或已注销，请重新登录！",
        //   type: 'error',
        //   duration: 5 * 1000
        // });
        //  store.dispatch('FedLogOut').then(() => {
        //     location.reload() // 为了重新实例化vue-router对象 避免bug
        //   })
        // store.commit('SET_INFO', null)
        // store.commit('SET_routes', null);
        // router.push({ path: "/login" });
        // // location.reload()
        // store.dispatch('LogOut').then(() => {
        //   location.reload() // 为了重新实例化vue-router对象 避免bug
        // })
      }
      return Promise.reject(res.msg);
    } else {
      return res;
    }
  },
  error => {
    console.log('err' + error) // for debug
    Message({
      message: error.message,
      type: 'error',
      duration: 5 * 1000
    })
    return Promise.reject(error)
  }
);

export default service

export const get = (url, params = {}) => {
  return service({
    method: 'get',
    url: `${url}`,
    params: params
  });
};

export const post = (url, params) => {
  return service({
    method: 'post',
    url: `${url}`,
    data: params
  })
};

export const put = (url, params) => {
  return service({
    method: 'put',
    url: `${url}`,
    data: params
  })
};
export const deleteRequest = (url, params = {}) => {
  return service({
    method: 'delete',
    url: `${url}`,
    data: params
  });
};
export const formPost = (url, params) => {
  return service({
    method: 'post',
    url: `${url}`,
    data: params,
    transformRequest: [function (data) {
      let ret = ''
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  });
};

export const uploadFileRequest = (url, params) => {
  return service({
    method: 'post',
    url: `${url}`,
    data: params,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
};
export const formPut = (url, params) => {
  return service({
    method: 'put',
    url: `${url}`,
    data: params,
    transformRequest: [function (data) {
      let ret = ''
      for (let it in data) {
        ret += encodeURIComponent(it) + '=' + encodeURIComponent(data[it]) + '&'
      }
      return ret
    }],
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded'
    }
  });
};
