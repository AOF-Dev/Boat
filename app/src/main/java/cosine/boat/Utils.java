package cosine.boat;

import java.io.*;
import java.util.*;
import android.content.res.*;
import java.security.MessageDigest;
import android.content.pm.PackageInfo;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.Signature;

public final class Utils {
	
	
	public static File createFile(String filePath){
		File file = new File(filePath);
		if (file.exists()){
			file.delete();
		}
		file.getParentFile().mkdirs();

		try
		{
			file.createNewFile();
		}
		catch (IOException e)
		{
			e.printStackTrace();
			return null;
		}
		return file;
	}
	
	public static byte[] readFile(String filePath){
		return Utils.readFile(new File(filePath));
	}
	public static byte[] readFile(File file){
		FileInputStream fis = null;
		try{
			
			fis=new FileInputStream(file);
			byte b[]=new byte[(int)file.length()];
			fis.read(b);
			fis.close();
			return b;
		}catch(Exception e){

			e.printStackTrace();
		}
		finally{
			if (fis != null){
				try
				{
					fis.close();
				}
				catch (IOException e)
				{}
			}
		}
		return null;
	}
	
	
	
	public static boolean writeFile(String filePath, byte[] arys){
		
		File file = Utils.createFile(filePath);
		
		if (file == null){
			return false;
		}
		FileOutputStream fos = null;
		try{
			fos = new FileOutputStream(file);
			fos.write(arys);
			fos.flush();
			fos.close();
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if (fos != null){
				try
				{
					fos.close();
				}
				catch (IOException e)
				{}
			}
		}
		return false;
	}
	public static boolean writeFile(String filePath, String str){

		try
		{
			return Utils.writeFile(filePath, str.getBytes("UTF-8"));
		}
		catch (UnsupportedEncodingException e)
		{
			return false;
		}
	}
	public static boolean writeFile(String filePath,String strings[]){
		
		File file = Utils.createFile(filePath);
		if (file == null){
			return false;
		}
		FileOutputStream fos = null;
		try{
			fos = new FileOutputStream(file);
			String mainString = new String();
			for (String s :strings){
				s = s + "\n";
				mainString = mainString + s;
			}
			fos.write(mainString.getBytes());
			fos.flush();
			fos.close();
			return true;
		}catch(Exception e){
			e.printStackTrace();
		}
		finally{
			if (fos != null){
				try
				{
					fos.close();
				}
				catch (IOException e)
				{}
			}
		}
		return false;
	}
	public static boolean hasFile(String filePath){
		return new File(filePath).exists();
		
	}
	
	
	public static boolean extractAsset(AssetManager am, String src, String tgt ){
		FileOutputStream fos = null;
		InputStream is = null;
		
		try
		{
			File target = new File(tgt);
			if (!target.exists()) {
				target.createNewFile();
			}
			fos = new FileOutputStream(target);
			
			is = am.open(src);
			byte[] buf = new byte[1024];
			int count = 0;
			
			while ((count = is.read(buf)) != -1) {
				fos.write(buf, 0, count);
			}

			fos.flush();
			fos.close();
			is.close();
			return true;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			try
			{
				if (is != null){
					is.close();
				}
				if (fos != null){
					fos.close();
				}
			}
			catch (IOException s)
			{}
			
			
			return false;
		}
	

	
	}
	/**
     * MD5加密
     * @param byteStr 需要加密的内容
     * @return 返回 byteStr的md5值
     */
    public static String encryptionMD5(byte[] byteStr) {
        MessageDigest messageDigest = null;
        StringBuffer md5StrBuff = new StringBuffer();
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(byteStr);
            byte[] byteArray = messageDigest.digest();
            for (int i = 0; i < byteArray.length; i++) {
                if (Integer.toHexString(0xFF & byteArray[i]).length() == 1) {
                    md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
                } else {
                    md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return md5StrBuff.toString();
    }

    /**
     * 获取app签名md5值
     */
    public static String getSignMd5Str(Context mActivity) {
        try {
            PackageInfo packageInfo = mActivity.getPackageManager().getPackageInfo(mActivity.getPackageName(), PackageManager.GET_SIGNATURES);
            Signature[] signs = packageInfo.signatures;
            Signature sign = signs[0];
            String signStr = encryptionMD5(sign.toByteArray());
            return signStr;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
    
}

