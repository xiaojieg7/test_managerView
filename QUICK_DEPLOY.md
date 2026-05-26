# 🚀 快速部署指南

## 第一步：创建 GitHub 仓库

1. 打开浏览器访问 https://github.com/new
2. 填写仓库信息：
   - **Repository name**: `property-management-system`
   - **Description**: 小区物业管理系统
   - 选择 **Public** (免费)
   - ✅ 不要勾选 "Initialize this repository with a README"
3. 点击 **Create repository**

## 第二步：推送代码

创建仓库后，GitHub 会显示推送命令，运行以下命令：

```bash
cd /workspace

# 添加远程仓库（替换 YOUR_USERNAME 为您的 GitHub 用户名）
git remote add origin https://github.com/YOUR_USERNAME/property-management-system.git

# 推送代码
git push -u origin main
```

## 第三步：启用 GitHub Pages

1. 访问您的仓库：https://github.com/YOUR_USERNAME/property-management-system
2. 点击 **Settings**（设置）
3. 左侧菜单找到 **Pages**
4. 在 **Source** 部分：
   - 选择 **Deploy from a branch**
   - Branch 选择 **main**，Folder 选择 **/ (root)**
5. 点击 **Save**

## 第四步：等待部署

大约 1-2 分钟后，您的网站将上线：
```
https://YOUR_USERNAME.github.io/property-management-system/
```

## 第五步：验证部署

访问上述地址，使用测试账号登录：
- 管理员：admin / 123456
- 维修人员：repairer / 123456
- 业主：owner1 / 123456

---

## ⚠️ 注意事项

GitHub Pages 只能托管前端，后端需要另外部署。完整功能需要：
1. 本地运行后端（`cd backend && mvn spring-boot:run`）
2. 或部署后端到云服务器

或者，您可以使用 Vercel/Railway 等平台免费部署后端！
