import Vue from 'vue'
import Vuex from 'vuex'
import app from './modules/app'
import user from './modules/user'
import chats from './modules/chats'
import routers from './modules/routers'
import getters from './getters'
import { chat } from '@/utils/chat'
//websocket相关需要
import VueNativeSock from 'vue-native-websocket'

Vue.use(Vuex)

const store = new Vuex.Store({
  modules: {
    app,
    user,
    routers,
    chats
  },
  getters,

  //socket相关
  state: {
    socket: {
      isConnected: false,
      message: '',
      msgList: [],
      reconnectError: false,
    }
  },
  mutations: {
    //websocket
    //链接成功时
    SOCKET_ONOPEN(state, event) {
      Vue.prototype.$socket = event.currentTarget;
      //判断是否已经连接成功！如果已经连接成功就不需要再次发送请求
      if (state.socket.isConnected) {
      } else {
        //建立连接后，向后端发送一次请求，用于channel和userid关联起来
        let userInfo = state.user.info;
        console.log(userInfo);
        let chatMsg = new chat.chatMsg(null, userInfo.id, null, "用户登录完成！绑定channel和userid");
        let msgContent = new chat.MsgContent(chat.CONNECT, chatMsg, "");
        console.log(msgContent);
        Vue.prototype.$socket.send(JSON.stringify(msgContent));
      }
      state.socket.isConnected = true;
    },
    //连接关闭时
    SOCKET_ONCLOSE(state, event) {
      state.socket.isConnected = false
    },
    //连接发生错误时
    SOCKET_ONERROR(state, event) {
      console.error(state, event)
    },
    // default handler called for all methods
    //接收到消息时
    SOCKET_ONMESSAGE(state, message) {
      state.socket.message = message.data;
      let msg = JSON.parse(message.data);
      chat.setChatMsgsToLocalStorage(msg.receiveUserId,msg.sendUserId,msg);
      chat.signHasMsg(msg.receiveUserId,msg.sendUserId,0);
      state.socket.msgList.push(msg);
      //接收到消息以后，发送签收消息，将数据库中的聊天内容标记为已签收。
      let msgContent = new chat.MsgContent(chat.SIGNED, msg, "");
      Vue.prototype.$socket.send(JSON.stringify(msgContent));
    },
    // mutations for reconnect methods
    // 重新连接
    SOCKET_RECONNECT(state, count) {
      //websocket  用于在用户的登录后在创建websocket链接和断开连接后重新连接。 重点！！！！
      Vue.use(VueNativeSock, 'ws://localhost:8088/ws', { store: store })
    },
    //重现连接出错时
    SOCKET_RECONNECT_ERROR(state) {
      state.socket.reconnectError = true;
    },
    //操作msgList
    SOCKET_MSGLIST(state,msg){
      state.socket.msgList.push(msg);
    },
    //清空msgList
    SOCKET_CLEAR_MSGLIST(state){
      state.socket.msgList = [];
    }
  }
})

export default store
