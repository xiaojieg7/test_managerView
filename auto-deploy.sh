#!/bin/bash

# GitHub 自动部署脚本
# 使用前需要设置环境变量 GH_TOKEN

if [ -z "$GH_TOKEN" ]; then
    echo "❌ 错误：需要设置 GitHub Token"
    echo ""
    echo "请先创建一个 GitHub Personal Access Token："
    echo "1. 访问 https://github.com/settings/tokens"
    echo "2. 点击 'Generate new token (classic)'"
    echo "3. 勾选 'repo' 权限"
    echo "4. 生成 token 并复制"
    echo ""
    echo "然后运行："
    echo "export GH_TOKEN='your_token_here'"
    echo "bash deploy.sh"
    exit 1
fi

echo "=========================================="
echo "🚀 小区物业管理系统 - GitHub 自动部署"
echo "=========================================="

# 配置 gh
echo ""
echo "📝 配置 GitHub CLI..."
echo "$GH_TOKEN" | gh auth login --with-token

# 创建仓库
echo ""
echo "📦 创建 GitHub 仓库..."
REPO_NAME="property-management-system"
gh repo create "$REPO_NAME" --public --description "小区物业管理系统 - Spring Boot + Vue3" --source=. --push

# 启用 GitHub Pages
echo ""
echo "⚙️ 启用 GitHub Pages..."
gh repo edit "$REPO_NAME" --enable-pages

# 等待 Actions 运行
echo ""
echo "⏳ 等待 GitHub Actions 完成构建..."
sleep 10

# 显示结果
echo ""
echo "=========================================="
echo "✅ 部署完成！"
echo "=========================================="
echo ""
echo "📱 访问您的网站："
echo "https://$(gh api user --jq '.login').github.io/$REPO_NAME/"
echo ""
echo "⏳ 注意：GitHub Pages 可能需要 2-5 分钟才能完全生效"
echo ""
echo "🔑 测试账号："
echo "   管理员：admin / 123456"
echo "   维修人员：repairer / 123456"
echo "   业主：owner1 / 123456"
