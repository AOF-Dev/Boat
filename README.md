# Boat - Running Minecraft Java Edition on Android
[中文](./README-zh_CN.md)
## Contents
- [Introduction](#Introduction)
- [Building](#Building)
- [Components](#Components)
- [Related Projects](#Related-Projects)
- [License](#License)

## Introducion
  Boat provides an environment to run Minecraft Java Edition on Android. It evolves from zhuowei's [Boarwalk](https://github.com/zhuowei/Boardwalk). Boat includes a series of Android ports of important software packages.
This repository contains the Android app part of Boat. Other packages could be found in respective repositories. Please look at [Components](#Components).
  
  This app is NOT a Minecraft launcher.
## Building  
  It takes time to build whole project. So it is not suggested to build whole one youself. But you may want to build and update some components. Please follow the instructions in the repositories.
  
  We set up a [reference material repository](https://github.com/AOF-Dev/Boat-reference-material), hoping that we can collect useful imformation about boat :).
  
  Building instruction:
  1. Build OpenJDK 8 Android port.
  2. Build this app project. Note that you should copy `libboat.so` and `boat.h` that other components depend on.
  3. Build GL4ES and OpenAL-soft for Android.
  4. Build LWJGL 2 port for Boat. Need `libboat.so` and `boat.h`.
  5. Build GLFW port for Boat. Need `libboat.so` and `boat.h`.
  6. Build LWJGL 3 port for Boat. Need GLFW port for Boat.
  
## Components
  - [OpenJDK 8 Android port](https://github.com/CosineMath/openjdk-jdk8u-aarch32-android)
  - [Boat app project](https://github.com/CosineMath/BoatApp)
  - [GL4ES](https://github.com/ptitSeb/gl4es)
  - [OpenAL-soft](https://github.com/kcat/openal-soft)
  - [LWJGL 2 port for Boat](https://github.com/CosineMath/lwjgl-boat)
  - [GLFW port for Boat](https://github.com/CosineMath/glfw-boat)
  - [LWJGL 3 port for Boat](https://github.com/CosineMath/lwjgl3-boat)
  - [Gson](https://github.com/google/gson)
  - [Commons Compress](https://github.com/apache/commons-compress)
  - [XZ for Java](https://git.tukaani.org/?p=xz-java.git;a=summary)
  
## Related-Projects
  - [MCinaBox](https://github.com/AOF-Dev/MCinaBox). A Minecraft Java Edition Launcher on Android. Using Boat to provide runtime environment.
  
## License
  This app project is distributed under [GPL v2.0](http://www.gnu.org/licenses/old-licenses/gpl-2.0.html). Other components have their respective license.
  
