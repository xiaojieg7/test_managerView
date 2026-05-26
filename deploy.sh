#!/bin/bash

# 小区物业管理系统 - GitHub Pages 快速部署脚本
# 使用前请确保已经在 GitHub 上创建了仓库

set -e

echo "=========================================="
echo "小区物业管理系统 - GitHub Pages 部署工具"
echo "=========================================="

# 检查是否已经初始化 git
if [ ! -d ".git" ]; then
    echo "初始化 Git 仓库..."
    git init
fi

# 检查 git 配置
if [ -z "$(git config --global user.name)" ]; then
    echo ""
    echo "请输入您的 Git 用户信息："
    read -p "用户名: " GIT_NAME
    read -p "邮箱: " GIT_EMAIL
    
    git config user.name "$GIT_NAME"
    git config user.email "$GIT_EMAIL"
fi

# 添加文件并提交
echo ""
echo "添加文件到 Git..."
git add .

echo ""
echo "提交变更..."
git commit -m "Initial commit - Property Management System" || true

# 获取仓库信息
echo ""
echo "请输入您的 GitHub 仓库信息："
read -p "GitHub 用户名: " GITHUB_USER
read -p "仓库名称: " REPO_NAME

# 添加远程仓库
echo ""
echo "连接到 GitHub 仓库..."
git remote remove origin 2>/dev/null || true
git remote add origin "https://github.com/${GITHUB_USER}/${REPO_NAME}.git"

# 推送到 GitHub
echo ""
echo "推送到 GitHub main 分支..."
git branch -M main
if git push -u origin main; then
    echo ""
    echo "✅ 代码已成功推送到 GitHub!"
    echo ""
    echo "下一步："
    echo "1. 访问 https://github.com/${GITHUB_USER}/${REPO_NAME}"
    echo "2. 打开 Settings -> Pages"
    echo "3. 配置 Source 为 Deploy from a branch"
    echo "4. 选择 gh-pages 分支 (等 Actions 运行一次后才会出现)"
    echo "5. 部署地址将会是: https://${GITHUB_USER}.github.io/${REPO_NAME}/"
    echo ""
else
    echo ""
    echo "❌ 推送失败，请检查："
    echo "1. 您是否在 GitHub 上创建了仓库 ${REPO_NAME}"
    echo "2. 您是否有推送权限"
    echo "3. 尝试手动执行推送命令"
fi
