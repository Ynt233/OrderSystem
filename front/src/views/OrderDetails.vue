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
      <el-table-column
          prop="foodName"
          label="商品名称">
      </el-table-column>
      <el-table-column
          prop="type"
          label="商品类型">
      </el-table-column>
      <el-table-column
          prop="price"
          label="单价(元)">
      </el-table-column>
      <el-table-column
          prop="amount"
          label="数量">
        <template #default="scope">
          <el-input-number name="cart" v-model="scope.row.amount" @change="handleChange(scope.row)" :min="1" :max="100" label="描述文字"></el-input-number>
        </template>
      </el-table-column>
      <el-table-column
          prop="tip"
          label="备注">
      </el-table-column>
      <!--      <el-table-column-->
      <!--          prop="createTime"-->
      <!--          label="时间">-->
      <!--      </el-table-column>-->
      <el-table-column label="操作">
        <template #default="scope">
          <el-button size="mini" @click="handleEdit(scope.row)">编辑备注</el-button>
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
      <label>共计：</label>
      <span>{{this.total_price}}元</span>
      <el-button @click="dialogFormAdd" style="float: right; margin-top: -25px ;margin-right: 75px" type="primary">结算</el-button>
    </div>

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
<!--          <el-form-item label="封面">-->
<!--            <el-upload ref="upload" :action="filesUploadUrl" :on-success="filesUploadSuccess">-->
<!--              <el-button type="primary">点击上传</el-button>-->
<!--            </el-upload>-->
<!--          </el-form-item>-->
          <el-form-item label="商品名称" v-if="user.role === 1">
            <el-input v-model="form.foodName" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="商品类型" v-if="user.role === 1">
            <el-input v-model="form.type" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="商品价格" v-if="user.role === 1">
            <el-input v-model="form.price" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="数量" v-if="user.role === 1">
            <el-input v-model="form.amount" style="width: 80%"></el-input>
          </el-form-item>
          <el-form-item label="备注">
            <el-input v-model="form.tip" type="textarea" :autosize="{ minRows: 0, maxRows: 50}" maxlength="50" style="width: 80%; height: 150%"></el-input>
          </el-form-item>
          <!--          <el-form-item label="时间">-->
          <!--            <el-date-picker v-model="form.createTime" value-format="YYYY-MM-DD" type="date" style="width: 80%" clearable></el-date-picker>-->
          <!--          </el-form-item>-->
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
  name: 'OrderDetails',
  components: {

  },
  data() {
    return {
      user: {},
      loading: true,
      form: {},
      tableVisible: true,
      dialogVisible: false,
      search: '',
      textarea: {},
      selected: [],
      currentPage: 1,
      currentDate: '',
      pageSize: 10,
      total: 0,
      total_price: 0,
      tableData: [],
      multipleSelection: [],
      activeNames: ['1'],
      amount: 1,
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
    this.load();
    this.getUser();
    this.addDate();
  },
  methods: {
    // filesUploadSuccess(res) {
    //   console.log(res)
    //   this.form.cover = res.data
    // },
    load() {
      this.loading = true
      request.get("/orderDetails", {
        params: {
          pageNum: this.currentPage,
          pageSize: this.pageSize,
          search: this.search,
          userId: parseInt(this.user.id)
        }
      }).then(res => {
        this.loading = false
        this.tableData = res.data.records
        this.total = res.data.total
      })
    },
    getUser(){
      // console.log(this.user);
      request.post("/order/getUser", this.user).then(res => {
        if (res.code === '1'){
          this.$message({
            type: "error",
            message: res.msg
          })
        }
      })
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
      var total = 0;
      for (var i = 0; i < this.multipleSelection.length; i++){
        var halo = this.multipleSelection[i];
        // console.log(halo);
        total = total + halo.price * halo.amount;
        this.selected[i] = halo;
      }
      this.total_price = total;
      // console.log(this.total_price);
      // console.log(this.selected);
    },
    addDate(){
      const nowDate = new Date();
      const date = {
        year: nowDate.getFullYear(),
        month: nowDate.getMonth() + 1,
        date: nowDate.getDate(),
      }
      const newmonth = date.month>10?date.month:'0'+date.month;
      const day = date.date>10?date.date:'0'+date.date;
      this.currentDate = date.year + '-' + newmonth + '-' + day;
      console.log(this.currentDate);
    },
    dialogFormAdd(){
      request.post("/order/getDetails",this.selected).then(res => {
        // sessionStorage.setItem("orderDetails",JSON.stringify(res.data));
        // console.log(JSON.stringify(res.data));
        this.$router.push("/order");
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
      })

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
        request.put("/orderDetails", this.form).then(res => {
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
        request.post("/orderDetails", this.form).then(res => {
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
      request.delete("/orderDetails/" + id).then(res => {
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
      request.put("/orderDetails", row).then(res => {
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
    }
  }
}
</script>
