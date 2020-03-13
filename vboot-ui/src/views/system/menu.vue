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
        <el-tree class="filter-tree" node-key="id" highlight-current :data="treeData" :default-expanded-keys="aExpandedKeys" :filter-node-method="filterNode" :props="defaultProps" @node-click="getNodeData" @node-expand="nodeExpand" @node-collapse="nodeCollapse">
        </el-tree>
      </el-col>
      <!-- 左侧树控件结束-->
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
            <el-form-item label="名称" prop="name">
              <el-input v-model="form.name" :disabled="formEdit" placeholder="请输入名称"></el-input>
            </el-form-item>
            <el-form-item label="图标" prop="iconCls">
              <el-input v-model="form.iconCls" :disabled="formEdit" placeholder="请输入图标"></el-input>
            </el-form-item>
            <el-form-item label="路径(path)" prop="path">
              <el-input v-model="form.path" :disabled="formEdit" placeholder="请输入资源路径（不是菜单时不需要填写）"></el-input>
            </el-form-item>
            <el-form-item label="菜单" prop="ismenu">
              <el-select class="filter-item" v-model="form.ismenu" :disabled="formEdit" placeholder="请选择是否式菜单">
                <el-option v-for="item in dictIsOrNo" :key="item.num" :label="item.name" :value="item.num"> </el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="组件路径" prop="component">
              <el-input v-model="form.component" :disabled="formEdit" placeholder="请输入前端组件路径（不是菜单时不需要填写）"></el-input>
            </el-form-item>
            <el-form-item label="请求路径" prop="url">
              <el-input v-model="form.url" :disabled="isBtn" placeholder="请输入后端请求url"></el-input>
            </el-form-item>
            <el-form-item label="请求方法" prop="method">
              <el-select class="filter-item" v-model="form.method" :disabled="isBtn" placeholder="请选择请求方法">
                <el-option v-for="item in dictRest" :key="item.name" :label="item.name" :value="item.name"> </el-option>
              </el-select>
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
import { getDictInfoById } from "@/utils/tools";
import { hasPermission } from "@/utils/auth";
export default {
  data() {
    return {
      /**固定不变区 */
      labelPosition: "right",
      /**固定不变区结束 */
      //树的数据
      treeData: [],
      // 默认展开的节点
      aExpandedKeys: [],
      // 树的配置选项
      defaultProps: {
        children: "children"
      },
      // 右侧编辑区数据表
      form: {
        name: undefined,
        id: undefined,
        parentid: undefined,
        path: undefined,
        iconCls: undefined,
        component: undefined,
        ismenu: undefined,
        url: undefined,
        method: undefined
      },
      currentId: 0,
      formEdit: true,
      formStatus: "",
      // 字典数据
      dictRest: [],
      dictIsOrNo: []
    };
  },
  mounted() {
    this.getList();
    this.getDict();
    hasPermission();
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
          message: "请选择你要删除的菜单！",
          type: "warning"
        });
      } else {
        this.$confirm("此操作将删除此菜单及其子菜单, 是否继续?", "提示", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(() => {
          this.$deleteRequest(`/system/menu/${this.currentId}`)
            .then(res => {
              this.getList();
              this.resetForm();
              this.onCancel();
              this.$notify({
                title: "成功",
                message: "删除成功",
                type: "success",
                duration: 2000
              });
            })
            .catch(err => {
              this.$message.error(err);
            });
        });
      }
    },

    //获取字典数据
    getDict() {
      getDictInfoById(7).then(res => {
        this.dictRest = res.data;
      }),
        getDictInfoById(4).then(res => {
          this.dictIsOrNo = res.data;
        });
    },
    /**
     * 获取树列表数据
     */
    getList() {
      this.$get("/system/menu/menuOptions")
        .then(res => {
          this.treeData = JSON.parse(res.data);
        })
        .catch(err => {
          this.$message.error(`请求菜单树失败！=>${err}`);
        });
    },
    /**
     * 更新
     */
    update() {
      this.$confirm("你确认更新此菜单吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          //点确认要做的事
          this.$put(`/system/menu`, this.form)
            .then(res => {
              this.getList();
              this.resetForm();
              this.$notify({
                title: "成功",
                message: "更新成功",
                type: "success",
                duration: 2000
              });
            })
            .catch(err => {
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
    /**
     * 添加
     */
    create() {
      this.$confirm("你确认添加此菜单吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          //点确认要做的事
          this.$post(`/system/menu`, this.form)
            .then(res => {
              this.getList();
              this.resetForm();
              this.$notify({
                title: "成功",
                message: "添加成功",
                type: "success",
                duration: 2000
              });
            })
            .catch(err => {
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
    /**
     * 取消
     */
    onCancel() {
      this.formEdit = true;
      this.formStatus = "";
    },
    /**
     * 对树节点进行筛选时执行的方法，返回 true 表示这个节点可以显示，返回 false 则表示这个节点会被隐藏
     */
    filterNode(value, data) {
      if (!value) return true;
      return data.label.indexOf(value) !== -1;
    },
    // 节点被点击时的回调
    getNodeData(data) {
      if (!this.formEdit) {
        this.formStatus = "update";
      }
      this.$get(`/system/menu/${data.id}`)
        .then(res => {
          this.form = res.data;
        })
        .catch(err => {
          this.$message.error(err);
        });
      this.currentId = data.id;
      this.formEdit = true;
      this.formStatus = "";
      // this.showElement = true
    },
    // 节点被展开时触发的事件
    nodeExpand(data) {},
    // 节点被关闭时触发的事件
    nodeCollapse(data) {},
    // 重置表格
    resetForm() {
      this.form = {
        name: undefined,
        id: undefined,
        parentid: this.currentId,
        path: undefined,
        iconCls: undefined,
        component: undefined,
        ismenu: undefined,
        url: undefined,
        method: undefined
      };
      this.formEdit = true;
    }
  },
  computed: {
    // 用于计算form表单中的是否是菜单选项，如果是菜单，则不能填写url和method的。
    isBtn() {
      if (this.form.ismenu === 1 || this.form.ismenu === undefined) {
        this.form.url = undefined;
        this.form.method = undefined;
        return true;
      } else {
        return false;
      }
    },
    /**计算按钮是否显示 */
    btn_add() {
      return hasPermission("/system/menu", "POST");
    },
    btn_edit(){
      return hasPermission("/system/menu","PUT");
    },
    btn_del(){
      return hasPermission("/system/menu/*","DELETE");
    }
  }
};
</script>

<style scoped>
</style>