package com.dhcc.scm.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.dhcc.scm.R;
import com.dhcc.scm.adapter.InGdRecAdapter;
import com.dhcc.scm.entity.Constants;
import com.dhcc.scm.entity.InGdRec;
import com.dhcc.scm.http.net.ThreadPoolUtils;
import com.dhcc.scm.http.thread.HttpPostThread;
import com.dhcc.scm.ui.base.BaseActivity;
import com.dhcc.scm.ui.base.FindView;
import com.dhcc.scm.utils.CommonTools;
import com.dhcc.scm.zxing.CaptureActivity;

/**
 * 
 * @ClassName: InGdRecActivity
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author zhouxin
 * @date 2015年7月30日 下午3:33:18
 * 
 */
public class InGdRecActivity extends BaseActivity implements OnClickListener {
	
	@FindView(id = R.id.ingdrec_back_btn, click = true)
	private ImageView imgBack; //回退按钮
	
	@FindView(id = R.id.ingdrec_barcode_btn, click = true)
	private Button btnScanCode;// 扫码
	
	//保存按钮
	@FindView(id = R.id.ingdrec_save_btn, click = true)
	private Button btnSave;
	
	//查询按钮
	@FindView(id = R.id.ingdrec_search_btn, click = true)
	private Button btnSearch;
	
	@FindView(id = R.id.ingdrec_barcode_txt)
	private EditText barcodeTxt;// 条码框
	
	@FindView(id = R.id.ingdrec_itm_scroll_list)
	private ListView listview;
	
	private List<InGdRec> inGdRecs=new ArrayList<InGdRec>();
	private InGdRecAdapter inGdRecAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ingdrec);
		super.onCreate(savedInstanceState);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {

		inGdRecAdapter=new InGdRecAdapter(this, inGdRecs);
		listview.setAdapter(inGdRecAdapter);
	}

	@Override
	protected void initView() {

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ingdrec_back_btn:
			finish();
			break;
		case R.id.ingdrec_barcode_btn:
			startActivityForResult(new Intent(InGdRecActivity.this, CaptureActivity.class), 0);
			break;
		case R.id.ingdrec_save_btn:
			saveIngdrec();
			break;
		case R.id.ingdrec_search_btn:
			searchBarCode();
			break;
		default:
			break;
		}
	}
	
	/**
	 * 
	* @Title: saveIngdrec 
	* @Description: TODO(保存入库单) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws 
	* @author zhouxin   
	* @date 2015年10月20日 上午11:36:18
	 */
	private void saveIngdrec() {
		if(inGdRecAdapter.getCount()>0){
			StringBuffer sb=new StringBuffer();
			for(InGdRec gdRec:inGdRecs){
				sb.append(gdRec.getScmId()+"^");
			}
			List<NameValuePair> nameValuePairs=new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("value", sb.toString()));
			ThreadPoolUtils.execute(new HttpPostThread(savehandler, getIpByType("scm")+Constants.METHOD_SAVE_BARCODE, nameValuePairs));
		}else{
			CommonTools.showShortToast(InGdRecActivity.this, "没有明细,请扫码");
			return;
		}
		
	}
	
	/**
	 * 
	* @Title: searchBarCode 
	* @Description: TODO(查询条码) 
	* @param     设定文件 
	* @return void    返回类型 
	* @throws 
	* @author zhouxin   
	* @date 2015年10月20日 上午11:36:37
	 */
	private void searchBarCode() {
		if(inGdRecAdapter.checkExist(barcodeTxt.getText().toString())){
			CommonTools.showShortToast(InGdRecActivity.this, "列表中已存在,不能重复扫码");
		}else{
			List<NameValuePair> barCodeNameValuePairs=new ArrayList<NameValuePair>();
			barCodeNameValuePairs.add(new BasicNameValuePair("value", barcodeTxt.getText().toString()));
			ThreadPoolUtils.execute(new HttpPostThread(searchhandler, getIpByType("scm")+Constants.METHOD_GET_BARCODE_INFO, barCodeNameValuePairs));
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			barcodeTxt.setText(scanResult);
			CommonTools.showShortToast(getApplicationContext(), scanResult);
		}
	}
	/**
	 * 回调句柄
	 * 111
	 * 222
	 * 333
	 * 444
    */
	Handler searchhandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				CommonTools.showShortToast(InGdRecActivity.this, "请求失败，服务器故障");
			} else if (msg.what == 100) {
				CommonTools.showShortToast(InGdRecActivity.this, "服务器无响应");
			} else if (msg.what == 200) {
				
				InGdRec gdRec = new InGdRec();
				try {
					JSONObject jsonObject=new JSONObject((String)msg.obj);	
					if(jsonObject.getString("resultCode").equals("0")){
						gdRec.setDesc(jsonObject.getString("desc"));
						gdRec.setBatno(jsonObject.getString("batno"));
						gdRec.setExpDate(jsonObject.getString("expDate"));
						gdRec.setManf(jsonObject.getString("vendor"));
						gdRec.setQty(Float.valueOf(jsonObject.getString("qty")));
						gdRec.setUom(jsonObject.getString("uom"));
						gdRec.setScmId(jsonObject.getString("scmid"));
						barcodeTxt.setText("");
					}else{
						CommonTools.showShortToast(InGdRecActivity.this, "错误:"+jsonObject.getString("resultMsg"));
						return;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				inGdRecs.add(gdRec);
			}
			inGdRecAdapter.notifyDataSetChanged();
		};
	};
	Handler savehandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				CommonTools.showShortToast(InGdRecActivity.this, "请求失败，服务器故障");
			} else if (msg.what == 100) {
				CommonTools.showShortToast(InGdRecActivity.this, "服务器无响应");
			} else if (msg.what == 200) {
				try {
					JSONObject jsonObject=new JSONObject((String)msg.obj);	
					if(jsonObject.getString("resultCode").equals("0")){
						CommonTools.showShortToast(InGdRecActivity.this, "操作成功");
						CommonTools.showShortToast(InGdRecActivity.this, jsonObject.getString("resultContent"));
						return;
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		};
	};
}
