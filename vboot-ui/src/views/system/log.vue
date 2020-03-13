<template>
  <div class="app-container calendar-list-container">
    <div class="filter-container">
      <!-- <el-form :inline="true">
                <el-form-item>
                    <el-input v-model="selectDictName" placeholder="字典名称"></el-input>
                </el-form-item>
                <el-form-item>
                    <el-button @click="selectByName" type="primary">查询</el-button>
                </el-form-item>
                <el-form-item>
                    <el-button v-if="btn_add" @click="handleAdd" type="primary">新增</el-button>
                </el-form-item>
            </el-form> -->
    </div>
    <!-- 列表 -->
    <el-table :data="logs" border highlight-current-row @selection-change="selsChange" style="width: 100%;">
      <el-table-column align="center" type="selection" width="55">
      </el-table-column>
      <el-table-column align="center" type="index">
      </el-table-column>
      <el-table-column align="center" prop="id" label="ID" sortable width="80">
      </el-table-column>
      <el-table-column align="center" prop="name" label="异常名称" sortable>
      </el-table-column>
      <el-table-column align="center" prop="code" label="状态码" width="80">
      </el-table-column>
      <el-table-column align="center" prop="msg" show-overflow-tooltip label="信息">
      </el-table-column>
      <el-table-column align="center" prop="details" show-overflow-tooltip label="详情">
      </el-table-column>
      <el-table-column align="center" prop="other" show-overflow-tooltip label="其他参数">
      </el-table-column>
      <el-table-column align="center" prop="createdata" :formatter="formatter" label="创建时间">
      </el-table-column>
      <el-table-column align="center" label="操作">
        <template slot-scope="scope">
          <el-button type="warning" size="small" @click="displayErr(scope.$index, scope.row)">详情</el-button>
          <el-button type="danger" size="small" @click="handleDel(scope.$index, scope.row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <!--分页条-->
    <br/>
    <el-col :span="24" class="pagination-container">
      <el-button type="danger" @click="batchRemove" :disabled="this.sels.length===0">批量删除</el-button>
      <el-pagination layout="prev, pager, next" @current-change="handlePageChange" :page-size="10" :total="total" style="float:right;">
      </el-pagination>
    </el-col>
    <!-- 错误详情弹窗 -->
    <el-dialog title="错误详情" :visible.sync="errVisible" :close-on-click-modal="false">
      <div>
        <el-input type="textarea" :autosize="{ minRows: 2, maxRows: 20}" readonly="readonly" v-model="details">
        </el-input>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { timestampToTime } from "@/utils/tools";
export default {
  data() {
    return {
      logs: [],
      /**分页 */
      page: 1,
      total: 0,
      sels: [], //列表选中列
      errVisible: false,
      details: ""
    };
  },
  mounted() {
    this.getLogs();
  },
  methods: {
    /**获取错误数据 */
    getLogs() {
      this.$get("/system/log/logs", { page: this.page })
        .then(res => {
          this.logs = res.data.records;
          this.total = res.data.total;
        })
        .catch(err => {
          this.$message.error(err);
        });
    },
    /**表格数据的格式化 */
    formatter(row, column) {
      // console.log(row);
      return timestampToTime(row.createdata);
    },
    handleDel(index, row) {
      this.$confirm("你确认删除此日志错误吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          //点确认要做的事
          this.$deleteRequest(`/system/log/log/${row.id}`)
            .then(res => {
              this.$message({
                message: res.msg,
                type: "success"
              });
              this.page = 1;
              this.getLogs();
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
    handlePageChange(val) {
      this.page = val;
      this.getLogs();
    },
    selsChange(sels) {
      this.sels = sels;
    },
    batchRemove() {
      //将要删除的id封装成数组。
      let ids = [];
      this.sels.map(item => {
        ids.push(item.id);
      });
      this.$confirm("你确认批量删除这些错误日志吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      })
        .then(() => {
          //点确认要做的事
          this.$deleteRequest("/system/log/logs", ids)
            .then(res => {
              this.$message({
                message: res.msg,
                type: "success"
              });
              this.page = 1;
              this.getLogs();
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
    /**显示错误详情 */
    displayErr(index, row) {
      this.errVisible = true;
      this.details = row.details;
    }
  }
};
</script>

<style scoped>
</style>