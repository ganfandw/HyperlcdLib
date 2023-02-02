# HyperlcdLib

## 项目概要
HyperlcdLib 是北京超显科技基于超显Android模组开发的一套方便客户进行二次开发的扩展库。拥有丰富的 Android 功能控制接口，满足客户应用快速开发需求。

[![](https://jitpack.io/v/ganfandw/HyperlcdLib.svg)](https://jitpack.io/#ganfandw/HyperlcdLib)
![https://img.shields.io/github/languages/code-size/ganfandw/HyperlcdLib](https://img.shields.io/github/languages/code-size/ganfandw/HyperlcdLib)

## 功能简介
HyperlcdLib 提供丰富的 Android 功能控制接口，满足客户应用快速开发需求。

## 接入方法
### gradle 依赖
1. 在根目录 build.gradle 中添加 JitPack 仓库地址:
```
allprojects {
	repositories {
		...
		maven { url 'https://jitpack.io' }
	}
}
```
2. 添加项目依赖
```
  // 添加 HyperlcdLib gradle 依赖
  implementation 'com.github.zhuawaz:HyperlcdLib:v1.0.2'
```

### 本地 aar 依赖
下载 aar 文件后，放到 `app/libs` 目录中，并在 `build.gradle` 中添加如下代码:
```
  implementation files('libs/HyperlcdLib.aar')
```

## 更新日志
- 2023/02/1: HyperlcdLib 1.0.0 开始更新。
