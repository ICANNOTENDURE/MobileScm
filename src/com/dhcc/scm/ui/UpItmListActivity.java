package com.dhcc.scm.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.dhcc.scm.R;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.http.thread.HttpGetPostCls;
import com.dhcc.scm.ui.base.BaseActivity;
import com.dhcc.scm.ui.base.FindView;

public class UpItmListActivity extends BaseActivity {
	@FindView(id = R.id.mylv)
	private ListView mylv;
	// private String RetData =
	// "ȲŵͪƬ[625ug*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]ȲŵͪƬ[625ug*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)@������Ƭ[0.2g*100Ƭ]^20210908^2089-09-09^��(100)";
	private String RetData = new String();
	private static final int handle_Init = 1;
	private ProgressDialog progressDialog = null;
	@FindView(id = R.id.et_barcode)
	private EditText barcodeEdit;
	private View currUpView;
	/* ����PDAɨ����� */
	Map<String, String> actionMap = new HashMap<String, String>();
	private DataReceiver dataReceiver = null;
	private String ACTION_CONTENT_NOTIFY = "android.intent.action.CONTENT_NOTIFY";
	private String ACTION_CONTENT_NOTIFY_EMII = "com.ge.action.barscan";
	private String ACTION_CONTENT_NOTIFY_MOTO = "ACTION_CONTENT_NOTIFY_MOTO";
	private String ACTION_CONTENT_NOTIFY_HoneyWell = "com.android.server.scannerservice.broadcast"; // by

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_upitmlist);
		super.onCreate(savedInstanceState);
		findViewById();
		initView();
		// ������ͷ
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		View mainViewm = mLayoutInflater.inflate(R.layout.uplv_header, null);
		mylv.addHeaderView(mainViewm);
		mylv.setAdapter(null);
		mylv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View convertView, int arg2, long arg3) {

				switch (arg2) {
				case 0:
					break;
				default:
					String incidesc = null;
					String inci = null;
					String barcode = null;
					String inclb = null;
					currUpView = convertView;
					if (convertView != null) {
						TextView textViewIncidesc = (TextView) convertView.findViewById(R.id.incidesc);
						TextView textViewInci = (TextView) convertView.findViewById(R.id.inci);
						TextView textViewBarCode = (TextView) convertView.findViewById(R.id.barcode);
						TextView textViewInclb = (TextView) convertView.findViewById(R.id.inclb);
						incidesc = textViewIncidesc.getText().toString();
						inci = textViewInci.getText().toString();
						barcode = textViewBarCode.getText().toString();
						inclb = textViewInclb.getText().toString();
					}
					Intent intent = new Intent(UpItmListActivity.this, UpItemActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("incidesc", incidesc);
					bundle.putString("inci", inci);
					bundle.putString("barcode", barcode);
					bundle.putString("inclb", inclb);
					intent.putExtras(bundle);

					// startActivity(intent);
					startActivityForResult(intent, 0);
				}
			}
		});

		Button btn_search = (Button) findViewById(R.id.btn_scancode);
		btn_search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				QueryItemList();
			}
		});

	}

	/**
	 * Activity�У�ָMainActivity�ࣩ��дonActivityResult����
	 * 
	 * requestCode �����룬������startActivityForResult()���ݹ�ȥ��ֵ resultCode
	 * ����룬��������ڱ�ʶ������������ĸ���Activity
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!data.equals(null)) {
			Integer upretsult = data.getExtras().getInt("upretsult");// �õ���Activity
																		// �رպ󷵻ص����
			if (upretsult == 1) {
				currUpView.setBackgroundColor(Color.BLUE);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	private void ThreadHttp(final String Cls, final String mth, final String Param, final String Typ, final Activity context, final int whatmsg) {

		Thread thread = new Thread() {
			public void run() {
				try {
					// this.sleep(100);
					try {
						RetData = HttpGetPostCls.LinkData(Cls, mth, Param, Typ, context);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					Message msg = new Message();
					msg.what = whatmsg;
					handler.sendMessage(msg);
				} catch (Exception e) {
				}
			}
		};
		thread.start();
	}

	Handler handler = new Handler() {
		public void handleMessage(Message paramMessage) {
			if (paramMessage.what == handle_Init) {
				try {
					if (RetData.indexOf("error") != -1) {
						// �������б���ݣ���رնԻ���
						progressDialog.dismiss();
						AlertDialog.Builder build = new Builder(UpItmListActivity.this);
						build.setIcon(R.drawable.add).setTitle("��ʾ").setMessage("��ȡ��ݴ���,�����������ӣ�").setPositiveButton("确定", null).show();
						return;
					}
					JSONObject retString = new JSONObject(RetData);
					String total = retString.getString("total");
					if (total.compareTo("0") == 0) {
						// �������б���ݣ���رնԻ���
						progressDialog.dismiss();
						AlertDialog.Builder build = new Builder(UpItmListActivity.this);
						build.setIcon(R.drawable.add).setTitle("��ʾ").setMessage("��ݿ��޶�Ӧ����,��˶Ժ����ԣ�").setPositiveButton("ȷ��", null).show();
						return;
					}
					String jsonarr1 = retString.getString("rows");
					JSONArray jsonArrayinfo = new JSONArray(jsonarr1);
					ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
					for (int i = 0; i < jsonArrayinfo.length(); i++) {
						JSONObject itemsobj = jsonArrayinfo.getJSONObject(i);
						HashMap<String, Object> map = new HashMap();
						map.put("���", i + 1);
						map.put("���", itemsobj.getString("incidesc"));
						map.put("���", itemsobj.getString("batno"));
						map.put("Ч��", itemsobj.getString("expdate"));
						map.put("��װ��λ", itemsobj.getString("puomdesc"));
						map.put("ID", itemsobj.getString("inci"));
						map.put("barcode", itemsobj.getString("barcode"));
						map.put("inclb", itemsobj.getString("incib"));
						listItem.add(map);
					}
					SimpleAdapter mAdapter = new SimpleAdapter(UpItmListActivity.this, listItem, R.layout.uplv_listitem, new String[] { "���", "���", "���", "Ч��", "��װ��λ", "ID", "barcode", "inclb" }, new int[] { R.id.xh, R.id.incidesc, R.id.batno, R.id.expdate, R.id.packuom, R.id.inci, R.id.barcode, R.id.inclb });
					mylv.setAdapter(mAdapter);
					progressDialog.dismiss(); // �������б���ݣ���رնԻ���
					barcodeEdit.setText("");
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};

	/* ��ѯ����б� */
	private void QueryItemList() {
		mylv.setAdapter(null);
		TextView barCodeView = (TextView) findViewById(R.id.et_barcode);
		String et_barcode = barCodeView.getText().toString();
		// ���״̬
		progressDialog = ProgressDialog.show(UpItmListActivity.this, "���Ե�...", "��ȡ�����...", true);
		String Param = "&LocId=" + LoginUser.UserLoc + "&BarCode=" + et_barcode;
		ThreadHttp("web.DHCST.DHCSTSUPCHAUNPACK", "QueryIncBatPackList", Param, "Method", UpItmListActivity.this, handle_Init);
	}

	/* ��������PDAɨ�빦�� */
	private void registerReceiver() {
		if (dataReceiver != null)
			return;
		dataReceiver = new DataReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(ACTION_CONTENT_NOTIFY);
		intentFilter.addAction(ACTION_CONTENT_NOTIFY_EMII);
		intentFilter.addAction(ACTION_CONTENT_NOTIFY_MOTO);
		intentFilter.addAction(ACTION_CONTENT_NOTIFY_HoneyWell);

		registerReceiver(dataReceiver, intentFilter);
	}

	private void unregisterReceiver() {
		if (dataReceiver != null) {
			unregisterReceiver(dataReceiver);
		}
	}

	protected void onResume() { // onCreate֮���Լ�ÿ�κ������������д�������淵�ص�ʱ�򣬶������onResume()
		registerReceiver();
		// initialComponent();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		unregisterReceiver();
		super.onDestroy();
	}

	private class DataReceiver extends BroadcastReceiver {
		String content = "", result = "";

		@Override
		public void onReceive(Context context, Intent intent) {
			String paraName = "";
			if (intent.getAction().equals(ACTION_CONTENT_NOTIFY)) {
				paraName = actionMap.get(ACTION_CONTENT_NOTIFY);
			} else if (intent.getAction().equals(ACTION_CONTENT_NOTIFY_EMII)) {
				paraName = actionMap.get(ACTION_CONTENT_NOTIFY_EMII);
			} else if (intent.getAction().equals(ACTION_CONTENT_NOTIFY_MOTO)) {
				paraName = actionMap.get(ACTION_CONTENT_NOTIFY_MOTO);
			} else if (intent.getAction().equals(ACTION_CONTENT_NOTIFY_HoneyWell)) {
				paraName = actionMap.get(ACTION_CONTENT_NOTIFY_HoneyWell);
			} else {
				return;
			}

			Bundle bundle = new Bundle();
			bundle = intent.getExtras();
			content = bundle.getString(paraName);
			barcodeEdit.setText(content);
			QueryItemList();
		}
	}

	public UpItmListActivity() {
		super();
		actionMap.put(ACTION_CONTENT_NOTIFY, "CONTENT");
		actionMap.put(ACTION_CONTENT_NOTIFY_EMII, "value");
		actionMap.put(ACTION_CONTENT_NOTIFY_MOTO, "com.motorolasolutions.emdk.datawedge.data_string");
		actionMap.put(ACTION_CONTENT_NOTIFY_HoneyWell, "scannerdata");
	}

	@Override
	protected void findViewById() {
		// barcodeEdit = (EditText) findViewById(R.id.et_barcode);
		// mylv = (ListView) findViewById(R.id.mylv);
	}

	@Override
	protected void initView() {
	}
}
