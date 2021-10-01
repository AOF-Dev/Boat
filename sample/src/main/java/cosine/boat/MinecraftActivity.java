package cosine.boat;

import android.view.MotionEvent;
import android.os.Bundle;
import android.app.Activity;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.view.LayoutInflater;
import android.view.Gravity;
import android.view.WindowManager.LayoutParams;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import java.nio.ByteBuffer;
import android.widget.LinearLayout;
import android.os.Handler;
import android.os.Message;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.view.TextureView;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.os.SystemClock;
import android.text.InputType;
import android.util.Log;

import cosine.boat.BoatActivity;
import cosine.boat.BoatTask;
import cosine.boat.BoatLib;
import cosine.boat.BoatKeycodes;

import com.mojang.minecraftpe.TextInputProxyEditTextbox;

public class MinecraftActivity extends BoatActivity
{
    @Override
    public void findViews() {
        setContentView(R.layout.minecraft_layout);
        
        base = (RelativeLayout)this.findViewById(R.id.main_base);
        mainTextureView = this.findViewById(R.id.main_surface);
        textInputWidget = this.findViewById(R.id.text_box);
        mouseCursor = (ImageView)findViewById(R.id.mouse_cursor);
        controlUp = this.findButton(R.id.control_up);
        controlDown = this.findButton(R.id.control_down);
        controlLeft = this.findButton(R.id.control_left);
        controlRight = this.findButton(R.id.control_right);
        controlJump = this.findButton(R.id.control_jump);
        controlInv = this.findButton(R.id.control_inventory);
        controlLshift = this.findButton(R.id.control_lshift);
        control1 = this.findButton(R.id.control_1);
        control2 = this.findButton(R.id.control_2);
        control3 = this.findButton(R.id.control_3);
        control4 = this.findButton(R.id.control_4);
        control5 = this.findButton(R.id.control_5);
        control6 = this.findButton(R.id.control_6);
        control7 = this.findButton(R.id.control_7);
        control8 = this.findButton(R.id.control_8);
        control9 = this.findButton(R.id.control_9);

        itemBar = (LinearLayout)findViewById(R.id.item_bar);
        mousePrimary = this.findButton(R.id.mouse_primary);
        mouseSecondary = this.findButton(R.id.mouse_secondary);
        esc = this.findButton(R.id.esc);
        showIME = this.findButton(R.id.show_ime);
        controlDebug = this.findButton(R.id.control_debug);
        controlCommand = this.findButton(R.id.control_command);
        control3rd = this.findButton(R.id.control_3rd);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new MyHandler();
        base.setSystemUiVisibility(BOAT_SYSTEM_UI_FLAGS);
    }

    public static final int BOAT_SYSTEM_UI_FLAGS =
            View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_FULLSCREEN |
            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

    @Override
    protected void onResume() {
        super.onResume();
        base.setSystemUiVisibility(BOAT_SYSTEM_UI_FLAGS);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int scale = 1;
                    while(width / (scale + 1) >= 320 && height / (scale + 1) >= 240) {
                        scale++;
                    }
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)itemBar.getLayoutParams();
                    lp.height = 20 * scale;
                    lp.width = 20 * scale * 9;
                    itemBar.setLayoutParams(lp);
                }
        });
        super.onSurfaceTextureAvailable(surface, width, height);
    }

    @Override
    public void setCursorViewMode(int mode) { 
        Message msg = new Message();
        msg.what = mode;
        mHandler.sendMessage(msg);
    }

    @Override
    public void setCursorViewPos(float x, float y) {
        mouseCursor.setX(x);
        mouseCursor.setY(y);
    }

    private RelativeLayout base;
    private Button controlUp;
    private Button controlDown;
    private Button controlLeft;
    private Button controlRight;
    private Button controlJump;
    private Button controlInv;
    private Button controlLshift;
    private Button control1;
    private Button control2;
    private Button control3;
    private Button control4;
    private Button control5;
    private Button control6;
    private Button control7;
    private Button control8;
    private Button control9;
    private LinearLayout itemBar;
    private Button mousePrimary;
    private Button mouseSecondary;
    private Button controlDebug;
    private Button controlCommand;
    private Button control3rd;
    private ImageView mouseCursor;
    private Button esc;
    private Button showIME;

    private Button findButton(int id) {
        Button b = (Button)findViewById(id);
        b.setOnTouchListener(this);
        return b;
    }

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BoatLib.CursorDisabled:
                    hideKeyboard();
                    MinecraftActivity.this.mouseCursor.setVisibility(View.INVISIBLE);
                    MinecraftActivity.this.itemBar.setVisibility(View.VISIBLE);
                    MinecraftActivity.this.cursorMode = BoatLib.CursorDisabled;
                    break;
                
                case BoatLib.CursorEnabled:
                    MinecraftActivity.this.mouseCursor.setVisibility(View.VISIBLE);
                    MinecraftActivity.this.itemBar.setVisibility(View.INVISIBLE);
                    MinecraftActivity.this.cursorMode = BoatLib.CursorEnabled;
                    break;
                default:
                    MinecraftActivity.this.finish();
                    break;
            }
        }
    }

    private MyHandler mHandler;

    @Override
    public boolean onTouch(View p1, MotionEvent p2) {
        if (p1 == showIME) {
            if (p2.getActionMasked() == MotionEvent.ACTION_UP && this.cursorMode == BoatLib.CursorEnabled) {
                this.showKeyboard();
            }
            return false;
        }
        
        if (super.onTouch(p1, p2)) {
            return true;
        }
        
        if (p2.getActionMasked() != MotionEvent.ACTION_DOWN && p2.getActionMasked() != MotionEvent.ACTION_UP) {
            return false;
        }
        
        boolean down = p2.getActionMasked() == MotionEvent.ACTION_DOWN;
        if (p1 == mousePrimary) {
            BoatLib.pushEventMouseButton(BoatLib.Button1, down);
            return false;
        }
        if (p1 == mouseSecondary) {
            BoatLib.pushEventMouseButton(BoatLib.Button3, down);
            return false;
        }
        if (p1 == controlUp) {
            BoatLib.pushEventKey(BoatKeycodes.KEY_W, 0, down);
            return false;
        }
        if (p1 == controlDown) {
            BoatLib.pushEventKey(BoatKeycodes.KEY_S, 0, down);
            return false;
        }
        if (p1 == controlLeft) {
            BoatLib.pushEventKey(BoatKeycodes.KEY_A, 0, down);
            return false;
        }
        if (p1 == controlRight) {
            BoatLib.pushEventKey(BoatKeycodes.KEY_D, 0, down);
            return false;
        }
        if (p1 == controlJump) {
            BoatLib.pushEventKey(BoatKeycodes.KEY_SPACE, 0, down);
            return false;
        }
        if (p1 == esc) {
            BoatLib.pushEventKey(BoatKeycodes.KEY_ESC, 0, down);
            return false;
        }
        if (p1 == controlInv) {
            BoatLib.pushEventKey(BoatKeycodes.KEY_E, 0, down);
            return false;
        }
        if (p1 == controlLshift) {
            BoatLib.pushEventKey(BoatKeycodes.KEY_LEFTSHIFT, 0, down);
            return false;
        }
        if (p1 == control1) {
            BoatLib.pushEventKey(BoatKeycodes.KEY_1, 0, down);
            return false;
        }
        if (p1 == control2) {
            BoatLib.pushEventKey(BoatKeycodes.KEY_2, 0, down);
            return false;
        }
        if (p1 == control3) {
            BoatLib.pushEventKey(BoatKeycodes.KEY_3, 0, down);
            return false;
        }
        if (p1 == control4) {
            BoatLib.pushEventKey(BoatKeycodes.KEY_4, 0, down);
            return false;
        }
        if (p1 == control5) {
            BoatLib.pushEventKey(BoatKeycodes.KEY_5, 0, down);
            return false;
        }
        if (p1 == control6) {
            BoatLib.pushEventKey(BoatKeycodes.KEY_6, 0, down);
            return false;
        }
        if (p1 == control7) {
            BoatLib.pushEventKey(BoatKeycodes.KEY_7, 0, down);
            return false;
        }
        if (p1 == control8) {
            BoatLib.pushEventKey(BoatKeycodes.KEY_8, 0, down);
            return false;
        }
        if (p1 == control9) {
            BoatLib.pushEventKey(BoatKeycodes.KEY_9, 0, down);
            return false;
        }
        if (p1 == controlDebug) {    
            BoatLib.pushEventKey(BoatKeycodes.KEY_F3, 0, down);
            return false;
        }
        if (p1 == controlCommand) {
            BoatLib.pushEventKey(BoatKeycodes.KEY_SLASH, 0, down);
            return false;
        }
        if (p1 == control3rd) {
            BoatLib.pushEventKey(BoatKeycodes.KEY_F5, 0, down);
            return false;
        }
        return false;
    }   
}

