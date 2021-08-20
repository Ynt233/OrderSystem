<template>
  <div style="width: 100%; height: 100%; overflow: hidden">
    <div style="width: 400px; margin: 0px auto">
      <div style="font-size: 30px; text-align: center; padding: 30px 0">欢迎注册</div>
      <el-form ref="form" :model="form" size="normal" :rules="rules">
        <el-form-item prop="name">姓名：
          <el-input prefix-icon="el-icon-user-solid" v-model="form.name"></el-input>
        </el-form-item>
        <el-form-item prop="gender">性别：
          <el-radio v-model="form.gender" label="男">男</el-radio>
          <el-radio v-model="form.gender" label="女">女</el-radio>
        </el-form-item>
        <el-form-item>任职部门：
          <el-select v-model="form.department" placeholder="请选择部门">
            <el-option
                v-for="department in options"
                :key="department.value"
                :label="department.label"
                :value="department.value">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item prop="phoneNumber">手机号码：
          <el-input prefix-icon="el-icon-user-solid" v-model="form.phoneNumber"></el-input>
        </el-form-item>
        <el-form-item prop="username">用户名：
          <el-input prefix-icon="el-icon-user-solid" v-model="form.username"></el-input>
        </el-form-item>
        <el-form-item prop="password">密码：
          <el-input prefix-icon="el-icon-lock" v-model="form.password" show-password></el-input>
        </el-form-item>
        <el-form-item prop="confirm">确认您的密码：
          <el-input prefix-icon="el-icon-lock" v-model="form.confirm" show-password></el-input>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%" type="primary" @click="register">注册</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
import request from "../utils/request";

export default {
  name: "Register",
  data() {
    return {
      form: {},
      rules: {
        name: [
          {required: true, message: '请输入您的姓名', trigger: 'blur'},
        ],
        phoneNumber: [
          {required: true, message: '请输入您的手机号码', trigger: 'blur'},
        ],
        username: [
          {required: true, message: '请输入用户名', trigger: 'blur'},
        ],
        password: [
          {required: true, message: '请输入密码', trigger: 'blur'},
        ],
        confirm: [
          {required: true, message: '请确认密码', trigger: 'blur'},
        ],
      },
      options: [{
        value: '生产线',
        label: '生产线'
      }, {
        value: '财政产品一部',
        label: '财政产品一部'
      }, {
        value: '财政产品二部',
        label: '财政产品二部'
      }, {
        value: '财政拓展部',
        label: '财政拓展部'
      }, {
        value: '工程五部',
        label: '工程五部'
      }]
    }
  },
  methods: {
    register() {

      if (this.form.password !== this.form.confirm) {
        this.$message({
          type: "error",
          message: '两次密码输入不一致！'
        })
        return
      }

      this.$refs['form'].validate((valid) => {
        if (valid) {
          request.post("/user/register", this.form).then(res => {
            if (res.code === '0') {
              this.$message({
                type: "success",
                message: "注册成功"
              })
              this.$router.push("/login")  //注册成功之后进行页面的跳转，跳转到登录页
            } else {
              this.$message({
                type: "error",
                message: res.msg
              })
            }
          })
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
