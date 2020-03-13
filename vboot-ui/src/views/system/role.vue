<template>
  <div class="app-container calendar-list-container">
    <div class="filter-container">
      <el-form :inline="true">
        <el-form-item>
          <el-input v-model="selectField" placeholder="角色名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="selectBtn">查询</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="btn_add" @click="handleAdd" type="primary">新增</el-button>
        </el-form-item>
        <el-form-item>
          <el-button v-if="btn_auth" @click="handleAuth" type="primary" :disabled="authBtn">设置权限</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 列表 -->
    <el-table :data="roles" border highlight-current-row @current-change="handleCurrentChange" style="width: 100%;">
      <el-table-column align="center" type="index">
      </el-table-column>
      <el-table-column align="center" prop="id" label="ID" sortable>
      </el-table-column>
      <el-table-column align="center" prop="name" label="英文名称" sortable>
      </el-table-column>
      <el-table-column align="center" prop="nameZh" label="中文名称">
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button type="primary" size="small" @click="handleEdit(scope.$index, scope.row)" :disabled="btn_edit">编辑</el-button>
          <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)" :disabled="btn_del">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 列表结束 -->
    <!--分页条-->
    <el-col :span="24" class="pagination-container">
      <el-pagination layout="prev, pager, next" @current-change="handlePageChange" :page-size="10" :total="total" style="float:right;">
      </el-pagination>
    </el-col>
    <!-- 新增角色弹窗 -->
    <el-dialog title="新增角色" :visible.sync="addFormVisible" :show-close="false" :close-on-click-modal="false">
      <el-form :inline="true" :model="addForm" ref="addForm">
        <div>
          <el-form-item prop="name" label="英文名称" :rules="{required: true, message: '英文名称不能为空', trigger: 'blur'}">
            <el-input placeholder="英文名称" v-model="addForm.name"></el-input>
          </el-form-item>
          <el-form-item prop="nameZh" label="中文名称" :rules="{required: true, message: '中文名称不能为空', trigger: 'blur'}">
            <el-input placeholder="中文名称" v-model="addForm.nameZh"></el-input>
          </el-form-item>
        </div>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="addFormVisible = false;resetForm('addForm')">取消</el-button>
        <el-button type="primary" @click.native="addSubmit('addForm')" :loading="addLoading">提交</el-button>
      </div>
    </el-dialog>
    <!-- 新增角色弹窗结束 -->
    <!-- 编辑角色弹窗 -->
    <el-dialog title="编辑角色" :visible.sync="editFormVisible" :show-close="false" :close-on-click-modal="false">
      <el-form :model="editForm" ref="editForm">
        <el-form-item prop="name" label="id">
          <el-input placeholder="角色ID" v-model="editForm.id" readonly="readonly"></el-input>
        </el-form-item>
        <el-form-item prop="name" label="英文名称" :rules="{required: true, message: '英文名称不能为空', trigger: 'blur'}">
          <el-input placeholder="英文名称" v-model="editForm.name"></el-input>
        </el-form-item>
        <el-form-item prop="nameZh" label="中文名称" :rules="{required: true, message: '中文名称不能为空', trigger: 'blur'}">
          <el-input placeholder="中文名称" v-model="editForm.nameZh"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="editFormVisible = false;resetForm('editForm')">取消</el-button>
        <el-button type="primary" @click.native="editSubmit('editForm')" :loading="editLoading">提交</el-button>
      </div>
    </el-dialog>
    <!-- 编辑角色弹窗结束 -->
    <!-- 设置权限弹窗 -->
    <el-dialog title="设置权限" :visible.sync="authVisible" :show-close="false" :close-on-click-modal="false">
      <el-tree :data="authTree" check-strictly default-expand-all show-checkbox node-key="id" ref="tree" highlight-current :props="defaultProps">
      </el-tree>
      <div slot="footer" class="dialog-footer">
        <el-button @click.native="authVisible = false">取消</el-button>
        <el-button type="primary" @click.native="authSubmit()" :loading="authLoading">提交</el-button>
      </div>
    </el-dialog>
    <!-- 设置权限弹窗结束 -->
  </div>
</template>

<script>
import { hasPermission } from "@/utils/auth";
export default {
  data() {
    return {
      roles: [],
      //控制设置权限按钮的禁用状态
      authBtn: true,
      authVisible: false,
      authLoading: false,
      authTree: [],
      currentAuth: [],
      cheackRole: null,
      defaultProps: {
        children: "children",
        label: "label"
      },
      /**编辑 */
      editForm: {},
      editFormVisible: false,
      editLoading: false,
      /**添加 */
      addForm: {},
      addFormVisible: false,
      addLoading: false,
      //分页
      total: 0,
      page: 1,
      // 查询
      selectField:''
    };
  },
  mounted() {
    this.getRoles();
  },
  methods: {
    /**获取角色信息 */
    getRoles() {
      this.$get("/system/role/roles", {
        //请求参数
        page: this.page,
        selectField:this.selectField
      })
        .then(res => {
          this.total = res.data.total;
          this.roles = res.data.records;
          this.selectField = '';
        })
        .catch(err => {
          //失败！
          this.$message.error(`错误-->${err}`);
        });
    },
    // 新增按钮
    handleAdd() {
      this.addFormVisible = true;
    },
    addSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          //验证成功的处理
          this.addLoading = true;
          this.$post("/system/role", this.addForm)
            .then(res => {
              //成功！
              if (res.status === 200) {
                //成功！
                this.$message({
                  message: res.msg,
                  type: "success"
                });
                this.addFormVisible = false;
                this.addLoading = false;
                this.authBtn = true;
                this.getRoles();
                this.addForm = {};
              } else {
                this.$message.error(res.msg);
              }
            })
            .catch(err => {
              //失败！
              this.$message.error(err);
            });
        } else {
          //验证失败
          this.$message.error("请填全信息！");
          return false;
        }
      });
    },
    //单选选中时调用
    handleCurrentChange(val) {
      this.authBtn = false;
      this.cheackRole = val.id;
    },
    //行编辑按钮
    handleEdit(index, row) {
      this.editForm = Object.assign({}, row);
      this.editFormVisible = true;
    },
    editSubmit(formName) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          //验证成功的处理
          this.editLoading = true;
          this.$put("/system/role", this.editForm)
            .then(res => {
              //成功！
              if (res.status === 200) {
                //成功！
                this.$message({
                  message: res.msg,
                  type: "success"
                });
                this.editFormVisible = false;
                this.editLoading = false;
                this.authBtn = true;
                this.getRoles();
                this.editForm = {};
              } else {
                this.$message.error(res.msg);
              }
            })
            .catch(err => {
              //失败！
              this.$message.error(err);
            });
        } else {
          //验证失败
          this.$message.error("请填全信息！");
          return false;
        }
      });
    },
    //行删除按钮
    handleDel(index, row) {
      this.$message.error(`抱歉！不能删除哦！`);
    },
    //重置from
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },

    /**权限配置相关 */
    //获取菜单路由数据
    getData() {
      this.$get("/system/menu/menuOptions")
        .then(res => {
          if (res.status === 200) {
            this.authTree = JSON.parse(res.data);
          }
        })
        .catch(err => {
          console.log(err);
        });
    },
    //获取当前角色id的权限信息
    getCurrentIdData() {
      this.$get(`/system/role/${this.cheackRole}`)
        .then(res => {
          //成功！
          if (res.status === 200) {
            //设置选中的节点
            console.log(res.data);

            this.$refs.tree.setCheckedKeys(res.data);
          } else {
            this.$message.error(res.msg);
          }
        })
        .catch(err => {
          //失败！
          this.$message.error(err);
        });
    },
    /**设置权限按钮 */
    handleAuth() {
      this.authVisible = true;
      if (!(this.authTree.length > 0)) {
        this.getData();
      }
      //设置选中的节点
      this.getCurrentIdData();
    },
    authSubmit() {
      this.authLoading = false;
      // 获取选中的id数组
      let CheckedKeys = this.$refs.tree.getCheckedKeys();
      //获取半选中的id数组
      let HalfCheckedKeys = this.$refs.tree.getHalfCheckedKeys();
      // 将两个数组连接
      let idsArray = CheckedKeys.concat(HalfCheckedKeys);
      let params = {
        rid: this.cheackRole,
        ids: idsArray
      };
      this.$confirm("你确认修改该角色的权限吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          //点确认要做的事
          this.$post("/system/role/menuRole", params)
            .then(res => {
              //成功！
              if (res.status === 200) {
                //成功！
                this.$message({
                  message: res.msg,
                  type: "success"
                });
                this.authLoading = false;
                this.authVisible = false;
              } else {
                this.$message.error(res.msg);
              }
            })
            .catch(err => {
              //失败！
              this.$message.error(err);
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消"
          });
        });
    },
    //设置权限结束
    /**分页 */
    handlePageChange(val) {
      this.page = val;
      this.getRoles();
    },
    /**模糊查询 */
    selectBtn(){
      this.page = 1;
      this.getRoles();
    }
  },
  computed:{
    btn_add(){
      return hasPermission("/system/role","POST");
    },
    btn_auth(){
      return hasPermission("/system/role/menuRole","POST");
    },
    btn_edit(){
      return !(hasPermission("/system/role","PUT"));
    },
    btn_del(){
      return true;
    }
  }
};
</script>

<style scoped>
</style>