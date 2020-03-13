
// import { getToken, setToken, removeToken } from '@/utils/auth'
import { get } from '@/utils/request'

const user = {
  state: {
    // token: getToken(),
    name: '',
    avatar: '',
    roles: [],
    info: null,
    urls:null
  },

  mutations: {
    // SET_TOKEN: (state, token) => {
    //   state.token = token
    // },
    // SET_NAME: (state, name) => {
    //   state.name = name
    // },
    // SET_AVATAR: (state, avatar) => {
    //   state.avatar = avatar
    // },
    // SET_ROLES: (state, roles) => {
    //   state.roles = roles
    // },
    SET_INFO: (state, info) => {
      state.info = info
    },
    SET_URLS: (state, urls) => {
      state.urls = urls
    }
  },

  actions: {
    // // 登出
    // LogOut({ commit, state }) {
    //   return new Promise((resolve, reject) => {
    //     get("/logout").then(res=>{
    //       commit('SET_routes', null);
    //       commit('SET_INFO', null);
    //       resolve()
    //     }).catch(err=>{
    //       reject(error)
    //     })
    //   })
    // },

    // // 前端 登出
    // FedLogOut({ commit }) {
    //   return new Promise(resolve => {
    //     commit('SET_INFO', null)
    //     commit('SET_routes', null);
    //     resolve()
    //   })
    // }

    // 登出
    LogOut({ commit, state }) {
      return new Promise((resolve, reject) => {
        get("/logout").then(res => {
          commit('SET_INFO', null);
          commit('SET_routes', null);
          resolve()
        }).catch(err => {
          reject(err);
        })
      })
    },

    // 前端 登出
    FedLogOut({ commit }) {
      return new Promise(resolve => {
        commit('SET_INFO', null);
        commit('SET_routes', null);
        resolve()
      })
    }
  }
}

export default user
