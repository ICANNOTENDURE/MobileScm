package com.dhcc.scm.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.dhcc.scm.R;
import com.dhcc.scm.adapter.CommonAdapter;
import com.dhcc.scm.entity.CommonItem;
import com.dhcc.scm.entity.Constants;
import com.dhcc.scm.http.Http;
import com.dhcc.scm.http.HttpCallBack;
import com.dhcc.scm.http.HttpConfig;
import com.dhcc.scm.http.HttpParams;
import com.dhcc.scm.ui.base.BaseActivity;
import com.dhcc.scm.ui.base.FindView;
import com.dhcc.scm.utils.Loger;
import com.dhcc.scm.widgets.EmptyLayout;
import com.dhcc.scm.widgets.listview.PullToRefreshBase;
import com.dhcc.scm.widgets.listview.PullToRefreshBase.OnRefreshListener;
import com.dhcc.scm.widgets.listview.PullToRefreshList;

public class StkGrpCatListActivity extends BaseActivity {
	
	public static final String TAG = StkGrpCatListActivity.class.getSimpleName();
	
	ListView mListView;
	@FindView(id = R.id.pull_refresh_listview)
	PullToRefreshList mRefreshLayout;
	@FindView(id = R.id.empty_layout)
	EmptyLayout mEmptyLayout;
	@FindView(id = R.id.pull_refresh_title)
	TextView titleTxt;

	private CommonAdapter adapter;
	private Http http;
	private HttpParams httpParams = new HttpParams();
	private final List<CommonItem> items = new ArrayList<CommonItem>();
	private static int totalnum = 0;
	private static int curpage = 0;

	@Override
	public void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.pull_refresh_listview);
		super.onCreate(savedInstanceState);
		initData();
		initView();
	}

	@Override
	protected void initData() {
		HttpConfig config = new HttpConfig();
		config.cacheTime = 0;
		config.useDelayCache = false;
		http = new Http(config);
		httpParams.put("Limit", Constants.PAGE_SIZE);
		httpParams.put("Type", "G");
		httpParams.put("className", "web.DHCST.Util.DrugUtil");
		httpParams.put("methodName", "GetStkCatGroup");
		httpParams.put("type", "Method");
	}

	@Override
	protected void initView() {
		titleTxt.setText("类组");

		mListView = mRefreshLayout.getRefreshView();
		mListView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				CommonItem commonItem = (CommonItem) arg0.getItemAtPosition(arg2);
				Intent intent = new Intent();
				intent.setClass(StkGrpCatListActivity.this, TransferOut.class);
				intent.putExtra("stkgrprowid", String.valueOf(commonItem.getRowId()));
				intent.putExtra("stkgrpdesc", commonItem.getDescription());
				setResult(3, intent);
				finish();
			}
		});

		mRefreshLayout.setPullLoadEnabled(true);
		mRefreshLayout.setPullRefreshEnabled(false);
		mRefreshLayout.setOnRefreshListener(new OnRefreshListener<ListView>() {
			@Override
			public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

			}

			@Override
			public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
				refresh();
			}
		});
		refresh(0);

	}

	private void refresh() {
		int totalpage = (int) totalnum / Constants.PAGE_SIZE;
		if (curpage == totalpage) {
			mRefreshLayout.setHasMoreData(false);
			return;
		}
		curpage++;
		refresh(curpage);

	}

	private void refresh(int page) {

		httpParams.put("Start", page * Constants.PAGE_SIZE);
		http.get(super.getIpByType(), httpParams, new HttpCallBack() {
			@Override
			public void onSuccess(java.util.Map<String, String> headers, byte[] t) {
				String str = new String(t);
				Loger.debug(TAG + "网络请求" + str);
				try {
					JSONObject retString = new JSONObject(str);
					totalnum = retString.getInt("results");
					List<CommonItem> datas = JSON.parseArray(retString.getString("rows"), CommonItem.class);
					items.addAll(datas);
					if (adapter == null) {
						adapter = new CommonAdapter(mListView, items, R.layout.item_list_common);
						mListView.setAdapter(adapter);
					} else {
						adapter.refresh(items);
					}
					mEmptyLayout.dismiss();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				if (adapter != null && adapter.getCount() > 0) {
					return;
				} else {
					mEmptyLayout.setErrorType(EmptyLayout.NODATA);
				}
			}

			@Override
			public void onFinish() {
				super.onFinish();
				mRefreshLayout.onPullDownRefreshComplete();
				mRefreshLayout.onPullUpRefreshComplete();
			}
		});
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			Intent intent = new Intent();
			intent.setClass(StkGrpCatListActivity.this, TransferOut.class);
			intent.putExtra("stkgrpdesc", "");
			intent.putExtra("stkgrprowid", "");
			setResult(3, intent);
			finish();
			return false;
		}
		return super.onKeyDown(keyCode, event);
	}
	@Override
	public void finish() {
		curpage=0;
		super.finish();
	}
}
