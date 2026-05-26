# 🚀 快速创建 GitHub Personal Access Token

## 步骤：
1. 访问：https://github.com/settings/tokens
2. 点击 **Generate new token (classic)**
3. 填写：
   - **Note**: Trae Deploy
   - **Expiration**: No expiration
   - ✅ 勾选 **repo** (所有子选项)
4. 点击 **Generate token**
5. 复制生成的 token

## 然后运行：
```bash
export GH_TOKEN="ghp_xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"
gh auth login --with-token <<< "$GH_TOKEN"
```

或者直接发给我，我来帮您完成部署！
