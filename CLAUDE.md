# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**撸了么 (Luleme)** - 健康记录 Android 应用，帮助用户管理个人健康频率。数据完全本地存储，无网络上传。

## Build Commands

```bash
# 构建 Debug APK
./gradlew assembleDebug

# 构建 Release APK (需要签名配置)
./gradlew assembleRelease

# 运行测试
./gradlew test                    # 单元测试
./gradlew connectedAndroidTest    # 仪器测试

# 清理构建
./gradlew clean

# 检查依赖
./gradlew dependencies
```

**环境要求**: JDK 17, Gradle 8.5, Android SDK 34

**版本管理**: 版本号存储在根目录 `VERSION` 文件中，格式 `x.y.z`。构建时自动计算 versionCode。

## Architecture

采用 **MVVM + Clean Architecture** 分层架构，依赖注入使用 Hilt。

### 分层结构

```
com.luleme/
├── data/                    # 数据层
│   ├── encryption/          # AES-256-GCM 加密 (AndroidKeyStore)
│   ├── local/
│   │   ├── dao/             # Room DAO 接口
│   │   ├── database/        # Room Database 定义
│   │   └── entity/          # Room Entity (RecordEntity, UserSettingsEntity)
│   └── repository/          # Repository 实现
├── domain/                  # 领域层
│   ├── model/               # 领域模型 (Record, UserSettings)
│   └── repository/          # Repository 接口
├── di/                      # Hilt 模块
│   ├── AppModule.kt         # Gson 等通用依赖
│   ├── DatabaseModule.kt    # Room Database + DAO
│   └── RepositoryModule.kt  # Repository 绑定
└── ui/                      # UI 层
    ├── components/          # 可复用 Compose 组件
    ├── navigation/          # Navigation Compose 路由
    ├── screens/             # 各页面 Screen + ViewModel
    │   ├── home/            # 主页 (记录按钮、本周概览、健康提示)
    │   ├── lock/            # PIN 锁屏
    │   ├── settings/        # 设置 (年龄、PIN、数据备份/恢复)
    │   └── statistics/      # 统计 (周视图、月热力图、连续天数)
    └── theme/               # Material3 主题定义
```

### 关键设计决策

1. **数据加密**: Record 的 note 字段使用 AES-256-GCM 加密存储，密钥由 AndroidKeyStore 管理
2. **PIN 安全**: PIN 使用 SHA-256 哈希存储，不保存明文
3. **用户设置**: UserSettingsEntity 使用单例模式 (固定 id=1)
4. **数据库迁移**: 使用 `fallbackToDestructiveMigration()`，schema 变更时允许重建
5. **动态颜色**: 默认禁用 (`dynamicColor = false`)，保持一致的品牌风格

### 导航流程

启动 → LockScreen (PIN 验证) → HomeScreen (主页面)

底部导航栏: 主页 | 统计 | 设置

### 依赖注入要点

- 所有 Repository 通过 `RepositoryModule` 绑定接口到实现
- Database 和 DAO 通过 `DatabaseModule` 提供
- ViewModel 使用 `@HiltViewModel` + `@Inject constructor`

## CI/CD

GitHub Actions 自动构建: push 到 main 且修改 `VERSION` 或 `app/**` 时触发。

签名通过环境变量配置:
- `SIGNING_KEY_STORE_PATH`
- `SIGNING_STORE_PASSWORD`
- `SIGNING_KEY_ALIAS`
- `SIGNING_KEY_PASSWORD`

## Code Conventions

- UI 组件使用 Material3 设计系统
- 可复用组件定义在 `ui/components/` (CuteCard, SettingItem, SettingGroup 等)
- ViewModel 状态使用 `sealed class` 或 `data class` + `StateFlow`
- 日期格式统一使用 `yyyy-MM-dd` (ISO_DATE)
- 中文界面，用户-facing 文本直接硬编码
