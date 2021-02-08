package com.mio.boat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.chenzy.owloading.OWLoadingView;
import cosine.boat.BoatClientActivity_aj;
import cosine.boat.MioLogin;
import cosine.boat.MioMod;
import cosine.boat.MioPack;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.widget.RelativeLayout;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import cosine.boat.logcat.Logcat;
import cosine.boat.logcat.LogcatService;
import com.leon.lfilepickerlibrary.LFilePicker;
import android.view.Gravity;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.InputStreamReader;
import android.os.Message;
import android.os.Handler;
import android.content.Context;
import android.text.ClipboardManager;
import cosine.boat.MioDownloader;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.content.FileProvider;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import ru.ivanarh.jndcrash.NDCrashError;
import ru.ivanarh.jndcrash.NDCrash;
import ru.ivanarh.jndcrash.NDCrashUnwinder;
import ru.ivanarh.jndcrash.NDCrashService;
import android.widget.ProgressBar;
import com.wingsofts.guaguale.WaveLoadingView;

public class MioLauncher extends AppCompatActivity implements OnClickListener {
    int version=1;
    
    //jni
    
    private native boolean init(String str,Context context);
    public static native String getText(String filepath);
    static{
        System.loadLibrary("Mio");
    }
    //数据储存
    String ip="49.234.85.55";
    SharedPreferences msh;
    SharedPreferences.Editor mshe;
    boolean isOfficial=false;
    //主界面
    RelativeLayout background;
    //toolbar区域
    Toolbar main_toolbar;
    Button toolbar_button_home,toolbar_button_exit;
    TextView toolbar_text_state;
    //开始游戏
    LinearLayout startGame;
    List<String> startGame_versionsFile;
    ArrayAdapter startGame_arrayAdapter;
    Spinner spinner_choice_version;
    boolean isFirstInitGameSpinner=true;
    TextView versiontext;
    //左侧功能区域
    Button left_btn_user,
    left_btn_version,
    left_btn_gamelist,
    left_btn_gamedir,
    left_btn_setting;
    LinearLayout layout_user,
    layout_version,
    layout_gamelist,
    layout_gamedir,
    layout_setting;
    List<LinearLayout> list_layouts;
    //用户区域
    LinearLayout layout_user_adduser,
    layout_user_refresh;
    ListView layout_user_listview;
    AdapterListUser adaptwr_user;
    List<UserBean> userList;
    //游戏管理区域
    LinearLayout layout_version_modctrl,
                 layout_version_packctrl,
                 layout_version_refresh;
    EditText layout_version_editJvm;
    Button layout_version_save;
    //游戏列表区域
    LinearLayout layout_gamelist_addgame,layout_gamelist_refresh;
    ListView layout_gamelist_listview;
    AdapterListGame adapter_game;
    List<String> gamelist_name;
    List<File> gamelist_file;
    //启动器设置区域
    Button layout_settingButtonMouse,
           layout_settingButtonBackground,
           layout_settingButtonFix,
           layout_settingButtonHelp;
    CheckBox layout_settingCheckBoxFull,
             layout_settingCheckBoxMaK;
    Button layout_settingButtonUpdate;
    //其他
    PopupWindow popupWindow;
    
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        final String logPath = MioUtils.getStoragePath() + "/boat/log.txt";
		Logcat.initializeOutOfProcess(this, logPath, LogcatService.class);
        final String reportPath = "/mnt/sdcard/boat/crash.txt";
        System.out.println("Crash report: " + reportPath);
        final NDCrashError error = NDCrash.initializeOutOfProcess( this, reportPath, NDCrashUnwinder.libcorkscrew, NDCrashService.class);
        if (error == NDCrashError.ok) {
            System.out.println("NDCrash: OK");
            // Initialization is successful. 
        } else {
            System.out.println("NDCrash: Error");
            System.out.println(error.name());
            // Initialization failed, check error value. 
		} 
        setContentView(R.layout.activity_main);
        

        //初始化数据储存
        msh = getSharedPreferences("Mio", MODE_PRIVATE);
        mshe = msh.edit();
        //初始化UI
        background=findViewById(R.id.activity_mainRelativeLayout);
        if (new File(MioUtils.getExternalFilesDir(this) + "/澪/bg.png").exists()) {
            Bitmap bitmap=BitmapFactory.decodeFile(MioUtils.getExternalFilesDir(this) + "/澪/bg.png");
            BitmapDrawable bd=new BitmapDrawable(bitmap);
            background.setBackground(bd);
        }
 
        try{
            if(!init(SignatureUtils.getSignatureHashCode(this)+"",this)){
//            System.exit(0);  /*可有可无*/
            }else{
                //此处不会执行，只是为了装个样子，当第一步判断时必定会抛出异常
                AlertDialog dialog=new AlertDialog.Builder(this)
                    .setTitle("标题")
                    .setMessage("应用无异常")
                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dia, int which) {

                        }
                    })
                    .setNegativeButton("取消", null)
                    .create();
                dialog.show();
            }
        }catch(Exception e){

        } 
        initUI();
        initLoadingView();
        
    }
    private boolean isNetworkConnected(Context context){
        //判断上下文是不是空的
        //为啥要判断啊? 防止context是空的导致 报空指针异常
        if (context!=null){
            //获取连接管理器
            ConnectivityManager mConnectivityManager= (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            //获取网络状态mConnectivityManager.getActiveNetworkInfo();
            NetworkInfo mNnetNetworkInfo=mConnectivityManager.getActiveNetworkInfo();
            if (mNnetNetworkInfo!=null){
                //判断网络是否可用//如果可以用就是 true  不可用就是false
                return mNnetNetworkInfo.isAvailable();
            }
        }
        return false;
    }
    private Handler 公告=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            View tempview=LayoutInflater.from(MioLauncher.this).inflate(R.layout.popup_tip,null);
            final PopupWindow popup=new PopupWindow(MioLauncher.this);
            popup.setWidth(LayoutParams.FILL_PARENT);
            popup.setHeight(LayoutParams.FILL_PARENT);
            popup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popup.setFocusable(true);
            popup.setOutsideTouchable(false);
            popup.setContentView(tempview);
            TextView temptext=tempview.findViewById(R.id.popup_tipTextView);
            temptext.setText(""+(String)msg.obj);
            ImageButton tempbtn=tempview.findViewById(R.id.popup_tipImageButton);
            tempbtn.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View p1) {
                        popup.dismiss();
                    }
                });
            popup.showAtLocation(MioLauncher.this.getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, 0, 0);
        }};
private boolean flag_first=true;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(flag_first){
            flag_first=false;
            if(isNetworkConnected(this))
                new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                HttpURLConnection con=(HttpURLConnection)new URL("http://"+ip+"/Mio/tips.txt").openConnection();
                                con.setConnectTimeout(5000);
                                InputStream in = con.getInputStream();
                                BufferedReader bfr=new BufferedReader(new InputStreamReader(in));
                                String temp=null;
                                String str="";
                                while ((temp = bfr.readLine()) != null) {
                                    str += temp + "\n";
                                }
                                bfr.close();
                                in.close();
                                con.disconnect();
                                Message msg=new Message();
                                msg.obj = ""+str;
                                公告.sendMessage(msg);
                            } catch (IOException e) {
                                Message msg=new Message();
                                msg.obj = "公告获取出错:" + e.toString();
                                公告.sendMessage(msg);
                            }
                        }
                    }).start();
        }
        
    }
    
    private void initUI() {
        //toolbar区域
        main_toolbar = findViewById(R.id.main_toolbar);
        main_toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(main_toolbar);
        toolbar_button_home = findButton(R.id.toolbar_button_home);
        toolbar_button_exit = findButton(R.id.toolbar_button_exit);
        toolbar_text_state = findViewById(R.id.main_text_state);
        //启动游戏
        startGame=findViewById(R.id.layout_startgame);
        startGame.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    if(!versiontext.getText().toString().equals("未选择版本")){
                        if(msh.getBoolean("键鼠", false)){
//                            startActivity(new Intent(MioLauncher.this,BoatClientActivity_js.class));
                        }else{
                            startActivity(new Intent(MioLauncher.this,BoatClientActivity_aj.class));

                        }
                    }else{
                        toast("未选择游戏版本");
                    }
                    
                      }
            });
        versiontext=findViewById(R.id.activity_main_versiontext);
        spinner_choice_version=findViewById(R.id.spinner_choice_version);
        spinner_choice_version.setOnItemSelectedListener(new OnItemSelectedListener(){
                @Override
                public void onItemSelected(AdapterView<?> p1, View p2, int p3, long p4) {
                    if(isFirstInitGameSpinner){
                        isFirstInitGameSpinner=false;
                    }else{
                        versiontext.setText((String)startGame_arrayAdapter.getItem(p3));
                        MioLogin.set("currentVersion",new File(MioLogin.get("game_directory"),"versions/"+(String)startGame_arrayAdapter.getItem(p3)).getAbsolutePath());
                    }
//                    toast((String)startGame_arrayAdapter.getItem(p3));
                }

                @Override
                public void onNothingSelected(AdapterView<?> p1) {
                    
                }
            });
        initVersionSpinner();
        spinner_choice_version.setAdapter(startGame_arrayAdapter);
        try{
            if(new File(MioLogin.get("currentVersion")).exists()){
                spinner_choice_version.setSelection(startGame_versionsFile.indexOf(new File(MioLogin.get("currentVersion")).getName()));
                versiontext.setText(startGame_versionsFile.get(startGame_versionsFile.indexOf(new File(MioLogin.get("currentVersion")).getName())));
            }
            
//            if(versiontext.getText().toString().equals("未选择版本")){
//                spinner_choice_version.setSelection(0);
//                versiontext.setText(startGame_versionsFile.get(startGame_versionsFile.indexOf(0)));
//            }
        }catch(Exception e){
            spinner_choice_version.setSelection(0);
            versiontext.setText(startGame_versionsFile.get(0));
            MioLogin.set("currentVersion",new File(MioLogin.get("game_directory"),"versions/"+startGame_versionsFile.get(0)).getAbsolutePath());
        }
        

        
        //左侧功能区域
        left_btn_user = findButton(R.id.main_button_user);
        left_btn_version = findButton(R.id.main_button_version);
        left_btn_gamelist = findButton(R.id.main_button_gamelist);
        left_btn_gamedir = findButton(R.id.main_button_gamedir);
        left_btn_setting = findButton(R.id.main_button_setting);
        layout_user = findViewById(R.id.layout_user);
        layout_version = findViewById(R.id.layout_version);
        layout_gamelist = findViewById(R.id.layout_gamelist);
        layout_gamedir = findViewById(R.id.layout_gamedir);
        layout_setting = findViewById(R.id.layout_setting);
        list_layouts = new ArrayList<LinearLayout>();
        list_layouts.add(layout_user);
        list_layouts.add(layout_version);
        list_layouts.add(layout_gamelist);
        list_layouts.add(layout_gamedir);
        list_layouts.add(layout_setting);
        for (View layout:list_layouts) {
            layout.setVisibility(View.INVISIBLE);
        }
        //用户区域
        layout_user_adduser = findViewById(R.id.layout_user_adduser);
        layout_user_adduser.setOnClickListener(this);
        layout_user_refresh = findViewById(R.id.layout_user_refresh);
        layout_user_refresh.setOnClickListener(this);
        layout_user_listview=findViewById(R.id.layout_user_listview);
        parseJsonToUser();
        adaptwr_user=new AdapterListUser(this,userList);
        layout_user_listview.setAdapter(adaptwr_user);
        //游戏版本区域
        layout_version_modctrl=findViewById(R.id.layout_version_modctrl);
        layout_version_modctrl.setOnClickListener(this);
        layout_version_packctrl=findViewById(R.id.layout_version_packctrl);
        layout_version_packctrl.setOnClickListener(this);
        layout_version_editJvm=findViewById(R.id.layout_version_editJvm);
        if(new File(MioUtils.getStoragePath(),"boat/config.txt").exists()){
            layout_version_editJvm.setText(MioLogin.get("extraJavaFlags"));
        }
        layout_version_save=findViewById(R.id.layout_version_save);
        layout_version_save.setOnClickListener(this);
        layout_version_refresh=findViewById(R.id.layout_version_refresh);
        layout_version_refresh.setOnClickListener(this);
        //游戏列表区域
        layout_gamelist_addgame=findViewById(R.id.layout_gamelist_addgame);
        layout_gamelist_addgame.setOnClickListener(this);
        layout_gamelist_refresh=findViewById(R.id.layout_gamelist_refresh);
        layout_gamelist_refresh.setOnClickListener(this);
        layout_gamelist_listview=findViewById(R.id.layout_gamelist_listview);
        initGamelist();
        layout_gamelist_listview.setAdapter(adapter_game);
        //启动器设置区域
        layout_settingButtonMouse=findButton(R.id.layout_settingButtonMouse);
        layout_settingButtonBackground=findButton(R.id.layout_settingButtonBackground);
        layout_settingButtonFix=findButton(R.id.layout_settingButtonFix);
        layout_settingButtonHelp=findButton(R.id.layout_settingButtonHelp);
        layout_settingCheckBoxFull=findViewById(R.id.layout_settingCheckBoxFull);
        layout_settingCheckBoxFull.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton p1, boolean p2) {
                    mshe.putBoolean("刘海",p2);
                    mshe.commit();
                }
            });
        layout_settingCheckBoxFull.setChecked(msh.getBoolean("刘海",false));
        layout_settingCheckBoxMaK=findViewById(R.id.layout_settingCheckBoxMaK);
        layout_settingCheckBoxMaK.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton p1, boolean p2) {
                    mshe.putBoolean("键鼠",p2);
                    mshe.commit();
                }
            });
        layout_settingCheckBoxMaK.setChecked(msh.getBoolean("键鼠", false));
        layout_settingButtonUpdate=findViewById(R.id.layout_settingButtonUpdate);
        layout_settingButtonUpdate.setOnClickListener(this);
    }
    //获取当前目录游戏版本
    public void initVersionSpinner(){
        File path=new File(MioLogin.get("game_directory"),"versions");
        if(startGame_versionsFile==null){
            startGame_versionsFile=new ArrayList<String>();
        }else{
            startGame_versionsFile.clear();
            startGame_arrayAdapter.notifyDataSetChanged();
        }
        if(path.exists()){
            for(File f:path.listFiles()){
                startGame_versionsFile.add(f.getName());
            }
            if(startGame_arrayAdapter==null){
                startGame_arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,startGame_versionsFile);
                startGame_arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            }else{
                startGame_arrayAdapter.notifyDataSetChanged();
                spinner_choice_version.setSelection(0);
                versiontext.setText("未选择版本");
            }
        }
            
    }
    //获取已安装游戏
    private void initGamelist(){
        if(gamelist_name==null&gamelist_file==null){
            gamelist_name=new ArrayList<String>();
            gamelist_file=new ArrayList<File>();
        }else{
            gamelist_name.clear();
            gamelist_file.clear();
        }
        
        if(new File(MioUtils.getStoragePath(),"boat").exists()){
            gamelist_file.add(new File(MioUtils.getStoragePath(),"/boat/gamedir"));
            gamelist_name.add("boat");
        }
        if(new File(new File(MioUtils.getExternalFilesDir(this)),"/澪/MC").exists()){
            File[] fs=new File(new File(MioUtils.getExternalFilesDir(this)),"/澪/MC").listFiles();
            if(fs!=null){
                for(File f:fs){
                    if(f.isDirectory()){
                        tempfilename=f.getName();
                        getGamedir(f);
                    }
                    
                }
            }
        }
        if(adapter_game==null){
//            for(int i=0;i<gamelist_file.size();i++){
//                for(int j=0;j<gamelist_name.size();j++){
//                    if(!gamelist_file.get(i).getAbsolutePath().contains(gamelist_name.get(j))){
//                        gamelist_name.remove(j);
//                    }
//                }
//            }
            adapter_game=new AdapterListGame(MioLauncher.this,gamelist_name,gamelist_file);
        }else{
            adapter_game.notifyDataSetChanged();
        }
        
    }
    private String tempfilename;
    private void getGamedir(File file){
        File[] listFiles = file.listFiles();
        if(listFiles != null){
            for (File file2 : listFiles) {
                if(file2.isDirectory()){
                    if(file2.getName().equals("gamedir")||file2.getName().equals(".minecraft")){
                        gamelist_file.add(file2);
                        gamelist_name.add(tempfilename);
                    }else{
                        getGamedir(file2);
                    }
                }
            }
            
        }
    }
    //初始化加载动画
    WaveLoadingView wave;
    private void initLoadingView(){
        View base = LayoutInflater.from(this).inflate(R.layout.waveloading, null);
        wave=base.findViewById(R.id.wave);
        ImageButton exit=base.findViewById(R.id.waveloadingImageButtonExit);
        exit.setVisibility(View.INVISIBLE);
        popupWindow = new PopupWindow();
        popupWindow.setWidth(LayoutParams.FILL_PARENT);
        popupWindow.setHeight(LayoutParams.FILL_PARENT);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setContentView(base);
        
    }
    //添加用户
    private void addUserToJson(String name,String state,boolean isSelected,String account,String pass,String uuid,String token){
        try {
            JSONObject json=null;
            if(msh.getString("users","").equals("")){
                json=new JSONObject();
            }else{
                json=new JSONObject(msh.getString("users",""));
            }
            JSONObject userData=new JSONObject();
            userData.put("state",state);
            userData.put("isSelected",isSelected);
            userData.put("loginInfo",new JSONArray().put(0,account).put(1,pass));
            userData.put("uuid",uuid);
            userData.put("token",token);
            json.put(name,userData);
            mshe.putString("users",json.toString());
            mshe.commit();
            parseJsonToUser();
        } catch (JSONException e) {
            toast(e.toString());
        }

    }
    //解析用户信息
    private void parseJsonToUser(){
        if(userList==null){
            userList = new ArrayList<UserBean>();
        }else{
            userList.clear();
        }
        if((!msh.getString("users","").equals(""))&&!msh.getString("users","").equals("{}")){
            try {
                JSONObject json=new JSONObject(msh.getString("users",""));
                JSONArray jsa=json.names();
                for (int i=0;i < jsa.length();i++) {
                    UserBean user=new UserBean();
                    user.setUserName(jsa.getString(i));
                    JSONObject json2=json.getJSONObject(jsa.getString(i));
                    user.setUserState(json2.getString("state"));
                    user.setIsSelected(json2.getBoolean("isSelected"));
                    user.setUuid(json2.getString("uuid"));
                    user.setToken(json2.getString("token"));
                    JSONArray jsa2=json2.getJSONArray("loginInfo");
                    user.setUserAccount(jsa2.getString(0));
                    user.setUserPass(jsa2.getString(1));
                    userList.add(user);
                }
            } catch (JSONException e) {
                toast(e.toString());
            }
        }
        


    }
    //find按键并绑定点击事件
    private Button findButton(int id) {
        Button temp=findViewById(id);
        temp.setOnClickListener(this);
        return temp;
    }
    @Override
    public void onClick(View v) {
        if (v == toolbar_button_home) {
            toolbar_text_state.setText("主页");
            startGame.setVisibility(View.VISIBLE);
            AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
            disappearAnimation.setDuration(500);
            for (final View vv:list_layouts) {
                if (vv.getVisibility() == View.VISIBLE) {
                    vv.startAnimation(disappearAnimation);
                    disappearAnimation.setAnimationListener(new Animation.AnimationListener(){
                            @Override
                            public void onAnimationStart(Animation p1) {
                            }

                            @Override
                            public void onAnimationEnd(Animation p1) {
                                vv.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onAnimationRepeat(Animation p1) {
                            }
                        });
                }
            }
        } else if (v == toolbar_button_exit) {
            finish();
            System.exit(0);
        } else if (v == left_btn_user) {
            showTargetView(layout_user);
            toolbar_text_state.setText(((Button)v).getText().toString());
        } else if (v == left_btn_version) {
            showTargetView(layout_version);
            toolbar_text_state.setText(((Button)v).getText().toString());
        } else if (v == left_btn_gamelist) {
            showTargetView(layout_gamelist);
            toolbar_text_state.setText(((Button)v).getText().toString());
        } else if (v == left_btn_gamedir) {
            showTargetView(layout_gamedir);
            toolbar_text_state.setText(((Button)v).getText().toString());
        } else if (v == left_btn_setting) {
            showTargetView(layout_setting);
            toolbar_text_state.setText(((Button)v).getText().toString());
        } else if (v == layout_user_adduser) {
            View tempView=LayoutInflater.from(MioLauncher.this).inflate(R.layout.alert_login, null);
            final AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("添加用户")
                .setView(tempView)
                .create();
            dialog.show();
            final EditText tempUser=tempView.findViewById(R.id.alert_login_edit_account);
            final EditText tempPass=tempView.findViewById(R.id.alert_login_edit_password);
            Button tempLogin=tempView.findViewById(R.id.alert_login_btn_login);
            Button tempCancle=tempView.findViewById(R.id.alert_login_btn_cancle);
            CheckBox tempCheck=tempView.findViewById(R.id.alert_login_check);
            final LinearLayout tempPassLinear=tempView.findViewById(R.id.alert_login_linear_pass);
            tempLogin.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View p1) {
                        final String temp1=tempUser.getText().toString();
                        final String temp2=tempPass.getText().toString();
                        if (!isOfficial) {
                            if (temp1.equals("")) {
                                toast("用户名不能为空");
                            } else {
                                addUserToJson(temp1,"离线模式",false,"","","","");
                                adaptwr_user.notifyDataSetChanged();
                                dialog.dismiss();
                            }
                        }else{
                            if(temp1.trim().equals("")||temp2.trim().equals("")){
                                toast("账号和密码都不能为空");
                            }else{
                                toast("登录中");
                                new MioLogin(new MioLogin.LoginListener(){
                                        @Override
                                        public void onSucceed(String name, String token, String uuid) {
                                            addUserToJson(name,"正版登录",false,temp1,temp2,uuid,token);
                                            adaptwr_user.notifyDataSetChanged();
                                            toast("登录成功");
                                        }

                                        @Override
                                        public void onError(String error) {
                                            toast(error);
                                        }
                                    }).login(temp1,temp2);
                                
                                dialog.dismiss();
                            }
                        }
                    }
                });
            tempCancle.setOnClickListener(new OnClickListener(){
                    @Override
                    public void onClick(View p1) {
                        dialog.dismiss();
                    }
                });
            tempCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                    @Override
                    public void onCheckedChanged(CompoundButton p1, boolean p2) {
                        if (p2) {
                            tempPassLinear.setVisibility(View.VISIBLE);
                        } else {
                            tempPassLinear.setVisibility(View.INVISIBLE);
                        }
                        isOfficial = p2;
                    }
                });

        } else if (v == layout_user_refresh) {
            
        }else if(v==layout_version_modctrl){
            startActivity(new Intent(MioLauncher.this,MioMod.class));
        }else if(v==layout_version_packctrl){
            startActivity(new Intent(MioLauncher.this,MioPack.class));
        }else if(v==layout_version_save){
            MioLogin.set("extraJavaFlags",layout_version_editJvm.getText().toString());
            toast("已保存");
        }else if(v==layout_version_refresh){
            layout_version_editJvm.setText(MioLogin.get("extraJavaFlags"));
            toast("已刷新");
        }else if(v==layout_gamelist_addgame){
            String[] items={"在线安装","本地安装"};
            AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("请选择安装方式")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        if(which==0){
                            startActivity(new Intent(MioLauncher.this,MioDownload.class));
                        }else if(which==1){
                            new LFilePicker()
                                .withActivity(MioLauncher.this)
                                .withRequestCode(233)
                                .withStartPath("/storage/emulated/0/")
                                .withFileFilter(new String[]{".zip"})
                                .start();
                        }
                    }
                })
                .setPositiveButton("确定", null)
                .setNegativeButton("取消", null)
                .create();
            dialog.show();
        }else if(v== layout_gamelist_refresh){
            initGamelist();
        }else if(v==layout_settingButtonMouse){
            String[] items={"重置","选择"};
            AlertDialog dialog=new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        if(which==0){
                            new File(MioUtils.getExternalFilesDir(MioLauncher.this),"澪/cursor.png").delete();
                        }else if(which==1){
                            Intent intent = new Intent(Intent.ACTION_PICK);  //跳转到 ACTION_IMAGE_CAPTURE
                            intent.setType("image/*");
                            startActivityForResult(intent,100);
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .create();
            dialog.show();
            
        }else if(v==layout_settingButtonBackground){
            String[] items={"重置","选择"};
            AlertDialog dialog=new AlertDialog.Builder(this)
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        if(which==0){
                            background.setBackgroundResource(R.drawable.background);
                            new File(MioUtils.getExternalFilesDir(MioLauncher.this),"澪/bg.png").delete();
                        }else if(which==1){
                            Intent intent = new Intent(Intent.ACTION_PICK);  //跳转到 ACTION_IMAGE_CAPTURE
                            intent.setType("image/*");
                            startActivityForResult(intent,101);
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .create();
            dialog.show();
        }else if(v==layout_settingButtonFix){
            final String[] items={"GBK","GB2312","GB18030","UTF-8"};
            AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("请选择游戏启动编码")
                .setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        MioLogin.set("extraJavaFlags",MioLogin.get("extraJavaFlags")+" -Dfile.encoding="+items[which]);
                        layout_version_editJvm.append(" -Dfile.encoding="+items[which]);
                        Toast.makeText(MioLauncher.this, "已选择编码:" + items[which], 1000).show();
                    }
                })
                .setNegativeButton("取消", null)
                .create();
            dialog.show();
        }else if(v==layout_settingButtonUpdate){
            final Handler up=new Handler(){
                @Override
                public void handleMessage(Message msg) {
                    super.handleMessage(msg);
                    if(msg.what==0){
                        AlertDialog dialog=new AlertDialog.Builder(MioLauncher.this)
                            .setTitle("更新")
                            .setMessage((String)msg.obj)
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dia, int which) {

                                }
                            })
                            .setNegativeButton("取消", null)
                            .create();
                        dialog.show();
                    }else if(msg.what==1){
                        if(Integer.valueOf((String)msg.obj)>version){
                            if(!new File(MioUtils.getExternalFilesDir(MioLauncher.this),"/澪/update").exists()){
                                new File(MioUtils.getExternalFilesDir(MioLauncher.this),"/澪/update").mkdirs();
                            }
                            AlertDialog dialog_up=new AlertDialog.Builder(MioLauncher.this)
                                .setTitle("更新")
                                .setMessage("检测到新版本，是否更新？")
                                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dia, int which) {
                                        View vi=LayoutInflater.from(MioLauncher.this).inflate(R.layout.alert_update,null);
                                        TextView progressText=vi.findViewById(R.id.alertupdateTextView);
                                        AlertDialog dialog=new AlertDialog.Builder(MioLauncher.this)
                                            .setTitle("更新")
                                            .setView(vi)
                                            .create();
                                        dialog.setCanceledOnTouchOutside(false);
                                        dialog.show();
                                        new MioDownloader(MioLauncher.this, progressText, new MioDownloader.OnDownloadFinishListener(){

                                                @Override
                                                public void onFinish()
                                                {
                                                    if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
                                                        boolean isGranted = getPackageManager().canRequestPackageInstalls();
                                                        if(!isGranted){
                                                            Toast.makeText(MioLauncher.this,"请允许本应用安装更新包",1000).show();
                                                            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES);
                                                            startActivityForResult(intent, 233);
                                                        }else{
                                                            installApk(MioLauncher.this,new File(MioUtils.getExternalFilesDir(MioLauncher.this),"/澪/update/boat-澪.apk").toString());
                                                        }
                                                    }else{
                                                        installApk(MioLauncher.this,new File(MioUtils.getExternalFilesDir(MioLauncher.this),"/澪/update/boat-澪.apk").toString());
                                                    }


                                                }
                                            }).download("http://"+ip+":30945/files/boat-pro.apk",new File(MioUtils.getExternalFilesDir(MioLauncher.this),"/澪/update").toString(),"boat-澪.apk");

                                    }
                                }).setNegativeButton("取消",null)
                                .create();
                            dialog_up.show();

                        }else{
                            Toast.makeText(MioLauncher.this,"当前已是最新版本。",1000).show();
                        }

                    }

                }
            };
            new Thread(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            HttpURLConnection con=(HttpURLConnection)new URL("http://"+ip+"/Mio/mio_version.txt").openConnection();
                            InputStream in = con.getInputStream();
                            BufferedReader bfr=new BufferedReader(new InputStreamReader(in));
                            String temp=null;
                            String str="";
                            while ((temp = bfr.readLine()) != null) {
                                str += temp;
                            }
                            bfr.close();
                            in.close();
                            con.disconnect();
                            Message msg=new Message();
                            msg.obj = str;
                            msg.what=1;
                            up.sendMessage(msg);
                        } catch (IOException e) {
                            Message msg=new Message();
                            msg.what=0;
                            msg.obj = "版本获取出错:" + e.toString();
                            up.sendMessage(msg);
                        }
                    }
                }).start();
        }
        
    }
    private void installApk(Context context, String apkPath) {
        File file = new File(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //判读版本是否在7.0以上
        if (Build.VERSION.SDK_INT >= 24) {
            //provider authorities
            Uri apkUri = FileProvider.getUriForFile(context, "cosine.boat.fileprovider", file);
            //Granting Temporary Permissions to a URI
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        }

        context.startActivity(intent);

    }
    
//消失/出现动画
    private void showTargetView(View v) {
        startGame.setVisibility(View.INVISIBLE);
        AlphaAnimation appearAnimation = new AlphaAnimation(0, 1);
        appearAnimation.setDuration(500);
        AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
        disappearAnimation.setDuration(500);
        if (v.getVisibility() == View.INVISIBLE) {
            v.startAnimation(appearAnimation);
            v.setVisibility(View.VISIBLE);
        }
        for (View vv:list_layouts) {
            if (vv != v && vv.getVisibility() == View.VISIBLE) {
                vv.startAnimation(disappearAnimation);
                vv.setVisibility(View.INVISIBLE);
            }
        }

    }
    private void toast(String str) {
        Toast.makeText(this, str, 1000).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode!=RESULT_CANCELED){
            Uri uri=data.getData();
            if(requestCode==100){
                if(!new File(MioUtils.getExternalFilesDir(MioLauncher.this)+"/澪/").exists()){
                    new File(MioUtils.getExternalFilesDir(MioLauncher.this)+"/澪/").mkdirs();
                }
                try {
                    InputStream in= getContentResolver().openInputStream(uri);
                    FileOutputStream fw=new FileOutputStream(MioUtils.getExternalFilesDir(MioLauncher.this)+"/澪/cursor.png");
                    byte[] b=new byte[in.available()];
                    in.read(b);
                    fw.write(b);
                    fw.flush();
                    fw.close();
                    in.close();
                } catch (Exception e) {
                    Toast.makeText(this,e.toString(),1000).show();
                }
            } else if (requestCode == 101) {
                if(!new File(MioUtils.getExternalFilesDir(MioLauncher.this)+"/澪/").exists()){
                    new File(MioUtils.getExternalFilesDir(MioLauncher.this)+"/澪/").mkdirs();
                }
                try {
                    InputStream in= getContentResolver().openInputStream(uri);
                    FileOutputStream fw=new FileOutputStream(MioUtils.getExternalFilesDir(MioLauncher.this)+"/澪/bg.png");
                    byte[] b=new byte[in.available()];
                    in.read(b);
                    fw.write(b);
                    fw.flush();
                    fw.close();
                    in.close();
                    Bitmap bitmap=BitmapFactory.decodeFile(MioUtils.getExternalFilesDir(this) + "/澪/bg.png");
                    BitmapDrawable bd=new BitmapDrawable(bitmap);
                    background.setBackground(bd);
                } catch (Exception e) {
                    Toast.makeText(this,e.toString(),1000).show();
                }
            }else if(requestCode==233){
                final File path=new File((String)data.getStringArrayListExtra("paths").get(0));
                new File(MioUtils.getExternalFilesDir(MioLauncher.this), "/澪/MC/" + path.getName().replace(".zip","").replace("+","").replace("_","")).mkdirs();
                popupWindow.showAtLocation(MioLauncher.this.getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, 0, 0);
                new MioUtils(new MioUtils.ZipListener(){
                        @Override
                        public void onStart() {
                        }

                        @Override
                        public void onProgress(int progress) {
                            wave.setPercent(progress);
                        }

                        @Override
                        public void onFinish() {
                            popupWindow.dismiss();
                            new File(MioUtils.getExternalFilesDir(MioLauncher.this) , path.getName()).delete();
                            initGamelist();
                            Toast.makeText(MioLauncher.this, "运行库安装完成。", 1000).show();
                        }

                        @Override
                        public void onError(String err) {

                            popupWindow.dismiss();
                            Toast.makeText(MioLauncher.this,"导入出错："+err,1000).show();;

                        }
                    }).unzipWithProgress(path.getAbsolutePath(),new File(MioUtils.getExternalFilesDir(MioLauncher.this), "/澪/MC/" + path.getName().replace(".zip","").replace("+","").replace("_","")).getAbsolutePath());
            }
        }
    }
    
}
