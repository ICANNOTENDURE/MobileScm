package com.dhcc.scm.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.dhcc.scm.R;
import com.dhcc.scm.adapter.InGdRecSearchAdapter;
import com.dhcc.scm.entity.Constants;
import com.dhcc.scm.entity.InGdRecSearch;
import com.dhcc.scm.entity.InGdRecSearchItm;
import com.dhcc.scm.http.Http;
import com.dhcc.scm.http.HttpCallBack;
import com.dhcc.scm.http.HttpConfig;
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

	@FindView(id = R.id.ingdrecsearch_result_back, click = true)
	ImageView imgBack;// 回退按钮
	@FindView(id = R.id.ingdrecsearch_itm_scroll_list)
	ListView listview;

	private List<InGdRecSearchItm> inGdRecsearchs = new ArrayList<InGdRecSearchItm>();
	private InGdRecSearchAdapter inGdRecsearchAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ingdrec_search_result);
		super.onCreate(savedInstanceState);
		findViewById();
		initView();
		getResult();
	}

	@Override
	protected void findViewById() {
		// InGdRecSearch gdRecSearch=new InGdRecSearch();
		// gdRecSearch.setHome("dota");
		// gdRecSearch.setNum("2015");
		// inGdRecsearchs.add(gdRecSearch);
		inGdRecsearchAdapter = new InGdRecSearchAdapter(this, inGdRecsearchs);
		listview.setAdapter(inGdRecsearchAdapter);
	}

	private void getResult() {
		// List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		// valuePairs.add(new BasicNameValuePair("start", "2015-01-01"));
		// valuePairs.add(new BasicNameValuePair("end", "2"));
		// ThreadPoolUtils.execute(new HttpPostThread(handler,
		// getIpByType("scm") + Constants.METHOD_SEARCH_INGDREC_ITM,
		// valuePairs));

		HttpConfig config = new HttpConfig();
		config.cacheTime = 0;
		Http http = new Http(config);
		HttpParams params = new HttpParams();
		params.put("start", "2015-01-01");
		params.put("end", "2");
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
					// InGdRecSearchItm gdRec = new InGdRecSearchItm();
					// try {
					// JSONObject jsonObject = new JSONObject(str);
					// String resultCode =
					// jsonObject.get("resultCode").toString();
					// if (resultCode.equals("0")) {
					// List<InGdRecSearchItm>
					// gdRecSearchs=JSON.parseArray(jsonObject.getString("dataList"),InGdRecSearchItm.class);
					// inGdRecsearchs.clear();
					// inGdRecsearchs.addAll(gdRecSearchs);
					// inGdRecsearchAdapter.notifyDataSetChanged();
					// } else {
					// ViewInject.toast(jsonObject.get("resultContent").toString());
					// }
					// } catch (JSONException e) {
					// e.printStackTrace();
					// ViewInject.toast(e.getMessage());
					// }
					// inGdRecsearchs.add(gdRec);
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
