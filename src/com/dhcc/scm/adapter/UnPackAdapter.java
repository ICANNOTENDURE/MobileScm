package com.dhcc.scm.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dhcc.scm.R;

public class UnPackAdapter extends BaseAdapter{
	private LayoutInflater mInflater;
	private String itemList = "";
	public UnPackAdapter(Context context) {
		this.mInflater = LayoutInflater.from(context);
    }
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return getDate().size();//��������ĳ���
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.uplv_listitem,null);
			holder = new ViewHolder();
			/*�õ������ؼ��Ķ���*/                    
			holder.xh = (TextView) convertView.findViewById(R.id.xh);
			holder.incidesc = (TextView) convertView.findViewById(R.id.incidesc);
			holder.batno = (TextView) convertView.findViewById(R.id.batno);
			holder.expdate = (TextView) convertView.findViewById(R.id.expdate);
			holder.packuom = (TextView) convertView.findViewById(R.id.packuom);
            convertView.setTag(holder);//��ViewHolder����  

		}else{
            holder = (ViewHolder)convertView.getTag();//ȡ��ViewHolder����  
        }
		holder.xh.setText(getDate().get(position).get("xh").toString());
		holder.incidesc.setText(getDate().get(position).get("incidesc").toString());
		holder.batno.setText(getDate().get(position).get("batno").toString());
		holder.expdate.setText(getDate().get(position).get("expdate").toString());
		holder.packuom.setText(getDate().get(position).get("packuom").toString());
		return convertView;
	}
	
	private ArrayList<HashMap<String, Object>> getDate(){

		ArrayList<HashMap<String, Object>>listItem = new ArrayList<HashMap<String, Object>>();
		String[] ret=itemList.split("\\@");
		for (int i=0;i<ret.length;i++){
			HashMap<String, Object> map = new HashMap<String, Object>();  
			String[] str=ret[i].split("\\^");
			map.put("xh", i+1);
			map.put("incidesc",str[0]);
			map.put("batno", str[1]);
			map.put("expdate", str[2]);
			map.put("packuom", str[3]);
			listItem.add(map);  
		} 
		return listItem;
	}
	//�������
	public void SetDateList(String inString){
		itemList = inString;
	}
	public final class ViewHolder{
		  public TextView xh;
		  public TextView incidesc;
		  public TextView batno;
		  public TextView expdate;
		  public TextView packuom;
		 }
}
