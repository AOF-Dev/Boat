package com.mio.boat;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.DownloadListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AbsoluteLayout.LayoutParams;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.wingsofts.guaguale.WaveLoadingView;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

public class MioDownload extends Activity {
    LinearLayout layout_miodownload_addtask;
    boolean isdown=true;
    LinearLayout layout;
    PopupWindow popupWindow;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_miodownload);
        if(isNetworkConnected(this)){
            getUrl("mio_url_d");
        }else{
            Toast.makeText(this,"网络未连接，下载功能无法使用",1000).show();
        }
        initLoadingView();
        layout_miodownload_addtask = findViewById(R.id.layout_miodownload_addtask);
        layout = findViewById(R.id.layout_miodownload_view);
        layout_miodownload_addtask.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    if(isUrlExist){
                        isdown=true;
                        final WebView web=new WebView(MioDownload.this);
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
                                        popup.dismiss();
                                        final String filename=URLDecoder.decode(contentDisposition.substring(contentDisposition.indexOf("filename=") + "filename=".length()).replace("\"", ""));
                                        Toast.makeText(MioDownload.this, "开始下载游戏本体:" + filename, 1000).show();
                                        final View view=LayoutInflater.from(MioDownload.this).inflate(R.layout.item_view_miodownload, null);
                                        final ProgressBar progressBar=view.findViewById(R.id.item_listview_miodownloadProgressBar);
                                        final TextView progressText=view.findViewById(R.id.item_listview_miodownloadTextView);
                                        TextView name=view.findViewById(R.id.item_listview_miodownloadName);
                                        name.setText(filename+"   ");
                                        final int downloadID=PRDownloader.download(url, MioUtils.getExternalFilesDir(MioDownload.this), filename)
                                            .build()
                                            .setOnPauseListener(new OnPauseListener() {
                                                @Override
                                                public void onPause() {

                                                }
                                            })
                                            .setOnCancelListener(new OnCancelListener() {
                                                @Override
                                                public void onCancel() {

                                                }
                                            })
                                            .setOnProgressListener(new OnProgressListener() {
                                                @Override
                                                public void onProgress(Progress progress) {
                                                    long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                                                    progressBar.setProgress((int)progressPercent);
                                                    progressText.setText(progress.currentBytes / (1024 * 1024) + "Mb/" + progress.totalBytes / (1024 * 1024) + "Mb");
                                                }
                                            })
                                            .start(new OnDownloadListener() {
                                                @Override
                                                public void onError(Error error) {
                                                    toast(error.getConnectionException().toString());

                                                }

                                                @Override
                                                public void onDownloadComplete() {
                                                    new File(MioUtils.getExternalFilesDir(MioDownload.this), "/澪/MC/" + filename.replace(".zip","").replace("+","").replace("_","")).mkdirs();
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
                                                                new File(MioUtils.getExternalFilesDir(MioDownload.this) , filename).delete();
                                                                progressText.setText("安装完成");
                                                            }

                                                            @Override
                                                            public void onError(String err) {
                                                                toast("错误：" + err);
                                                                popupWindow.dismiss();
                                                                progressText.setText("安装出错");
                                                            }
                                                        }).unzipWithProgress(new File(MioUtils.getExternalFilesDir(MioDownload.this) , filename).getAbsolutePath(),new File(MioUtils.getExternalFilesDir(MioDownload.this), "/澪/MC/" + filename.replace(".zip","").replace("+","").replace("_","")).getAbsolutePath());

                                                    if(!popupWindow.isShowing())
                                                        popupWindow.showAtLocation(MioDownload.this.getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, 0, 0);

                                                }
                                            });            
                                        Button cancle=view.findViewById(R.id.item_listview_miodownloadButtonCancle);
                                        cancle.setOnClickListener(new OnClickListener(){
                                                @Override
                                                public void onClick(View p1) {
                                                    PRDownloader.cancel(downloadID);
                                                    layout.removeView(view);
                                                }
                                            });
                                        final Button pause=view.findViewById(R.id.item_listview_miodownloadButtonPause);
                                        pause.setOnClickListener(new OnClickListener(){
                                                @Override
                                                public void onClick(View p1) {
                                                    if (pause.getText().equals("暂停")) {
                                                        PRDownloader.pause(downloadID);
                                                        pause.setText("继续");
                                                    } else {
                                                        pause.setText("暂停");
                                                        PRDownloader.resume(downloadID);
                                                    }
                                                }
                                            });
                                        layout.addView(view);
                                    }
                                }});
                        popup.showAtLocation(MioDownload.this.getWindow().getDecorView(), Gravity.TOP | Gravity.LEFT, 0, 0);
                        
                    }else{
                        toast("获取下载链接失败");
                    }
                    
                }
            });
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
    //初始化加载动画
    WaveLoadingView wave;
    private void initLoadingView(){
        View base = LayoutInflater.from(this).inflate(R.layout.waveloading, null);
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
    }
    private void toast(String str) {
        Toast.makeText(this, str, 1000).show();
    }
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
                Toast.makeText(MioDownload.this,"下载链接获取成功",1000).show();
            }else{
                Toast.makeText(MioDownload.this,(String)msg.obj,1000).show();
                isUrlExist=false;
                
            }
        }};
}
