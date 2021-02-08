package com.chenzy.owloading;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * 仿守望先锋的loading加载
 * Created by zhangyu on 2016/11/28.
 */

public class OWLoadingView extends SurfaceView {
    private static final String TAG = "OWLoadingView";
    //view的宽度和高度
    private int viewWidth, viewHeight;
    //view的中心点
    private Point center = new Point();
    //六边形的中心点
    private Point[] hexagonCenters = new Point[6];
    //六边形实例
    private Hexagon[] hexagons = new Hexagon[7];
    //六边形之间的间距
    private float space;
    //六边形的半径
    private float hexagonRadius;
    private int color = Color.parseColor("#FFF97297");//默认橙色
    private Paint paint;
    private float sin30 = (float) Math.sin(30f * 2f * Math.PI / 360f);
    private float cos30 = (float) Math.cos(30f * 2f * Math.PI / 360f);
    //进行显示动画和进行隐藏动画的标志常量
    private final int ShowAnimatorFlag = 0x1137, HideAnimatorFlag = 0x1139;
    private int nowAnimatorFlag = ShowAnimatorFlag;
    //触发下一个动画开始的缩放临界点值
    private final float scaleCritical = 0.7f;
    //控制动画运行的标示位
    private boolean runAnim = false;
    private SurfaceHolder surfaceHolder;
    //基准数据是否已初始化
    private boolean baseDataInited = false;
    //是否自动开始动画
    private boolean autoStartAnim = false;

    public OWLoadingView(Context context) {
        super(context);
        init();
    }

    public OWLoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public OWLoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    private void init() {
        surfaceHolder = getHolder();
        setZOrderOnTop(true);
        surfaceHolder.setFormat(PixelFormat.TRANSLUCENT);//背景透明
        initPaint();
    }

    /**
     * 绘制图案
     */
    private void draw() {
        Canvas canvas = surfaceHolder.lockCanvas();
        if(canvas == null)
            return;
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.CLEAR));
        canvas.drawPaint(paint);
        paint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_OVER));

        for (int i = 0; i < 7; i++) {
            hexagons[i].drawHexagon(canvas, paint);
        }
        surfaceHolder.unlockCanvasAndPost(canvas);
    }

    private Runnable animRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                while (runAnim) {
                    Thread.sleep(2);
                    flush();
                    draw();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    //重置六边形数据
    private void resetHexagons() {
        for (int i = 0; i < hexagons.length; i++) {
            hexagons[i].setScale(0);
            hexagons[i].setAlpha(0);
        }
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);
    }

    /**
     * 设置颜色
     *
     * @param color
     */
    public void setColor(int color) {
        this.color = color;
        paint.setColor(color);
    }

    /**
     * 开始动画
     *
     * @return 动画是否启动成功，true 启动成功;false 启动失败
     */
    public boolean startAnim() {
        if (runAnim || !baseDataInited)
            return false;
        runAnim = true;
        new Thread(animRunnable).start();
        setVisibility(View.VISIBLE);
        return true;
    }

    /**
     * 中止动画
     */
    public void stopAnim() {
        runAnim = false;
        nowAnimatorFlag = ShowAnimatorFlag;
        resetHexagons();
        draw();
        setVisibility(View.GONE);
    }

    /**
     * 设置初始化完成后是否自动开始动画。
     * 如果开启，建议在页面onStop()时调用{@link #stopAnim}()方法中止动画，节约内存开销
     * @param autoStartAnim
     */
    public void setAutoStartAnim(boolean autoStartAnim) {
        this.autoStartAnim = autoStartAnim;
    }

    /**
     * 初始化六边形中心店坐标
     */
    private void initHexagonCenters() {
        //1.5*r 加上竖直方向space 等于R*cos30(博客里画的图是不匹配的)
        float bigR = (float) ((1.5 * hexagonRadius + space) / cos30);
        hexagonCenters[0] = new Point(center.x - bigR * sin30, center.y - bigR * cos30);
        hexagonCenters[1] = new Point(center.x + bigR * sin30, center.y - bigR * cos30);
        hexagonCenters[2] = new Point(center.x + bigR, center.y);
        hexagonCenters[3] = new Point(center.x + bigR * sin30, center.y + bigR * cos30);
        hexagonCenters[4] = new Point(center.x - bigR * sin30, center.y + bigR * cos30);
        hexagonCenters[5] = new Point(center.x - bigR, center.y);

        for (int i = 0; i < 6; i++) {
            hexagons[i] = new Hexagon(hexagonCenters[i], hexagonRadius);
        }
        hexagons[6] = new Hexagon(center, hexagonRadius);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        viewHeight = getMeasuredHeight();
        viewWidth = getMeasuredWidth();
        if (viewWidth != 0 && viewHeight != 0) {
            center.x = viewWidth / 2f;
            center.y = viewHeight / 2f;
            float spaceRate = 1 / 100f;
            space = viewWidth <= viewHeight ? viewWidth * spaceRate : viewHeight * spaceRate;
            hexagonRadius = (float) ((viewWidth - 2 * space) / (3 * Math.sqrt(3)));
            initHexagonCenters();
            //圆角处理
            paint.setPathEffect(new CornerPathEffect(hexagonRadius * 0.1f));
            baseDataInited = true;
        }
    }

    /**
     * 刷新数据
     */
    private void flush() {
        if (nowAnimatorFlag == ShowAnimatorFlag) {//逐个显示出来
            hexagons[0].addScale();
            hexagons[0].addAlpha();
            for (int i = 0; i < hexagons.length - 1; i++) {
                if (hexagons[i].getScale() >= scaleCritical) {
                    hexagons[i + 1].addScale();
                    hexagons[i + 1].addAlpha();
                }
            }

            if (hexagons[6].getScale() == 1) {//当最后一个六边形都完全显示时，切换模式，下一轮逐个消失
                nowAnimatorFlag = HideAnimatorFlag;
            }

        } else {//逐个消失
            hexagons[0].subScale();
            hexagons[0].subAlpha();
            for (int i = 0; i < hexagons.length - 1; i++) {
                if (hexagons[i].getScale() <= 1 - scaleCritical) {
                    hexagons[i + 1].subScale();
                    hexagons[i + 1].subAlpha();
                }
            }
            if (hexagons[6].getScale() == 0) {//当最后一个六边形都完全消失时，切换模式，下一轮逐个开始显示
                nowAnimatorFlag = ShowAnimatorFlag;
            }
        }
    }

    /**
     * 六边形
     */
    private class Hexagon {

        //缩放值
        private float scale = 0;
        //透明度
        private int alpha = 0;
        public Point centerPoint;
        public float radius;
        //六个顶点
        private Point[] vertexs = new Point[6];
        //缩放程度每次改变量 变化范围为[0,1]
        private final float scaleChange = 0.07f;
        //透明度每次改变量 变化范围为[0,255]
        private final int alpahChange = 18;

        public Hexagon(Point centerPoint, float radius) {
            this.centerPoint = centerPoint;
            this.radius = radius;
            calculatePointsPosition();
        }

        public void drawHexagon(Canvas canvas, Paint paint) {
            paint.setAlpha(alpha);
            canvas.drawPath(getPath(), paint);
        }

        private int calculatePointsPosition() {
            if (centerPoint == null) {
                return -1;
            }
            //从最上方顺时针数1-6给各顶点标序号 共6个点
            vertexs[0] = new Point(centerPoint.x, centerPoint.y - radius * scale);
            vertexs[1] = new Point(centerPoint.x + radius * cos30 * scale, centerPoint.y - radius * sin30 * scale);
            vertexs[2] = new Point(centerPoint.x + radius * cos30 * scale, centerPoint.y + radius * sin30 * scale);
            vertexs[3] = new Point(centerPoint.x, centerPoint.y + radius * scale);
            vertexs[4] = new Point(centerPoint.x - radius * cos30 * scale, centerPoint.y + radius * sin30 * scale);
            vertexs[5] = new Point(centerPoint.x - radius * cos30 * scale, centerPoint.y - radius * sin30 * scale);
            return 1;
        }


        private Path getPath() {
            Path path = new Path();
            for (int i = 0; i < 6; i++) {
                if (i == 0)
                    path.moveTo(vertexs[i].x, vertexs[i].y);
                else
                    path.lineTo(vertexs[i].x, vertexs[i].y);
            }
            path.close();
            return path;
        }

        /**
         * 设置透明度
         *
         * @param alpha
         */
        public void setAlpha(int alpha) {
            this.alpha = alpha;
        }

        public int getAlpha() {
            return alpha;
        }


        /**
         * 设置缩放比例
         *
         * @param scale
         */
        public void setScale(float scale) {
            this.scale = scale;
            calculatePointsPosition();
        }

        public void addScale() {
            if (scale == 1)
                return;

            scale += scaleChange;
            scale = scale > 1 ? 1 : scale;
            calculatePointsPosition();
        }

        public void subScale() {
            if (scale == 0) {
                return;
            }
            scale -= scaleChange;
            scale = scale < 0 ? 0 : scale;
            calculatePointsPosition();
        }

        public void addAlpha() {
            if (alpha == 255) {
                return;
            }
            alpha += alpahChange;
            alpha = alpha > 255 ? 255 : alpha;
        }

        public void subAlpha() {
            if (alpha == 0) {
                return;
            }
            alpha -= alpahChange;
            alpha = alpha < 0 ? 0 : alpha;
        }

        /**
         * 获取当前缩放比例
         *
         * @return
         */
        public float getScale() {
            return scale;
        }
    }

    private class Point {
        public float x, y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }

        public Point() {
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        Log.d(TAG,"onWindowFocusChanged  "+hasWindowFocus);
        if(hasWindowFocus && autoStartAnim){
            startAnim();
        }
    }

}
