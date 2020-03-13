<template>
  <div class="app-container calendar-list-container">
    <!-- 头部 -->
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-input placeholder="用户名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="btn_add" @click="handleAdd" type="primary">新增</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="btn_role" @click="handleRole" type="primary" :disabled="roleBtn">角色部门设置</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" disabled>批量导入用户</el-button>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" disabled>导出用户</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 列表 -->
    <el-table :data="userData" border highlight-current-row @current-change="handleCurrentChange" style="width: 100%;">
      <el-table-column align="center" type="index">
      </el-table-column>
      <el-table-column align="center" prop="id" label="ID" width="50" sortable>
      </el-table-column>
      <el-table-column align="center" prop="name" label="昵称" width="120" sortable>
      </el-table-column>
      <el-table-column align="center" prop="username" label="用户名" width="120">
      </el-table-column>
      <el-table-column align="center" prop="roleName" label="角色" width="120">
      </el-table-column>
      <el-table-column align="center" prop="deptName" label="部门" width="100">
      </el-table-column>
      <el-table-column align="center" prop="mobilephone" label="手机号">
      </el-table-column>
      <el-table-column align="center" prop="mail" label="邮箱">
      </el-table-column>
      <el-table-column align="center" prop="address" label="地址">
      </el-table-column>
      <el-table-column align="center" label="操作" width="180">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.$index, scope.row)" :disabled="scope.row.id ===1">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)" :disabled="scope.row.id ===1">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 列表结束 -->
    <!--分页条-->
    <el-col :span="24" class="pagination-container">
      <el-pagination layout="prev, pager, next" @current-change="handlePageChange" :page-size="10" :total="total" style="float:right;">
      </el-pagination>
    </el-col>
    <!--分页条结束-->
    <!-- 新增用户弹窗 -->
    <el-dialog title="新增用户" :visible.sync="addFormVisible" :show-close="false" :close-on-click-modal="false">
      <el-form :rules="formRules" :model="addForm" ref="addForm" label-width="100px">
        <el-form-item prop="username" label="用户名">
          <el-input placeholder="用户名" v-model="addForm.username"></el-input>
        </el-form-item>
        <el-form-item prop="password" label="密码">
          <el-input placeholder="密码" v-model="addForm.password"></el-input>
        </el-form-item>
        <el-form-item prop="mobilephone" label="手机号">
          <el-input placeholder="手机号" v-model="addForm.mobilephone"></el-input>
        </el-form-item>
        <el-form-item prop="mail" label="邮箱">
          <el-input placeholder="邮箱" v-model="addForm.mail"></el-input>
        </el-form-item>
        <el-form-item label="地址">
          <el-input placeholder="地址" v-model="addForm.address"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="addFormVisible = false;resetForm('addForm')">取消</el-button>
        <el-button type="primary" @click.native="addSubmit('addForm')" :loading="addLoading">提交</el-button>
      </div>
    </el-dialog>
    <!-- 新增用户弹窗结束 -->
    <!-- 角色设置弹窗 -->
    <el-dialog title="角色设置" :visible.sync="roleFormVisible" :show-close="false" :close-on-click-modal="false">
      <el-form :inline="true">
        <el-form-item>
          <el-button type="danger" round disabled>当前用户</el-button>
        </el-form-item>
        <el-form-item label="用户名">
          <el-input placeholder="用户名" v-model="currentUser.username" disabled=""></el-input>
        </el-form-item>
        <el-form-item label="昵称">
          <el-input placeholder="昵称" v-model="currentUser.name" disabled=""></el-input>
        </el-form-item>
      </el-form>
      <hr/>
      <el-form :inline="true">
        <el-form-item label="用户角色">
          <el-select v-model="currentUser.roleId" placeholder="选择用户角色">
            <el-option v-for="item in roles" :key="item.id" :label="item.nameZh" :value="item.id">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="所属部门">
          <el-input v-model="currentUser.deptName" placeholder="选择部门" @focus="handleDept()" readonly></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="roleCancel()">取消</el-button>
        <el-button type="primary" @click.native="roleSubmit()" :loading="roleLoading">提交</el-button>
      </div>
    </el-dialog>
    <!-- 角色设置弹窗结束 -->
    <!-- 选择部门弹窗 -->
    <el-dialog title="请选择部门" :visible.sync="dialogDeptVisible">
      <el-tree class="filter-tree" node-key="id" highlight-current :data="deptTreeData" :default-expanded-keys="aExpandedKeys" :props="defaultProps" @node-click="getNodeData">
      </el-tree>
    </el-dialog>
    <!-- 选择部门弹窗结束 -->
  </div>
</template>

<script>
import { isvalidPhone, isSimpleMail } from "@/utils/validate.js";
import { hasPermission } from "@/utils/auth";
export default {
  data() {
    // 自定义的验证规则
    // 验证手机号非必填。
    let chackMobile = (rule, value, callback) => {
      if (!value) {
        //没有值跳过
        callback();
      } else {
        if (!isvalidPhone(value)) {
          callback(new Error("请输入正确的11位手机号码"));
        } else {
          callback();
        }
      }
    };
    //验证邮箱非必填
    let chackMail = (rule, value, callback) => {
      if (!value) {
        //没有值跳过
        callback();
      } else {
        if (!isSimpleMail(value)) {
          callback(new Error("请输入正确的邮箱号！"));
        } else {
          callback();
        }
      }
    };
    return {
      roleBtn: true,
      userData: [],
      //当前被选中的用户
      currentUser: {},
      //分页
      total: 0,
      page: 1,
      //新增用户相关
      addFormVisible: false,
      addLoading: false,
      addForm: {},
      formRules: {
        username: [
          { required: true, message: "用户名不能为空", trigger: "blur" }
        ],
        password: [
          { required: true, message: "密码不能为空", trigger: "blur" }
        ],
        mobilephone: [{ validator: chackMobile, trigger: "blur" }],
        mail: [{ validator: chackMail, trigger: "blur" }]
      },
      /**角色设置相关 */
      roleFormVisible: false,
      roleLoading: false,
      roles: [],
      role: null,
      /**部门相关 */
      dialogDeptVisible: false,
      //树的数据
      deptTreeData: [],
      // 默认展开的节点
      aExpandedKeys: [1],
      // 树的配置选项
      defaultProps: {
        children: "children",
        label: "name"
      }
    };
  },
  mounted() {
    this.getUsers();
    this.getRoles();
  },
  methods: {
    //获取用户数据
    getUsers() {
      this.$get("/system/user/users", {
        //请求参数
        page: this.page
      })
        .then(res => {
          // console.log(res);
          this.userData = res.data.records;
          this.total = res.data.total;
        })
        .catch(err => {
          this.$message.error("获取全部用户数据失败！");
        });
    },
    //添加按钮
    handleAdd() {
      this.addFormVisible = true;
    },
    //添加用户
    addSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.addLoading = true;
          //验证成功的处理
          this.$post("/system/user", this.addForm)
            .then(res => {
              this.$message({
                message: res.msg,
                type: "success"
              });
              this.addLoading = false;
              this.addFormVisible = false;
              this.resetForm("addForm");
              this.page = 1;
              this.getUsers();
            })
            .catch(err => {
              this.$message.error(err);
              this.addLoading = false;
            });
        } else {
          //验证失败
          this.$message.error("请完善信息后提交！");
          return false;
        }
      });
    },
    //角色配置相关
    /**获取所有角色 */
    getRoles() {
      this.$get("/system/role/roles")
        .then(res => {
          this.roles = res.data;
        })
        .catch(err => {
          //失败！
          this.$message.error(err);
        });
    },
    handleRole() {
      if (!this.currentUser.id) {
        this.$message.error("请选择你要编辑的用户！");
      } else {
        this.roleFormVisible = true;
      }
    },
    roleSubmit() {
      this.$confirm("你确认修改此用户的角色和部门信息吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          this.roleLoading = true;
          this.$post("/system/user/roleDept", this.currentUser)
            .then(res => {
              this.$message({
                message: res.msg,
                type: "success"
              });
              this.roleFormVisible=false;
              this.roleLoading = false;
              this.getUsers();
            })
            .catch(err => {
              this.$message.error(err);
              this.roleLoading = false;              
              this.roleFormVisible=false;              
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消"
          });
        });
    },
    roleCancel() {
      this.roleFormVisible = false;
      this.currentUser = {};
    },
    //部门设置按钮
    handleDept() {
      this.getDeptTree();
      this.dialogDeptVisible = true;
    },
    //获取部门树数据
    getDeptTree() {
      this.$get("/system/dept/deptsTree")
        .then(res => {
          this.deptTreeData = JSON.parse(res.data);
        })
        .catch(err => {
          this.$message.error("获取部门树错误！");
        });
    },
    // 部门树节点被点击时的回调
    getNodeData(data) {
      this.currentUser.deptId = data.id;
      this.currentUser.deptName = data.name;
      this.dialogDeptVisible = false;
    },
    //用户列表被选择时
    handleCurrentChange(data) {
      this.currentUser = JSON.parse(JSON.stringify(data));
      this.roleBtn = false;
    },
    //编辑
    handleEdit(index, row) {
      this.$message("太简单了，忽略！");
    },
    //删除
    handleDel(index, row) {
      this.$message("太简单了，忽略！");
    },
    //页码变化
    handlePageChange(val) {
      this.page = val;
      this.getUsers();
    },
    //重置from
    resetForm(formName) {
      this.$refs[formName].resetFields();
    }
  },
  computed:{
    btn_add(){
      return hasPermission("/system/user","POST");
    },
    btn_role(){
      return hasPermission("/system/user/roleDept","POST");
    }
  }
};
</script>

<style scoped>
</style>