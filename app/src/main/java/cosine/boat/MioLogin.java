package cosine.boat;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.json.JSONException;
import org.json.JSONObject;
import android.os.Handler;
import android.os.Message;
import android.view.ViewGroup;
import com.mio.boat.MioLauncher;
import com.mio.boat.MioUtils;

public class MioLogin {
    private LoginListener listener;
    public interface LoginListener {
        void onSucceed(String name, String token, String uuid);
        void onError(String error);
    }
    public MioLogin(LoginListener listener) {
        this.listener = listener;
    }
    private Handler loginHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String[] result=(String[])msg.obj;
                listener.onSucceed(result[0], result[1], result[2]);
            } else if (msg.what == 1) {
                listener.onError((String)msg.obj);
            }
        }};
    public void login(final String username, final String password) {
        new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        JSONObject json=new JSONObject();
                        json.put("username", username);
                        json.put("password", password);
                        json.put("agent", new JSONObject().put("name", "Minecraft").put("version", 1));
                        json.put("requestUser", true);
                        json.put("clientToken", "mio_launcher");
                        String str=getJsonData(json, "https://authserver.mojang.com/authenticate", false);
                        JSONObject data=new JSONObject(str);
                        String[] msg=new String[3];
                        //name
                        msg[0] = data.getJSONObject("selectedProfile").getString("name");
                        //token
                        msg[1] = data.getString("accessToken");
                        //uuid
                        msg[2] = data.getJSONObject("selectedProfile").getString("id");
                        Message msgs=new Message();
                        msgs.obj = msg;
                        msgs.what = 0;
                        loginHandler.sendMessage(msgs);
                    } catch (Exception e) {
                        Message msgs=new Message();
                        msgs.obj = e.toString();
                        msgs.what = 1;
                        loginHandler.sendMessage(msgs);
                    }
                }
            }).start();

    }
    private Handler checkOrRefreshHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                listener.onSucceed(null, null, null);
            } else if (msg.what == 1) {
                listener.onSucceed(null, (String)msg.obj, null);
            } else if (msg.what == 2) {
                listener.onError((String)msg.obj);
            }
        }};
	public void checkOrRefresh(final String accessToken) {
        new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        JSONObject json=new JSONObject();
                        json.put("accessToken", accessToken);
                        json.put("clientToken", "mio_launcher");
                        if (getJsonData(json, "https://authserver.mojang.com/validate", true).equals("204")) {
                            checkOrRefreshHandler.sendEmptyMessage(0);
                        } else {
                            String str=getJsonData(json, "https://authserver.mojang.com/refresh", false);
                            JSONObject data=new JSONObject(str);
                            Message msg=new Message();
                            msg.what = 1;
                            msg.obj = data.getString("accessToken");
                            checkOrRefreshHandler.sendMessage(msg);
                        }

                    } catch (JSONException e) {
                        Message msg=new Message();
                        msg.what = 2;
                        msg.obj = e.toString();
                        checkOrRefreshHandler.sendMessage(msg);
                    }
                }
            }).start();
    }
    private String getJsonData(JSONObject jsonParam, String urls, boolean nodata) {
        StringBuffer sb=new StringBuffer();
        try {
            // 创建url资源
            URL url = new URL(urls);
            // 建立http连接
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
			SSLContext sc=SSLContext.getInstance("SSL");
			sc.init(null, new TrustManager[] { new TrustAnyTrustManager() },
					new java.security.SecureRandom());

			conn.setSSLSocketFactory(sc.getSocketFactory());
			conn.setHostnameVerifier(new TrustAnyHostnameVerifier());
            // 设置允许输出
            conn.setDoOutput(true);
            // 设置允许输入
            conn.setDoInput(true);
            // 设置不用缓存
            conn.setUseCaches(false);
            // 设置传递方式
            conn.setRequestMethod("POST");
            // 设置维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive");
            // 设置文件字符集:
            conn.setRequestProperty("Charset", "UTF-8");
            // 转换为字节数组
            byte[] data = (jsonParam.toString()).getBytes();
            // 设置文件长度
            conn.setRequestProperty("Content-Length", String.valueOf(data.length));
            // 设置文件类型:
            conn.setRequestProperty("Content-Type", "application/json");
            // 开始连接请求
            conn.connect();        
            OutputStream out = new DataOutputStream(conn.getOutputStream()) ;
            // 写入请求的字符串
            out.write(data);
            out.flush();
            out.close();

            System.out.println(conn.getResponseCode());
			if (nodata) {
				return conn.getResponseCode() + "";
			}
            // 请求返回的状态
            if (HttpsURLConnection.HTTP_OK == conn.getResponseCode()) {
                System.out.println("连接成功");
                // 请求返回的数据
                InputStream in1 = conn.getInputStream();
                try {
                    String readLine=new String();
                    BufferedReader responseReader=new BufferedReader(new InputStreamReader(in1, "UTF-8"));
                    while ((readLine = responseReader.readLine()) != null) {
                        sb.append(readLine).append("\n");
                    }
                    responseReader.close();
                    System.out.println(sb.toString());

                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            } else {
                System.out.println("error++");

            }

        } catch (Exception e) {
            System.out.println(e);
        }

        return sb.toString();

    }

    public static void set(String key, String value) {
        try {
            String str=MioLauncher.getText(new File(MioUtils.getStoragePath(),"/boat/config.txt").getAbsolutePath());
            JSONObject json=new JSONObject(str);
            json.remove(key);
            json.put(key, value);
            FileWriter fr=new FileWriter(new File(MioUtils.getStoragePath() + "/boat/config.txt"));
            fr.write(json.toString());
            fr.flush();
            fr.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    public static String get(String key) {
        try {
            String str=MioLauncher.getText(new File(MioUtils.getStoragePath(),"/boat/config.txt").getAbsolutePath());
            JSONObject json=new JSONObject(str);
            return json.getString(key);
        } catch (Exception e) {
            return e.toString();
        }
    }
    static void setResourcePack(String packName) {
        try {
            BufferedReader bfr=new BufferedReader(new FileReader(MioUtils.getStoragePath() + "/boat/gamedir/options.txt"));
            String temp=null;
            String options="";
            while ((temp = bfr.readLine()) != null) {
                if (temp.indexOf("resourcePacks") != -1 && temp.indexOf("incompatibleResourcePacks") == -1) {
                    temp = "resourcePacks:[\"" + packName + "\"]";
                }
                if (temp.indexOf("incompatibleResourcePacks") != -1) {
                    temp = "incompatibleResourcePacks:[\"" + packName + "\"]";
                }
                options += temp;
                options += "\n";
            }
            System.out.println(options);
            BufferedWriter bfw=new BufferedWriter(new FileWriter(MioUtils.getStoragePath() + "/boat/gamedir/options.txt"));
            bfw.write(options);
            bfw.flush();
            bfw.close();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
	private class TrustAnyTrustManager implements X509TrustManager {

		public void checkClientTrusted(X509Certificate[] chain, String authType)
		throws CertificateException {
		}

		public void checkServerTrusted(X509Certificate[] chain, String authType)
		throws CertificateException {
		}

		public X509Certificate[] getAcceptedIssuers() {
			return new X509Certificate[] {};
		}
	}

	private class TrustAnyHostnameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

}

