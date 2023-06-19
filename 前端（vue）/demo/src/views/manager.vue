<template>
  <el-backtop :right="100" :bottom="100" />
  <div class="container">
    <div class="login">
      <el-button v-if="!logined" type="primary" @click="login">
        <i class="el-icon-user"></i> 登录
      </el-button>
      <el-button v-if="logined" type="danger" @click="logout">
        <i class="el-icon-user"></i> 退出登录
      </el-button>
    </div>
    <div class="news-form" v-if="logined">
      <br>
      <el-button type="primary" style="margin-left: 16px" @click="drawer2 = true">
        发布新闻
      </el-button>
      <router-link to="/words">
      <el-button type="primary" style="margin-left: 16px" @click="">
        修改违禁词
      </el-button>
      </router-link>
      <el-drawer v-model="drawer2" :direction="direction">
        <h3 class="form-title">发布新闻</h3>
        <br>
        <div class="form-container">
          <el-form ref="newsForm" :model="newsForm" label-width="120px">
            <el-form-item label="标题">
              <el-input style="width: auto;" v-model="newsForm.title"></el-input>
              <div style="color: #e5242b">{{ titleMsg }}</div>
            </el-form-item>
            <el-form-item label="内容">
              <el-input style="width: auto;" type="textarea" v-model="newsForm.content"></el-input>
              <div style="color: #e5242b">{{ contentMsg }}</div>
            </el-form-item>
            <el-form-item label="相关图片">
              <el-upload
                  class="avatar-uploader"
                  action="http://localhost:8080/Java_news_war_exploded/upload"
                  :show-file-list="false"
                  :on-success="handleUploadSuccess"
                >
                  <img v-if="imageUrl" :src="imageUrl" class="avatar" />
                  <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                </el-upload>
              <el-dialog v-model="dialogVisible">
                <img w-full :src="dialogImageUrl" alt="Preview Image" />
              </el-dialog>
            </el-form-item>
            <el-form-item label="链接">
              <el-input style="width: auto;" type="textarea" v-model="newsForm.link"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="submitForm('newsForm')">发布新闻</el-button>
            </el-form-item>
          </el-form>
        </div>
      </el-drawer>
    </div>
    <el-table :data="currentNewsList" style="width: 100%">
      <el-table-column label="标题" prop="title" />
      <el-table-column label="相关图片" prop="pic">
        <template #default="scope">
          <a :href="scope.row.link">
            <img :src="scope.row.pic" style="max-width: 100px; max-height: 100px" />
          </a>
        </template>
      </el-table-column>
      <el-table-column label="链接" prop="link">
        <template #default="scope">
          <a :href="scope.row.link" target="_blank">{{ scope.row.link }}</a>
        </template>
      </el-table-column>
      <el-table-column label="内容" prop="content" />
      <el-table-column label="发布时间" prop="created_at" />
      <el-table-column align="right">
        <template #header>
          <el-input v-model="search" size="small" placeholder="Type to search" />
        </template>
        <template #default="scope">
          <el-button size="small" @click="handleEdit(scope.$index, scope.row)">Edit</el-button>
          <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">Delete</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination @size-change="handlePageSizeChange" @current-change="handleCurrentPageChange"
      :current-page="currentPage" :page-sizes="[10, 20, 30, 40]" :page-size="pageSize" :total="filteredNewsList.length"
      layout="sizes, prev, pager, next, jumper" style="margin-top: 20px; text-align: right"></el-pagination>
    <el-dialog v-model="dialogVisible" title="Tips" width="30%">
      <span>修改新闻</span>
      <template #footer>
        <span class="dialog-footer">
          <el-form ref="newsForm" :model="editingNews" label-width="120px">
            <el-form-item label="标题">
              <el-input style="width: auto;" v-model="editingNews.title"></el-input>
              <div v-show="sensitiveWordNotice" style="color: #e5242b">{{ titleMsg }}</div>
            </el-form-item>
            <el-form-item label="内容">
              <el-input style="width: auto;" type="textarea" v-model="editingNews.content"></el-input>
              <div v-show="sensitiveWordNotice" style="color: #e5242b">{{ contentMsg }}</div>
            </el-form-item>
            <el-form-item label="相关图片">
              <el-upload
                  class="avatar-uploader"
                  action="http://localhost:8080/Java_news_war_exploded/upload"
                  :show-file-list="false"
                  :on-success="handleEditSuccess"
                >
                  <img v-if="EimageUrl" :src="EimageUrl" class="avatar" />
                  <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                </el-upload>
            </el-form-item>
            <el-form-item label="链接">
              <el-input style="width: auto;" type="textarea" v-model="editingNews.link"></el-input>
            </el-form-item>
            <el-form-item>
              <el-button @click="dialogVisible = false">取消</el-button>
              <el-button type="primary" @click="updateNews">
                修改
              </el-button>
            </el-form-item>
          </el-form>
        </span>
      </template>
    </el-dialog>
  </div>
</template>
<style scoped>
.container {
  padding: 20px;
  font-family: Arial, sans-serif;
}

.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.login {
  text-align: right;
  margin-bottom: 20px;
}

.login .el-button {
  margin-left: 10px;
}

.news-form {
  margin-top: 20px;
}

.form-title {
  text-align: center;
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.form-container {
  max-width: 400px;
  margin: 0 auto;
  background-color: #f5f5f5;
  padding: 20px;
  border-radius: 5px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.el-form-item {
  margin-bottom: 15px;
}

.el-form-item label {
  font-weight: bold;
  color: #333;
}

.sensitiveWordNotice {
  font-size: 12px;
  font-weight: bold;
}
</style>
  
<script>
import axios from 'axios';
import 'element-plus/theme-chalk/dark/css-vars.css'
import { ElMessage, ElNotification } from 'element-plus';
import { User, UserFilled, UploadFilled } from '@element-plus/icons-vue'
import { ref, computed, h } from 'vue'
import router from '@/router';
export default {
  data() {
    return {
      newsList: [],
      newsForm: {
        title: "",
        content: "",
        pic: "",
        link: "",
      },
      titleMsg: '',
      imageUrl: '', // 上传图片的链接
      EimageUrl: '', // 编辑新闻时的图片链接
      pageSize: 10, // 每页显示的条目数
      currentPage: 1, // 当前页码
      drawer1: false,
      drawer2: false,
      direction: 'rtl',
      contentMsg: '',
      sensitiveWords: [],
      search: '',
      logined: false,
      editingNews: {
        id: "",
        title: "",
        content: "",
        pic: "",
        link: "",
      },
      dialogVisible: false,
    }
  },
  created() {
    axios({
      url: 'http://localhost:8080/Java_news_war_exploded/news',
      method: 'get',
    }).then(res => {
      const newsList = res.data
      this.newsList = newsList
      console.log(res)
    }),
    axios({
      method: 'get',
      url: 'http://localhost:8080/Java_news_war_exploded/dict',
    }).then(res =>{
      console.log(res);
      const words = res.data;
      for (let i = 0; i < words.length; i++) {
        this.sensitiveWords.push(words[i].word);
      }
    })
  },
  mounted() {
    const isLoggedIn = localStorage.getItem('isLoggedIn');
    if (isLoggedIn === 'true') {
      // 用户已登录
      this.logined = true;
    } else {
      // 用户未登录
      this.logined = false;
    }
  },
  computed: {
    filteredNewsList() {
      if (this.search === '') {
        return this.newsList;
      }
      return this.newsList.filter(news => {
        return news.title.toLowerCase().includes(this.search.toLowerCase());
      });
    },
    currentNewsList() {
      const startIndex = (this.currentPage - 1) * this.pageSize;
      const endIndex = startIndex + this.pageSize;
      return this.filteredNewsList.slice(startIndex, endIndex);
    },
  },

  methods: {
    handleEditSuccess(res, file) {
      this.EimageUrl = 'http://localhost/images/'+res;
      console.log(res);
    },
    handleUploadSuccess(res, file) {
      this.imageUrl = 'http://localhost/images/'+res;
      console.log(res);
    },
    handlePageSizeChange(newSize) {
      this.pageSize = newSize;
    },

    handleCurrentPageChange(newPage) {
      this.currentPage = newPage;
    },

    detectSensitiveWords(text, sensitiveWords) {
      for (let i = 0; i < sensitiveWords.length; i++) {
        const sensitiveWord = sensitiveWords[i];
        const regex = new RegExp(sensitiveWord, 'gi');
        if (regex.test(text)) {
          return true; // 文本中包含敏感词
        }
      }
      return false; // 文本中不包含敏感词
    },
    submitForm(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          const title = this.newsForm.title;
          const content = this.newsForm.content;
          const sensitiveWords = this.sensitiveWords;
          const isTitleSensitive = this.detectSensitiveWords(title, sensitiveWords);
          const isContentSensitive = this.detectSensitiveWords(content, sensitiveWords);
          if (isTitleSensitive) {
            // 标题包含敏感词
            this.titleMsg = '标题包含敏感词，请修改后再提交。';
          } else {
            this.titleMsg = '';
          }

          if (isContentSensitive) {
            // 内容包含敏感词
            this.contentMsg = '内容包含敏感词，请修改后再提交。';
          } else {
            this.contentMsg = '';
          }

          this.sensitiveWordNotice = isTitleSensitive || isContentSensitive;

          if (isTitleSensitive || isContentSensitive) {
            // 检测到敏感词，执行相应的处理逻辑，如给出提示信息或阻止发布
            return;
          } else {
            // 通过敏感词检测，可以继续发布新闻
            const newNews = {
              method: 'post',
              title: this.newsForm.title,
              content: this.newsForm.content,
              pic: this.imageUrl,
              link: this.newsForm.link,
            };
            axios({
              url: 'http://localhost:8080/Java_news_war_exploded/news',
              method: 'post',
              data: newNews,
            }).then(res => {
              console.log(res)
              newNews.id = res.data.id
              if (res.status == 200) {
                ElNotification({
                  title: 'Success!',
                  message: h('i', { style: 'color: teal' }, '发布成功!'),
                  type: 'success'
                });
                this.newsList.push(newNews) // add new news to newsList
              }
              else {
                ElNotification({
                  title: 'Error!',
                  message: h('i', { style: 'color: red' }, '发布失败,查看控制台!'),
                  type: 'error'
                });
              }
            })
          }
        } else {
          console.log("error submit!!");
          return false;
        }
      });
    },
    resetForm(formName) {
      this.$refs[formName].resetFields();
    },
    handleEdit(index, row) {
      if (this.logined == false) {
        ElMessage({
          message: '请先登录!',
          type: 'warning',
        })
      }
      else {
        console.log(index, row);
        this.dialogVisible = true
        this.editingNews.id = row.id
        }

    },
    login() {
      router.push('/login')
    },
    logout() {
      localStorage.setItem('isLoggedIn', false);
      this.logined = false;
    },
    updateNews() {
      axios({
        url: 'http://localhost:8080/Java_news_war_exploded/news',
        method: 'post',
        data: {
          method: 'put',
          id: this.editingNews.id,
          title: this.editingNews.title,
          content: this.editingNews.content,
          pic: this.EimageUrl,
          link: this.editingNews.link,
        }
      }).then(res => {
        this.dialogVisible = false
        if (res.status == 200) {
          ElNotification({
            title: 'Success!',
            message: h('i', { style: 'color: teal' }, '修改成功!'),
            type: 'success'
          });
          this.newsList.forEach((item, index) => {
            if (item.id === this.editingNews.id) {
              this.newsList[index] = this.editingNews
            }
          })

        }
        else {
          ElNotification({
            title: 'Error!',
            message: h('i', { style: 'color: red' }, '修改失败,查看控制台!'),
            type: 'error'
          });
        }
      })
    },
    handleDelete(index, row) {
      if (this.logined == false) {
        ElMessage({
          message: '请先登录!',
          type: 'warning',
        })
      }
      else {
        const id = row.id
        console.log(id)
        axios({
          url: 'http://localhost:8080/Java_news_war_exploded/news',
          method: 'post',
          data: {
            method: 'delete',
            id: id
          }
        }).then(res => {
          if (res.status == 200) {
            ElNotification({
              title: 'Success!',
              message: h('i', { style: 'color: teal' }, '删除成功!'),
              type: 'success'
            });
            this.newsList.splice(index, 1);
          }
        })

      }
    }
  },
};

</script>

<style>
.el-header {
  color: #333;
  padding: 0 !important;
  height: 50px !important;
  background: antiquewhite;
}

.el-main {
  height: 100%;
  padding: 0 !important;
  overflow: hidden;
  position: relative;
}
.avatar-uploader .el-upload {
  border: 1px dashed var(--el-border-color);
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style>