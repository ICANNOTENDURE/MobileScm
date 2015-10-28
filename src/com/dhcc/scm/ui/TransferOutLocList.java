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
import com.dhcc.scm.entity.LoginUser;

public class TransferOutLocList extends Activity implements OnClickListener {
	private ListView lt;
	public static TextView titleview = null;
	mobilecom com = new mobilecom();
	public static final int FROM_LOC = 1;
	public static final int TO_LOC = 0;
	String loctext = "";
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		public void handleMessage(Message paramMessage) {

			if (com.RetData.indexOf("error") != -1) {
				Toast.makeText(getApplicationContext(), "�������!", Toast.LENGTH_LONG).show();
				return;
			}
			if (paramMessage.what == 0) {
				if (!com.RetData.equals("")) {
					try {
						ArrayList<HashMap<String, String>> listitem = new ArrayList<HashMap<String, String>>();
						JSONObject retString = new JSONObject(com.RetData);
						String jsonarr1 = retString.getString("rows");
						JSONArray jsonArrayinfo = new JSONArray(jsonarr1);
						for (int g = 0; g < jsonArrayinfo.length(); g++) {

							JSONObject locsobj = jsonArrayinfo.getJSONObject(g);
							HashMap<String, String> map = new HashMap<String, String>();
							String LocDesc = locsobj.getString("Description");
							String LocID = locsobj.getString("RowId");
							map.put("Description", LocDesc);
							map.put("RowId", LocID);
							listitem.add(map);
						}

						SimpleAdapter simp = new SimpleAdapter(TransferOutLocList.this, listitem, R.layout.loc_list, new String[] { "Description", "RowId" }, new int[] { R.id.Description, R.id.RowId });
						lt.setAdapter(simp);
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
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.transferloclist);

		// ��ҳ��������
		Bundle bundle = this.getIntent().getExtras();
		// ����nameֵ
		String flag = bundle.getString("flag");
		loctext = bundle.getString("inputtext");
		if (flag.equals("to")) {
			initToLoc(); // ����������

			// �����б���ӵ����¼�
			lt = (ListView) findViewById(R.id.loclistview);/* ����һ����̬���� */
			lt.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					HashMap<String, String> map = (HashMap<String, String>) lt.getItemAtPosition(arg2);
					String locdesc = map.get("Description");
					String locid = map.get("RowId");

					Intent intent = new Intent();
					intent.setClass(TransferOutLocList.this, TransferOut.class);
					intent.putExtra("locdesc", locdesc);
					intent.putExtra("locid", locid);
					setResult(0, intent);
					finish();
				}
			});

		} else if (flag.equals("from")) {
			initFromLoc();
			// �����б���ӵ����¼�
			lt = (ListView) findViewById(R.id.loclistview);/* ����һ����̬���� */
			lt.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
					HashMap<String, String> map = (HashMap<String, String>) lt.getItemAtPosition(arg2);
					String locdesc = map.get("Description");
					String locid = map.get("RowId");
					Intent intent = new Intent();
					intent.setClass(TransferOutLocList.this, TransferOut.class);
					intent.putExtra("locdesc", locdesc);
					intent.putExtra("locid", locid);
					setResult(1, intent);
					finish();
				}
			});
		}
	}

	/**
	 * 
	 * @Title: initFromLoc
	 * @Description: TODO(����������)
	 * @param @param view
	 * @param @return �趨�ļ�
	 * @return List<String> ��������
	 * @throws
	 */

	@SuppressLint("CutPasteId")
	public void initToLoc() {
		String Params = "&Start=0&Limit=100&Desc=" + loctext;
		// com.ThreadHttp("web.DHCST.Util.OrgUtil", "GetDeptLoc",
		// Params,"Method", TransferOutLocList.this, 0, handler);
		com.ThreadHttp("web.DHCST.AndroidCommon", "GetDeptLoc", Params, "Method", TransferOutLocList.this, 0, handler);
	}

	/**
	 * 
	 * 
	 * @Title: initFromLoc
	 * @Description: ���ع�����
	 * @param @param view
	 * @param @return �趨�ļ�
	 * @return List<String> ��������
	 * @throws
	 */
	public void initFromLoc() {
		String LocDesc = "";
		String GroupId = LoginUser.UserGroupID;
		String Params = "&Start=0&Limit=200&GroupId=" + GroupId + "&LocDesc=" + LocDesc;
		com.ThreadHttp("web.DHCST.Util.OrgUtil", "GetGroupDept", Params, "Method", TransferOutLocList.this, 0, handler);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub

	}

	/**
	 * ����
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(TransferOutLocList.this, TransferOut.class);
			intent.putExtra("locdesc", "");
			intent.putExtra("locid", "");
			// 2�����ؼ���룬�������κ����
			setResult(2, intent);
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

}
