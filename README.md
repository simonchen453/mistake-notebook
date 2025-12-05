# 错题本管理系统 (Mistake Notebook)

智能错题本管理系统 - 基于 AI 的初中学习辅助平台

## 功能特性

- 📝 **错题管理** - 录入、分类、标记错题
- 🤖 **AI 分析** - Gemini AI 分析错误原因、生成解题思路
- 📊 **统计报表** - 错题分布、薄弱知识点分析
- 🔄 **复习系统** - 艾宾浩斯遗忘曲线智能复习
- 👥 **多角色** - 支持学生、教师、家长

## 技术栈

- **后端**: Spring Boot 3.5 + JPA + MySQL
- **前端**: React 18 + Vite + Ant Design 5
- **AI**: Google Gemini API
- **缓存**: Redis
- **部署**: Docker

## 项目结构

```
mistake-notebook/
├── mn-server/              # 后端服务
│   └── src/main/java/com/mistakenotebook/
│       ├── ai/             # Gemini AI 集成
│       ├── domain/         # 领域模型
│       └── web/api/        # REST API
├── mn-web/                 # 前端应用
│   └── src/
│       ├── pages/          # 页面组件
│       └── api/            # API 服务
├── docker-compose.yml      # Docker 部署
└── Dockerfile              # 镜像构建
```

## 快速开始

### 1. 配置环境变量

```bash
cp .env.example .env
# 编辑 .env 填写 Gemini API Key
```

### 2. 启动后端

```bash
cd mn-server
mvn spring-boot:run
```

### 3. 启动前端

```bash
cd mn-web
npm install
npm run dev
```

### 4. Docker 部署

```bash
docker-compose up -d
```

## 访问地址

- 前端: http://localhost:3000
- 后端 API: http://localhost:8080/api
- API 文档: http://localhost:8080/swagger-ui.html

## License

MIT
