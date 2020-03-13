<template>
    <div class="app-container calendar-list-container">
        <div class="filter-container">
            <el-form :inline="true">
                <el-form-item>
                    <el-input v-model="selectDictName" placeholder="字典名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="selectByName" type="primary">查询</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button v-if="btn_add" @click="handleAdd" type="primary">新增</el-button>
                </el-form-item>
            </el-form>
        </div>
        <!-- 列表 -->
        <el-table :data="dicts" border highlight-current-row style="width: 100%;">
            <el-table-column align="center" type="index">
            </el-table-column>
            <el-table-column align="center" prop="id" label="ID" sortable>
            </el-table-column>
            <el-table-column align="center" prop="name" label="字典名称" sortable>
            </el-table-column>
            <el-table-column align="center" prop="detail" label="详情">
            </el-table-column>
            <el-table-column align="center" label="操作">
                <template slot-scope="scope">
                    <el-button type="primary" :disabled="btn_edit" size="small" @click="handleEdit(scope.$index, scope.row)">编辑</el-button>
                </template>
            </el-table-column>
        </el-table>
        <!--分页条-->
        <el-col :span="24" class="pagination-container">
            <el-pagination layout="prev, pager, next" @current-change="handleCurrentChange" :page-size="10" :total="total" style="float:right;">
            </el-pagination>
        </el-col>
        <!-- 新增字典 -->
        <!--新增界面-->
        <el-dialog title="新增" :visible.sync="addFormVisible" :show-close="false" :close-on-click-modal="false">
            <el-form :inline="true" :model="addEntryForm" ref="addEntryForm">
                <div>
                    <el-form-item prop="name" label="字典名称" :rules="{required: true, message: '字典名称不能为空', trigger: 'blur'}">
                        <el-input placeholder="字典名称" v-model="addEntryForm.name"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button @click="addDomain" type="primary">添加条目</el-button>
                    </el-form-item>
                </div>
                <!-- 增加的条目 -->
                <div v-for="(domain, index) in addEntryForm.children" :key="domain.key">
                    <el-form-item :label="'值' + index" :prop="'children.' + index + '.num'" :rules="[{ required: true, message: '值不能为空'},{ type: 'number', message: '值必须为数字值'}]">
                        <el-input placeholder="必须是数值型" v-model.number="domain.num" type="number"></el-input>
                    </el-form-item>
                    <el-form-item label="名称" :prop="'children.' + index + '.name'" :rules="{required: true, message: '名称不能为空', trigger: 'blur'}">
                        <el-input placeholder="名称" v-model="domain.name"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="danger" @click.prevent="removeDomain(domain)">删除</el-button>
                    </el-form-item>
                </div>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="addFormVisible = false;resetForm('addEntryForm')">取消</el-button>
                <el-button type="primary" @click.native="addSubmit('addEntryForm',1)" :loading="addLoading">提交</el-button>
            </div>
        </el-dialog>
        <!-- 编辑字典 -->
        <el-dialog title="编辑" :visible.sync="editFormVisible" :show-close="false" :close-on-click-modal="false">
            <el-form :inline="true" :model="addEntryForm" ref="addEntryForm">
                <div>
                    <el-form-item prop="name" label="字典名称" :rules="{required: true, message: '字典名称不能为空', trigger: 'blur'}">
                        <el-input placeholder="字典名称" v-model="addEntryForm.name"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button @click="addDomain" type="primary">添加条目</el-button>
                    </el-form-item>
                </div>
                <!-- 增加的条目 -->
                <div v-for="(domain, index) in addEntryForm.children" :key="domain.key">
                    <el-form-item :label="'值' + index" :prop="'children.' + index + '.num'" :rules="[{ required: true, message: '值不能为空'},{ type: 'number', message: '值必须为数字值'}]">
                        <el-input placeholder="必须是数值型" v-model.number="domain.num" type="number"></el-input>
                    </el-form-item>
                    <el-form-item label="名称" :prop="'children.' + index + '.name'" :rules="{required: true, message: '名称不能为空', trigger: 'blur'}">
                        <el-input placeholder="名称" v-model="domain.name"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="danger" @click.prevent="removeDomain(domain)">删除</el-button>
                    </el-form-item>
                </div>
            </el-form>
            <div slot="footer" class="dialog-footer">
                <el-button @click.native="editFormVisible = false;resetForm('addEntryForm')">取消</el-button>
                <el-button type="primary" :loading="addLoading" @click.native="addSubmit('addEntryForm',2)">提交</el-button>
            </div>
        </el-dialog>
    </div>
</template>

<script>
import { getDictInfoById } from "@/utils/tools";
import { hasPermission } from "@/utils/auth";
export default {
  data() {
    return {
      dicts: [],
      //分页
      total: 0,
      page: 1,
      /**添加用到的*/
      addFormVisible: false,
      addLoading: false,
      //添加时新增条目数组
      addEntryForm: {
        num: 0,
        //字典名称
        name: "",
        //更多条目信息
        children: []
      },
      /**编辑 */
      editFormVisible: false,
      editLoading: false,
      /**模糊查询 */
      selectDictName:'',
    };
  },
  mounted() {
    this.getDicts();
  },
  methods: {
    getDicts() {
      this.$get("/system/dict/dicts", {
        //请求参数
        page: this.page
      })
        .then(res => {
          this.dicts = res.data.data;
          this.total = res.data.total;
        })
        .catch(err => {
          //失败！
          this.$message.error(`错误-->${err}`);
        });
    },
    /**分页 */
    handleCurrentChange(val) {
      this.page = val;
      this.getDicts();
    },
    /**新增按钮 */
    //打开新增界面
    handleAdd() {
      this.addFormVisible = true;
    },
    // 新增界面添加条目
    addDomain() {
      this.addEntryForm.children.push({
        num: 1,
        name: "",
        key: Date.now()
      });
    },
    // 新增界面删除条目
    removeDomain(item) {
      var index = this.addEntryForm.children.indexOf(item);
      if (index !== -1) {
        this.addEntryForm.children.splice(index, 1);
      }
    },
    //新增或修改字典
    addSubmit(formName, num) {
      this.$refs[formName].validate(valid => {
        if (valid) {
          this.addLoading = true;
          this.$post("/system/dict", this.addEntryForm)
            .then(res => {
              //成功！
              if (res.status === 200) {
                //成功！
                this.$message({
                  message: res.msg,
                  type: "success"
                });
                this.getDicts();
                this.resetForm("addEntryForm");
                this.addLoading = false;
                if (num === 1) {
                  this.addFormVisible = false;
                } else {
                  this.editFormVisible = false;
                }
              } else {
                this.$message.error(res.msg);
              }
            })
            .catch(err => {
              //失败！
              this.$message.error(err);
            });
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    //重置from
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    /**字典编辑 */
    //字典编辑
    handleEdit(index, row) {
      this.addEntryForm.id = Object.assign(row.id);
      this.addEntryForm.num = Object.assign(row.num);
      this.addEntryForm.name = Object.assign(row.name);
      //获取相关信息赋值给addEntryForm
      getDictInfoById(row.id).then(res => {
        this.addEntryForm.children = res.data;
      });
      //打开编辑窗口
      this.editFormVisible = true;
    },
    /**模糊查询 */
    selectByName(){
        if (this.selectDictName===''||this.selectDictName === null||this.selectDictName === undefined) {
            this.page = 1;
            this.getDicts();
        } else {
            this.$get(`/system/dict/name`,{
                name:this.selectDictName
            }).then(res=>{
                this.page = 1;
                this.total = 10;
                this.dicts = res.data;
                this.selectDictName = '';
            }).catch(err=>{
                this.$message.error(err);
            })
        }
    }
  },
  computed:{
      /**计算按钮是否显示 */
    btn_add() {
      return hasPermission("/system/dict", "POST");
    },
    btn_edit(){
      return !(hasPermission("/system/dict","PUT"));
    },
  }
};
</script>

<style scoped>
</style>