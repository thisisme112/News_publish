<template>
  <div class="center">
    <div class="table-container">
      <h3>修改违禁词</h3>
      <el-table :data="words" style="width: 100%" max-height="250" border>
        <el-table-column fixed prop="id" label="id" width="150" />
        <el-table-column prop="word" label="word" width="120" />
        <el-table-column prop="created_at" label="created_at" width="120" />
        <el-table-column fixed="right" label="Operations" width="120">
          <template #default="scope">
            <el-button
              link
              type="primary"
              size="small"
              @click.prevent="deleteRow(scope.$index, scope.row)"
            >
              Remove
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    <el-button style="width: 50%" @click="drawer = true">
      Add Item
    </el-button>
  </div>
  <el-drawer v-model="drawer" :direction="direction">
    <template #header>
      <h4>添加违禁词</h4>
    </template>
    <template #default>
      <div>
        <el-input
          placeholder="请输入违禁词"
          v-model="word"
          style="width: 100%"
        ></el-input>
      </div>
    </template>
    <template #footer>
      <div style="flex: auto">
        <el-button @click="cancelClick">取消</el-button>
        <el-button type="primary" @click="confirmClick">确认</el-button>
      </div>
    </template>
  </el-drawer>
</template>

<style scoped>
.center {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
}

.table-container {
  width: 50%;
  margin-top: 300px;
}

</style>


<script>
import 'element-plus/theme-chalk/dark/css-vars.css'
import { ElMessage, ElNotification } from 'element-plus';
import { User, UserFilled, UploadFilled } from '@element-plus/icons-vue'
import { ref, computed, h } from 'vue'
import router from '@/router';
import dayjs from 'dayjs'
import { ElTableV2 } from 'element-plus'
import axios from 'axios';
export default {
  data() {
    return {
      words: [], // 单词列表
      word: '', // 新增的单词
      direction: 'btt',
      drawer: false,
    }
  },
  created() {
    axios({
      method: 'get',
      url: 'http://localhost:8080/Java_news_war_exploded/dict',
    }).then(res =>{
      console.log(res);
      const words = res.data;
      this.words = words;
    })
  },
  methods: {
    onAddItem() {
      this.drawer = true
    },
    deleteRow(index, row) {
      const id = row.id;
      axios({
        method: 'post',
        url: 'http://localhost:8080/Java_news_war_exploded/dict',
        data: {
          method: 'delete',
          id: id,
        }
      }).then(res =>{
        console.log(res);
        this.words.splice(index, 1)
      })

    },
    cancelClick() {
      this.drawer = false
    },
    confirmClick() {
      const now = new Date()
      const date = dayjs(now).format('YYYY-MM-DD HH:mm:ss')
      const word = this.word
      axios({
        method: 'post',
        url: 'http://localhost:8080/Java_news_war_exploded/dict',
        data: {
          method: 'add',
          word: word,
        }
      }).then(res =>{
        console.log(res.data.id);
        const newid = res.data.id;
        this.words.push({
        date: date,
        word : word,
        id : newid,
      })
      })
      
  }
  },
}
</script>