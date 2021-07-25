package cosine.boat;

import java.io.*;
import java.util.*;

public class LoadMe {

    public static native int chdir(String str);
    public static native void redirectStdio(String file);
    public static native void setenv(String str, String str2);
    public static native int dlopen(String name);
    public static native void patchLinker();
    public static native int dlexec(String[] name);

    static {
        System.loadLibrary("loadme");
    }

    public static int exec(int w, int h) {
        try {
            String runtimePath = "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64";

            String arch = "aarch64";
            String vm_variant = "server";

            //String libraryPath = runtimePath + "/j2re-image/lib/" + arch + "/jli:" + runtimePath + "/j2re-image/lib/" + arch + ":" + runtimePath + "/lwjgl-2:" + runtimePath;
            String libraryPath = runtimePath + "/j2re-image/lib/" + arch + "/jli:" + runtimePath + "/j2re-image/lib/" + arch + ":" + runtimePath + "/lwjgl-3:" + runtimePath;

            patchLinker();

            String home = "/sdcard/boat";
            String tmpdir = BoatApplication.getCurrentActivity().getCacheDir().getAbsolutePath();

            //String classPath = config.get("runtimePath") + "/lwjgl-2/lwjgl.jar:" + config.get("runtimePath") + "/lwjgl-2/lwjgl_util.jar:" + mcVersion.getClassPath(config);
            //String classPath = "/storage/emulated/0/boat/gamedir/versions/1.16.4/1.16.4.jar:" + runtimePath + "/lwjgl-3/lwjgl-jemalloc.jar:" + runtimePath + "/lwjgl-3/lwjgl-tinyfd.jar:" + runtimePath + "/lwjgl-3/lwjgl-opengl.jar:" + runtimePath + "/lwjgl-3/lwjgl-openal.jar:" + runtimePath + "/lwjgl-3/lwjgl-glfw.jar:" + runtimePath + "/lwjgl-3/lwjgl-stb.jar:" + runtimePath + "/lwjgl-3/lwjgl.jar:" +  mcVersion.getClassPath(config);
 
            setenv("HOME", home);
            setenv("JAVA_HOME" ,runtimePath + "/j2re-image");
            //setenv("_JAVA_LAUNCHER_DEBUG", "1");

            // openjdk
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libfreetype.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/jli/libjli.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/" + vm_variant + "/libjvm.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libverify.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libjava.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libnet.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libnio.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libawt.so");
            dlopen(runtimePath + "/j2re-image/lib/" + arch + "/libawt_headless.so");

            // others
            dlopen(runtimePath + "/libopenal.so.1");

            /*
            libraryPath = libraryPath + ":" + runtimePath + "/gl4es";
            setenv("LIBGL_MIPMAP", "3");
            setenv("LIBGL_NORMALIZE", "1");
            dlopen(runtimePath + "/gl4es/libGL.so.1");
            dlopen(runtimePath + "/gl4es/libEGL.so.1");
            */

            libraryPath = libraryPath + ":" + runtimePath + "/mesa/lib";
            
            setenv("ZINK_DEBUG", "validation");
            setenv("LIBGL_DEBUG", "verbose");
            setenv("MESA_DEBUG", "incomplete_tex,incomplete_fbo,context");
            setenv("LIBGL_DRIVERS_PATH", runtimePath + "/mesa/lib/dri");
            setenv("MESA_GL_VERSION_OVERRIDE", "3.2");
            //setenv("MESA_GLSL_VERSION_OVERRIDE", "150");
            setenv("force_glsl_extensions_warn", "true");
            setenv("allow_higher_compat_version", "true");
            setenv("allow_glsl_extension_directive_midshader", "true");
            setenv("GALLIUM_DRIVER", "zink");
            setenv("MESA_GLSL_CACHE_DIR", tmpdir);
            dlopen(runtimePath + "/mesa/lib/libexpat.so.1");
            dlopen(runtimePath + "/mesa/lib/libglapi.so.0");
            dlopen(runtimePath + "/mesa/lib/libEGL.so.1");
            dlopen(runtimePath + "/mesa/lib/libGL.so.1");
            dlopen(runtimePath + "/mesa/lib/dri/swrast_dri.so");

            //dlopen(runtimePath + "/lwjgl-2/liblwjgl64.so");

            dlopen(runtimePath + "/libglfw.so");

            dlopen(runtimePath + "/lwjgl-3/liblwjgl.so");
            dlopen(runtimePath + "/lwjgl-3/liblwjgl_stb.so");
            dlopen(runtimePath + "/lwjgl-3/liblwjgl_tinyfd.so");
            dlopen(runtimePath + "/lwjgl-3/liblwjgl_opengl.so");

            redirectStdio(home + "/boat_output.txt");
            chdir(home);

            /*
            String finalArgs[] = new String[]{
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/glfw-test/libparticles.so", 
            };
            System.out.println("Executable exited with code : " + dlexec(finalArgs));
            */

            String finalArgs[] = new String[]{
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/j2re-image/bin/java",
                "-Dminecraft.client.jar=/storage/emulated/0/boat/gamedir/versions/21w18a/21w18a.jar",
                "-Duser.home=null",
                "-XX:+UnlockExperimentalVMOptions",
                "-XX:+UseG1GC",
                "-XX:G1NewSizePercent=20",
                "-XX:G1ReservePercent=20",
                "-XX:MaxGCPauseMillis=50",
                "-XX:G1HeapRegionSize=16M",
                "-XX:-UseAdaptiveSizePolicy",
                "-XX:-OmitStackTraceInFastThrow",
                "-Xmn1024m",
                "-Xms2048m",
                "-Xmx4096m",
                "-Dfml.ignoreInvalidMinecraftCertificates=true",
                "-Dfml.ignorePatchDiscrepancies=true",
                "-Djava.library.path=" + libraryPath,
                "-Dminecraft.launcher.brand=HMCL",
                "-Dminecraft.launcher.version=3.3.163",
                "-Djava.io.tmpdir=" + tmpdir,
                "-cp",
                "/storage/emulated/0/boat/gamedir/libraries/com/mojang/patchy/1.2.3/patchy-1.2.3.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/oshi-project/oshi-core/1.1/oshi-core-1.1.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/net/java/dev/jna/jna/4.4.0/jna-4.4.0.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/net/java/dev/jna/platform/3.4.0/platform-3.4.0.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/ibm/icu/icu4j/66.1/icu4j-66.1.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/mojang/javabridge/1.1.23/javabridge-1.1.23.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/net/sf/jopt-simple/jopt-simple/5.0.3/jopt-simple-5.0.3.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/io/netty/netty-all/4.1.25.Final/netty-all-4.1.25.Final.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/google/guava/guava/21.0/guava-21.0.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/org/apache/commons/commons-lang3/3.5/commons-lang3-3.5.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/commons-io/commons-io/2.5/commons-io-2.5.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/commons-codec/commons-codec/1.10/commons-codec-1.10.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/mojang/brigadier/1.0.18/brigadier-1.0.18.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/mojang/datafixerupper/4.0.26/datafixerupper-4.0.26.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/google/code/gson/gson/2.8.0/gson-2.8.0.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/mojang/authlib/2.1.29/authlib-2.1.29.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/org/apache/commons/commons-compress/1.8.1/commons-compress-1.8.1.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/org/apache/httpcomponents/httpclient/4.3.3/httpclient-4.3.3.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/org/apache/httpcomponents/httpcore/4.3.2/httpcore-4.3.2.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/it/unimi/dsi/fastutil/8.2.1/fastutil-8.2.1.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/org/apache/logging/log4j/log4j-api/2.8.1/log4j-api-2.8.1.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/org/apache/logging/log4j/log4j-core/2.8.1/log4j-core-2.8.1.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-jemalloc.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-openal.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-opengl.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-glfw.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-stb.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-tinyfd.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/mojang/text2speech/1.11.3/text2speech-1.11.3.jar:" +
                "/storage/emulated/0/boat/gamedir/versions/21w18a/21w18a.jar",
                "net.minecraft.client.main.Main",
                "--username",
                "cosine",
                "--version",
                "HMCL 3.3.163",
                "--gameDir",
                "/storage/emulated/0/boat/gamedir",
                "--assetsDir",
                "/storage/emulated/0/boat/gamedir/assets",
                "--assetIndex",
                "1.17",
                "--uuid",
                "f940db7497ea30aca0f3f57f0ab8bbf3",
                "--accessToken",
                "b3dafab6ee0144319f8675d73ecee600",
                "--userType",
                "mojang",
                "--versionType",
                "HMCL 3.3.163",
                "--width",
                "" + w,
                "--height",
                "" + h,
            };
            System.out.println("OpenJDK exited with code : " + dlexec(finalArgs));

            /*
            String finalArgs[] = new String[]{
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/j2re-image/bin/java", 
                "-Dminecraft.client.jar=/storage/emulated/0/boat/gamedir/versions/1.16.4/1.16.4.jar", 
                "-Duser.home=null", 
                "-XX:+UnlockExperimentalVMOptions", 
                "-XX:+UseG1GC", 
                "-XX:G1NewSizePercent=20", 
                "-XX:G1ReservePercent=20", 
                "-XX:MaxGCPauseMillis=50", 
                "-XX:G1HeapRegionSize=16M", 
                "-XX:-UseAdaptiveSizePolicy", 
                "-XX:-OmitStackTraceInFastThrow", 
                "-Xms4096m", 
                "-Xmn2048m", 
                "-Xmx4096m", 
                "-Dfml.ignoreInvalidMinecraftCertificates=true", 
                "-Dfml.ignorePatchDiscrepancies=true", 
                "-Djava.library.path=" + libraryPath, 
                "-Dminecraft.launcher.brand=HMCL", 
                "-Dminecraft.launcher.version=3.3.163", 
                "-Djava.io.tmpdir=/data/user/0/jackpal.androidterm/app_HOME/tmp", 
                "-cp", 
                "/storage/emulated/0/boat/gamedir/libraries/com/mojang/patchy/1.1/patchy-1.1.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/oshi-project/oshi-core/1.1/oshi-core-1.1.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/net/java/dev/jna/jna/4.4.0/jna-4.4.0.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/net/java/dev/jna/platform/3.4.0/platform-3.4.0.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/ibm/icu/icu4j/66.1/icu4j-66.1.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/mojang/javabridge/1.0.22/javabridge-1.0.22.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/net/sf/jopt-simple/jopt-simple/5.0.3/jopt-simple-5.0.3.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/io/netty/netty-all/4.1.25.Final/netty-all-4.1.25.Final.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/google/guava/guava/21.0/guava-21.0.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/org/apache/commons/commons-lang3/3.5/commons-lang3-3.5.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/commons-io/commons-io/2.5/commons-io-2.5.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/commons-codec/commons-codec/1.10/commons-codec-1.10.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/mojang/brigadier/1.0.17/brigadier-1.0.17.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/mojang/datafixerupper/4.0.26/datafixerupper-4.0.26.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/google/code/gson/gson/2.8.0/gson-2.8.0.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/mojang/authlib/2.1.28/authlib-2.1.28.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/org/apache/commons/commons-compress/1.8.1/commons-compress-1.8.1.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/org/apache/httpcomponents/httpclient/4.3.3/httpclient-4.3.3.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/org/apache/httpcomponents/httpcore/4.3.2/httpcore-4.3.2.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/it/unimi/dsi/fastutil/8.2.1/fastutil-8.2.1.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/org/apache/logging/log4j/log4j-api/2.8.1/log4j-api-2.8.1.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/org/apache/logging/log4j/log4j-core/2.8.1/log4j-core-2.8.1.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-jemalloc.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-openal.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-opengl.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-glfw.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-stb.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-tinyfd.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/mojang/text2speech/1.11.3/text2speech-1.11.3.jar:" +
                "/storage/emulated/0/boat/gamedir/versions/1.16.4/1.16.4.jar", 
                "net.minecraft.client.main.Main", 
                "--username", 
                "cosine", 
                "--version", 
                "HMCL 3.3.163", 
                "--gameDir", 
                "/storage/emulated/0/boat/gamedir", 
                "--assetsDir", 
                "/storage/emulated/0/boat/gamedir/assets", 
                "--assetIndex", 
                "1.16", 
                "--uuid", 
                "f940db7497ea30aca0f3f57f0ab8bbf3", 
                "--accessToken", 
                "e102e1ba308a46ba97427db9b48fc97c", 
                "--userType", 
                "mojang", 
                "--versionType", 
                "HMCL 3.3.163", 
                "--width", 
                "" + w, 
                "--height", 
                "" + h,
            };
            System.out.println("OpenJDK exited with code : " + dlexec(finalArgs));
            
            */
        
            /*
            String finalArgs[] = new String[]{
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/j2re-image/bin/java",
                "-Dminecraft.client.jar=/storage/emulated/0/boat/gamedir/versions/21w17a/21w17a.jar",
                "-Duser.home=null",
                "-XX:+UnlockExperimentalVMOptions",
                "-XX:+UseG1GC",
                "-XX:G1NewSizePercent=20",
                "-XX:G1ReservePercent=20",
                "-XX:MaxGCPauseMillis=50",
                "-XX:G1HeapRegionSize=16M",
                "-XX:-UseAdaptiveSizePolicy",
                "-XX:-OmitStackTraceInFastThrow",
                "-Xms4096m", 
                "-Xmn2048m", 
                "-Xmx4096m", 
                "-Dfml.ignoreInvalidMinecraftCertificates=true",
                "-Dfml.ignorePatchDiscrepancies=true",
                "-Djava.library.path=" + libraryPath,
                "-Dminecraft.launcher.brand=HMCL",
                "-Dminecraft.launcher.version=3.3.163",
                "-Djava.io.tmpdir=/data/user/0/jackpal.androidterm/app_HOME/tmp", 
                "-cp",
                "/storage/emulated/0/boat/gamedir/libraries/com/mojang/patchy/1.2.3/patchy-1.2.3.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/oshi-project/oshi-core/1.1/oshi-core-1.1.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/net/java/dev/jna/jna/4.4.0/jna-4.4.0.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/net/java/dev/jna/platform/3.4.0/platform-3.4.0.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/ibm/icu/icu4j/66.1/icu4j-66.1.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/mojang/javabridge/1.1.23/javabridge-1.1.23.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/net/sf/jopt-simple/jopt-simple/5.0.3/jopt-simple-5.0.3.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/io/netty/netty-all/4.1.25.Final/netty-all-4.1.25.Final.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/google/guava/guava/21.0/guava-21.0.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/org/apache/commons/commons-lang3/3.5/commons-lang3-3.5.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/commons-io/commons-io/2.5/commons-io-2.5.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/commons-codec/commons-codec/1.10/commons-codec-1.10.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/mojang/brigadier/1.0.18/brigadier-1.0.18.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/mojang/datafixerupper/4.0.26/datafixerupper-4.0.26.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/google/code/gson/gson/2.8.0/gson-2.8.0.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/mojang/authlib/2.1.29/authlib-2.1.29.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/org/apache/commons/commons-compress/1.8.1/commons-compress-1.8.1.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/org/apache/httpcomponents/httpclient/4.3.3/httpclient-4.3.3.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/org/apache/httpcomponents/httpcore/4.3.2/httpcore-4.3.2.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/it/unimi/dsi/fastutil/8.2.1/fastutil-8.2.1.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/org/apache/logging/log4j/log4j-api/2.8.1/log4j-api-2.8.1.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/org/apache/logging/log4j/log4j-core/2.8.1/log4j-core-2.8.1.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-jemalloc.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-openal.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-opengl.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-glfw.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-stb.jar:" +
                "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-tinyfd.jar:" +
                "/storage/emulated/0/boat/gamedir/libraries/com/mojang/text2speech/1.11.3/text2speech-1.11.3.jar:" +
                "/storage/emulated/0/boat/gamedir/versions/21w17a/21w17a.jar",
                "net.minecraft.client.main.Main",
                "--username",
                "cosine",
                "--version",
                "HMCL 3.3.163",
                "--gameDir",
                "/storage/emulated/0/boat/gamedir",
                "--assetsDir",
                "/storage/emulated/0/boat/gamedir/assets",
                "--assetIndex",
                "1.17",
                "--uuid",
                "f940db7497ea30aca0f3f57f0ab8bbf3",
                "--accessToken",
                "f45ec47a2b2e4665b42ee1e924e161ca",
                "--userType",
                "mojang",
                "--versionType",
                "HMCL 3.3.163",
                "--width",
                "" + w,
                "--height",
                "" + h,

            };
            */
            /*

             String finalArgs[] = new String[]{
             "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/j2re-image/bin/java",
             "-Dminecraft.client.jar=/storage/emulated/0/boat/gamedir/versions/1.16.4/1.16.4.jar",
             "-Duser.home=null",
             "-XX:+UnlockExperimentalVMOptions",
             "-XX:+UseG1GC",
             "-XX:G1NewSizePercent=20",
             "-XX:G1ReservePercent=20",
             "-XX:MaxGCPauseMillis=50",
             "-XX:G1HeapRegionSize=16M",
             "-XX:-UseAdaptiveSizePolicy",
             "-XX:-OmitStackTraceInFastThrow",
             "-Xms4096M", 
             "-Xmn2048M",
             "-Xmx4096M",
             "-Xss256K",
             "-Dfml.ignoreInvalidMinecraftCertificates=true",
             "-Dfml.ignorePatchDiscrepancies=true",
             "-Djava.library.path=" + libraryPath,
             "-Dminecraft.launcher.brand=HMCL",
             "-Dminecraft.launcher.version=3.3.163",
             "-Djava.io.tmpdir=/data/user/0/jackpal.androidterm/app_HOME/tmp", 
             "-cp",
             "/storage/emulated/0/boat/gamedir/libraries/net/fabricmc/tiny-mappings-parser/0.2.2.14/tiny-mappings-parser-0.2.2.14.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/net/fabricmc/sponge-mixin/0.8.2+build.24/sponge-mixin-0.8.2+build.24.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/net/fabricmc/tiny-remapper/0.3.0.70/tiny-remapper-0.3.0.70.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/net/fabricmc/access-widener/1.0.0/access-widener-1.0.0.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/net/fabricmc/fabric-loader-sat4j/2.3.5.4/fabric-loader-sat4j-2.3.5.4.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/com/google/jimfs/jimfs/1.2-fabric/jimfs-1.2-fabric.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/org/ow2/asm/asm/9.0/asm-9.0.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/org/ow2/asm/asm-analysis/9.0/asm-analysis-9.0.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/org/ow2/asm/asm-commons/9.0/asm-commons-9.0.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/org/ow2/asm/asm-tree/9.0/asm-tree-9.0.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/org/ow2/asm/asm-util/9.0/asm-util-9.0.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/com/google/guava/guava/21.0/guava-21.0.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/net/fabricmc/intermediary/1.16.4/intermediary-1.16.4.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/net/fabricmc/fabric-loader/0.10.8/fabric-loader-0.10.8.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/com/mojang/patchy/1.1/patchy-1.1.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/oshi-project/oshi-core/1.1/oshi-core-1.1.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/net/java/dev/jna/jna/4.4.0/jna-4.4.0.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/net/java/dev/jna/platform/3.4.0/platform-3.4.0.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/com/ibm/icu/icu4j/66.1/icu4j-66.1.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/com/mojang/javabridge/1.0.22/javabridge-1.0.22.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/net/sf/jopt-simple/jopt-simple/5.0.3/jopt-simple-5.0.3.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/io/netty/netty-all/4.1.25.Final/netty-all-4.1.25.Final.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/org/apache/commons/commons-lang3/3.5/commons-lang3-3.5.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/commons-io/commons-io/2.5/commons-io-2.5.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/commons-codec/commons-codec/1.10/commons-codec-1.10.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/com/mojang/brigadier/1.0.17/brigadier-1.0.17.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/com/mojang/datafixerupper/4.0.26/datafixerupper-4.0.26.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/com/google/code/gson/gson/2.8.0/gson-2.8.0.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/com/mojang/authlib/2.0.27/authlib-2.0.27.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/org/apache/commons/commons-compress/1.8.1/commons-compress-1.8.1.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/org/apache/httpcomponents/httpclient/4.3.3/httpclient-4.3.3.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/org/apache/httpcomponents/httpcore/4.3.2/httpcore-4.3.2.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/it/unimi/dsi/fastutil/8.2.1/fastutil-8.2.1.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/org/apache/logging/log4j/log4j-api/2.8.1/log4j-api-2.8.1.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/org/apache/logging/log4j/log4j-core/2.8.1/log4j-core-2.8.1.jar:" +
             "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl.jar:" +
             "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-jemalloc.jar:" +
             "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-openal.jar:" +
             "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-opengl.jar:" +
             "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-glfw.jar:" +
             "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-stb.jar:" +
             "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-3/lwjgl-tinyfd.jar:" +
             "/storage/emulated/0/boat/gamedir/libraries/com/mojang/text2speech/1.11.3/text2speech-1.11.3.jar:" +
             "/storage/emulated/0/boat/gamedir/versions/1.16.4/1.16.4.jar",
             "net.fabricmc.loader.launch.knot.KnotClient",
             "--username",
             "cosine",
             "--version",
             "HMCL 3.3.163",
             "--gameDir",
             "/storage/emulated/0/boat/gamedir",
             "--assetsDir",
             "/storage/emulated/0/boat/gamedir/assets",
             "--assetIndex",
             "1.16",
             "--uuid",
             "f940db7497ea30aca0f3f57f0ab8bbf3",
             "--accessToken",
             "046c840acd7b4727b080024b1e360a4f",
             "--userType",
             "mojang",
             "--versionType",
             "HMCL 3.3.163",
             "--width",
             "2270",
             "--height",
             "1080" 
             };
             */
            /*
             String finalArgs[] = new String[]{
             "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/j2re-image/bin/java", 
             "-Dminecraft.client.jar=/storage/emulated/0/boat/gamedir/versions/1.7.10/1.7.10.jar", 
             "-Duser.home=null", 
             "-XX:+UnlockExperimentalVMOptions", 
             "-XX:+UseG1GC", 
             "-XX:G1NewSizePercent=20", 
             "-XX:G1ReservePercent=20", 
             "-XX:MaxGCPauseMillis=50", 
             "-XX:G1HeapRegionSize=16M", 
             "-XX:-UseAdaptiveSizePolicy", 
             "-XX:-OmitStackTraceInFastThrow", 
             "-Xmn128m", 
             "-Xmx4096m", 
             "-Dfml.ignoreInvalidMinecraftCertificates=true", 
             "-Dfml.ignorePatchDiscrepancies=true", 
             "-Djava.library.path=" + libraryPath, 
             "-Dminecraft.launcher.brand=HMCL", 
             "-Dminecraft.launcher.version=3.3.163", 
             "-cp", 
             "/storage/emulated/0/boat/gamedir/libraries/com/mojang/netty/1.6/netty-1.6.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/com/mojang/realms/1.3.5/realms-1.3.5.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/org/apache/commons/commons-compress/1.8.1/commons-compress-1.8.1.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/org/apache/httpcomponents/httpclient/4.3.3/httpclient-4.3.3.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/org/apache/httpcomponents/httpcore/4.3.2/httpcore-4.3.2.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/java3d/vecmath/1.3.1/vecmath-1.3.1.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/net/sf/trove4j/trove4j/3.0.3/trove4j-3.0.3.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/com/ibm/icu/icu4j-core-mojang/51.2/icu4j-core-mojang-51.2.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/net/sf/jopt-simple/jopt-simple/4.5/jopt-simple-4.5.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/com/paulscode/codecjorbis/20101023/codecjorbis-20101023.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/com/paulscode/codecwav/20101023/codecwav-20101023.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/com/paulscode/libraryjavasound/20101123/libraryjavasound-20101123.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/com/paulscode/librarylwjglopenal/20100824/librarylwjglopenal-20100824.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/com/paulscode/soundsystem/20120107/soundsystem-20120107.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/io/netty/netty-all/4.0.10.Final/netty-all-4.0.10.Final.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/com/google/guava/guava/15.0/guava-15.0.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/org/apache/commons/commons-lang3/3.1/commons-lang3-3.1.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/commons-io/commons-io/2.4/commons-io-2.4.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/commons-codec/commons-codec/1.9/commons-codec-1.9.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/com/google/code/gson/gson/2.2.4/gson-2.2.4.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/com/mojang/authlib/1.5.21/authlib-1.5.21.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/org/apache/logging/log4j/log4j-api/2.0-beta9/log4j-api-2.0-beta9.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/org/apache/logging/log4j/log4j-core/2.0-beta9/log4j-core-2.0-beta9.jar:" + 
             "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-2/lwjgl.jar:" + 
             "/data/user/0/jackpal.androidterm/app_HOME/boat_runtime/aarch64/lwjgl-2/lwjgl_util.jar:" + 
             "/storage/emulated/0/boat/gamedir/libraries/tv/twitch/twitch/5.16/twitch-5.16.jar:" + 
             "/storage/emulated/0/boat/gamedir/versions/1.7.10/1.7.10.jar",
             "net.minecraft.client.main.Main", 
             "--username", 
             "cosine", 
             "--version", 
             "HMCL 3.3.163", 
             "--gameDir", 
             "/storage/emulated/0/boat/gamedir", 
             "--assetsDir", 
             "/storage/emulated/0/boat/gamedir/assets", 
             "--assetIndex", 
             "1.7.10", 
             "--uuid", 
             "f940db7497ea30aca0f3f57f0ab8bbf3", 
             "--accessToken", 
             "89fc9cbef7264d52b5a33ad7659ee185", 
             "--userProperties", 
             "{}", 
             "--userType", 
             "mojang", 
             "--width", 
             "" + w, 
             "--height", 
             "" + h, 
             };
            */
        } catch (Exception e) {
            e.printStackTrace();
            return 1;
        }
        return 0;
    }
}

