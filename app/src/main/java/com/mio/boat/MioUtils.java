package com.mio.boat;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class MioUtils 
{
    public interface ZipListener{
        void onStart();
        void onProgress(int progress);
        void onFinish();
        void onError(String err);
    }
    ZipListener zipListener;
    public MioUtils(ZipListener zipListener){
        this.zipListener=zipListener;
    }
    private Handler zipHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                zipListener.onStart();
            }else if(msg.what==1){
                zipListener.onProgress((int)msg.obj);
            }else if(msg.what==2){
                zipListener.onFinish();
            }else if(msg.what==3){
                zipListener.onError((String)msg.obj);
            }
        }};
    public void unzipWithProgress(final String srcPath,final String destPath){
        new Thread(new Runnable(){
                @Override
                public void run() {
                    try {
                        zipHandler.sendEmptyMessage(0);
                        if (!new File(srcPath).exists()) {
                            zipListener.onError("文件不存在");
                            return ;
                        }
                        FileInputStream in=new FileInputStream(srcPath);
                        ZipInputStream zin=new ZipInputStream(in);
                        ZipEntry ze;
                        long zipCurrentLen = 0;
                        long zipLen=getZipFileSize(srcPath);
                        while ((ze = zin.getNextEntry()) != null) {
                            if (ze.isDirectory()) {
                                new File(destPath, ze.getName()).mkdirs();
                            } else {
                                File f=new File(destPath, ze.getName());
                                f.createNewFile();
                                FileOutputStream out=new FileOutputStream(f);
                                int len;
                                byte[] buf=new byte[2048];
                                while ((len = zin.read(buf)) != -1) {
                                    zipCurrentLen += len;
                                    updateProgress((int)(zipCurrentLen * 100 / zipLen));
                                    out.write(buf, 0, len);
                                    out.flush();
                                }
                                out.close();
                            }
                        }
                        zin.closeEntry();
                        zin.close();
                        in.close();
                        zipHandler.sendEmptyMessage(2);
                    } catch (IOException e) {
                        Message msg=new Message();
                        msg.what=3;
                        msg.obj=e.toString();
                        zipHandler.sendMessage(msg);
                    }
                }
            }).start();
        
    }
    int lastProgress=0;
    private void updateProgress(int progress){
        if(progress>lastProgress){
            lastProgress=progress;
            Message msg=new Message();
            msg.what=1;
            msg.obj=progress;
            zipHandler.sendMessage(msg);
        }
    }
    public static boolean moveFile(String src, String dest) {
        File fs=new File(src);
        if (!fs.exists()) {
            return false;
        }
        if (fs.isDirectory()) {
            File fss=new File(dest, fs.getName());
            fss.mkdir();
            for (File f:fs.listFiles()) {
                moveFile(f.getAbsolutePath(), dest + fss.getName());
            }
        } else {
            fs.renameTo(new File(dest, fs.getName()));
            return true;
        }
        fs.renameTo(new File(dest, fs.getName()));
        return true;
	}
    public static boolean unzip(String srcPath,String destPath) throws Exception{
        if(!new File(srcPath).exists()){
            return false;
        }
        FileInputStream in=new FileInputStream(srcPath);
        ZipInputStream zin=new ZipInputStream(in);
        ZipEntry ze;
        long zipCurrentLen = 0;
        long zipLen=getZipFileSize(srcPath);
        while((ze=zin.getNextEntry())!=null){
            if(ze.isDirectory()){
                new File(destPath,ze.getName()).mkdirs();
            }else{
                File f=new File(destPath,ze.getName());
                f.createNewFile();
                FileOutputStream out=new FileOutputStream(f);
                int len;
                byte[] buf=new byte[2048];
                while((len=zin.read(buf))!=-1){
                    zipCurrentLen+=len;
                    System.out.println((zipCurrentLen*100/zipLen)+"%");
                    out.write(buf,0,len);
                    out.flush();
                }
                out.close();
            }
        }
        zin.closeEntry();
        zin.close();
        in.close();
        return true;
    }
    private static long getZipFileSize(String filePath) { 
        long size = 0; 
        ZipFile f; 
        try { 
            f = new ZipFile(filePath); 
            Enumeration<? extends ZipEntry> en = f.entries(); 
            while (en.hasMoreElements()) { 
                size += en.nextElement().getSize(); 
            } 
        } catch (IOException e) { 
            e.printStackTrace(); 
        } 
        return size; 
    } 
    public static boolean writeTxt(String str,String filePath){
        try {
            BufferedWriter bfw=new BufferedWriter(new FileWriter(filePath));
            bfw.write(str);
            bfw.close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    public static String readTxt(String filePath){
        String str="";
        try {
            BufferedReader bfr=new BufferedReader(new FileReader(filePath));
            String temp;
            while((temp=bfr.readLine())!=null){
                str+=temp+"\n";
            }
        } catch (IOException e) {
            str+= "错误："+e.toString();
        }
        return str;
    }
    public static String getStoragePath(){
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }
    public static String getExternalFilesDir(Context context){
        return context.getExternalFilesDir(null).getParentFile().getAbsolutePath();
    }
    public static void copyFromAssets(Context context, String source,
                                      String destination) throws IOException {
        /**
         * 获取assets目录下文件的输入流
         *
         * 1、获取AssetsManager : 调用 Context 上下文对象的 context.getAssets() 即可获取 AssetsManager对象;
         * 2、获取输入流 : 调用 AssetsManager 的 open(String fileName) 即可获取对应文件名的输入流;
         */
        InputStream is = context.getAssets().open(source);
        // 获取文件大小
        int size = is.available();
        // 创建文件的缓冲区
        byte[] buffer = new byte[size];
        // 将文件读取到缓冲区中
        is.read(buffer);
        // 关闭输入流
        is.close();
        /*
         *  打开app安装目录文件的输出流
         *  Context.MODE_PRIVATE  设置该文件只能被本应用使用，为私有数据
         */
        FileOutputStream output = context.openFileOutput(destination,
                                                         Context.MODE_PRIVATE);
        // 将文件从缓冲区中写出到内存中
        output.write(buffer);
        //关闭输出流
        output.close();
    }
    public static boolean deleteFile(String src) {
        File fs=new File(src);
        if (!fs.exists()) {
            return false;
        }
        if (fs.isDirectory()) {
            for (File f:fs.listFiles()) {
                deleteFile(f.getAbsolutePath());
            }
        } else {
            fs.delete();
            return true;
        }
        fs.delete();
        return true;
	}
    public static boolean dirCopy(String srcPath, String destPath) {
        File src = new File(srcPath);
        if (!new File(destPath).exists()) {
            new File(destPath).mkdirs();
        }
        for (File s : src.listFiles()) {
            if (s.isFile()) {
                fileCopy(s.getPath(), destPath + File.separator + s.getName());
            } else {
                dirCopy(s.getPath(), destPath + File.separator + s.getName());
            }
        }
        return true;
    }

    public static boolean fileCopy(String srcPath, String destPath) {
        File src = new File(srcPath);
        File dest = new File(destPath);
        try
        {
            InputStream is = new BufferedInputStream(new FileInputStream(src));
            OutputStream out =new BufferedOutputStream(new FileOutputStream(dest));
            byte[] flush = new byte[2*1024];
            int len = -1;
            while ((len = is.read(flush)) != -1) {
                out.write(flush, 0, len);
            }
            out.flush();
            out.close();
            is.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
