<template>
    <!-- 最外层的大盒子 -->
    <div class="bigbox">
        <div class="box">
            <!-- 滑动盒子 -->
            <div class="pre-box" ref="preRef">
                <h1>WELCOME</h1>
                <p>JOIN US!</p>
                <div class="img-box">
                    <img id="avatar" src="@/assets/img/waoku.jpg">
                </div>
            </div>
            <!-- 注册盒子 -->
            <div class="register-form">
                <!-- 标题盒子 -->
                <div class="title-box">
                    <h1>注册</h1>
                </div>
                <!-- 输入框盒子 -->
                <el-form :model="registerForm" :rules="rules" ref="registerFormRef" label-with="5px">
                    <el-form-item prop="username" lable=" ">
                        <el-input type="text" placeholder="用户名" :suffix-icon="User" v-model="registerForm.username"></el-input>
                    </el-form-item>
                    <el-form-item prop="password" lable=" ">
                        <el-input type="password" placeholder="密码" :suffix-icon="Lock" v-model="registerForm.password"></el-input>
                    </el-form-item>
                    <el-form-item prop="confirmpassword" lable=" ">
                        <el-input type="password" placeholder="确认密码" :suffix-icon="Lock" v-model="registerForm.confirmpassword"></el-input>
                    </el-form-item>
                </el-form>
                <!-- 按钮盒子 -->
                <div class="btn-box">
                    <button @click="register">注册</button>
                    <!-- 绑定点击事件 -->
                    <p @click="mySwitch">已有账号?去登录</p>
                </div>
            </div>
            <!-- 登录盒子 -->
            <div class="login-form">
                <!-- 标题盒子 -->
                <div class="title-box">
                    <h1>登录</h1>
                </div>
                <!-- 输入框盒子 -->
                <el-form :model="loginForm" :rules="rules" ref="loginFormRef" label-with="5px">
                    <el-form-item prop="username" lable=" ">
                        <el-input type="text" placeholder="用户名" :suffix-icon="User" v-model="loginForm.username">
                        </el-input>
                    </el-form-item>
                    <el-form-item prop="password" lable=" ">
                        <el-input type="password" placeholder="密码" :suffix-icon="Lock" v-model="loginForm.password">
                        </el-input>
                    </el-form-item>
                </el-form>

                <!-- 按钮盒子 -->
                <div class="btn-box">
                    <button @click="login">登录</button>
                    <!-- 绑定点击事件 -->
                    <p @click="mySwitch">没有账号?去注册</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
// import router from '@/router';
import axios from 'axios';
import { Lock, User } from '@element-plus/icons-vue'
import mySwitch from '@/utils/mySwitch'
import { reactive, ref } from '@vue/reactivity';
import { ElMessage } from 'element-plus';
import { useRouter } from 'vue-router';
import { mapMutations } from 'vuex';
const loginForm = reactive({
    username: '',
    password: ''
})
const registerForm = reactive({
    username: '',
    password: '',
    confirmpassword: ''
})
const loginFormRef = ref('')
const registerFormRef = ref('')
const token=''
const rules = reactive({
    username: [
        { required: true, message: '请输入用户名', trigger: 'blur' },
        { min: 3, max: 10, message: '长度在 3 到 10 个字符', trigger: 'blur' }
    ],
    password: [
        { required: true, message: '请输入密码', trigger: 'blur' },
        { min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur' }
    ],
    confirmpassword: [
        { required: true, message: '请再次输入密码', trigger: 'blur' },
        { min: 6, max: 12, message: '长度在 6 到 12 个字符', trigger: 'blur' }
    ]


})
const router = useRouter()
const login = () => {
    loginFormRef.value.validate((valid)=>{
        if(valid) {
            console.log(loginForm.username,loginForm.password);
            axios({
        method: 'post',
        url: 'http://localhost:8080/Java_news_war_exploded/admin',
        data: {
            username: loginForm.username,
            password: loginForm.password
        }
        }).then(res => {
            console.log(res);
            if (res.status === 200){
                ElMessage.success('登录成功')
                // 跳转到首页
                localStorage.setItem('isLoggedIn', 'true'); // 设置登录状态为true
                router.push('/manager')
            }
            else{
                ElMessage.error('登录失败')
            }
        
    })
    
    
}
})
}
const register = () => {
    ElMessage.error('暂未开放注册功能')
    // axios({
    //     method: 'post',
    //     url: 'http://127.0.0.1:8000/api/register/',
    //     data: {
    //         username: registerForm.username,
    //         password: registerForm.password,
    //         confirmpassword: registerForm.confirmpassword
    //     }
    //     }).then(res => {
    //         console.log(res);
    //         if (res.status === 200){
    //             ElMessage.success('注册成功')
    //         }
    //         else{
    //             ElMessage.error('注册失败')
    //         }
    //     }).catch(err => {
    //         console.log(err);
    //     })
    
};
</script>

<style scoped>
* {
    /* 去除浏览器默认内外边距 */
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

/* 去除input的轮廓 */
input {
    outline: none;
}


.bigbox {
    /* 溢出隐藏 */
    height: 100vh;
    overflow-x: hidden;
    display: flex;
    /* 渐变方向从左到右 */
    background: linear-gradient(to right, rgb(247, 209, 215), rgb(191, 227, 241));
}

span {
    position: absolute;
    z-index: 0;
    bottom: 0;
    border-radius: 50%;
    /* 径向渐变 */
    background: radial-gradient(circle at 72% 28%, #fff 3px, #ff7edf 8%, #5b5b5b, #aad7f9 100%);
    /* 泡泡内阴影 */
    box-shadow: inset 0 0 6px #fff,
        inset 3px 0 6px #eaf5fc,
        inset 2px -2px 10px #efcde6,
        inset 0 0 60px #f9f6de,
        0 0 20px #fff;
    /* 动画 */
    animation: myMove 4s linear infinite;
}


@keyframes myMove {
    0% {
        transform: translateY(0%);
        opacity: 1;
    }

    50% {
        transform: translate(10%, -1000%);
    }

    75% {
        transform: translate(-20%, -1200%);
    }

    99% {
        opacity: .9;
    }

    100% {
        transform: translateY(-1800%) scale(1.5);
        opacity: 0;
    }
}

/* 最外层的大盒子 */
.box {
    width: 1050px;
    height: 600px;
    display: flex;
    /* 相对定位 */
    position: relative;
    z-index: 2;
    margin: auto;
    /* 设置圆角 */
    border-radius: 8px;
    /* 设置边框 */
    border: 1px solid rgba(255, 255, 255, .6);
    /* 设置盒子阴影 */
    box-shadow: 2px 1px 19px rgba(0, 0, 0, .1);
}

/* 滑动的盒子 */
.pre-box {
    /* 宽度为大盒子的一半 */
    width: 50%;
    /* width: var(--width); */
    height: 100%;
    /* 绝对定位 */
    position: absolute;
    /* 距离大盒子左侧为0 */
    left: 0;
    /* 距离大盒子顶部为0 */
    top: 0;
    z-index: 99;
    border-radius: 4px;
    background-color: #edd4dc;
    box-shadow: 2px 1px 19px rgba(0, 0, 0, .1);
    /* 动画过渡，先加速再减速 */
    transition: 0.5s ease-in-out;
}

/* 滑动盒子的标题 */
.pre-box h1 {
    margin-top: 150px;
    text-align: center;
    /* 文字间距 */
    letter-spacing: 5px;
    color: white;
    /* 禁止选中 */
    user-select: none;
    /* 文字阴影 */
    text-shadow: 4px 4px 3px rgba(0, 0, 0, .1);
}

/* 滑动盒子的文字 */
.pre-box p {
    height: 30px;
    line-height: 30px;
    text-align: center;
    margin: 20px 0;
    /* 禁止选中 */
    user-select: none;
    font-weight: bold;
    color: white;
    text-shadow: 4px 4px 3px rgba(0, 0, 0, .1);
}

/* 图片盒子 */
.img-box {
    width: 200px;
    height: 200px;
    margin: 20px auto;
    /* 设置为圆形 */
    border-radius: 50%;
    /* 设置用户禁止选中 */
    user-select: none;
    overflow: hidden;
    box-shadow: 4px 4px 3px rgba(0, 0, 0, .1);
}

/* 图片 */
.img-box img {
    width: 100%;
    transition: 0.5s;
}

/* 登录和注册盒子 */
.login-form,
.register-form {
    flex: 1;
    height: 100%;
}

/* 标题盒子 */
.title-box {
    height: 300px;
    line-height: 500px;

}

/* 标题 */
.title-box h1 {
    text-align: center;
    color: white;
    /* 禁止选中 */
    user-select: none;
    letter-spacing: 5px;
    text-shadow: 4px 4px 3px rgba(0, 0, 0, .1);

}

/* 输入框盒子 */
.el-form {
    display: flex;
    /* 纵向布局 */
    flex-direction: column;
    /* 水平居中 */
    align-items: center;
}

.el-form-item {
    width: 65%;
}

/* 输入框 */
input {
    /* width: 60%; */
    height: 40px;
    margin-bottom: 20px;
    text-indent: 10px;
    border: 1px solid #fff;
    background-color: rgba(255, 255, 255, 0.3);
    border-radius: 120px;
    /* 增加磨砂质感 */
    backdrop-filter: blur(10px);
}

input:focus {
    /* 光标颜色 */
    color: #b0cfe9;

}

/* 聚焦时隐藏文字 */
input:focus::placeholder {
    opacity: 0;
}

/* 按钮盒子 */
.btn-box {
    display: flex;
    justify-content: center;
}

/* 按钮 */
button {
    width: 100px;
    height: 30px;
    margin: 0 7px;
    line-height: 30px;
    border: none;
    border-radius: 4px;
    background-color: #69b3f0;
    color: white;
}

/* 按钮悬停时 */
button:hover {
    /* 鼠标小手 */
    cursor: pointer;
    /* 透明度 */
    opacity: .8;
}

/* 按钮文字 */
.btn-box p {
    height: 30px;
    line-height: 30px;
    /* 禁止选中 */
    user-select: none;
    font-size: 14px;
    color: white;

}

.btn-box p:hover {
    cursor: pointer;
    border-bottom: 1px solid white;
}
</style>