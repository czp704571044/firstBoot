<template>
    <div>
      <el-form :model="user">
      <h3>系统登录页面</h3>
      <el-form-item prop="userName">
        <el-input type="text" v-model="user.userName" placeholder="用户名"></el-input>
      </el-form-item>

        <el-form-item prop="password">
          <el-input type="tetx" v-model="user.password" placeholder="密码"></el-input>
        </el-form-item>

        <el-checkbox v-model="checked">记住密码</el-checkbox>

        <el-form-item>
          <el-button type="primary" @click="login();">登录</el-button>
        </el-form-item>

        <el-form-item>
          <el-button type="default" @click="register();">注册</el-button>
        </el-form-item>
      </el-form>

    </div>
</template>

<script>
  import axios from 'axios'
    export default {
        name: "login",
        data(){
        return {
          user:{
            userName : '',
            password : '',
            checked : false,
          }
        }
  },
      methods :{
         login(){
           axios.get('http://localhost:8080/user/Login',{
             params:{
               userName : this.user.userName,
               password : this.user.password,
             }
           }).then(response => {
             if(response.data){
               sessionStorage.setItem('user',this.user.userName);
               this.$route.push({path : '/'});
             }else{
               alert("操作失败")
             }
           })
         }
      }
    }
</script>

<style scoped>

</style>
