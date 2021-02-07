# Boat - 在 Android 设备上运行 Minecraft Java 版

## 目录
- [简介](#简介)
- [构建](#构建)
- [组件](#组件)
- [相关项目](#相关项目)
- [许可](#许可)

## 简介
  Boat 提供一个在 Android 上运行 Mincraft Java 版的环境，显然它不是一个模拟器。它起源自 zhuowei 的 [Boarwalk](https://github.com/zhuowei/Boardwalk)。Boat 提供一系列开源软件的 Android 移植，例如 OpenJDK。这个仓库存储的是 Boat 的 
Android app 部分。其他软件包可在其各自的仓库里找到。参看[组件](#组件)。
  
  这个 app 包含一个非常简单的 Minecraft 启动器，仅仅用于开发中的测试。
  
## 构建  
  要构建整个项目是非常麻烦的，所以我们不建议你自己从源码构建整个项目。不过你可能希望自己构建和更新某些组件。请参看仓库中关于构建的说明。
  
  我们有一个[参考资料仓库](https://github.com/AOF-Dev/Boat-reference-material)，希望能分享各种关于 Boat 的参考资料 ：）
  
  构建指南：
  1. 构建可用于 Android 的 OpenJDK 8。
  2. 构建本项目。请注意保存好 `libboat.so` 与 `boat.h`，其他项目会使用到的。
  3. 构建适用于 Android 的 GL4ES 与 OpenAL-soft。
  4. 构建针对 Boat 的 LWJGL 2 。需要用到 `libboat.so` 与 `boat.h`。
  5. 构建针对 Boat 的 GLFW 。需要用到 `libboat.so` 与 `boat.h`。
  6. 构建针对 Boat 的 LWJGL 3 。需要 GLFW 。
  
## 组件
  - [OpenJDK 8 Android port](https://github.com/CosineMath/openjdk-jdk8u-aarch32-android).
  - [Boat app project](https://github.com/CosineMath/BoatApp).
  - [GL4ES](https://github.com/ptitSeb/gl4es).
  - [OpenAL-soft](https://github.com/kcat/openal-soft). 
  - [LWJGL 2 port for Boat](https://github.com/CosineMath/lwjgl-boat).
  - [GLFW port for Boat](https://github.com/CosineMath/glfw-boat).
  - [LWJGL 3 port for Boat](https://github.com/CosineMath/lwjgl3-boat).
  - [Gson](https://github.com/google/gson)
  - [Commons Compress](https://github.com/apache/commons-compress).
  - [XZ for Java](https://git.tukaani.org/?p=xz-java.git;a=summary).
  
## 相关项目
  - [MCinaBox](https://github.com/AOF-Dev/MCinaBox). 一个用于 Android 的 Minecraft Java 版启动器，基于 Boat 提供 Minecraft 运行环境。
  
## 许可
  本项目以 [GPL v2.0](http://www.gnu.org/licenses/old-licenses/gpl-2.0.html) 授权。各个组件有其各自的许可。
  
