package com.dhcc.scm.ui;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dhcc.scm.R;
import com.dhcc.scm.http.Http;
import com.dhcc.scm.http.HttpCallBack;
import com.dhcc.scm.http.HttpConfig;
import com.dhcc.scm.http.HttpParams;
import com.dhcc.scm.ui.base.BaseActivity;
import com.dhcc.scm.ui.base.FindView;
import com.dhcc.scm.ui.base.ViewInject;
import com.dhcc.scm.utils.Loger;

public class StkGrpCatList extends BaseActivity {

	@FindView(id = R.id.stkgrpcatlistview)
	private ListView grpcatlistview;

	public static TextView titleview = null;
	public static final int hand_grpcat = 1;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.stkgrpcat_common);
		super.onCreate(savedInstanceState);

		initCatGrp();
		grpcatlistview.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				@SuppressWarnings("unchecked")
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

	// ��ʼ������
	@SuppressLint("CutPasteId")
	public void initCatGrp() {
		Http http = new Http(new HttpConfig());
		HttpParams params = new HttpParams();
		params.put("Start", 0);
		params.put("Limit", 100);
		params.put("Type", "G");
		params.put("className", "web.DHCST.Util.OrgUtil");
		params.put("methodName", "GetStkCatGroup");
		params.put("type", "Method");
		http.post(getIpByType(), params, new HttpCallBack() {
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				ViewInject.toast("网络不好" + strMsg);
			}

			@Override
			public void onSuccess(java.util.Map<String, String> headers, byte[] t) {
				if (t != null) {
					String str = new String(t);
					Loger.debug(str);
					try {
						ArrayList<HashMap<String, String>> grpcatlistitem = new ArrayList<HashMap<String, String>>();
						JSONObject retString = new JSONObject(str);
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
						SimpleAdapter catgrpAdapter = new SimpleAdapter(StkGrpCatList.this, grpcatlistitem, R.layout.stkgrpcat_list, new String[] { "Description", "RowId" }, new int[] { R.id.Description, R.id.RowId });
						grpcatlistview.setAdapter(catgrpAdapter);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			}

			;
		});
	}


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

}
