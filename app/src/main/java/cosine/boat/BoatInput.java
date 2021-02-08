package cosine.boat;


import android.app.*;

public class BoatInput{
	
	public static final int KeyPress              = 2;
	public static final int KeyRelease            = 3;
	public static final int ButtonPress           = 4;
	public static final int ButtonRelease	      = 5;
	public static final int MotionNotify          = 6;

	public static final int Button1               = 1;
	public static final int Button2               = 2;
	public static final int Button3               = 3;
	public static final int Button4               = 4;
	public static final int Button5               = 5;
	public static final int Button6               = 6;
	public static final int Button7               = 7;
	
	public static final int CursorEnabled         = 1;
	public static final int CursorDisabled        = 0;
	
	static {
        System.loadLibrary("boat");
    }
	
	public static void setMouseButton(int button, boolean press) {
        send(System.nanoTime(), press ? ButtonPress : ButtonRelease, button, 0);
    }
	public static void setPointer(int x, int y) {
        send(System.nanoTime(), MotionNotify, x, y);
    }
    
	public static void setKey(int keyCode, int keyChar, boolean press){
		send(System.nanoTime(), press ? KeyPress : KeyRelease, keyCode, keyChar);
	}
	
	
	public static native void send(long time, int type, int p1, int p2);
	
	// To be called by lwjgl/glfw.
	public static void setCursorMode(int mode){
		
		Activity activity = MyApplication.getCurrentActivity();
		if (activity instanceof BoatClientActivity_aj){
			BoatClientActivity_aj boatActivity = (BoatClientActivity_aj)activity;
			boatActivity.setCursorMode(mode);
		}
		
	}
    
}
