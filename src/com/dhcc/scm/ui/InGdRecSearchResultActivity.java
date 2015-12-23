package com.dhcc.scm.ui;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.alibaba.fastjson.JSON;

import com.dhcc.scm.R;
import com.dhcc.scm.adapter.InGdRecSearchAdapter;
import com.dhcc.scm.entity.Constants;
import com.dhcc.scm.entity.InGdRecSearch;
import com.dhcc.scm.entity.InGdRecSearchItem;
import com.dhcc.scm.http.Http;
import com.dhcc.scm.http.HttpCallBack;
import com.dhcc.scm.http.HttpParams;
import com.dhcc.scm.ui.base.BaseActivity;
import com.dhcc.scm.ui.base.FindView;
import com.dhcc.scm.ui.base.ViewInject;

/**
 * 
 * @author huaxiaoying
 * @date 2015年10月22日 15:09
 */

public class InGdRecSearchResultActivity extends BaseActivity implements OnClickListener {

	String mstartdate = "";
	String menddate = "";
	@FindView(id = R.id.result_startdate)
	TextView restartdate;
	@FindView(id = R.id.result_enddate)
	TextView reenddate;

	@FindView(id = R.id.ingdrecsearch_result_back, click = true)
	ImageView imgBack;// 回退按钮
	@FindView(id = R.id.ingdrecsearch_itm_scroll_list)
	ListView listview;

	private List<InGdRecSearchItem> inGdRecsearchs = new ArrayList<InGdRecSearchItem>();
	private InGdRecSearchAdapter inGdRecsearchAdapter = null;

	private HttpParams params = new HttpParams();
	private Http http = new Http();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ingdrec_search_result);
		super.onCreate(savedInstanceState);
		//跨类取数据
		Bundle bundle = this.getIntent().getExtras();
		mstartdate = bundle.getString("startdate");
		menddate = bundle.getString("enddate");
		//显示开始结束日期
		restartdate.setText(mstartdate);
		reenddate.setText(menddate);
		findViewById();
		initView();
		getResult();
	}

	@Override
	protected void findViewById() {
		inGdRecsearchAdapter = new InGdRecSearchAdapter(this, inGdRecsearchs);
		listview.setAdapter(inGdRecsearchAdapter);
	}

	private void getResult() {

		params.put("start", mstartdate);
		params.put("end", menddate);
		params.put("requestType", "apk");
		http.post(getIpByType("scm") + "/" + Constants.METHOD_SEARCH_INGDREC_ITM, params, new HttpCallBack() {
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				ViewInject.toast("网络不好" + strMsg);
			}

			@Override
			public void onSuccess(java.util.Map<String, String> headers, byte[] t) {
				if (t != null) {
					String str = new String(t);
					InGdRecSearch gdRecSearch = JSON.parseObject(str, InGdRecSearch.class);
					if (gdRecSearch.getResultCode().equals("0")) {
						inGdRecsearchs.clear();
						inGdRecsearchs.addAll(gdRecSearch.getDataList());
						inGdRecsearchAdapter.notifyDataSetChanged();
					} else {
						ViewInject.toast(gdRecSearch.getResultContent());
					}
				}
			};
		});
	}

	@Override
	protected void initView() {
		imgBack.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.ingdrecsearch_result_back:
			finish();
			break;
		default:
			break;
		}
	}
}
