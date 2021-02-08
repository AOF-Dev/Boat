package cosine.boat;

import android.app.AlertDialog;
import android.app.NativeActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.CardView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.ant.liao.GifView;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.mio.boat.R;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import android.content.Intent;
import com.mio.boat.MioCustom;
import com.mio.boat.MioUtils;


public class BoatClientActivity_aj extends NativeActivity implements View.OnClickListener, TextWatcher, TextView.OnEditorActionListener
{
    private PopupWindow popupWindow;
    private DrawerLayout base;
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
    private ImageView mouseCursor;

    private EditText inputScanner;
	public boolean mode = false;
    
	private Button crossing,leftup,leftdown,rightup,rightdown;
	private DrawerLayout mDrawerLayout;
	private Button btq;
    private Button btw;
    private Button bte;
    private Button btr;
    private Button btt;
    private Button bty;
    private Button btu;
    private Button bti;
    private Button bto;
    private Button btp;
    private Button bta;
    private Button btns;
    private Button btd;
    private Button btf;
    private Button btg;
    private Button bth;
    private Button btj;
    private Button btk;
    private Button btl;
    private Button btz;
    private Button btx;
    private Button btc;
    private Button btv;
    private Button btb;
    private Button btn;
    private Button btm;
    private Button f1;
    private Button f2;
    private Button f3;
    private Button f4;
    private Button f5;
    private Button f6;
    private Button f7;
    private Button f8;
    private Button f9;
    private Button f10;
    private Button f11;
    private Button f12;
    private Button shift,space,btpr,btse;
    private long 按下_时间;
    private boolean 点击状态=false;
    private boolean 攻击状态=true;
    private boolean 全键_潜行=false;
    private int screenwidth,screenheight;
    private int dx,dy;
    private Handler handler=new Handler();
    private boolean 长按=false;
    
    private boolean 自定义=false;
    private GifView mio;
    
	private RockerView rockerViewLeft;
    private SharedPreferences msh;
    private SharedPreferences.Editor mshe;
	
	private boolean 连点=false;
    
    private boolean 显示=true;
	
	private RelativeLayout crossingLayout,overlay;
    
    private Button touchPad;
    
    
    private SeekBar seekItem;
    
    private MioCustom mioCustom;
    
    //陀螺仪
    Sensor 陀螺仪;
    SensorManager 传感器管理器;
    long 时间;
    boolean 传感器开关=false;
    SensorEventListener 传感器事件;
   
    
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
        base =(DrawerLayout) LayoutInflater.from(BoatClientActivity_aj.this).inflate(R.layout.mio_overlay,null);
        overlay=base.findViewById(R.id.overlayRelativeLayout);
        
        msh=getSharedPreferences("Mio",MODE_PRIVATE);
        mshe=msh.edit();
        
  
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenwidth = dm.widthPixels;
		screenheight = dm.heightPixels;
		popupWindow = new PopupWindow();
		popupWindow.setWidth(LayoutParams.FILL_PARENT);
		popupWindow.setHeight(LayoutParams.FILL_PARENT);
		popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(false);
        if(msh.getBoolean("刘海",false))
        {
            // 延伸显示区域到刘海

            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
            //覆盖刘海屏
            popupWindow.setClippingEnabled(false);
        }
		
		
		mouseCursor = (ImageView)base.findViewById(R.id.mouse_cursor);
		touchPad = base.findViewById(R.id.touch_pad);
        touchPad.setOnTouchListener(ot);
		initCrossing();
        
		control1 = base.findViewById(R.id.control_1);
		control2 = base.findViewById(R.id.control_2);
		control3 = base.findViewById(R.id.control_3);
		control4 = base.findViewById(R.id.control_4);
		control5 = base.findViewById(R.id.control_5);
		control6 = base.findViewById(R.id.control_6);
		control7 = base.findViewById(R.id.control_7);
		control8 = base.findViewById(R.id.control_8);
		control9 = base.findViewById(R.id.control_9);
		
		control1.setOnTouchListener(ot);//(R.id.control_1);
		control2.setOnTouchListener(ot);//(R.id.control_2);
		control3.setOnTouchListener(ot);//(R.id.control_3);
		control4.setOnTouchListener(ot);//(R.id.control_4);
		control5.setOnTouchListener(ot);//(R.id.control_5);
		control6.setOnTouchListener(ot);//(R.id.control_6);
		control7.setOnTouchListener(ot);//(R.id.control_7);
		control8.setOnTouchListener(ot);//(R.id.control_8);
		control9.setOnTouchListener(ot);//(R.id.control_9);
        
		itemBar = (LinearLayout)base.findViewById(R.id.item_bar);
        //全键
        btq=findB(R.id.btq);
        btw=findB(R.id.btw);
        bte=findB(R.id.bte);
        btr=findB(R.id.btr);
        btt=findB(R.id.btt);
        bty=findB(R.id.bty);
        btu=findB(R.id.btu);
        bti=findB(R.id.bti);
        bto=findB(R.id.bto);
        btp=findB(R.id.btp);
        bta=findB(R.id.bta);
        btns=findB(R.id.bts);
        btd=findB(R.id.btd);
        btf=findB(R.id.btf);
        btg=findB(R.id.btg);
        bth=findB(R.id.bth);
        btj=findB(R.id.btj);
        btk=findB(R.id.btk);
        btl=findB(R.id.btl);
        btz=findB(R.id.btz);
        btx=findB(R.id.btx);
        btc=findB(R.id.btc);
        btv=findB(R.id.btv);
        btb=findB(R.id.btb);
        btn=findB(R.id.btn);
        btm=findB(R.id.btm);
        f1=findB(R.id.control_f1);
        f2=findB(R.id.control_f2);
        f3=findB(R.id.control_f3);
        f4=findB(R.id.control_f4);
        f5=findB(R.id.control_f5);
        f6=findB(R.id.control_f6);
        f7=findB(R.id.control_f7);
        f8=findB(R.id.control_f8);
        f9=findB(R.id.control_f9);
        f10=findB(R.id.control_f10);
        f11=findB(R.id.control_f11);
        f12=findB(R.id.control_f12);
        shift=(Button)base.findViewById(R.id.btshift);
//        shift.setOnClickListener(new OnClickListener(){
//                @Override
//                public void onClick(View p1) {
//                    if(全键_潜行)
//                    {
//						((Button)p1).setTextColor(Color.parseColor("#FFF97297"));
//                        BoatInput.setKey(Keyboard.KEY_LSHIFT, true, 0);
//                        全键_潜行=false;
//                    }
//                    else
//                    {
//						((Button)p1).setTextColor(Color.WHITE);
//                        BoatInput.setKey(Keyboard.KEY_LSHIFT, false, 0);
//                        全键_潜行=true;
//                    }
//
//                }
//            });
		mDrawerLayout=base.findViewById(R.id.mDrawerLayout);
        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        mDrawerLayout.setDrawerListener(new DrawerLayout.DrawerListener(){
                @Override
                public void onDrawerSlide(View p1, float p2) {
                }

                @Override
                public void onDrawerOpened(View p1) {
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                }

                @Override
                public void onDrawerClosed(View p1) {
                    mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                }

                @Override
                public void onDrawerStateChanged(int p1) {
                }
            });
        space=findB(R.id.btjump);
        btpr=findB(R.id.btpr);
        btse=findB(R.id.btse);
        
        
		inputScanner = (EditText)base.findViewById(R.id.input_scanner);
		inputScanner.setFocusable(true);
		inputScanner.addTextChangedListener(this);
		inputScanner.setOnEditorActionListener(this);
		inputScanner.setImeOptions(EditorInfo.IME_FLAG_NO_EXTRACT_UI|EditorInfo.IME_FLAG_NO_FULLSCREEN|EditorInfo.IME_ACTION_DONE);
		inputScanner.setSelection(1);
        inputScanner.setOnClickListener(this);
		popupWindow.setContentView(base);
		mHandler = new MyHandler();
		
		
        initMio();
		initCard();
		
		
		
		rockerViewLeft = (RockerView)base.findViewById(R.id.rockerView);
            rockerViewLeft.setCallBackMode(RockerView.CallBackMode.CALL_BACK_MODE_STATE_CHANGE);
            rockerViewLeft.setOnShakeListener(RockerView.DirectionMode.DIRECTION_8, new RockerView.OnShakeListener() {
					@Override
					public void onStart() {

					}

					@Override
					public void direction(RockerView.Direction direction) {
						方向判断(direction);
					}

					@Override
					public void onFinish() {
						BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0, false);
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0, false);
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0, false);
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0, false);
						W=A=S=D=AW=DW=AS=DS=false;
					}
				});
					base.findViewById(R.id.tips).setOnClickListener(new OnClickListener(){
							@Override
							public void onClick(View p1) {
								p1.setVisibility(View.GONE);
							}
						});
		
        seekItem=base.findViewById(R.id.seek_itembar);
        seekItem.setOnTouchListener(new OnTouchListener(){
                @Override
                public boolean onTouch(View p1, MotionEvent p2) {
                    if(自定义){
                        p1.setX( p2.getRawX() - p1.getHeight() / 2);
                        p1.setY( p2.getRawY() - p1.getWidth() / 2);
                       
                    }
                    return false;
                }
            });
         
        if(new File(MioUtils.getExternalFilesDir(BoatClientActivity_aj.this)+"/澪/cursor.png").exists()){
            Bitmap bitmap=BitmapFactory.decodeFile(MioUtils.getExternalFilesDir(this)+"/澪/cursor.png");
            mouseCursor.setImageBitmap(bitmap);
        }
        mioCustom=new MioCustom(this);
        mioCustom.init(overlay,mouseCursor);
        if(!msh.getBoolean("摇杆",false)){
            crossingLayout.setVisibility(View.VISIBLE);
            crossingLayout.setX(msh.getFloat("十字键x",112.0f));
            crossingLayout.setY(msh.getFloat("十字键y",564.4815f));
        }else{
            rockerViewLeft.setVisibility(View.VISIBLE);
        }
        
	}
    private int 灵敏度;
    private void initTLY(){
        时间=0;
        灵敏度=msh.getInt("灵敏度",8);
        if(传感器管理器==null){
            传感器管理器=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
            陀螺仪=传感器管理器.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            传感器事件=new SensorEventListener(){
                @Override
                public void onSensorChanged(SensorEvent event) {
                    //1z  2x  3y
                    if(时间!=0){
                        float 时间差=(event.timestamp-时间)/1000000000.0f;
                        if(mode&&(!(Math.abs(Math.toDegrees(event.values[0]*时间差)*灵敏度)<0.2)||!(Math.abs(Math.toDegrees(event.values[1]*时间差)*灵敏度)<0.2))){
                            baseX+=-Math.toDegrees(event.values[0]*时间差)*灵敏度;
                            baseY+=Math.toDegrees(event.values[1]*时间差)*灵敏度;
                            BoatInput.setPointer(baseX, baseY);
                            mouseCursor.setX(baseX);
                            mouseCursor.setY(baseY);
                        }
                        
                    }
                    时间=event.timestamp;

                }

                @Override
                public void onAccuracyChanged(Sensor p1, int p2) {

                }
            };
        }
        
        传感器管理器.registerListener(传感器事件, 陀螺仪, SensorManager.SENSOR_DELAY_GAME);
    }
    int miox,mioy;
    long mio_down_time;
    int mio_down_x,mio_down_y;
    public void initMio() {
        final RelativeLayout mio_gif_layout=base.findViewById(R.id.mio_gif_layout);
        Button mio_gif_touch=mio_gif_layout.findViewById(R.id.mio_gif_touch);
        mio = mio_gif_layout.findViewById(R.id.mio);
        // 设置Gif图片源
        mio.setGifImage(R.drawable.fbk);     
        // 设置显示的大小，拉伸或者压缩
        mio.setShowDimension(120,150);
        // 设置加载方式：先加载后显示、边加载边显示、只显示第一帧再显示
        mio.setGifImageType(GifView.GifImageType.COVER);
        mio_gif_layout.setX((screenwidth / 2)-(mio.getWidth()/2));
        mio_gif_layout.setY((screenheight / 2)-(mio.getHeight()/2));
        mio_gif_touch.setOnTouchListener(new OnTouchListener(){
                @Override
                public boolean onTouch(View p1, MotionEvent event) {
                    switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            mio_down_x=(int)event.getX();
                            mio_down_y=(int)event.getY();
                            mio_down_time = System.currentTimeMillis();
                            break;
                        case MotionEvent.ACTION_MOVE:
                            miox += event.getX()-(mio.getWidth()/2);
                            mioy += event.getY()-(mio.getHeight()/2);
                            mio_gif_layout.setX(miox);
                            mio_gif_layout.setY(mioy);
                            break;
                        case MotionEvent.ACTION_UP:
                            if (System.currentTimeMillis() - mio_down_time < 250 &&Math.abs(event.getX()-mio_down_x)<100&&Math.abs( event.getY()-mio_down_y)<100) {
                                if (自定义) {
                                    Toast.makeText(BoatClientActivity_aj.this, "自定义关闭", 1000).show();
                                    自定义 = false;
                                    mioCustom.自定义开关();
                                    mioCustom.储存器();
                                    mshe.putFloat("方向x", crossingLayout.getX());
                                    mshe.commit();
                                    mshe.putFloat("方向y", crossingLayout.getY());
                                    mshe.commit();
                                    mshe.putFloat("拖动条x", seekItem.getX());
                                    mshe.commit();
                                    mshe.putFloat("拖动条y", seekItem.getY());
                                    mshe.commit();
                                    seekItem.setVisibility(View.INVISIBLE);
                                    Toast.makeText(BoatClientActivity_aj.this, "配置已保存", 800).show();
                                } else {
                                    mDrawerLayout.openDrawer(Gravity.LEFT);
                                    mDrawerLayout.openDrawer(Gravity.RIGHT);
                                }
                            }
                            break;
                    }
                    return false;
                }
            });
    }
	private boolean isMove=false;
    private boolean isShift=false;
	private void initCrossing(){
        crossingLayout=overlay.findViewById(R.id.crossingKeyboardLayout);
		crossing=overlay.findViewById(R.id.mioKeyboard);
		leftup=overlay.findViewById(R.id.control_leftup);
		leftdown=overlay.findViewById(R.id.control_leftdown);
		rightup=overlay.findViewById(R.id.control_rightup);
		rightdown=overlay.findViewById(R.id.control_rightdown);
		crossing.setOnTouchListener(new View.OnTouchListener(){

				private float ddx;

				private float ddy;


				private int ww;

				private int wh;

				@Override
				public boolean onTouch(View p1, MotionEvent p2)
				{
					if(自定义){
						x += p2.getX() - p1.getWidth() / 2;
						y += p2.getY() - p1.getHeight() / 2;
						crossingLayout.setX(x);
						crossingLayout.setY(y);
					}else{
					if(p2.getAction()==MotionEvent.ACTION_DOWN){
						ddx=p2.getX()-crossing.getX();
						ddy= p2.getY()-crossing.getY();
						ww=crossing.getWidth();
						wh=crossing.getHeight();

						if(ddy<wh/3&&ddx<ww/3){

                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,true);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,true);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);

                            leftup.setAlpha(0);
                            rightup.setAlpha(0);
                            leftdown.setAlpha(0);
                            rightdown.setAlpha(0);
                        }else if(ddy<wh/3&&ddx>ww/3&&ddx<ww*2/3){

                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,true);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);

                            leftup.setAlpha(0.2f);
                            rightup.setAlpha(0.2f);
                            leftdown.setAlpha(0);
                            rightdown.setAlpha(0);

                        }
                        else if(ddy<wh/3&&ddx>ww*2/3){

                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,true);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,true);

                            leftup.setAlpha(0);
                            rightup.setAlpha(0);
                            leftdown.setAlpha(0);
                            rightdown.setAlpha(0);

                        }
                        else if(ddy>wh/3&&ddy<wh*2/3&&ddx<ww/3){

                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,true);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);

                            leftup.setAlpha(0.2f);
                            rightup.setAlpha(0);
                            leftdown.setAlpha(0.2f);
                            rightdown.setAlpha(0);




                        }else if(ddy>wh/3&&ddy<wh*2/3&&ddx>ww/3&&ddx<ww*2/3){

                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);

                            leftup.setAlpha(0);
                            rightup.setAlpha(0);
                            leftdown.setAlpha(0);
                            rightdown.setAlpha(0);
                            
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Shift_L, 0,true);
                            isShift=true;

                        }
                        else if(ddy>wh/3&&ddy<wh*2/3&&ddx>ww*2/3){

                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,true);

                            leftup.setAlpha(0);
                            rightup.setAlpha(0.2f);
                            leftdown.setAlpha(0);
                            rightdown.setAlpha(0.2f);



                        }
                        else if(ddy>wh*2/3&&ddx<ww/3){

                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,true);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,true);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);

                            leftup.setAlpha(0);
                            rightup.setAlpha(0);
                            leftdown.setAlpha(0);
                            rightdown.setAlpha(0);
                        }else if(ddy>wh*2/3&&ddx>ww/3&&ddx<ww*2/3){

                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,true);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);

                            leftup.setAlpha(0);
                            rightup.setAlpha(0);
                            leftdown.setAlpha(0.2f);
                            rightdown.setAlpha(0.2f);
                        }
                        else if(ddy>wh*2/3&&ddx>ww*2/3){

                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,true);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,true);

                            leftup.setAlpha(0);
                            rightup.setAlpha(0);
                            leftdown.setAlpha(0);
                            rightdown.setAlpha(0);
                        }
                    }else if(p2.getAction()==MotionEvent.ACTION_MOVE){

                        float ddxx=p2.getX()-crossing.getX();
                        float ddxy=p2.getY()-crossing.getY();
                        if(ddxy<wh/3&&ddxy>0&&ddxx<ww/3&&ddxx>0){

                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,true);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,true);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);


                        }else if(ddxy<wh/3&&ddxy>0&&ddxx>ww/3&&ddxx<ww*2/3&&ddxx>0){

                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,true);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);
                            isMove=true;
//                          
                            leftup.setAlpha(0.2f);
                            rightup.setAlpha(0.2f);
                            leftdown.setAlpha(0);
                            rightdown.setAlpha(0);
                            



                        }
                        else if(ddxy<wh/3&&ddxy>0&&ddxx>0&&ddxx>ww*2/3){

                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,true);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,true);

                        }
                        else if(ddxy>wh/3&&ddxy<wh*2/3&&ddxx<ww/3){

                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,true);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);

                            leftup.setAlpha(0.2f);
                            rightup.setAlpha(0);
                            leftdown.setAlpha(0.2f);
                            rightdown.setAlpha(0);


                        }else if(ddxy>wh/3&&ddxy<wh*2/3&&ddxx>ww/3&&ddxx<ww*2/3){
                            if(!isMove){
                                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                            }else{
                                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_space, 0, true);
                            }
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);


                        }
                        else if(ddxy>wh/3&&ddxy<wh*2/3&&ddxx>ww*2/3){

                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,true);


                            leftup.setAlpha(0);
                            rightup.setAlpha(0.2f);
                            leftdown.setAlpha(0);
                            rightdown.setAlpha(0.2f);


                        }
                        else if(ddxy>wh*2/3&&ddxx<ww/3){

                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,true);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,true);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);

                        }else if(ddxy>wh*2/3&&ddxx>ww/3&&ddxx<ww*2/3){

                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,true);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);

                            leftup.setAlpha(0);
                            rightup.setAlpha(0);
                            leftdown.setAlpha(0.2f);
                            rightdown.setAlpha(0.2f);


                        }
                        else if(ddxy>wh*2/3&&ddxx>ww*2/3){

                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,true);
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,true);




                        }


                    }else if(p2.getAction()==MotionEvent.ACTION_UP){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);

                        leftup.setAlpha(0);
                        rightup.setAlpha(0);
                        leftdown.setAlpha(0);
                        rightdown.setAlpha(0);
                        if(isMove){
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_space, 0,false);
                            isMove=false;
                        }

                        if(isShift){
                            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Shift_L, 0,false);
                            isShift=false;
                        }


                        

					}
					}
					// TODO: Implement this method
					return false;
				}
			});
	}
//分割线————————————————————————————————————————————————————————————
    
	private boolean A=false,D=false,W=false,S=false,AW=false,DW=false,AS=false,DS=false;
	private void 方向判断(RockerView.Direction direction) {
//        String message = null;
        switch (direction) {
            case DIRECTION_LEFT:
//                message = "左";
                A=true;
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A,0,true);
                if(W){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W,0,false);
                    W=false;
                }
                if(S){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S,0,false);
                    S=false;
                }
                if(D){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D,0,false);
                    D=false;
                }
                if(AW){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W,0,false);
                    AW=false;
                }
                if(DW){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D,0,false);
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W,0,false);
                    DW=false;
                }
                if(AS){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S,0,false);
                    AS=false;
                }
                if(DS){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D,0,false);
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S,0,false);
                    DS=false;
                }
                break;
            case DIRECTION_RIGHT:
//                message = "右";
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D,0,true);
                D=true;
                if(W){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W,0,false);
                    W=false;
                }
                if(A){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A,0,false);
                    A=false;
                }
                if(S){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S,0,false);
                    S=false;
                }
                if(AW){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A,0,false);
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W,0,false);
                    AW=false;
                }
                if(DW){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W,0,false);
                    DW=false;
                }
                if(AS){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A,0,false);
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S,0,false);
                    AS=false;
                }
                if(DS){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S,0,false);
                    DS=false;
                }
                break;
            case DIRECTION_UP:
//                message = "上";
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W,0,true);
                W=true;
                if(A){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A,0,false);
                    A=false;
                }
                if(S){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S,0,false);
                    S=false;
                }
                if(D){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D,0,false);
                    D=false;
                }
                if(AW){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A,0,false);
                    AW=false;
                }
                if(DW){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D,0,false);
                    DW=false;
                }
                if(AS){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A,0,false);
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S,0,false);
                    AS=false;
                }
                if(DS){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D,0,false);
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S,0,false);
                    DS=false;
                }
                break;
            case DIRECTION_DOWN:
//                message = "下";
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S,0,true);
                S=true;
                if(W){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W,0,false);
                    W=false;
                }
                if(A){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A,0,false);
                    A=false;
                }
                if(D){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D,0,false);
                    D=false;
                }
                if(AW){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A,0,false);
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W,0,false);
                    AW=false;
                }
                if(DW){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D,0,false);
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W,0,false);
                    DW=false;
                }
                if(AS){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A,0,false);
                    AS=false;
                }
                if(DS){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D,0,false);
                    DS=false;
                }
                break;
            case DIRECTION_UP_LEFT:
//                message = "左上";
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A,0,true);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W,0,true);
                AW=true;
                if(W){
                    W=false;
                }
                if(A){
                    A=false;
                }
                if(S){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S,0,false);
                    S=false;
                }
                if(D){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D,0,false);
                    D=false;
                }
                if(DW){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D,0,false);
                    DW=false;
                }
                if(AS){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S,0,false);
                    AS=false;
                }
                if(DS){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D,0,false);
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S,0,false);
                    DS=false;
                }
                break;
            case DIRECTION_UP_RIGHT:
//                message = "右上";
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D,0,true);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W,0,true);
                DW=true;
                if(W){
                    W=false;
                }
                if(A){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A,0,false);
                    A=false;
                }
                if(S){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S,0,false);
                    S=false;
                }
                if(D){
                    D=false;
                }
                if(AW){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A,0,false);
                    AW=false;
                }

                if(AS){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A,0,false);
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S,0,false);
                    AS=false;
                }
                if(DS){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S,0,false);
                    DS=false;
                }
                break;
            case DIRECTION_DOWN_LEFT:
//                message = "左下";
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A,0,true);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S,0,true);
                AS=true;
                if(W){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W,0,false);
                    W=false;
                }
                if(A){
                    A=false;
                }
                if(S){
                    S=false;
                }
                if(D){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D,0,false);
                    D=false;
                }
                if(AW){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W,0,false);
                    AW=false;
                }
                if(DW){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D,0,false);
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W,0,false);
                    DW=false;
                }
                if(DS){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D,0,false);
                    DS=false;
                }
                break;
            case DIRECTION_DOWN_RIGHT:
//                message = "右下";
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D,0,true);
                BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S,0,true);
                DS=true;
                if(W){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W,0,false);
                    W=false;
                }
                if(A){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A,0,false);
                    A=false;
                }
                if(S){
                    S=false;
                }
                if(D){
                    D=false;
                }
                if(AW){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A,0,false);
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W,0,false);
                    AW=false;
                }
                if(DW){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W,0,false);
                    DW=false;
                }
                if(AS){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A,0,false);
                    AS=false;
                }
                break;


        }
    }
	private CardView custom,reset,finalReset,exit,userocker;
    private CardView command,commandSetting,attackSwitch,destorySwitch,buttonVisible,liandianqi,tly,quan;
    private boolean 全键=false;
	private void initCard(){
		OnClickListener card_listener=new OnClickListener(){
			@Override
			public void onClick(View p1) {
				if(p1==custom){
					Toast.makeText(BoatClientActivity_aj.this, getStr(R.string.customBtnOpen), 1000).show();
					自定义 = true;
                    //seekItem.setVisibility(View.VISIBLE);
                    mioCustom.自定义开关();
				}else if(p1==reset){
                    mioCustom.reset();
                    crossingLayout.setX(msh.getFloat("十字键x",112.0f));
                    crossingLayout.setY(msh.getFloat("十字键y",564.4815f));
                    mshe.putFloat("十字键x",112.0f);
                    mshe.commit();
                    mshe.putFloat("十字键y",564);
				    mshe.commit();
				}else if(p1==finalReset){
                    mshe.putFloat("十字键x",0);
                    mshe.commit();
                    mshe.putFloat("十字键y",0);
				    mshe.commit();
                    crossingLayout.setX(msh.getFloat("十字键x",0));
                    crossingLayout.setY(msh.getFloat("十字键y",0));
                    mioCustom.finalReset();
                    自定义=true;
                    mioCustom.自定义开关();
				}else if(p1==exit){
                    
					AlertDialog dialog=new AlertDialog.Builder(BoatClientActivity_aj.this)
						.setTitle(getStr(R.string.tip))
						.setMessage(getStr(R.string.sureExit))
						.setPositiveButton(getStr(R.string.ok), new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dia, int which) {
                                
                                BoatClientActivity_aj.this.finish();
							}
						})
						.setNegativeButton(getStr(R.string.cancle), null)
						.create();
					dialog.show();
				}else if(p1==userocker){
					if(msh.getBoolean("摇杆",false)){
						mshe.putBoolean("摇杆",false);
						mshe.commit();
						rockerViewLeft.setVisibility(View.GONE);
						crossingLayout.setVisibility(View.VISIBLE);
					}else{
						mshe.putBoolean("摇杆",true);
						mshe.commit();
						rockerViewLeft.setVisibility(View.VISIBLE);
						crossingLayout.setVisibility(View.INVISIBLE);
					}

				}else if(p1==command){
                    List<String[]> clist=getCommandAndNames();
                    if(clist!=null){
                        final String[] items=clist.get(0);
                        final String[] comms=clist.get(1);
                        AlertDialog dialog=new AlertDialog.Builder(BoatClientActivity_aj.this)
                            .setTitle("快捷命令菜单")
                            .setItems(items, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dia, int which) {
                                    mioCustom.input(comms[which]);
                                }
                            })
                            .setNeutralButton("删除", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface p1, int p2) {
                                    AlertDialog dialog=new AlertDialog.Builder(BoatClientActivity_aj.this)
                                        .setTitle("删除命令")
                                        .setItems(items, new DialogInterface.OnClickListener() {

                                            @Override
                                            public void onClick(DialogInterface dia, int which) {
                                                try {
                                                    JSONObject temp=new JSONObject(msh.getString("命令", ""));
                                                    temp.remove(items[which]);
                                                    mshe.putString("命令", temp.toString());
                                                    mshe.commit();
                                                    Toast.makeText(BoatClientActivity_aj.this,"移除命令:"+items[which],1000).show();
                                                } catch (JSONException e) {}
                                            }
                                        })
                                        .setPositiveButton("关闭菜单", null)
                                        .create();
                                    dialog.show();
                                }
                            })
                            .setPositiveButton("关闭菜单", null)
                            .create();
                        dialog.show();
                    }
                    
                }else if(p1==commandSetting){
                    final LinearLayout add_command=(LinearLayout)LayoutInflater.from(BoatClientActivity_aj.this).inflate(R.layout.alert_add_command,null);
                    new AlertDialog.Builder(BoatClientActivity_aj.this)
                        .setTitle("新增命令")//提示框标题
                        .setView(add_command)
                        .setPositiveButton(getStr(R.string.ok),//提示框的两个按钮
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                String c_name,c_c;
                                c_name=((EditText) add_command.findViewById(R.id.ed_add_command_name)).getText().toString();
                                c_c=((EditText) add_command.findViewById(R.id.ed_add_command)).getText().toString();
                                
                                if(!c_name.equals("")&&!c_c.equals(""))
                                {
                                    try {
                                        mshe.putString("命令", new JSONObject(msh.getString("命令", "")).put(c_name, c_c).toString());
                                        mshe.commit();
                                        Toast.makeText(BoatClientActivity_aj.this,"新增命令:"+c_name,1000).show();
                                    } catch (JSONException e) {}
                                }
                            }
                        }).setNegativeButton(getStr(R.string.cancle),null).create().show();
                }else if(p1==attackSwitch){
                    if(攻击状态){
                        攻击状态=false;
                        Toast.makeText(BoatClientActivity_aj.this,"○✕已切换为攻击模式",500).show();
                    }else{
                        攻击状态=true;
                        Toast.makeText(BoatClientActivity_aj.this,"○✕已切换为挖矿模式",500).show();
                    }
                }else if(p1==destorySwitch){
                    if(点击状态){
                        点击状态=false;
                        Toast.makeText(BoatClientActivity_aj.this,"触屏已切换为放置",500).show();
                    }else{
                        点击状态=true;
                        Toast.makeText(BoatClientActivity_aj.this,"触屏已切换为攻击",500).show();
                    }
                }else if(p1==buttonVisible){
                    if(显示){
                        显示=false;
                        mioCustom.setVisible(View.INVISIBLE);
                        if(msh.getBoolean("摇杆",false)==true)
                        {
                            rockerViewLeft.setVisibility(View.INVISIBLE);
                        }else{
                            crossingLayout.setVisibility(View.INVISIBLE);
                        }
                    }else{
                        显示=true;
                        mioCustom.setVisible(View.VISIBLE);
                        if(msh.getBoolean("摇杆",false)==true)
                        {
                            rockerViewLeft.setVisibility(View.VISIBLE);
                        }else{
                            crossingLayout.setVisibility(View.VISIBLE);
                        }
                    }
                }else if(p1==liandianqi){
//                    if(连点){
//                        连点=false;
//                        搜索按键("○").setTextColor(Color.WHITE);
//                        搜索按键("✕").setTextColor(Color.WHITE);
//                        Toast.makeText(BoatClientActivity_aj.this,"已关闭连点器",1000).show();
//                    }else{
//                        连点=true;
//                        搜索按键("○").setTextColor(Color.RED);
//                        搜索按键("✕").setTextColor(Color.RED);
//                        Toast.makeText(BoatClientActivity_aj.this,"已开启连点器",1000).show();
//                    }
                }else if(p1==tly){
                    if(传感器开关){
                        传感器管理器.unregisterListener(传感器事件);
                        Toast.makeText(BoatClientActivity_aj.this,"陀螺仪已关闭",1000).show();
                        传感器开关=false;
                    }else{
                        final View al_tly=LayoutInflater.from(BoatClientActivity_aj.this).inflate(R.layout.alert_tly,null);
                        AlertDialog dialog=new AlertDialog.Builder(BoatClientActivity_aj.this)
                            .setTitle("陀螺仪")
                            .setView(al_tly)
                            .setPositiveButton("开启", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dia, int which) {
                                    EditText temp=al_tly.findViewById(R.id.ed_tly);
                                    mshe.putInt("灵敏度",Integer.valueOf(temp.getText().toString()));
                                    mshe.commit();
                                    initTLY();
                                    Toast.makeText(BoatClientActivity_aj.this,"陀螺仪已开启",1000).show();
                                    传感器开关=true;
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create();
                        dialog.show();
                        
                    }
                } else if (p1 == quan) {
                    if (!全键) {
                        mioCustom.setVisible(View.INVISIBLE);
                        f1.setVisibility(View.VISIBLE);
                        f2.setVisibility(View.VISIBLE);
                        f3.setVisibility(View.VISIBLE);
                        f4.setVisibility(View.VISIBLE);
                        f5.setVisibility(View.VISIBLE);
                        f6.setVisibility(View.VISIBLE);
                        f7.setVisibility(View.VISIBLE);
                        f8.setVisibility(View.VISIBLE);
                        f9.setVisibility(View.VISIBLE);
                        f10.setVisibility(View.VISIBLE);
                        f11.setVisibility(View.VISIBLE);
                        f12.setVisibility(View.VISIBLE);
                        btq.setVisibility(View.VISIBLE);
                        btw.setVisibility(View.VISIBLE);
                        bte.setVisibility(View.VISIBLE);
                        btr.setVisibility(View.VISIBLE);
                        btt.setVisibility(View.VISIBLE);
                        bty.setVisibility(View.VISIBLE);
                        btu.setVisibility(View.VISIBLE);
                        bti.setVisibility(View.VISIBLE);
                        bto.setVisibility(View.VISIBLE);
                        btp.setVisibility(View.VISIBLE);
                        bta.setVisibility(View.VISIBLE);
                        btns.setVisibility(View.VISIBLE);
                        btd.setVisibility(View.VISIBLE);
                        btf.setVisibility(View.VISIBLE);
                        btg.setVisibility(View.VISIBLE);
                        bth.setVisibility(View.VISIBLE);
                        btj.setVisibility(View.VISIBLE);
                        btk.setVisibility(View.VISIBLE);
                        btl.setVisibility(View.VISIBLE);
                        btz.setVisibility(View.VISIBLE);
                        btx.setVisibility(View.VISIBLE);
                        btc.setVisibility(View.VISIBLE);
                        btv.setVisibility(View.VISIBLE);
                        btb.setVisibility(View.VISIBLE);
                        btn.setVisibility(View.VISIBLE);
                        btm.setVisibility(View.VISIBLE);
                        space.setVisibility(View.VISIBLE);
                        btpr.setVisibility(View.VISIBLE);
                        btse.setVisibility(View.VISIBLE);
                        shift.setVisibility(View.VISIBLE);
                        
                        if (msh.getBoolean("摇杆", false) == true) {
                            rockerViewLeft.setVisibility(View.INVISIBLE);
                        } else {
                            crossingLayout.setVisibility(View.INVISIBLE);
                        }
                        全键=true;
                    } else {
                        mioCustom.setVisible(View.VISIBLE);
                        f1.setVisibility(View.GONE);
                        f2.setVisibility(View.GONE);
                        f3.setVisibility(View.GONE);
                        f4.setVisibility(View.GONE);
                        f5.setVisibility(View.GONE);
                        f6.setVisibility(View.GONE);
                        f7.setVisibility(View.GONE);
                        f8.setVisibility(View.GONE);
                        f9.setVisibility(View.GONE);
                        f10.setVisibility(View.GONE);
                        f11.setVisibility(View.GONE);
                        f12.setVisibility(View.GONE);
                        btq.setVisibility(View.GONE);
                        btw.setVisibility(View.GONE);
                        bte.setVisibility(View.GONE);
                        btr.setVisibility(View.GONE);
                        btt.setVisibility(View.GONE);
                        bty.setVisibility(View.GONE);
                        btu.setVisibility(View.GONE);
                        bti.setVisibility(View.GONE);
                        bto.setVisibility(View.GONE);
                        btp.setVisibility(View.GONE);
                        bta.setVisibility(View.GONE);
                        btns.setVisibility(View.GONE);
                        btd.setVisibility(View.GONE);
                        btf.setVisibility(View.GONE);
                        btg.setVisibility(View.GONE);
                        bth.setVisibility(View.GONE);
                        btj.setVisibility(View.GONE);
                        btk.setVisibility(View.GONE);
                        btl.setVisibility(View.GONE);
                        btz.setVisibility(View.GONE);
                        btx.setVisibility(View.GONE);
                        btc.setVisibility(View.GONE);
                        btv.setVisibility(View.GONE);
                        btb.setVisibility(View.GONE);
                        btn.setVisibility(View.GONE);
                        btm.setVisibility(View.GONE);
                        space.setVisibility(View.GONE);
                        btpr.setVisibility(View.GONE);
                        btse.setVisibility(View.GONE);
                        shift.setVisibility(View.GONE);
                        
                        if (msh.getBoolean("摇杆", false) == true) {
                            rockerViewLeft.setVisibility(View.VISIBLE);
                        } else {
                            crossingLayout.setVisibility(View.VISIBLE);
                        }
                        全键=false;
                    }

                }
                
				mDrawerLayout.closeDrawer(Gravity.LEFT);
                mDrawerLayout.closeDrawer(Gravity.RIGHT);
			}
		};
		
		custom=base.findViewById(R.id.card_customBtn);
		custom.setOnClickListener(card_listener);
		reset=base.findViewById(R.id.card_reset);
		reset.setOnClickListener(card_listener);
		finalReset=base.findViewById(R.id.card_finalReset);
		finalReset.setOnClickListener(card_listener);
		exit=base.findViewById(R.id.card_exit);
		exit.setOnClickListener(card_listener);
		userocker=base.findViewById(R.id.card_rocker);
		userocker.setOnClickListener(card_listener);
        
        command=base.findViewById(R.id.card_command);
        command.setOnClickListener(card_listener);
        commandSetting=base.findViewById(R.id.card_commandSetting);
        commandSetting.setOnClickListener(card_listener);
        attackSwitch=base.findViewById(R.id.card_attackSwitch);
        attackSwitch.setOnClickListener(card_listener);
        destorySwitch=base.findViewById(R.id.card_destorySwitch);
        destorySwitch.setOnClickListener(card_listener);
        buttonVisible=base.findViewById(R.id.card_buttonVisible);
        buttonVisible.setOnClickListener(card_listener);
        liandianqi=base.findViewById(R.id.card_liandianqi);
        liandianqi.setOnClickListener(card_listener);
        tly=base.findViewById(R.id.card_tly);
        tly.setOnClickListener(card_listener);
        quan=base.findViewById(R.id.card_quan);
        quan.setOnClickListener(card_listener);
	}
    float x,y;
    private long 点击时间,按下时间;
    private int cx,cy;
    private int ldx,ldy;
    private float index1_dx,index1_dy;
	private float index2_dx,index2_dy;
	private float index1_dt,index2_dt;
    private int customDX,customDY;
    private long customDT;
    OnTouchListener ot=new OnTouchListener(){
        @Override
        public boolean onTouch(View p1, MotionEvent p2)
        {
            // TODO: Implement this method
            if (自定义)
            {
                if(p2.getAction()==MotionEvent.ACTION_DOWN){
                    customDX=(int)p2.getX();
                    customDY=(int)p2.getY();
                    customDT=System.currentTimeMillis();
                }
                if(p2.getAction()==MotionEvent.ACTION_UP){
                    if(Math.abs(customDX-p2.getX())<50&&Math.abs(customDY-p2.getY())<50&&Math.abs(System.currentTimeMillis()-customDT)<250){
                        String[] items={"普通按键","命令按键"};
                        AlertDialog dialog=new AlertDialog.Builder(BoatClientActivity_aj.this)
                            .setTitle("选择要添加的按键类型")
                            .setItems(items, new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dia, int which) {
                                    if(0==which){
                                        mioCustom.showCustomDialog();
                                    }else if(1==which){
                                        mioCustom.showCustomCmdDialog();
                                    }
                                }
                            })
                            .setNegativeButton("取消", null)
                            .create();
                        dialog.show();
                    }
                }
            }else if(!自定义){
                if (p1 == control1){
                    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_1, 0, true);

                    }
                    else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_1, 0, false);

                    }
                    return false;
                }
                if (p1 == control2){
                    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_2, 0, true);

                    }
                    else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_2, 0, false);

                    }
                    return false;
                }
                if (p1 == control3){
                    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_3, 0, true);

                    }
                    else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_3, 0, false);

                    }
                    return false;
                }
                if (p1 == control4){
                    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_4, 0, true);

                    }
                    else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_4, 0, false);

                    }
                    return false;
                }
                if (p1 == control5){
                    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_5, 0, true);

                    }
                    else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_5, 0, false);

                    }
                    return false;
                }
                if (p1 == control6){
                    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_6, 0, true);

                    }
                    else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_6, 0, false);

                    }
                    return false;
                }
                if (p1 == control7){
                    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_7, 0, true);

                    }
                    else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_7, 0, false);

                    }
                    return false;
                }
                if (p1 == control8){
                    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_8, 0, true);

                    }
                    else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_8, 0, false);

                    }
                    return false;
                }
                if (p1 == control9){
                    if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_9, 0, true);

                    }
                    else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_9, 0, false);

                    }
                    return false;
                }
                // TODO: Implement this method
                if (p1 == touchPad){
                    if (cursorMode == BoatInput.CursorDisabled){
                        switch(p2.getActionMasked()){
                            case MotionEvent.ACTION_DOWN:
                                handler.postDelayed(我跟你讲这个是长按, 700);
//                                传感器管理器.unregisterListener(传感器事件);
                                if(p2.getX()>=screenwidth/2)
                                {
                                    initialX = (int)p2.getX();
                                    initialY = (int)p2.getY();
                                }
                                dx=(int)p2.getX();
                                dy=(int)p2.getY();
                                按下_时间=System.currentTimeMillis();
                                
                                break;
                            case MotionEvent.ACTION_MOVE:
                                if (Math.abs(p2.getX() - dx) >= 50 || Math.abs(p2.getY() - dy) >= 50) {
                                    handler.removeCallbacks(我跟你讲这个是长按);
                                }
                                if(p2.getX()>=screenwidth/2)
                                {
                                    baseX += ((int)p2.getX() -initialX);
                                    baseY += ((int)p2.getY() - initialY);
                                    BoatInput.setPointer(baseX,baseY);
                                    mouseCursor.setX(baseX);
                                    mouseCursor.setY(baseY);
                                    initialX = (int)p2.getX();
                                    initialY = (int)p2.getY();
                                }
                                break;
                            case MotionEvent.ACTION_UP:
//                                initTLY();
                                if (Math.abs(System.currentTimeMillis() - 按下_时间) <= 700) {
                                    handler.removeCallbacks(我跟你讲这个是长按);
                                }
                                if (长按) {
                                    BoatInput.setMouseButton(BoatInput.Button1, false);
                                    长按 = false;
                                }
                                if(p2.getX()>=screenwidth/2)
                                {
                                    baseX += ((int)p2.getX() - initialX);
                                    baseY += ((int)p2.getY() - initialY);
                                    BoatInput.setPointer(baseX, baseY);
                                }
                                if(System.currentTimeMillis()-按下_时间<=250&&Math.abs(p2.getX()-dx)<50&&Math.abs(p2.getY()-dy)<50)
                                {
                                    if(点击状态)
                                    {
                                        BoatInput.setMouseButton(BoatInput.Button1, true);
                                        BoatInput.setMouseButton(BoatInput.Button1, false);
                                    }
                                    else
                                    {
                                        BoatInput.setMouseButton(BoatInput.Button3, true);
                                        BoatInput.setMouseButton(BoatInput.Button3, false);
                                    }
                                }
                                break;
                            case MotionEvent.ACTION_POINTER_DOWN:
                                if(p2.getActionIndex()==1){
                                    index1_dx=p2.getX(1);
                                    index1_dy=p2.getY(1);
                                    index1_dt=System.currentTimeMillis();
                                    seekItem.setVisibility(View.VISIBLE);
                                }
                                if(p2.getActionIndex()==2){
                                    index2_dx=p2.getX(2);
                                    index2_dy=p2.getY(2);
                                    index2_dt=System.currentTimeMillis();
                                }
                                break;
                            case MotionEvent.ACTION_POINTER_UP:
                                    if(p2.getActionIndex()==1)
                                    {
                                        if(System.currentTimeMillis()-index1_dt<=200&&Math.abs(p2.getX(1)-index1_dx)<40&&Math.abs(p2.getY(1)-index1_dy)<40){
                                            BoatInput.setMouseButton(1, true);
                                            BoatInput.setMouseButton(1, false);
                                        }
                                        seekItem.setVisibility(View.INVISIBLE);
                                    }
                                    else if(p2.getActionIndex()==2)
                                    {
                                        if(System.currentTimeMillis()-index2_dt<=200&&Math.abs(p2.getX(2)-index2_dx)<40&&Math.abs(p2.getY(2)-index2_dy)<40){
                                            BoatInput.setMouseButton(3, true);
                                            BoatInput.setMouseButton(3, false);
                                        }
                                    }
                                
                                break;
                        }
//                        if(p2.getActionIndex()==2){
//                            switch(getOrientation(p2.getX(2)-index2_dx,p2.getY(2)-index2_dy)){
//                                case 't':
//                                    if(imanoitem<9){
//                                        imanoitem++;
//                                    }else if(imanoitem==9){
//                                        imanoitem=1;
//                                    }
//                                    setItem(imanoitem);
//                                    break;
//                                case 'b':
//                                    if(imanoitem>1){
//                                        imanoitem--;
//                                    }else if(imanoitem==1){
//                                        imanoitem=9;
//                                    }
//                                    setItem(imanoitem);
//                                    break;
//                            }
//						}
							
                    }
                    else if(cursorMode == BoatInput.CursorEnabled){
                        baseX = (int)p2.getX();
                        baseY = (int)p2.getY();
                        BoatInput.setPointer(baseX, baseY);
                        if (p2.getAction() == MotionEvent.ACTION_DOWN) {
                            handler.postDelayed(我跟你讲这个是长按, 150);
                            cx = (int)p2.getX();
                            cy = (int)p2.getY();
                            点击时间 = System.currentTimeMillis();
                            按下时间 = 点击时间;
                            ldx = cx;
                            ldy = cy;

                        } else if (p2.getAction() == MotionEvent.ACTION_MOVE) {
                            if (Math.abs(p2.getX() - ldx) >= 50 || Math.abs(p2.getY() - ldy) >= 50) {
                                handler.removeCallbacks(我跟你讲这个是长按);
                            }
                        } else if (p2.getAction() == MotionEvent.ACTION_UP) {
                            if (System.currentTimeMillis() - 点击时间 <= 250 && Math.abs(p2.getX() - cx) < 50 && Math.abs(p2.getY() - cy) < 50) {

                                BoatInput.setMouseButton(1, true);
                                new Thread(new Runnable(){
                                        @Override
                                        public void run() {
                                            try {
                                                Thread.sleep(100);
                                                BoatInput.setMouseButton(1, false);
                                            } catch (InterruptedException e) {

                                            }
                                        }
                                    }).start();


                            }
                            if (Math.abs(System.currentTimeMillis() - 按下时间) <= 150) {
                                handler.removeCallbacks(我跟你讲这个是长按);
                            }
                            if (长按) {
                                BoatInput.setMouseButton(1, false);
                                长按 = false;
                            }
//                            mPreviousUpEvent = MotionEvent.obtain(p2);

                        }
                    }
                    mouseCursor.setX(baseX);
                    mouseCursor.setY(baseY);
                    return true;
                }
                
            }
            return false;
        }
    };
//﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉
    
//﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉﹉
	
	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
		popupWindow.dismiss();
	}
	
	


	@Override
	public void surfaceCreated(SurfaceHolder holder)
	{
		// TODO: Implement this method
		
		super.surfaceCreated(holder);
		System.out.println("Surface is created!");
		new Thread(){
			@Override
			public void run(){
				LauncherConfig config = LauncherConfig.fromFile(MioUtils.getStoragePath()+"/boat/config.txt");
				LoadMe.exec(config);		
				Message msg=new Message();
				msg.what = -1;
				mHandler.sendMessage(msg);

			}
		}.start();
		
		
		
		
	}
	
	
	public int cursorMode = BoatInput.CursorEnabled;
    
    private class MyHandler extends Handler{
        @Override
        public void handleMessage(Message msg)
        {

            switch (msg.what)
            {
                case BoatInput.CursorDisabled:
                    mouseCursor.setVisibility(View.INVISIBLE);
                    itemBar.setVisibility(View.VISIBLE);
                    cursorMode = BoatInput.CursorDisabled;
                    break;
                case BoatInput.CursorEnabled:
                    mouseCursor.setVisibility(View.VISIBLE);
                    itemBar.setVisibility(View.INVISIBLE);
                    cursorMode = BoatInput.CursorEnabled;
                    break;
                default:
                    finish();
                    break;
            }
        }
    }
    
	private MyHandler mHandler;
    public void setCursorMode(int mode){

        Message msg=new Message();
        msg.what = mode;
        mHandler.sendMessage(msg);
	}
    private Button findB(int id)
    {
        Button b = (Button)base.findViewById(id);
        b.setOnTouchListener(全键事件);
		return b;
    }
	@Override
	public void onClick(View p1)
	{
		// TODO: Implement this method
		if (p1 == inputScanner){
			inputScanner.setSelection(1);
		}
	}
	
	private int initialX;
	private int initialY;
	public int baseX;
	public int baseY;
	@Override
	public void beforeTextChanged(CharSequence p1, int p2, int p3, int p4)
	{
		// TODO: Implement this method
	}

	@Override
	public void onTextChanged(CharSequence p1, int p2, int p3, int p4)
	{
		// TODO: Implement this method
	}

	@Override
	public void afterTextChanged(Editable p1)
	{
		// TODO: Implement this method
		String newText = p1.toString();
		if (newText.length() < 1){

            BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_BackSpace, 0, true);
			BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_BackSpace, 0, false);
			inputScanner.setText(">");
            inputScanner.setSelection(1);
		}else if (newText.length() > 1){
			for(int i = 1; i < newText.length(); i++){
                BoatInput.setKey(0, newText.charAt(i), true);
				BoatInput.setKey(0, newText.charAt(i), false);
			}
			inputScanner.setText(">");
            inputScanner.setSelection(1);
		}
	}
	
	@Override
	public boolean onEditorAction(TextView p1, int p2, KeyEvent p3)
	{
		// TODO: Implement this method
        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Return, '\n', true);
		BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Return, '\n', false);
        return false;  
	}
    private Runnable 我跟你讲这个是长按=new Runnable(){
        @Override
        public void run() {
            长按=true;
            BoatInput.setMouseButton((byte)1, true);
            
            
        }
    };
	boolean pr_lock=false;
    OnTouchListener 全键事件=new OnTouchListener(){
        @Override
        public boolean onTouch(View p1, MotionEvent p2) {
            if (p1 == btq){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Q, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Q, 0,false);
                }
                return false;
            }
            if (p1 == btw){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_W, 0,false);
                }
                return false;
            }
            if (p1 == bte){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_E, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_E, 0,false);
                }
                return false;
            }
            if (p1 == btr){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_R, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_R, 0,false);
                }
                return false;
            }
            if (p1 == btt){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_T, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_T, 0,false);
                }
                return false;
            }
            if (p1 == bty){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Y, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Y, 0,false);
                }
                return false;
            }
            if (p1 == btu){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_U, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_U, 0,false);
                }
                return false;
            }
            if (p1 == bti){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_I, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_I, 0,false);
                }
                return false;
            }
            if (p1 == bto){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_O, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_O, 0,false);
                }
                return false;
            }
            if (p1 == btp){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_P, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_P, 0,false);
                }
                return false;
            }
            if (p1 == bta){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_A, 0,false);
                }
                return false;
            }
            if (p1 == btns){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_S, 0,false);
                }
                return false;
            }
            if (p1 == btd){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_D, 0,false);
                }
                return false;
            }
            if (p1 == btf){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F, 0,false);
                }
                return false;
            }
            if (p1 == btg){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_G, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_G, 0,false);
                }
                return false;
            }
            if (p1 == bth){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_H, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_H, 0,false);
                }
                return false;
            }
            if (p1 == btj){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_J, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_J, 0,false);
                }
                return false;
            }
            if (p1 == btk){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_K, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_K, 0,false);
                }
                return false;
            }
            if (p1 == btl){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_L, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_L, 0,false);
                }
                return false;
            }
            if (p1 == btz){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Z, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Z, 0,false);
                }
                return false;
            }
            if (p1 == btx){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_X, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_X, 0,false);
                }
                return false;
            }
            if (p1 == btc){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_C, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_C, 0,false);
                }
                return false;
            }
            if (p1 == btv){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_V, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_V, 0,false);
                }
                return false;
            }
            if (p1 == btb){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_B, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_B, 0,false);
                }
                return false;
            }
            if (p1 == btn){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_N, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_N, 0,false);
                }
                return false;
            }
            if (p1 == btm){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_M, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_M, 0,false);
                }
                return false;
            }
            if (p1 == f1){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F1, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F1, 0,false);
                }
                return false;
            }
            if (p1 == f2){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F2, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F2, 0,false);
                }
                return false;
            }
            if (p1 == f3){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F3, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F3, 0,false);
                }
                return false;
            }
            if (p1 == f4){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F4, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F4, 0,false);
                }
                return false;
            }
            if (p1 == f5){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F5, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F5, 0,false);
                }
                return false;
            }
            if (p1 == f6){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F6, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F6, 0,false);
                }
                return false;
            }
            if (p1 == f7){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F7, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F7, 0,false);
                }
                return false;
            }
            if (p1 == f8){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F8, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F8, 0,false);
                }
                return false;
            }
            if (p1 == f9){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F9, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F9, 0,false);
                }
                return false;
            }
            if (p1 == f10){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F10, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F10, 0,false);
                }
                return false;
            }
            if (p1 == f11){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F11, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F11, 0,false);
                }
                return false;
            }
            if (p1 == f12){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F12, 0,true);
                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_F12, 0,false);
                }
                return false;
            }
            if (p1 == space){

                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_space, 0,true);

                }
                else if(p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_space, 0,false);

                }
                return false;
            }
            if (p1 == btpr){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){
                    if(pr_lock){
                        BoatInput.setMouseButton(1, false);
                        pr_lock=false;
                    }else{
                        BoatInput.setMouseButton(1, true);
                        pr_lock=true;
                    }


                }
                return false;

            }
            if (p1 == btse){
                if (p2.getActionMasked() == MotionEvent.ACTION_DOWN){

                    BoatInput.setMouseButton(3, true);

                }
                if (p2.getActionMasked() == MotionEvent.ACTION_UP){
                    BoatInput.setMouseButton(3, false);

                }
                return false;
            }
            return false;
        }
    };
	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		// TODO: Implement this method
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus){
			popupWindow.showAtLocation(BoatClientActivity_aj.this.getWindow().getDecorView(),Gravity.TOP|Gravity.LEFT,0,0);	
//			hideBottomMenu();
		}
		
	}
	
    private List<String[]> getCommandAndNames(){
        try {
            String commands_str=msh.getString("命令","");
            if(commands_str.equals("")){
                mshe.putString("命令","{\"创造\":\"/gamemode 1\",\"生存\":\"/gamemode 0\",\"死亡不掉落\":\"/gamerule keepInventory true\",\"自杀\":\"/kill\",\"清空背包\":\"/clear\",\"设置世界重生点\":\"/setworldspawn\",\"白天\":\"/time set day\",\"夜晚\":\"/time set night\",\"午夜\":\"/time set midnight\",\"获取经验\":\"/xp 1000L\"}");
                mshe.commit();
                commands_str=msh.getString("命令","");
            }else{
                JSONObject commandJson=new JSONObject(commands_str);
                JSONArray commandNames=commandJson.names();
                String[] namess=new String[commandNames.length()];
                String[] commands=new String[namess.length];
                for(int i=0;i<commandNames.length();i++){
                    namess[i]=commandNames.getString(i);
                    commands[i]=commandJson.getString(namess[i]);
                }
                List<String[]> listc=new ArrayList<String[]>();
                listc.add(namess);
                listc.add(commands);
                return listc;
            }
            
        } catch (JSONException e) {
            Toast.makeText(this,e.toString(),1000).show();
        }
        return null;
    }
    private String getStr(int id){
        return getResources().getString(id);
    }
}



