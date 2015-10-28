package com.dhcc.scm.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.dhcc.scm.R;

public class StkGrpCatList extends Activity implements OnClickListener {
	private ListView grpcatlistview;
	public static TextView titleview = null;
	mobilecom com = new mobilecom();
	public static final int hand_grpcat = 1;
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message paramMessage) {

			if (com.RetData.indexOf("error") != -1) {
				Toast.makeText(getApplicationContext(), "�������!",
						Toast.LENGTH_LONG).show();
				return;
			}
			if (paramMessage.what == 0) {
				if (!com.RetData.equals("")) {
					try {
						ArrayList<HashMap<String, String>> grpcatlistitem = new ArrayList<HashMap<String, String>>();
						JSONObject retString = new JSONObject(com.RetData);
						String jsonarr1 = retString.getString("rows");
						JSONArray jsonArrayinfo = new JSONArray(jsonarr1);
						for (int g = 0; g < jsonArrayinfo.length(); g++) {

							JSONObject locsobj = jsonArrayinfo.getJSONObject(g);
							HashMap<String, String> map = new HashMap<String, String>();
							String CatGrpDesc = locsobj.getString("Description");
							String CatGrpRowid = locsobj.getString("RowId");
							map.put("Description", CatGrpDesc);
							map.put("RowId", CatGrpRowid);
							grpcatlistitem.add(map);
						}
						SimpleAdapter catgrpAdapter = new SimpleAdapter(
								StkGrpCatList.this, grpcatlistitem,
								R.layout.stkgrpcat_list, new String[] {
										"Description", "RowId" }, new int[] {
										R.id.Description, R.id.RowId });
						grpcatlistview.setAdapter(catgrpAdapter);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}
		}
	};

	@SuppressLint({ "NewApi", "ResourceAsColor" })
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.stkgrpcat_common);
		initCatGrp();
		// �����б���ӵ����¼�
		grpcatlistview = (ListView) findViewById(R.id.stkgrpcatlistview);
		grpcatlistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				HashMap<String, String> map = (HashMap<String, String>) grpcatlistview.getItemAtPosition(arg2);
				String catgrpdesc = map.get("Description");
				String catgrprowid = map.get("RowId");
				Intent intent = new Intent();
				intent.setClass(StkGrpCatList.this, TransferOut.class);
				intent.putExtra("stkgrpdesc", catgrpdesc);
				intent.putExtra("stkgrprowid", catgrprowid);
				setResult(3, intent);
				finish();
			}
		});
	}
	//��ʼ������
	@SuppressLint("CutPasteId")
	public void initCatGrp() {
		String Param="&Start=0&Limit=99&Type=G";
		com.ThreadHttp("web.DHCST.Util.DrugUtil", "GetStkCatGroup", Param,"Method", StkGrpCatList.this, 0, handler);
	}

	//������¼�,�������κ����
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(StkGrpCatList.this, TransferOut.class);
			intent.putExtra("stkgrpdesc", "");
			intent.putExtra("stkgrprowid", "");
			setResult(3, intent);
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}
	
	
}

