package com.dhcc.scm.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.alibaba.fastjson.JSON;
import com.dhcc.scm.R;
import com.dhcc.scm.adapter.InGdRecAdapter;
import com.dhcc.scm.entity.Constants;
import com.dhcc.scm.entity.InGdRec;
import com.dhcc.scm.entity.InGdRecResult;
import com.dhcc.scm.http.Http;
import com.dhcc.scm.http.HttpCallBack;
import com.dhcc.scm.http.HttpParams;
import com.dhcc.scm.ui.base.BaseActivity;
import com.dhcc.scm.ui.base.FindView;
import com.dhcc.scm.ui.base.ScanType;
import com.dhcc.scm.ui.base.ViewInject;
import com.dhcc.scm.utils.Loger;
import com.dhcc.scm.utils.StringUtils;
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
	ImageView imgBack; // 回退按钮
	@FindView(id = R.id.ingdrec_barcode_btn, click = true)
	Button btnScanCode;// 扫码
	@FindView(id = R.id.ingdrec_save_btn, click = true)
	Button btnSave; // 保存按钮
	@FindView(id = R.id.ingdrec_search_btn, click = true)
	Button btnSearch; // 查询按钮
	@FindView(id = R.id.ingdrec_barcode_txt)
	EditText barcodeTxt;// 条码框
	@FindView(id = R.id.ingdrec_itm_scroll_list)
	ListView listview;

	private List<InGdRec> inGdRecs = new ArrayList<InGdRec>();
	private InGdRecAdapter inGdRecAdapter = null;
	// 进度条
	private ProgressDialog progressDialog = null;
	Http http = new Http();
	private static String codeMode="";
	
	// 条码Receiver
	private BroadcastReceiver mScanDataReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			ViewInject.toast("action:" + action);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ingdrec);
		super.onCreate(savedInstanceState);
		initView();
		// 注册条码Receiver
		registerReceiver(mScanDataReceiver, ScanType.initFilter());
		if (!isServiceRunning("android.intent.action.CONTENT_NOTIFY")) {
			ViewInject.toast("未检测到扫描服务");
		}
		barcodeTxt.setText("{\"content\":2c939386513d403901513dad61b10000,\"seq\":1,\"codeType\":ByInc}");
	}

	@Override
	protected void initView() {
		inGdRecAdapter = new InGdRecAdapter(this, inGdRecs);
		listview.setAdapter(inGdRecAdapter);
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
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 * @author zhouxin
	 * @date 2015年10月20日 上午11:36:18
	 */
	private void saveIngdrec() {
		if (inGdRecAdapter.getCount() > 0) {
			progressDialog = ViewInject.getprogress(InGdRecActivity.this, Constants.PRO_WAIT_MESSAGE, false);
			StringBuffer sb = new StringBuffer();
			for (InGdRec gdRec : inGdRecs) {
				sb.append(gdRec.getScmId() + "^");
			}
			HttpParams params = new HttpParams();
			params.put("requestType", "apk");
			params.put("value", sb.toString());
			http.post(getIpByType("scm") + Constants.METHOD_SAVE_BARCODE, params, new HttpCallBack() {
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
						Loger.debug("登陆网络请求：" + new String(t));
						try {
							Log.i("dhcc", str);
							JSONObject jsonObject = new JSONObject(str);
							if (jsonObject.getString("resultCode").equals("0")) {
								ViewInject.toast("操作成功");
								return;
							}
						} catch (JSONException e) {
							e.printStackTrace();
							ViewInject.toast(e.getMessage());
						}
					}
				};
			});
		} else {
			ViewInject.toast("没有明细,请扫码");
			return;
		}

	}

	/**
	 * 
	 * @Title: searchBarCode
	 * @Description: TODO(查询条码)
	 * @param 设定文件
	 * @return void 返回类型
	 * @throws
	 * @author zhouxin
	 * @date 2015年10月20日 上午11:36:37
	 */
	private void searchBarCode() {
		String content = "2c93938651a47ca10151a4a36c420001";
		String seq = "1";
		String codeType = "ByQty"; // ByQty ByInc ByOrder
		try {
			JSONObject jsonObject = new JSONObject(barcodeTxt.getText().toString());
			content = jsonObject.getString("content");
			seq = jsonObject.getString("seq");
			codeType = jsonObject.getString("codeType");
		} catch (JSONException e1) {
		}
		if(StringUtils.isEmpty(codeMode)){
			codeMode=codeType;
		}
		if(!codeMode.equals(codeType)){
			ViewInject.toast("一次入库只能扫描同一种类型条码");
			return;
		}
		if (StringUtils.isEmpty(content) || StringUtils.isEmpty(codeType)) {
			ViewInject.toast("条码错误");
			return;
		}
		if (codeType.equals("ByInc") && inGdRecAdapter.checkExist(content)) {
			ViewInject.toast("列表中已存在,不能重复扫码");
			return;
		}
		if (codeType.equals("ByQty") && inGdRecAdapter.checkExistByQty(content)) {
			ViewInject.toast("列表中已存在,不能重复扫码");
			return;
		}
		HttpParams params = new HttpParams();
		params.put("requestType", "apk");
		params.put("content", content);
		params.put("seq", seq);
		params.put("codeType", codeType);
		http.post(getIpByType("scm") + Constants.METHOD_GET_BARCODE_INFO, params, new HttpCallBack() {
			@Override
			public void onFailure(int errorNo, String strMsg) {
				super.onFailure(errorNo, strMsg);
				ViewInject.dismiss(progressDialog);
				ViewInject.toast("网络不好," + strMsg);
			}

			@Override
			public void onSuccess(java.util.Map<String, String> headers, byte[] t) {
				ViewInject.dismiss(progressDialog);
				if (t != null) {
					String str = new String(t);
					Loger.debug("登陆网络请求：" + str);
					try {
						InGdRecResult inGdRecResult = JSON.parseObject(str, InGdRecResult.class);
						if (inGdRecResult.getResultCode().equals("0")) {
							if (inGdRecResult.getCodeType().equals("ByInc")) {
								if (inGdRecResult.getGdRecItms().size() == 1) {
									inGdRecs.add(inGdRecResult.getGdRecItms().get(0));
								}
							}
							if (inGdRecResult.getCodeType().equals("ByQty")) {
								if (inGdRecResult.getGdRecItms().size() == 1) {
									Loger.debug("inGdRecAdapter.checkExistByQty(inGdRecResult.getGdRecItms().get(0).getLabelId()):"+inGdRecAdapter.checkExistByQty(inGdRecResult.getGdRecItms().get(0).getLabelId()));
									if(inGdRecAdapter.checkExist(inGdRecResult.getGdRecItms().get(0).getScmId())){
										//inGdRecAdapter.updateQty(inGdRecResult.getGdRecItms().get(0).getScmId(),inGdRecResult.getGdRecItms().get(0).getLabelId());
										for(InGdRec gdRec:inGdRecs){
											if(gdRec.getScmId().equals(inGdRecResult.getGdRecItms().get(0).getScmId())){
												gdRec.setQty(gdRec.getQty()+1);
												inGdRecAdapter.updateQty(inGdRecResult.getGdRecItms().get(0).getLabelId());
											}
										}
									}else{
										inGdRecs.add(inGdRecResult.getGdRecItms().get(0));
									}
								}
							}
							if (inGdRecResult.getCodeType().equals("ByOrder")) {
								inGdRecs.addAll(inGdRecResult.getGdRecItms());
							}
							inGdRecAdapter.notifyDataSetChanged();
						} else {
							ViewInject.toast(inGdRecResult.getResultComtent());
						}
					} catch (Exception e) {
						e.printStackTrace();
						ViewInject.toast(e.getMessage());
					}
				}
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		// 处理扫描结果（在界面上显示）
		if (resultCode == RESULT_OK) {
			Bundle bundle = data.getExtras();
			String scanResult = bundle.getString("result");
			barcodeTxt.setText(scanResult);
			ViewInject.toast(scanResult);
		}
	}

	@Override
	protected void onDestroy() {
		if (mScanDataReceiver != null) {
			unregisterReceiver(mScanDataReceiver);
		}
		super.onDestroy();
	}

	// 检查Service是否运行
	private boolean isServiceRunning(String className) {
		boolean isRunning = false;
		ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		List<ActivityManager.RunningServiceInfo> serviceList = activityManager.getRunningServices(100);

		if (!(serviceList.size() > 0)) {
			return false;
		}

		for (int i = 0; i < serviceList.size(); i++) {
			if (serviceList.get(i).service.getClassName().equals(className) == true) {
				isRunning = true;
				break;
			}
		}
		return isRunning;
	}
}
