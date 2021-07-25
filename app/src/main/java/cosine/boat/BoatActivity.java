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
import android.view.SurfaceHolder;
import android.view.ViewGroup;
import android.view.TextureView;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.os.SystemClock;
import android.text.InputType;
import android.util.Log;

import com.mojang.minecraftpe.TextInputProxyEditTextbox;

public class BoatActivity extends Activity implements View.OnTouchListener, TextureView.SurfaceTextureListener
{
    private TextInputProxyEditTextbox textInputWidget;
    private boolean textBoxShowing = false;

    public void showKeyboard() {
        BoatActivity.this.textBoxShowing = true;
        BoatActivity.this.textInputWidget.setVisibility(View.VISIBLE);
        BoatActivity.this.mouseCursor.setVisibility(View.INVISIBLE);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)new RelativeLayout.LayoutParams(BoatActivity.this.textInputWidget.getLayoutParams());
        lp.leftMargin = baseX;
        lp.topMargin = baseY;
        BoatActivity.this.textInputWidget.setLayoutParams(lp);

        BoatActivity.this.textInputWidget.postDelayed(new Runnable() {
                public void run() {
                    MotionEvent event = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 0, 0.0f, 0.0f, 0);
                    BoatActivity.this.textInputWidget.dispatchTouchEvent(event);
                    event.recycle();
                    MotionEvent event2 = MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), 1, 0.0f, 0.0f, 0);
                    BoatActivity.this.textInputWidget.dispatchTouchEvent(event2);
                    event2.recycle();
                    BoatActivity.this.textInputWidget.setSelection(BoatActivity.this.textInputWidget.length());
                }
            }, 200);
    }

    public void hideKeyboard() {
        BoatActivity.this.textBoxShowing = false;
        BoatActivity.this.textInputWidget.setVisibility(View.GONE);
        BoatActivity.this.mouseCursor.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        base = (RelativeLayout)this.findViewById(R.id.main_base);
        mainTextureView = (TextureView)this.findViewById(R.id.main_surface);
        mainTextureView.setSurfaceTextureListener(this);
        //mainTextureView.setOpaque(false);
        mainTextureView.setOnTouchListener(this);
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

        textInputWidget = (TextInputProxyEditTextbox)this.findViewById(R.id.text_box);
        this.textInputWidget.setFocusable(true);
        this.textInputWidget.setFocusableInTouchMode(true);
        this.textInputWidget.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        this.textInputWidget.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_ACTION_DONE);

        this.textInputWidget.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    Log.w("mcpe - keyboard", "onEditorAction: " + actionId);
                    //BoatActivity.this.nativeReturnKeyPressed();
                    BoatInput.setKey(BoatKeycodes.KEY_ENTER, '\n', true);
                    BoatInput.setKey(BoatKeycodes.KEY_ENTER, '\n', false);
                    return false;

                }
        });
        this.textInputWidget.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                    //MainActivity.this.nativeSetTextboxText(s.toString());
                    String newText = s.toString();
                    if (newText.length() > 0){
                        for(int i = 0; i < newText.length(); i++){
                            BoatInput.setKey(0, newText.charAt(i), true);
                            BoatInput.setKey(0, newText.charAt(i), false);
                        }

                        BoatActivity.this.textInputWidget.setText("");
                    }
                }
        });
        this.textInputWidget.setOnMCPEKeyWatcher(new TextInputProxyEditTextbox.MCPEKeyWatcher() {
                public void onDeleteKeyPressed() {
                    BoatActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                //BoatActivity.this.nativeBackSpacePressed();
                                BoatInput.setKey(BoatKeycodes.KEY_BACKSPACE, 0, true);
                                BoatInput.setKey(BoatKeycodes.KEY_BACKSPACE, 0, false);
                            }
                        });
                }

                public void onBackKeyPressed() {
                    BoatActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                Log.w("mcpe - keyboard", "textInputWidget.onBackPressed");
                                //MainActivity.this.nativeBackPressed();
                                hideKeyboard();
                            }
                        });
                }
        });
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
    public void onBackPressed() {
        // TODO: Implement this method
    }

    public static native void setBoatNativeWindow(Surface surface);
    static {
        System.loadLibrary("boat");
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        System.out.println("SurfaceTexture is available!");
        BoatActivity.setBoatNativeWindow(new Surface(surface));

        final int w = width;
        final int h = height;

        runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int scale = 1;
                    while(w / (scale + 1) >= 320 && h / (scale + 1) >= 240) {
                        scale++;
                    }
                    RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams)itemBar.getLayoutParams();
                    lp.height = 20 * scale;
                    lp.width = 20 * scale * 9;
                    itemBar.setLayoutParams(lp);
                }
        });

        new Thread() {
            @Override
            public void run() {
                LoadMe.exec(w, h);
                Message msg = new Message();
                msg.what = -1;
                mHandler.sendMessage(msg);
            }
        }.start();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture p1, int p2, int p3) {
        // TODO: Implement this method
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture p1) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture p1) {
        // TODO: Implement this method
    }

    public void setCursorMode(int mode) { 
        Message msg = new Message();
        msg.what = mode;
        mHandler.sendMessage(msg);
    }

    public void setCursorPos(int x, int y) {
        Message msg = new Message();
        msg.what = BoatInput.CursorSetPos;
        msg.arg1 = x;
        msg.arg2 = y;
        mHandler.sendMessage(msg);
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
    private TextureView mainTextureView;
    private Button esc;
    private Button showIME;

    private Button findButton(int id) {
        Button b = (Button)findViewById(id);
        b.setOnTouchListener(this);
        return b;
    }

    public int cursorMode = BoatInput.CursorEnabled;

    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BoatInput.CursorDisabled:
                    hideKeyboard();
                    BoatActivity.this.mouseCursor.setVisibility(View.INVISIBLE);
                    BoatActivity.this.itemBar.setVisibility(View.VISIBLE);
                    BoatActivity.this.cursorMode = BoatInput.CursorDisabled;
                    break;
                
                case BoatInput.CursorEnabled:
                    BoatActivity.this.mouseCursor.setVisibility(View.VISIBLE);
                    BoatActivity.this.itemBar.setVisibility(View.INVISIBLE);
                    BoatActivity.this.cursorMode = BoatInput.CursorEnabled;
                    break;
                case BoatInput.CursorSetPos:
                    BoatActivity.this.mouseCursor.setX((float)msg.arg1);
                    BoatActivity.this.mouseCursor.setY((float)msg.arg2);
                    break;
                default:
                    BoatActivity.this.finish();
                    break;
            }
        }
    }

    private MyHandler mHandler;

    private int initialX;
    private int initialY;
    private int baseX;
    private int baseY;

    @Override
    public boolean onTouch(View p1, MotionEvent p2) {
        if (p1 == showIME) {
            if (p2.getActionMasked() == MotionEvent.ACTION_UP && this.cursorMode == BoatInput.CursorEnabled) {
                this.showKeyboard();
            }
            return false;
        }
        
        if (p1 == mainTextureView) {
            if (textBoxShowing != false) {
                hideKeyboard();
            }
            if (cursorMode == BoatInput.CursorDisabled) {
                switch(p2.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = (int)p2.getX();
                        initialY = (int)p2.getY();
                    case MotionEvent.ACTION_MOVE:
                        BoatInput.setPointer(baseX + (int)p2.getX() -initialX, baseY + (int)p2.getY() - initialY);
                        break;
                    case MotionEvent.ACTION_UP:
                        baseX += ((int)p2.getX() - initialX);
                        baseY += ((int)p2.getY() - initialY);

                        BoatInput.setPointer(baseX, baseY);
                        break;
                    default:
                        break;
                }
            }
            else if (cursorMode == BoatInput.CursorEnabled) {
                baseX = (int)p2.getX();
                baseY = (int)p2.getY();
                BoatInput.setPointer(baseX, baseY);
            }

            mouseCursor.setX(p2.getX());
            mouseCursor.setY(p2.getY());
            return true;
        }
        
        if (p2.getActionMasked() != MotionEvent.ACTION_DOWN && p2.getActionMasked() != MotionEvent.ACTION_UP) {
            return false;
        }
        
        boolean down = p2.getActionMasked() == MotionEvent.ACTION_DOWN;
        if (p1 == mousePrimary) {
            BoatInput.setMouseButton(BoatInput.Button1, down);
            return false;
        }
        if (p1 == mouseSecondary) {
            BoatInput.setMouseButton(BoatInput.Button3, down);
            return false;
        }
        if (p1 == controlUp) {
            BoatInput.setKey(BoatKeycodes.KEY_W, 0, down);
            return false;
        }
        if (p1 == controlDown) {
            BoatInput.setKey(BoatKeycodes.KEY_S, 0, down);
            return false;
        }
        if (p1 == controlLeft) {
            BoatInput.setKey(BoatKeycodes.KEY_A, 0, down);
            return false;
        }
        if (p1 == controlRight) {
            BoatInput.setKey(BoatKeycodes.KEY_D, 0, down);
            return false;
        }
        if (p1 == controlJump) {
            BoatInput.setKey(BoatKeycodes.KEY_SPACE, 0, down);
            return false;
        }
        if (p1 == esc) {
            BoatInput.setKey(BoatKeycodes.KEY_ESC, 0, down);
            return false;
        }
        if (p1 == controlInv) {
            BoatInput.setKey(BoatKeycodes.KEY_E, 0, down);
            return false;
        }
        if (p1 == controlLshift) {
            BoatInput.setKey(BoatKeycodes.KEY_LEFTSHIFT, 0, down);
            return false;
        }
        if (p1 == control1) {
            BoatInput.setKey(BoatKeycodes.KEY_1, 0, down);
            return false;
        }
        if (p1 == control2) {
            BoatInput.setKey(BoatKeycodes.KEY_2, 0, down);
            return false;
        }
        if (p1 == control3) {
            BoatInput.setKey(BoatKeycodes.KEY_3, 0, down);
            return false;
        }
        if (p1 == control4) {
            BoatInput.setKey(BoatKeycodes.KEY_4, 0, down);
            return false;
        }
        if (p1 == control5) {
            BoatInput.setKey(BoatKeycodes.KEY_5, 0, down);
            return false;
        }
        if (p1 == control6) {
            BoatInput.setKey(BoatKeycodes.KEY_6, 0, down);
            return false;
        }
        if (p1 == control7) {
            BoatInput.setKey(BoatKeycodes.KEY_7, 0, down);
            return false;
        }
        if (p1 == control8) {
            BoatInput.setKey(BoatKeycodes.KEY_8, 0, down);
            return false;
        }
        if (p1 == control9) {
            BoatInput.setKey(BoatKeycodes.KEY_9, 0, down);
            return false;
        }
        if (p1 == controlDebug) {    
            BoatInput.setKey(BoatKeycodes.KEY_F3, 0, down);
            return false;
        }
        if (p1 == controlCommand) {
            BoatInput.setKey(BoatKeycodes.KEY_SLASH, 0, down);
            return false;
        }
        if (p1 == control3rd) {
            BoatInput.setKey(BoatKeycodes.KEY_F5, 0, down);
            return false;
        }
        return false; 
    }   
}

