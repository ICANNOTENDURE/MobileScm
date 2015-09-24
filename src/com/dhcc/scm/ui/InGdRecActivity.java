package com.dhcc.scm.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.dhcc.scm.R;
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
	private EditText barcodeTxt; //条码框

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
		imgBack.setOnClickListener(this);
		// 扫码
		btnScanCode = (Button) this.findViewById(R.id.ingdrec_barcode_btn);
		btnScanCode.setOnClickListener(this);
		
		barcodeTxt=(EditText)this.findViewById(R.id.ingdrec_barcode_txt);
		

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
			startActivityForResult(new Intent(InGdRecActivity.this, CaptureActivity.class),0);
			break;
		default:
			break;
		}
		
	}

	 @Override  
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {  
        super.onActivityResult(requestCode, resultCode, data);  
        //处理扫描结果（在界面上显示）  
        if (resultCode == RESULT_OK) {  
            Bundle bundle = data.getExtras();  
            String scanResult = bundle.getString("result");
            barcodeTxt.setText(scanResult);
            CommonTools.showShortToast(getApplicationContext(), scanResult);
        }  
    }  
}
