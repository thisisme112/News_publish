import axios from 'axios'

//创建axios实例
const Service = axios.create({
    baseURL: 'http://192.168.2.180:8000', // api的base_url
    //定义统一的请求头
    headers: {
        'Content-Type': 'application/json;charset=UTF-8'
    },
    //配置请求超时时间
    timeout: 10000

})
//请求拦截器
Service.interceptors.request.use(config => {
    config.headers.common['Authorization'] = window.sessionStorage.getItem('token')===null?null:window.sessionStorage.getItem('token');
    return config
})
//响应拦截器
Service.interceptors.response.use(response => {
    //获取接口返回的状态码
    const res = response.data
    console.log(res)
    if (res.code === 200) {
        return res
    } else {
        return Promise.reject(res)
        return res
    }
}, error => {
    return Promise.reject(error)
})

export default Service