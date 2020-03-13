<template>
  <div class="app-container calendar-list-container">
    <el-form :model="codeForm" :rules="rules" ref="codeForm" label-width="110px">
      <!-- 头部 -->
      <div class="filter-container">
        <el-row :gutter="10">
          <el-col :span="8">
            <el-form-item label="代码存放路径:" prop="codePath">
              <el-input v-model="codeForm.codePath"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="操作人:" prop="author">
              <el-input v-model="codeForm.author"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="包名全路径:" prop="packageName">
              <el-input v-model="codeForm.packageName"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="表前缀:" prop="tablePrefix">
              <el-input v-model="codeForm.tablePrefix"></el-input>
            </el-form-item>
          </el-col>
        </el-row>
      </div>
      <!-- 数据表区域 -->
      <el-table :data="dataTables" ref="multipleTable" @selection-change="handleSelectionChange" height="400" border style="width: 100%">
        <el-table-column type="selection" width="60">
        </el-table-column>
        <el-table-column prop="tableName" label="表名">
        </el-table-column>
        <el-table-column prop="tableComment" label="描述">
        </el-table-column>
      </el-table>
    </el-form>
    <el-row style="margin-top: 1rem;">
      <el-col :span="2" :offset="22">
        <el-button type="primary" @click="generateCode">生成代码</el-button>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  data() {
    return {
      codeForm: {
        codePath: "",
        packageName: "cn.xiuminglee.vboot.modules.system",
        author: "",
        tablePrefix: "vb_"
      },
      rules: {
        codePath: [
          { required: true, message: "请输入代码生成的路径", trigger: "blur" }
        ],
        packageName: [
          { required: true, message: "请输入包名", trigger: "blur" }
        ],
        author: [{ required: true, message: "请输入操作人", trigger: "blur" }],
        tablePrefix: [
          { required: true, message: "请输入表前缀", trigger: "blur" }
        ]
      },
      dataTables: [],
      selTableList: [],
      multipleTable: []
    };
  },
  mounted() {
    this.getAllTable();
  },
  methods: {
    /**获取数据库表 */
    getAllTable() {
      this.$get("/system/code/tables")
        .then(res => {
          this.dataTables = res.data;
        })
        .catch(err => {
          this.$message.error(err);
        });
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      this.selTableList = [];
      val.forEach(element => {
        this.selTableList.push(element.tableName);
      });
    },
    /**生产代码 */
    generateCode() {
      if (this.selTableList.length == 0) {
        this.$message.error("请选择你要生成代码的数据表！");
      } else {
        this.$refs.codeForm.validate(valid => {
          if (valid) {
            this.codeForm.tableNames = this.selTableList;
            //验证成功的处理
            this.$post("/system/code/generate", this.codeForm)
              .then(res => {
                this.$message.success("生成代码成功！");
                this.resetData();
              })
              .catch(err => {
                this.$message.error(err);
              });
          } else {
            return false;
          }
        });
      }
    },
    /**重置 */
    resetData() {
      this.$refs.codeForm.resetFields();
      this.$refs.multipleTable.clearSelection();
    }
  }
};
</script>

<style scoped>
</style>
