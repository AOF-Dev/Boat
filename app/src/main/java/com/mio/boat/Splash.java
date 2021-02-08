package com.mio.boat;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import cosine.boat.Activity_Download;
import java.io.File;
import android.os.Handler;
import java.io.IOException;
import cosine.boat.Utils;
import android.widget.Toast;

public class Splash extends Activity {
    File MC,MOD,PACK,RUNTIME,UPDATE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        init();
        hideBottomMenu();
        checkFiles();
    }

    private void init() {
        MC=new File(MioUtils.getExternalFilesDir(this),"澪/MC");
        MOD=new File(MioUtils.getExternalFilesDir(this),"澪/MOD");
        PACK=new File(MioUtils.getExternalFilesDir(this),"澪/PACK");
        RUNTIME=new File(MioUtils.getExternalFilesDir(this),"澪/runtime");
        UPDATE=new File(MioUtils.getExternalFilesDir(this),"澪/update");
        MC.mkdirs();
        MOD.mkdir();
        PACK.mkdirs();
        RUNTIME.mkdirs();
        UPDATE.mkdirs();
        boolean runtime=new File(this.getApplicationContext().getFilesDir().getParent() , "/app_runtime").exists();
        boolean lwjgl3=new File(this.getApplicationContext().getFilesDir().getParent() , "/app_runtime/lwjgl-3").exists();
        if(runtime&&!lwjgl3){
            MioUtils.deleteFile(new File(this.getApplicationContext().getFilesDir().getParent() , "/app_runtime").getAbsolutePath());
        }
        if(!new File(MioUtils.getExternalFilesDir(this),"澪/key_normal.txt").exists()){
            Utils.extractAsset(getAssets(),"key_normal.txt",new File(MioUtils.getExternalFilesDir(this),"澪/key_normal.txt").getAbsolutePath());
            Toast.makeText(this,"未发现按键信息，已自动创建默认按键。",1000).show();
        }
    }
    private void hideBottomMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(lp);
        }
    }
    private void checkFiles() {
        final boolean config=new File(MioUtils.getStoragePath(),"/boat/config.txt").exists();
        final boolean gamedir=new File(MioUtils.getStoragePath(),"/boat/gamedir").exists();
        boolean runtime=new File(this.getApplicationContext().getFilesDir().getParent() , "/app_runtime").exists();
        
        if (!config || !gamedir || !runtime) {
            AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("核心文件检测结果如下:\nconfig.txt:" + config + "\nGamedir:" + gamedir + "\nRuntime:" + runtime + "\n文件缺失，程序无法正常运行")
                .setPositiveButton("进入文件下载页", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        startActivity(new Intent(Splash.this,Activity_Download.class));
                        finish();
                    }
                })
                .create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);
            dialog.show();
        } else {
            new Handler().postDelayed(new Runnable(){
                    @Override
                    public void run() {
                        startActivity(new Intent(Splash.this,MioLauncher.class));
                        finish();
                    }
                }, 1000);
        }

    }
}
