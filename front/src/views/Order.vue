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

    <el-collapse v-model="activeNames">
      <el-collapse-item title="订单1" name="1">
        <el-table
            v-loading="loading"
            ref="multipleTable"
            @selection-change="handleSelectionChange"
            :data="tableData"
            border
            stripe
            style="width: 100%">
          <el-table-column
              type="selection"
              width="55">
          </el-table-column>
          <el-table-column
              prop="id"
              label="ID"
              sortable
              v-if="user.role === 1">
          </el-table-column>
          <el-table-column>

          </el-table-column>
          <el-table-column
              prop="orderDate"
              label="订餐日期">
          </el-table-column>
          <el-table-column
              prop="totalPrice"
              label="总价">
          </el-table-column>
          <!--      <el-table-column-->
          <!--          prop="createTime"-->
          <!--          label="时间">-->
          <!--      </el-table-column>-->
          <el-table-column label="操作">
            <template #default="scope">
              <el-button size="mini" @click="showOrderDetails(scope.row.orderDetails)">查看订单详情</el-button>
              <el-popconfirm title="确定删除吗？" @confirm="handleDelete(scope.row.id)" v-if="user.role === 1">
                <template #reference>
                  <el-button size="mini" type="danger">删除</el-button>
                </template>
              </el-popconfirm>
            </template>
          </el-table-column>
        </el-table>
        <el-divider></el-divider>
        <div style="margin: 10px 0">
          <!--          <el-svg-icon><goods /></el-svg-icon>-->
          <label>共计：</label>
          <span>{{this.total_price}}元</span>
          <el-button @click="dialogFormAdd" style="float: right; margin-top: -25px ;margin-right: 75px" type="primary">结算</el-button>
          <el-button @click="backward" style="float: right; margin-top: -25px ;margin-right: 75px" type="primary">返回</el-button>
        </div>
      </el-collapse-item>
    </el-collapse>

    <el-dialog title="订单详情" v-model="orderDetailsVis" width="50%" height="50%">
      <el-table :data="orderDetails" stripe border>
        <el-table-column prop="foodName" label="商品名称"></el-table-column>
        <el-table-column prop="type" label="商品类型"></el-table-column>
        <el-table-column prop="amount" label="数量"></el-table-column>
        <el-table-column prop="price" label="价格"></el-table-column>
        <el-table-column prop="tip" label="备注"></el-table-column>
      </el-table>
    </el-dialog>

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
    </div>
  </div>
</template>

<script>


import request from "../utils/request";

export default {
  name: 'Order',
  components: {

  },
  data() {
    return {
      user: {},
      loading: true,
      form: {},
      dialogVisible: false,
      search: '',
      textarea: {},
      currentPage: 1,
      pageSize: 10,
      total: 0,
      total_price: 0,
      tableData: [],
      multipleSelection: [],
      activeNames: ['1'],
      amount: 1,
      orderDetails: [],
      orderDetailsVis: false,
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
    // filesUploadSuccess(res) {
    //   console.log(res)
    //   this.form.cover = res.data
    // },
    load() {
      this.loading = true
      request.get("/order", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search
        }
      }).then(res => {
        this.loading = false
        this.tableData = res.data.records
        console.log(this.tableData)
        this.total = res.data.total
      })
    },
    showOrderDetails(orderDetails){
      this.orderDetails = orderDetails;
      console.log(this.orderDetails)
      this.orderDetailsVis = true;
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      for (var i = 0; i < this.multipleSelection.length; i++){
        var halo = this.multipleSelection[i];
      }
      // console.log(val)
    },
    dialogFormAdd(){

    },
    add() {
      this.dialogVisible = true
      this.form = {}
      if (this.$refs['upload']) {
        this.$refs['upload'].clearFiles()  // 清除历史文件列表
      }
    },
    save() {
      if (this.form.id) {  // 更新
        request.put("/order", this.form).then(res => {
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
        request.post("/order", this.form).then(res => {
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
      request.delete("/order/" + id).then(res => {
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
    handleChange(row) {
      console.log(row)
      request.put("/order", row).then(res => {
        console.log(res)
        this.load() // 刷新表格的数据
      })
    },
    handleSizeChange(pageSize) {   // 改变当前每页的个数触发
      this.pageSize = pageSize
      this.load()
    },
    handleCurrentChange(pageNum) {  // 改变当前页码触发
      this.currentPage = pageNum
      this.load()
    },
    backward(){
      this.$router.push("/orderDetails")
    }
  }
}
</script>
