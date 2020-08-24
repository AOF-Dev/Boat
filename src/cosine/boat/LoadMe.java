package cosine.boat;

import java.io.IOException;
import java.util.*;
import java.io.*;
import android.widget.Toast;

public class LoadMe {

    public static native int chdir(String str);
    public static native int jliLaunch(String[] strArr);
    public static native void redirectStdio(String file);
    public static native void setenv(String str, String str2);
	public static native void setupJLI();
	public static native int dlopen(String name);

    static {
        System.loadLibrary("boat");
    }

    public static int exec(LauncherConfig config) {
        try {

			MinecraftVersion mcVersion = MinecraftVersion.fromDirectory(new File(config.get("currentVersion")));

			String runtimePath = config.get("runtimePath");


			String home = config.get("home");


			setenv("HOME", home);
			setenv("JAVA_HOME" , runtimePath + "/j2re-image");

            // openjdk
			dlopen(runtimePath + "/j2re-image/lib/aarch32/libfreetype.so.6");
			dlopen(runtimePath + "/j2re-image/lib/aarch32/jli/libjli.so");
			dlopen(runtimePath + "/j2re-image/lib/aarch32/client/libjvm.so");
			dlopen(runtimePath + "/j2re-image/lib/aarch32/libverify.so");
			dlopen(runtimePath + "/j2re-image/lib/aarch32/libjava.so");
			dlopen(runtimePath + "/j2re-image/lib/aarch32/libnet.so");
			dlopen(runtimePath + "/j2re-image/lib/aarch32/libnio.so");
			dlopen(runtimePath + "/j2re-image/lib/aarch32/libawt.so");
			dlopen(runtimePath + "/j2re-image/lib/aarch32/libawt_headless.so");

            //添加版本判断
            boolean isLwjgl3=false;
            if (mcVersion.minimumLauncherVersion >= 21) {
                isLwjgl3 = true;
            }
            String classPath;
            String libraryPath;
            dlopen(runtimePath + "/libopenal.so.1");
            dlopen(runtimePath + "/libGL.so.1");
            //根据版本判断是否启用lwjgl3
            if (!isLwjgl3) {
                libraryPath = runtimePath + "/j2re-image/lib/aarch32/jli:" + runtimePath + "/j2re-image/lib/aarch32:" + runtimePath + "/lwjgl-2:" + runtimePath;

                classPath = config.get("runtimePath") + "/lwjgl-2/lwjgl.jar:" + config.get("runtimePath") + "/lwjgl-2/lwjgl_util.jar:" + mcVersion.getClassPath(config);
                
                dlopen(runtimePath + "/lwjgl-2/liblwjgl.so");
            } else {
                libraryPath = runtimePath + "/j2re-image/lib/aarch32/jli:" + runtimePath + "/j2re-image/lib/aarch32:" + runtimePath + "/lwjgl-3:" + runtimePath;

                classPath = runtimePath + "/lwjgl-3/lwjgl-jemalloc.jar:" + runtimePath + "/lwjgl-3/lwjgl-tinyfd.jar:" + runtimePath + "/lwjgl-3/lwjgl-opengl.jar:" + runtimePath + "/lwjgl-3/lwjgl-openal.jar:" + runtimePath + "/lwjgl-3/lwjgl-glfw.jar:" + runtimePath + "/lwjgl-3/lwjgl-stb.jar:" + runtimePath + "/lwjgl-3/lwjgl.jar:" +  mcVersion.getClassPath(config);
                dlopen(runtimePath + "/libglfw.so");
                dlopen(runtimePath + "/lwjgl-3/liblwjgl.so");
                dlopen(runtimePath + "/lwjgl-3/liblwjgl_stb.so");
                dlopen(runtimePath + "/lwjgl-3/liblwjgl_tinyfd.so");
                dlopen(runtimePath + "/lwjgl-3/liblwjgl_opengl.so");
            }
			setupJLI();	

            redirectStdio(home + "/boat_output.txt");
            chdir(home);


			Vector<String> args = new Vector<String>();


			args.add(runtimePath +  "/j2re-image/bin/java");
			args.add("-cp");
			args.add(classPath);
			args.add("-Djava.library.path=" + libraryPath);

			String extraJavaFlags[] = config.get("extraJavaFlags").split(" ");
			for (String flag : extraJavaFlags) {
				args.add(flag);
			}

			args.add(mcVersion.mainClass);

			String minecraftArgs[]=null;
            if (isLwjgl3) {
                minecraftArgs = mcVersion.getMinecraftArguments(config, true);	
            } else {
                minecraftArgs = mcVersion.getMinecraftArguments(config, false);	
            }

			for (String flag : minecraftArgs) {
				args.add(flag);
			}
            args.add("--width");
            args.add(Integer.toString(BoatApplication.getCurrentActivity().getResources().getDisplayMetrics().widthPixels));
            args.add("--height");
            args.add(Integer.toString(BoatApplication.getCurrentActivity().getResources().getDisplayMetrics().heightPixels));
			String extraMinecraftArgs[] = config.get("extraMinecraftFlags").split(" ");
			for (String flag : extraMinecraftArgs) {
				args.add(flag);
			}

			String finalArgs[] = new String[args.size()];
			for (int i = 0; i < args.size(); i++) {
                if (!args.get(i).equals(" ")) {
                    finalArgs[i] = args.get(i);
                    System.out.println("MC启动参数:" + finalArgs[i]);
                }


			}


            System.out.println("OpenJDK exited with code : " + jliLaunch(finalArgs));

        } catch (Exception e) {
            e.printStackTrace();
			return 1;
        }
		return 0;
    }
}





