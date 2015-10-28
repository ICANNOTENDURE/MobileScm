package com.dhcc.scm.adapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dhcc.scm.R;
  
public class JSONArrayAdapter extends BaseAdapter{  
  
    private Context ctx;  
    //������Ҫ��װ��JSONArray����  
    private JSONArray jsonArray;  
    //�����б���ʾJSONOBject������Ǹ�����  
    private String property;  
    private boolean hasIcon;  
      
    public JSONArrayAdapter(Context ctx,JSONArray jsonArray,String property,boolean hasIcon) {  
        // TODO Auto-generated constructor stub  
      this.ctx = ctx;  
      this.jsonArray = jsonArray;  
      this.property = property;  
      this.hasIcon  = hasIcon;  
      
    }  
  
    @Override  
    public int getCount() {  
        // TODO Auto-generated method stub  
        return jsonArray.length();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        // TODO Auto-generated method stub  
        return jsonArray.optJSONObject(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        // TODO Auto-generated method stub  
      
        try{  
              
            return ((JSONObject)getItem(position)).getInt("id");  
        }catch(JSONException e){  
            e.printStackTrace();  
        }  
        return 0;  
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
        // TODO Auto-generated method stub  
          
        //����һ�����Բ��ֹ�����  
        LinearLayout linear = new LinearLayout(ctx);  
        //����Ϊˮƽ�����Բ��ֹ�����  
        linear.setOrientation(0);  
        //����һ��ImageView  
        ImageView iv = new ImageView(ctx);  
        iv.setPadding(10, 0, 20, 0);  
        iv.setImageResource(R.drawable.ic_launcher);  
        linear.addView(iv);  
        //����һ��TextView  
        TextView tv = new TextView(ctx);  
          
        try{  
  
                //��ȡJSONArray����Ԫ�ص�property����  
                String itemName = ((JSONObject)getItem(position)).getString(property);  
                //������ʾ��Textview����ʾ������  
                tv.setText(itemName);  
              
              
        }catch(JSONException e){  
            e.printStackTrace();  
        }  
          
        tv.setTextSize(20);  
          
        if(hasIcon){  
            //��TextView��ӵ�LinearLayout  
            linear.addView(tv);  
            return linear;  
        }else{  
            tv.setTextColor(Color.BLACK);  
            return tv;  
        }  
    }  
      
} 