# GitHub Pages 部署指南

## 第一步：创建 GitHub 仓库

1. 访问 https://github.com/new
2. 仓库名称填写为：`property-management-system` (或您喜欢的名称)
3. 选择 Public 或 Private (Public 免费)
4. **不要**勾选 "Initialize this repository" 选项
5. 点击 "Create repository"

## 第二步：初始化本地 Git 并推送到 GitHub

在 `/workspace` 目录下执行：

```bash
# 初始化 git (如果还没有)
git init

# 配置用户名和邮箱 (如果还没有)
git config user.name "Your Name"
git config user.email "you@example.com"

# 添加文件
git add .

# 提交
git commit -m "Initial commit"

# 推送到 GitHub (替换为您的用户名和仓库名)
git remote add origin https://github.com/<您的用户名>/property-management-system.git
git branch -M main
git push -u origin main
```

## 第三步：启用 GitHub Actions

1. 打开 GitHub 仓库页面
2. 点击顶部菜单的 "Actions"
3. 如果看到 "Workflows aren’t being run on this repository" 消息，点击 "I understand my workflows, go ahead and enable them"

## 第四步：配置 GitHub Pages

1. 打开仓库的 "Settings"
2. 在左侧菜单找到 "Pages"
3. 在 "Build and deployment" 部分：
   - Source 选择 "Deploy from a branch"
   - Branch 选择 `gh-pages` (如果不存在，选择 main，等 Action 运行一次后再回来选 gh-pages)
   - 点击 "Save"

## 第五步：触发部署

GitHub Actions 会在每次推送到 main 分支时自动运行。

或者，您也可以手动触发：

1. 打开 GitHub 仓库的 "Actions" 页面
2. 点击左侧的 "Deploy to GitHub Pages"
3. 点击 "Run workflow" 按钮

## 部署地址

部署完成后，您的网站将可通过以下地址访问：

```
https://<您的用户名>.github.io/property-management-system/
```

## 注意事项

### 1. API 地址

由于 GitHub Pages 只托管前端，API 后端需要另外部署。您可以：

- 部署到云服务器 (阿里云/腾讯云/AWS 等)
- 使用 Vercel, Render, Railway 等免费平台部署后端
- 本地运行后端时使用内网穿透工具

### 2. 临时预览

如果只是想预览，可以使用 GitHub Pages，但是 API 调用需要后端支持。

### 3. 快速本地预览

您可以使用 Python 快速预览构建好的前端：

```bash
cd frontend/dist
python3 -m http.server 8080
```

然后打开 http://localhost:8080

## 部署后的测试

使用测试账号登录：
- 管理员：admin / 123456
- 维修人员：repairer / 123456
- 业主：owner1 / 123456
