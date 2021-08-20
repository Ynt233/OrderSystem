<template>
  <div>
    <el-menu
        style="width: 200px; min-height: calc(100vh - 50px)"
        :default-active="path"
        router
        class="el-menu-vertical-demo">
      <el-submenu index="1" v-if="user.role === 1">//仅管理员可见的界面
        <template #title>订餐系统</template>
        <el-menu-item index="/user">人员管理</el-menu-item>
      </el-submenu>
      <el-menu-item index="/order">订单管理</el-menu-item>
      <el-menu-item index="/food">热销菜式</el-menu-item>
      <el-menu-item index="/personManage">今旬菜式</el-menu-item>
      <el-menu-item index="/personManage">粥粉面</el-menu-item>
      <el-menu-item index="/personManage">靓汤</el-menu-item>
      <el-menu-item index="/personManage">饮料</el-menu-item>
    </el-menu>
  </div>
</template>

<script>
import request from "../utils/request";

export default {
  name: "Aside",
  data() {
    return {
      user: {},
      path: this.$route.path   // 设置默认高亮的菜单
    }
  },
  created() {
    let userStr = sessionStorage.getItem("user") || "{}"//获取用户信息的JSON
    this.user = JSON.parse(userStr)

    // 请求服务端，确认当前登录用户的 合法信息
    //判断是否为管理员
    request.get("/user/" + this.user.id).then(res => {
      if (res.code === '0') {
        this.user = res.data
      }
    })
  }
}
</script>

<style scoped>

</style>
