package cosine.boat;
import java.util.*;
import com.google.gson.*;
import java.io.*;

public class MinecraftVersion
{
	public class AssetsIndex{
		public String id;
		public String sha1;
		public int size;
		public int totalSize;
		public String url;
	}
	public class Download{
		public String path;
		public String sha1;
		public int size;
		public String url;
	}
	
	public AssetsIndex assetsIndex;
	public String assets;
	
	public HashMap<String, Download> downloads;
	public String id;
	
	public class Library{
		public HashMap<String, Download> downloads;
		public String name;
	}
	
	public Library libraries[];
	
	public String mainClass;
	public String minecraftArguments;
	public int minimumLauncherVersion;
	public String releaseTime;
	public String time;
	public String type;
	
	
	public String jarFile;
	public String jsonFile;
	public static MinecraftVersion getVersion(String dir){
		try
		{
			String jsonFile = new File(dir, dir.substring(dir.lastIndexOf("/") + 1) + ".json").getAbsolutePath();
			String json = new String(Utils.readFile(jsonFile), "UTF-8");
			MinecraftVersion result = new Gson().fromJson(json, MinecraftVersion.class);
			result.jarFile = new File(dir, dir.substring(dir.lastIndexOf("/") + 1) + ".jar").getAbsolutePath();
			result.jsonFile = jsonFile;
			return result;
			
		}
		catch (UnsupportedEncodingException e)
		{
			return null;
		}
	}
	
	public String getClassPath(Config config){
		String cp = "";
		int count = 0;
		for (int i = 0; i < this.libraries.length; i++){
			
			if (this.libraries[i].downloads.get("artifact") != null){
				String p = this.libraries[i].downloads.get("artifact").path;
				if (p != null && !p.equals("")){
					if (count != 0){
						cp = cp + ":";
						
					}
					cp = cp + config.get("game_directory") + "/libraries/" + p;
					
					count++;
				}
			}
			
		}
		return cp;
	}
	
	public String[] getMinecraftArguments(Config config){
		String test = new String(this.minecraftArguments);
		String result = "";
		
		int state = 0;
		int start = 0;
		int stop = 0;
		for (int i = 0; i < test.length(); i++){
			if (state == 0 ){
				if (test.charAt(i) != '$'){
					result = result + test.charAt(i);
					
				}
				else{
					if ( i + 1 < test.length() && test.charAt(i + 1) == '{'){
						state = 1;
						start = i;
					}
					else{
						result = result + test.charAt(i);
					}
					
				}
				continue;
			}
			else{
				if (test.charAt(i) == '}'){
					stop = i;
					
					String key = test.substring(start + 2, stop);
					
					result = result + config.get(key);

					i = stop;
					state = 0;
				}
			}
		}
		return result.split(" ");
	}
}
