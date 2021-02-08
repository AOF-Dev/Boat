package cosine.boat;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation;

public class MyAnimationUtils {

    /**
     * 从控件所在位置移动到控件的底部
     *
     * @return
     */
    public static TranslateAnimation moveToViewBottom() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                                                                  Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                                                                  0.0f, Animation.RELATIVE_TO_SELF, 10.0f);
        mHiddenAction.setDuration(1000);
        return mHiddenAction;
    }

    /**
     * 从控件的底部移动到控件所在位置
     *
     * @return
     */
    public static TranslateAnimation moveToViewLocation() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                                                                  Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                                                                  10.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(1000);
        return mHiddenAction;
    }
}

