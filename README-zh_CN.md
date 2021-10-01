# Boat - 用于移植程序到 Android
[English](./README.md)
## 目录
- [简介](#简介)
- [构建](#构建)
- [相关项目](#相关项目)
- [许可](#许可)

## 简介
  Boat 提供一个在 Android 上运行一类图形程序的环境，不是模拟器。它最早衍生自 zhuowei 的 [Boarwalk](https://github.com/zhuowei/Boardwalk)。
最初完全是为了运行 Minecraft Java 版，目前也主要针对 Minecraft Java 版做有关测试。但是，*Boat 不是 Minecraft 启动器*，目前我们还有
Xboat X server 等项目。请参看 [相关项目](#相关项目)。
  
## 构建 
  通过 gradle 可以轻松构建 Android library 的 aar 包，不过也可以直接使用源码。请注意，仅支持 aarch64。原来对 32位 arm 的支持已经放弃，理论上
还可以使用不过我们不再测试。
  `export ANDROID_SDK_ROOT=你的 Android SDK 路径`
  `./gradlew assembleDebug`
  
## 相关项目
  - [MCinaBox](https://github.com/AOF-Dev/MCinaBox)。 一个用于 Android 的 Minecraft Java 版启动器，基于 Boat 提供 Minecraft 运行环境。
  - [xserver-xboat](https://github.com/AOF-Dev/xserver-xboat)。 一个用于 Android 的 X 显示服务器，从 X.org 的 Xephyr 移植而来。
  - [GLFW](https://github.com/AOF-Dev/MCinaBox/glfw-boat)。 在 Android 上使用 GLFW 创建和管理 OpenGL 上下文！大部分 API 可以使用！

## 许可
  本项目以 [GPL v2.0](http://www.gnu.org/licenses/old-licenses/gpl-2.0.html) 授权。
