package com.dhcc.scm.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.dhcc.scm.config.Constants;
import com.dhcc.scm.entity.InGdRec;
import com.dhcc.scm.http.net.ThreadPoolUtils;
import com.dhcc.scm.http.thread.HttpPostThread;
import com.dhcc.scm.ui.base.BaseActivity;
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

	private ImageView imgBack;// 回退按钮
	private Button btnScanCode;// 扫码
	private Button btnSave;
	private EditText barcodeTxt; // 条码框
	private ListView listview;
	private List<InGdRec> inGdRecs=new ArrayList<InGdRec>();
	private InGdRecAdapter inGdRecAdapter = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_ingdrec);
		findViewById();
		initView();
	}

	@Override
	protected void findViewById() {
		imgBack = (ImageView) findViewById(R.id.ingdrec_back_btn);
		btnScanCode = (Button) this.findViewById(R.id.ingdrec_barcode_btn);
		barcodeTxt = (EditText) this.findViewById(R.id.ingdrec_barcode_txt);
		btnSave=(Button) this.findViewById(R.id.ingdrec_save_btn);
		listview = (ListView) this.findViewById(R.id.ingdrec_itm_scroll_list);
		inGdRecAdapter=new InGdRecAdapter(this, inGdRecs);
		listview.setAdapter(inGdRecAdapter);
	}

	@Override
	protected void initView() {
		imgBack.setOnClickListener(this);
		btnScanCode.setOnClickListener(this);
		btnSave.setOnClickListener(this);
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
			ThreadPoolUtils.execute(new HttpPostThread(handler, Constants.METHOD_GET_BARCODE_INFO, barcodeTxt.getText().toString()));
			break;
		default:
			break;
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

	Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			if (msg.what == 404) {
				CommonTools.showShortToast(InGdRecActivity.this, "请求失败，服务器故障");
			} else if (msg.what == 100) {
				CommonTools.showShortToast(InGdRecActivity.this, "服务器无响应");
			} else if (msg.what == 200) {
				String result = (String) msg.obj;
				InGdRec gdRec = new InGdRec();
				inGdRecs.add(gdRec);
			}
			InGdRec gdRec = new InGdRec();
			gdRec.setBatno("20111");
			gdRec.setDesc("2222222222222");
			gdRec.setExpDate(new Date());
			gdRec.setQty(2);
			gdRec.setUom("瓶");
			inGdRecs.add(gdRec);
			
			inGdRecAdapter.notifyDataSetChanged();
		};
	};
}
