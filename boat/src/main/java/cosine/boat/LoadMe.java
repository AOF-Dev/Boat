package cosine.boat;

public class LoadMe {

    public static native int chdir(String path);
    public static native void redirectStdio(String file);
    public static native void setenv(String name, String value);
    public static native int dlopen(String name);
    public static native void patchLinker();
    public static native int dlexec(String[] args);

    static {
        System.loadLibrary("loadme");
    }
}

