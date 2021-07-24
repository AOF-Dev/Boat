package cosine.boat;
import java.util.*;
import com.google.gson.*;
import java.io.*;
import java.lang.reflect.*;
import com.google.gson.reflect.*;

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
	public AssetsIndex assetIndex;
	public String assets;
	
	public HashMap<String, Download> downloads;
	public String id;
	
	public class Library{
		public String name;
		public HashMap<String, Download> downloads;
		
	}
	
	public Library libraries[];
	
	public String mainClass;
	public String minecraftArguments;
	public int minimumLauncherVersion;
	public String releaseTime;
	public String time;
	public String type;
	
	public class Rule {
		String action;
		HashMap<String, Boolean> os;
		HashMap<String, Boolean> features;
	}
	public class RuledArgument extends Argument
	{
		public Rule rules[];
		public String value[];
		@Override
		public String[] getValue(HashMap<String, Boolean> features, HashMap<String, String> os)
		{
			// TODO: Implement this method
			for (Rule r : rules) {
				if (r.features != null) {
					for (Map.Entry<String, Boolean> e : r.features.entrySet()) {
						if (e.getValue() != features.get(e.getKey())) {
							if (r.action != null && r.action.equals("allow")) {
								return null;
							}
						}
					}
					for (Map.Entry<String, Boolean> e : r.os.entrySet()) {
						if (e.getValue() != null && !e.getValue().equals(os.get(e.getKey()))) {
							if (r.action != null && r.action.equals("allow")) {
								return null;
							}
						}
					}
				}
			}
			return value;
		}
		
		
	}
	public class RuledArgumentJsonDeserializer implements JsonDeserializer<RuledArgument> {
		@Override
		public MinecraftVersion.RuledArgument deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
		{
			// TODO: Implement this method
			JsonObject obj = json.getAsJsonObject();
			RuledArgument result = new RuledArgument();
			result.rules = context.deserialize(obj.get("rules"), Rule[].class);
			if (obj.get("value").isJsonPrimitive()) {
				result.value = new String[]{obj.get("value").getAsString()};
			}
			else {
				result.value = context.deserialize(obj.get("value"), String[].class);
			}
			return result;
		}
	}
	
	public class StringArgument extends Argument
	{
		public String value;
		public StringArgument(String v) {
			this.value = v;
		}
		@Override
		public String[] getValue(HashMap<String, Boolean> features, HashMap<String, String> os)
		{
			// TODO: Implement this method
			return new String[] {value};
		}
		
		
	}
	
	public abstract class Argument
	{
		public abstract String[] getValue(HashMap<String, Boolean> features, HashMap<String, String> os);
	}
	public class ArgumentJsonDeserializer implements JsonDeserializer<Argument> {
		@Override
		public MinecraftVersion.Argument deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException
		{
			// TODO: Implement this method
			if (json.isJsonPrimitive()) {
				return new StringArgument(json.getAsString());
			}
			else {
				return context.deserialize(json, RuledArgument.class);
			}
		}
	}
	
	
	public class Arguments {
		public Argument game[];
		public Argument jvm[];
	}
	
	//public Arguments arguments;
	// forge
	public String inheritsFrom;
	
	
	
	
	
	public String minecraftPath;
	
	public static MinecraftVersion fromDirectory(File file){
		try
		{
			
			String json = new String(Utils.readFile(new File(file, file.getName() + ".json")), "UTF-8");
			MinecraftVersion result = new Gson().fromJson(json, MinecraftVersion.class);
			if (new File(file, file.getName() + ".jar").exists()){
				result.minecraftPath = new File(file, file.getName() + ".jar").getAbsolutePath();
			}
			else{
				result.minecraftPath = "";
			}
			
			if (result.inheritsFrom != null && !result.inheritsFrom.equals("") ){
				
				MinecraftVersion self = result;
				result = MinecraftVersion.fromDirectory(new File(file.getParentFile(), self.inheritsFrom));
				
				if (self.assetIndex != null){
					result.assetIndex = self.assetIndex;
				}
				if (self.assets != null && !self.assets.equals("")){
					result.assets = self.assets;
				}
				if (self.downloads != null && !self.downloads.isEmpty()){
					
					if (result.downloads == null){
						result.downloads = new HashMap<String, Download>();
					}
					
					for (Map.Entry<String, Download> e : self.downloads.entrySet()){
						result.downloads.put(e.getKey(), e.getValue());
					}
				}
				if (self.libraries != null && self.libraries.length > 0){
					Library newLibs[] = new Library[result.libraries.length + self.libraries.length];
					int i = 0;
					for (Library lib : self.libraries){
						newLibs[i] = lib;
						i++;
					}
					for (Library lib : result.libraries){
						newLibs[i] = lib;
						i++;
					}
					
					
					result.libraries = newLibs;
				}
				
				if (self.mainClass != null && !self.mainClass.equals("")){
					result.mainClass = self.mainClass;
				}
				if (self.minecraftArguments != null && !self.minecraftArguments.equals("")){
					result.minecraftArguments = self.minecraftArguments;
				}
				
				if (self.minimumLauncherVersion > result.minimumLauncherVersion){
					result.minimumLauncherVersion = self.minimumLauncherVersion;
				}
				if (self.releaseTime != null && !self.releaseTime.equals("")){
					result.releaseTime = self.releaseTime;
				}
				if (self.time != null && !self.time.equals("")){
					result.time = self.time;
				}
				if (self.type != null && !self.type.equals("")){
					result.type = self.type;
				}
				
				if (self.minecraftPath != null && !self.minecraftPath.equals("")){
					result.minecraftPath = self.minecraftPath;
				}
				
			}
			result.minecraftArguments = "--username ${auth_player_name} --version ${version_name} --gameDir ${game_directory} --assetsDir ${assets_root} --assetIndex ${assets_index_name} --uuid ${auth_uuid} --accessToken ${auth_access_token} --userType ${user_type} --versionType ${version_type}";
			return result;
			
		}
		catch (UnsupportedEncodingException e)
		{
			return null;
		}
	}
	
	public String getClassPath(LauncherConfig config){
		String cp = "";
		int count = 0;
		
		String libraries_path = config.get("game_directory") + "/libraries/";
		
		for (Library lib : this.libraries){
			if (lib.name == null || lib.name.equals("")){
				continue;
			}
			
			String names[] = lib.name.split(":");
			String packageName = names[0];
			String mainName = names[1];
			String versionName = names[2];
			
			String path = "";
			
			path = path + libraries_path;
			
			path = path + packageName.replaceAll("\\.", "/");
			
			path = path + "/";
			
			path = path + mainName;
			
			path = path + "/";
			
			path = path + versionName;
			
			path = path + "/" + mainName + "-" + versionName + ".jar";
			
			
			if (count > 0){
				cp = cp + ":";
			}
			cp = cp + path;
			count++;
		}
		if (count > 0){
			cp = ":" + cp;
		}
		cp = minecraftPath + cp;
		
		return cp;
	}
	
	public String[] getMinecraftArguments(LauncherConfig config){
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

					String value = "";

					if (key != null && key.equals("version_name")){
						value = id;
					}
					else if (key != null && key.equals("assets_index_name")){


						if (assetIndex != null){
							value = assetIndex.id;
						}
						else{
							value = assets;
						}

					}

					else{
						value = config.get(key);
					}
					result = result + value;
					i = stop;
					state = 0;
				}
			}
		}
		return result.split(" ");
	
		
	}
	
}
