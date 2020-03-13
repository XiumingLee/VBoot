<template>
  <div class="info_container">
    <el-row class="info_row row" :gutter="10">
      <!-- 修改资料 -->
      <el-col :span="8">
        <div class="area">
          <p class="title">
            <i class="fa fa-edit"></i>修改信息</p>
          <el-form class="form" :model="infoForm" :rules="infoRules" ref="infoForm" label-width="80px">
            <el-form-item label="用户名">
              <el-input v-model="infoForm.username" size="small" disabled placeholder="请输入用户名"></el-input>
            </el-form-item>
            <el-form-item label="昵称" prop="name">
              <el-input v-model="infoForm.name" size="small" placeholder="请输入昵称"></el-input>
            </el-form-item>
            <el-form-item label="性别" prop="sex">
              <!-- <el-input v-model="infoForm.sex" size="small" placeholder="请选择性别"></el-input> -->
              <el-select v-model="infoForm.sex" placeholder="请选择性别">
                <el-option v-for="item in dictSex" :key="item.num" :label="item.name" :value="item.num">
                </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="绑定邮箱" prop="mail">
              <el-col :span="20">
                <el-input v-model="infoForm.mail" size="small" disabled></el-input>
              </el-col>
              <el-col :span="4">
                <el-button size="mini" type="warning" round @click="bindMail">绑定邮箱</el-button>
              </el-col>
            </el-form-item>
            <el-form-item label="绑定手机" prop="mobilephone">
              <el-col :span="20">
                <el-input v-model="infoForm.mobilephone" size="small" disabled></el-input>
              </el-col>
              <el-col :span="4">
                <el-button size="mini" type="warning" round @click="bindPhone">绑定手机</el-button>
              </el-col>
            </el-form-item>
            <el-form-item label="地址" prop="address">
              <el-input v-model="infoForm.address" size="small" placeholder="请输入地址"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button :disabled="editBtn" type="primary" @click="editInfo('infoForm')">修改</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-col>
      <!-- 修改密码 -->
      <el-col :span="8">
        <div class="area">
          <div class="pwdarea">
            <p class="title">
              <i class="fa fa-edit"></i>修改密码</p>
            <el-form class="form" :model="pwdForm" :rules="pwdRules" ref="pwdForm" label-width="100px">
              <el-form-item label="原密码" prop="password">
                <el-input type="password" v-model="pwdForm.password" auto-complete="off" size="small" placeholder="请输入原密码"></el-input>
              </el-form-item>
              <el-form-item label="新密码" prop="newpassword">
                <el-input type="password" v-model="pwdForm.newpassword" auto-complete="off" size="small" placeholder="请输入新密码"></el-input>
              </el-form-item>
              <el-form-item label="确认新密码" prop="surepassword">
                <el-input type="password" v-model="pwdForm.surepassword" auto-complete="off" size="small" placeholder="请输入确认新密码"></el-input>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="editPwd('pwdForm')">提交</el-button>
                <el-button @click="resetForm('pwdForm')">重置</el-button>
              </el-form-item>
            </el-form>
          </div>
        </div>
      </el-col>
      <!-- 修改头像 -->
      <el-col :span="8">
        <div class="area">
          <div class="phonearea">
            <p class="title">
              <i class="fa fa-edit"></i>修改头像</p>
            <div class="imgarea">
              <img :src="infoForm.avatar?infoForm.avatar:'http://qiniu.xiuminglee.cn/vboot/touxiang/vbootdefaultavatar.png'">
              <el-button type="success" @click="toggleShow" size="small">上传
                <i class="el-icon-upload el-icon--right"></i>
              </el-button>
              <my-upload field="file" @crop-upload-success="cropUploadSuccess" v-model="show" :width="300" :height="300" :url="'/system/user/me/avatar'" withCredentials img-format="png"></my-upload>
            </div>
          </div>
        </div>
      </el-col>
      <!-- 绑定邮箱弹框 -->
      <el-dialog title="绑定邮箱" :visible.sync="mailVisible" :show-close="false" :close-on-click-modal="false">
        <el-form :model="mail" ref="mail" :rules="rules" label-width="100px">
          <el-form-item prop="email" label="邮箱">
            <el-input v-model="mail.email"></el-input>
          </el-form-item>
          <el-form-item prop="code" label="验证码">
            <el-col :span="20">
              <el-input v-model="mail.code" size="small"></el-input>
            </el-col>
            <el-col :span="4">
              <el-button size="mini" type="warning" round @click="sendCode" :disabled="getCodeBtnDisabled">{{getCodeBtn}}</el-button>
            </el-col>
          </el-form-item>
        </el-form>
        <div slot="footer" class="dialog-footer">
          <el-button @click.native="resetForm('mail')">取消</el-button>
          <el-button type="primary" @click.native="mailSubmit('mail')" :loading="mailLoading">提交</el-button>
        </div>
      </el-dialog>

    </el-row>

  </div>
</template>

<script>
import { getDictInfoById, isObjectValueEqual } from "@/utils/tools";
import myUpload from "vue-image-crop-upload";
export default {
  data() {
    // 附带callback(),是在明确通过验证的情况下去掉输入框上的loading
    // let validateEmail = (rule, value, callback) => {
    //   if (value == "") {
    //     callback(new Error("请输入邮箱~"));
    //     return;
    //   }
    //   let emailRegex = /^([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+@([a-zA-Z0-9]+[_|\_|\.]?)*[a-zA-Z0-9]+\.[a-zA-Z]{2,3}$/;
    //   if (!emailRegex.test(value)) {
    //     callback(new Error("邮箱格式不正确！"));
    //   } else {
    //     callback();
    //   }
    // };
    // let validatePhone = (rule, value, callback) => {
    //   if (value == "") {
    //     callback(new Error("请输入手机号码~"));
    //   }
    //   {
    //     let phoneRegex = /^1[34578]\d{9}$/;
    //     if (!phoneRegex.test(value)) {
    //       callback(new Error("手机号码格式不正确！"));
    //     } else {
    //       callback();
    //     }
    //   }
    // };
    // validateField:对部分表单字段进行校验的方法
    let validateNewpassword = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入新密码"));
      } else {
        if (this.pwdForm.surepassword !== "") {
          this.$refs.pwdForm.validateField("surepassword");
        }
        callback();
      }
    };
    let validateSurepassword = (rule, value, callback) => {
      if (value === "") {
        callback(new Error("请输入确认密码"));
      } else if (value !== this.pwdForm.newpassword) {
        callback(new Error("两次输入密码不一致!"));
      } else {
        callback();
      }
    };
    return {
      infoForm: {
        username: "",
        name: "",
        sex: "",
        mail: "",
        mobilephone: "",
        address: ""
      },
      pwdForm: {
        password: "",
        newpassword: "",
        surepassword: ""
      },
      infoRules: {
        name: [
          { required: true, message: "请输入昵称", trigger: "blur" },
          { min: 2, max: 8, message: "长度在 2 到 8 个字符", trigger: "blur" }
        ],
        sex: [{ required: true, message: "请选择性别", trigger: "change" }]
        // ,
        // email: [{ required: true, validator: validateEmail, trigger: "blur" }],
        // telphone: [
        //   { required: true, validator: validatePhone, trigger: "blur" }
        // ]
      },
      pwdRules: {
        password: [
          { required: true, message: "请输入原密码", trigger: "blur" }
        ],
        newpassword: [
          { required: true, validator: validateNewpassword, trigger: "blur" }
        ],
        surepassword: [
          { required: true, validator: validateSurepassword, trigger: "blur" }
        ]
      },
      // 字典
      dictSex: [],
      // 比较数据，用于和infoForm做比较
      oriInfoForm: {},
      editBtn: true,
      show: false,
      /**绑定邮箱 */
      mailVisible: false,
      mailLoading: false,
      mail: {
        email: null
      },
      rules: {
        email: [
          { required: true, message: "请输入邮箱地址", trigger: "blur" },
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],
        code: [
          { required: true, message: "请输入验证码", trigger: "blur" },
          { min: 6, max: 6, message: "长度为6个字符", trigger: "blur" }
        ]
      },
      /**获取验证码相关 */
      getCodeBtn: "获取验证码",
      getCodeBtnDisabled: false,
      time: 60
    };
  },
  created() {
    // this.showUsername();
  },
  mounted() {
    this.getUserInfo();
    this.getDictInInfo();
  },
  computed: {},
  /**
   * 监听事件
   */
  watch: {
    /**监听infoForm表单的变化 */
    infoForm: {
      handler() { // handler(val, oldVal)
        //比较跟原始的数据是否相等。
        this.editBtn = isObjectValueEqual(this.infoForm, this.oriInfoForm);
      },
      deep: true
    }
  },
  components: {
    "my-upload": myUpload
  },
  methods: {
    //获取字典数据
    getDictInInfo() {
      getDictInfoById(28).then(res => {
        this.dictSex = res.data;
      });
    },
    /**
     * 查询用户信息
     */
    getUserInfo() {
      this.$get("/system/user/me/current")
        .then(res => {
          this.infoForm = res.data;
          this.oriInfoForm = JSON.parse(JSON.stringify(res.data));
          this.$store.commit("SET_INFO", res.data);
        })
        .catch(err => {
          this.$message.error(err);
        });
    },
    /**
     * 修改个人资料
     */
    editInfo(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          //验证成功的处理
          this.$put("/system/user/me/info", {
            id: this.infoForm.id,
            name: this.infoForm.name,
            sex: this.infoForm.sex,
            address: this.infoForm.address
          })
            .then(res => {
              this.$message({
                message: res.msg,
                type: "success"
              });
              this.getUserInfo();
            })
            .catch(err => {
              this.$message.error("错了哦，修改个人资料失败！" + err);
            });
        } else {
          this.$message.error("请填全信息！");
          return false;
        }
      });
    },
    /**修改密码 */
    editPwd(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.$put("/system/user/me/pwd", this.pwdForm)
            .then(res => {
              this.$message({
                message: "修改密码成功！",
                type: "success"
              });
              this.resetForm("pwdForm");
            })
            .catch(err => {
              this.$message.error(err);
            });
        } else {
          //验证失败
          this.$message.error("请填写正确信息！");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    /**头像 */
    toggleShow() {
      this.show = !this.show;
    },
    cropUploadSuccess(jsonData, field) {
      if (jsonData.status === 200) {
        this.getUserInfo();
        this.$message({
          message: jsonData.msg,
          type: "success"
        });
      } else {
        this.$message.error("更新头像失败！");
      }
    },
    /**绑定 */
    bindMail() {
      this.mailVisible = true;
    },
    sendCode() {
      if (this.mail.email) {
        this.getCodeBtnDisabled = true;
        this.$get("/system/mail/me/code", {
          email: this.mail.email
        })
          .then(res => {
            this.$message({
              message: res.msg,
              type: "success"
            });
            let me = this;
            let interval = window.setInterval(function() {
              me.getCodeBtn = "（" + me.time + "秒）";
              --me.time;
              if (me.time < 0) {
                me.getCodeBtn = "获取验证码";
                me.time = 60;
                me.getCodeBtnDisabled = false;
                window.clearInterval(interval);
              }
            }, 1000);
          })
          .catch(err => {
            this.$message.error(err);
          });
      } else {
        this.$message({
          message: "请填写邮箱地址!",
          type: "warning"
        });
      }
    },
    mailSubmit(formName) {
      //判断验证码是否为空
      this.$refs[formName].validate(valid => {
        if (valid) {
          //验证成功的处理
          this.mailLoading = true;
          this.$post("/system/mail/me/code", this.mail)
            .then(res => {
              this.$message({
                message: res.msg,
                type: "success"
              });
              this.mailLoading = false;
              this.mailVisible = false;
              this.getUserInfo();
            })
            .catch(err => {
              this.$message.error(err);
              this.mailLoading = false;
            });
        } else {
          //验证失败
          return false;
        }
      });
    },
    bindPhone() {
      this.$message({
        message: "与绑定邮箱类似,忽略!",
        type: "warning"
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
      this.mailLoading = false;
      this.mailVisible = false;
    }
  }
};
</script>

<style lang="scss" scoped>
.info_container {
  padding: 10px;
  margin: 0 10px;
  overflow: auto;
}
.title {
  text-align: center;
  width: 100%;
  height: 30px;
  line-height: 30px;
  cursor: pointer;
  background-color: #3bc5ff;
  border: 1px solid #3bc5ff;
  color: white;
  display: block;
  .fa {
    margin-right: 5px;
  }
}
.info_row {
  .area {
    border: 1px solid #dfdfdf;
    height: 100%;
    font-size: 14px;
    padding: 10px;
    .form {
      width: 90%;
      margin-top: 20px;
    }
    .imgarea {
      text-align: center;
      padding: 10px;
      img {
        width: 120px;
        height: 120px;
        border-radius: 50%;
      }
    }
  }
}
</style>
