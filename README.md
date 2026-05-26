# 小区物业管理系统

基于 Spring Boot + Vue3 的小区物业管理系统，支持三种角色：
- 管理员：管理所有功能
- 维修人员：处理报修
- 业主：提交报修、查看缴费和公告

## 技术栈

### 后端
- Spring Boot 3.x
- MyBatis-Plus
- MySQL 8.0
- Spring Security + JWT

### 前端
- Vue3 + TypeScript
- Vite
- Element Plus
- Pinia

## 快速开始

### 1. 数据库

```bash
# 创建数据库
mysql -u root -p < sql/init.sql
```

### 2. 后端

编辑 `backend/src/main/resources/application.yml` 配置数据库连接

```bash
cd backend
mvn spring-boot:run
```

### 3. 前端

```bash
cd frontend
npm install
npm run dev
```

## 测试账号

| 角色 | 用户名 | 密码 |
|------|--------|------|
| 管理员 | admin | 123456 |
| 维修人员 | repairer | 123456 |
| 业主 | owner1 | 123456 |

## 部署

### GitHub Pages

1. 创建 GitHub 仓库
2. 推送代码到 main 分支
3. 在仓库 Settings > Pages 中，选择部署来源为 gh-pages 分支
4. 启用 GitHub Actions，触发部署

部署后的地址将是：`https://<username>.github.io/<repo-name>/`

### 本地部署

前端构建：
```bash
cd frontend
npm run build
```
然后将 `frontend/dist` 目录部署到 Web 服务器。
