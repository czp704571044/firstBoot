<template>
    <div>
      <el-form :model="user">
      <h3>系统登录页面</h3>
      <el-form-item prop="userName">
        <el-input type="text" v-model="user.userName" placeholder="用户名"></el-input>
      </el-form-item>

        <el-form-item prop="password">
          <el-input type="text" v-model="user.password" placeholder="密码"></el-input>
        </el-form-item>

        <el-checkbox  class="rememberme" >记住密码</el-checkbox>

        <el-form-item>
          <el-button type="primary" @click="login();" :loading="logining">登录</el-button>
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
          logining : false,
          user:{
            userName : '',
            password : '',
            checked : false,
          },
          rules:{
            userName : [{require:true,message:'请输入用户名',trigger:'blur'}],
            password : [{require:true,message:'请输入密码',trigger:'blur'}]
          },
        }
  },
      methods :{
         login(){
           axios.get('http://localhost:7070/user/Login',{
             params:{
               userName : this.user.userName,
               password : this.user.password,
             }
           }).then(response => {
             this .logging = true;
             if(response.data){
               this .logging = false;
               sessionStorage.setItem('user',this.user.userName);
               this.$route.push({path : '/'});
             }else{
               this .logging = false;
               alert("操作失败")
             }
           })
         }
      }
    }
</script>

<style scoped>
  .rememberme {
    margin: 0px 0px 15px;
    text-align: left;
  }
</style>
