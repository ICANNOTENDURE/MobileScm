package com.dhcc.scm.ui;

import java.util.ArrayList;
import java.util.List;
import android.os.Bundle;
import android.widget.ListView;
import com.alibaba.fastjson.JSON;

import com.dhcc.scm.R;
import com.dhcc.scm.adapter.PersonalAllOrderAdapter;
import com.dhcc.scm.entity.Constants;
import com.dhcc.scm.entity.PersonalAllOrder;
import com.dhcc.scm.entity.PersonalAllOrderItem;
import com.dhcc.scm.http.Http;
import com.dhcc.scm.http.HttpCallBack;
import com.dhcc.scm.http.HttpConfig;
import com.dhcc.scm.http.HttpParams;
import com.dhcc.scm.ui.base.BaseActivity;
import com.dhcc.scm.ui.base.FindView;
import com.dhcc.scm.ui.base.ViewInject;


public class PersonalOederSoonSearchActivity extends BaseActivity{
	
	@FindView(id = R.id.soon_search_order_itm_scroll_list)
	ListView listview;
	
	private List<PersonalAllOrderItem> personalAllOrders = new ArrayList<PersonalAllOrderItem>();
	private PersonalAllOrderAdapter personalAllOrderAdapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		setContentView(R.layout.activity_personal_oeder_soon_search);
		super.onCreate(savedInstanceState);
		findViewById();
		initView();
		getResult();
	}
	@Override
	protected void findViewById() {
		personalAllOrderAdapter = new PersonalAllOrderAdapter(this, personalAllOrders);
		listview.setAdapter(personalAllOrderAdapter);

	}

	private void getResult() {
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
					PersonalAllOrder personalAllOrder = JSON.parseObject(str, PersonalAllOrder.class);
					if (personalAllOrder.getResultCode().equals("0")) {
						personalAllOrders.clear();
						personalAllOrders.addAll(personalAllOrder.getDataList());
						personalAllOrderAdapter.notifyDataSetChanged();
					} else {
						ViewInject.toast(personalAllOrder.getResultContent());
					}
				}
			};
		});
	}
}
