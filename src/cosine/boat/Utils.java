package cosine.boat;

import java.io.*;
import java.util.*;

public final class Utils {
	
	
	public static File createFile(String filePath){
		File file = new File(filePath);
		if (!file.exists()){
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
		}
		return file;
	}
	
	public static byte[] readFile(String filePath){
		FileInputStream fis = null;
		try{
			File file = new File(filePath);
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
	
	
}
