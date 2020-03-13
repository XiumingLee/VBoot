<template>
    <div class="app-container calendar-list-container">
        <!-- 头部信息 -->
        <div class="filter-container">
            <el-button-group>
                <el-button type="primary" v-if="btn_add" icon="plus" @click="handlerAdd">添加</el-button>
                <el-button type="primary" v-if="btn_edit" icon="edit" @click="handlerEdit">编辑</el-button>
                <el-button type="primary" v-if="btn_del" icon="delete" @click="handleDelete">删除</el-button>
            </el-button-group>
        </div>
        <!-- 内容 -->
        <el-row>
            <!-- 左侧树控件 -->
            <el-col :span="8" style='margin-top:15px;'>
                <el-tree class="filter-tree" node-key="id" highlight-current :data="deptTreeData" :default-expanded-keys="aExpandedKeys" :props="defaultProps" @node-click="getNodeData">
                </el-tree>
            </el-col>
            <!-- 右侧可可编辑区 -->
            <el-col :span="16" style='margin-top:15px;'>
                <el-card class="box-card">
                    <el-form :label-position="labelPosition" label-width="80px" :model="form" ref="form">
                        <el-form-item label="父级ID" prop="parentid">
                            <el-input v-model="form.parentid" :disabled="true" placeholder="请输入父级节点"></el-input>
                        </el-form-item>
                        <el-form-item label="节点ID" prop="id">
                            <el-input v-model="form.id" :disabled="true" placeholder="请输入节点ID"></el-input>
                        </el-form-item>
                        <el-form-item label="部门名称" prop="name">
                            <el-input v-model="form.name" :disabled="formEdit" placeholder="请输入名称"></el-input>
                        </el-form-item>
                        <el-form-item v-if="formStatus == 'update'">
                            <el-button type="primary" @click="update">更新</el-button>
                            <el-button @click="onCancel">取消</el-button>
                        </el-form-item>
                        <el-form-item v-if="formStatus == 'create'">
                            <el-button type="primary" @click="create">保存</el-button>
                            <el-button @click="onCancel">取消</el-button>
                        </el-form-item>
                    </el-form>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<script>
import { hasPermission } from "@/utils/auth";
export default {
  data() {
    return {
      /**固定不变区 */
      labelPosition: "right",
      /**固定不变区结束 */
      //树的数据
      deptTreeData: [],
      // 默认展开的节点
      aExpandedKeys: [],
      // 树的配置选项
      defaultProps: {
        children: "children",
        label: "name"
      },
      //右侧表单相关
      form: {},
      formEdit: true,
      formStatus: "",
      currentId: 0
    };
  },
  mounted() {
    this.getDeptTree();
  },
  methods: {
    //添加按钮
    handlerAdd() {
      this.resetForm();
      this.formEdit = false;
      this.formStatus = "create";
    },
    //编辑按钮
    handlerEdit() {
      if (this.form.id) {
        this.formEdit = false;
        this.formStatus = "update";
      } else {
        this.$message({
          message: "请选择你要编辑的菜单项！",
          type: "warning"
        });
      }
    },
    //删除按钮
    handleDelete() {
      if (this.currentId === 0) {
        this.$message({
          message: "请选择你要删除的部门！",
          type: "warning"
        });
      } else {
        this.$confirm("你确认删除此部门及其子部门吗?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        })
          .then(() => {
            this.$deleteRequest(`/system/dept/${this.currentId}`)
              .then(res => {
                this.getDeptTree();
                this.resetForm();
                this.onCancel();
                this.$message({
                  message: res.msg,
                  type: "success"
                });
              })
              .catch(err => {});
          })
          .catch(() => {
            this.$message({
              type: "info",
              message: "已取消"
            });
          });
      }
    },
    //获取部门树形结构
    getDeptTree() {
      this.$get("/system/dept/deptsTree")
        .then(res => {
          this.deptTreeData = JSON.parse(res.data);
        })
        .catch(err => {
          this.$message.error("获取部门树错误！");
        });
    },
    // 节点被点击时的回调
    getNodeData(data) {
      this.form = data;
      this.currentId = data.id;
      this.formEdit = true;
      this.formStatus = "";
    },
    update() {
      this.$confirm("你确认修改该部门吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          //点确认要做的事
          this.$put("/system/dept/dept", this.form)
            .then(res => {
              this.getDeptTree();
              this.resetForm();
              this.$message({
                message: res.msg,
                type: "success"
              });
            })
            .catch(err => {
              //失败！
              this.$message.error(`更新部门错误！`);
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消"
          });
        });
    },
    create() {
      this.$confirm("你确认添加此部门吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          //点确认要做的事
          this.$post("/system/dept/dept", this.form)
            .then(res => {
              //成功！
              if (res.status === 200) {
                this.getDeptTree();
                this.resetForm();
                this.$message({
                  message: res.msg,
                  type: "success"
                });
              } else {
                this.$message.error(res.msg);
              }
            })
            .catch(err => {
              //失败！
              this.$message.error(`添加部门失败！`);
            });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "已取消"
          });
        });
    },
    onCancel() {
      this.formEdit = true;
      this.formStatus = "";
    },
    // 重置表格
    resetForm() {
      this.form = {
        parentid: this.currentId
      };
      this.formEdit = true;
    }
  },
  computed:{
    /**计算按钮是否显示 */
    btn_add() {
      return hasPermission("/system/dept/dept", "POST");
    },
    btn_edit(){
      return hasPermission("/system/dept/dept","PUT");
    },
    btn_del(){
      return hasPermission("/system/dept/*","DELETE");
    }
  }
};
</script>

<style scoped>
</style>