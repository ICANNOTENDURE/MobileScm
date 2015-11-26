package com.dhcc.scm.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.ListView;

import com.dhcc.scm.R;
import com.dhcc.scm.adapter.PersonalAllOrderAdapter;
import com.dhcc.scm.entity.Constants;
import com.dhcc.scm.entity.PersonalAllOrder;
import com.dhcc.scm.http.net.ThreadPoolUtils;
import com.dhcc.scm.http.thread.HttpPostThread;
import com.dhcc.scm.ui.base.BaseActivity;
import com.dhcc.scm.ui.base.FindView;
import com.dhcc.scm.utils.CommonTools;

public class PersonalNopayOrderActivity extends BaseActivity {

	@FindView(id = R.id.nopay_order_itm_scroll_list)
	ListView listview;

	private List<PersonalAllOrder> personalAllOrders = new ArrayList<PersonalAllOrder>();
	private PersonalAllOrderAdapter personalAllOrderAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		setContentView(R.layout.activity_personal_nopay_order);
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
		List<NameValuePair> valuePairs = new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("start", "2"));
		valuePairs.add(new BasicNameValuePair("end", "2"));
		ThreadPoolUtils.execute(new HttpPostThread(handler, getIpByType("scm") + Constants.METHOD_SEARCH_INGDREC_ITM, valuePairs));
	}

	/**
	 * 回调句柄
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				CommonTools.showShortToast(PersonalNopayOrderActivity.this, "请求失败，服务器故障");
			} else if (msg.what == 100) {
				CommonTools.showShortToast(PersonalNopayOrderActivity.this, "服务器无响应");
			} else if (msg.what == 200) {

				PersonalAllOrder gdRec = new PersonalAllOrder();
				try {
					Log.i("dhcc", "msf:" + (String) msg.obj);
					JSONObject jsonObject = new JSONObject((String) msg.obj);
					String resultCode = jsonObject.get("resultCode").toString();
					if (resultCode.equals("0")) {
						JSONArray array = jsonObject.getJSONArray("dataList");
						for (int i = 0; i < array.length(); i++) {
							JSONObject jo = (JSONObject) array.get(i);
							PersonalAllOrder gdRecSearch = new PersonalAllOrder();
							gdRecSearch.setBatno(jo.get("batno").toString());
							gdRecSearch.setHopincname(jo.get("name").toString());
							gdRecSearch.setRp(jo.get("rp").toString());
							gdRecSearch.setHisqty(jo.get("qty").toString());
							gdRecSearch.setManf(jo.get("manf").toString());
							gdRecSearch.setExpdate(jo.get("expdate").toString());
							personalAllOrders.add(gdRecSearch);
						}
						personalAllOrderAdapter.notifyDataSetChanged();

					} else {
						CommonTools.showShortToast(getApplication(), jsonObject.get("resultContent").toString());
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				personalAllOrders.add(gdRec);
			}
			personalAllOrderAdapter.notifyDataSetChanged();
		};
	};
}
