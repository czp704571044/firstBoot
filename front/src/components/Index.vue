<template>
  <div>
    <el-table :data="userData">

      <el-table-column prop="userId" label="ID"></el-table-column>
      <el-table-column prop="userName" label="用户名"></el-table-column>
      <el-table-column prop="loginPassword" label="密码"></el-table-column>
      <el-table-column prop="userSex" label="性别"></el-table-column>

    </el-table>
  </div>
</template>

<script>
  import axios from 'axios'
export default {
  name: 'Index',
  data () {
    return {
      userData: '',
      searchContent: '',
    }
  },
  methods: {
    getData() {
      axios
        .get('http://localhost:7070/getUserList')
        .then(response => {
          this.userData = response.data
          console.log(response);
          alert(response);
        }).catch(error => {

      })
    },
  },
  created() {
    this.getData()
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}
</style>
