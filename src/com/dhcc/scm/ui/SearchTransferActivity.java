package com.dhcc.scm.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dhcc.scm.R;
import com.dhcc.scm.adapter.SearchTransferAdapter;
import com.dhcc.scm.entity.Constants;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.entity.TransferOut;
import com.dhcc.scm.http.Http;
import com.dhcc.scm.http.HttpCallBack;
import com.dhcc.scm.http.HttpConfig;
import com.dhcc.scm.http.HttpParams;
import com.dhcc.scm.ui.base.BaseActivity;
import com.dhcc.scm.ui.base.FindView;
import com.dhcc.scm.ui.base.ViewInject;
import com.dhcc.scm.utils.Loger;
import com.dhcc.scm.utils.SystemTool;

public class SearchTransferActivity extends BaseActivity implements OnClickListener {

	public static final String TAG = TransferOutLocListActivity.class.getSimpleName();

	@FindView(id = R.id.troutstartdate)
	EditText trOutStartdDate;
	@FindView(id = R.id.troutenddate)
	EditText trOutEndDate;
	@FindView(id = R.id.troutfromloc)
	EditText trOutfromloc;
	@FindView(id = R.id.trouttoloc)
	EditText trOutToLoc;
	@FindView(id = R.id.btn_troutseekloc,click=true)
	ImageView trOutSeekloc;
	@FindView(id = R.id.btn_troutsearch,click=true)
	ImageView trOutsearch;
	@FindView(id = R.id.trout_mainlistview)
	ListView trOutMainList;

	private String trOutfromlocID;
	private String trOutToLocID = "";

	private Calendar c = null;

	private ProgressDialog progressDialog = null;
	private ArrayList<TransferOut> listItem=new ArrayList<TransferOut>();
	private Http http;
	private HttpParams httpParams = new HttpParams();
	
	private SearchTransferAdapter searchTransferAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.search_transferout_list);
		super.onCreate(savedInstanceState);
		initView();
		initData();
	}
	
	@Override
	protected void initData() {
		trOutStartdDate.setText(SystemTool.getDataTime("yyyy-MM-dd"));
		trOutEndDate.setText(SystemTool.getDataTime("yyyy-MM-dd"));
		trOutfromloc.setText(LoginUser.LocDesc);
		trOutfromlocID = LoginUser.UserLoc;
		
		HttpConfig config = new HttpConfig();
		config.cacheTime = 0;
		config.useDelayCache = false;
		http = new Http(config);
		httpParams.put("className", "web.DHCST.AndroidTransferOut");
		httpParams.put("methodName", "jsQueryTransferOut");
		httpParams.put("type", "Method");
		
	}
	
	@SuppressLint("InflateParams")
	@Override
	protected void initView() {
		trOutStartdDate.setInputType(InputType.TYPE_NULL);
		trOutEndDate.setInputType(InputType.TYPE_NULL);
		trOutfromloc.setEnabled(false);
		
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		View mainView = mLayoutInflater.inflate(R.layout.search_transferout_header, null);
		trOutMainList.addHeaderView(mainView);
		trOutMainList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View convertView, int arg2, long arg3) {
				Intent intent = new Intent();
				Bundle bundle = new Bundle();

				TextView trNo = (TextView) convertView.findViewById(R.id.s_transferno);
				TextView trInti = (TextView) convertView.findViewById(R.id.s_initID);
				TextView trStkCatGrpId = (TextView) convertView.findViewById(R.id.s_catgrpid);
				TextView trCatGrpDesc = (TextView) convertView.findViewById(R.id.s_catgrpdesc);
				bundle.putString("transferNo", trNo.getText().toString());
				bundle.putString("initID", trInti.getText().toString());
				bundle.putString("initCatGrpID", trStkCatGrpId.getText().toString());
				bundle.putString("initCatGrpDesc", trCatGrpDesc.getText().toString());
				intent.putExtras(bundle);
				intent.setClass(SearchTransferActivity.this, ScanForTransfer.class);
				startActivity(intent);
			}
		});

		
		trOutStartdDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				hideIM(v);
				showDialog(1);
			}
		});
		trOutEndDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				hideIM(v);
				showDialog(2);
			}
		});
		trOutStartdDate.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					hideIM(v);
					showDialog(1);
				}
			}
		});
		trOutEndDate.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					hideIM(v);
					showDialog(2);
				}
			}
		});
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// / �����������
	public void initInTrFromLoc() {
		String locinputtext = "";
		locinputtext = trOutToLoc.getText().toString().trim();
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("flag", "from");
		bundle.putString("inputtext", locinputtext);
		intent.putExtras(bundle);
		intent.setClass(SearchTransferActivity.this, TransferOutLocListActivity.class);
		startActivityForResult(intent, 0);
	}



	/**
	 * �������ڼ�ʱ��ѡ��Ի���
	 */
	@Override
	protected Dialog onCreateDialog(final int id) {

		Dialog dialog = null;
		c = Calendar.getInstance();
		dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
				if (id == 1) {
					trOutStartdDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
				} else {
					trOutEndDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);

				}
			}
		}, c.get(Calendar.YEAR), // �������
				c.get(Calendar.MONTH), // �����·�
				c.get(Calendar.DAY_OF_MONTH) // ��������
		);
		return dialog;
	}

	private void hideIM(View edt) {
		try {
			InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
			IBinder windowToken = edt.getWindowToken();

			if (windowToken != null) {
				im.hideSoftInputFromWindow(windowToken, 0);
			}
		} catch (Exception e) {

		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!data.equals(null)) {
			String result = data.getExtras().getString("locdesc").toString();// �õ���Activity
			String locid = data.getExtras().getString("locid").toString();// �õ���Activity
			switch (resultCode) {
			case 1:
				trOutToLoc.setText(result);
				trOutToLocID = locid; 
				break;
			default:
				break;
			}
		}
	}

	private void QueryTransferOut() {
		String startDate = trOutStartdDate.getText().toString();
		String endDate = trOutEndDate.getText().toString();
		progressDialog =ViewInject.getprogress(SearchTransferActivity.this, Constants.PRO_WAIT_MESSAGE, false);
		httpParams.put("startDate", startDate);
		httpParams.put("endDate", endDate);
		httpParams.put("fromLocID", trOutfromlocID);
		httpParams.put("toLocID", trOutToLocID);
		http.post(getIpByType(), httpParams, new HttpCallBack() {

			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				progressDialog.dismiss();
				ViewInject.toast("网络不好" + strMsg);
			}

			@Override
			public void onSuccess(java.util.Map<String, String> headers, byte[] t) {
				progressDialog.dismiss();
				if (t != null) {
					String str = new String(t);
					Loger.debug("登陆网络请求：" + new String(t));
					try {
						JSONObject retString = new JSONObject(str);
						String ErrCode = retString.getString("ErrCode");
						if (!ErrCode.equals("0")) {
							progressDialog.dismiss(); 
							AlertDialog.Builder build = new Builder(SearchTransferActivity.this);
							build.setIcon(R.drawable.add).setTitle("错误ʾ").setMessage("查询失败").setPositiveButton("确认", null).show();
							return;
						}
						listItem.clear();
						List<TransferOut> items=(ArrayList<TransferOut>) JSON.parseArray(retString.getString("rows"), TransferOut.class);
						listItem.addAll(items);
						if (listItem.size() == 0) {
							progressDialog.dismiss(); 
							ViewInject.toast("没有出库记录");
							return;
						}
						if(searchTransferAdapter==null){
							searchTransferAdapter=new SearchTransferAdapter(SearchTransferActivity.this, listItem);
							trOutMainList.setAdapter(searchTransferAdapter);
						}else{
							searchTransferAdapter.notifyDataSetChanged();
						}
					} catch (Exception e) {
						ViewInject.toast(e.getMessage());
						e.printStackTrace();
					}

				}
			}

			;
		});
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_troutseekloc:
			initInTrFromLoc();
			break;
		case R.id.btn_troutsearch:
			QueryTransferOut();
			break;
		}

	}
}
