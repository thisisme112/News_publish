# 基于tomcat和vue新闻发布系统

***注意：*** 本项目是基于vue-cli3.0和tomcat开发的，所以需要安装node.js和jdk1.8，tomcat10，以及mysql数据库。本项目未添加任何安全措施，只能用于学校课设使用！如果有帮到你，请点击Star

***tomcat的版本不能低于10！***
## 安装运行环境
```
npm install
```

### 开发使用的编译运行，可以热更新
```
npm run serve
```

### 编译打包
```
npm run build
```

### 使用方法

#### 1.需要修改manager.vue中EimageUrl和imageUrl的地址，改为自己的图片服务器地址。我的静态资源是使用nginx来配置的，所以这里的地址是nginx的地址。
nginx配置静态资源如下：
```
在conf/nginx.conf中的server{}中添加如下配置：
    location /images/ {
        alias D:/Web/Java_news/images; // 换成你自己的图片地址
        expires 1w;   
    }
```
***注意：*** 在windows下，需要配置nginx的自启动，否则每次重启电脑都需要手动启动nginx，很麻烦。在nginx的安装目录下，找到nginx.exe，右键创建快捷方式，然后将快捷方式复制到C:\ProgramData\Microsoft\Windows\Start Menu\Programs\StartUp目录下，这样就可以实现开机自启动了。

#### 2.后端主要需要修改mysql的配置，修改为自己的数据库名称，用户名和密码。在src/main/java/com/example/java_news/util/DatabaseConnector.java中修改。
src/main/java/com/example/java_news/servlet/DictServlet.java也需要修改。

***注意：*** 在使用mysql时，需要在mysql中创建数据库java_web，然后在数据库中创建表，表的结构如下：
```
# 储存新闻：
CREATE TABLE `news` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `content` text NOT NULL,
  `created_at` datetime NOT NULL,
  `updated_at` datetime NOT NULL,
  `pic` varchar(255) NOT NULL,
  `link` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=188 DEFAULT CHARSET=utf8mb3
```
```
# 储存管理员用户：
CREATE TABLE `users` (
  `username` varchar(20) NOT NULL,
  `password` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3
```
```
# 储存敏感词：
CREATE TABLE `sensitive words` (
  `id` int NOT NULL AUTO_INCREMENT,
  `word` varchar(50) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb3
```
