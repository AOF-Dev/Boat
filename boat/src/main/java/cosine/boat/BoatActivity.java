package cosine.boat;

import android.view.MotionEvent;
import android.os.Bundle;
import android.app.Activity;
import android.os.SystemClock;
import android.widget.RelativeLayout;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.text.TextWatcher;
import android.text.Editable;
import android.view.inputmethod.EditorInfo;
import android.view.KeyEvent;
import android.view.TextureView;
import android.graphics.SurfaceTexture;
import android.view.Surface;
import android.text.InputType;
import java.util.Map;
import java.util.HashMap;

import com.mojang.minecraftpe.TextInputProxyEditTextbox;

public abstract class BoatActivity extends Activity implements View.OnTouchListener, TextureView.SurfaceTextureListener
{
    private static BoatActivity currentActivity = null;

    public static BoatActivity getCurrentInstance() {
        return currentActivity;
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentActivity = this;
    }

    public TextInputProxyEditTextbox textInputWidget;
    public boolean textBoxShowing = false;

    public void showKeyboard() {
        BoatActivity.this.textBoxShowing = true;
        BoatActivity.this.textInputWidget.setVisibility(View.VISIBLE);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) new RelativeLayout.LayoutParams(BoatActivity.this.textInputWidget.getLayoutParams());
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
    }

    public abstract void findViews();

    public TextureView mainTextureView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        findViews();

        this.mainTextureView.setSurfaceTextureListener(this);
        //this.mainTextureView.setOpaque(false);
        this.mainTextureView.setOnTouchListener(this);

        this.textInputWidget.setFocusable(true);
        this.textInputWidget.setFocusableInTouchMode(true);
        this.textInputWidget.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_NORMAL);
        this.textInputWidget.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI | EditorInfo.IME_ACTION_DONE);

        this.textInputWidget.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                    BoatLib.pushEventKey(BoatKeycodes.KEY_ENTER, '\n', true);
                    BoatLib.pushEventKey(BoatKeycodes.KEY_ENTER, '\n', false);
                    return false;

                }
        });
        this.textInputWidget.addTextChangedListener(new TextWatcher() {
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                public void afterTextChanged(Editable s) {
                    String newText = s.toString();
                    if (newText.length() > 0){
                        for(int i = 0; i < newText.length(); i++){
                            BoatLib.pushEventKey(0, newText.charAt(i), true);
                            BoatLib.pushEventKey(0, newText.charAt(i), false);
                        }

                        BoatActivity.this.textInputWidget.setText("");
                    }
                }
        });
        this.textInputWidget.setOnMCPEKeyWatcher(new TextInputProxyEditTextbox.MCPEKeyWatcher() {
                public void onDeleteKeyPressed() {
                    BoatActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                BoatLib.pushEventKey(BoatKeycodes.KEY_BACKSPACE, 0, true);
                                BoatLib.pushEventKey(BoatKeycodes.KEY_BACKSPACE, 0, false);
                            }
                        });
                }

                public void onBackKeyPressed() {
                    BoatActivity.this.runOnUiThread(new Runnable() {
                            public void run() {
                                hideKeyboard();
                            }
                        });
                }
        });
    }

    public int cursorMode = BoatLib.CursorEnabled;

    @Override
    public void onBackPressed() {
        BoatLib.pushEventMessage(BoatLib.CloseRequest);
    }

    private BoatTask task;

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        BoatLib.setBoatNativeWindow(new Surface(surface));
        BoatLib.setEventPipe();

        Map<String, String> envvars = new HashMap<>();
        envvars.put(BoatScript.BOAT_ENV_WINDOW_WIDTH, Integer.toString(width));
        envvars.put(BoatScript.BOAT_ENV_WINDOW_HEIGHT, Integer.toString(height));
        envvars.put(BoatScript.BOAT_ENV_TMPDIR, this.getCacheDir().getAbsolutePath());
        this.task = new BoatTask(envvars, getIntent().getStringExtra(BoatTask.BOAT_TASK_SCRIPT_PATH)) {
            @Override
            public void afterExecute() {
                BoatActivity.this.finish();
            }
        };
        this.task.startTask();
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
        BoatLib.pushEventWindow(width, height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {
        // TODO: Implement this method
    }

    public abstract void setCursorViewPos(float x, float y);
    public abstract void setCursorViewMode(int mode);

    private int initialX;
    private int initialY;
    private int baseX;
    private int baseY;

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (view == mainTextureView) {
            if (textBoxShowing != false) {
                hideKeyboard();
            }
            int currentX = (int)event.getX();
            int currentY = (int)event.getY();
            if (cursorMode == BoatLib.CursorDisabled) {
                switch(event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        initialX = currentX;
                        initialY = currentY;
                    case MotionEvent.ACTION_MOVE:
                        BoatLib.pushEventPointer(baseX + currentX -initialX, baseY + currentY - initialY);
                        break;
                    case MotionEvent.ACTION_UP:
                        baseX += (currentX - initialX);
                        baseY += (currentY - initialY);
                        BoatLib.pushEventPointer(baseX, baseY);
                        break;
                    default:
                        break;
                }
            }
            else if (cursorMode == BoatLib.CursorEnabled) {
                baseX = currentX;
                baseY = currentY;
                BoatLib.pushEventPointer(baseX, baseY);
            }
            setCursorViewPos(event.getX(), event.getY());
            return true;
        }
        return false;
    }
}

