package com.mio.boat;
import android.widget.BaseAdapter;
import android.view.ViewGroup;
import android.view.View;
import android.view.LayoutInflater;
import android.content.Context;
import java.util.List;
import android.widget.TextView;
import android.widget.RadioButton;
import android.widget.ImageButton;
import android.widget.CompoundButton;
import android.view.View.OnClickListener;
import android.content.SharedPreferences;
import org.json.JSONObject;
import org.json.JSONException;
import cosine.boat.MioLogin;
import android.widget.Toast;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;

public class AdapterListUser extends BaseAdapter {

    private Context context;
    private List<UserBean> list;
    private int selected;
    //数据储存
    private SharedPreferences msh;
    private SharedPreferences.Editor mshe;
    
    public AdapterListUser(Context context, List<UserBean> list) {
        this.context = context;
        this.list=list;
        //初始化数据储存
        msh = context.getSharedPreferences("Mio", context.MODE_PRIVATE);
        mshe = msh.edit();
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
        convertView = LayoutInflater.from(context).inflate(R.layout.item_listview_user, parent, false);
        TextView name=convertView.findViewById(R.id.item_listview_user_name);
        TextView state=convertView.findViewById(R.id.item_listview_user_state);
        RadioButton selector=convertView.findViewById(R.id.item_listview_user_select);
        ImageButton remove=convertView.findViewById(R.id.item_listview_user_remove);
        final UserBean bean= list.get(position);
        name.setText(bean.getUserName());
        state.setText(bean.getUserState());
        if(bean.isSelected()){
            selector.setChecked(true);
            selected=position;
            MioLogin.set("auth_player_name", bean.getUserName());
            if(bean.getUserState().equals("正版登录")){
                new MioLogin(new MioLogin.LoginListener(){
                        @Override
                        public void onSucceed(String name, String token, String uuid) {
                            if(token==null){
                                MioLogin.set("auth_access_token",bean.getToken());
                            }else{
                                MioLogin.set("auth_access_token",token);
                                try {
                                    JSONObject json=new JSONObject(msh.getString("users", ""));
                                    JSONObject json2= json.getJSONObject(list.get(position).getUserName());
                                    json2.put("token", token);
                                    json.put(list.get(position).getUserName(), json2);
                                    mshe.putString("users",json.toString());
                                    mshe.commit();
                                } catch (JSONException e) {

                                }
                            }
                        }

                        @Override
                        public void onError(String error) {
                            Toast.makeText(context,error,1000).show();
                            AlertDialog dialog=new AlertDialog.Builder(context)
                                .setTitle("提示")
                                .setMessage("Token刷新失败，是否再次登录？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dia, int which) {
                                        Toast.makeText(context,"登录中...",1000).show();
                                        new MioLogin(new MioLogin.LoginListener(){
                                                @Override
                                                public void onSucceed(String name, String token, String uuid) {
                                                    if(token==null){
                                                        MioLogin.set("auth_access_token",bean.getToken());
                                                    }else{
                                                        MioLogin.set("auth_access_token",token);
                                                    }
                                                    Toast.makeText(context,"登录成功",1000).show();
                                                }

                                                @Override
                                                public void onError(String error) {
                                                    Toast.makeText(context,error,1000).show();
                                                }
                                            }).login(bean.getUserAccount(),bean.getUserPass());
                                    }
                                })
                                .setNegativeButton("取消", null)
                                .create();
                            dialog.show();
                        }
                    }).checkOrRefresh(bean.getToken());
            }
        }
        selector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
                @Override
                public void onCheckedChanged(CompoundButton p1, boolean p2) {
                    try {
                        JSONObject json=new JSONObject(msh.getString("users", ""));
                        JSONObject json2= json.getJSONObject(list.get(selected).getUserName());
                        json2.put("isSelected",false);
                        json.put(list.get(selected).getUserName(),json2);
                        JSONObject json3=json.getJSONObject(list.get(position).getUserName());
                        json3.put("isSelected",true);
                        json.put(list.get(position).getUserName(),json3);
                        if(list.get(position).getUserState().equals("正版登录")){
                            new MioLogin(new MioLogin.LoginListener(){
                                    @Override
                                    public void onSucceed(String name, String token, String uuid) {
                                        if(token==null){
                                            MioLogin.set("auth_access_token",list.get(position).getToken());
                                        }else{
                                            MioLogin.set("auth_access_token",token);
                                            try {
                                                JSONObject json=new JSONObject(msh.getString("users", ""));
                                                JSONObject json2= json.getJSONObject(list.get(position).getUserName());
                                                json2.put("token", token);
                                                json.put(list.get(position).getUserName(), json2);
                                                mshe.putString("users",json.toString());
                                                mshe.commit();
                                            } catch (JSONException e) {
                                                
                                            }
                                        }
                                    }

                                    @Override
                                    public void onError(String error) {
                                        Toast.makeText(context,error,1000).show();
                                        AlertDialog dialog=new AlertDialog.Builder(context)
                                            .setTitle("提示")
                                            .setMessage("Token刷新失败，是否再次登录？")
                                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dia, int which) {
                                                    Toast.makeText(context,"登录中...",1000).show();
                                                    new MioLogin(new MioLogin.LoginListener(){
                                                            @Override
                                                            public void onSucceed(String name, String token, String uuid) {
                                                                if(token==null){
                                                                    MioLogin.set("auth_access_token",bean.getToken());
                                                                }else{
                                                                    MioLogin.set("auth_access_token",token);
                                                                }
                                                                Toast.makeText(context,"登录成功",1000).show();
                                                            }

                                                            @Override
                                                            public void onError(String error) {
                                                                Toast.makeText(context,error,1000).show();
                                                            }
                                                        }).login(bean.getUserAccount(),bean.getUserPass());
                                                }
                                            })
                                            .setNegativeButton("取消", null)
                                            .create();
                                        dialog.show();
                                    }
                                }).checkOrRefresh(list.get(position).getToken());
                            MioLogin.set("auth_uuid", list.get(position).getUuid());
                        }
                        MioLogin.set("auth_player_name", list.get(position).getUserName());
                        list.get(selected).setIsSelected(false);
                        list.get(position).setIsSelected(true);
                        AdapterListUser.this.notifyDataSetChanged();
                        selected=position;
                        mshe.putString("users",json.toString());
                        mshe.commit();
                    } catch (JSONException e) {
                        
                    }
                }
            });
        remove.setOnClickListener(new OnClickListener(){
                @Override
                public void onClick(View p1) {
                    try {
                        JSONObject json=new JSONObject(msh.getString("users", ""));
                        json.remove(list.get(position).getUserName());
                        mshe.putString("users",json.toString());
                        mshe.commit();
                        list.remove(position);
                        AdapterListUser.this.notifyDataSetChanged();
                    } catch (JSONException e) {
                        
                    }
                }
            });
        return convertView;
    }
    
    
}
