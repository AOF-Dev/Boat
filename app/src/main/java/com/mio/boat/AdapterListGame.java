package com.mio.boat;
import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import com.mio.boat.R;
import java.util.List;
import org.json.JSONException;
import java.io.File;
import cosine.boat.MioLogin;
import java.util.Map;
import android.app.AlertDialog;
import android.content.DialogInterface;
import java.util.ArrayList;

public class AdapterListGame extends BaseAdapter {

    private MioLauncher context;
    private List<String> list_name;
    private List<File> list_file;
    private String path;
    //数据储存
    private SharedPreferences msh;
    private SharedPreferences.Editor mshe;
    private List<RadioButton> radios;
    public AdapterListGame(MioLauncher context, List<String> list_name,List<File> list_file) {
        this.context = context;
        this.list_name=list_name;
        this.list_file=list_file;
        path=MioLogin.get("game_directory");
        //初始化数据储存
        msh = context.getSharedPreferences("Mio", context.MODE_PRIVATE);
        mshe = msh.edit();
        
        radios=new ArrayList<RadioButton>();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return list_name.size();
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
        convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_game, parent, false);
        TextView name=convertView.findViewById(R.id.item_listview_game_name);
        name.setText(list_name.get(position));
        RadioButton selector=convertView.findViewById(R.id.item_listview_game_select);
        radios.add(selector);
        ImageButton remove=convertView.findViewById(R.id.item_listview_game_remove);
        selector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton p1, boolean p2) {
                    if(p2){
                        setPath(list_file.get(position));
                        for(RadioButton r:radios){
                            if(r.isChecked()){
                                r.setChecked(false);
                            }
                        }
                        p1.setChecked(true);
                    }
                }
            });
        if(path.contains("/sdcard")){
            if(path.replace("/sdcard",MioUtils.getStoragePath()).equals(list_file.get(position).getAbsolutePath())){
                selector.setChecked(true);
                
            }
        }else{
            if(path.equals(list_file.get(position).getAbsolutePath())){
                selector.setChecked(true);
                
            }
        }
        
        remove.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    AlertDialog dialog=new AlertDialog.Builder(context)
                        .setTitle("提示")
                        .setMessage("确定要删除："+list_name.get(position)+"吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dia, int which) {
                                if(!list_name.get(position).equals("boat")){
                                    MioUtils.deleteFile(new File(MioUtils.getExternalFilesDir(context),"澪/MC/"+list_name.get(position)).getAbsolutePath());
                                    list_name.remove(position);
                                    list_file.remove(position);
                                    AdapterListGame.this.notifyDataSetChanged();
                                }
                                
                            }
                        })
                        .setNegativeButton("取消", null)
                        .create();
					dialog.show();
                }
            });
        return convertView;
    }
    private void setPath(File fpath) {
        MioLogin.set("home", fpath.getParent());
        MioLogin.set("game_assets", new File(fpath , "/assets/virtual/legacy").getAbsolutePath());
        MioLogin.set("game_directory", fpath.getAbsolutePath());
        MioLogin.set("assets_root", new File(fpath, "/assets").getAbsolutePath());
	}

}
