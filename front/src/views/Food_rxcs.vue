<template>
  <div style="padding: 10px">
    <!--    功能区域-->
    <div style="margin: 10px 0">
      <el-button type="primary" @click="add" v-if="user.role === 1">新增</el-button>
    </div>

    <!--    搜索区域-->
    <div style="margin: 10px 0">
      <el-input v-model="search" placeholder="请输入关键字" style="width: 20%" clearable></el-input>
      <el-button type="primary" style="margin-left: 5px" @click="load">查询</el-button>
    </div>
    <!--    表单区域-->
    <el-table
        v-loading="loading"
        :data="tableData"
        :model="table"
        v-model="foodType"
        border
        stripe
        style="width: 100%">
      <el-table-column
          prop="id"
          label="ID"
          v-model="table.id"
          v-if="user.role === 1"
          sortable>
      </el-table-column>
      <el-table-column
          prop="foodName"
          v-model="table.foodName"
          label="商品名称">
      </el-table-column>
      <el-table-column
          prop="type"
          v-model="table.type"
          label="商品类型">
      </el-table-column>
      <el-table-column
          prop="price"
          v-model="table.price"
          label="单价(元)"
          sortable>
      </el-table-column>
      <el-table-column
          prop="amount"
          v-model="table.amount"
          label="数量">
        <template #default="scope">
          <el-input-number  v-model="scope.row.amount" :min="1" :max="100"></el-input-number>
        </template>
      </el-table-column>
<!--      <el-table-column-->
<!--          prop="tip"-->
<!--          label="备注"-->
<!--      #default="scope">-->
<!--        <el-input v-model="scope.row.tip">无</el-input>-->
<!--      </el-table-column>-->
<!--      <el-table-column-->
<!--          label="封面">-->
<!--        <template #default="scope">-->
<!--          <el-image-->
<!--              style="width: 100px; height: 100px"-->
<!--              :src="scope.row.cover"-->
<!--              :preview-src-list="[scope.row.cover]">-->
<!--          </el-image>-->
<!--        </template>-->
<!--      </el-table-column>-->
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.row)" v-if="user.role === 1">编辑</el-button>
          <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.id)" v-if="user.role === 1">
            <template #reference>
              <el-button size="mini" type="danger">删除</el-button>
            </template>
          </el-popconfirm>
          <el-button size="mini" type="primary" @click="handleOrder(scope.row)">下单</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div style="margin: 10px 0">
      <el-pagination
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
          :current-page="currentPage"
          :page-sizes="[5, 10, 20]"
          :page-size="pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
      </el-pagination>

      <el-dialog title="提示" v-model="dialogVisible" width="30%">
        <el-form :model="form" label-width="120px">
          <el-form-item label="商品名称">
            <el-input v-model="form.foodName" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="商品类型">
            <el-input v-model="form.type" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="商品价格">
            <el-input v-model="form.price" style="width: 80%"></el-input>
          </el-form-item>
<!--          <el-form-item label="数量">-->
<!--            <el-input v-model="form.amount" style="width: 80%"></el-input>-->
<!--          </el-form-item>-->
<!--          <el-form-item label="时间">-->
<!--            <el-date-picker v-model="form.createTime" value-format="YYYY-MM-DD" type="date" style="width: 80%" clearable></el-date-picker>-->
<!--          </el-form-item>-->
          <el-form-item label="封面">
            <el-upload ref="upload" :action="filesUploadUrl" :on-success="filesUploadSuccess">
              <el-button type="primary">点击上传</el-button>
            </el-upload>
          </el-form-item>
        </el-form>
        <template #footer>
          <span class="dialog-footer">
            <el-button @click="dialogVisible = false">取 消</el-button>
            <el-button type="primary" @click="save">确 定</el-button>
          </span>
        </template>
      </el-dialog>

    </div>
  </div>
</template>

<script>


import request from "../utils/request";

export default {
  name: 'Food_rxcs',
  components: {

  },
  data() {
    return {
      user: {},
      loading: true,
      form: {},
      table:{},
      amount: 1,
      dialogVisible: false,
      search: '',
      foodType: '热销菜式',
      currentPage: 1,
      pageSize: 10,
      total: 0,
      tableData: [],
      filesUploadUrl: "http://" + window.server.filesUploadUrl + ":9090/files/upload"
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user") || "{}"
    this.user = JSON.parse(userStr)
    // 请求服务端，确认当前登录用户的 合法信息
    request.get("/user/" + this.user.id).then(res => {
      if (res.code === '0') {
        this.user = res.data
      }
    })
    this.load()
  },
  methods: {
    filesUploadSuccess(res) {
      console.log(res)
      this.form.cover = res.data
    },
    load() {
      this.loading = true
      request.get("/food", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search,
          foodType: this.foodType
        }
      }).then(res => {
        this.loading = false
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    add() {
      this.dialogVisible = true
      this.form = {}//清空表单
      if (this.$refs['upload']) {
        this.$refs['upload'].clearFiles()  // 清除历史文件列表
      }
    },
    save() {
      if (this.form.id) {  // 更新
        request.put("/food", this.form).then(res => {
          console.log(res)
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "更新成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }
          this.load() // 刷新表格的数据
          this.dialogVisible = false  // 关闭弹窗
        })
      }  else {  // 新增
        request.post("/food", this.form).then(res => {
          console.log(res)
          if (res.code === '0') {
            this.$message({
              type: "success",
              message: "新增成功"
            })
          } else {
            this.$message({
              type: "error",
              message: res.msg
            })
          }

          this.load() // 刷新表格的数据
          this.dialogVisible = false  // 关闭弹窗
        })
      }

    },
    handleChange(){

    },
    handleEdit(row) {
      this.form = JSON.parse(JSON.stringify(row))
      this.dialogVisible = true
      this.$nextTick(() => {
        if (this.$refs['upload']) {
          this.$refs['upload'].clearFiles()  // 清除历史文件列表
        }
      })

    },
    handleDelete(id) {
      console.log(id)
      request.delete("/food/" + id).then(res => {
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "删除成功"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
        this.load()  // 删除之后重新加载表格的数据
      })
    },
    handleOrder(row){
      request.post("/orderDetails", row).then(res => {
        console.log(res)
        if (res.code === '0') {
          this.$message({
            type: "success",
            message: "下单成功！"
          })
        } else {
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    },
    handleSizeChange(pageSize) {   // 改变当前每页的个数触发
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {  // 改变当前页码触发
      this.currentPage = pageNum
      this.load()
    }
  }
}
</script>
