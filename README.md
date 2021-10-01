# Boat - Helper library for Android
[中文](./README-zh_CN.md)
## Contents
- [Introduction](#Introduction)
- [Building](#Building)
- [Related Projects](#Related-Projects)
- [License](#License)

## Introducion
  Boat provides an environment to run some interesting programs on Android. It evolves from zhuowei's [Boarwalk](https://github.com/zhuowei/Boardwalk).
Initilaly, Boat is just for playing Minecraft Java Edition on Android. Currently, we still test Boat with Minecraft JE. However,
*Boat is NOT a Minecraft launcher*. Please have a look on [Related Projects](#Related-Projects).

## Building  
  It is very simple to build the aar package. *NOTE*，only support aarch64。32bit arm has been abandoned and is never tested.

  `export ANDROID_SDK_ROOT=Your Android SDK path`

  `./gradlew assembleDebug`
  
## Related-Projects
  - [MCinaBox](https://github.com/AOF-Dev/MCinaBox). A Minecraft Java Edition Launcher on Android. Using Boat to provide runtime environment.
  - [xserver-xboat](https://github.com/AOF-Dev/xserver-xboat)。 X server on Android, ported from Xephyr.
  - [GLFW](https://github.com/AOF-Dev/MCinaBox/glfw-boat)。 Create and manage OpenGL context with GLFW! Most of APIs work!
  
## License
  This app project is distributed under [GPL v2.0](http://www.gnu.org/licenses/old-licenses/gpl-2.0.html).
