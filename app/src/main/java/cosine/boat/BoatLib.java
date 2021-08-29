package cosine.boat;

import android.view.Surface;
import android.content.Context;
import android.app.Activity;
import android.content.ClipboardManager;
import android.content.ClipData;

public class BoatLib {

    public static final int KeyPress              = 2;
    public static final int KeyRelease            = 3;
    public static final int ButtonPress           = 4;
    public static final int ButtonRelease         = 5;
    public static final int MotionNotify          = 6;
    public static final int ConfigureNotify       = 22;
    public static final int BoatMessage           = 37;

    public static final int Button1               = 1;
    public static final int Button2               = 2;
    public static final int Button3               = 3;
    public static final int Button4               = 4;
    public static final int Button5               = 5;
    public static final int Button6               = 6;
    public static final int Button7               = 7;

    public static final int CursorEnabled         = 1;
    public static final int CursorDisabled        = 0;

    public static final int ShiftMask             = 1 << 0;
    public static final int LockMask              = 1 << 1;
    public static final int ControlMask           = 1 << 2;
    public static final int Mod1Mask              = 1 << 3;
    public static final int Mod2Mask              = 1 << 4;
    public static final int Mod3Mask              = 1 << 5;
    public static final int Mod4Mask              = 1 << 6;
    public static final int Mod5Mask              = 1 << 7;

    static {
        System.loadLibrary("boat");
    }

    public static native void setBoatNativeWindow(Surface surface);
    public static native void setEventPipe();
    public static native void pushEvent(long time, int type, int p1, int p2);

    public static void pushEventMouseButton(int button, boolean press) {
        BoatLib.pushEvent(System.nanoTime(), press ? ButtonPress : ButtonRelease, button, 0);
    }
    public static void pushEventPointer(int x, int y) {
        BoatLib.pushEvent(System.nanoTime(), MotionNotify, x, y);
    }
    public static void pushEventKey(int keyCode, int keyChar, boolean press) {
        BoatLib.pushEvent(System.nanoTime(), press ? KeyPress : KeyRelease, keyCode, keyChar);
    }
    public static void pushEventWindow(int width, int height) {
        BoatLib.pushEvent(System.nanoTime(), ConfigureNotify, width, height);
    }
    public static void pushEventMessage(int msg) {
        BoatLib.pushEvent(System.nanoTime(), BoatMessage, msg, 0);
    }
 
    // BoatLib callbacks
    public static void setCursorMode(int mode) {
        Activity activity = BoatApplication.getCurrentActivity();
        if (activity instanceof BoatActivity){
            BoatActivity boatActivity = (BoatActivity)activity;
            boatActivity.setCursorMode(mode);
        }
    }

    public static void setPrimaryClipString(String string) {
        Activity activity = BoatApplication.getCurrentActivity();
        if (activity instanceof BoatActivity){
            ClipboardManager clipboard = (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Boat Clipboard", string);
            clipboard.setPrimaryClip(clip);
        }
    }

    public static String getPrimaryClipString() {
        Activity activity = BoatApplication.getCurrentActivity();
        if (activity instanceof BoatActivity){
            ClipboardManager clipboard = (ClipboardManager)activity.getSystemService(Context.CLIPBOARD_SERVICE);
            if (!clipboard.hasPrimaryClip()) {
                return null;
            }
            ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
            return item.getText().toString();
        }
        return null;
    }
}

