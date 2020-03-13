<template>
  <el-menu class="navbar" mode="horizontal">
    <hamburger :toggle-click="toggleSideBar" :is-active="sidebar.opened" class="hamburger-container" />
    <breadcrumb />
    <!-- 消息提示区 -->
    <div class="message">
      <!-- 信息通知 -->
      <router-link to="/chat/chatPage">
        <span v-if="isHasMsg">
          <svg class="icon" aria-hidden="true">
            <use xlink:href="#icon-_xiaoxitishitubiao"></use>
          </svg>
        </span>
        <span v-else>
          <svg class="icon" aria-hidden="true">
            <use xlink:href="#icon-xiaoxi"></use>
          </svg>
        </span>
      </router-link>
    </div>
    <el-dropdown class="avatar-container" trigger="click">
      <div class="avatar-wrapper">
        <img :src="info.avatar?info.avatar:'http://qiniu.xiuminglee.cn/vboot/touxiang/vbootdefaultavatar.png'+'?imageView2/1/w/80/h/80'" class="user-avatar">
        <span class="user-name">{{info.name}}</span>
        <i class="el-icon-caret-bottom" />
        </span>
      </div>
      <el-dropdown-menu slot="dropdown" class="user-dropdown">
        <el-dropdown-item>
          <router-link class="inlineBlock" to="/">
            首页
          </router-link>
        </el-dropdown-item>
        <el-dropdown-item>
          <router-link class="inlineBlock" to="/info">
            个人资料
          </router-link>
        </el-dropdown-item>
        <el-dropdown-item divided>
          <span style="display:block;" @click="logout">注销</span>
        </el-dropdown-item>
      </el-dropdown-menu>
    </el-dropdown>
  </el-menu>
</template>

<script>
import { mapGetters } from "vuex";
import Breadcrumb from "@/components/Breadcrumb";
import Hamburger from "@/components/Hamburger";
import { chat } from "@/utils/chat.js";
export default {
  data() {
    return {
      // avatar:
      // "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif"
      isHasMsg: false
    };
  },
  components: {
    Breadcrumb,
    Hamburger
  },
  computed: {
    ...mapGetters(["sidebar"]),
    info() {
      return this.$store.state.user.info;
    },
    //计算是否有新的消息
    hasMsg() {
      return this.$store.state.socket.msgList;
    }
  },
  watch: {
    hasMsg: {
      handler(newVal) {
        if (this.$route.path == "/chat/chatPage") {
          this.isHasMsg = false;
        } else {
          this.isHasMsg = true;
        }
      }
    }
  },
  mounted() {
    this.getMsgsWithNotSigned();
  },
  methods: {
    toggleSideBar() {
      this.$store.dispatch("ToggleSideBar");
    },
    logout() {
      let _this = this;
      this.$confirm("你确认注销该账户的登录状态吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.$store.dispatch("LogOut").then(() => {
            location.reload(); // 为了重新实例化vue-router对象 避免bug
            this.$router.push({ path: "/login" });
          });
        })
        .catch(() => {
          _this.$message({
            type: "info",
            message: "已取消"
          });
        });
    },
    // 获取离线时的消息
    getMsgsWithNotSigned() {
      this.$get("chat/chatMsgs/notsigned")
        .then(res => {
          let chatList = res.data;
          if (chatList.length <= 0) {
            return false;
          } else {
            this.isHasMsg = true;
            for (let i = 0; i < chatList.length; i++) {
              const element = chatList[i];
              chat.setChatMsgsToLocalStorage(
                element.receiveUserId,
                element.sendUserId,
                element
              );
              chat.signHasMsg(element.receiveUserId, element.sendUserId, 0);
            }
          }
          this.batchSignIn(chatList);
        })
        .catch(err => {
          this.$message.error(err);
        });
    },
    /**
     * 批量签收消息
     */
    batchSignIn(chatList) {
      this.$post("/chat/batchSignIn", chatList)
        .then(res => {})
        .catch(err => {
          this.$message.error(err);
        });
    }
  }
};
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.navbar {
  height: 50px;
  line-height: 50px;
  border-radius: 0px !important;
  .hamburger-container {
    line-height: 58px;
    height: 50px;
    float: left;
    padding: 0 10px;
  }
  .screenfull {
    position: absolute;
    right: 90px;
    top: 16px;
    color: red;
  }
  .message {
    height: 50px;
    display: inline-block;
    position: absolute;
    right: 180px;
    span {
      margin-right: 0.7rem;
    }
  }
  .avatar-container {
    height: 50px;
    display: inline-block;
    position: absolute;
    right: 35px;
    .avatar-wrapper {
      cursor: pointer;
      // margin-top: 5px;
      position: relative;
      display: flex;
      justify-content: center;
      align-items: center;
      .user-avatar {
        width: 40px;
        height: 40px;
        border-radius: 10px;
      }
      .user-name {
        margin-left: 0.8rem;
        font-size: 0.8rem;
      }
      .el-icon-caret-bottom {
        position: absolute;
        right: -20px;
        // top: 25px;
        font-size: 12px;
      }
    }
  }
}
</style>

