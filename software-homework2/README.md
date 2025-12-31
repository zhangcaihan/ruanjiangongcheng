# 部署说明

## 环境准备

### 1. 系统环境

- `Python 3.9+`
- `jdk 25+`

### 2. 数据库环境

- `mysql 8.0.27+`
- `redis 8.0.3+`

### 3.构建工具

- `maven 3.9.9+`
- `npm 10.8.1`

## 部署步骤

### 1. 安装依赖

#### 1.1 安装 Python 依赖

```bash 
pip install openai
```

#### 1.2 安装 Java 依赖

```bash 
cd zeyuli/zeyuli-job
mvn clean package
```

####  1.3 安装 npm 依赖

```bash 
cd zeyuli/front
npm install
```

#### 1.4 初始化数据库

见[script.sql](zeyuli/sql/script.sql)

### 2.修改配置

#### 2.1 yaml配置

```bash 
cd zeyuli/zeyuli-job/src/main/resources

# 修改 application.yaml 启用开发环境
# 将 spring.profiles.active: prod,修改为 spring.profiles.active: dev
vim application.yaml

# 修改 application-prod.yaml 修改数据库配置
# 将 spring.datasource.url: jdbc:mysql://localhost:3306/homework2?useSSL=false
# 将 spring.datasource.username: root 修改为你的数据库用户名
# 将 spring.datasource.password: root 修改为你的数据库密码
# 将spring.data.redis: localhost 修改为你的redis地址
# 修改ai.script.path: /path/to/your/script.py 修改为你的脚本路径
vim application-dev.yaml

# 修改 python脚本的api-key

# 修改api-key
# api_key=os.getenv("DEEPSeek_API_KEY", "your_api_key")
vim path/to/your/script.py
```

### 3.打包

```bash 
# 前端项目打包
cd zeyuli/front
npm install
npm run build

# 将dist目录下的所有文件拷贝到静态资源目录下
cp -r dist/* zeyuli/zeyuli-job/src/main/resources/static

# 后端项目打包
cd zeyuli/zeyuli-job
mvn clean package spring-boot:repackage -DskipTests
```

### 4.启动相关服务

```bash 
# 启动redis
sudo systemctl start redis

# 启动mysql
sudo service mysql start
```

### 5.启动项目      

```bash 
cd zeyuli/zeyuli-job/target

# 启动后端项目
nohup java -jar zeyuli/zeyuli-job/target/zeyuli-job-0.0.1-SNAPSHOT.jar > zeyuli.log 2>&1 &
```