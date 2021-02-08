package cosine.boat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import com.chenzy.owloading.OWLoadingView;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import com.mio.boat.R;
import com.leon.lfilepickerlibrary.LFilePicker;
import com.mio.boat.MioUtils;

public class MioMod extends Activity {
	ListView mio_left,mio_right;
	MioAdapter_left adapt_left;
	MioAdapter_right adapt_right;
	List<File> list_right,list_left;
    List<String> left_info,right_info;
	TextView text_left,text_right;
	PopupWindow popupWindow;
	View base;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mio_mod);
		mio_left = findViewById(R.id.list_mod_left);
		mio_right = findViewById(R.id.list_mod_right);
		list_left = new ArrayList<File>();
		list_right = new ArrayList<File>();
        left_info=new ArrayList<String>();
        right_info=new ArrayList<String>();
		adapt_left = new MioAdapter_left(this, list_left,list_right);
		mio_left.setAdapter(adapt_left);
		adapt_right =adapt_left.getRight();
		mio_right.setAdapter(adapt_right);
		text_left=findViewById(R.id.miomodTextViewleft);
		text_right=findViewById(R.id.miomodTextViewright);
		if(new File(MioUtils.getExternalFilesDir(MioMod.this)+"/澪").exists()){
			System.out.println( new File(MioUtils.getExternalFilesDir(this)+"/澪/MOD").mkdir());
		}else{
			new File(MioUtils.getExternalFilesDir(MioMod.this)+"/澪").mkdir();
			System.out.println( new File(MioUtils.getExternalFilesDir(MioMod.this)+"/澪/MOD").mkdir());
		}
		
		base = LayoutInflater.from(MioMod.this).inflate(R.layout.loading,null);
		OWLoadingView ow=base.findViewById(R.id.ow);
        ImageButton exit=base.findViewById(R.id.loadingImageButtonExit);
        exit.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    win=false;
                    popupWindow.dismiss();
                }
            });
		popupWindow = new PopupWindow();
		popupWindow.setWidth(LayoutParams.FILL_PARENT);
		popupWindow.setHeight(LayoutParams.FILL_PARENT);
		popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		popupWindow.setFocusable(true);
		popupWindow.setOutsideTouchable(false);
		popupWindow.setContentView(base);
		ow.setAutoStartAnim(true);
		new Handler().postDelayed(new Runnable(){
                @Override
                public void run() {
                    new Thread(new Runnable(){
                            @Override
                            public void run() {
                                getFirst(new File(MioUtils.getStoragePath()+"/Download"));
                                getFirst(new File(MioUtils.getStoragePath()+"/Android/data/com.tencent.mobileqq/Tencent/QQfile_recv/"));
                                getFirst(new File(MioUtils.getStoragePath()+"/Android/data/cosine.boat/澪/MOD"));
                                getFileName_right(new File(MioUtils.getStoragePath()));
                            }
                        }).start();
                    new Thread(new Runnable(){
                            @Override
                            public void run() {
                                getFileName_left(new File(MioLogin.get("game_directory")+"/mods"));
                            }
                        }).start();
                }
            }, 500);
	}
	boolean win=true;
	@Override
	public void onWindowFocusChanged(boolean hasFocus)
	{
		// TODO: Implement this method
		super.onWindowFocusChanged(hasFocus);
		if (hasFocus&&win){
			popupWindow.showAtLocation(MioMod.this.getWindow().getDecorView(),Gravity.TOP|Gravity.LEFT,0,0);	
		}

	}
    public class MioAdapter_left extends BaseAdapter {

		private Context context;
		public List<File> list;
		private MioAdapter_right right;
		public MioAdapter_left(Context context, List<File> list,List<File> list_r) {
			this.context = context;
			this.list = list;
			right=new MioAdapter_right(context,list_r,this);
		}
		public MioAdapter_right getRight(){
			return right;
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		Button del;
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_list_left, parent, false);
			TextView appNameTextView = convertView.findViewById(R.id.item_mod_name_left);
			appNameTextView.setText(MioMod.replace(list.get(position).getName()));
            TextView infoText= convertView.findViewById(R.id.item_mod_info_left);
            infoText.setText(left_info.get(position));
			del=convertView.findViewById(R.id.item_mod_del);
			del.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View p1) {
						try{
                            fileCopy(list.get(position),MioUtils.getExternalFilesDir(MioMod.this)+"/澪/MOD/");
                            right_info.add(left_info.get(position));
                            left_info.remove(position);
                            right.list.add(new File(MioUtils.getExternalFilesDir(MioMod.this)+"/澪/MOD/"+list.get(position).getName()));
                            right.notifyDataSetChanged();
                            list.remove(position);
                            MioAdapter_left.this.notifyDataSetChanged();

                        }catch(Exception e){

						}

					}
				});
			return convertView;
		}
	}
	public class MioAdapter_right extends BaseAdapter {

		private Context context;
		public List<File> list;
		private MioAdapter_left left;
		public MioAdapter_right(Context context, List<File> list,MioAdapter_left left) {
			this.context = context;
			this.list = list;
			this.left=left;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_list_right, parent, false);
			TextView appNameTextView = convertView.findViewById(R.id.item_mod_name_right);
			appNameTextView.setText(MioMod.replace(list.get(position).getName().trim()));
            TextView infoText= convertView.findViewById(R.id.item_mod_info_right);
            infoText.setText(right_info.get(position));
			Button add=convertView.findViewById(R.id.item_mod_add);
			add.setOnClickListener(new OnClickListener(){
					@Override
					public void onClick(View p1) {
						try{
							fileCopy(list.get(position),MioLogin.get("game_directory")+"/mods/");
                            left_info.add(right_info.get(position));
                            right_info.remove(position);
							left.list.add(new File(MioLogin.get("game_directory")+"/mods/"+list.get(position).getName()));
							left.notifyDataSetChanged();
							list.remove(position);
							MioAdapter_right.this.notifyDataSetChanged();
							
						}catch(Exception e){
							
						}

					}
				});

			return convertView;
		}
        
	}
	Handler handler=new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == 0) {
                String[] ss=MioMod.getModInfo(((File)msg.obj).getAbsolutePath());
                if(ss!=null){
                    if(ss.length==2){
                        String info="Mod名:"+ss[0]+"  版本:"+ss[1];
                        right_info.add(info);
                    }
                }else{
                    right_info.add(" ");
                }
                list_right.add((File)msg.obj);
				adapt_right.notifyDataSetChanged();
				win=false;
			    new Handler().postDelayed(new Runnable(){
						@Override
						public void run() {
							popupWindow.dismiss();
						}
					}, 1000);
			}
			if (msg.what == 1) {
                
                String[] ss=MioMod.getModInfo(((File)msg.obj).getAbsolutePath());
                if(ss!=null){
                    if(ss.length==2){
                        String info="Mod名:"+ss[0]+"  版本:"+ss[1];
                        left_info.add(info);
                    }
                }else{
                    left_info.add(" ");
                }
                list_left.add((File)msg.obj);
				adapt_left.notifyDataSetChanged();
			}
		}};
	/**
	 * 递归扫描文件
	 */
	public void getFileName_right(File file) {

		File[] listFiles = file.listFiles();

		if (listFiles != null && listFiles.length > 0) {

			for (File file2 : listFiles) {

				if (file2.isDirectory() && !(file2.getName().indexOf("boat") != -1) && !file2.getName().equals("AppProjects")&& !file2.getName().equals("Download")&&file2.getAbsolutePath().indexOf("com.tencent.mobileqq")==-1&&!file2.getName().equals("MC")) {
					getFileName_right(new File(file2.getAbsolutePath()));
				}

				if (file2.isFile()) {

					String path = file2.getAbsolutePath();

					if (path.endsWith(".jar")) {
                        try{
                            InputStream in=new BufferedInputStream(new FileInputStream(file2.getAbsolutePath()));
                            ZipInputStream zin=new ZipInputStream(in);
                            //ZipEntry 类用于表示 ZIP 文件条目。
                            ZipEntry ze;
                            while((ze=zin.getNextEntry())!=null){
                                if(ze.isDirectory()){
                                    //为空的文件夹什么都不做
                                }else{
                                    InputStream ins=new ZipFile(path).getInputStream(ze);
                                    InputStreamReader inr=new InputStreamReader(ins, "utf-8");
                                    
                                    char[] b=new char[ins.available()];
                                    inr.read(b);
                                    inr.close();
                                    ins.close();
                                    String str=new String(b);
                                    if(str.indexOf("minecraft")!=-1||str.indexOf("Minecraft")!=-1){
                                        Message msg=new Message();
                                        msg.obj=file2;
                                        msg.what=0;
                                        handler.sendMessage(msg);
                                        break;
                                    }
                                }
                            }
                        }catch(Exception e){

                        }
						
                        
					}
				}
			}
		}

	}
    public void getFirst(File file) {

        File[] listFiles = file.listFiles();

        if (listFiles != null && listFiles.length > 0) {

            for (File file2 : listFiles) {

                if (file2.isDirectory()) {
                    getFileName_right(new File(file2.getAbsolutePath()));
                }

                if (file2.isFile()) {

                    String path = file2.getAbsolutePath();

                    if (path.endsWith(".jar")) {
                        try{
                            InputStream in=new BufferedInputStream(new FileInputStream(file2.getAbsolutePath()));
                            ZipInputStream zin=new ZipInputStream(in);
                            //ZipEntry 类用于表示 ZIP 文件条目。
                            ZipEntry ze;
                            while((ze=zin.getNextEntry())!=null){
                                if(ze.isDirectory()){
                                    //为空的文件夹什么都不做
                                }else{
                                    InputStream ins=new ZipFile(path).getInputStream(ze);
                                    InputStreamReader inr=new InputStreamReader(ins, "utf-8");
                                    char[] b=new char[ins.available()];
                                    inr.read(b);
                                    inr.close();
                                    ins.close();
                                    String str=new String(b);
                                    if(str.indexOf("minecraft")!=-1||str.indexOf("Minecraft")!=-1){
                                        Message msg=new Message();
                                        msg.obj=file2;
                                        msg.what=0;
                                        handler.sendMessage(msg);
                                        break;
                                    }
                                }
                            }
                        }catch(Exception e){

                        }
                    }
                }
            }
        }

	}
    public static String[] getModInfo(String path){
        String[] infos=new String[2];
        try{
            InputStream in=new BufferedInputStream(new FileInputStream(path));
            ZipInputStream zin=new ZipInputStream(in);
            ZipFile zf=new ZipFile(path);
            //ZipEntry 类用于表示 ZIP 文件条目。
            ZipEntry ze;
            while((ze=zin.getNextEntry())!=null){
                if(ze.getName().equals("mcmod.info")){

                    BufferedReader br =new BufferedReader(new InputStreamReader(zf.getInputStream(ze)));
                    String line="";
                    String str="";
                    while ((line =br.readLine()) !=null) {
                        str+=line;
                    }
                    JSONObject jsonobj=new JSONObject(new JSONArray(str).get(0).toString());
                    System.out.println(jsonobj.toString());
                    infos[0]=jsonobj.getString("name");
                    infos[1]=jsonobj.getString("mcversion");
                    return infos;
                }

            }
        }catch(Exception e){
            return null;
        }
        return null;
	}
	public void fileCopy(File f,String target){
		try{
			FileInputStream in=new FileInputStream(f);
			FileOutputStream out=new FileOutputStream(target+f.getName());
			byte[] flush = new byte[1024*4];
            int len = -1;
            while ((len = in.read(flush)) != -1) {
                out.write(flush, 0, len);
            }
            out.flush();
            out.close();
            in.close();
			f.delete();
		}catch(IOException e){
			Toast.makeText(MioMod.this,e.toString(),1000).show();
		}
	}
	public void getFileName_left(File file) {

		File[] listFiles = file.listFiles();

		if (listFiles != null && listFiles.length > 0) {

			for (File file2 : listFiles) {

				if (file2.isDirectory()) {
					getFileName_left(new File(file2.getAbsolutePath()));
				}

				if (file2.isFile()) {

					String path = file2.getAbsolutePath();

					if (path.endsWith(".jar")) {
						Message msg=new Message();
                        msg.obj=file2;
                        msg.what=1;
                        handler.sendMessage(msg);
					}
				}
			}
		}

	}
	public static String replace(String str) {
		String destination = "";
		if (str!=null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			destination = m.replaceAll("");
		}
		return destination;
	}
	public void oc_select(View v){
        Toast.makeText(this,"请手动选择需要导入的文件",1000).show();
        new LFilePicker()
            .withActivity(MioMod.this)
            .withRequestCode(233)
            .withStartPath("/storage/emulated/0/")
            .withFileFilter(new String[]{".jar"})
            .start();
    }
    String path;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            path= null;
            if(requestCode==233){
                path=data.getStringArrayListExtra("paths").get(0);
                if(path.endsWith(".jar")){
                    AlertDialog dialog=new AlertDialog.Builder(this)
                        .setTitle("提示")
                        .setMessage("已选择文件："+path+"。是否导入?")
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dia, int which) {
                                win=true;
                                popupWindow.showAtLocation(MioMod.this.getWindow().getDecorView(),Gravity.TOP|Gravity.LEFT,0,0);
                                
                                if(MioUtils.moveFile(path,MioLogin.get("game_directory")+"/mods")){
                                    popupWindow.dismiss();
                                    win=false;
                                    left_info.add("");
                                    list_left.add(new File(MioLogin.get("game_directory")+"/mods/"+new File(path).getName()));
                                    adapt_left.notifyDataSetChanged();
                                    for(File f:list_right){
                                        if(f.getName().equals(new File(path).getName())){
                                            right_info.remove(list_right.indexOf(f));
                                            list_right.remove(f);
                                            adapt_right.notifyDataSetChanged();
                                        }
                                    }
                                    Toast.makeText(MioMod.this, "导入完成。", 1000).show();
                                }else{
                                    Toast.makeText(MioMod.this,"出现了未知错误", 1000).show();
                                    win=false;
                                    popupWindow.dismiss();
                                }
                            }
                        })
                        .setNegativeButton("否", null)
                        .create();
                    dialog.show();
                }else{
                    Toast.makeText(this,"请选择jar格式的文件，其他格式暂不支持",1000).show();
                }

            }
        }else{
            Toast.makeText(this,"未选择文件",1000).show();
        }
    }
}
