package com.dhcc.scm.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.dhcc.scm.adapter.TransferInMainAdapter;
import com.dhcc.scm.entity.Constants;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.entity.TransferIn;
import com.dhcc.scm.entity.TransferInItem;
import com.dhcc.scm.http.Http;
import com.dhcc.scm.http.HttpCallBack;
import com.dhcc.scm.http.HttpParams;
import com.dhcc.scm.ui.base.BaseActivity;
import com.dhcc.scm.ui.base.FindView;
import com.dhcc.scm.ui.base.ViewInject;
import com.dhcc.scm.utils.Loger;

public class TransferInActivity extends BaseActivity {

	@FindView(id = R.id.trinstartdate)
	private EditText trInStartdDate;
	@FindView(id = R.id.trinenddate)
	private EditText trInEndDate;
	@FindView(id = R.id.trinfromloc)
	private EditText trInFromLoc;
	private String trInFromLocID = "";
	@FindView(id = R.id.trinstartdate)
	private EditText trInToLoc;
	private String trInToLocID;
	@FindView(id = R.id.btn_trinseekloc)
	private ImageView trInSeekloc;
	@FindView(id = R.id.trin_mainlistview)
	private ListView trInMainList;
	private Calendar c = null;
	@FindView(id = R.id.btn_trinsearch)
	private ImageView trinsearch; // ���Ұ�ť
	private ProgressDialog progressDialog = null;

	// **************
	private List<TransferInItem> transferInMains = new ArrayList<TransferInItem>();
	private TransferInMainAdapter transferInMainAdapter = null;

	Http http = new Http();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.transfer_in_main);
		super.onCreate(savedInstanceState);
		initView();

		trInSeekloc.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				initInTrFromLoc();
			}
		});

		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		View mainView = mLayoutInflater.inflate(R.layout.activity_tranferin_header, null);
		trInMainList.addHeaderView(mainView);
		// Item点击监听
		trInMainList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View convertView, int arg2, long arg3) {
				TextView textViewTrInit = null;
				if (convertView != null) {
					textViewTrInit = (TextView) convertView.findViewById(R.id.trInit);
				}
				Intent intent = new Intent(TransferInActivity.this, TransferInDetActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("trInit", textViewTrInit.getText().toString());
				intent.putExtras(bundle);

				startActivity(intent);
			}
		});

		// �����¼�
		trInStartdDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				hideIM(v);
				showDialog(1);
			}
		});
		trInEndDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				hideIM(v);
				showDialog(2);
			}
		});
		trInStartdDate.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					hideIM(v);
					showDialog(1);
				}
			}
		});
		trInEndDate.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					hideIM(v);
					showDialog(2);
				}
			}
		});

		// ��ѯ�¼�
		trinsearch.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				QueryTransferIn();
			}
		});

		// InputType
		trInStartdDate.setInputType(InputType.TYPE_NULL);
		trInEndDate.setInputType(InputType.TYPE_NULL);

		deFaultDate();
	}

	@Override
	protected void initView() {
		transferInMainAdapter = new TransferInMainAdapter(this, transferInMains);
		trInMainList.setAdapter(transferInMainAdapter);
	}

	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * @Title: deFaultDate
	 * @Description:
	 * @param
	 * @return void
	 * @throws
	 */
	@SuppressLint("SimpleDateFormat")
	public void deFaultDate() {
		// 开始结束日期按规格显示
		trInStartdDate = (EditText) findViewById(R.id.trinstartdate);
		SimpleDateFormat st = new SimpleDateFormat("yyyy-MM-dd");
		trInStartdDate.setText(st.format(new Date()).toString());
		trInEndDate = (EditText) findViewById(R.id.trinenddate);
		SimpleDateFormat et = new SimpleDateFormat("yyyy-MM-dd");
		trInEndDate.setText(et.format(new Date()).toString());
		// 科室显示
		trInToLoc = (EditText) findViewById(R.id.trintoloc);
		trInToLoc.setText(LoginUser.LocDesc);
		trInToLoc.setEnabled(false);
		trInToLocID = LoginUser.UserLoc;
	}

	public void initInTrFromLoc() {
		String locinputtext = "";
		locinputtext = trInFromLoc.getText().toString().trim();

		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("flag", "to");
		bundle.putString("inputtext", locinputtext);
		intent.putExtras(bundle);
		intent.setClass(TransferInActivity.this, TransferOutLocListActivity.class);
		startActivityForResult(intent, 0);
	}

	/**
	 * date
	 */
	@Override
	protected Dialog onCreateDialog(final int id) {

		Dialog dialog = null;
		c = Calendar.getInstance();
		dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
				if (id == 1) {
					trInStartdDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
				} else {
					trInEndDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
				}
			}
		}, c.get(Calendar.YEAR), // 年
				c.get(Calendar.MONTH), // 月
				c.get(Calendar.DAY_OF_MONTH) // 日
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

	/**
	 * Activity�У�ָMainActivity�ࣩ��дonActivityResult����
	 * 
	 * requestCode �����룬������startActivityForResult()���ݹ�ȥ��ֵ resultCode
	 * ����룬��������ڱ�ʶ������������ĸ���Activity
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (!data.equals(null)) {
			String result = data.getExtras().getString("locdesc").toString();// �õ���Activity
			String locid = data.getExtras().getString("locid").toString();// �õ���Activity
			switch (resultCode) {
			// ������
			case 0:
				// �������ֵ������ã�������>=0
				trInFromLoc.setText(result);
				trInFromLocID = locid; // ID
				break;
			default:
				break;
			}
		}
	}

	private void QueryTransferIn() {
		String startDate = trInStartdDate.getText().toString();
		String endDate = trInEndDate.getText().toString();
		// 提示
		progressDialog = ViewInject.getprogress(TransferInActivity.this, Constants.PRO_WAIT_MESSAGE, false);

		HttpParams params = new HttpParams();
		params.put("fromLocID", trInFromLocID);
		params.put("toLocID", trInToLocID);
		params.put("startDate", startDate);
		params.put("endDate", endDate);
		params.put("className", "web.DHCST.AndroidTransferIn");
		params.put("methodName", "jsQueryTransferIn");
		params.put("type", "Method");
		http.post(getIpByType(), params, new HttpCallBack() {
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				ViewInject.dismiss(progressDialog);
				ViewInject.toast("网络不好" + strMsg);
			}

			@Override
			public void onSuccess(java.util.Map<String, String> headers, byte[] t) {
				ViewInject.dismiss(progressDialog);
				if (t != null) {
					String str = new String(t);
					Loger.debug("登陆网络请求：" + str);
					TransferIn transferIn = JSON.parseObject(str, TransferIn.class);
					if (transferIn.getErrcode().equals("0")) {
						transferInMains.clear();
						transferInMains.addAll(transferIn.getRows());
						transferInMainAdapter.notifyDataSetChanged();
					}
				}
			};

		});
	}

}
