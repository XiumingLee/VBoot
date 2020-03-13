<template>
  <div class="app-container calendar-list-container">
    <!-- <button @click="test">测试</button> -->
    <div class="chat_main">
      <el-container style="height: 100%;width: 100%;">
        <!-- 好友栏 -->
        <el-aside width="200px">
          <div class="sidebar">
            <el-button v-for="friend in friendsList" :key="friend.id" @click="friendClick(friend)">
              <div class="block">
                <img :src="friend.avatar?friend.avatar:'http://pfhyaer1v.bkt.clouddn.com/vbootdefaultavatar.png'">
                <i v-if="friend.isRead == 0" class="el-icon-message" style="color:red;"></i>
                <div class="title">{{friend.name}}</div>
                <div class="desc">{{friend.address}}</div>
              </div>
            </el-button>
          </div>
        </el-aside>
        <!-- 好友栏结束 -->
        <el-main v-show="!(chatTarUser.name)">欢迎来到VBoot聊天平台</el-main>
        <el-container v-show="chatTarUser.name">
          <!-- 聊天对象信息 -->
          <el-header>{{chatTarUser.name}}</el-header>
          <!-- 聊天内容展示区  -->
          <el-main id="msgShowContent">
            <div>
              <template v-for="msg in showMsg">
                <!--发送来的消息-->
                <div v-if="msg.sendUserId == chatTarUser.id" style="display: flex;justify-content: flex-start;align-items: center;box-sizing: border-box;margin-bottom: 0.5rem;">
                  <img :src="chatTarUser.avatar?chatTarUser.avatar:'http://pfhyaer1v.bkt.clouddn.com/vbootdefaultavatar.png'" class="userfaceImg">
                  <div style="display: inline-flex;border-style: solid;border-width: 1px;border-color: #20a0ff;border-radius: 5px;padding: 5px 8px 5px 8px">
                    {{msg.msg}}
                  </div>
                </div>
                <!--发出去的消息-->
                <div v-if="msg.sendUserId == currentUser.id" style="display: flex;justify-content: flex-end;align-items: center;box-sizing: border-box;margin-bottom: 0.5rem;">
                  <div style="display: inline-flex;border-style: solid;border-width: 1px;border-color: #20a0ff;border-radius: 5px;padding: 5px 8px 5px 8px;margin-right: 3px;background-color: #9eea6a">
                    {{msg.msg}}
                  </div>
                  <img :src="currentUser.avatar?currentUser.avatar:'http://pfhyaer1v.bkt.clouddn.com/vbootdefaultavatar.png'" class="userfaceImg">
                </div>
              </template>
            </div>
          </el-main>
          <!-- 文本编辑域和发送按钮 -->
          <el-footer>
            <el-form :inline="true" class="send_area">
              <el-row :gutter="10">
                <el-col :span="20">
                  <el-form-item>
                    <el-input v-model="sendMsg" placeholder="请输入内容" size="mini" type="textarea" style="width:50rem" autosize></el-input>
                  </el-form-item>
                </el-col>
                <el-col :span="4">
                  <el-form-item>
                    <el-button size="small" type="primary" @click="sendChat">
                      <i class="fa fa-send" style="margin-right: 1rem"></i>发送
                    </el-button>
                  </el-form-item>
                </el-col>
              </el-row>
            </el-form>
          </el-footer>
        </el-container>
      </el-container>
    </div>
  </div>
</template>

<script>
import { chat } from "@/utils/chat";
export default {
  data() {
    return {
      friendsList: [],
      chatTarUser: {},
      msgList:[],
      sendMsg: "",
      socket: null
    };
  },
  mounted() {
    this.getAllFriends();
  },
  computed: {
    currentUser() {
      return this.$store.state.user.info;
    },
    showMsg() {
      return this.$store.state.socket.msgList;
    }
    // msgStatus() {
    //   return this.$store.state.socket.msgStatus;
    // }
  },
  watch: {
    /**监视showMsg数据的变化，用于是滚动条处在最低端 */
    showMsg: {
      handler() {
        this.$nextTick(() => {
          document.getElementById(
            "msgShowContent"
          ).scrollTop = document.getElementById("msgShowContent").scrollHeight;
        });
        this.getAllFriends();
      }
    },
  },
  methods: {
    getAllFriends() {
      // 从localStorage中获取
      let friendsListJSON = this.$localStorage.get(
        this.currentUser.id + "_friends"
      );
      this.friendsList = JSON.parse(friendsListJSON);
    },
    sendChat() {
      //判断websocket是否断开连接
      if (this.$store.state.socket.isConnected) {
        this.chatSocket();
      } else {
        this.$store.commit("SOCKET_RECONNECT");
        // this.sendChat();
        setTimeout(() => {
          this.chatSocket();
        }, 1000);
      }
    },
    chatSocket() {
      if (this.sendMsg.replace(/\s+/g, "")) {
        let chatMsg = new chat.chatMsg(
          null,
          this.currentUser.id,
          this.chatTarUser.id,
          this.sendMsg
        );
        let msgContent = new chat.MsgContent(chat.CHAT, chatMsg, "");
        chat.setChatMsgsToLocalStorage(
          chatMsg.sendUserId,
          chatMsg.receiveUserId,
          chatMsg
        );
        this.$socket.send(JSON.stringify(msgContent));
        chat.signHasMsg(this.currentUser.id,this.chatTarUser.id,1);
        this.$store.commit("SOCKET_MSGLIST", chatMsg);        
        this.sendMsg = "";
        this.getAllFriends();
      }
    },
    friendClick(friend) {
      //先将聊天列表清空
      this.$store.commit("SOCKET_CLEAR_MSGLIST");
      this.chatTarUser = friend;
      let msgKey = this.currentUser.id + "_" + friend.id;
      //获取好友聊天内容
      let allChatMsg = JSON.parse(this.$localStorage.get(chat.ChATMSGS));
      if (msgKey in allChatMsg) {
        //保存聊天记录
        let chatMsgArr = allChatMsg[msgKey];
        for (let i = 0; i < chatMsgArr.length; i++) {
          const element = chatMsgArr[i];
          this.$store.commit("SOCKET_MSGLIST", element);
        }
      }
      //设置该好友的消息为已读
      chat.signHasMsg(this.currentUser.id, friend.id, 1);
    }

    // test() {
    //   chat.signHasMsg(this.currentUser.id + "_friends");
    // }
  }
};
</script>

<style lang="scss" scoped>
.chat_main {
  border-radius: 5px;
  border: 1px solid #ccc;
  box-shadow: 0px 0px 30px rgba(0, 0, 0, 0.3);
  position: absolute;
  height: 80%;
  width: 92%;
  .el-header,
  .el-footer {
    border-bottom: 1px solid #ccc;
    color: #333;
    line-height: 60px;
  }

  #msgShowContent {
    overflow: auto;
  }
  .el-main {
    color: #333;
    // line-height: 160px;
    .userfaceImg {
      width: 37px;
      height: 37px;
      border-radius: 30px;
      margin-right: 10px;
    }
  }
  .el-aside {
    background-color: #f1f1f1;
    color: #333;
    line-height: 100%;
    // text-align: center;
    .sidebar {
      float: left;
      width: 100%;
      height: calc(90vh - 55px);
      background-color: #f1f1f1;
      border-radius: 0 0 0 5px;
      overflow: auto;
      border-right: 1px solid #ccc;
      .el-button {
        display: block;
        margin-left: 0;
        // background: #f1f1f1;
        width: 100%;
        padding: 0;
        text-align: left;
      }
      .block {
        padding: 0.6rem;
        transition: all 0.1s;
      }
      img {
        float: left;
        width: 40px;
        height: 40px;
        border-radius: 100px;
        margin-right: 0.3rem;
      }
      .title {
        padding: 0.1rem;
        font-size: 0.8rem;
        font-weight: bold;
      }
      .desc {
        padding: 0.1rem;
        font-size: 0.5rem;
      }
      i {
        float: right;
      }
    }
  }
}
</style>
