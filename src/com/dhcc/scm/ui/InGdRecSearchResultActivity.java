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
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.ListView;

import com.dhcc.scm.R;
import com.dhcc.scm.adapter.InGdRecSearchAdapter;
import com.dhcc.scm.entity.Constants;
import com.dhcc.scm.entity.InGdRecSearch;
import com.dhcc.scm.http.net.ThreadPoolUtils;
import com.dhcc.scm.http.thread.HttpPostThread;
import com.dhcc.scm.ui.base.BaseActivity;
import com.dhcc.scm.ui.base.FindView;
import com.dhcc.scm.utils.CommonTools;

/**
 * 
 * @author huaxiaoying
 * @date 2015年10月22日 15:09
 */

public class InGdRecSearchResultActivity extends BaseActivity implements OnClickListener {
	
	@FindView(id = R.id.ingdrecsearch_result_back, click = true)
	private ImageView imgBack=null;// 回退按钮

	@FindView(id = R.id.ingdrecsearch_itm_scroll_list, click = true)
	private ListView listview;
	
	private List<InGdRecSearch> inGdRecsearchs = new ArrayList<InGdRecSearch>();
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
		
//		InGdRecSearch gdRecSearch=new InGdRecSearch();
//		gdRecSearch.setHome("dota");
//		gdRecSearch.setNum("2015");
//		inGdRecsearchs.add(gdRecSearch);
//		inGdRecsearchAdapter = new InGdRecSearchAdapter(this, inGdRecsearchs);
//		listview.setAdapter(inGdRecsearchAdapter);

	}

	private void getResult() {
		List<NameValuePair> valuePairs=new ArrayList<NameValuePair>();
		valuePairs.add(new BasicNameValuePair("start", "2"));
		valuePairs.add(new BasicNameValuePair("end", "2"));
//		ThreadPoolUtils.execute(new HttpPostThread(handler, Constants.METHOD_SEARCH_INGDREC_ITM, valuePairs));
		ThreadPoolUtils.execute(new HttpPostThread(handler, getIpByType("scm")+Constants.METHOD_SEARCH_INGDREC_ITM, valuePairs));
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

	/**
	 * 回调句柄
	 */
	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				CommonTools.showShortToast(InGdRecSearchResultActivity.this, "请求失败，服务器故障");
			} else if (msg.what == 100) {
				CommonTools.showShortToast(InGdRecSearchResultActivity.this, "服务器无响应");
			} else if (msg.what == 200) {

				InGdRecSearch gdRec = new InGdRecSearch();
				try {
					JSONObject jsonObject = new JSONObject((String) msg.obj);
					String resultCode=jsonObject.get("resultCode").toString();
					if(resultCode.equals("0")){
						JSONArray array=jsonObject.getJSONArray("dataList");
						for (int i = 0; i < array.length(); i++) {
							JSONObject jo = (JSONObject) array.get(i);
							InGdRecSearch gdRecSearch=new InGdRecSearch();
							gdRecSearch.setHome(jo.get("name").toString());
							gdRecSearch.setNum(jo.get("name").toString());
							inGdRecsearchs.add(gdRecSearch);
						}
						inGdRecsearchAdapter.notifyDataSetChanged();
						
					}else{
						CommonTools.showShortToast(getApplication(), jsonObject.get("resultContent").toString());
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				inGdRecsearchs.add(gdRec);
			}
			inGdRecsearchAdapter.notifyDataSetChanged();
		};
	};
}
