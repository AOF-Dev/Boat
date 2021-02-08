package cosine.boat;

import android.content.Context;
import android.widget.TextView;
import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Objects;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class MioDownloader {
    private Context context;
    private TextView text;
    private OnDownloadFinishListener listener;
    private int downloadId;
    public MioDownloader(Context context,TextView text,OnDownloadFinishListener listener){
        this.context=context;
        this.text=text;
        this.listener=listener;
        
    }
    public MioDownloader download(String url,String dirPath,String fileName){
        downloadId=PRDownloader.download(url, dirPath, fileName)
            .build()
            .setOnPauseListener(new OnPauseListener() {
                @Override
                public void onPause() {
                    text.setText("已暂停");
                }
            })
            .setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel() {
                    text.setText("已取消");
                }
            })
            .setOnProgressListener(new OnProgressListener() {
                @Override
                public void onProgress(Progress progress) {
                    text.setText(progress.currentBytes/(1024*1024)+"Mb/"+progress.totalBytes/(1024*1024)+"Mb");
                }
            })
            .start(new OnDownloadListener() {
                @Override
                public void onError(Error p1) {
                    text.setText(p1.getConnectionException().toString());
                    
                }

                @Override
                public void onDownloadComplete() {
                    text.setText("下载完毕!");
                    listener.onFinish();
                }
            });            
        return this;
    }
    public void cancle(){
        PRDownloader.cancel(downloadId);
    }
    public void pause(){
        PRDownloader.pause(downloadId);
    }
    public void resume(){
        PRDownloader.resume(downloadId);
    }
    public int getDownloadId(){
        return this.downloadId;
    }
    public interface OnDownloadFinishListener{
        void onFinish();
    }
    
    public static String getUrl(String initialUrl) throws Exception {
        String headerUrl = "https://wwa.lanzous.com";
        /*
         获取referer值
         */
        Document doc = Jsoup.connect(initialUrl)
            .maxBodySize(Integer.MAX_VALUE)
            .data("query", "Java")
            .cookie("auth", "token")
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.30 Safari/537.36")
            .timeout(20000)
            .get();//有时候为post(); 可以打印出doc.html自行分析  System.out.println(doc);
        String b = doc.select("iframe[class=ifr2]").attr("src");
        String referer = headerUrl + b;//拼接得到referer
        System.out.println(referer);
        /*
         获取sign值
         */
        Document doc1 = Jsoup.connect(referer)
            .maxBodySize(Integer.MAX_VALUE)
            .data("query", "Java")
            .cookie("auth", "token")
            .userAgent("Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.30 Safari/537.36")
            .timeout(20000)
            .get();
        //有时候为post(); 可以打印出doc.html自行分析  System.out.println(doc1);
//        System.out.println(doc1);
        Elements a1 = doc1.select("script[type=text/javascript]");
        String temp1=a1.toString();
        temp1=temp1.substring(temp1.indexOf("data : { 'action':'downprocess','sign':'", temp1.indexOf("//data")+6));
        temp1=temp1.substring(temp1.indexOf("sign':'")+"sign':'".length(),temp1.indexOf("','ves':1"));
        String sign = temp1;
        System.out.println(sign);
        /*
         获取对应target里的Response
         返回josn格式
         */
        //根据第一步、第二步获取的Referer和sign向target(服务器地址)发送Post请求
        StringBuilder result = new StringBuilder();
        //要提交的服务器地址
        String target = headerUrl + getSubString(a1.toString(), "url : '", "'");
        URL url = new URL(target); //创建URL对象
        HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();//创建一个HTTP连接
        urlConn.setRequestMethod("POST");//指定使用POST请求方式
        urlConn.setDoInput(true);//向连接中写入数据
        urlConn.setDoOutput(true);//从连接中读取数据
        urlConn.setUseCaches(false);//禁止缓存
        urlConn.setInstanceFollowRedirects(true);//自动执行HTTP重定向
        //设置RequestHeaders
        urlConn.setRequestProperty("Content-Type", "application/xml, text/*");
        urlConn.setRequestProperty("accept", "application/json, text/javascript, */*");
        urlConn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.9");
        urlConn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.30 Safari/537.36");
        urlConn.setRequestProperty("Host", "wwa.lanzous.com");
        urlConn.setRequestProperty("Connection", "Keep-Alive");
        urlConn.setRequestProperty("Referer", referer);
        DataOutputStream out = new DataOutputStream(urlConn.getOutputStream());//获取输出流
        //拼接要提交的数据
        String data = "action=downprocess&sign=" + sign + "&ves=1";
        out.writeBytes(data);//将要传递的数据写入数据输出流
        out.flush();//输出缓存
        out.close();//关闭数据输出流
        if (urlConn.getResponseCode() == HttpURLConnection.HTTP_OK) {//判断是否响应成功
            InputStreamReader in = new InputStreamReader(urlConn.getInputStream());//获得读取的内容
            BufferedReader buffer = new BufferedReader(in); // 获取输入流对象
            String inputLine;
            while ((inputLine = buffer.readLine()) != null) {//通过循环逐行读取输入流中的内容
                result.append(inputLine);
            }
            in.close();//关闭字符输入流
        }
        urlConn.disconnect();//断开连接
        System.out.println(result.toString());
        /*
         对得到的结果进行Json解析
         将解析的结果按照指示进行拼接 获得一个链接
         */
        JSONObject obj =new JSONObject(result.toString());//转换为json对象
        String results = obj.get("dom") + "/file/" + obj.get("url");//拼接字符串得到链接
        System.out.println(results);
        /*
         重定向
         将上述得到的链接重定向得到最终下载直链
         */
        Connection connect = Jsoup.connect(results);
        connect.header("Accept-Language", "zh-CN,zh;q=0.8,zh-TW;q=0.7,zh-HK;q=0.5,en-US;q=0.3,en;q=0.2");
        Connection.Response response = connect.followRedirects(true).execute();
        String finalUrl = response.header("Location");//response.header("Location")即为文件的直链
        System.out.println("finalUrl:" + finalUrl);
        return finalUrl;
    }

    private static String getSubString(String text, String left, String right) {
        String result;
        int zLen;
        if (left == null || left.isEmpty()) {
            zLen = 0;
        } else {
            zLen = text.indexOf(left);
            if (zLen > -1) {
                zLen += left.length();
            } else {
                zLen = 0;
            }
        }
        int yLen = text.indexOf(right, zLen);
        if (yLen < 0 || right.isEmpty()) {
            yLen = text.length();
        }
        result = text.substring(zLen, yLen);
        return result;
    }
}
