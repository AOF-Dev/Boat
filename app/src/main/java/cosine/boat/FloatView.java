package cosine.boat;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import com.mio.boat.R;

/**
 * Created by cool on 2016/8/30.
 */
public class FloatView {
    
    private Context mContext;
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams wmParams;
    private View mView;
    private boolean isShow = false;//悬浮框是否已经显示


    public FloatView(Context context) {
        mContext = context;
    }
    /**
     * 显示悬浮框
     */
    public void showFloatView() {
        if (isShow)
        {
            return;
        }

        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        wmParams = new WindowManager.LayoutParams();
        wmParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.format = PixelFormat.RGBA_8888;
        wmParams.x = 0;
        wmParams.y = 0;

        wmParams.width = 32;
        wmParams.height = 32;

        mView = LayoutInflater.from(mContext).inflate(R.layout.overlay_js, null);
        mWindowManager.addView(mView, wmParams);



        isShow = true;
    }
    public View getView() {
        return mView;
    }
    public void setP(int x, int y) {
        wmParams.x = x;
        wmParams.y = y;
        mWindowManager.updateViewLayout(mView, wmParams);
    }
    /**
     * 隐藏悬浮窗
     */
    public void hideFloatView() {
        if (mWindowManager != null && isShow)
        {
            mWindowManager.removeView(mView);
            isShow = false;
        }
    }
}

