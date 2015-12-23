package com.dhcc.scm.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.dhcc.scm.adapter.InStkTkMainAdapter;
import com.dhcc.scm.entity.Constants;
import com.dhcc.scm.entity.InStkTkMain;
import com.dhcc.scm.entity.InStkTkMainItem;
import com.dhcc.scm.entity.LoginUser;
import com.dhcc.scm.http.Http;
import com.dhcc.scm.http.HttpCallBack;
import com.dhcc.scm.http.HttpParams;
import com.dhcc.scm.ui.base.BaseActivity;
import com.dhcc.scm.ui.base.FindView;
import com.dhcc.scm.ui.base.ViewInject;
import com.dhcc.scm.utils.Loger;

public class InStkTkMainActivity extends BaseActivity {

	@FindView(id = R.id.ist_startdate)
	private EditText istStartDate;
	@FindView(id = R.id.ist_enddate)
	private EditText istEndDate;
	@FindView(id = R.id.ist_mainlistview)
	private ListView istMainList;
	@FindView(id = R.id.ist_locdesc)
	private EditText istLoc;
	@FindView(id = R.id.ist_way)
	private EditText istWay;
	@FindView(id = R.id.btn_search)
	private ImageView istsearch; // ���Ұ�ť
	private String istLocID = "";
	private Calendar c = null;
	private ProgressDialog progressDialog = null;
	private int selectedWayIndex = 0;

	// **************
	private List<InStkTkMainItem> inStkTkMainins = new ArrayList<InStkTkMainItem>();
	private InStkTkMainAdapter inStkTkMaininsAdapter = null;

	Http http = new Http();

	// @SuppressLint("InflateParams")// xin jia de
	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_instktk_main);
		super.onCreate(savedInstanceState);
		initView();
		// InputType设定
		istStartDate.setInputType(InputType.TYPE_NULL);
		istEndDate.setInputType(InputType.TYPE_NULL);
		istWay.setInputType(InputType.TYPE_NULL);

	
		// ������ͷ
		LayoutInflater mLayoutInflater = LayoutInflater.from(this);
		View mainView = mLayoutInflater.inflate(R.layout.activity_instktk_mheader, null);
		istMainList.addHeaderView(mainView);
		// item点击监听
		istMainList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View convertView, int arg2, long arg3) {
				TextView textViewInst = null;
				if (convertView != null) {
					textViewInst = (TextView) convertView.findViewById(R.id.ist_rowid);
				}// itm取值zi该界面
				Intent intent = new Intent(InStkTkMainActivity.this, InStkTkByItemActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("inst", textViewInst.getText().toString());
				intent.putExtras(bundle);
				startActivity(intent);
			}
		});

		// 查找后执行QueryInStkTk
		istsearch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				QueryInStkTk();
			}
		});
		// �����¼�
		istStartDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				hideIM(v);
				showDialog(1);
			}
		});
		istEndDate.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				hideIM(v);
				showDialog(2);
			}
		});
		istStartDate.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					hideIM(v);
					showDialog(1);
				}
			}
		});
		istEndDate.setOnFocusChangeListener(new OnFocusChangeListener() {
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus == true) {
					hideIM(v);
					showDialog(2);
				}
			}
		});

		// �̵㷽ʽ
		istWay.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				final String[] wayStr = { "��ҩƷ", "����λ" };
				// boolean[] bool = {true,false};
				AlertDialog.Builder dailog = new Builder(InStkTkMainActivity.this);
				dailog.setTitle("�̵㷽ʽ").setItems(wayStr, null).setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						istWay.setText(wayStr[selectedWayIndex]);

					}
				}).setSingleChoiceItems(wayStr, 0, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						selectedWayIndex = which;
					}
				}).show();
			}
		});
		// 默认日期
		deFaultDate();
	}

	@Override
	protected void initView() {
		inStkTkMaininsAdapter = new InStkTkMainAdapter(this, inStkTkMainins);
		istMainList.setAdapter(inStkTkMaininsAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
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
	 * �������ڼ�ʱ��ѡ��Ի���
	 */
	@Override
	protected Dialog onCreateDialog(final int id) {

		Dialog dialog = null;
		c = Calendar.getInstance();
		dialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
			public void onDateSet(DatePicker dp, int year, int month, int dayOfMonth) {
				if (id == 1) {
					istStartDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
				} else {
					istEndDate.setText(year + "-" + (month + 1) + "-" + dayOfMonth);
				}
			}
		}, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
		return dialog;
	}

	/**
	 * @Title: deFaultDate
	 * @Description: TODO(������һ�仰�����������������)
	 * @param �趨�ļ�
	 * @return void ��������
	 * @throws
	 */
	@SuppressLint("SimpleDateFormat")
	public void deFaultDate() {
		// Ĭ������
		SimpleDateFormat sysdate = new SimpleDateFormat("yyyy-MM-dd");
		istStartDate.setText(sysdate.format(new Date()).toString());
		istEndDate.setText(sysdate.format(new Date()).toString());
		// 默认loc
		istLoc.setText(LoginUser.LocDesc);
		istLoc.setEnabled(false);
		istLocID = LoginUser.UserLoc;
	}

	private void QueryInStkTk() {
		String startDate = istStartDate.getText().toString();
		String endDate = istEndDate.getText().toString();
		progressDialog = ViewInject.getprogress(InStkTkMainActivity.this, Constants.PRO_WAIT_MESSAGE, false);

		HttpParams params = new HttpParams();
		params.put("LocID", istLocID);
		params.put("endDate", endDate);
		params.put("startDate", startDate);
		params.put("className", "web.DHCST.AndroidInStkTk");
		params.put("methodName", "jsQueryInStkTk");
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
					// itm显示
					InStkTkMain inStkSearch = JSON.parseObject(str, InStkTkMain.class);
					if (inStkSearch.getErrcode().equals("0")) {
						inStkTkMainins.clear();
						// Loger.debug("inStkSearch.getRows().size()="+inStkSearch.getRows().size());
						inStkTkMainins.addAll(inStkSearch.getRows());
						inStkTkMaininsAdapter.notifyDataSetChanged();
					}
				}
			};

		});
	}

	// 错误消息提示
	public void setTipErrorMessage(String ErrorMessage) {
		progressDialog.dismiss();
		AlertDialog.Builder build = new Builder(InStkTkMainActivity.this);
		build.setIcon(R.drawable.add).setTitle("��ʾ").setMessage(ErrorMessage).setPositiveButton("ȷ��", null).show();
	}
}
