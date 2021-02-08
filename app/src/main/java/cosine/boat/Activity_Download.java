package cosine.boat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.mio.boat.MioLauncher;
import com.mio.boat.MioUtils;
import com.mio.boat.R;
import com.wingsofts.guaguale.WaveLoadingView;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Activity_Download extends Activity {
    TextView runtimeProgress,gameProgress,runtimeText,gameText;
    boolean isRuntime=false,isGame=false;
    boolean config,gamedir,runtime;
    private SharedPreferences msh;
    private SharedPreferences.Editor mshe;
    PopupWindow popupWindow;
    WaveLoadingView wave;
    View base;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_download);
        if(isNetworkConnected(this)){
            getUrl("mio_url_d");
        }else{
            Toast.makeText(this,"网络未连接，下载功能无法使用",1000).show();
        }

        runtimeText = findViewById(R.id.acdownloadTextView1);
        gameText = findViewById(R.id.acdownloadTextView2);
        runtimeProgress = findViewById(R.id.acdownloadTextViewRuntime);
        gameProgress = findViewById(R.id.acdownloadTextViewGame);
        config = new File(MioUtils.getStoragePath() + "/boat/config.txt").exists();
        gamedir = new File(MioUtils.getStoragePath() + "/boat/gamedir").exists();
        runtime = new File(this.getApplicationContext().getFilesDir().getParent() + "/app_runtime").exists(); //new File("/data/data/cosine.boat/app_runtime").exists();
        runtimeText.setText("运行库:" + runtime);
        gameText.setText("游戏本体:" + (gamedir && config));
        if (gamedir && config) {
            gameProgress.setText("数据已安装");
        }
        if (runtime) {
            runtimeProgress.setText("运行库已安装");
        }
        msh = getSharedPreferences("Mio", MODE_PRIVATE);
        mshe = msh.edit();
        hideBottomMenu();
        base = LayoutInflater.from(Activity_Download.this).inflate(R.layout.waveloading, null);
        wave=base.findViewById(R.id.wave);
        ImageButton exit=base.findViewById(R.id.waveloadingImageButtonExit);
        exit.setVisibility(View.GONE);
        popupWindow = new PopupWindow();
        popupWindow.setWidth(LayoutParams.FILL_PARENT);
        popupWindow.setHeight(LayoutParams.FILL_PARENT);
        popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setContentView(base);
//		ow.setAutoStartAnim(true);
    }
    Handler han=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (runtime && gamedir) {
                Toast.makeText(Activity_Download.this, "检测到数据文件已完整，将于一秒后启动。", 1000).show();
                new Handler().postDelayed(new Runnable(){
                        @Override
                        public void run() {
                            mshe.putBoolean("First", false);
                            mshe.commit();
                            Intent in=new Intent(Activity_Download.this, MioLauncher.class);
                            in.putExtra("miopw", true);
                            startActivity(in);
                            finish();
                        }
                    }, 1000);
            } else {
                Toast.makeText(Activity_Download.this, "检测到数据不完整，无法启动。", 1000).show();
            }
        }};
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
    public void oc_runtime(View v) {
        runtime = new File(this.getApplicationContext().getFilesDir().getParent() + "/app_runtime").exists(); //new File("/data/data/cosine.boat/app_runtime").exists();
        runtimeText.setText("运行库:" + runtime);
        if (runtimeProgress.getText().toString().indexOf("java") != -1) {
            isRuntime = false;
        }
        if (!isRuntime && new File(MioUtils.getExternalFilesDir(Activity_Download.this) + "/澪/runtime/mioruntimev4.zip").exists()) {
            isRuntime = true;
            Toast.makeText(this, "检测到运行库已下载，开始导入。", 1000).show();
            win = true;
            popupWindow.showAtLocation(Activity_Download.this.getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, 0, 0);	
           
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
                        
                        win = false;
                        runtime = new File(Activity_Download.this.getApplicationContext().getFilesDir().getParent() + "/app_runtime").exists(); //new File("/data/data/cosine.boat/app_runtime").exists();
                        runtimeText.setText("运行库:" + runtime);
                        isRuntime = false;
                        Toast.makeText(Activity_Download.this, "运行库安装完成。", 1000).show();
                    }

                    @Override
                    public void onError(String err) {
  
                        popupWindow.dismiss();
                        Toast.makeText(Activity_Download.this,"导入出错："+err,1000).show();;
                    
                    }
                  }).unzipWithProgress(new File(MioUtils.getExternalFilesDir(this), "/澪/runtime/mioruntimev4.zip").getAbsolutePath(),getFilesDir().getParent());
        } else if (runtime) {
            Toast.makeText(this, "运行库已下载，请勿重复下载!", 1000).show();
        } else if (!isRuntime) {
            final WebView web=new WebView(this);
            final PopupWindow popup = new PopupWindow();
            popup.setWidth(LayoutParams.FILL_PARENT);
            popup.setHeight(LayoutParams.FILL_PARENT);
            popup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popup.setFocusable(true);
            popup.setOutsideTouchable(false);
            popup.setContentView(web);
            WebSettings webSettings = web.getSettings();
//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
            webSettings.setJavaScriptEnabled(true);
            webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
            webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
            webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
            webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
            webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
            webSettings.setAppCacheEnabled(false);
            webSettings.setDomStorageEnabled(true);
            webSettings.setAllowFileAccess(true); //设置可以访问文件
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
            webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
            webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
            

// 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            web.clearCache(true);
            web.clearHistory();
            web.clearFormData();
            web.clearSslPreferences();
            web.loadUrl("https://www.lanzoui.com/irOZ9g6n0kf");
            //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
            web.setWebViewClient(new WebViewClient(){
                    @Override    
                    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {    
                        handler.proceed();    //表示等待证书响应
                        // handler.cancel();      //表示挂起连接，为默认方式
                        // handler.handleMessage(null);    //可做其他处理
                    }    
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        //使用WebView加载显示url
                        view.loadUrl(url);

//                        Toast.makeText(Activity_Download.this,url,1000).show();
                        //返回true
                        return true;
                    }
                });
            //声明WebSettings子类
            web.setDownloadListener(new DownloadListener(){
                    @Override
                    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                        Toast.makeText(Activity_Download.this,url,1000).show();
                        if (isdown) {
                            isdown = false;
                            isRuntime=true;
                            new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        isdown=true;
                                    }
                                }, 2000);
                            popup.dismiss();
                            try {
                                new MioDownloader(Activity_Download.this, runtimeProgress, new MioDownloader.OnDownloadFinishListener(){
                                        @Override
                                        public void onFinish() {
                                            Toast.makeText(Activity_Download.this, "运行库下载完毕，开始导入。", 1000).show();
                                            win = true;
                                            popupWindow.showAtLocation(Activity_Download.this.getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, 0, 0);
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
                                                        win = false;
                                                        runtime = new File(Activity_Download.this.getApplicationContext().getFilesDir().getParent() + "/app_runtime").exists(); //new File("/data/data/cosine.boat/app_runtime").exists();
                                                        runtimeText.setText("运行库:" + runtime);
                                                        isRuntime = false;
                                                        Toast.makeText(Activity_Download.this, "运行库安装完成。", 1000).show();
                                                    }

                                                    @Override
                                                    public void onError(String err) {

                                                        popupWindow.dismiss();
                                                        Toast.makeText(Activity_Download.this,"导入出错："+err,1000).show();;

                                                    }
                                                }).unzipWithProgress(new File(MioUtils.getExternalFilesDir(Activity_Download.this), "/澪/runtime/mioruntimev4.zip").getAbsolutePath(),getFilesDir().getParent());
                                            
                                        }
                                    }).download(url, MioUtils.getExternalFilesDir(Activity_Download.this) + "/澪/runtime", "mioruntimev4.zip");
                                isRuntime = true;
                                popup.dismiss();
                                Toast.makeText(Activity_Download.this, "开始下载运行库，请勿重复点击!", 1000).show();
                            } catch (Exception e) {
                                Toast.makeText(Activity_Download.this, e.toString(), 1000).show();
                            }
                        }
                    }});

            popup.showAtLocation(Activity_Download.this.getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, 0, 0);

        } else {
            Toast.makeText(this, "正在下载运行库，请勿重复点击!", 1000).show();
        }
    }
    boolean isdown=true;
    public void oc_game(View v) {
        gamedir = new File(MioUtils.getStoragePath() + "/boat/gamedir").exists();
        gameText.setText("游戏本体:" + gamedir);
        if (gameProgress.getText().toString().indexOf("java") != -1) {
            isGame = false;
        }
        if (gamedir) {
            Toast.makeText(Activity_Download.this, "游戏已安装。", 1000).show();
        } else if (!isGame) {

            final WebView web=new WebView(this);
            final PopupWindow popup = new PopupWindow();
            popup.setWidth(LayoutParams.FILL_PARENT);
            popup.setHeight(LayoutParams.FILL_PARENT);
            popup.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            popup.setFocusable(true);
            popup.setOutsideTouchable(false);
            popup.setContentView(web);
            WebSettings webSettings = web.getSettings();
//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
            webSettings.setJavaScriptEnabled(true);
            webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
            webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
            webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
            webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
            webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件
            webSettings.setAppCacheEnabled(false);
            webSettings.setDomStorageEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(false);
            webSettings.setAllowFileAccess(true); //设置可以访问文件
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
            webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
            webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
// 特别注意：5.1以上默认禁止了https和http混用，以下方式是开启
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                webSettings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }
            web.clearCache(true);
            web.clearHistory();
            web.clearFormData();
            web.clearSslPreferences();
            
            //系统默认会通过手机浏览器打开网页，为了能够直接通过WebView显示网页，则必须设置
            web.setWebViewClient(new WebViewClient(){
                    @Override    
                    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {    
                        handler.proceed();    //表示等待证书响应
                        // handler.cancel();      //表示挂起连接，为默认方式
                        // handler.handleMessage(null);    //可做其他处理
                    }    
                    @Override
                    public void onPageFinished(WebView view,String url){
                        if(url.contains("qingstore.cn/#/s")){
                            String jsFunction="javascript:function startHide(){"+
                                "document.getElementsByTagName('body')[0].innerHTML;"+
                                "document.getElementById('app-7').remove();}";
//注入 js函数
                            view.loadUrl(jsFunction);//调用 js函数
                            view.loadUrl("javascript:startHide();");
                        }
                    }
                    @Override
                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                        if(url.contains("http")){
                            //使用WebView加载显示url
                            view.loadUrl(url);
                        }

                        //返回true
                        return true;
                    }
                });
             web.loadUrl(url);
            //声明WebSettings子类
            web.setDownloadListener(new DownloadListener(){
                    @Override
                    public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                        Toast.makeText(Activity_Download.this,url,1000).show();
                        if (isdown) {
                            isdown = false;
                            isGame = true;
                            new Handler().postDelayed(new Runnable(){
                                    @Override
                                    public void run() {
                                        isdown=true;
                                    }
                                }, 2000);
                            popup.dismiss();
                            final String filename=URLDecoder.decode(contentDisposition.substring(contentDisposition.indexOf("filename=") + "filename=".length()).replace("\"", ""));
                            Toast.makeText(Activity_Download.this, "开始下载游戏本体:" + filename + "，请勿重复点击!", 1000).show();
                            new MioDownloader(Activity_Download.this, gameProgress, new MioDownloader.OnDownloadFinishListener(){
                                    @Override
                                    public void onFinish() {
                                        Toast.makeText(Activity_Download.this, "游戏本体下载完毕，开始导入。", 1000).show();
                                     
                                        win = true;
                                        popupWindow.showAtLocation(Activity_Download.this.getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, 0, 0);
                                        
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
                                                    MioUtils.deleteFile(MioUtils.getStoragePath() + "/" + filename);
                                                    popupWindow.dismiss();
                                                    win = false;
                                                    gamedir = new File(MioUtils.getStoragePath() + "/boat/gamedir").exists();
                                                    gameText.setText("游戏本体:" + gamedir);
                                                    isGame = false;
                                                    Toast.makeText(Activity_Download.this, "游戏安装完成。", 1000).show();
                                                }

                                                @Override
                                                public void onError(String err) {
                                                    Toast.makeText(Activity_Download.this, err, 1000).show();
                                                    win = false;
                                                    popupWindow.dismiss();
                                                }
                                            }).unzipWithProgress(new File(MioUtils.getStoragePath() , filename).getAbsolutePath(),MioUtils.getStoragePath());
                                        

                                    }
                                }).download(url, MioUtils.getStoragePath() + "/", filename);

                        }
                    }});

            String[] items={"在线下载","本地导入"};
            AlertDialog dialog=new AlertDialog.Builder(this)
                .setTitle("选择安装方式")
                .setItems(items, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dia, int which) {
                        if (which == 0) {
                            if(isUrlExist){
                                popup.showAtLocation(Activity_Download.this.getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, 0, 0);
                                
                                
                            }else{
                                Toast.makeText(Activity_Download.this,"下载链接不存在",1000).show();
                            }
                            
                        } else if (which == 1) {
                            Toast.makeText(Activity_Download.this, "请手动选择需要导入的文件，格式为zip", 1000).show();
                            Intent in=new Intent(Intent.ACTION_GET_CONTENT);
                            in.setType("*/*");
                            in.addCategory(Intent.CATEGORY_OPENABLE);
                            startActivityForResult(in, 233);
                        }
                    }
                })
                .setNegativeButton("取消", null)
                .create();
            dialog.show();
        } else {
            Toast.makeText(this, "正在下载游戏数据，请勿重复点击!", 1000).show();
        }
    }

    String path;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Uri uri=data.getData();
            path = null;
            if (requestCode == 233) {
                if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                    path = uri.getPath();
                } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                    path = FileChooseUtil.getPath(this, uri);
                }
                if (path.endsWith(".zip")) {
                    AlertDialog dialog=new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("已选择文件：" + path + "。是否开始导入")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dia, int which) {
                                isGame = true;
                                Toast.makeText(Activity_Download.this, "导入中请耐心等待", 1000).show();
                                win = true;
                                popupWindow.showAtLocation(Activity_Download.this.getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, 0, 0);
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
                                            win = false;
                                            gamedir = new File(MioUtils.getStoragePath() + "/boat/gamedir").exists();
                                            gameText.setText("游戏本体:" + gamedir);
                                            isGame = false;
                                            Toast.makeText(Activity_Download.this, "游戏安装完成。", 1000).show();
                                        }

                                        @Override
                                        public void onError(String err) {
                                            Toast.makeText(Activity_Download.this, err, 1000).show();
                                            win = false;
                                            popupWindow.dismiss();
                                        }
                                    }).unzipWithProgress(path,MioUtils.getStoragePath());

                                
                            }
                        })
                        .setNegativeButton("否", null)
                        .create();
					dialog.show();
                } else {
                    Toast.makeText(this, "请选择zip格式的文件，其他格式暂不支持", 1000).show();
                }

            }
        } else {
            Toast.makeText(this, "未选择文件", 1000).show();
        }
    }

    public Map getFiles(String url, String type) {
        Map<String,String> temp=new HashMap<String,String>();
        try {
            Document doc=Jsoup.parse(new URL(url), 30000);
            System.out.println(doc);
            for (Element e: doc.getElementsByTag("a")) {
                String s=e.attr("href");
                if (s.contains(type)) {
                    temp.put(e.text(), s);
                }
            }
            return temp;
        } catch (IOException e) {
            temp.put("error", e.toString());
            return temp;
        }
    }
    public void oc_start(View v) {
        han.sendEmptyMessage(3);
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
    boolean win=false;
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        // TODO: Implement this method
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && win) {
            popupWindow.showAtLocation(Activity_Download.this.getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, 0, 0);    
        }

	}
    String url;
    private void getUrl(final String name) {
        new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        HttpURLConnection con=(HttpURLConnection)new URL("http://49.234.85.55/Mio/"+name).openConnection();
                        con.setConnectTimeout(3000);
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
                        msg.obj =str;
                        msg.what=0;
                        webFilePath.sendMessage(msg);

                    } catch (IOException e) {
                        Message msg=new Message();
                        msg.obj = "链接获取错误:" + e.toString();
                        msg.what=1;
                        webFilePath.sendMessage(msg);
                    }
                }
            }).start();
    }
    boolean isUrlExist=false;
    Handler webFilePath=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                url=(String)msg.obj;
                isUrlExist=true;
                Toast.makeText(Activity_Download.this,"下载链接获取成功",1000).show();
            }else{
                Toast.makeText(Activity_Download.this,(String)msg.obj,1000).show();
                isUrlExist=false;

            }
        }};
}
