package com.mio.boat;
import android.annotation.Nullable;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;
import com.larswerkman.holocolorpicker.ColorPicker;
import com.larswerkman.holocolorpicker.OpacityBar;
import com.larswerkman.holocolorpicker.SVBar;
import cosine.boat.BoatClientActivity_aj;
import cosine.boat.BoatInput;
import cosine.boat.BoatKeycodes;
import cosine.boat.MyAnimationUtils;
import cosine.boat.Utils;
import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MioCustom implements OnTouchListener,CompoundButton.OnCheckedChangeListener {
    private BoatClientActivity_aj context;
    private JSONObject 配置信息_普通按键,配置信息_特殊按键;
    private RelativeLayout base;
    private CheckBoxGroup checkGroup;
    private Map<String,Integer> map;
    private List<String> 已勾选;
    private List<MioButtonNormal> mioButtonNormals;
    private List<MioButtonSpecial> mioButtonSpecials;
    private ImageView mouseCursor;
    private boolean 自定义=false;
    private String currentColor;
    private String backgroundcolor;
    private String strokecolor;
    private String backgroundcolor2;
    private String strokecolor2;
	private Button keyassignchoose;
    private AlertDialog dialog_custom,dialog_keys;
    private AlertDialog dialog_custom_cmd;
    private EditText ed_add_name_cmd,ed_add_height_cmd,ed_add_width_cmd,ed_add_cmd;
    private SeekBar seekbar_width_cmd,seekbar_height_cmd;
    private Button positivebt_cmd;
    private Button negativebt_cmd;
    private EditText ed_add_name,ed_add_height,ed_add_width,ed_add_size,ed_add_textcolor,ed_add_bgcolor,ed_add_cornerRadius
    ,ed_add_strokewidth,ed_add_strokecolor,ed_add_strokewidth2,ed_add_strokecolor2,ed_add_bgcolor2,ed_add_cornerRadius2;
    private SeekBar seekbar_width,seekbar_height,seekbar_textsize,seekbar_corner,seekbar_corner2,seekbar_strokew,seekbar_strokew2;
    private CheckBox autokeep;
	private CheckBox sjmove;
    private boolean autokeepmode=false;
	private boolean sjmovemode=false;
    private Button positivebt;
    private Button negativebt;
    private int screenwidth,screenheight;
    private List<CheckBox> list_checks;


    public MioCustom(BoatClientActivity_aj context) {
        this.context = context;
    }
    public void init(RelativeLayout base,
                     ImageView mouseCursor) {

        this.base = base;
        checkGroup = new CheckBoxGroup(context);
        map = new HashMap<String,Integer>();
        DisplayMetrics dm =context. getResources().getDisplayMetrics();
        screenwidth = dm.widthPixels;
		screenheight = dm.heightPixels;
        list_checks = new ArrayList<CheckBox>();
        setAllComponentsName(new BoatKeycodes());
        已勾选 = new ArrayList<String>();
        mioButtonNormals = new ArrayList<MioButtonNormal>();
        mioButtonSpecials = new ArrayList<MioButtonSpecial>();
        this.mouseCursor = mouseCursor;
        findCheck();
        解释器();
    }

    @Override
    public void onCheckedChanged(CompoundButton p1, boolean p2) {
        if (p2) {
            已勾选.add(p1.getHint().toString());
        } else {
            已勾选.remove(p1.getHint().toString());
        }
    }

    int dx,dy;
    float x,y;
    private int cx,cy;
    long 点击时间;
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (自定义) {
            try {
                x = event.getRawX() - v.getWidth() ;
                y = event.getRawY() - v.getHeight() ;
                v.setX(x);
                v.setY(y);
                if (v instanceof MioButtonNormal) {
                    JSONObject temp=配置信息_普通按键.getJSONObject(((MioButtonNormal)v).getTAG());
                    配置信息_普通按键.put(((MioButtonNormal)v).getTAG(), temp.put("位置", new JSONArray().put(0, (int)x).put(1, (int)y)));
                } else if (v instanceof MioButtonSpecial) {
                    JSONObject temp=配置信息_特殊按键.getJSONObject(((MioButtonSpecial)v).getTAG());
                    配置信息_特殊按键.put(((MioButtonSpecial)v).getTAG(), temp.put("位置", new JSONArray().put(0, (int)x).put(1, (int)y)));
                }
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    cx = (int)event.getX();
                    cy = (int)event.getY();
                    点击时间 = System.currentTimeMillis();

                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (System.currentTimeMillis() - 点击时间 <= 250 && Math.abs(event.getX() - cx) < 50 && Math.abs(event.getY() - cy) < 50) {
                        final View button=v;
                        final LinearLayout view_change=(LinearLayout)context.getLayoutInflater().inflate(R.layout.alert_change_button, null);
                        new AlertDialog.Builder(context)
                            .setTitle("设置")//提示框标题
                            .setView(view_change)
                            .setPositiveButton("确定",//提示框的两个按钮
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String mheight,mwidth,tsize;
                                    mheight = ((EditText)view_change.findViewById(R.id.ed_change_height)).getText().toString();
                                    mwidth = ((EditText)view_change.findViewById(R.id.ed_change_width)).getText().toString();
                                    tsize = ((EditText)view_change.findViewById(R.id.ed_change_tsize)).getText().toString();
                                    if (mheight.equals("")) {
                                        Toast.makeText(context, "高不能为空", 1000).show();
                                    } else if (mwidth.equals("")) {
                                        Toast.makeText(context, "宽不能为空", 1000).show();
                                    } else if (tsize.equals("")) {
                                        Toast.makeText(context, "文字大小不能为空", 1000).show();
                                    } else {
                                        setViewSize(button, mwidth, mheight);
                                        ((Button)button).setTextSize(Integer.valueOf(tsize));
                                        if (button instanceof MioButtonNormal) {
                                            try {
                                                JSONObject temp= 配置信息_普通按键.getJSONObject(((MioButtonNormal)button).getTAG());
                                                配置信息_普通按键.put(((MioButtonNormal)button).getTAG(), temp.put("大小", new JSONArray().put(0, Integer.valueOf(mheight)).put(1, Integer.valueOf(mwidth))).put("文字大小", Integer.valueOf(tsize)));
                                            } catch (JSONException e) {
                                                Toast.makeText(context, e.toString(), 1000).show();
                                            }
                                        } else if (button instanceof MioButtonSpecial) {
                                            try {
                                                JSONObject temp= 配置信息_特殊按键.getJSONObject(((MioButtonSpecial)button).getTAG());
                                                配置信息_特殊按键.put(((MioButtonNormal)button).getTAG(), temp.put("大小", new JSONArray().put(0, Integer.valueOf(mheight)).put(1, Integer.valueOf(mwidth))).put("文字大小", Integer.valueOf(tsize)));
                                            } catch (JSONException e) {
                                                Toast.makeText(context, e.toString(), 1000).show();
                                            }
                                        }

                                    }
                                }
                            }).setNeutralButton("删除", new DialogInterface.OnClickListener(){
                                @Override
                                public void onClick(DialogInterface p1, int p2) {
                                    base.removeView(button);
                                    if (button instanceof MioButtonNormal) {
                                        mioButtonNormals.remove((MioButtonNormal)button);
                                        配置信息_普通按键.remove(((MioButtonNormal)button).getTAG());
                                    } else if (button instanceof MioButtonSpecial) {
                                        mioButtonSpecials.remove((MioButtonSpecial)button);
                                        配置信息_特殊按键.remove(((MioButtonSpecial)button).getTAG());
                                    }

                                }
                            }).setNegativeButton("取消", null).create().show();
                    }
                }
            } catch (JSONException e) {
                Toast.makeText(context, e.toString(), 1000).show();
            }
        } else if (!自定义) {
            if (v instanceof MioButtonSpecial) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                    beginScale(v,R.anim.zoom_in);
                    MioButtonSpecial miobtn=(MioButtonSpecial)v;
                    if (miobtn.getType() == miobtn.TYPE_CMD) {
                        input(miobtn.getCmd());
                    } else {

                    }
                }else if(event.getAction() == MotionEvent.ACTION_UP){
//                    beginScale(v,R.anim.zoom_out);
                }
            }
            if (v instanceof MioButtonNormal) {
                MioButtonNormal miobtn=(MioButtonNormal)v;
                JSONArray temp=miobtn.getKeys();
                for (int i=0;i < temp.length();i++) {
                    try {
                        String key=temp.getString(i);
                        if (event.getAction() == MotionEvent.ACTION_DOWN) {
//                            beginScale(v,R.anim.zoom_in);
                            dx = (int)event.getX();
                            dy = (int)event.getY();
                            if (key.contains("Mouse")) {
                                BoatInput.setMouseButton(map.get(key), true);
                            } else {
                                BoatInput.setKey(map.get(key), 0, true);
                            }

                        } else if (event.getAction() == MotionEvent.ACTION_UP) {
//                            beginScale(v,R.anim.zoom_out);
                            if (miobtn.isKeep() && !miobtn.isOnKeep()) {
                                miobtn.setOnKeep(true);
                            } else {
                                miobtn.setOnKeep(false);
                                if (key.contains("Mouse")) {
                                    BoatInput.setMouseButton(map.get(key), false);
                                } else {
                                    BoatInput.setKey(map.get(key), 0, false);
                                }
                            }
                        }
                        if (event.getAction() == MotionEvent.ACTION_MOVE && miobtn.isCanMoveScreen()) {
                            context.baseX += ((int)event.getX() - dx);
                            context.baseY += ((int)event.getY() - dy);
                            BoatInput.setPointer(context.baseX, context.baseY);
                            mouseCursor.setX(context.baseX);
                            mouseCursor.setY(context.baseY);
                            dx = (int)event.getX();
                            dy = (int)event.getY();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(context, "按键功能解析错误:" + e.toString(), 500).show();
                    }
                }
            }

        }

        return false;
    }

    public void 自定义开关() {
        if (自定义) {
            自定义 = false;
        } else {
            自定义 = true;
        }
    }
    public void showCustomDialog() {
        dialog_custom.show();
    }
    public void showCustomCmdDialog() {
        dialog_custom_cmd.show();
    }
    public void showCustomViDialog() {

    }
    public void 添加普通按键(
        String timeMillis,
        String name, 
        JSONArray key,
        int textSize, 
        int height, int width, 
        int x, int y,
        String textcolor,
        String backgcolor,
        int cornerR,
        int strokew,
        String strokec,
        String backgcolor2,
        int cornerR2,
        int strokew2,
        String strokec2,
        boolean keep,
        boolean canMove,
        boolean first
    ) {
        MioButtonNormal b=new MioButtonNormal(context);
        base.addView(b);
        setViewSize(b, width + "", height + "");
        b.setX(x);
        b.setY(y);
        b.setTAG(timeMillis);
        b.setName(name);
        b.setText(name);
        b.setKeys(key);
        b.setTextSize(textSize);
        b.setGravity(Gravity.CENTER);
        b.setOnTouchListener(this);
        b.setTextColor(Color.parseColor(textcolor));
        mioButtonNormals.add(b);
        StateListDrawable drawable=new StateListDrawable();
        GradientDrawable gradientDrawable_normal = new GradientDrawable();
        gradientDrawable_normal.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable_normal.setColor(Color.parseColor(backgcolor));
        gradientDrawable_normal.setCornerRadius(cornerR);
        gradientDrawable_normal.setStroke(strokew, Color.parseColor(strokec));
        GradientDrawable gradientDrawable_press = new GradientDrawable();
        gradientDrawable_press.setShape(GradientDrawable.RECTANGLE);
        gradientDrawable_press.setColor(Color.parseColor(backgcolor2));
        gradientDrawable_press.setCornerRadius(cornerR2);
        gradientDrawable_press.setStroke(strokew2, Color.parseColor(strokec2));
        drawable.addState(new int[]{android.R.attr.state_pressed}, gradientDrawable_normal);
        drawable.addState(new int[]{-android.R.attr.state_pressed}, gradientDrawable_press);
        b.setBackground(drawable);
        b.setKeep(keep);
        b.setCanMoveScreen(canMove);
        if (first) {
            b.setVisibility(View.INVISIBLE);
        }
        if (!first) {
            try {
                配置信息_普通按键.put(timeMillis, new JSONObject().put("文本", name).put("键值组", key).put("文字大小", textSize).put("位置", new JSONArray().put(0, x).put(1, y)).put("大小", new JSONArray().put(0, height).put(1, width)).put("文字颜色", textcolor).put("背景颜色", backgcolor).put("圆角半径", cornerR).put("边框宽度", strokew).put("边框颜色", strokec).put("背景颜色2", backgcolor2).put("圆角半径2", cornerR2).put("边框宽度2", strokew2).put("边框颜色2", strokec2).put("自动保持", keep).put("视角移动", canMove));
//                ((ClipboardManager)context.getSystemService(context.CLIPBOARD_SERVICE)).setText(配置信息_普通按键.toString());
            } catch (Exception e) {
                Toast.makeText(context, "普通按键添加出错：" + e.toString(), 1000).show();
            }
        }

    }
    public void 添加特殊按键(String tag,
                       String name,
                       int x, int y,
                       int height, int width, 
                       int type,
                       Object obj) {
        MioButtonSpecial b=new MioButtonSpecial(context);
        base.addView(b);
        setViewSize(b, width + "", height + "");
        b.setX(x);
        b.setY(y);
        b.setTAG(tag);
        b.setName(name);
        b.setTextColor(Color.RED);
        b.setText(name);
        b.setOnTouchListener(this);
        b.setBackgroundResource(R.drawable.control_button);
        mioButtonSpecials.add(b);
        if (type == b.TYPE_CMD) {
            b.setCmd((String)obj);
        } else {
            b.setSelectedTags((JSONArray)obj);
        }
        try {
            配置信息_特殊按键.put(tag, new JSONObject().put("文本", name).put("大小", new JSONArray().put(0, height).put(1, width)).put("位置", new JSONArray().put(0, x).put(1, y)).put("类型", type).put("对象", obj));

        } catch (JSONException e) {
            Toast.makeText(context, "特殊按键添加出错：" + e.toString(), 1000).show();
        }
    }
    public void 解释器() {
        try {
            配置信息_特殊按键 = new JSONObject();
            if (new File(MioUtils.getExternalFilesDir(context), "澪/key_special.txt").exists()) {
                if (MioUtils.readTxt(new File(MioUtils.getExternalFilesDir(context), "澪/key_special.txt").getAbsolutePath()).length() > 5) {
                    配置信息_特殊按键 = new JSONObject(MioUtils.readTxt(new File(MioUtils.getExternalFilesDir(context), "澪/key_special.txt").getAbsolutePath()));
                    JSONArray temp=配置信息_特殊按键.names();
                    for (int i=0;i < temp.length();i++) {
                        JSONObject obj=配置信息_特殊按键.getJSONObject(temp.get(i).toString());
                        JSONArray xy=obj.getJSONArray("位置");
                        JSONArray size=obj.getJSONArray("大小");
                        添加特殊按键(temp.getString(i), obj.getString("文本"), xy.getInt(0), xy.getInt(1), size.getInt(0), size.getInt(1), obj.getInt("类型"), obj.get("对象"));
                    }
                }

            }
            times = 0;
            配置信息_普通按键 = new JSONObject();
            if (new File(MioUtils.getExternalFilesDir(context), "澪/key_normal.txt").exists()) {
                配置信息_普通按键 = new JSONObject(MioUtils.readTxt(new File(MioUtils.getExternalFilesDir(context), "澪/key_normal.txt").getAbsolutePath()));
                JSONArray names=配置信息_普通按键.names();
                for (int i=0;i < names.length();i++) {
                    JSONObject obj=配置信息_普通按键.getJSONObject(names.get(i).toString());
                    JSONArray xy=obj.getJSONArray("位置");
                    JSONArray size=obj.getJSONArray("大小");
                    JSONArray kks=obj.getJSONArray("键值组");
                    添加普通按键(names.getString(i), obj.getString("文本"), kks, obj.getInt("文字大小"), size.get(0), size.get(1), xy.get(0), xy.get(1), obj.getString("文字颜色"), obj.getString("背景颜色"), obj.getInt("圆角半径"), obj.getInt("边框宽度"), obj.getString("边框颜色"), obj.getString("背景颜色2"), obj.getInt("圆角半径2"), obj.getInt("边框宽度2"), obj.getString("边框颜色2"), obj.getBoolean("自动保持"), obj.getBoolean("视角移动"), true);

                }
                h.sendEmptyMessage(1);
                anmtimer = new Timer();
                anmtimer.schedule(new TimerTask(){
                        @Override
                        public void run() {
                            if (times <= mioButtonNormals.size() - 1) {
                                h.sendEmptyMessage(0);
                            } else {
                                h.sendEmptyMessage(2);
                            }
                        }
                    }, 0, 300);
            }

        } catch (JSONException e) {
            Toast.makeText(context, e.toString(), 1000).show();
            if (配置信息_普通按键 == null) {
                配置信息_普通按键 = new JSONObject();
            }

        }
    }
    public void 储存器() {
        MioUtils.writeTxt(配置信息_普通按键.toString(), new File(MioUtils.getExternalFilesDir(context), "澪/key_normal.txt").getAbsolutePath());
        MioUtils.writeTxt(配置信息_特殊按键.toString(), new File(MioUtils.getExternalFilesDir(context), "澪/key_special.txt").getAbsolutePath());
    }
    private Timer anmtimer;
    private int times=0;
    private Handler h=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0 && times <= mioButtonNormals.size() - 1) {
                mioButtonNormals.get(times).setVisibility(View.VISIBLE);
                times++;
            }
//
//            if(msg.what==1){
//                if(msh.getBoolean("摇杆",false)==true)
//                {
////                    rockerViewLeft.setVisibility(View.VISIBLE);
//                }else{
//                    crossingLayout.setVisibility(View.VISIBLE);
//
//
//                }
//            }
            if (msg.what == 2) {
                anmtimer.cancel();
            }
        }};
    public void setViewSize(View view, String width, String height) {  
        ViewGroup.LayoutParams params = view.getLayoutParams();  
        params.width = dip2px(context, Integer.parseInt(width));
        params.height = dip2px(context, Integer.parseInt(height));
        view.setLayoutParams(params);  
    }  
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }
    public MioButtonNormal searchNormalButton(String tag) {
        for (MioButtonNormal nor:mioButtonNormals) {
            if (nor.getTag().equals(tag)) {
                return nor;
            }
        }
        return null;
    }
    private void setAllComponentsName(Object f) {
        map.put("MousePri", BoatInput.Button1);
        map.put("MouseSec", BoatInput.Button3);
        map.put("MouseTer", BoatInput.Button2);
        map.put("MouseGR", BoatInput.Button4);
        map.put("MouseGL", BoatInput.Button5);
        // 获取f对象对应类中的所有属性域
        Field[] fields = f.getClass().getDeclaredFields();
        for (int i = 0 , len = fields.length; i < len; i++) {
            // 对于每个属性，获取属性名
            String varName = fields[i].getName();
            try {
                // 获取原来的访问控制权限
                boolean accessFlag = fields[i].isAccessible();
                // 修改访问控制权限
                fields[i].setAccessible(true);
                // 获取在对象f中属性fields[i]对应的对象中的变量
                Object o = fields[i].get(f);
                if (!varName.contains("BOAT_KEYBOARD_dead")) {
                    map.put(varName.replace("BOAT_KEYBOARD_", ""), ((int)o));
                }
                // 恢复访问控制权限
                fields[i].setAccessible(accessFlag);
            } catch (IllegalArgumentException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            }
        }
        Set<String> set=map.keySet();
        Object[] arr=set.toArray();
        Arrays.sort(arr);
        for (Object key:arr) {
            CheckBox tempview=new CheckBox(context);
            tempview.setHint((String)key);
            tempview.setOnCheckedChangeListener(this);
            checkGroup.addView(tempview);
            list_checks.add(tempview);
        }
//        for (Map.Entry<String, Integer> entry : map.entrySet()) {
//            CheckBox tempview=new CheckBox(context);
//            tempview.setHint(entry.getKey());
//            tempview.setOnCheckedChangeListener(this);
//            checkGroup.addView(tempview);
//            list_checks.add(tempview);
//        }
    }
    public void setVisible(int vi) {
        for (MioButtonNormal b:mioButtonNormals) {
            b.setVisibility(vi);
            if (vi == View.VISIBLE) {
                b.setAnimation(MyAnimationUtils.moveToViewLocation());
            } else {
                b.setAnimation(MyAnimationUtils.moveToViewBottom());
            }
        }
        for (MioButtonSpecial b:mioButtonSpecials) {
            b.setVisibility(vi);
            if (vi == View.VISIBLE) {
                b.setAnimation(MyAnimationUtils.moveToViewLocation());
            } else {
                b.setAnimation(MyAnimationUtils.moveToViewBottom());
            }
        }
    }
    public void reset() {
        for (MioButtonNormal b:mioButtonNormals) {
            base.removeView(b);
        }
        mioButtonNormals.clear();
        File file=new File(MioUtils.getExternalFilesDir(context), "澪/key_normal.txt");
        file.delete();
        Utils.extractAsset(context.getAssets(), "key_normal.txt", file.getAbsolutePath());
        Toast.makeText(context, "已重置。", 1000).show();
        解释器();
    }
    public void finalReset() {
        for(MioButtonNormal b: mioButtonNormals){
            b.setX(screenwidth/2);
            b.setY(screenheight/2);
        }
    }
    public void input(final String str) {
        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_T, 0, true);
        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_T, 0, false);
        new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        Thread.sleep(200);
                        for (char ch:str.toCharArray()) {
                            BoatInput.setKey(0, ch, true);
                            BoatInput.setKey(0, ch, false);
                        }

                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Return, '\n', true);
                        BoatInput.setKey(BoatKeycodes.BOAT_KEYBOARD_Return, '\n', false);
                    } catch (InterruptedException e) {

                    }

                }
            }).start();


    }
    OnSeekBarChangeListener oscl=new OnSeekBarChangeListener(){

        @Override
        public void onStartTrackingTouch(SeekBar p1) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar p1) {
        }

        @Override
        public void onProgressChanged(SeekBar p1, int p2, boolean p3) {
            // TODO: Implement this method
            if (p1 == seekbar_width) {
                ed_add_width.setText(String.valueOf(seekbar_width.getProgress()));
            }
            if (p1 == seekbar_height) {
                ed_add_height.setText(String.valueOf(seekbar_height.getProgress()));
            }
            if (p1 == seekbar_textsize) {
                ed_add_size.setText(String.valueOf(seekbar_textsize.getProgress()));
            }
            if (p1 == seekbar_corner) {
                ed_add_cornerRadius.setText(String.valueOf(seekbar_corner.getProgress()));
            }
            if (p1 == seekbar_corner2) {
                ed_add_cornerRadius2.setText(String.valueOf(seekbar_corner2.getProgress()));
            }
            if (p1 == seekbar_strokew) {
                ed_add_strokewidth.setText(String.valueOf(seekbar_strokew.getProgress()));
            }
            if (p1 == seekbar_strokew2) {
                ed_add_strokewidth2.setText(String.valueOf(seekbar_strokew2.getProgress()));
            }
            if (p1 == seekbar_height_cmd) {
                ed_add_height_cmd.setText(String.valueOf(seekbar_height_cmd.getProgress()));
            }
            if (p1 == seekbar_width_cmd) {
                ed_add_width_cmd.setText(String.valueOf(seekbar_width_cmd.getProgress()));
            }
		}
    };
    private void findCheck() {
        LinearLayout ad=(LinearLayout)LayoutInflater.from(context).inflate(R.layout.alert_add_button, null);

        autokeep = (CheckBox)ad.findViewById(R.id.autokeep);
        sjmove = (CheckBox)ad.findViewById(R.id.sjmove);
        autokeep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton p1, boolean p2) {
                    autokeepmode = p2;
                }
            });
        sjmove.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton p1, boolean p2) {
                    sjmovemode = p2;
                }
            });

        ScrollView scroll=new ScrollView(context);
        LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        scroll.addView(checkGroup, params);
        dialog_custom = new AlertDialog.Builder(context)
            .setView(ad)
            .create();
        dialog_custom.setCanceledOnTouchOutside(false);
        dialog_keys = new AlertDialog.Builder(context).setView(scroll).create();
        keyassignchoose = ad.findViewById(R.id.keyassignchoose);
        keyassignchoose.setOnClickListener(new OnClickListener(){

                @Override
                public void onClick(View p1) {
                    // TODO: Implement this method
                    dialog_keys.show();
                }
            });

        seekbar_width = ad.findViewById(R.id.seekbar_width);
        seekbar_height = ad.findViewById(R.id.seekbar_height);
        seekbar_textsize = ad.findViewById(R.id.seekbar_textsize);
        seekbar_corner = ad.findViewById(R.id.seekbar_corner);
        seekbar_corner2 = ad.findViewById(R.id.seekbar_corner2);
        seekbar_strokew = ad.findViewById(R.id.seekbar_strokew);
        seekbar_strokew2 = ad.findViewById(R.id.seekbar_strokew2);

        seekbar_height.setOnSeekBarChangeListener(oscl);
        seekbar_width.setOnSeekBarChangeListener(oscl);
        seekbar_textsize.setOnSeekBarChangeListener(oscl);
        seekbar_corner.setOnSeekBarChangeListener(oscl);
        seekbar_corner2.setOnSeekBarChangeListener(oscl);
        seekbar_strokew.setOnSeekBarChangeListener(oscl);
        seekbar_strokew2.setOnSeekBarChangeListener(oscl);

        ed_add_height = ad.findViewById(R.id.ed_add_heigth);
        ed_add_width = ad.findViewById(R.id.ed_add_width);
        ed_add_name = ad.findViewById(R.id.ed_add_name);
        ed_add_size = ad.findViewById(R.id.ed_add_size);
        ed_add_textcolor = ad.findViewById(R.id.ed_add_textcolor);
        ed_add_cornerRadius = ad.findViewById(R.id.ed_add_cornerradius);
        ed_add_strokewidth = ad.findViewById(R.id.ed_add_strokewidth);
        ed_add_strokewidth2 = ad.findViewById(R.id.ed_add_strokewidth2);
        ed_add_cornerRadius2 = ad.findViewById(R.id.ed_add_cornerradius2);
        ed_add_textcolor.setOnLongClickListener(new OnLongClickListener(){
                @Override
                public boolean onLongClick(final View p1) {

                    final View v=LayoutInflater.from(context).inflate(R.layout.dialog_colorpicker, null);
                    ColorPicker colorpicker=v.findViewById(R.id.picker);
                    OpacityBar opacityBar =v.findViewById(R.id.opacitybar);
                    SVBar svbar=v.findViewById(R.id.svbar);
                    colorpicker.addOpacityBar(opacityBar);
                    colorpicker.addSVBar(svbar);
                    colorpicker.setShowOldCenterColor(false);
                    currentColor = "#" + Integer.toHexString(colorpicker.getColor());
                    colorpicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener(){
                            @Override
                            public void onColorChanged(int p1) {
                                currentColor = "#" + Integer.toHexString(p1);
                            }
                        });
                    new AlertDialog.Builder(context)
                        .setTitle("颜色选择")//提示框标题
                        .setView(v)
                        .setPositiveButton("确定",//提示框的两个按钮
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //事件
                                ed_add_textcolor.setText(currentColor);
                            }
                        }).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface di, int p2) {
                                // TODO: Implement this method

                            }
                        }).create().show();
                    return false;
                }
            });

        ed_add_bgcolor = ad.findViewById(R.id.ed_add_bgcolor);
        ed_add_bgcolor.setOnLongClickListener(new OnLongClickListener(){
                @Override
                public boolean onLongClick(final View p1) {

                    final View v=LayoutInflater.from(context).inflate(R.layout.dialog_colorpicker, null);
                    ColorPicker colorpicker=v.findViewById(R.id.picker);
                    OpacityBar opacityBar =v.findViewById(R.id.opacitybar);
                    SVBar svbar=v.findViewById(R.id.svbar);
                    colorpicker.addOpacityBar(opacityBar);
                    colorpicker.addSVBar(svbar);
                    colorpicker.setShowOldCenterColor(false);
                    backgroundcolor = "#" + Integer.toHexString(colorpicker.getColor());
                    colorpicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener(){
                            @Override
                            public void onColorChanged(int p1) {
                                backgroundcolor = "#" + Integer.toHexString(p1);
                            }
                        });
                    new AlertDialog.Builder(context)
                        .setTitle("颜色选择")//提示框标题
                        .setView(v)
                        .setPositiveButton("确定",//提示框的两个按钮
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //事件
                                ed_add_bgcolor.setText(backgroundcolor);
                            }
                        }).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface di, int p2) {
                                // TODO: Implement this method

                            }
                        }).create().show();
                    return false;
                }
            });

        ed_add_strokecolor = ad.findViewById(R.id.ed_add_strokecolor);
        ed_add_strokecolor.setOnLongClickListener(new OnLongClickListener(){
                @Override
                public boolean onLongClick(final View p1) {

                    final View v=LayoutInflater.from(context).inflate(R.layout.dialog_colorpicker, null);
                    ColorPicker colorpicker=v.findViewById(R.id.picker);
                    OpacityBar opacityBar =v.findViewById(R.id.opacitybar);
                    SVBar svbar=v.findViewById(R.id.svbar);
                    colorpicker.addOpacityBar(opacityBar);
                    colorpicker.addSVBar(svbar);
                    colorpicker.setShowOldCenterColor(false);
                    strokecolor = "#" + Integer.toHexString(colorpicker.getColor());
                    colorpicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener(){
                            @Override
                            public void onColorChanged(int p1) {
                                strokecolor = "#" + Integer.toHexString(p1);
                            }
                        });
                    new AlertDialog.Builder(context)
                        .setTitle("颜色选择")//提示框标题
                        .setView(v)
                        .setPositiveButton("确定",//提示框的两个按钮
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //事件
                                ed_add_strokecolor.setText(strokecolor);
                            }
                        }).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface di, int p2) {
                                // TODO: Implement this method

                            }
                        }).create().show();
                    return false;
                }
            });

        ed_add_bgcolor2 = ad.findViewById(R.id.ed_add_bgcolor2);
        ed_add_bgcolor2.setOnLongClickListener(new OnLongClickListener(){
                @Override
                public boolean onLongClick(final View p1) {

                    final View v=LayoutInflater.from(context).inflate(R.layout.dialog_colorpicker, null);
                    ColorPicker colorpicker=v.findViewById(R.id.picker);
                    OpacityBar opacityBar =v.findViewById(R.id.opacitybar);
                    SVBar svbar=v.findViewById(R.id.svbar);
                    colorpicker.addOpacityBar(opacityBar);
                    colorpicker.addSVBar(svbar);
                    colorpicker.setShowOldCenterColor(false);
                    backgroundcolor2 = "#" + Integer.toHexString(colorpicker.getColor());
                    colorpicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener(){
                            @Override
                            public void onColorChanged(int p1) {
                                backgroundcolor2 = "#" + Integer.toHexString(p1);
                            }
                        });
                    new AlertDialog.Builder(context)
                        .setTitle("颜色选择")//提示框标题
                        .setView(v)
                        .setPositiveButton("确定",//提示框的两个按钮
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //事件
                                ed_add_bgcolor2.setText(backgroundcolor2);
                            }
                        }).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface di, int p2) {
                                // TODO: Implement this method

                            }
                        }).create().show();
                    return false;
                }
            });

        ed_add_strokecolor2 = ad.findViewById(R.id.ed_add_strokecolor2);
        ed_add_strokecolor2.setOnLongClickListener(new OnLongClickListener(){
                @Override
                public boolean onLongClick(final View p1) {

                    final View v=LayoutInflater.from(context).inflate(R.layout.dialog_colorpicker, null);
                    ColorPicker colorpicker=v.findViewById(R.id.picker);
                    OpacityBar opacityBar =v.findViewById(R.id.opacitybar);
                    SVBar svbar=v.findViewById(R.id.svbar);
                    colorpicker.addOpacityBar(opacityBar);
                    colorpicker.addSVBar(svbar);
                    colorpicker.setShowOldCenterColor(false);
                    strokecolor2 = "#" + Integer.toHexString(colorpicker.getColor());
                    colorpicker.setOnColorChangedListener(new ColorPicker.OnColorChangedListener(){
                            @Override
                            public void onColorChanged(int p1) {
                                strokecolor2 = "#" + Integer.toHexString(p1);
                            }
                        });
                    new AlertDialog.Builder(context)
                        .setTitle("颜色选择")//提示框标题
                        .setView(v)
                        .setPositiveButton("确定",//提示框的两个按钮
                        new android.content.DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //事件
                                ed_add_strokecolor2.setText(strokecolor2);
                            }
                        }).setNegativeButton("取消", new android.content.DialogInterface.OnClickListener(){
                            @Override
                            public void onClick(DialogInterface di, int p2) {
                                // TODO: Implement this method

                            }
                        }).create().show();
                    return false;
                }
            });
        positivebt = ad.findViewById(R.id.positivebt);
		negativebt = ad.findViewById(R.id.negativebt);
        positivebt.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    // TODO: Implement this method
                    String mname,mheight,mwidth,msize,mconerradius,mstrokew,mtcolor,mstrokew2,mcornerradius2,bgcolor,mscolor,mbgcolor2,mscolor2;
                    mname = ed_add_name.getText().toString();
                    mheight = ed_add_height.getText().toString();
                    mwidth = ed_add_width.getText().toString();
                    msize = ed_add_size.getText().toString();
                    mconerradius = ed_add_cornerRadius.getText().toString();
                    mstrokew = ed_add_strokewidth.getText().toString();
                    mcornerradius2 = ed_add_cornerRadius2.getText().toString();
                    mstrokew2 = ed_add_strokewidth2.getText().toString();
                    mtcolor = ed_add_textcolor.getText().toString();
                    bgcolor = ed_add_bgcolor.getText().toString();
                    mscolor = ed_add_strokecolor.getText().toString();
                    mbgcolor2 = ed_add_bgcolor2.getText().toString();
                    mscolor2 = ed_add_strokecolor2.getText().toString();
                    if (已勾选.size() != 0 && !mname.equals("") && !mheight.equals("") && !mwidth.equals("") && !msize.equals("") && !mtcolor.equals("") && !mconerradius.equals("") && !mcornerradius2.equals("") && !mstrokew.equals("") && !mstrokew2.equals("") && !bgcolor.equals("") && !mbgcolor2.equals("") && !mscolor.equals("") && !mscolor2.equals("")) {
                        JSONArray 已选键值=new JSONArray();
                        for (String asd:已勾选) {
                            已选键值.put(asd);
                        }
                        添加普通按键(String.valueOf(System.currentTimeMillis()), mname, 已选键值, Integer.parseInt(msize), Integer.parseInt(mheight), Integer.parseInt(mwidth), screenwidth / 2, screenheight / 2, mtcolor, bgcolor, Integer.parseInt(mconerradius), Integer.parseInt(mstrokew), mscolor, mbgcolor2, Integer.parseInt(mcornerradius2), Integer.parseInt(mstrokew2), mscolor2, autokeepmode, sjmovemode, false);


                        已勾选.clear();
                        for (CheckBox checkbox:list_checks) {
                            checkbox.setChecked(false);
                        }
                        autokeep.setChecked(false);
                        sjmove.setChecked(false);
                        autokeepmode = false;
                        sjmovemode = false;
                        ed_add_name.setText("");
                        dialog_custom.dismiss();
                    } else {
                        Toast.makeText(context, "请将参数填写完整", 1000).show();
                    }
                }
            });
        negativebt.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    // TODO: Implement this method
                    已勾选.clear();
                    ed_add_name.setText("");
                    for (CheckBox checkbox:list_checks) {
                        checkbox.setChecked(false);
                    }
                    autokeep.setChecked(false);
                    sjmove.setChecked(false);
                    autokeepmode = false;
                    sjmovemode = false;
                    dialog_custom.dismiss();
                    储存器();
                }
            });
        LinearLayout ad2=(LinearLayout)LayoutInflater.from(context).inflate(R.layout.alert_add_button_cmd, null);
        ed_add_height_cmd = ad2.findViewById(R.id.ed_add_heigth_cmd);
        ed_add_width_cmd = ad2.findViewById(R.id.ed_add_width_cmd);
        ed_add_name_cmd = ad2.findViewById(R.id.ed_add_name_cmd);
        ed_add_cmd = ad2.findViewById(R.id.ed_add_cmd);
        seekbar_height_cmd = ad2.findViewById(R.id.seekbar_height_cmd);
        seekbar_width_cmd = ad2.findViewById(R.id.seekbar_width_cmd);

        seekbar_height_cmd.setOnSeekBarChangeListener(oscl);
        seekbar_width_cmd.setOnSeekBarChangeListener(oscl);

        positivebt_cmd = ad2.findViewById(R.id.positivebt_cmd);
		negativebt_cmd = ad2.findViewById(R.id.negativebt_cmd);

        positivebt_cmd.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    if (配置信息_特殊按键 == null) {
                        配置信息_特殊按键 = new JSONObject();
                    }
                    String name,height,width,cmd;
                    name = ed_add_name_cmd.getText().toString();
                    height = ed_add_height_cmd.getText().toString();
                    width = ed_add_width_cmd.getText().toString();
                    cmd = ed_add_cmd.getText().toString();
                    if (!name.equals("") && !height.equals("") && !width.equals("") && !cmd.equals("")) {
                        添加特殊按键(String.valueOf(System.currentTimeMillis()), name, screenwidth / 2, screenheight / 2, Integer.valueOf(height), Integer.valueOf(width), 0, cmd);
                        dialog_custom_cmd.dismiss();
                        ed_add_cmd.setText("");

                    }

                }
            });
        negativebt_cmd.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    ed_add_cmd.setText("");
                    dialog_custom_cmd.dismiss();
                    储存器();
                }
            });
        dialog_custom_cmd = new AlertDialog.Builder(context).setView(ad2).create();
    }
    public synchronized void beginScale(View v,int animation) {
        Animation an = AnimationUtils.loadAnimation(context, animation);
        an.setDuration(80);
        an.setFillAfter(true);
        v.startAnimation(an);
    }
    interface MioCustomListener {
        void onStart();
        void onFinish();
    }
    public class MioButtonNormal extends Button {
        private String tag;
        private String name;
        private JSONArray keys;
        private boolean keep=false;
        private boolean canMoveScreen=false;
        private boolean onKeep=false;
        public MioButtonNormal(Context context) {
            super(context);
            keys = new JSONArray();
        }
        
        public MioButtonNormal(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
            keys = new JSONArray();
        }

        public void setOnKeep(boolean onKeep) {
            this.onKeep = onKeep;
        }

        public boolean isOnKeep() {
            return onKeep;
        }

        public void setKeep(boolean keep) {
            this.keep = keep;
        }

        public boolean isKeep() {
            return keep;
        }

        public void setCanMoveScreen(boolean canMoveScreen) {
            this.canMoveScreen = canMoveScreen;
        }

        public boolean isCanMoveScreen() {
            return canMoveScreen;
        }

        public void setTAG(String tag) {
            this.tag = tag;
        }

        public String getTAG() {
            return tag;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setKeys(JSONArray keys) {
            this.keys = keys;
        }

        public JSONArray getKeys() {
            return keys;
        }

    }
    public class MioButtonSpecial extends Button {
        private String tag;
        private String name;
        public int TYPE_CMD=0;
        public int TYPE_VISIBILITY=1;
        private int type;
        private String cmd;
        private JSONArray selectedTags;

        public MioButtonSpecial(Context context) {
            super(context);
        }
        public MioButtonSpecial(Context context, @Nullable AttributeSet attrs) {
            super(context, attrs);
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getType() {
            return type;
        }

        public void setCmd(String cmd) {
            this.cmd = cmd;
        }

        public String getCmd() {
            return cmd;
        }

        public void setSelectedTags(JSONArray selectedTags) {
            this.selectedTags = selectedTags;
        }

        public JSONArray getSelectedTags() {
            return selectedTags;
        }

        public void setTAG(String tag) {
            this.tag = tag;
        }

        public String getTAG() {
            return tag;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }
}
